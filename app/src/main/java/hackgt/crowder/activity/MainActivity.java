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
    protected void onResume() {
        super.onResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        MenuItem searchItem = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.toggle) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            if (isListShowing) {
                transaction.replace(R.id.container, mapFragment);
                item.setIcon(android.R.drawable.ic_dialog_info);
                isListShowing = false;
            } else {
                transaction.replace(R.id.container, eventViewerFragment);
                item.setIcon(android.R.drawable.ic_menu_mapmode);
                isListShowing = true;
            }
            transaction.commit();
        }
        return super.onOptionsItemSelected(item);
    }
}
