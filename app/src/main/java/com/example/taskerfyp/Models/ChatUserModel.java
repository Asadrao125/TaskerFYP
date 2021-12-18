package com.example.taskerfyp.Models;

public class ChatUserModel {
    String username;
    String userid;
    String title;
    String gender;
    String phonenumber;
    String email;
    String profileimage;

    public ChatUserModel(String username, String userid, String title, String gender, String phonenumber, String email, String profileimage) {
        this.username = username;
        this.userid = userid;
        this.title = title;
        this.gender = gender;
        this.phonenumber = phonenumber;
        this.email = email;
        this.profileimage = profileimage;
    }

    public String getProfileImage() {
        return profileimage;
    }

    public void setProfileImage(String profileImage) {
        this.profileimage = profileImage;
    }

    public ChatUserModel() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getPhonenumber() {
        return phonenumber;
    }

    public void setPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
