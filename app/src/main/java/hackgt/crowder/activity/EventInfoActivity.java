package hackgt.crowder.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hackgt.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

import hackgt.crowder.Constants;
import hackgt.crowder.fragment.AddCommentDiaglogFragment;
import hackgt.crowder.fragment.AddEventDialogFragment;
import hackgt.crowder.model.Event;

public class EventInfoActivity extends AppCompatActivity implements AddCommentDiaglogFragment.AddCommentInterface {

    private ProgressDialog progressDialog;
    private ProgressDialog commentProgressDialog;
    private int eventID;

    LinearLayout commentsView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_info);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        initialize();
    }

    private void initialize() {
        eventID = getIntent().getIntExtra(Constants.ID, -1);
        commentsView = ((LinearLayout) findViewById(R.id.comments));
        ((Button) findViewById(R.id.like)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView score = (TextView) findViewById(R.id.score);
                int scoreInt = Integer.parseInt(score.getText().toString().trim());
                score.setText((scoreInt + 1) + "");
            }
        });
        ((TextView) findViewById(R.id.add_comment)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddCommentDiaglogFragment fragment = new AddCommentDiaglogFragment();
                fragment.show(getSupportFragmentManager(), "comment");
            }
        });
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(getResources().getString(R.string.load_event));
        commentProgressDialog = new ProgressDialog(this);
        commentProgressDialog.setMessage("Adding comment");

        progressDialog.show();

        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            progressDialog.show();
            new GetEventTask().execute(eventID);
        } else {
            Toast.makeText(this, R.string.no_connection, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void addComment(String title, String comment) {
        commentProgressDialog.show();
        new AddCommentTask().execute(title, comment);
    }

    private class AddCommentTask extends AsyncTask<String, Void, Boolean> {
        private String title;
        private String comment;

        @Override
        protected Boolean doInBackground(String... params) {
            title = params[0];
            comment = params[1];
            try {
                String urlString = Constants.COMMENT_URL;
                URL url = new URL(urlString);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(10000);
                conn.setConnectTimeout(15000);
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.setDoOutput(true);
                HashMap<String, String> postDataParams = new HashMap<>();
                postDataParams.put("title", title);
                postDataParams.put("comment", comment);
                postDataParams.put("event", eventID + "");

                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, "UTF-8"));
                writer.write(getPostDataString(postDataParams));

                writer.flush();
                writer.close();
                os.close();

                conn.connect();
                int response = conn.getResponseCode();
                if (response == 200) {
                    return true;
                } else {
                    Log.e("comment error", response + "");
                    return false;
                }
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }

        @Override
        protected void onPostExecute(Boolean success) {
            commentProgressDialog.dismiss();
            if (success) {
                CardView card = new CardView(EventInfoActivity.this);
                card.setCardBackgroundColor(0xFFFFFFFF);
                card.setRadius(16);
                ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                LinearLayout tempLinear = new LinearLayout(EventInfoActivity.this);
                tempLinear.setLayoutParams(params);
                tempLinear.setOrientation(LinearLayout.VERTICAL);
                TextView temp0 = new TextView(EventInfoActivity.this);
                temp0.setText(title);
                temp0.setTypeface(null, Typeface.BOLD);
                temp0.setTextSize(15);
                temp0.setPadding(15, 7, 7, 7);
                TextView temp1 = new TextView(EventInfoActivity.this);
                temp1.setText(comment);
                temp1.setTextSize(15);
                temp1.setPadding(25, 7, 7, 7);
                tempLinear.addView(temp0);
                tempLinear.addView(temp1);
                card.addView(tempLinear);
                commentsView.addView(card);
                View view = new View(EventInfoActivity.this);
                view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 10));
                commentsView.addView(view, 0);
            } else {
                Toast.makeText(EventInfoActivity.this, "Fail to add comment", Toast.LENGTH_SHORT).show();
            }
        }

        private String getPostDataString(HashMap<String, String> params) throws UnsupportedEncodingException {
            StringBuilder result = new StringBuilder();
            boolean first = true;
            for (Map.Entry<String, String> entry : params.entrySet()) {
                if (first)
                    first = false;
                else
                    result.append("&");

                result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
                result.append("=");
                result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
            }

            return result.toString();
        }
    }

    private class GetEventTask extends AsyncTask<Integer, Void, Event> {

        @Override
        protected Event doInBackground(Integer... params) {
            try {
                String json = getEventJson(params[0]);
                JSONObject jsonEvent = new JSONObject(json);
                Event temp = new Event();
                temp.setId(jsonEvent.getInt(Constants.ID));
                temp.setTitle(jsonEvent.getString(Constants.TITLE));
                DateFormat tempFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:sss");
                Date start = tempFormat.parse(jsonEvent.getString(Constants.START));
                temp.setStartDate(start.toString());
                Date end = tempFormat.parse(jsonEvent.getString(Constants.END));
                temp.setEndDate(end.toString());
                temp.setScore(jsonEvent.getInt(Constants.SCORE));
                temp.setAddress(jsonEvent.getString(Constants.ADDRESS));
                temp.setDescription(jsonEvent.getString(Constants.DESCRIPTION));
                temp.setLatitude(jsonEvent.getDouble(Constants.LATITUDE));
                temp.setLongitude(jsonEvent.getDouble(Constants.LONGITUDE));
                ArrayList<String> comments = new ArrayList<>();
                JSONArray commentsJson = jsonEvent.getJSONArray(Constants.COMMENT);
                for (int j = 0; j < commentsJson.length(); j++) {
                    comments.add(commentsJson.getString(j));
                }
                temp.setComments(comments);
                return temp;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(Event event) {
            if (event != null) {
                ((TextView) findViewById(R.id.title)).setText(event.getTitle());
                ((TextView) findViewById(R.id.score)).setText(event.getScore() + "");
                ((TextView) findViewById(R.id.location)).setText(event.getAddress());
                ((TextView) findViewById(R.id.start)).setText(event.getStartDate());
                ((TextView) findViewById(R.id.end)).setText(event.getEndDate());
                ((TextView) findViewById(R.id.description)).setText(event.getDescription());
                if (event.getComments().size() != 0) {
                    for (String comment : event.getComments()) {
                        CardView card = new CardView(EventInfoActivity.this);
                        card.setCardBackgroundColor(0xFFFFFFFF);
                        card.setRadius(16);
                        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                        LinearLayout tempLinear = new LinearLayout(EventInfoActivity.this);
                        tempLinear.setLayoutParams(params);
                        tempLinear.setOrientation(LinearLayout.VERTICAL);
                        TextView temp0 = new TextView(EventInfoActivity.this);
                        int colonIndex = comment.indexOf(':');
                        temp0.setText(comment.substring(0, colonIndex));
                        temp0.setTypeface(null, Typeface.BOLD);
                        temp0.setTextSize(15);
                        temp0.setPadding(15, 7, 7, 7);
                        TextView temp1 = new TextView(EventInfoActivity.this);
                        temp1.setText(comment.substring(colonIndex + 1));
                        temp1.setTextSize(15);
                        temp1.setPadding(25, 7, 7, 7);
                        tempLinear.addView(temp0);
                        tempLinear.addView(temp1);
                        card.addView(tempLinear);
                        commentsView.addView(card);
                        View view = new View(EventInfoActivity.this);
                        view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 10));
                        commentsView.addView(view);
                    }
                } else {
                    TextView temp = new TextView(EventInfoActivity.this);
                    temp.setText(getResources().getString(R.string.no_comment));
                    temp.setTextSize(15);
                    temp.setPadding(5, 10, 0, 0);
                    commentsView.addView(temp);
                }
            } else {
                Toast.makeText(EventInfoActivity.this, getResources().getString(R.string.connection_error), Toast.LENGTH_SHORT).show();
            }
            if (progressDialog != null && progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
        }

        private String getEventJson(int id) throws IOException {
            InputStream is = null;
            try {
                String urlString = Constants.EVENT_URL + id + Constants.JSON_FORMAT;
                URL url = new URL(urlString);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(10000 /* milliseconds */);
                conn.setConnectTimeout(15000 /* milliseconds */);
                conn.setRequestMethod("GET");
                conn.setDoInput(true);
                // Starts the query
                conn.connect();
                int response = conn.getResponseCode();
                is = conn.getInputStream();
                StringBuilder builder = new StringBuilder();
                BufferedReader reader = new BufferedReader(new InputStreamReader(is));
                String line;
                while ((line = reader.readLine()) != null) {
                    builder.append(line);
                }
                return builder.toString();
            } catch (Exception e) {
                return null;
            } finally {
                if (is != null) {
                    is.close();
                }
            }
        }
    }

}
