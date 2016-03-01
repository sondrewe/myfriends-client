package com.bouvet.sandvika.myfriends.position;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import com.bouvet.sandvika.myfriends.Dagger.App;
import com.bouvet.sandvika.myfriends.http.MyFriendsRestService;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * Created by Kristoffer on 01.03.2016.
 */
public class PositionPublishService extends Service implements GoogleApiClient.ConnectionCallbacks, LocationListener, GoogleApiClient.OnConnectionFailedListener {

    @Inject SharedPreferences sharedPreferences;
    @Inject Retrofit retrofit;

    private GoogleApiClient googleApiClient;
    private MyFriendsRestService service;


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;

    }

    @Override
    public void onCreate() {
        ((App) getApplication()).getNetComponent().inject(this);

        //region Google LocationServices Api connect
        googleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        //endregion

        service = retrofit.create(MyFriendsRestService.class);

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        googleApiClient.connect();
        return 0;
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        tryToConfigureLocationUpdates();
    }

    private void tryToConfigureLocationUpdates() {
        if (googleApiClient.isConnected()) {
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                Log.e("PERMISSION", "");
            } else {
                configureLocationUpdates();
            }
        }
    }

    private void configureLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        LocationRequest locationRequest = new LocationRequest();
        locationRequest.setInterval(5000);
        locationRequest.setFastestInterval(5000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, this);
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onLocationChanged(Location location) {
        String user = sharedPreferences.getString("userName",null);

        if (user != null) {

            Call<Void> locationChanged = service.updateLocation(user, new double[]{location.getLatitude(), location.getLongitude()});
            locationChanged.enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    Log.d("[onLocationChanged]", " success!!");
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    System.out.println("[onLocationChanged] failure!!");
                }
            });
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
