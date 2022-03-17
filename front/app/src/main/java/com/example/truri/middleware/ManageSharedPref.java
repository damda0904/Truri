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
        JSONArray history = getSearchHist(share);
        if(history != null) {
            for(int i = 0;  i < history.length(); i++) {
                try {
                    if (history.get(i).equals(keyword)) {
                        //존재한다면 순서를 바꾸고 반환
                        changeOrder(share, history, i);
                        return true;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        } else {
            history = new JSONArray();
        }

        System.out.println("History create!");

        //히스토리 새로 생성하기
        SharedPreferences.Editor editor = share.edit();
        try {
            if(history.length() == 7) {
                history.remove(0);
            }

            history.put(keyword);

            System.out.println(history);

            editor.putString("search", history.toString());
            editor.commit();

            System.out.println(getSearchHist(share));

            return true;
        } catch (Exception e) {
            e.printStackTrace();

            return false;
        }
    }

    public JSONArray getSearchHist(SharedPreferences share) {
        String json = share.getString("search", null);
        JSONArray jsonArray = null;


        try {
            if(json != null){
                jsonArray = new JSONArray(json);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return jsonArray;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void changeOrder(SharedPreferences share, JSONArray array, int idx) {
        System.out.println("change!");

        SharedPreferences.Editor editor = share.edit();

        if(idx == array.length()-1) return;

        try {
            String target = array.get(idx).toString();
            array.remove(idx);
            array.put(target);

            editor.putString("search", array.toString());
            editor.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
