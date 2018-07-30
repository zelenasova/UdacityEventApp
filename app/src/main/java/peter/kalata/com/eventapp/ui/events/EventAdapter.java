package peter.kalata.com.eventapp.ui.events;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import java.util.List;

import peter.kalata.com.eventapp.R;
import peter.kalata.com.eventapp.data.model.Event;

public class EventAdapter extends RecyclerView.Adapter {

    List<Event> events;
    private final ActionListener actionListener;

    public EventAdapter(ActionListener actionListener){
        this.actionListener = actionListener;
        setHasStableIds(true);
    }

    public void setEvents(List<Event> events) {
        this.events = events;
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_event, viewGroup, false);
        return new EventViewHolder(itemView, actionListener);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int i) {
        ((EventViewHolder) holder).bindData(events.get(i));
    }

    @Override
    public int getItemCount() {
        return events == null ? 0 : events.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    interface ActionListener {

        void onEventClick(Event event);

    }



}