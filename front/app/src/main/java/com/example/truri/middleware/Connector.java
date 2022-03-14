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

    public JSONObject post(URL url, String body, String token) {
        try{
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            //헤더 설정
            conn.setRequestProperty("User-Agent", "truri-v0.1");
            conn.setRequestProperty("Content-Type", "text/html");
            conn.setRequestProperty("Authorization", token);
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);

            //서버 연결 및 토큰 받아오기
            //파싱 후 전송
            conn.getOutputStream().write(body.getBytes());

            //TODO: 예외처리 - 서버연결불가

            //응답
            String response = null;
            if (conn.getResponseCode() == 200) {
                InputStream responseBody = conn.getInputStream();
                StringBuilder builder = new StringBuilder();

                BufferedReader reader = new BufferedReader(new InputStreamReader(responseBody, "UTF-8"));
                String line;
                while ((line = reader.readLine()) != null) {
                    builder.append(line);
                }

                response = builder.toString();

                System.out.println("Post - "+ url + " : Connection is Successful");

                JSONObject result = new JSONObject(response);

                return result;

            } else {
                System.out.println("Post - " + url + " : connection error--------------------");
                System.out.println(conn.getResponseCode());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public JSONObject login(URL url, String body, String nullToken) {
        try {
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            //헤더 설정
            conn.setRequestProperty("User-Agent", "truri-v0.1");
            conn.setRequestProperty("Content-Type", "text/html");
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);

            //서버 연결 및 토큰 받아오기
            //파싱 후 전송
            conn.getOutputStream().write(body.getBytes());

            //TODO: 예외처리 - 서버연결불가

            //응답
            if (conn.getResponseCode() == 200) {
                InputStream responseBody = conn.getInputStream();
                StringBuilder builder = new StringBuilder();

                BufferedReader reader = new BufferedReader(new InputStreamReader(responseBody, "UTF-8"));
                String line;
                while ((line = reader.readLine()) != null) {
                    builder.append(line);
                }

                String token = builder.toString();

                JSONObject response = new JSONObject();
                response.put("token", token);

                return response;

            } else {
                System.out.println("Post - " + url + " : connection error--------------------");
                System.out.println(conn.getResponseCode());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
