package peter.kalata.com.eventapp.utils;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import peter.kalata.com.eventapp.App;
import peter.kalata.com.eventapp.data.model.Event;

public class SharedPrefsUtils {

    public static final String GROUP_NAME_KEY = "group_name";
    public static final String USER_KEY_KEY = "user_key";
    public static final String FIRST_RUN_KEY = "first_run";
    public static final String CHECK_NEW_EVENT_TIMESTAMP_KEY = "check_new_event_timestamp";
    private static final String WIDGET_PREFIX_KEY = "appwidget";

    public static SharedPreferences getSharedPrefs() {
        return PreferenceManager.getDefaultSharedPreferences(App.getAppContext());
    }

    private static SharedPreferences.Editor getSharedPrefsEditor() {
        return PreferenceManager.getDefaultSharedPreferences(App.getAppContext()).edit();
    }


    public static String getGroupName() {
        return getSharedPrefs().getString(GROUP_NAME_KEY, null);
    }

    public static void setGroupName(String groupName) {
        getSharedPrefsEditor().putString(GROUP_NAME_KEY, groupName).apply();
    }

    public static String getUserKey() {
        return getSharedPrefs().getString(USER_KEY_KEY, null);
    }

    public static void setUserKey(String userKey) {
        getSharedPrefsEditor().putString(USER_KEY_KEY, userKey).apply();
    }

    public static Boolean isFirstRun() {
        return getSharedPrefs().getBoolean(FIRST_RUN_KEY, true);
    }

    public static void setFirstRun(boolean isFirstRun) {
        getSharedPrefsEditor().putBoolean(FIRST_RUN_KEY, isFirstRun).apply();
    }

    public static long getCheckNewEventTimestamp() {
        return getSharedPrefs().getLong(CHECK_NEW_EVENT_TIMESTAMP_KEY, -1);
    }

    public static void setCheckNewEventTimestamp(long timestamp) {
        getSharedPrefsEditor().putLong(CHECK_NEW_EVENT_TIMESTAMP_KEY, timestamp).apply();
    }

    public static Event.EventType getWidgetType(int appWidgetId) {
        String enumString = getSharedPrefs().getString(WIDGET_PREFIX_KEY + appWidgetId, null);
        if (enumString != null) {
            return Event.EventType.valueOf(enumString);
        }
        return null;
    }

    public static void setWidgetType(int appWidgetId, Event.EventType eventType) {
        getSharedPrefsEditor().putString(WIDGET_PREFIX_KEY + appWidgetId, eventType.name()).apply();
    }

    public static void removeWidgetType(int appWidgetId) {
        getSharedPrefsEditor().remove(WIDGET_PREFIX_KEY + appWidgetId).apply();
    }

}
