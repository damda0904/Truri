package com.example.truri.middleware;

import android.graphics.Color;

import com.example.truri.R;

public class LevelCheck {



    public int circle(String level){
        switch(level) {
            case "1": return R.drawable.red_circle;
            case "2": return R.drawable.yellow_circle;
            default:  return R.drawable.blue_circle;
        }
    }

    public int icon(String level){
        switch(level) {
            case "1" : return R.drawable.baseline_dangerous_24;
            case "2" : return R.drawable.baseline_report_problem_24;
            default : return R.drawable.baseline_verified_20;
        }
    }

    public int intColor(String level) {
        int blue = Color.parseColor("#13A6BA");
        int yellow = Color.parseColor("#FBB743");
        int red = Color.parseColor("#F33362");

        switch(level){
            case "1" : return red;
            case "2" : return yellow;
            default : return blue;
        }
    }

    public String stringColor(String level){

        String blue = "#13A6BA";
        String yellow = "#FBB743";
        String red = "#F33362";

        switch(level){
            case "1" : return red;
            case "2" : return yellow;
            default : return blue;
        }
    }
}
