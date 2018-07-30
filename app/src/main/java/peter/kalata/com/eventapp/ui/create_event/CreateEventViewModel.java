package peter.kalata.com.eventapp.ui.create_event;


import android.arch.lifecycle.ViewModel;

import javax.inject.Inject;

import peter.kalata.com.eventapp.App;
import peter.kalata.com.eventapp.data.model.Event;
import peter.kalata.com.eventapp.data.model.User;
import peter.kalata.com.eventapp.data.repository.EventRepository;
import peter.kalata.com.eventapp.utils.SingleLiveEvent;
import peter.kalata.com.eventapp.data.model.Event.EventType;


public class CreateEventViewModel extends ViewModel {

    private SingleLiveEvent<Throwable> error;
    private SingleLiveEvent<Boolean> createEventSuccess;

    @Inject
    EventRepository eventRepository;

    public CreateEventViewModel() {
        App.getAppComponent().inject(this);
        error = eventRepository.getError();
        createEventSuccess = eventRepository.getCreateEventSuccess();
    }

    public void createEvent(Event event, EventType eventType) {
        eventRepository.writeEvent(event, eventType);
    }

    SingleLiveEvent<Throwable> getError() {
        return error;
    }

    public SingleLiveEvent<Boolean> getCreateEventSuccess() { return createEventSuccess; }
}