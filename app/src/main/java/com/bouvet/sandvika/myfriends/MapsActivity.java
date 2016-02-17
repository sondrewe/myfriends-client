package com.bouvet.sandvika.myfriends;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;

import com.bouvet.sandvika.myfriends.model.User;
import com.bouvet.sandvika.myfriends.http.MyFriendsRestService;
import com.bouvet.sandvika.myfriends.gcm.RegistrationIntentService;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MapsActivity extends AppCompatActivity implements LocationListener, OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, ActivityCompat.OnRequestPermissionsResultCallback {

    private final static String BASE_URL = "http://10.4.100.28:8080";

    private GoogleApiClient googleApiClient;
    private BroadcastReceiver messageReceiver;
    private MyFriendsRestService service;
    private User user;
    private GoogleMap googleMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        //region Map init
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        //endregion

        //region Google LocationServices Api connect
        googleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        //endregion

        //region Retrofit init
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        service = retrofit.create(MyFriendsRestService.class);
        //endregion

        //region MessageReceiver init
        messageReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                System.out.println("Received broadcastmessage from Registration Intent");

                String key = intent.getStringExtra(RegistrationIntentService.TOKEN);
                user = createUser(key);
            }
        };
        //endregion

        Intent intent = new Intent(this, RegistrationIntentService.class);
        startService(intent);

    }

    private User createUser(String regKey) {

//        final User user = new User("kris", "Kristoffer", "Mysen", regKey);
        final User user = new User("sondrew", "Sondre", "Engell", regKey);

        Call<Void> call = service.createUser(user);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                System.out.println("[createUser] Success!!!");
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                System.out.println("[createUser] Failed!!!!!");
            }
        });

        return user;

    }

    @Override
    protected void onStart() {
        googleApiClient.connect();
        super.onStart();
    }

    @Override
    protected void onResume() {
        IntentFilter intentFilter = new IntentFilter(RegistrationIntentService.ID_TOKEN_RECEIVED);
        LocalBroadcastManager.getInstance(this).registerReceiver(messageReceiver, intentFilter);

        super.onResume();
    }

    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(messageReceiver);

        super.onPause();
    }

    @Override
    protected void onStop() {
        googleApiClient.disconnect();
        super.onStop();
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;

        tryToConfigureLocationUpdates();
    }

    private void tryToConfigureLocationUpdates() {
        if (googleMap != null && googleApiClient.isConnected()) {
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 0);
            } else {
                configureLocationUpdates();
            }
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        tryToConfigureLocationUpdates();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode != 0) {
            throw new RuntimeException("Unknown request code received");
        }
        for (String permission : permissions) {
            if (android.Manifest.permission.ACCESS_FINE_LOCATION.equals(permission)) {
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
        this.googleMap.setMyLocationEnabled(true);
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(final Location location) {

        if (user != null) {

            Call<Void> locationChanged = service.updateLocation(user.getUserName(), new double[]{location.getLatitude(), location.getLongitude()});
            locationChanged.enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    System.out.println("[onLocationChanged] success!!");
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    System.out.println("[onLocationChanged] failure!!");
                }
            });
        }

    }
}
