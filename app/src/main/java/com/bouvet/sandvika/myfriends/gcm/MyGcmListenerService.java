package com.bouvet.sandvika.myfriends.gcm;


import android.app.NotificationManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.app.NotificationCompat;

import com.bouvet.sandvika.myfriends.R;
import com.google.android.gms.gcm.GcmListenerService;

public class MyGcmListenerService extends GcmListenerService{

    public  static final String BroadCastRecieved = "BroadCastRecieved";
    public  static final String BroadCastRecievedMessage = "BroadCastRecievedMessage";
    @Override
    public void onMessageReceived(String from, Bundle data) {
        // TODO: Legg til logikk for å vise varsel
        System.out.println("[onMessageReceived]");
        super.onMessageReceived(from, data);
        Intent broadCastIntent = new Intent(BroadCastRecieved).putExtra(BroadCastRecievedMessage, data.getString("message"));
        LocalBroadcastManager.getInstance(this).sendBroadcast(broadCastIntent);
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.common_plus_signin_btn_icon_dark)
                        .setContentTitle("MyFriends")
                        .setContentText(data.getString("message"));

        // Sets an ID for the notification
        int mNotificationId = 001;
// Gets an instance of the NotificationManager service
        NotificationManager mNotifyMgr =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
// Builds the notification and issues it.
        mNotifyMgr.notify(mNotificationId, mBuilder.build());

    }
}
