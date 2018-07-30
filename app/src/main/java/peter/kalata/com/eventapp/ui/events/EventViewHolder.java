package peter.kalata.com.eventapp.ui.events;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import peter.kalata.com.eventapp.R;
import peter.kalata.com.eventapp.data.model.Event;
import peter.kalata.com.eventapp.utils.DateTimeUtils;

public class EventViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_place)
    TextView tvPlace;
    @BindView(R.id.tv_date)
    TextView tvDate;

    private EventAdapter.ActionListener actionListener;
    private Event event;

    EventViewHolder(View itemView, final EventAdapter.ActionListener actionListener) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        this.actionListener = actionListener;
    }

    public void bindData(Event event) {
        this.event = event;
        setupTexts();
    }

    private void setupTexts() {
        tvTitle.setText(event.getTitle());
        tvPlace.setText(event.getPlace());
        tvDate.setText(DateTimeUtils.getDateTimeStingFromDate(event.getDate()));
    }

    @OnClick(R.id.cl_root)
    void onEventClick() {
        actionListener.onEventClick(event);
    }
}
