package peter.kalata.com.eventapp.ui.settings;


import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import javax.inject.Inject;

import peter.kalata.com.eventapp.App;
import peter.kalata.com.eventapp.data.model.User;
import peter.kalata.com.eventapp.data.repository.EventRepository;
import peter.kalata.com.eventapp.utils.SingleLiveEvent;


public class SettingsViewModel extends ViewModel {

    private SingleLiveEvent<Throwable> error;
    private SingleLiveEvent<Boolean> leaveGroupSuccess;
    private MutableLiveData<User> currentUser;

    @Inject
    EventRepository eventRepository;

    public SettingsViewModel() {
        App.getAppComponent().inject(this);
        error = eventRepository.getError();
        leaveGroupSuccess = eventRepository.getLeaveGroupSuccess();
        currentUser = eventRepository.getCurrentUser();
        eventRepository.loadCurrentUser();
    }

    public void updateUser(User user) {
        eventRepository.updateUser(user);
    }

    public void leaveGroup() {
        eventRepository.leaveGroup();
    }

    SingleLiveEvent<Throwable> getError() {
        return error;
    }

    SingleLiveEvent<Boolean> getLeaveGroupSuccess() {
        return leaveGroupSuccess;
    }

    public MutableLiveData<User> getCurrentUser() {
        return currentUser;
    }

}