package peter.kalata.com.eventapp.scheduler;

import android.app.NotificationManager;
import android.content.Context;

import com.firebase.jobdispatcher.JobParameters;
import com.firebase.jobdispatcher.JobService;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import peter.kalata.com.eventapp.App;
import peter.kalata.com.eventapp.data.model.Event;
import peter.kalata.com.eventapp.data.model.Event.EventType;
import peter.kalata.com.eventapp.data.repository.EventRepository;
import peter.kalata.com.eventapp.utils.NotificationUtils;
import peter.kalata.com.eventapp.utils.SharedPrefsUtils;

public class EventJobService extends JobService {

    @Inject
    EventRepository eventRepository;

    private final CompositeDisposable composite = new CompositeDisposable();

    @Override
    public void onCreate() {
        super.onCreate();
        App.getAppComponent().inject(this);
    }

    @Override
    public boolean onStartJob(final JobParameters jobParameters) {
        if (!SharedPrefsUtils.isFirstRun()) {
            loadNewEvents(EventType.MEETINGS);
            loadNewEvents(EventType.HELP);
            loadNewEvents(EventType.TRIPS);
        }
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters jobParameters) {
        return true;
    }

    private void loadNewEvents(EventType eventType) {
        composite.add(
                eventRepository.loadNewEvents(eventType)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                events -> {
                                    for (Event event: events) {
                                        showNotification(event, eventType);
                                    }
                                }
                        )
        );
    }

    private void showNotification(Event event, Event.EventType eventType) {
        NotificationManager mNotificationManager = (NotificationManager) App.getAppContext().getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(NotificationUtils.getID(),
                NotificationUtils.createForegroundNotification(App.getAppContext(), event, eventType));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        composite.clear();
    }
}