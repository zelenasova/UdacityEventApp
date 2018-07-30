package peter.kalata.com.eventapp.ui.events;


import android.app.NotificationManager;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.content.Context;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import peter.kalata.com.eventapp.data.model.Event.EventType;
import java.util.List;

import javax.inject.Inject;

import peter.kalata.com.eventapp.App;
import peter.kalata.com.eventapp.data.model.Event;
import peter.kalata.com.eventapp.data.repository.EventRepository;
import peter.kalata.com.eventapp.utils.NotificationUtils;
import peter.kalata.com.eventapp.utils.SingleLiveEvent;


public class EventsViewModel extends ViewModel {

    private SingleLiveEvent<Throwable> error;
    private MutableLiveData<List<Event>> events;

    @Inject
    EventRepository eventRepository;

    public EventsViewModel(EventType eventType) {
        App.getAppComponent().inject(this);
        error = eventRepository.getError();
        switch (eventType) {
            case MEETINGS:
                events = eventRepository.getMeetings();
                break;
            case HELP:
                events = eventRepository.getHelp();
                break;
            case TRIPS:
                events = eventRepository.getTrips();
                break;
            default:
                throw new IllegalArgumentException();
        }

        eventRepository.loadEvents(eventType);
    }

    public MutableLiveData<List<Event>> getEvents() {
        return events;
    }

    SingleLiveEvent<Throwable> getError() {
        return error;
    }

}