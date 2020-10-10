package com.example.taskerfyp.Models;

public class RatingModel {
    float rating;
    String review;
    String date;
    String name;

    public RatingModel(float rating, String review, String date, String name) {
        this.rating = rating;
        this.review = review;
        this.date = date;
        this.name = name;
    }

    public RatingModel() {
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
