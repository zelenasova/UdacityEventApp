package peter.kalata.com.eventapp.utils;

import android.app.Activity;
import android.util.Patterns;
import java.util.Locale;
import peter.kalata.com.eventapp.App;
import peter.kalata.com.eventapp.R;
import peter.kalata.com.eventapp.data.model.Event;
import peter.kalata.com.eventapp.ui.events.EventsFragment;
import peter.kalata.com.eventapp.data.model.Event.EventType;
import static peter.kalata.com.eventapp.data.repository.EventRepository.HELP_NODE;
import static peter.kalata.com.eventapp.data.repository.EventRepository.MEETINGS_NODE;
import static peter.kalata.com.eventapp.data.repository.EventRepository.TRIPS_NODE;

/**
 * NetworkUtils contains useful methods for checking network state and connectivity.
 */
public final class TextsUtils {

    public static boolean isEmailValid(String email) {
        if( !email.isEmpty() && Patterns.EMAIL_ADDRESS.matcher(email).matches())
            return true;
        else
            return false;
    }

    public static String getGroupNodeString(EventType eventType) {
        switch (eventType) {
            case MEETINGS:
                return MEETINGS_NODE;
            case HELP:
                return HELP_NODE;
            case TRIPS:
                return TRIPS_NODE;
            default:
                throw new IllegalArgumentException();
        }
    }

    public static String getEventCategoryDisplayString(EventType eventType) {
        switch (eventType) {
            case MEETINGS:
                return App.getAppContext().getString(R.string.meetings);
            case HELP:
                return App.getAppContext().getString(R.string.help);
            case TRIPS:
                return App.getAppContext().getString(R.string.trips);
            default:
                throw new IllegalArgumentException();
        }
    }

    public static int getEventCategoryId(EventType eventType) {
        switch (eventType) {
            case MEETINGS:
                return R.id.navigation_meetings;
            case HELP:
                return R.id.navigation_help;
            case TRIPS:
                return R.id.navigation_trips;
            default:
                throw new IllegalArgumentException();
        }
    }

    public static int getEventCategoryDrawableId(EventType eventType) {
        switch (eventType) {
            case MEETINGS:
                return R.drawable.ic_local_bar;
            case HELP:
                return R.drawable.ic_build;
            case TRIPS:
                return R.drawable.ic_landscape;
            default:
                throw new IllegalArgumentException();
        }
    }

    public static String getNotificationContentString (Event event) {
        return String.format(App.getAppContext().getString(R.string.event_line),
               event.getTitle(), event.getPlace(), DateTimeUtils.getDateTimeStingFromDate(event.getDate()));
    }


}
