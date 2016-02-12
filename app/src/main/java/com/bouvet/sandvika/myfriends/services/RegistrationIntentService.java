package com.bouvet.sandvika.myfriends.services;

import android.app.IntentService;
import android.content.Intent;
import android.provider.SyncStateContract;
import android.support.v4.content.LocalBroadcastManager;

import com.bouvet.sandvika.myfriends.R;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;

import java.io.IOException;


public class RegistrationIntentService extends IntentService {

    public final static String ID_TOKEN_RECEIVED = "IdTokenReceived";
    public static final String TOKEN = "token";

    public RegistrationIntentService() {
        super("RegistrationIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        InstanceID instanceID = InstanceID.getInstance(this);
        try {
            String token = instanceID.getToken(getString(R.string.gcm_defaultSenderId), GoogleCloudMessaging.INSTANCE_ID_SCOPE, null);

            Intent broadCastIntent = new Intent(ID_TOKEN_RECEIVED).putExtra(TOKEN, token);
            LocalBroadcastManager.getInstance(this).sendBroadcast(broadCastIntent);

            System.out.println("Got token!" + token);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
