package com.example.mtndew3;

import com.google.gson.annotations.SerializedName;

import java.util.Date;
import java.util.UUID;

/**
 * Created by eaglebrosi on 10/26/16.
 */

public class ToDoItem implements Comparable<ToDoItem>{
    @SerializedName("title")
    private String title;
    @SerializedName("text")
    private String text;
    @SerializedName("dateModified")
    private Date dateModified;
    @SerializedName("dueDate")
    private String dueDate;
    @SerializedName("category")
    private String category;
    @SerializedName("catCall")
    private String catCall;
    @SerializedName("key")
    private String key;

    public ToDoItem(String title, String text, String category,  Date dateModified, String dueDate) {
        this.title = title;
        this.text = text;
        this.category = category;
        this.dateModified = dateModified;
        this.dueDate = dueDate;
        this.key = UUID.randomUUID().toString();
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }


    public Date getDateModified() {
        return dateModified;
    }

    public void setDateModified(Date dateModified) {
        this.dateModified = dateModified;
    }

    public String getDueDate() {
        return dueDate;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    //todo actually do something with my cat call.
    public String getCatCall() {
        return catCall;
    }

    public void setCatCall(String catCall) {
        this.catCall = catCall;
    }

    @Override
    public int compareTo(ToDoItem another) {
        return another.getCategory().compareToIgnoreCase(getCategory());
    }
}