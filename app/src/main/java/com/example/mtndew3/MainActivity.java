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

    private ListView notesList;
    private ArrayList<ToDoItem> toDoArrayList;
    private Gson gson;
    List<Category> categories = new ArrayList<>();
    private SharedPreferences toDoPrefs;
    private ToDoArrayAdapter toDoArrayAdapter;
    String filename = "ToDoItemsFile";
    TextView toDoTitle;
    TextView toDoDate;
    TextView toDoText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toDoPrefs = getPreferences(Context.MODE_PRIVATE);
        toDoTitle = (TextView) findViewById(R.id.todo_title);
        toDoDate = (TextView) findViewById(R.id.due_date);
        toDoText = (TextView) findViewById(R.id.todo_text);

        gson = new Gson();
//        setupToDoCards();

        notesList = (ListView)findViewById(R.id.listView);
        toDoArrayAdapter = new ToDoArrayAdapter(this, R.layout.card_of_todo, toDoArrayList);
        notesList.setAdapter(toDoArrayAdapter);
        notesList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ToDoItem note = toDoArrayList.get(position);

                Intent intent = new Intent(MainActivity.this, ToDoCardCreate.class);
                intent.putExtra("Title", note.getTitle());
                intent.putExtra("Text", note.getText());
                intent.putExtra("category", note.getCategory());
                intent.putExtra("dueDate", note.getDueDate());
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

                ToDoItem toDoItem = toDoArrayList.get(position);

                Intent intent = new Intent(MainActivity.this, ToDoCardCreate.class);

                intent.putExtra("toDoTitle", toDoItem.getTitle());
                intent.putExtra("completionDueDate", toDoItem.getDateModified());
                intent.putExtra("category", toDoItem.getCategory());

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
                        ToDoItem note = toDoArrayList.get(position);
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
            // TODO i put null for categories- fix it.
            ToDoItem card = new ToDoItem(data.getStringExtra("title"),
                    data.getStringExtra("text"),
                    data.getStringExtra("due_date"),
                    new Date(),
                    data.getStringExtra("category"));
            if (index < 0 || index > toDoArrayList.size() - 1) {
                toDoArrayList.add(card);
            }else {
                ToDoItem oldCard = toDoArrayList.get(index);
                toDoArrayList.set(index, card);
                if (!oldCard.getTitle().equals(card.getTitle())) {
                    File oldFile = new File (this.getFilesDir(), oldCard.getTitle());
                    File newFile = new File (this.getFilesDir(), card.getTitle());
                    oldFile.renameTo(newFile);
                }
            }
            writeToDos();
            toDoArrayAdapter.updateAdapter(toDoArrayList);
        }
    }


//        getIntent();
//        String title = data.getStringExtra("title");
//        String fullDate = data.getStringExtra("fullDate");
//        String text = data.getStringExtra("text");
//        toDoTitle.setText(title);
//        toDoDate.setText(fullDate);
    //   private ListView notesList;
    // private ArrayList<ToDoItem> arrayOfList;

    public void setupToDoCards() {
            toDoArrayList = new ArrayList<>();

            File filesDir = this.getFilesDir();
            File todoFile = new File(filesDir + File.separator + filename);
            if (todoFile.exists()){
                readTodos(todoFile);
            }else {
                //new way
                categories.add(new Category("Personal", new ArrayList<ToDoItem>()));
                categories.add(new Category("Maritial", new ArrayList<ToDoItem>()));
                categories.add(new Category("Professional", new ArrayList<ToDoItem>()));

                for(int i = 0; i < categories.size(); i++) {
                    categories.get(i).cards.add(new ToDoItem("Mountain Dew",
                            "This is your first To dew",
                            "Maritial",
                            new Date(),
                            "01/13/1984"));

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

            // Determine type of our collection
            Type collectionType = new TypeToken<List<Category>>(){}.getType();
            // Pull out our categories in a list
            List<Category> categoryList = gson.fromJson(todosText, collectionType);
            // Create a LinkedList that we can edit from our categories list and save it
            // to our global categories
            //Todo they want this final- maybe it'll fix itself.
            categories = new LinkedList(categoryList);
            // old way
//            Note[] noteList = gson.fromJson(todosText, Note[].class);
//            noteLists = Arrays.asList(noteList);

            //Object[] notesArray = noteLists.toArray();
        }
//            ToDoItem[] noteList = gson.fromJson(todosText, ToDoItem[].class);
        }


    private void saveIt(ToDoItem aToDo) {
        //  if()

        FileOutputStream outputStream = null;
        try {
            outputStream = openFileOutput("##" + aToDo.getTitle(), Context.MODE_PRIVATE);
            outputStream.write(aToDo.getText().getBytes());
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
            toDoArrayList.add(new ToDoItem(title, text, date, null));
        }
    }

            arrayOfList = new ArrayList<>();
        if (toDoPrefs.getBoolean("firstRun", true)){
            SharedPreferences.Editor editor = toDoPrefs.edit();
            editor.putBoolean("firstRun", false);
            editor.apply();

            // this just shows what the card looks like. It populates with true because it's done.
            ToDoItem toDo1 = new ToDoItem("Mountain Dew", "This is your first To do", new Date(), true);
            arrayOfList.add(toDo1);

            for (ToDoItem aToDo: arrayOfList) {
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
                        ToDoItem aToDo = gson.fromJson(text, ToDoItem.class);
                    aToDo.setDateModified(date);
                    arrayOfList.add(aToDo);
                    try {
                        inputStream.close();
                    } catch (Exception ignored) {}
                }
            }
        }
    }
    */



    /*
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

*/

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
            } catch (Exception ignored) {}
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
        for (ToDoItem note : toDoArrayList){
            saveIt(note);
        }
    }
}