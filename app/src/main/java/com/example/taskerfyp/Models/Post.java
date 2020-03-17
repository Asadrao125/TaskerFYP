package com.example.taskerfyp.Models;

import android.net.Uri;

public class Post {
    String budget;
    String current_user_name;
    String date;
    String deadline;
    String description;
    String id;
    String time;
    String title;
    String image;

    public Post(String budget, String current_user_name, String date, String deadline, String description, String id, String time, String title, String image) {
        this.budget = budget;
        this.current_user_name = current_user_name;
        this.date = date;
        this.deadline = deadline;
        this.description = description;
        this.id = id;
        this.time = time;
        this.title = title;
        this.image = image;
    }

    public Post() {
    }

    public String getBudget() {
        return budget;
    }

    public void setBudget(String budget) {
        this.budget = budget;
    }

    public String getCurrent_user_name() {
        return current_user_name;
    }

    public void setCurrent_user_name(String current_user_name) {
        this.current_user_name = current_user_name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDeadline() {
        return deadline;
    }

    public void setDeadline(String deadline) {
        this.deadline = deadline;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
