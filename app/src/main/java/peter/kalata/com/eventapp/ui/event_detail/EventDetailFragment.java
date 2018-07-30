package peter.kalata.com.eventapp.ui.event_detail;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import peter.kalata.com.eventapp.data.model.Event.EventType;


import org.parceler.Parcels;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import peter.kalata.com.eventapp.BaseFragment;
import peter.kalata.com.eventapp.R;
import peter.kalata.com.eventapp.data.model.Event;
import peter.kalata.com.eventapp.data.model.User;
import peter.kalata.com.eventapp.ui.common.ViewModelFactory;
import peter.kalata.com.eventapp.ui.events.EventsViewModel;
import peter.kalata.com.eventapp.ui.users.UserAdapter;
import peter.kalata.com.eventapp.utils.DateTimeUtils;
import peter.kalata.com.eventapp.utils.UserUtils;

public class EventDetailFragment extends BaseFragment {

    private static final String ARGS_EVENT = "args_event";
    private static final String ARGS_EVENT_TYPE = "args_event_type";

    @BindView(R.id.rv_users)
    RecyclerView rvUsers;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_description)
    TextView tvDescription;
    @BindView(R.id.tv_place)
    TextView tvPlace;
    @BindView(R.id.tv_time)
    TextView tvTime;
    @BindView(R.id.btn_join)
    TextView btnJoin;
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    private EventDetailViewModel eventDetailViewModel;
    private Event event;
    private EventType eventType = EventType.MEETINGS;
    private UserAdapter adapter;
    private User currentUser;
    private boolean isGoingToEvent = false;


    public EventDetailFragment() {
    }

    public static EventDetailFragment newInstance(Event event, EventType eventType) {
        EventDetailFragment fragment = new EventDetailFragment();
        Bundle arguments = new Bundle();
        arguments.putParcelable(ARGS_EVENT, Parcels.wrap(event));
        arguments.putSerializable(ARGS_EVENT_TYPE, eventType);
        fragment.setArguments(arguments);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            event = Parcels.unwrap(getArguments().getParcelable(ARGS_EVENT));
            eventType = (EventType) getArguments().getSerializable(ARGS_EVENT_TYPE);
        }
        setHasOptionsMenu(true);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setupViewModel();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_event_detail, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupToolBar();
        tvTitle.setText(event.getTitle());
        tvDescription.setText(event.getDescription());
        tvPlace.setText(event.getPlace());
        tvTime.setText(DateTimeUtils.getDateTimeStingFromDate(event.getDate()));
        setupRecyclerView();
    }

    private void setupToolBar() {
        toolbar.setTitle(event.getTitle());
        ((AppCompatActivity)requireActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity)requireActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void setupViewModel() {
        eventDetailViewModel = ViewModelProviders.of(this,
                new ViewModelFactory(event.getKey(), eventType)).get(EventDetailViewModel.class);
        eventDetailViewModel.getError().observe(this, throwable -> showToast(throwable.getMessage()));
        eventDetailViewModel.getCurrentUser().observe(this, user -> {
           currentUser = user;
        });
        eventDetailViewModel.getEventUsers().observe(this, users -> {
            if (UserUtils.isCurrentUserInList(users)) {
                btnJoin.setText(getResources().getString(R.string.leave_the_event));
                isGoingToEvent = true;
            } else {
                btnJoin.setText(getResources().getString(R.string.join_the_event));
                isGoingToEvent = false;
            }
            btnJoin.setVisibility(View.VISIBLE);
            adapter.setUsers(users);
        });
    }

    private void setupRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false);
        rvUsers.setLayoutManager(layoutManager);
        adapter = new UserAdapter();
        rvUsers.setAdapter(adapter);
        rvUsers.addItemDecoration(new DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL));
    }

    @OnClick(R.id.btn_join)
    void onJoinEventClick(View view) {
        if (currentUser != null) {
            if (isGoingToEvent) {
                eventDetailViewModel.leaveEvent(currentUser);
            } else {
                eventDetailViewModel.joinEvent(currentUser);
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            getActivity().onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
