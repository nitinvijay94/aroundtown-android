package hackgt.crowder.fragment;

import android.app.Activity;
import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.hackgt.R;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.HashMap;

import hackgt.crowder.model.Event;

public class MainMapFragment extends Fragment {

    private static View view;
    private MainMapInterAction mListener;
    private GoogleMap map;
    private ArrayList<Event> events;
    private HashMap<Marker, Event> eventMap;

    public static MainMapFragment newInstance() {
        MainMapFragment fragment = new MainMapFragment();
        return fragment;
    }

    public MainMapFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (eventMap == null) {
            eventMap = new HashMap<>();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (view != null) {
            ViewGroup parent = (ViewGroup) view.getParent();
            if (parent != null) {
                parent.removeView(view);
            }
        }
        view = inflater.inflate(R.layout.fragment_main_map, container, false);
        SupportMapFragment fragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        fragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                map = googleMap;
                googleMap.setMyLocationEnabled(true);
                LocationManager locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
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
                googleMap.moveCamera(center);
                googleMap.animateCamera(zoom);
                if (events != null) {
                    for (Event event : events) {
                        Marker marker = googleMap.addMarker(new MarkerOptions().position(new LatLng(event.getLocation()
                                .getLatitude(), event.getLocation().getLongitude())).title(event.getTitle()));
                        eventMap.put(marker, event);
                    }
                }
                googleMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                    @Override
                    public void onInfoWindowClick(Marker marker) {

                    }
                });
            }
        });
        return view;
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
//        mListener = (MainMapInterAction) activity;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface MainMapInterAction {
    }

    public ArrayList<Event> getEvents() {
        return events;
    }

    public void setEvents(ArrayList<Event> events) {
        this.events = events;
        if (map != null) {
            map.clear();
            eventMap.clear();
            for (Event event : events) {
                Marker marker = map.addMarker(new MarkerOptions().position(new LatLng(event.getLocation()
                        .getLatitude(), event.getLocation().getLongitude())).title(event.getTitle()));
                eventMap.put(marker, event);
            }
        }
    }
}
