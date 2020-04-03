package com.example.taskerfyp.Models;

public class SendMessage {
    String post_id;
    String message_id;
    String message;
    String current_user_id;
    String customer_name;
    String customer_email;
    String customer_number;
    String customer_gender;
    String time;
    String date;

    public SendMessage(String post_id, String message_id, String message, String current_user_id, String customer_name, String customer_email, String customer_number, String customer_gender, String time, String date) {
        this.post_id = post_id;
        this.message_id = message_id;
        this.message = message;
        this.current_user_id = current_user_id;
        this.customer_name = customer_name;
        this.customer_email = customer_email;
        this.customer_number = customer_number;
        this.customer_gender = customer_gender;
        this.time = time;
        this.date = date;
    }

    public SendMessage() {
    }

    public String getPost_id() {
        return post_id;
    }

    public void setPost_id(String post_id) {
        this.post_id = post_id;
    }

    public String getMessage_id() {
        return message_id;
    }

    public void setMessage_id(String message_id) {
        this.message_id = message_id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getCurrent_user_id() {
        return current_user_id;
    }

    public void setCurrent_user_id(String current_user_id) {
        this.current_user_id = current_user_id;
    }

    public String getCustomer_name() {
        return customer_name;
    }

    public void setCustomer_name(String customer_name) {
        this.customer_name = customer_name;
    }

    public String getCustomer_email() {
        return customer_email;
    }

    public void setCustomer_email(String customer_email) {
        this.customer_email = customer_email;
    }

    public String getCustomer_number() {
        return customer_number;
    }

    public void setCustomer_number(String customer_number) {
        this.customer_number = customer_number;
    }

    public String getCustomer_gender() {
        return customer_gender;
    }

    public void setCustomer_gender(String customer_gender) {
        this.customer_gender = customer_gender;
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
}
