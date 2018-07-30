package peter.kalata.com.eventapp.injection;

import android.content.Context;

import javax.inject.Singleton;
import dagger.Module;
import dagger.Provides;
import peter.kalata.com.eventapp.data.repository.EventRepository;

@Module
public class AppModule {

    Context mAppContext;

    public AppModule(Context appContext) {
        this.mAppContext = appContext;
    }

    @Provides
    @Singleton
    Context provideAppContext() {
        return mAppContext;
    }

    @Provides
    @Singleton
    EventRepository provideEventRepository() {
        return new EventRepository();
    }

}
