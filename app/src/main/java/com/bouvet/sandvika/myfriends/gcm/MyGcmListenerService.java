package com.bouvet.sandvika.myfriends.gcm;


import android.os.Bundle;

import com.google.android.gms.gcm.GcmListenerService;

public class MyGcmListenerService extends GcmListenerService{

    @Override
    public void onMessageReceived(String from, Bundle data) {
        // TODO: Legg til logikk for å vise varsel
        System.out.println("[onMessageReceived]");
        super.onMessageReceived(from, data);
    }
}
