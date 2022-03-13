package com.example.truri.middleware;

import android.os.AsyncTask;

import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;

public class AsyncPost extends AsyncTask<String, Object, JSONObject> {

    Connector connector = new Connector();

    @Override
    protected JSONObject doInBackground(String... params) {
        //params : url, body, token

        try {
            URL url = new URL(params[0]);
            JSONObject result;
            if(params[0].contains("login")){
                result = connector.login(url, params[1], params[2]);
            } else {
                result = connector.post(url, params[1], params[2]);
            }

            return result;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return null;
    }
}
