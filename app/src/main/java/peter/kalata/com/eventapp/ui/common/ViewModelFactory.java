package peter.kalata.com.eventapp.ui.common;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import peter.kalata.com.eventapp.data.model.Event.EventType;
import peter.kalata.com.eventapp.ui.event_detail.EventDetailViewModel;
import peter.kalata.com.eventapp.ui.events.EventsViewModel;

public class ViewModelFactory implements ViewModelProvider.Factory {

    private EventType eventType;
    private String eventKey;

    public ViewModelFactory(EventType eventType) {
        this.eventType = eventType;
    }

    public ViewModelFactory(String eventKey, EventType eventType) {
        this.eventKey = eventKey;
        this.eventType = eventType;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(EventsViewModel.class)) {
            return (T) new EventsViewModel(eventType);
        }
        if (modelClass.isAssignableFrom(EventDetailViewModel.class)) {
            return (T) new EventDetailViewModel(eventKey, eventType);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }


}