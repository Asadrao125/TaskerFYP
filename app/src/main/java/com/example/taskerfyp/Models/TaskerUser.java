package com.example.taskerfyp.Models;

public class TaskerUser {

    private String taskerUsername, taskerPhonenumber , taskerGender , taskerProfession , taskerEmail;

    public TaskerUser() {
    }

    public TaskerUser(String taskerUsername, String taskerPhonenumber, String taskerGender, String taskerProfession, String taskerEmail) {
        this.taskerUsername = taskerUsername;
        this.taskerPhonenumber = taskerPhonenumber;
        this.taskerGender = taskerGender;
        this.taskerProfession = taskerProfession;
        this.taskerEmail = taskerEmail;
    }

    public String getTaskerUsername() {
        return taskerUsername;
    }

    public void setTaskerUsername(String taskerUsername) {
        this.taskerUsername = taskerUsername;
    }

    public String getTaskerPhonenumber() {
        return taskerPhonenumber;
    }

    public void setTaskerPhonenumber(String taskerPhonenumber) {
        this.taskerPhonenumber = taskerPhonenumber;
    }

    public String getTaskerGender() {
        return taskerGender;
    }

    public void setTaskerGender(String taskerGender) {
        this.taskerGender = taskerGender;
    }

    public String getTaskerProfession() {
        return taskerProfession;
    }

    public void setTaskerProfession(String taskerProfession) {
        this.taskerProfession = taskerProfession;
    }

    public String getTaskerEmail() {
        return taskerEmail;
    }

    public void setTaskerEmail(String taskerEmail) {
        this.taskerEmail = taskerEmail;
    }
}
