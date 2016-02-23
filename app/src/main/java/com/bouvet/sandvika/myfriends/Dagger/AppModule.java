package com.bouvet.sandvika.myfriends.Dagger;

/**
 * Created by Morten on 20.02.2016.
 */
import android.app.Application;

import com.bouvet.sandvika.myfriends.http.MyFriendsRestService;

import javax.inject.Inject;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


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

}