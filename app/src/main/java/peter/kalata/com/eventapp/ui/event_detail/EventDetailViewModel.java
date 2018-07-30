package peter.kalata.com.eventapp.ui.event_detail;


import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import java.util.List;
import javax.inject.Inject;
import peter.kalata.com.eventapp.App;
import peter.kalata.com.eventapp.data.model.Event.EventType;
import peter.kalata.com.eventapp.data.model.User;
import peter.kalata.com.eventapp.data.repository.EventRepository;
import peter.kalata.com.eventapp.data.repository.EventRepository.EventUsersRepositoryCallback;
import peter.kalata.com.eventapp.utils.SingleLiveEvent;

public class EventDetailViewModel extends ViewModel {

    private SingleLiveEvent<Throwable> error = new SingleLiveEvent<>();
    private MutableLiveData<List<User>> eventUsers = new MutableLiveData<>();
    private MutableLiveData<User> currentUser;
    private String eventKey;
    private EventType eventType;

    @Inject
    EventRepository eventRepository;

    public EventDetailViewModel(String eventKey, EventType eventType) {
        App.getAppComponent().inject(this);
        this.eventKey = eventKey;
        this.eventType = eventType;
        currentUser = eventRepository.getCurrentUser();
        eventRepository.loadCurrentUser();
        eventRepository.loadUsersForEvent(eventKey, eventType, new EventUsersRepositoryCallback<User>() {
            @Override
            public void onSuccess(List<User> result) {
                eventUsers.setValue(result);
            }

            @Override
            public void onError(Throwable throwable) {
                error.setValue(throwable);
            }
        });
    }

    public void joinEvent(User currentUser) {
        eventRepository.writeEventUser(eventKey, eventType, currentUser);
    }

    public void leaveEvent(User currentUser) {
        eventRepository.removeEventUser(eventKey, eventType, currentUser);
    }

    SingleLiveEvent<Throwable> getError() {
        return error;
    }

    public MutableLiveData<List<User>> getEventUsers() {
        return eventUsers;
    }

    public MutableLiveData<User> getCurrentUser() {
        return currentUser;
    }
}