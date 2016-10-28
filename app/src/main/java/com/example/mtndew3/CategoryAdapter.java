package com.example.mtndew3;

import android.content.Context;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import static android.R.attr.resource;

/**
 * Created by eaglebrosi on 10/28/16.
 */

public class CategoryAdapter extends ArrayAdapter<Category> {
    private ArrayList<Category> items;
    private LayoutInflater layoutInflater;

    private static final int TYPE_TODOCONTSTURCTOR = 0;
    private static final int TYPE_CATEGORY = 1;

    public CategoryAdapter(Context context, int resource, ArrayList<Category> object){
        super(context, resource, object);
        this.items = object;
        this.layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return items.size();
    }

    // Get the position in our array we are at
    @Override
    public long getItemId(int position) {
        return position;
    }

    // Get the position of our item in the array
    @Override
    public Category getItem(int position) {
        return items.get(position);
    }
/*
    // Determine the amount of separate views our adapter will need to handle
    @Override
    public int getViewTypeCount() {
        return 2;
    }

    // Determine the type of view we will need to use for the position in our item array
    @Override
    public int getItemViewType(int position) {
        if(getItem(position) instanceof ToDoItem) {
            return TYPE_TODOCONTSTURCTOR;
        }
        return TYPE_CATEGORY;
    }

    // Enable or disabled the ability to interact with the view
    @Override public boolean isEnabled(int position) {
        return false;
    }

    // Determine the type of view we are creating, then insert the necessary info from our
    // items array
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        int type = getItemViewType(position);
        if(convertView == null) {
            if(type == TYPE_TODOCONTSTURCTOR) {
                convertView = layoutInflater.inflate(R.layout.card_of_todo, parent, false);
            } else if (type == TYPE_CATEGORY) {
                convertView = layoutInflater.inflate(R.layout.category_view, parent, false);
            }
        }

        if(type == TYPE_TODOCONTSTURCTOR) {
            ContactsContract.CommonDataKinds.Note note = (ToDoItem)getItem(position);
            TextView title = (TextView) convertView.findViewById(R.id.card_title);
            TextView text = (TextView) convertView.findViewById(R.id.card_text);
            TextView date = (TextView) convertView.findViewById(R.id.date);
            title.setText(note.getTitle());
            text.setText(note.getText());
            date.setText(note.getDate().toString());
        } else if(type == TYPE_CATEGORY) {
            String categoryName = (String) getItem(position);
            TextView category = (TextView) convertView.findViewById(R.id.category);
            category.setText(categoryName);
        }
        return convertView;
    }
    */
}
