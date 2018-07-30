package peter.kalata.com.eventapp.ui.main;

import peter.kalata.com.eventapp.data.model.Event;

public interface MainChangeFragmentListener {

    void onChangeToCreateEvent(Event.EventType eventType);

    void onChangeToEventDetail(Event event, Event.EventType eventType);

}
