package com.bouvet.sandvika.myfriends.Dagger;

/**
 * Created by Morten on 20.02.2016.
 */
import android.app.Application;
import android.content.Context;
import android.location.LocationManager;


import javax.inject.Inject;


import dagger.Module;
import dagger.Provides;



@Module
public class AppModule {

    private App app;
    @Inject
    public AppModule(App app) {
        this.app = app;
    }

    @Provides
    public Application provideApplication() {
        return app;
    }

    @Provides
    public App application() {
        return this.app;
    }

    @Provides
    public Context applicationContext() {
        return this.app;
    }

    @Provides
    public LocationManager locationService(Context context) {
        return (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
    }
}