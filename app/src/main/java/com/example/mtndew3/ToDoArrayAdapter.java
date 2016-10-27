package com.example.mtndew3;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by eaglebrosi on 10/26/16.
 */

public class ToDoArrayAdapter extends ArrayAdapter<ToDoConstructor> {
    private int resource;
    private ArrayList<ToDoConstructor> toDo;
    private LayoutInflater inflater;
    private SimpleDateFormat formatter;

    public ToDoArrayAdapter(Context context, int resource, ArrayList<ToDoConstructor> objects) {
        super(context, resource, objects);
        this.resource = resource;
        this.toDo = objects;
        inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        formatter = new SimpleDateFormat("MM/dd/yyyy", Locale.getDefault());
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){

        View toDoRow = inflater.inflate(resource, parent, false);
        //you know how in my Card_of_todo.xml file says text: @title and everything?
        // this is how that little bit happens.
        TextView toDoTitle = (TextView)toDoRow.findViewById(R.id.todo_title);
        TextView toDoText = (TextView)toDoRow.findViewById(R.id.todo_text);
        TextView toDoDate = (TextView)toDoRow.findViewById(R.id.todo_date);

        ToDoConstructor aToDo = toDo.get(position);

        toDoTitle.setText(aToDo.getTitle());
        toDoText.setText(aToDo.getText());
        // we gotta make this beast (date) into a string! we do that with Formatter.
        toDoDate.setText(formatter.format(aToDo.getDate()));

        return toDoRow;
    }

    public void updateAdapter (ArrayList<ToDoConstructor> toDo){
        this.toDo = toDo;
        super.notifyDataSetChanged();
    }
}
