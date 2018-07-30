package peter.kalata.com.eventapp.ui.users;


import android.app.NotificationManager;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import peter.kalata.com.eventapp.data.model.Event.EventType;
import android.content.Context;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import peter.kalata.com.eventapp.App;
import peter.kalata.com.eventapp.data.model.Event;
import peter.kalata.com.eventapp.data.model.User;
import peter.kalata.com.eventapp.data.repository.EventRepository;
import peter.kalata.com.eventapp.utils.NotificationUtils;
import peter.kalata.com.eventapp.utils.SingleLiveEvent;


public class UsersViewModel extends ViewModel {

    private SingleLiveEvent<Throwable> error;
    private MutableLiveData<List<User>> users;
    private final CompositeDisposable composite = new CompositeDisposable();

    @Inject
    EventRepository eventRepository;

    public UsersViewModel() {
        App.getAppComponent().inject(this);
        error = eventRepository.getError();
        users = eventRepository.getUsers();
        eventRepository.loadUsers();
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        composite.clear();
    }

    public MutableLiveData<List<User>> getUsers() {
        return users;
    }

    SingleLiveEvent<Throwable> getError() {
        return error;
    }

}