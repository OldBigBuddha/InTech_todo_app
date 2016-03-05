package com.intechschool.oji.test_list_db;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.activeandroid.query.Select;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    ListView listView;
    EditText createToDo;
    Button btAdd;
    ToDoDB mDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = (ListView)findViewById(R.id.listView);
        createToDo = (EditText)findViewById(R.id.editToDo);
        btAdd = (Button)findViewById(R.id.btCreate);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ListView list = (ListView)parent;
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
        saveToDo();
        createToDo.setText("");
    }

    public void saveToDo() {
        mDB.todo = createToDo.getText().toString();
        mDB.save();
        mDB = new ToDoDB();
        setToDoList();

    }

    void setToDoList() {
        List<ToDoDB> todoList = new Select().from(ToDoDB.class).execute();
        ArrayAdapter<ToDoDB> adapter = new ArrayAdapter<>(
                getApplicationContext(),
                R.layout.todo_row,
                todoList
        );

        listView.setAdapter(adapter);
    }
}

