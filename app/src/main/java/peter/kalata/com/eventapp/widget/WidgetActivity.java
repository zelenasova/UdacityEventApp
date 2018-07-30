package peter.kalata.com.eventapp.widget;

import android.appwidget.AppWidgetManager;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatRadioButton;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import peter.kalata.com.eventapp.App;
import peter.kalata.com.eventapp.R;
import peter.kalata.com.eventapp.data.model.Event;
import peter.kalata.com.eventapp.data.model.Event.EventType;
import peter.kalata.com.eventapp.ui.dialogs.AlertDialogFragment;
import peter.kalata.com.eventapp.ui.events.EventsFragment;
import peter.kalata.com.eventapp.utils.SharedPrefsUtils;

import static peter.kalata.com.eventapp.ui.events.EventsFragment.ARGS_EVENT_TYPE;

public class WidgetActivity extends AppCompatActivity {

    @BindView(R.id.radioGroup)
    RadioGroup radioGroup;

    private int appWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID;
    private WidgetViewModel widgetViewModel;
    private EventType eventType = EventType.MEETINGS;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_widget);
        ButterKnife.bind(this);
        setupViewModel();
        if (getIntent().getExtras() != null) {
            appWidgetId = getIntent().getExtras().getInt(
                    AppWidgetManager.EXTRA_APPWIDGET_ID,
                    AppWidgetManager.INVALID_APPWIDGET_ID);

            if (appWidgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {
                finish();
            }
        }
    }

    private void setupViewModel() {
        widgetViewModel = ViewModelProviders.of(this).get(WidgetViewModel.class);
        widgetViewModel.getEvents().observe(this, meetings -> {
            updateWidget(meetings);
        });
        widgetViewModel.getError().observe(this, throwable ->
                AlertDialogFragment.newInstance(throwable.getMessage()).show(getSupportFragmentManager(), WidgetActivity.class.getName())
        );
    }

    @OnClick(R.id.btn_ok)
    public void onOkButtonClick() {
        int radioButtonID = radioGroup.getCheckedRadioButtonId();
        View radioButton = radioGroup.findViewById(radioButtonID);
        int index = radioGroup.indexOfChild(radioButton);
        switch (index) {
            case 0: {
                eventType = EventType.MEETINGS;
                break;
            }
            case 1: {
                eventType = EventType.HELP;
                break;
            }
            case 2: {
                eventType = EventType.TRIPS;
                break;
            }
        }
        widgetViewModel.loadEvents(eventType);

    }

    private void updateWidget(List<Event> events) {
        Context context = App.getAppContext();
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        appWidgetManager.getAppWidgetOptions(appWidgetId).putInt(ARGS_EVENT_TYPE, 92);
        SharedPrefsUtils.setWidgetType(appWidgetId, eventType);
        EventAppWidgetProvider.updateAppWidget(context, appWidgetManager, appWidgetId, events, eventType);

        Intent intent = new Intent();
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        setResult(RESULT_OK, intent);
        finish();
    }


}
