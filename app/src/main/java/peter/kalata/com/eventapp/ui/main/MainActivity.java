package peter.kalata.com.eventapp.ui.main;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import peter.kalata.com.eventapp.R;
import peter.kalata.com.eventapp.data.model.Event;
import peter.kalata.com.eventapp.scheduler.JobCenter;
import peter.kalata.com.eventapp.ui.create_event.CreateEventFragment;
import peter.kalata.com.eventapp.ui.event_detail.EventDetailFragment;
import peter.kalata.com.eventapp.ui.events.EventsFragment;
import peter.kalata.com.eventapp.ui.login.LoginActivity;
import peter.kalata.com.eventapp.ui.settings.SettingsFragment;
import peter.kalata.com.eventapp.ui.users.UsersFragment;
import peter.kalata.com.eventapp.utils.DateTimeUtils;
import peter.kalata.com.eventapp.utils.SharedPrefsUtils;
import peter.kalata.com.eventapp.utils.TextsUtils;

import static peter.kalata.com.eventapp.ui.events.EventsFragment.ARGS_EVENT_TYPE;

public class MainActivity extends AppCompatActivity implements MainChangeFragmentListener {

    private FragmentManager fm;

    public static void startMainActivity(Context context) {
        Intent intent = new Intent(context, MainActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        JobCenter.scheduleEventService(this);
        if (SharedPrefsUtils.isFirstRun()) {
            LoginActivity.startLoginActivity(this);
            finish();
        }
        setContentView(R.layout.activity_main);

        fm = getSupportFragmentManager();

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        if (savedInstanceState == null) {
            if (getIntent().hasExtra(ARGS_EVENT_TYPE)) {
                Event.EventType eventType = (Event.EventType) getIntent().getSerializableExtra(ARGS_EVENT_TYPE);
                navigation.setSelectedItemId(TextsUtils.getEventCategoryId(eventType));
            } else {
                navigation.setSelectedItemId(R.id.navigation_meetings);
            }
        }

    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_users:
                    fm.beginTransaction()
                            .replace(R.id.fl_container, UsersFragment.newInstance())
                            .commit();
                    return true;
                case R.id.navigation_meetings:
                    fm.beginTransaction()
                            .replace(R.id.fl_container, EventsFragment.newInstance(Event.EventType.MEETINGS))
                            .commit();
                    return true;
                case R.id.navigation_help:
                    fm.beginTransaction()
                            .replace(R.id.fl_container, EventsFragment.newInstance(Event.EventType.HELP))
                            .commit();
                    return true;
                case R.id.navigation_trips:
                    fm.beginTransaction()
                            .replace(R.id.fl_container, EventsFragment.newInstance(Event.EventType.TRIPS))
                            .commit();
                    return true;
                case R.id.navigation_settings:
                    fm.beginTransaction()
                            .replace(R.id.fl_container, SettingsFragment.newInstance())
                            .commit();
                    return true;
            }
            return false;
        }
    };

    @Override
    public void onChangeToCreateEvent(Event.EventType eventType) {
        fm.beginTransaction()
                .replace(R.id.fl_container, CreateEventFragment.newInstance(eventType))
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onChangeToEventDetail(Event event, Event.EventType eventType) {
        fm.beginTransaction()
                .replace(R.id.fl_container, EventDetailFragment.newInstance(event, eventType))
                .addToBackStack(null)
                .commit();
    }

    @Override
    protected void onStop() {
        super.onStop();
        SharedPrefsUtils.setCheckNewEventTimestamp(DateTimeUtils.getCurrentTimestamp());
    }
}
