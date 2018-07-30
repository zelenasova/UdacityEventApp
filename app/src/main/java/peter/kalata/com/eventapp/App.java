package peter.kalata.com.eventapp;

import android.app.Application;
import android.content.Context;

import peter.kalata.com.eventapp.injection.AppComponent;
import peter.kalata.com.eventapp.injection.AppModule;
import peter.kalata.com.eventapp.injection.DaggerAppComponent;


public class App extends Application {

    private AppComponent mAppComponent;
    private static App sInstance;

    public static Context getAppContext() {
        return sInstance.getApplicationContext();
    }

    public static AppComponent getAppComponent() {
        return sInstance.mAppComponent;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;
        initDagger();
    }

    private void initDagger() {
        if (mAppComponent == null) {
            mAppComponent = DaggerAppComponent.builder()
                    .appModule(new AppModule(this))
                    .build();
        }
    }

}
