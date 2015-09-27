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
        public TextView address;
        public TextView startTime;
        public TextView tag0;
        public TextView tag1;
        public TextView tag2;
        public View tags;
        public View view;

        public ViewHolder(View v) {
            super(v);
            view = v;
            titleView = (TextView) itemView.findViewById(R.id.titleEvent);
            scoreView = (TextView) itemView.findViewById(R.id.score);
            address = (TextView) itemView.findViewById(R.id.location);
            startTime = (TextView) itemView.findViewById(R.id.start);
            tag0 = (TextView) itemView.findViewById(R.id.tag_0);
            tag1 = (TextView) itemView.findViewById(R.id.tag_1);
            tag2 = (TextView) itemView.findViewById(R.id.tag_2);
            tags = itemView.findViewById(R.id.tags);
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
        Event temp = events.get(position);
        holder.titleView.setText(temp.getTitle());
        holder.scoreView.setText(temp.getScore() + "");
        holder.address.setText(temp.getAddress());
        holder.startTime.setText(temp.getStartDate());
        try {
            holder.tag0.setText('#' + temp.getTags().get(0));
            try {
                holder.tag1.setText('#' + temp.getTags().get(1));
                holder.tag2.setText('#' + temp.getTags().get(2));
            } catch (Exception e) {

            }
        } catch (Exception e) {
            holder.tags.setVisibility(View.GONE);
        }
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
                        if (event.getTags().contains(constraint.toString().toLowerCase().trim())) {
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