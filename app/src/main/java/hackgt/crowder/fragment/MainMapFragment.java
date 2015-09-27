package hackgt.crowder.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

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

import hackgt.crowder.activity.EventInfoActivity;
import hackgt.crowder.model.Event;

public class MainMapFragment extends Fragment {

    private static View view;
    private MapAddEventInterface addEventInterface;
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
        FloatingActionButton button = ((FloatingActionButton) view.findViewById(R.id.add_event_button));
        button.setBackgroundTintList(getResources().getColorStateList(android.R.color.holo_red_light));
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addEventInterface.addEventFromMap();
            }
        });
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
                        Marker marker = googleMap.addMarker(new MarkerOptions().position(new LatLng(event
                                .getLatitude(), event.getLongitude())));
                        eventMap.put(marker, event);
                    }
                }
                googleMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                    @Override
                    public void onInfoWindowClick(Marker marker) {
                        Intent intent = new Intent(getActivity(), EventInfoActivity.class);
                        getActivity().startActivity(intent);
                    }
                });

                map.setInfoWindowAdapter(new EventMapAdapter());
            }
        });
        return view;
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        addEventInterface = (MapAddEventInterface) activity;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        addEventInterface = null;
    }

    public interface MapAddEventInterface {
        public void addEventFromMap();
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
                Marker marker = map.addMarker(new MarkerOptions().position(new LatLng(event
                        .getLatitude(), event.getLongitude())));
                eventMap.put(marker, event);
            }
        }
    }

    private class EventMapAdapter implements GoogleMap.InfoWindowAdapter {

        @Override
        public View getInfoWindow(Marker marker) {
            return null;
        }

        @Override
        public View getInfoContents(Marker marker) {
            View view = LayoutInflater.from(getActivity()).inflate(R.layout.event_window_layout, null, false);
            Event temp = eventMap.get(marker);
            ((TextView) view.findViewById(R.id.event_title)).setText(temp.getTitle());
            ((TextView) view.findViewById(R.id.location)).setText(temp.getAddress());
            ((TextView) view.findViewById(R.id.score)).setText(temp.getScore() + "");
            ((TextView) view.findViewById(R.id.start)).setText(temp.getStartDate());
            return view;
        }
    }
}
