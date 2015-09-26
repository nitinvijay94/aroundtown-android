package hackgt.crowder.activity;

import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.example.hackgt.R;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

import hackgt.crowder.fragment.EventViewerFragment;
import hackgt.crowder.fragment.MainMapFragment;
import hackgt.crowder.model.Event;

public class MainActivity extends FragmentActivity {

    private EventViewerFragment evfragment;
    private MainMapFragment mapFragment;

    private ArrayList<Event> events;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        events = new ArrayList<>();
        events.add(new Event(33.776578, -84.395960, "Party", 100));
        events.add(new Event(33.776570, -84.395970, "Frat", 50));
        events.add(new Event(33.776580, -84.395968, "Yolo", 20));
        if (mapFragment == null) {
            mapFragment = MainMapFragment.newInstance();
        }

        evfragment = EventViewerFragment.newInstance();
        mapFragment.setEvents(events);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.container, evfragment);
        fragmentTransaction.commit();

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

}
