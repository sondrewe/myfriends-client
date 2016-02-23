package com.bouvet.sandvika.myfriends.Dagger;

import com.bouvet.sandvika.myfriends.LoginActivity;
import com.bouvet.sandvika.myfriends.MapsActivity;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by Morten on 20.02.2016.
 */
@Singleton
@Component(modules={AppModule.class, NetModule.class})
public interface NetComponent {
    void inject(LoginActivity activity);
    void inject(MapsActivity activity);
    // void inject(MyFragment fragment);
    // void inject(MyService service);
}