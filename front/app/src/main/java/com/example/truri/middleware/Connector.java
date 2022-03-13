package com.example.truri.middleware;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class Connector {

    private String blue = "#13A6BA";
    private String yellow = "#FBB743";
    private String red = "#F33362";

    public JSONObject get(URL url, String token) {
        try {
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            //헤더 설정
            conn.setRequestProperty("User-Agent", "truri-v0.1");
            conn.setRequestProperty("Authorization", token);
            conn.setConnectTimeout(100000);
            conn.setReadTimeout(100000);
            conn.setRequestMethod("GET");
            conn.setDoInput(true);


            //응답
            if (conn.getResponseCode() == 200) {

                System.out.println("get Response!!");

                //데이터 가져오기
                InputStream responseBody = conn.getInputStream();

                BufferedReader reader = new BufferedReader(new InputStreamReader(responseBody, "UTF-8"));
                String line = reader.readLine();
                conn.disconnect();
                reader.close();

                System.out.println("Connector-------------------");
                System.out.println(line);

                JSONObject result = new JSONObject(line);

                System.out.println("Get - " + url + " : Connection is Successful");

                return result;

            } else {
                System.out.println("Get - " + url + " : connection error--------------------");
                System.out.println(conn.getResponseCode());
                conn.disconnect();

            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public boolean delete(URL url) {
        try {

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            //헤더 설정
            conn.setRequestProperty("User-Agent", "truri-v0.1");
            conn.setRequestMethod("DELETE");
            conn.setDoInput(true);

            //응답
            if (conn.getResponseCode() == 200) {
                System.out.println("delete - " + url + " : Connection is Successful");
                conn.disconnect();
                return true;

            } else {
                System.out.println("delete - " + url + " : connection error--------------------");
                System.out.println(conn.getResponseCode());
                conn.disconnect();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }
}
