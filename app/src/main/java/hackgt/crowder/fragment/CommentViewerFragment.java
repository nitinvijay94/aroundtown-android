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

import hackgt.crowder.adapter.CommentAdapter;
import hackgt.crowder.model.Comment;

public class CommentViewerFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private CommentAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private List<Comment> comments = new ArrayList<>();


    public static CommentViewerFragment newInstance() {
        CommentViewerFragment fragment = new CommentViewerFragment();
        return fragment;
    }

    public CommentViewerFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (comments.size() == 0) {
            comments.add(new Comment("Hello this is a comment 1", "2 hours ago"));
            comments.add(new Comment("Hello this is another comment 2", "3 hours ago"));
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_comment_viewer, container, false);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.comment_list);
        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        mAdapter = new CommentAdapter(comments, getActivity());
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
    }

    public interface EventViewerInterface {
        public void onFragmentInteraction(Uri uri);
    }


}
