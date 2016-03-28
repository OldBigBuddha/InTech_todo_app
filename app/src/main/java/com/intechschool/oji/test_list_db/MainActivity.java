package com.intechschool.oji.test_list_db;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteCursor;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.widget.ExploreByTouchHelper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TimePicker;
import android.widget.Toast;
import android.text.format.Time;

import com.activeandroid.query.Select;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import de.timroes.android.listview.EnhancedListView;

public class MainActivity extends AppCompatActivity {

    EnhancedListView listView;
    EditText createToDo;
    Button btAdd;
    ToDoDB mDB;
    ToDoDB selectedItem;
    boolean is_Edit;
    String[] color_Code;
    Time mTime;
    int[] date;
    Calendar mCaledar;
    int year, month, day, hour, minute;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = (EnhancedListView) findViewById(R.id.listView);
        createToDo = (EditText) findViewById(R.id.editToDo);
        btAdd = (Button) findViewById(R.id.btCreate);

        is_Edit = false;
        mTime = new Time();
        mTime.setToNow();

        mCaledar = Calendar.getInstance();
        year  = mCaledar.get(Calendar.YEAR);
        month = mCaledar.get(Calendar.MONTH);
        day = mCaledar.get(Calendar.DAY_OF_MONTH);
        hour = mCaledar.get(Calendar.HOUR_OF_DAY);
        minute = mCaledar.get(Calendar.MINUTE);


//        setNotification_small();
//        setAlarm_small();

//        setmTime();

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
//                ListView list = (ListView) parent;
//                selectedItem = (ToDoDB) list.getItemAtPosition(position);
//                selectedItem.delete();
//                setToDoList()
                setTimePickerDialog();
                setDatePickerDialog();
                Log.d("Date",
                       "Yera:" + year +
                               "/Month:" + month +
                               "/Day" + day +
                               "//Hour" + hour +
                               "/Minute" + minute
                        );

                setAlarm_small();
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

    void setNotification_small () {
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getApplicationContext());

        mBuilder.setSmallIcon(R.drawable.icon_2)
                .setContentTitle("TEST")
                .setContentText("Test");

        NotificationManagerCompat mManager = NotificationManagerCompat.from(getApplicationContext());
        mManager.notify(1, mBuilder.build());
    }

    void setAlarm_small() {
        Intent intent = new Intent(MainActivity.this, Notifier.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(MainActivity.this, 0, intent, 0);

        Calendar alarmCalendar;
        alarmCalendar = MainActivity.this.mCaledar;

        alarmCalendar.set(Calendar.YEAR, year);
        alarmCalendar.set(Calendar.MONTH, month);
        alarmCalendar.set(Calendar.DAY_OF_MONTH, day);
        alarmCalendar.set(Calendar.HOUR_OF_DAY, hour);
        alarmCalendar.set(Calendar.MINUTE, minute);

        long calendar_long = alarmCalendar.getTimeInMillis();

        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, calendar_long, pendingIntent);

        Toast.makeText(this, "Date:" + calendar_long, Toast.LENGTH_SHORT).show();
    }

    public void setmTime() {
        if (mTime.hour > 12) {
            Toast.makeText(this, "AM", Toast.LENGTH_SHORT).show();
        }else if (mTime.hour <13) {
            Toast.makeText(this, "PM", Toast.LENGTH_SHORT).show();

        }
    }

    public void setDatePickerDialog() {


        final DatePickerDialog datePickerDialog;
        DatePickerDialog.OnDateSetListener onDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                MainActivity.this.year = year;
                MainActivity.this.month = monthOfYear;
                MainActivity.this.day = dayOfMonth;

                Log.d("DatePicker", "Year:" + MainActivity.this.year + "/Month:" + MainActivity.this.month + "/Day:" + MainActivity.this.day);

            }
        };

        datePickerDialog = new DatePickerDialog(this, onDateSetListener,year,month,day);

        datePickerDialog.show();

    }

    public void setTimePickerDialog() {

        TimePickerDialog timePickerDialog;
        TimePickerDialog.OnTimeSetListener onTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

                MainActivity.this.hour = hourOfDay;
                MainActivity.this.minute = minute;

                Log.d("TimePicker", "hour:" + MainActivity.this.hour + "/minute:" + MainActivity.this.minute);

            }
        };

        timePickerDialog = new TimePickerDialog(this, onTimeSetListener,hour,minute,true);

        timePickerDialog.show();

    }
}

