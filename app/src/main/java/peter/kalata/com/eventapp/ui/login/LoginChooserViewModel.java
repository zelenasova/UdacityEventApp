package peter.kalata.com.eventapp.ui.login;


import android.arch.lifecycle.ViewModel;

import javax.inject.Inject;

import peter.kalata.com.eventapp.App;
import peter.kalata.com.eventapp.data.model.User;
import peter.kalata.com.eventapp.data.repository.EventRepository;
import peter.kalata.com.eventapp.utils.SingleLiveEvent;


public class LoginChooserViewModel extends ViewModel {

    private SingleLiveEvent<Throwable> error;
    private SingleLiveEvent<Boolean> loginSuccess;

    @Inject
    EventRepository eventRepository;

    public LoginChooserViewModel() {
        App.getAppComponent().inject(this);
        error = eventRepository.getError();
        loginSuccess = eventRepository.getLoginSuccess();
    }


    public void login(String groupName, String password) {
        eventRepository.login(groupName, password);
    }

    SingleLiveEvent<Throwable> getError() {
        return error;
    }

    public SingleLiveEvent<Boolean> getLoginSuccess() { return loginSuccess; }
}