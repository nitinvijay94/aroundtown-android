package hackgt.crowder.fragment;

import android.app.Activity;
import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.hackgt.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import hackgt.crowder.adapter.EventAdapter;
import hackgt.crowder.model.Event;

public class EventViewerFragment extends Fragment {
    private EventViewerInterface eventViewerInterface;

    private RecyclerView mRecyclerView;
    private EventAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private List<Event> events = new ArrayList<>();
    private boolean sortByPop = false;
    private Button sortPop;
    private Button sortDistance;


    public static EventViewerFragment newInstance() {
        EventViewerFragment fragment = new EventViewerFragment();
        return fragment;
    }

    public EventViewerFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (events.size() == 0) {
            events.add(new Event(33.776578, -84.395960, "Party", 100));
            events.add(new Event(33.776570, -84.395970, "Frat", 50));
            events.add(new Event(33.776580, -84.395968, "Yolo", 10));
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_event_viewer, container, false);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.comment_list);
        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        mAdapter = new EventAdapter(events, getActivity());
        mRecyclerView.setAdapter(mAdapter);

        sortDistance = (Button) view.findViewById(R.id.distance);
        sortDistance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sortByPop = false;
                toggle();
                LocationManager locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
                Criteria criteria = new Criteria();
                String provider = locationManager.getBestProvider(criteria, true);
                final Location current;
                if(provider!=null) {
                    current = locationManager.getLastKnownLocation(provider);

                    Collections.sort(events, new Comparator<Event>() {
                        @Override
                        public int compare(Event event0, Event event1) {
                            if (current != null) {
                                double distance0 = event0.getLocation().distanceTo(current);
                                double distance1 = event1.getLocation().distanceTo(current);
                                return Double.compare(distance0, distance1);
                            }
                            return 0;
                        }
                    });
                    mAdapter.notifyDataSetChanged();
                }
            }
        });
        sortPop = (Button) view.findViewById(R.id.popular);
        sortPop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sortByPop = true;
                toggle();
                Collections.sort(events, new Comparator<Event>() {
                    @Override
                    public int compare(Event event0, Event event1) {
                        return event1.getScore() - event0.getScore();
                    }
                });
                mAdapter.notifyDataSetChanged();
            }
        });
        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
//        eventViewerInterface = (EventViewerInterface) activity;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        eventViewerInterface = null;
    }

    public interface EventViewerInterface {
        public void onFragmentInteraction(Uri uri);
    }

    public void filter(CharSequence s) {
        mAdapter.getFilter().filter(s);
    }

    public void toggle() {
//        Button sortPop = (Button) view.findViewById(R.id.popular);
//        Button sortDistance = (Button) view.findViewById(R.id.distance);
        if (sortByPop) {
            sortPop.setBackgroundColor(0xFF982929);
            sortDistance.setBackgroundColor(0xFFD76C56);
        } else {
            sortPop.setBackgroundColor(0xFFD76C56);
            sortDistance.setBackgroundColor(0xFF982929);
        }
    }

}
