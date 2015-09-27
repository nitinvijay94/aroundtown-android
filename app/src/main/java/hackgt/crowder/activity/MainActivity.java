package hackgt.crowder.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
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

import hackgt.crowder.Constants;
import hackgt.crowder.fragment.AddEventDialogFragment;
import hackgt.crowder.fragment.EventViewerFragment;
import hackgt.crowder.fragment.EventViewerFragment.EventViewerInterface;
import hackgt.crowder.fragment.MainMapFragment;
import hackgt.crowder.model.Event;

public class MainActivity extends AppCompatActivity implements MainMapFragment.MapAddEventInterface, EventViewerInterface, AddEventDialogFragment.AddEventInterface {

    private boolean isListShowing;
    private EventViewerFragment eventViewerFragment;
    private MainMapFragment mapFragment;
    private ProgressDialog progressDialog;
    private ProgressDialog addEventProgressDialog;
    boolean doubleBackToExitPressedOnce = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        initialize();
    }

    private void initialize() {
        progressDialog = new ProgressDialog(this);
        addEventProgressDialog = new ProgressDialog(this);
        addEventProgressDialog.setMessage("Adding Event....");
        ArrayList<Event> events = new ArrayList<>();
        mapFragment = MainMapFragment.newInstance();
        eventViewerFragment = EventViewerFragment.newInstance();
        eventViewerFragment.setEvents(events, this);
        mapFragment.setEvents(events);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.container, eventViewerFragment);
        fragmentTransaction.commit();
        isListShowing = true;
        refresh();

    }

    @Override
    public void onBackPressed() {
        if (eventViewerFragment != null && !isListShowing) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container, eventViewerFragment);
            fragmentTransaction.commit();
            isListShowing = true;
            invalidateOptionsMenu();
        } else {
            if (doubleBackToExitPressedOnce) {
                super.onBackPressed();
                return;
            }
            Toast.makeText(this, getResources().getString(R.string.quit), Toast.LENGTH_SHORT).show();
            this.doubleBackToExitPressedOnce = true;
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    doubleBackToExitPressedOnce = false;
                }
            }, 2000);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        final MenuItem searchItem = menu.findItem(R.id.search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                eventViewerFragment.filter(s);
                return true;
            }
        });
        searchView.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    searchItem.collapseActionView();
                }
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem search = menu.findItem(R.id.search);
        if (isListShowing) {
            search.setVisible(true);
            menu.findItem(R.id.toggle).setIcon(android.R.drawable.ic_menu_mapmode);
        } else {
            search.collapseActionView();
            search.setVisible(false);
            menu.findItem(R.id.toggle).setIcon(android.R.drawable.ic_menu_info_details);
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.toggle) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            if (isListShowing) {
                transaction.replace(R.id.container, mapFragment);
                item.setIcon(android.R.drawable.ic_menu_preferences);
                isListShowing = false;
                invalidateOptionsMenu();
            } else {
                transaction.replace(R.id.container, eventViewerFragment);
                item.setIcon(android.R.drawable.ic_menu_mapmode);
                isListShowing = true;
                invalidateOptionsMenu();
            }
            transaction.commit();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void addEventFromMap() {
        popAddDialog();
    }

    @Override
    public void showAddEventDialog() {
        popAddDialog();
    }

    private void popAddDialog() {
        AddEventDialogFragment dialog = new AddEventDialogFragment();
        dialog.show(getSupportFragmentManager(), getString(R.string.add));
    }

    @Override
    public void refresh() {
        progressDialog.setMessage(getResources().getString(R.string.load_events));
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            progressDialog.show();
            new GetEventsTask().execute();
        } else {
            Toast.makeText(this, R.string.no_connection, Toast.LENGTH_SHORT).show();
        }
    }

    public static String getFormattedDate(String start) {
        Date date = new Date();
        try {
            date = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyy").parse(start);
        } catch (Exception e) {
            return date.toString();
        }
        return new SimpleDateFormat("h:mm a EEE, MMM d, yyyy").format(date);
    }

    @Override
    public void addEvent(String name, String description, String address, String start, String end, String tag0, String tag1, String tag2) {
        addEventProgressDialog.show();
        new AddEventTask().execute(name, description, address, start, end, tag0, tag1, tag2);
    }


    private class GetEventsTask extends AsyncTask<Void, Void, ArrayList<Event>> {

        @Override
        protected ArrayList<Event> doInBackground(Void... params) {
            try {
                String jsonEvents = getEventJson();
                ArrayList<Event> events = new ArrayList<>();
                JSONArray jsonArray = new JSONArray(jsonEvents);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonEvent = jsonArray.getJSONObject(i);
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
                    try {
                        temp.setLatitude(jsonEvent.getDouble(Constants.LATITUDE));
                        temp.setLongitude(jsonEvent.getDouble(Constants.LONGITUDE));
                    } catch (Exception e) {

                    }
                    ArrayList<String> tempTags = new ArrayList<>();
                    JSONArray tagsJson = jsonEvent.getJSONArray(Constants.TAG);
                    for (int j = 0; j < tagsJson.length(); j++) {
                        tempTags.add(tagsJson.getString(j).toLowerCase());
                    }
                    temp.setTags(tempTags);
                    events.add(temp);
                }
                return events;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(ArrayList<Event> events) {
            if (events != null) {
                if (mapFragment != null) {
                    mapFragment.setEvents(events);
                }
                if (eventViewerFragment != null) {
                    eventViewerFragment.setEvents(events, MainActivity.this);
                }
            } else {
                Toast.makeText(MainActivity.this, getResources().getString(R.string.connection_error), Toast.LENGTH_SHORT).show();
            }
            if (progressDialog != null && progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
        }

        private String getEventJson() throws IOException {
            InputStream is = null;
            try {
                URL url = new URL(Constants.EVENTS_URL);
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

    private class AddEventTask extends AsyncTask<String, Void, Boolean> {
        String tag0;
        String tag1;
        String tag2;

        @Override
        protected Boolean doInBackground(String... params) {
            try {
                String urlString = Constants.ADD_EVENT_URL;
                URL url = new URL(urlString);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(10000);
                conn.setConnectTimeout(15000);
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.setDoOutput(true);
                HashMap<String, String> postDataParams = new HashMap<>();
                postDataParams.put("title", params[0]);
                postDataParams.put("description", params[1]);
                postDataParams.put("location", params[2]);
                postDataParams.put("start", params[3]);
                postDataParams.put("end", params[4]);
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
                    String responseJSON = conn.getResponseMessage();
                    return true;
                } else {
                    Log.e("Add event error", response + "");
                    return false;
                }
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }

        @Override
        protected void onPostExecute(Boolean success) {
            addEventProgressDialog.dismiss();
            if (success) {
                refresh();
            } else {
                Toast.makeText(MainActivity.this, "Fail to add event", Toast.LENGTH_SHORT).show();
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


}
