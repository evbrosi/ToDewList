package com.example.mtndew3;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.w3c.dom.Text;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import static android.R.attr.data;
import static com.example.mtndew3.R.array.categories;

public class MainActivity extends AppCompatActivity {
    // so each to do activity gets put into a card- and this is like the card catalogue.
    // That puts each card under another.
    private ListView cardListView;

    private ArrayList<ToDoItem> toDoArrayList;
    private Gson gson;

    List<ToDoItem> cardLists;
    private SharedPreferences toDoPrefs;
    private ToDoArrayAdapter toDoArrayAdapter;
    String filename = "ToDoItemsFile";
    //I need this to clear the bowels of the listed cards.
    private ArrayList<Object> allItems = new ArrayList<>();
    private CategoryAdapter catAdapter;
    // gotta make the cat numb so that it has a number associated with it that puts it in the right
    //place when an idiot like me creates categories and slowly melts inside.
    private int catNumb;
    private List<Category> categories = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Only used to figure out if this is the first run.
        toDoPrefs = getPreferences(Context.MODE_PRIVATE);

        gson = new Gson();
        setupToDoCards();

        // YES! THIS IS NOW THE PROBLEM! HA HA HA HA HA! YES! It's still a null pointer error.
        // ha ha ha ha. I mark it out and it shows off my horrible business. This project. This life.
        // So here's what's happening right now. If I sort by cards- it freaks out on me(easy fix- i
        // need todo look at all the itmes it sorts by.
        // But it only shows me the stuff at the beginning. If I click the button- it dies. it completely
        // dies. Also, it put my mountain dew note in each category. So I have a value and it put it in each.
        // isn't that weird?

        //Collections.sort(cardLists);

        flushOutAndPoopItAllBackAgain();

        cardListView = (ListView) findViewById(R.id.list_view);
        flushOutAndPoopItAllBackAgain();
        //Lets flush the old info and then refills with old info because that makes the most sense.

        //this is now a Null Pointer Exception, except we know it's not.
        //there is hatred in this life. There is Donald Trump. There is a null reference error.
        // It points to this code. Infinite sadness and maddness. No sleep. Nightmares about this
        // project. Daydreams of getting it done. Hatred in my heart for everyone who is having a
        // happy halloween weekend.
//        toDoArrayAdapter = new ToDoArrayAdapter(this, R.layout.card_of_todo, toDoArrayList);

        catAdapter = new CategoryAdapter(this, allItems);
        cardListView.setAdapter(catAdapter);

//        cardListView.setAdapter(toDoArrayAdapter);
//        cardListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

        cardListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                allItems.get(position);
                ToDoItem card = (ToDoItem) allItems.get(position);

                Intent intent = new Intent(MainActivity.this, ToDoCardCreate.class);
                intent.putExtra("Title", card.getTitle());
                intent.putExtra("Text", card.getText());
                intent.putExtra("category", card.getCategory());
                // date modified???
                // also i need TODO put cat call but I must figure out this NULL pointer exception.
                intent.putExtra("catCall", card.getCatCall());
                intent.putExtra("dueDate", card.getDueDate());
                intent.putExtra("Index", position);

                startActivityForResult(intent, 1);
            }
        });

//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);

        cardListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                android.support.v7.app.AlertDialog.Builder alertBuilder = new android.support.v7.app.AlertDialog.Builder(MainActivity.this);
                alertBuilder.setTitle("Delete");
                alertBuilder.setMessage("You sure?");
                alertBuilder.setNegativeButton("Cancel", null);
                alertBuilder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ToDoItem cards = (ToDoItem) allItems.get(position);
                        categories.get(catNumb).cards.remove(cards);
                        allItems.remove(position);
                        deleteFile(cards.getTitle());
                        catAdapter.notifyDataSetChanged();
                        writeToDos();
                    }
                });
                alertBuilder.create().show();
                return true;
            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, ToDoCardCreate.class);
                startActivityForResult(intent, 1);
//                Snackbar.make(view, "It's mtn dew. Not mtn don't.", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {

            int index = data.getIntExtra("Index", -1);

            ToDoItem toDoCard = new ToDoItem(data.getStringExtra("title"),
                    data.getStringExtra("text"),
                    data.getStringExtra("category"),
                    new Date(),
                    data.getStringExtra("due_date"),
                    data.getStringExtra("catCall"));
            // todo put in the cat call.
            switch (data.getStringExtra("category")) {
                //todo put a .toDowncase thingy on the editText for category.
                case ("maritial"):
                    catNumb = 0;
                    break;
                case ("personal"):
                    catNumb = 1;
                    break;
                case ("professional"):
                    catNumb = 2;
                    break;
                // i know that this probably will break the ability to add categories but you know. Things.
            }
// I GOTTA GO BACK AND PUT IN THE CATEGOIRES
            categories.get(catNumb).cards.add(toDoCard);
            writeToDos();
            flushOutAndPoopItAllBackAgain();
            catAdapter.notifyDataSetChanged();
            writeToDos();
//            toDoArrayAdapter.updateAdapter(toDoArrayList);
        }
    }

    public void setupToDoCards() {
        toDoArrayList = new ArrayList<>();

        File filesDir = this.getFilesDir();
        File todoFile = new File(filesDir + File.separator + filename);
        if (todoFile.exists()) {
            readTodos(todoFile);
        } else {
            //gotta get all these sweet categories into practice you know. Actually I'm just pretending that
            // there are three categories into hitting them sweet parameters.
            categories.add(new Category("Personal", new ArrayList<ToDoItem>()));
            categories.add(new Category("Maritial", new ArrayList<ToDoItem>()));
            categories.add(new Category("Professional", new ArrayList<ToDoItem>()));

            for (int i = 0; i < categories.size(); i++) {
                categories.get(i).cards.add(new ToDoItem("Diet Mountain Dew",
                        "This is your first To dew",
                        // i leave this empty because it'll populate all three categories
                        " ",
                        new Date(),
                        "01/13/1984",
                        //left the cat call empty
                        ""));

//                    categories.get(i).notes.add(new ToDoItem("Note 1", "This is a note", new Date(), "work", "mipmap-hdpi/ic_launcher.png", "1/1/1999", ""));
//                    categories.get(i).notes.add(new T("Note 2", "This is a note", new Date(), "work", "mipmap-hdpi/ic_launcher.png", "1/1/1999", ""));
//                    categories.get(i).notes.add(new Note("Note 3", "This is a note", new Date(), "work", "mipmap-hdpi/ic_launcher.png", "1/1/1999", ""));
            }
            writeToDos();
        }
    }

    /*
            File todoFile = new File(filesDir + File.separator + filename);

            if (todoFile.exists()){
                readTodos(todoFile);

                for (ToDoItem card : toDoList) {
                    Log.d("To do read from file", card.getTitle() + "" + card.getText());
                    toDoArrayList.add(card);
                }
            } else {
                toDoList.add(new ToDoItem("Mountain Dew", "This is your first To dew", new Date(), "01/13/1984", "personal"));
                writeToDos();
            }
        }
    */
    private void readTodos(File todoFile) {
        FileInputStream inputStream = null;
        String todosText = "";
        try {
            inputStream = openFileInput(todoFile.getName());
            byte[] input = new byte[inputStream.available()];
            while (inputStream.read(input) != -1) {
            }
            todosText += new String(input);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            Type collectType = new TypeToken<List<Category>>() {
            }.getType();
            List<Category> categoryList = gson.fromJson(todosText, collectType);
            categories = new LinkedList(categoryList);
        }
//            ToDoItem[] noteList = gson.fromJson(todosText, ToDoItem[].class);
    }

    private void writeToDos() {
        FileOutputStream outputStream = null;
        try {
            outputStream = openFileOutput(filename, Context.MODE_PRIVATE);
            String json = gson.toJson(toDoArrayList);
            byte[] bytes = json.getBytes();
            outputStream.write(bytes);

            outputStream.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                outputStream.close();
            } catch (Exception ignored) {
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_note_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int cardNumb = item.getItemId();
        if (cardNumb == R.id.action_settings) {
            sortCategory(1);
            return true;
        }else if (cardNumb == R.id.action_personal){
            sortCategory(2);
        }
        return super.onOptionsItemSelected(item);
    }


    private void flushOutAndPoopItAllBackAgain() {
        allItems.clear();
        for(int i = 0; i < categories.size(); i++) {
            allItems.add(categories.get(i).getName());
            for(int j = 0; j < categories.get(i).cards.size(); j++) {
                allItems.add(categories.get(i).cards.get(j));
            }
        }
    }

    public void sortCategory(int catNum){
        for (ToDoItem card : toDoArrayList){
            if (card.getCategory().equals("work") && catNum == 1){
                Log.d("*****", card.getTitle());

                toDoArrayAdapter.updateAdapter(toDoArrayList);
            }else if (card.getCategory().equals("personal") && catNum == 2){
                Log.d("***Sorted!***", card.getTitle());
            }
        }
    }



// I threw these last two bits and pieces into it because i felt like it might work or some crap.
// it doesn't. but I left it in. thinking something dumb.
    @Override
    public void onSaveInstanceState(Bundle savedState){
        //Do whatever you want to do when the application stops.
        File[] filesDir = MainActivity.this.getFilesDir().listFiles();
        for(File file : filesDir){
            file.delete();
        }
        for (ToDoItem card : toDoArrayList){
            myLifeIsOverButThisMightSave(card);
        }
    }


    private void myLifeIsOverButThisMightSave(ToDoItem card) {
        //  if()

        FileOutputStream outputStream = null;
        try {
            outputStream = openFileOutput(card.getTitle(), Context.MODE_PRIVATE);
            outputStream.write(card.getText().getBytes());
            outputStream.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                outputStream.close();
            } catch (IOException ioe) {
            } catch (NullPointerException npe) {
            } catch (Exception e) {
            }
        }
    }
}