package com.example.truri;

import android.widget.ImageView;
import android.widget.TextView;

public class Bookmark_data {

    private int reliability_icon;
    private String title, content;
    private String color;

    public Bookmark_data(int reliability_icon, String title, String content, String color) {
        this.reliability_icon = reliability_icon;
        this.title = title;
        this.content = content;
        this.color = color;
    }

    public int getReliability_icon() {
        return reliability_icon;
    }

    public void setReliability_icon(int reliability_icon) {
        this.reliability_icon = reliability_icon;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }
}
