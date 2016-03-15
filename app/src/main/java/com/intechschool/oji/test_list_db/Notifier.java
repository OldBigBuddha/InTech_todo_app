package com.intechschool.oji.test_list_db;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

/**
 * Created by 1 on 16/03/14.
 */
public class Notifier extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

//        Intent sendIntent = new Intent(context, MainActivity.class);
//        PendingIntent sender = PendingIntent.getActivity(context, 0, sendIntent, 0);
//
//        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context)
//                .setSmallIcon(0)
//                .setContentTitle("TEST")
//                .setContentText("TESTTEST");
//
//
//
//        mBuilder.setContentIntent(sender);
//
//        NotificationManager notificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
//        notificationManager.notify(0,mBuilder.build());

        Toast.makeText(context, "Alart", Toast.LENGTH_SHORT).show();


    }
}
