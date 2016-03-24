package com.intechschool.oji.test_list_db;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.widget.Toast;

/**
 * Created by 1 on 16/03/14.
 */
public class Notifier extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        setNotificatin(context);

        Toast.makeText(context, "Alart", Toast.LENGTH_SHORT).show();

    }

    void setNotificatin(Context context) {


//        Intent intent2 = new Intent(context, MainActivity.class);
//        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent2, 0);
//
//        NotificationManager mNotificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
//        Notification mNotification = new NotificationCompat.Builder(context)
//                .setSmallIcon(0)
//                .setTicker("TEST")
////                .setWhen(System.currentTimeMillis())
//                .setContentTitle("Nofication_test")
//                .setContentText("test")
//                .setContentIntent(pendingIntent)
//                .build();
//
//        mNotificationManager.cancelAll();
//        mNotificationManager.notify(0, mNotification);

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context);

        mBuilder.setSmallIcon(R.drawable.icon_2)
                .setContentTitle("TEST")
                .setContentText("Test");

        NotificationManagerCompat mManager = NotificationManagerCompat.from(context);
        mManager.notify(1, mBuilder.build());

    }

}
