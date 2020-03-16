package com.example.taskerfyp.Models;

public class AddPostCustomer {
    String id;
    String title;
    String description;
    String budget;
    String deadline;
    String time;
    String date;
    String current_user_name;

    public AddPostCustomer(String id, String title, String description, String budget, String deadline, String time, String date, String current_user_name) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.budget = budget;
        this.deadline = deadline;
        this.time = time;
        this.date = date;
        this.current_user_name = current_user_name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getBudget() {
        return budget;
    }

    public void setBudget(String budget) {
        this.budget = budget;
    }

    public String getDeadline() {
        return deadline;
    }

    public void setDeadline(String deadline) {
        this.deadline = deadline;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getCurrent_user_name() {
        return current_user_name;
    }

    public void setCurrent_user_name(String current_user_name) {
        this.current_user_name = current_user_name;
    }

    public AddPostCustomer() {
    }
}
