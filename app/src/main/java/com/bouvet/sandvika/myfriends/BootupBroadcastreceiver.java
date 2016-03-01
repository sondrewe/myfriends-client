package com.bouvet.sandvika.myfriends;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.bouvet.sandvika.myfriends.position.PositionPublishService;

/**
 * Created by Kristoffer on 01.03.2016.
 */
public class BootupBroadcastreceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent startServiceIntent = new Intent(context, PositionPublishService.class);
        context.startService(startServiceIntent);
    }
}
