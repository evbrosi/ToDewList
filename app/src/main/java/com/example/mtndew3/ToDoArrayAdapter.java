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

public class ToDoArrayAdapter extends ArrayAdapter<ToDoItem> {
    private int resource;
    private ArrayList<ToDoItem> toDo;
    private LayoutInflater inflater;
    private SimpleDateFormat formatter;
    private String cardDueDate;

    public ToDoArrayAdapter(Context context, int resource, ArrayList<ToDoItem> objects) {
        super(context, resource, objects);
        this.resource = resource;
        this.toDo = objects;

        inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        formatter = new SimpleDateFormat("MM/dd/yyyy", Locale.getDefault());
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){

        ToDoItem toDoThing69 = toDo.get(position);
        LayoutInflater inflater = LayoutInflater.from(getContext());

        View toDoRow = inflater.inflate(R.layout.card_of_todo, parent, false);
        //you know how in my Card_of_todo.xml file says text: @title and everything?
        // this is how that little bit happens.

        if(convertView ==null){
            convertView= LayoutInflater.from(getContext()).inflate(R.layout.card_of_todo, parent, false);
        }

        TextView toDoTitle = (TextView) convertView.findViewById(R.id.todo_title);
        TextView toDoText = (TextView) convertView.findViewById(R.id.todo_text);
        TextView toDoDateModified = (TextView) convertView.findViewById(R.id.todo_date);
        TextView toDoDueDate = (TextView) convertView.findViewById(R.id.due_date);
        TextView toDoCategory = (TextView) convertView.findViewById(R.id.todo_category);

        toDoTitle.setText(toDoThing69.getTitle());
        toDoText.setText(toDoThing69.getText());
        toDoDueDate.setText(toDoThing69.getDueDate());
        toDoCategory.setText(toDoThing69.getCategory());

   //     cardDueDate = cardDueHour +":"+ cardDueMinute+" on "+ cardDueMonth + "/" + cardDueDay + "/" + cardDueYear;

        // we gotta make this beast (date) into a string! we do that with Formatter.
//        toDoDate.setText(formatter.format(toDoThing69.getDateModified()));

        return convertView;
    }

    public void updateAdapter (ArrayList<ToDoItem> item1){
        this.toDo = item1;
        super.notifyDataSetChanged();
    }
}