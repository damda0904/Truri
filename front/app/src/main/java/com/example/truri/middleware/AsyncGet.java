package com.example.truri.middleware;

import android.os.AsyncTask;

import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;

public class AsyncGet extends AsyncTask<String, Object, JSONObject> {

    private Connector connector = new Connector();

    @Override
    protected JSONObject doInBackground(String... params) {

        try {
            URL url = new URL(params[0]);
            return connector.get(url, params[1]);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return null;
    }
}
