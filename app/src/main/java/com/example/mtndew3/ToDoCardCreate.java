package com.example.mtndew3;

import android.app.FragmentTransaction;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;


public class ToDoCardCreate extends AppCompatActivity {
    private int index;
    private EditText cardTitle;
    private EditText cardText;
    //    private EditText cardDueYear;
//    private EditText cardDueMonth;
//    private EditText cardDueDay;
//    private EditText cardDueMinute;
//    private EditText cardDueHour;
    // I really don't want to talk about WHAT A HORRIBLE MESS THE DATE PICK HORRIBLE MESS WAS.
    private EditText cardCategory;
    private EditText cardDueDate;
    private Button saveButt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_to_do_card_create);

        cardTitle = (EditText) findViewById(R.id.create_title);
        cardText = (EditText) findViewById(R.id.create_text);
        cardCategory = (EditText) findViewById(R.id.create_category);
        //date modified placeholder.
        cardDueDate = (EditText) findViewById(R.id.create_due_date);

        saveButt = (Button)findViewById(R.id.save_button1);

        Intent intent = getIntent();

        cardTitle.setText(intent.getStringExtra("title"));
        cardText.setText(intent.getStringExtra("text"));
        cardCategory.setText(intent.getStringExtra("category"));
        // date modified placeholder. I was just consistently getting confused.
        cardDueDate.setText(intent.getStringExtra("dueDate"));
        index = intent.getIntExtra("index", -1);

        //   CheckBox isItDone = (checkbox) findViewById(R.id.is_complete);

        // this is throwing a null pointer error- so i need to check what all I'm defining here.
        saveButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = getIntent();
                intent.putExtra("title", cardTitle.getText().toString());
                intent.putExtra("text", cardText.getText().toString());
                intent.putExtra("category", cardCategory.getText().toString());
                // date modified placeholder. I was just consistently getting confused.
                intent.putExtra("dueDate", cardDueDate.getText().toString());
                intent.putExtra("index", index);
                setResult(RESULT_OK, intent);
                finish();
            }
        });

        //for the stupid date picker horrible mess to pick a date it has to hit this sweet little baby.
        EditText dueDate = (EditText) findViewById(R.id.create_due_date);
        dueDate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    DatePickerHorribleMess sayMyDate = new DatePickerHorribleMess(v);
                    FragmentTransaction ft = getFragmentManager().beginTransaction();
                    sayMyDate.show(ft, "DatePicked");
                }
            }
        });
    }
}


//    Snackbar.make(v, "It's mtn dew. Not mtn don't.", Snackbar.LENGTH_LONG)
//            .setAction("Action", null).show();

//        ArrayAdapter.createFromResource(this, R.array.categories, R.layout.support_simple_spinner_dropdown_item);
//            onOptionsItemSelected()

/*
    @Override
    public void onBackPressed() {
        setResult(RESULT_CANCELED);
        super.onBackPressed();
    }

    public boolean onCreateOptionMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_note_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }*/