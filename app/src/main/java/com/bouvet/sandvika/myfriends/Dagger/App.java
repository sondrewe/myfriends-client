package com.bouvet.sandvika.myfriends.Dagger;

import android.app.Application;



public class App extends Application {

    private ContextComponent mContextComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        // Dagger%COMPONENT_NAME%
        mContextComponent = DaggerContextComponent.builder()
                // list of modules that are part of this component need to be created here too
                .appModule(new AppModule(this)) // This also corresponds to the name of your module: %component_name%Module
                .netModule(new NetModule("http://myfriends-server.cfapps.io/"))
                .build();

        // If a Dagger 2 component does not have any constructor arguments for any of its modules,
        // then we can use .create() as a shortcut instead: 
        //  mAppComponent = com.codepath.dagger.components.DaggerNetComponent.create();
    }

    public ContextComponent getContextComponent() {
        return mContextComponent;
    }
}