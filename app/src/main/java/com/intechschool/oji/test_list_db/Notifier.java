package com.intechschool.oji.test_list_db;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by 1 on 16/03/14.
 */
public class Notifier extends BroadcastReceiver {

    String todo;
    ToDoDB todo_DB;

    @Override
    public void onReceive(Context context, Intent intent) {


        setNotificatin(context);
        Toast.makeText(context, todo, Toast.LENGTH_SHORT).show();

    }

    void setNotificatin(Context context) {

        Intent intent = Intent.getIntent();
        String DB_todo = intent.getStringExtra("Alart_Context");
//        Log.d("DB_todo", DB_todo);

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context);

        mBuilder.setSmallIcon(R.drawable.icon_2)
                .setContentTitle(DB_todo)
                .setContentText(DB_todo + "の時間です");

        NotificationManagerCompat mManager = NotificationManagerCompat.from(context);
        mManager.notify(1, mBuilder.build());

    }

    public void sendTodo (String sendTodo) {

        Notifier.this.todo = sendTodo;
        Log.d("String" ,todo);
    }

}
