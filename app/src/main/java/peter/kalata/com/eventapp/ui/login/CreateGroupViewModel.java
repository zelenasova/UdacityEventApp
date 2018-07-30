package peter.kalata.com.eventapp.ui.login;


import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;
import peter.kalata.com.eventapp.App;
import peter.kalata.com.eventapp.data.model.Group;
import peter.kalata.com.eventapp.data.repository.EventRepository;
import peter.kalata.com.eventapp.utils.SingleLiveEvent;


public class CreateGroupViewModel extends ViewModel {

    private SingleLiveEvent<Throwable> error;
    private SingleLiveEvent<Boolean> createGroupSuccess;

    @Inject
    EventRepository eventRepository;

    public CreateGroupViewModel() {
        App.getAppComponent().inject(this);
        error = eventRepository.getError();
        createGroupSuccess = eventRepository.getCreateGroupSuccess();
    }


    public void createGroup(Group group) {
        eventRepository.writeNewGroup(group);
    }

    SingleLiveEvent<Throwable> getError() {
        return error;
    }

    public SingleLiveEvent<Boolean> getCreateGroupSuccess() { return createGroupSuccess; }
}