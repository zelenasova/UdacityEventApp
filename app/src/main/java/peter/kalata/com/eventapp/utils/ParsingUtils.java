package peter.kalata.com.eventapp.utils;

import java.util.List;
import peter.kalata.com.eventapp.App;
import peter.kalata.com.eventapp.R;
import peter.kalata.com.eventapp.data.model.Event;

public class ParsingUtils {

    public static String getEventsString(List<Event> events) {

        StringBuilder result = new StringBuilder();

        for (Event event : events) {

            String title = event.getTitle();
            String place = event.getPlace();
            String date = DateTimeUtils.getDateTimeStingFromDate(event.getDate());

            String format = App.getAppContext().getString(R.string.event_line);
            result.append(String.format(format, title, place, date));
            result.append("\n");
        }
        return result.toString();
    }


}
