package peter.kalata.com.eventapp.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.RemoteViews;

import org.parceler.Parcels;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import peter.kalata.com.eventapp.App;
import peter.kalata.com.eventapp.R;
import peter.kalata.com.eventapp.data.model.Event;
import peter.kalata.com.eventapp.data.repository.EventRepository;
import peter.kalata.com.eventapp.ui.main.MainActivity;
import peter.kalata.com.eventapp.utils.ParsingUtils;
import peter.kalata.com.eventapp.data.model.Event.EventType;
import peter.kalata.com.eventapp.utils.SharedPrefsUtils;

import static peter.kalata.com.eventapp.ui.events.EventsFragment.ARGS_EVENT_TYPE;

/**
 * Implementation of App Widget functionality.
 */
public class EventAppWidgetProvider extends AppWidgetProvider {

    private static final int PENDING_INTENT_ID = 0;

    @Inject
    EventRepository eventRepository;

    @Override
    public void onReceive(Context context, Intent intent) {
        App.getAppComponent().inject(this);
        super.onReceive(context, intent);
    }

    public static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                       int appWidgetId, List<Event> events, Event.EventType eventType) {

        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.event_app_widget);

        views.setTextViewText(R.id.tv_title, eventType.name());
        views.setTextViewText(R.id.tv_events, ParsingUtils.getEventsString(events));


        Intent intent = new Intent(context, MainActivity.class);
        intent.setPackage(context.getPackageName());
        intent.putExtra(ARGS_EVENT_TYPE, eventType);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, PENDING_INTENT_ID, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        views.setOnClickPendingIntent(R.id.ll_widget, pendingIntent);

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        CompositeDisposable composite = new CompositeDisposable();
        for (int appWidgetId : appWidgetIds) {
            EventType eventType = SharedPrefsUtils.getWidgetType(appWidgetId);
            if (eventType != null) {
                composite.add(
                        eventRepository.loadEventsForWidget(eventType)
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(
                                        events -> updateAppWidget(context, appWidgetManager, appWidgetId, events, eventType)
                                )
                );
            }
        }
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        for (int id: appWidgetIds) {
            SharedPrefsUtils.removeWidgetType(id);
        }
        super.onDeleted(context, appWidgetIds);
    }
}

