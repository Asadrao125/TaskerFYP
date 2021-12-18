package com.example.taskerfyp.Models;

public class SendOfferTasker {
    String offer_budget;
    String offer_deadline;
    String offer_description;
    String offer_id;
    String userName;
    String offer_sender_id;
    String post_id;
    String time;
    String date;

    public SendOfferTasker(String offer_budget, String offer_deadline, String offer_description, String offer_id, String userName, String offer_sender_id, String post_id, String time, String date) {
        this.offer_budget = offer_budget;
        this.offer_deadline = offer_deadline;
        this.offer_description = offer_description;
        this.offer_id = offer_id;
        this.userName = userName;
        this.offer_sender_id = offer_sender_id;
        this.post_id = post_id;
        this.time = time;
        this.date = date;
    }

    public SendOfferTasker() {
    }

    public String getOffer_budget() {
        return offer_budget;
    }

    public void setOffer_budget(String offer_budget) {
        this.offer_budget = offer_budget;
    }

    public String getOffer_deadline() {
        return offer_deadline;
    }

    public void setOffer_deadline(String offer_deadline) {
        this.offer_deadline = offer_deadline;
    }

    public String getOffer_description() {
        return offer_description;
    }

    public void setOffer_description(String offer_description) {
        this.offer_description = offer_description;
    }

    public String getOffer_id() {
        return offer_id;
    }

    public void setOffer_id(String offer_id) {
        this.offer_id = offer_id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getOffer_sender_id() {
        return offer_sender_id;
    }

    public void setOffer_sender_id(String offer_sender_id) {
        this.offer_sender_id = offer_sender_id;
    }

    public String getPost_id() {
        return post_id;
    }

    public void setPost_id(String post_id) {
        this.post_id = post_id;
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
