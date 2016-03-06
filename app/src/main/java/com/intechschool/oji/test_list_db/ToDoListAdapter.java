package com.intechschool.oji.test_list_db;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by 1 on 16/03/06.
 */
public class ToDoListAdapter extends ArrayAdapter {

    List<ToDoDB> TodoDB;
    private LayoutInflater layoutInflater;
    String[] priority = {"高","中","低"};
    String[] colorCode = {"#d11f1f","#c2b114","#469ee1"};

    public ToDoListAdapter(Context context, int textViewResourceId, List<ToDoDB> TodoDB) {
        super(context,textViewResourceId,TodoDB);
        layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = layoutInflater.inflate(R.layout.todo_row, null);

        ToDoDB item = (ToDoDB)getItem(position);
        TextView element_todo = (TextView) convertView.findViewById(R.id.todo);
        TextView element_priority = (TextView)convertView.findViewById(R.id.priority);


        element_todo.setText(item.todo);
        element_priority.setText(priority[item.priority]+"");
        element_priority.setBackgroundColor(Color.parseColor(colorCode[item.priority]));



        return convertView;
    }

}
