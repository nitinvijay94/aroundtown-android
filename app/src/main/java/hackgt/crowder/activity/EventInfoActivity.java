package hackgt.crowder.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hackgt.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import hackgt.crowder.Constants;
import hackgt.crowder.model.Event;

public class EventInfoActivity extends AppCompatActivity {

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_info);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        initialize();
    }

    private void initialize() {
        ((Button) findViewById(R.id.like)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView score = (TextView) findViewById(R.id.score);
                int scoreInt = Integer.parseInt(score.getText().toString().trim());
                score.setText((scoreInt + 1) + "");
            }
        });
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(getResources().getString(R.string.load_event));
        progressDialog.show();
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            progressDialog.show();
            new GetEventTask().execute(getIntent().getIntExtra(Constants.ID, -1));
        } else {
            Toast.makeText(this, R.string.no_connection, Toast.LENGTH_SHORT).show();
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
                ((TextView) findViewById(R.id.start)).setText(MainActivity.getFormattedDate(event.getStartDate()));
                ((TextView) findViewById(R.id.end)).setText(MainActivity.getFormattedDate(event.getEndDate()));
                ((TextView) findViewById(R.id.description)).setText(event.getDescription());
                LinearLayout commentsView = ((LinearLayout) findViewById(R.id.comments));
                //TODO
                if (event.getComments().size() != 0) {
                    for (String comment : event.getComments()) {
                        CardView card = new CardView(EventInfoActivity.this);
                        card.setCardElevation(4);
                        card.setRadius(10);
                        card.setCardBackgroundColor(0xFFFFFFFF);
                        card.setContentPadding(10, 10, 10, 10);
                        TextView temp = new TextView(EventInfoActivity.this);
                        temp.setText(comment);
                        temp.setTextSize(15);
                        temp.setPadding(5, 7, 7, 7);
                        card.addView(temp);
                        commentsView.addView(card);
                    }
                }
                else {
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
