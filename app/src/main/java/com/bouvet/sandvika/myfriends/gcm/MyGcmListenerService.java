package com.bouvet.sandvika.myfriends.gcm;


import android.app.NotificationManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.bouvet.sandvika.myfriends.R;
import com.google.android.gms.gcm.GcmListenerService;

public class MyGcmListenerService extends GcmListenerService {

    static final String TAG= "MyGcmListenerService";
    public static final String BroadCastRecieved = "BroadCastRecieved";
    public static final String BroadCastRecievedMessage = "BroadCastRecievedMessage";

    public static final String PositionsRecieved = "PositionsRecieved";
    public static final String PositionsRecievedPosition = "PositionsRecievedPosition";
    public static final String PositionsRecievedFrom = "PositionsRecievedFrom";

    @Override
    public void onMessageReceived(String from, Bundle data) {
        // TODO: Legg til logikk for å vise varsel
        String type = (String) data.get("type");
        String userName = (String) data.get("userName");
        if(type!= null) {
            if (type.equalsIgnoreCase("POSITION_NOTIFICATION")) {
                Log.v(TAG,"[onMessageReceived] - Got POSITION_NOTIFICATION from " + userName);
                InAppPositionNotification(data);
            } else if (type.equalsIgnoreCase("PROXIMITY_NOTIFICATION")) {
                Log.v(TAG,"[onMessageReceived] - Got PROXIMITY_NOTIFICATION from " + userName);
                super.onMessageReceived(from, data);
                InAppProximityNotification(data);
                ShowNotificationBar(data);
            }
        }
    }
    private void InAppPositionNotification(Bundle data) {
        Intent broadCastIntent = new Intent(PositionsRecieved).putExtra(PositionsRecievedPosition, data.getString("position"));
        broadCastIntent.putExtra(PositionsRecievedFrom, data.getString("userName"));
        LocalBroadcastManager.getInstance(this).sendBroadcast(broadCastIntent);
    }

    private void InAppProximityNotification(Bundle data) {
        Intent broadCastIntent = new Intent(BroadCastRecieved).putExtra(BroadCastRecievedMessage, data.getString("message"));
        LocalBroadcastManager.getInstance(this).sendBroadcast(broadCastIntent);
    }

    private void ShowNotificationBar(Bundle data) {
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.common_plus_signin_btn_icon_dark)
                        .setContentTitle("MyFriends")
                        .setContentText(data.getString("message"));

        // Sets an ID for the notification
        int mNotificationId = 1;
// Gets an instance of the NotificationManager service
        NotificationManager mNotifyMgr =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
// Builds the notification and issues it.
        mNotifyMgr.notify(mNotificationId, mBuilder.build());
    }
}
