package com.example.truri;

import android.graphics.drawable.Drawable;

public class Review_data {
    private int reliability_icon, circle_icon;
    private String title, review, color;

    public Review_data(int reliability_icon, int circle_icon, String title, String review, String color) {
        this.reliability_icon = reliability_icon;
        this.circle_icon = circle_icon;
        this.title = title;
        this.review = review;
        this.color = color;
    }

    public int getReliability_icon() {
        return reliability_icon;
    }

    public void setReliability_icon(int reliability_icon) {
        this.reliability_icon = reliability_icon;
    }

    public int getCircle_icon() {
        return circle_icon;
    }

    public void setCircle_icon(int circle_icon) {
        this.circle_icon = circle_icon;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }
}
