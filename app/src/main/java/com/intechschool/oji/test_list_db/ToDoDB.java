package com.intechschool.oji.test_list_db;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

/**
 * Created by 1 on 16/03/05.
 */
@Table(name = "todo_table")
public class ToDoDB extends Model {

    @Column(name = "todo")
    public String todo;

    @Column(name = "priority")
    public int priority;

    @Override
    public String toString() {
        return todo;
    }

    public ToDoDB() {
        super();
    }



    public ToDoDB(String name) {
        super();
        this.todo = todo;
    }
}
