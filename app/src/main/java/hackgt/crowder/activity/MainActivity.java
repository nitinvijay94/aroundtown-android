package hackgt.crowder.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.example.hackgt.R;

import java.util.ArrayList;

import hackgt.crowder.fragment.EventViewerFragment;
import hackgt.crowder.fragment.MainMapFragment;
import hackgt.crowder.model.Event;

public class MainActivity extends AppCompatActivity {

    private boolean isListShowing;
    private EventViewerFragment eventViewerFragment;
    private MainMapFragment mapFragment;

    private ArrayList<Event> events;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        initialize();
    }

    private void initialize() {
        events = new ArrayList<>();
        events.add(new Event(33.776578, -84.395960, "Party", 100));
        events.add(new Event(33.776570, -84.395970, "Frat", 50));
        events.add(new Event(33.776580, -84.395968, "Yolo", 20));
        mapFragment = MainMapFragment.newInstance();
        eventViewerFragment = EventViewerFragment.newInstance();
        mapFragment.setEvents(events);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.container, eventViewerFragment);
        fragmentTransaction.commit();
        isListShowing = true;
    }

    @Override
    public void onBackPressed() {
        if (eventViewerFragment != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container, eventViewerFragment);
            fragmentTransaction.commit();
            isListShowing = true;
            invalidateOptionsMenu();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
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
        } else {
            search.collapseActionView();
            search.setVisible(false);
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
}
