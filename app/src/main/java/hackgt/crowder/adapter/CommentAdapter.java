package hackgt.crowder.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;
import android.view.View;
import android.view.ViewGroup;
import android.view.LayoutInflater;

import com.example.hackgt.R;

import hackgt.crowder.activity.MainActivity;
import hackgt.crowder.model.Comment;

import java.util.ArrayList;
import java.util.List;


public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ViewHolder> {
    private List<Comment> comments;
    private Context context;


    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView comment;
        public TextView timeStamp;
        public View view;

        public ViewHolder(View v) {
            super(v);
            view = v;
            comment = (TextView) itemView.findViewById(R.id.comment);
            timeStamp = (TextView) itemView.findViewById(R.id.timeStamp);
        }
    }

    public CommentAdapter(List<Comment> myDataset, Context context) {
        comments = myDataset;
        this.context = context;
    }

    @Override
    public CommentAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                        int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.comment_list_item, parent, false);

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(CommentAdapter.ViewHolder holder, int position) {

        holder.comment.setText(comments.get(position).content);
        holder.timeStamp.setText(comments.get(position).timeStamp);
        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, MainActivity.class);
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return comments.size();
    }

}