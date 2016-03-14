package com.intechschool.oji.test_list_db;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteCursor;
import android.support.v4.app.NotificationCompat;
import android.support.v4.widget.ExploreByTouchHelper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.activeandroid.query.Select;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import de.timroes.android.listview.EnhancedListView;

public class MainActivity extends Activity {

    EnhancedListView listView;
    EditText createToDo;
    Button btAdd;
    ToDoDB mDB;
    ToDoDB selectedItem;
    boolean is_Edit;
    Notification notification;
    NotificationManager notificationManager;
    NotificationCompat.Builder mBuilder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = (EnhancedListView) findViewById(R.id.listView);
        createToDo = (EditText) findViewById(R.id.editToDo);
        btAdd = (Button) findViewById(R.id.btCreate);

        is_Edit = false;

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(final AdapterView<?> PARENT, View view, final int POSITION, long id) {
                final CharSequence[] ITEMS = {"編集"};
                AlertDialog.Builder listDig = new AlertDialog.Builder(MainActivity.this);
                listDig.setTitle("メニュー");
                listDig.setItems(
                        ITEMS,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ListView list = (ListView) PARENT;
                                selectedItem = (ToDoDB) list.getItemAtPosition(POSITION);
                                createToDo.setText(selectedItem.todo);
                                is_Edit = true;
                                setToDoList();
                            }
                        }
                );

                listDig.create().show();


            }
        });


        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                ListView list = (ListView) parent;
                selectedItem = (ToDoDB) list.getItemAtPosition(position);
                selectedItem.delete();
                setToDoList();


                return false;
            }
        });

        listView.setDismissCallback(new de.timroes.android.listview.EnhancedListView.OnDismissCallback() {
            @Override
            public EnhancedListView.Undoable onDismiss(final EnhancedListView listview, final int POSITIN) {
                selectedItem = (ToDoDB) listview.getItemAtPosition(POSITIN);
                selectedItem.delete();
                setToDoList();

                return null;
            }

        });
        listView.enableSwipeToDismiss();

        mDB = new ToDoDB();
        notification = new Notification();

        setToDoList();

    }

    @Override
    public void onResume() {
        super.onResume();

        setToDoList();
    }

    public void onAdd(View v) {

        //アラートダイアログ
        final CharSequence[] ITEMS = {"高","中","低"};
        AlertDialog.Builder listDig = new AlertDialog.Builder(this);
        listDig.setTitle("優先度");
        listDig.setItems(
                ITEMS,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        if (is_Edit) {
                            selectedItem.todo = createToDo.getText().toString();
                            selectedItem.priority = which;
                            selectedItem.save();
                            is_Edit = false;

                        } else {
                            saveToDo(which);
                        }
                        createToDo.setText("");
                        setToDoList();

                    }
                }
        );
        listDig.create().show();

    }

    public void saveToDo(int priority) {
        //変数宣言
        ToDoDB saveDB = new ToDoDB();
        String save_Str =createToDo.getText().toString();
        saveDB.todo = save_Str;
        saveDB.priority = priority;

        /* notificationManager */
        mBuilder = new NotificationCompat.Builder(this);
        mBuilder.setSmallIcon(R.drawable.icon);
        mBuilder.setContentTitle(save_Str);
        mBuilder.setContentText(save_Str + "が追加されました");
        notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0,mBuilder.build());
        /**/

//        Intent i = new Intent(getApplicationContext(),MainActivity.class);
//        PendingIntent sender = PendingIntent.getBroadcast(this, 0, i, 0);
//
//        Calendar calendar = Calendar.getInstance();
//        calendar.setTimeInMillis(System.currentTimeMillis());
//        calendar.add(Calendar.SECOND, 10);
//
//        AlarmManager alarmManager = (AlarmManager)getSystemService(ALARM_SERVICE);
//        alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),sender);



        //日付設定
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.JAPANESE);
        saveDB.date = sdf.format(date);
        saveDB.save();

        setToDoList();

    }

    void setToDoList() {
        List<ToDoDB> display_list = new ArrayList();
        for (int i=0;i<3;i++) {
            List<ToDoDB> todoList = new Select().from(ToDoDB.class).where("priority = ?",i).execute();
            display_list.addAll(todoList);
        }

        ToDoListAdapter adapter = new ToDoListAdapter(
                getApplicationContext(),
                R.layout.todo_row,
                display_list
        );

        listView.setAdapter(adapter);
    }
}

