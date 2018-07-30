package peter.kalata.com.eventapp.injection;

import javax.inject.Singleton;
import dagger.Component;
import peter.kalata.com.eventapp.scheduler.EventJobService;
import peter.kalata.com.eventapp.ui.create_event.CreateEventViewModel;
import peter.kalata.com.eventapp.ui.event_detail.EventDetailViewModel;
import peter.kalata.com.eventapp.ui.events.EventsViewModel;
import peter.kalata.com.eventapp.ui.login.CreateGroupViewModel;
import peter.kalata.com.eventapp.ui.login.LoginChooserViewModel;
import peter.kalata.com.eventapp.ui.login.UserDetailsViewModel;
import peter.kalata.com.eventapp.ui.settings.SettingsViewModel;
import peter.kalata.com.eventapp.ui.users.UsersViewModel;
import peter.kalata.com.eventapp.widget.EventAppWidgetProvider;
import peter.kalata.com.eventapp.widget.WidgetViewModel;

@Singleton
@Component(modules = {AppModule.class})
public interface AppComponent {

    void inject(CreateGroupViewModel viewModel);

    void inject(WidgetViewModel viewModel);

    void inject(EventDetailViewModel viewModel);

    void inject(LoginChooserViewModel viewModel);

    void inject(SettingsViewModel viewModel);

    void inject(UserDetailsViewModel viewModel);

    void inject(EventsViewModel viewModel);

    void inject(CreateEventViewModel viewModel);

    void inject(UsersViewModel viewModel);

    void inject(EventJobService eventJobService);

    void inject(EventAppWidgetProvider eventAppWidgetProvider);

}