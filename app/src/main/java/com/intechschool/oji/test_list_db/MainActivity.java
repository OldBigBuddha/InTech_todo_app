package com.intechschool.oji.test_list_db;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteCursor;
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

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity {

    ListView listView;
    EditText createToDo;
    Button btAdd;
    ToDoDB mDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = (ListView) findViewById(R.id.listView);
        createToDo = (EditText) findViewById(R.id.editToDo);
        btAdd = (Button) findViewById(R.id.btCreate);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ListView list = (ListView) parent;
                ToDoDB selectedItem = (ToDoDB) list.getItemAtPosition(position);
                selectedItem.delete();
                setToDoList();

            }
        });

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
                        saveToDo(which);
                        createToDo.setText("");
                    }
                }
        );

        listDig.create().show();

    }

    public void saveToDo(int priority) {
        ToDoDB saveDB = new ToDoDB();
        saveDB.todo = createToDo.getText().toString();
        saveDB.priority = priority;
        saveDB.save();
        setToDoList();

    }

    void setToDoList() {
        List<ToDoDB> display_list = new ArrayList();
        for (int i=0;i<3;i++) {
            List<ToDoDB> todoList = new Select().from(ToDoDB.class).where("priority = ?",i).execute();
            display_list.addAll(todoList);
        }
//        List<ToDoDB> todoList = new Select().from(ToDoDB.class).where("priority = ?",0).execute();
//       display_list.addAll(todoList);
        ToDoListAdapter adapter = new ToDoListAdapter(
                getApplicationContext(),
                R.layout.todo_row,
                display_list
        );

        listView.setAdapter(adapter);
    }
}

