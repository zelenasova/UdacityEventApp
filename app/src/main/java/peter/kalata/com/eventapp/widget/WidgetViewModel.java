package peter.kalata.com.eventapp.widget;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import peter.kalata.com.eventapp.App;
import peter.kalata.com.eventapp.data.model.Event;
import peter.kalata.com.eventapp.data.repository.EventRepository;
import peter.kalata.com.eventapp.utils.SingleLiveEvent;

public class WidgetViewModel extends ViewModel {

    @Inject
    EventRepository eventRepository;

    private final CompositeDisposable composite = new CompositeDisposable();
    private SingleLiveEvent<Throwable> error = new SingleLiveEvent<>();
    private MutableLiveData<List<Event>> events = new MutableLiveData<>();

    public WidgetViewModel() {
        App.getAppComponent().inject(this);
    }

    public void loadEvents(Event.EventType eventType) {
        composite.add(
                eventRepository.loadEventsForWidget(eventType)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                events -> {
                                    this.events.setValue(events);
                                },
                                throwable -> {
                                    error.setValue(throwable);
                                }
                        )
        );
    }

    public MutableLiveData<List<Event>> getEvents() {
        return events;
    }

    SingleLiveEvent<Throwable> getError() {
        return error;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        composite.clear();
    }
}