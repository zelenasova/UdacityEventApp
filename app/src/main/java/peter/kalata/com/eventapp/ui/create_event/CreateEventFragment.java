package peter.kalata.com.eventapp.ui.create_event;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import peter.kalata.com.eventapp.data.model.Event.EventType;

import com.jakewharton.rxbinding2.view.RxView;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import java.util.Calendar;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import peter.kalata.com.eventapp.BaseFragment;
import peter.kalata.com.eventapp.R;
import peter.kalata.com.eventapp.data.model.Event;
import peter.kalata.com.eventapp.data.model.User;
import peter.kalata.com.eventapp.utils.DateTimeUtils;
import peter.kalata.com.eventapp.utils.SharedPrefsUtils;
import peter.kalata.com.eventapp.utils.TextsUtils;

public class CreateEventFragment extends BaseFragment {

    private static final String DATE_PICKER_TAG = "DatePickerDialog";
    private static final String TIME_PICKER_TAG = "TimePickerDialog";
    private static final String ARGS_EVENT_TYPE = "args_event_type";

    @BindView(R.id.et_title)
    EditText etTitle;

    @BindView(R.id.et_description)
    EditText etDescription;

    @BindView(R.id.et_place)
    EditText etPlace;

    @BindView(R.id.tv_date)
    TextView tvDate;

    @BindView(R.id.tv_time)
    TextView tvTime;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.btn_create)
    Button btnCreate;

    private CreateEventViewModel createEventViewModel;
    private EventType eventType = EventType.MEETINGS;
    private final CompositeDisposable composite = new CompositeDisposable();

    public CreateEventFragment() {
    }

    public static CreateEventFragment newInstance(EventType eventType) {
        CreateEventFragment fragment = new CreateEventFragment();
        Bundle arguments = new Bundle();
        arguments.putSerializable(ARGS_EVENT_TYPE, eventType);
        fragment.setArguments(arguments);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_create_event, container, false);
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
        setContinueButtonListener();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setupViewModel();
    }

    private void setupToolBar() {
        toolbar.setTitle(getString(R.string.create_event));
        ((AppCompatActivity) requireActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity) requireActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void setupViewModel() {
        createEventViewModel = ViewModelProviders.of(this).get(CreateEventViewModel.class);
        createEventViewModel.getError().observe(this, throwable -> showErrorDialog(throwable.getMessage(),
                CreateEventFragment.class.getName()));
        createEventViewModel.getCreateEventSuccess().observe(this, success -> {
            requireActivity().onBackPressed();
        });
    }

    public void showDatePicker() {
        Calendar calendar = DateTimeUtils.getCalendarFromCurrentTimestamp();

        if (!TextUtils.isEmpty(tvDate.getText())) {
            calendar = DateTimeUtils.getCalendarFromString(tvDate.getText().toString());
        }

        DatePickerDialog dialog = DatePickerDialog.newInstance(dateSetListener,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        );
        dialog.setMinDate(calendar);
        dialog.show(getActivity().getFragmentManager(), DATE_PICKER_TAG);
    }

    public void showTimePicker() {
        Calendar calendar = DateTimeUtils.getCalendarFromCurrentTimestamp();

        if (!TextUtils.isEmpty(tvTime.getText())) {
            calendar = DateTimeUtils.getTimeCalendarFromString(tvTime.getText().toString());
        }

        TimePickerDialog dialog = TimePickerDialog.newInstance(timeSetListener,
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE),
                true
        );
        dialog.setMinTime(calendar.get(Calendar.HOUR), calendar.get(Calendar.MINUTE), calendar.get(Calendar.SECOND));
        dialog.show(getActivity().getFragmentManager(), TIME_PICKER_TAG);
    }

    private boolean isFormValid(String title, String place, String date, String time) {
        boolean isValid = true;

        if (TextUtils.isEmpty(title)) {
            isValid = false;
            etTitle.setError(getResources().getString(R.string.error_empty_field));
        }

        if (TextUtils.isEmpty(place)) {
            isValid = false;
            etPlace.setError(getResources().getString(R.string.error_empty_field));
        }

        if (TextUtils.isEmpty(date) || TextUtils.isEmpty(time)) {
            isValid = false;
            showErrorDialog(getString(R.string.error_date), CreateEventFragment.class.getName());
        }

        return isValid;
    }

    private void setContinueButtonListener() {
        composite.add(RxView.clicks(btnCreate)
                .debounce(300, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(aVoid -> {
                    String title = etTitle.getText().toString().trim();
                    String description = etDescription.getText().toString().trim();
                    String place = etPlace.getText().toString().trim();
                    String date = tvDate.getText().toString().trim();
                    String time = tvTime.getText().toString().trim();
                    if (isFormValid(title, place, date, time)) {
                        Event event = new Event(title,
                                description,
                                place,
                                DateTimeUtils.getDateFromDateAndTimeStrings(tvDate.getText().toString(), tvTime.getText().toString()),
                                SharedPrefsUtils.getUserKey(),
                                DateTimeUtils.getCalendarFromCurrentTimestamp().getTime());
                        createEventViewModel.createEvent(event, eventType);
                    }
                })
        );

    }

    @OnClick(R.id.tv_time)
    void onTimeClick(View view) {
        showTimePicker();
    }

    @OnClick(R.id.tv_date)
    void onDateClick(View view) {
        showDatePicker();
    }

    private final DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
            tvDate.setText(DateTimeUtils.getDateString(year, monthOfYear, dayOfMonth));
        }
    };

    private final TimePickerDialog.OnTimeSetListener timeSetListener = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePickerDialog view, int hour, int minute, int second) {
            tvTime.setText(DateTimeUtils.getTimeString(hour, minute));
        }
    };

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            requireActivity().onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        composite.clear();
    }
}
