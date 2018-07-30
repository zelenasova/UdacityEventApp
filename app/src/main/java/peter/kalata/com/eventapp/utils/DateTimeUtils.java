package peter.kalata.com.eventapp.utils;


import android.text.TextUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

public class DateTimeUtils {

    private static final SimpleDateFormat DATE_FORMAT;
    private static final SimpleDateFormat TIME_FORMAT;
    private static final SimpleDateFormat DATE_TIME_FORMAT;

    static {
        DATE_FORMAT = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
        TIME_FORMAT = new SimpleDateFormat("HH:mm", Locale.US);
        DATE_TIME_FORMAT = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.US);
    }

    public static synchronized Calendar getCalendarFromCurrentTimestamp() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeZone(TimeZone.getDefault());;
        calendar.setTimeInMillis(System.currentTimeMillis());
        return calendar;
    }

    public static synchronized String getDateFromCurrentTimestamp() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeZone(TimeZone.getDefault());
        calendar.setTimeInMillis(System.currentTimeMillis());
        return DATE_FORMAT.format(calendar.getTime());
    }

    public static synchronized long getCurrentTimestamp() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeZone(TimeZone.getDefault());
        calendar.setTimeInMillis(System.currentTimeMillis());
        return calendar.getTimeInMillis();
    }

    public static synchronized String getDateTimeStingFromDate(Date date) {
        if (date == null) {
            return "";
        }
        return DATE_TIME_FORMAT.format(date);
    }

    public static synchronized Calendar getCalendarFromString(String dateString) {
        try {
            final Date date = DATE_FORMAT.parse(dateString);
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeZone(TimeZone.getDefault());
            calendar.setTimeInMillis(date.getTime());
            return calendar;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return Calendar.getInstance();
    }

    public static synchronized String getDateString(int year, int monthOfYear, int dayOfMonth) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeZone(TimeZone.getDefault());
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, monthOfYear);
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

        return DATE_FORMAT.format(calendar.getTime());
    }

    public static synchronized String getTimeString(int hour, int minute) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        return TIME_FORMAT.format(calendar.getTime());
    }

    public static synchronized Calendar getTimeCalendarFromString(String dateString) {
        try {
            final Date date = TIME_FORMAT.parse(dateString);
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeZone(TimeZone.getDefault());
            calendar.setTimeInMillis(date.getTime());
            return calendar;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return Calendar.getInstance();
    }

    public static synchronized Date getDateFromDateAndTimeStrings(String dateString, String timeString) {
        try {
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeZone(TimeZone.getDefault());
            Date date = DATE_TIME_FORMAT.parse(dateString+ " " + timeString);
            calendar.setTimeInMillis(date.getTime());
            return calendar.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

}
