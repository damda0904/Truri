package com.example.truri.middleware;

import static android.content.Context.MODE_PRIVATE;

import android.content.SharedPreferences;
import android.os.Build;

import androidx.annotation.RequiresApi;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ManageSharedPref {

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public Boolean setSearchHist(SharedPreferences share, String keyword) {

        //히스토리 가져와 겹치는 게 있는지 비교
        String[] history = getSearchHist(share);
        boolean existed = false;
        for(String word: history){
            if(word.equals(keyword)){
                existed = true;
                break;
            }
        }

        if(existed) {
            return true;
        }

        //히스토리 새로 생성하기
        SharedPreferences.Editor editor = share.edit();

        String json = share.getString("search", null);

        System.out.println("json : " + json);
        try {
            JSONArray array;
            if(json == null) {
                array = new JSONArray();
            } else {
                array = new JSONArray(json);
            }

            System.out.println("previous : " + array);

            if(array.length() == 7) {
                array.remove(0);
            }

            array.put(keyword);

            System.out.println("new : " + array.toString());

            editor.putString("search", array.toString());
            editor.commit();

            return true;
        } catch (JSONException e) {
            e.printStackTrace();

            return false;
        }
    }

    public String[] getSearchHist(SharedPreferences share) {
        String[] history = new String[7];
        String json = share.getString("search", null);

        if(json != null) {
            try {
                JSONArray jsonArray = new JSONArray(json);

                for (int i = 0; i < history.length; i++) {
                    history[i] = jsonArray.optString(i);
                }

                System.out.println("history : " + history.toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return history;
    }
}
