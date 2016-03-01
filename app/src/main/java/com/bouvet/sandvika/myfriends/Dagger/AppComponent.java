package com.bouvet.sandvika.myfriends.Dagger;

import android.content.Context;
import android.location.LocationManager;

/**
 * Created by Morten on 20.02.2016.
 */
public interface AppComponent {
    App application(); //provision method
    Context applicationContext(); //provision method
    LocationManager locationManager(); //provision method
}
