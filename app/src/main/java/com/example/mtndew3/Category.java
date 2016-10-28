package com.example.mtndew3;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by eaglebrosi on 10/28/16.
 */

public class Category {
    @SerializedName("name")
    private String name;

    @SerializedName("cards")
    public ArrayList<ToDoItem> cards;

    public Category (String name, ArrayList<ToDoItem>cards){
        this.name= name;
        this.cards = cards;
    }

    public String getName(){
        return name;
    }

    public void setName(String name){
        this.name = name;
    }
}
