package hackgt.crowder.fragment;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.hackgt.R;

import java.util.ArrayList;
import java.util.List;

import hackgt.crowder.adapter.EventAdapter;
import hackgt.crowder.model.Event;

public class EventViewerFragment extends Fragment {
    private EventViewerInterface eventViewerInterface;

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private List<Event> events = new ArrayList<>();


    public static EventViewerFragment newInstance() {
        EventViewerFragment fragment = new EventViewerFragment();
        return fragment;
    }

    public EventViewerFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        events.add(new Event(33.776578, -84.395960, "Party", 100));
        events.add(new Event(33.776570, -84.395970, "Frat", 50));
        events.add(new Event(33.776580, -84.395968, "Yolo", 10));

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_event_viewer, container, false);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.my_recycler_view);
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        // specify an adapter (see also next example
        mAdapter = new EventAdapter(events, getActivity());
        mRecyclerView.setAdapter(mAdapter);

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

}
