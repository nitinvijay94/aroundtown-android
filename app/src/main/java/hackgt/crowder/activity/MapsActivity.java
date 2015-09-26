package hackgt.crowder.activity;

import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.example.hackgt.R;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

import hackgt.crowder.model.Event;

public class MapsActivity extends FragmentActivity {

    private GoogleMap mMap;

    private ArrayList<Event> events;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        events = new ArrayList<>();
        events.add(new Event(33.776578, -84.395960, "Party"));
        events.add(new Event(33.776570, -84.395970, "Frat"));
        events.add(new Event(33.776580, -84.395968, "Yolo"));
        setUpMapIfNeeded();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }


    private void setUpMapIfNeeded() {
        if (mMap == null) {
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            if (mMap != null) {
                setUpMap();
            }
        }
    }

    private void setUpMap() {
        mMap.setMyLocationEnabled(true);
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        String provider = locationManager.getBestProvider(criteria, true);
        Location location;
        try {
            location = locationManager.getLastKnownLocation(provider);
        } catch (SecurityException e) {
            location = new Location("origin");
            location.setLongitude(0);
            location.setLatitude(0);
        }
        LatLng currentPosition = new LatLng(location.getLatitude(), location.getLongitude());
        CameraUpdate center = CameraUpdateFactory.newLatLng(currentPosition);
        CameraUpdate zoom = CameraUpdateFactory.zoomTo(17);
        mMap.moveCamera(center);
        mMap.animateCamera(zoom);
        for (Event event : events) {
            mMap.addMarker(new MarkerOptions().position(new LatLng(event.getLocation()
                    .getLatitude(), event.getLocation().getLongitude())).title(event.getTitle()));
        }
    }
}
