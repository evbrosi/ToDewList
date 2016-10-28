package com.example.mtndew3;

import com.google.gson.annotations.SerializedName;

import java.util.Date;
import java.util.UUID;

/**
 * Created by eaglebrosi on 10/26/16.
 */

public class ToDoConstructor {
    @SerializedName("title")
    private String title;
    @SerializedName("text")
    private String text;
    @SerializedName("date")
    private Date date;
    @SerializedName("dueDate")
    private String dueDate;
    private String category;
    @SerializedName("key")
    private String key;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public ToDoConstructor(String title, String text, Date date, String dueDate) {
        this.title = title;
        this.text = text;
        this.date = date;
        this.dueDate = dueDate;

        this.key = UUID.randomUUID().toString();
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDueDate() {
        return dueDate;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

}
