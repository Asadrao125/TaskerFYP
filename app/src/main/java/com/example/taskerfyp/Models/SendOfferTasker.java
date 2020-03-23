package com.example.taskerfyp.Models;

public class SendOfferTasker {
    String offer_budget;
    String offer_deadline;
    String offer_description;
    String offer_id;
    String userName;

    public SendOfferTasker(String offer_budget, String offer_deadline, String offer_description, String offer_id, String userName) {
        this.offer_budget = offer_budget;
        this.offer_deadline = offer_deadline;
        this.offer_description = offer_description;
        this.offer_id = offer_id;
        this.userName = userName;
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
}
