package com.example.truri;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class ApiConnector {

    private URL url;
    static private String result;

    public ApiConnector(URL url) {
        this.url = url;
    }

    public String post(String body) {

        result = null;

        //백엔드 연결
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();

                    //헤더 설정
                    conn.setRequestProperty("User-Agent", "truri-v0.1");
                    conn.setRequestProperty("Content-Type", "text/html");
                    conn.setRequestMethod("POST");
                    conn.setDoOutput(true);

                    //파싱 후 전송
                    conn.getOutputStream().write(body.getBytes());

                    //응답
                    if (conn.getResponseCode() == 200) {
                        InputStream responseBody = conn.getInputStream();
                        StringBuilder builder = new StringBuilder();

                        BufferedReader reader = new BufferedReader(new InputStreamReader(responseBody, "UTF-8"));
                        String line;
                        while ((line = reader.readLine()) != null) {
                            builder.append(line);
                        }

                        result = builder.toString();

                        System.out.println("Connection is Successful");
                        System.out.println(result);

                    } else {
                        System.out.println("-----------------connector error");
                        System.out.println(conn.getResponseCode());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        System.out.println(result);

        return result;
    }
}
