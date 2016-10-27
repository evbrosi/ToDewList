package com.example.mtndew3;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import static com.example.mtndew3.R.id.activity_to_do_card_create;
import static com.example.mtndew3.R.id.is_complete;

public class ToDoCardCreate extends AppCompatActivity {
    private int index;
    private EditText cardTitle;
    private EditText cardText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_to_do_card_create);

        cardTitle = (EditText) findViewById(R.id.card_title);
        cardText = (EditText) findViewById(R.id.card_text);

        Intent intent = getIntent();
        index = intent.getIntExtra(MainActivity.CARD_INDEX, -1);

        cardTitle.setText(intent.getStringExtra(MainActivity.CARD_TITLE));
        cardText.setText(intent.getStringExtra(MainActivity.CARD_TEXT));

        Button saveButton = (Button) findViewById(R.id.save_button);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = getIntent();
                intent.putExtra(MainActivity.CARD_INDEX, index);
                intent.putExtra(MainActivity.CARD_TEXT, cardTitle.getText().toString());
                intent.putExtra(MainActivity.CARD_TEXT, cardText.getText().toString());
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }

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
    }
}