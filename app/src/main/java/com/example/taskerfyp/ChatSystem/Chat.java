package com.example.taskerfyp.ChatSystem;

class Chat {
    String sender;
    String reciever;
    String time;
    String date;
    String message;

    public Chat(String sender, String reciever, String time, String date, String message) {
        this.sender = sender;
        this.reciever = reciever;
        this.time = time;
        this.date = date;
        this.message = message;
    }

    public Chat() {
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReciever() {
        return reciever;
    }

    public void setReciever(String reciever) {
        this.reciever = reciever;
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

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
