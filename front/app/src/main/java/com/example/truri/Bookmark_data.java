package com.example.truri;

import android.widget.ImageView;
import android.widget.TextView;

public class Bookmark_data {

    private long id;
    private int reliability_icon;
    private String title, content, color, url;

    public Bookmark_data(long id, int reliability_icon, String title, String content, String color, String url) {
        this.id = id;
        this.reliability_icon = reliability_icon;
        this.title = title;
        this.content = content;
        this.color = color;
        this.url = url;
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

    public long getId() { return this.id = id; }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
