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

        ToDoConstructor toDoThing69 = toDo.get(position);
        LayoutInflater inflater = LayoutInflater.from(getContext());

        View toDoRow = inflater.inflate(R.layout.card_of_todo, parent, false);
        //you know how in my Card_of_todo.xml file says text: @title and everything?
        // this is how that little bit happens.

        if(convertView ==null){
            convertView= LayoutInflater.from(getContext()).inflate(R.layout.card_of_todo, parent, false);
        }

        TextView toDoTitle = (TextView) convertView.findViewById(R.id.todo_title);
        TextView toDoText = (TextView) convertView.findViewById(R.id.todo_text);
        TextView toDoDate = (TextView) convertView.findViewById(R.id.todo_date);
        TextView toDoDueDate = (TextView) convertView.findViewById(R.id.due_date);

        toDoTitle.setText(toDoThing69.getTitle());
        toDoText.setText(toDoThing69.getText());
        toDoDueDate.setText(toDoThing69.getDueDate());
        // we gotta make this beast (date) into a string! we do that with Formatter.
        toDoDate.setText(formatter.format(toDoThing69.getDate()));

        return convertView;
    }

    public void updateAdapter (ArrayList<ToDoConstructor> item1){
        this.toDo = item1;
        super.notifyDataSetChanged();
    }
}