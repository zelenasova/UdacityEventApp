package peter.kalata.com.eventapp.ui.events;

import android.app.NotificationManager;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.parceler.Parcels;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import peter.kalata.com.eventapp.BaseFragment;
import peter.kalata.com.eventapp.R;
import peter.kalata.com.eventapp.data.model.Event;
import peter.kalata.com.eventapp.data.model.Event.EventType;
import peter.kalata.com.eventapp.ui.common.ViewModelFactory;
import peter.kalata.com.eventapp.ui.main.MainChangeFragmentListener;
import peter.kalata.com.eventapp.utils.NotificationUtils;
import peter.kalata.com.eventapp.utils.TextsUtils;

public class EventsFragment extends BaseFragment implements EventAdapter.ActionListener {

    public static final String ARGS_EVENT_TYPE = "args_event_type";

    @BindView(R.id.rvEvents)
    RecyclerView rvEvents;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    private EventsViewModel eventsViewModel;
    private EventAdapter adapter;
    private MainChangeFragmentListener listener;
    private EventType eventType = EventType.MEETINGS;

    public EventsFragment() {}

    public static EventsFragment newInstance(EventType eventType) {
        EventsFragment fragment = new EventsFragment();
        Bundle arguments = new Bundle();
        arguments.putSerializable(ARGS_EVENT_TYPE, eventType);
        fragment.setArguments(arguments);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_events, container, false);
        ButterKnife.bind(this, view);
        if (getArguments() != null) {
            eventType = (EventType) getArguments().getSerializable(ARGS_EVENT_TYPE);
        }
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupToolBar();
        setupRecyclerView();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setupViewModel();
    }

    private void setupToolBar() {
        toolbar.setTitle(TextsUtils.getEventCategoryDisplayString(eventType));
        ((AppCompatActivity)requireActivity()).setSupportActionBar(toolbar);
    }

    private void setupRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false);
        rvEvents.setLayoutManager(layoutManager);
        adapter = new EventAdapter(this);
        rvEvents.setAdapter(adapter);
        rvEvents.addItemDecoration(new DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL));
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof MainChangeFragmentListener) {
            listener = (MainChangeFragmentListener) context;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    @Override
    public void onEventClick(Event event) {
        if (listener != null) {
            listener.onChangeToEventDetail(event, eventType);
        }
    }

    private void setupViewModel() {
        eventsViewModel = ViewModelProviders.of(this, new ViewModelFactory(eventType)).get(EventsViewModel.class);
        eventsViewModel.getError().observe(this, throwable -> showErrorDialog(throwable.getMessage(),
                EventsFragment.class.getName()));
        eventsViewModel.getEvents().observe(this, events -> {
            adapter.setEvents(events);
        });
    }

    @OnClick(R.id.fab)
    void onFabClick(View view) {
        if (listener != null) {
            listener.onChangeToCreateEvent(eventType);
        }
    }

}
