package peter.kalata.com.eventapp.ui.login;


import android.arch.lifecycle.ViewModel;

import javax.inject.Inject;

import peter.kalata.com.eventapp.App;
import peter.kalata.com.eventapp.data.model.Group;
import peter.kalata.com.eventapp.data.model.User;
import peter.kalata.com.eventapp.data.repository.EventRepository;
import peter.kalata.com.eventapp.utils.SingleLiveEvent;


public class UserDetailsViewModel extends ViewModel {

    private SingleLiveEvent<Throwable> error;
    private SingleLiveEvent<Boolean> createUserSuccess;

    @Inject
    EventRepository eventRepository;

    public UserDetailsViewModel() {
        App.getAppComponent().inject(this);
        error = eventRepository.getError();
        createUserSuccess = eventRepository.getCreateUserSuccess();
    }

    public void createUser(User user) {
        eventRepository.writeNewUser(user);
    }

    SingleLiveEvent<Throwable> getError() {
        return error;
    }

    public SingleLiveEvent<Boolean> getCreateUserSuccess() { return createUserSuccess; }
}