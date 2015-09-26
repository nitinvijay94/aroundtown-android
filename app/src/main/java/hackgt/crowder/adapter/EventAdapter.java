package hackgt.crowder.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import android.view.View;
import android.view.ViewGroup;
import android.view.LayoutInflater;

import com.example.hackgt.R;

import hackgt.crowder.activity.EventInfoActivity;
import hackgt.crowder.model.Event;

import java.util.ArrayList;
import java.util.List;


public class EventAdapter extends RecyclerView.Adapter<EventAdapter.ViewHolder> implements Filterable {
    private List<Event> events;
    private List<Event> eventsCopy;
    private Context context;


    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView titleView;
        public TextView scoreView;
        public View view;

        public ViewHolder(View v) {
            super(v);
            view = v;
            titleView = (TextView) itemView.findViewById(R.id.titleEvent);
            scoreView = (TextView) itemView.findViewById(R.id.score);
        }
    }

    public EventAdapter(List<Event> myDataset, Context context) {
        events = myDataset;
        eventsCopy = new ArrayList<>(myDataset);
        this.context = context;
    }

    @Override
    public EventAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                      int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.event_list_item, parent, false);

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.titleView.setText(events.get(position).getTitle());
        holder.scoreView.setText(events.get(position).getScore() + "");
        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, EventInfoActivity.class);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return events.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults results = new FilterResults();
                if (constraint == null || constraint.length() == 0) {
                    results.values = eventsCopy;
                    results.count = eventsCopy.size();
                } else {
                    ArrayList<Event> resultEvents = new ArrayList<>();
                    for (Event event : eventsCopy) {
                        if (event.getTitle().toLowerCase().contains(constraint.toString().toLowerCase().trim())) {
                            resultEvents.add(event);
                        }
                    }
                    results.values = resultEvents;
                    results.count = resultEvents.size();
                }
                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                events = (ArrayList<Event>) results.values;
                notifyDataSetChanged();
            }
        };
    }

    public List<Event> getEvents() {
        return events;
    }

    public void setEvents(List<Event> events) {
        this.events = events;
        eventsCopy = new ArrayList<>(events);
        notifyDataSetChanged();
    }
}