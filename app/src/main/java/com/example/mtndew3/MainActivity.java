package com.example.mtndew3;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.gson.Gson;

import com.google.gson.annotations.SerializedName;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import static com.example.mtndew3.R.layout.activity_main;

public class MainActivity extends AppCompatActivity {
    public static final String CARD_INDEX = "com.elevenfifty.www.elevennote.NOTE_INDEX";
    public static final String CARD_TITLE = "com.elevenfifty.www.elevennote.NOTE_TITLE";
    public static final String CARD_TEXT = "com.elevenfifty.www.elevennote.NOTE_TEXT";
    public static final String DATE = "com.elevenfifty.www.elevennote.NOTE_TEXT";

    private ListView notesList;
    private ArrayList<ToDoConstructor> toDoArrayList;
    private Gson gson;
    List<ToDoConstructor> toDoList = new ArrayList<>();
    private SharedPreferences toDoPrefs;
    private ToDoArrayAdapter toDoArrayAdapter;
    String filename = "ToDoItemsFile";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toDoPrefs = getPreferences(Context.MODE_PRIVATE);

        gson = new Gson();
        setupToDoCards();

        notesList = (ListView)findViewById(R.id.listView);
        toDoArrayAdapter = new ToDoArrayAdapter(this, R.layout.card_of_todo, toDoArrayList);
        notesList.setAdapter(toDoArrayAdapter);
        notesList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ToDoConstructor note = toDoArrayList.get(position);

                Intent intent = new Intent(MainActivity.this, ToDoCardCreate.class);
                intent.putExtra("Title", note.getTitle());
                intent.putExtra("Text", note.getText());
                intent.putExtra("Index", position);

                startActivityForResult(intent, 1);
            }
        });
//      i haven't set this up yet- i'll have to go into the constructor and add compariable notes to it
//        Collections.sort(arrayOfList);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

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

        notesList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                ToDoConstructor toDoConstructor = toDoArrayList.get(position);

                Intent intent = new Intent(MainActivity.this, ToDoCardCreate.class);

                intent.putExtra("toDoTitle", toDoConstructor.getTitle());
                intent.putExtra("completionDueDate", toDoConstructor.getDate());
                intent.putExtra("category", toDoConstructor.getCategory());

                intent.putExtra("Index", position);

                startActivityForResult(intent, -1);
            }
        });

        notesList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                android.support.v7.app.AlertDialog.Builder alertBuilder = new android.support.v7.app.AlertDialog.Builder(MainActivity.this);
                alertBuilder.setTitle("Delete");
                alertBuilder.setMessage("You sure?");
                alertBuilder.setNegativeButton("Cancel", null);
                alertBuilder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ToDoConstructor note = toDoArrayList.get(position);
                        deleteFile("##" + note.getTitle());
                        toDoArrayList.remove(position);
                        toDoArrayAdapter.updateAdapter(toDoArrayList);
                    }
                });
                alertBuilder.create().show();
                return true;
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            int index = data.getIntExtra("Index", -1);
            ToDoConstructor todoConstructor = new ToDoConstructor(data.getStringExtra("title"), data.getStringExtra("text"),
                    new Date(), data.getStringExtra("dueDate"));
            if (index < 0 || index > toDoArrayList.size() - 1) {
                toDoArrayList.add(todoConstructor);
                toDoArrayAdapter.updateAdapter(toDoArrayList);
            }
            writeToDos();
        }
    }

    //   private ListView notesList;
    // private ArrayList<ToDoConstructor> arrayOfList;

    public void setupToDoCards() {
        toDoArrayList = new ArrayList<>();

        File filesDir = this.getFilesDir();
        File todoFile = new File(filesDir + File.separator + filename);

        if (todoFile.exists()){
            readTodos(todoFile);

            for (ToDoConstructor card : toDoList) {
                Log.d("To do read from file", card.getTitle() + "" + card.getText());
                toDoArrayList.add(card);
            }
        } else {
            toDoList.add(new ToDoConstructor("Mountain Dew", "This is your first To dew", new Date(), "01/13/1984"));
            writeToDos();
        }

        toDoArrayList = new ArrayList<>();
        if (toDoPrefs.getBoolean("firstRun", true)) {
            SharedPreferences.Editor editor = toDoPrefs.edit();
            editor.putBoolean("firstRun", false);
            editor.apply();
            ToDoConstructor toDo1 = new ToDoConstructor("Mountain Dew", "This is your first To dew", new Date(), "01/13/1984");
            toDoArrayList.add(toDo1);

            for (ToDoConstructor aToDo: toDoArrayList) {
                writeToDos();
            }
        } else
        {
            readTodos(todoFile);
        }
    }

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
            ToDoConstructor[] noteList = gson.fromJson(todosText, ToDoConstructor[].class);
            toDoList = Arrays.asList();
        }
    }
/*
    private void returnFiles(){
        File[] filesDir = this.getFilesDir().listFiles();
        for (File file : filesDir) {
            FileInputStream inputStream = null;
            String title = file.getName();
            if (!title.startsWith("##")){
                continue;
            }else{
                title=title.substring(2,title.length());
            }
            Date date = new Date(file.lastModified());
            String text = "";
            try {
                inputStream = openFileInput("##" + title);
                byte[] input = new byte[inputStream.available()];
                while (inputStream.read(input) != -1) {
                }
                text += new String(input);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            toDoArrayList.add(new ToDoConstructor(title, text, date, null));
        }
    }

            arrayOfList = new ArrayList<>();
        if (toDoPrefs.getBoolean("firstRun", true)){
            SharedPreferences.Editor editor = toDoPrefs.edit();
            editor.putBoolean("firstRun", false);
            editor.apply();

            // this just shows what the card looks like. It populates with true because it's done.
            ToDoConstructor toDo1 = new ToDoConstructor("Mountain Dew", "This is your first To do", new Date(), true);
            arrayOfList.add(toDo1);

            for (ToDoConstructor aToDo: arrayOfList) {
                writeFile(aToDo);
            }
        } else {
            File[] filesDir = this.getFilesDir().listFiles();
            for (File file : filesDir) {
                FileInputStream inputStream = null;
                String title = file.getName();
                Date date = new Date(file.lastModified());
                String text ="";
                try {
                    inputStream = openFileInput(title);
                    byte[] input = new byte[inputStream.available()];
                    while (inputStream.read(input) != -1){}
                    text += new String (input);
                    // open file input required this exception
                    } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    // input stream available requires this exception
                    } catch (IOException e) {
                    e.printStackTrace();
                    } finally {
                        ToDoConstructor aToDo = gson.fromJson(text, ToDoConstructor.class);
                    aToDo.setDate(date);
                    arrayOfList.add(aToDo);
                    try {
                        inputStream.close();
                    } catch (Exception ignored) {}
                }
            }
        }
    }
    */

    private void writeToDos () {
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
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


/*
            outputStream.write(gson.toJson(aToDo).getBytes());
            outputStream.flush();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                outputStream.close();
            } catch (IOException ioe) {
            } catch (NullPointerException npe){
            }catch (Exception e){
            }
        }
    }

    */

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_note_detail, menu);
        return true;
    }

    @Override
    public void onSaveInstanceState(Bundle savedState){
        //Do whatever you want to do when the application stops.
        File[] filesDir = MainActivity.this.getFilesDir().listFiles();
        for(File file : filesDir){
            file.delete();
        }
        for (ToDoConstructor note : toDoArrayList){
 //           writeFile(note);
        }
    }
}