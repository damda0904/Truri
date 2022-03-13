package com.example.truri;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class SignUpPage extends AppCompatActivity {

    private Toolbar toolbar;
    private Button submit;
    private EditText userId, passwd, passwd_repeat, nickname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_page);

        //뒤로가기 버튼 설정
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setTitle("");

        //제출 버튼
        //추후 백엔드 연결
        submit = findViewById(R.id.submit);
        userId = findViewById(R.id.userId);
        passwd = findViewById(R.id.passwd);
        passwd_repeat = findViewById(R.id.passwd_repeat);
        nickname = findViewById(R.id.nickname);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                String userId_text = userId.getText().toString();
                String passwd_text = passwd.getText().toString();
                String repeat_text = passwd_repeat.getText().toString();
                String nickname_text = nickname.getText().toString();

                System.out.println("-------------signup------------------");
                System.out.println("userId : " + userId_text);
                System.out.println("passwd : " + passwd_text);
                System.out.println("passwd repeat : " + repeat_text);
                System.out.println("nickname : " + nickname_text);


                //모든 내용이 채워져있는지 확인
                if(userId_text.equals("") ||
                        passwd_text.equals("") ||
                        repeat_text.equals("") ||
                        nickname_text.equals("")) {
                    System.out.println("FORM IS EMPTY");
                    Toast toast = Toast.makeText(getApplicationContext(), "모든 항목을 작성해주세요", Toast.LENGTH_SHORT);
                    toast.show();
                    return;
                }

                //비밀번호가 일치하는지 확인
                if(!(passwd_text.equals(repeat_text))){
                    Toast toast = Toast.makeText(getApplicationContext(), "비밀번호가 서로 일치하지 않습니다", Toast.LENGTH_SHORT);
                    toast.show();
                    return;
                }

                //아이디가 이미 존재하는지 확인
                AsyncTask.execute(new Runnable() {
                    @Override
                    public void run() {
                        try{
                            URL url = new URL("http://10.0.2.2:8080/auth/me");

                            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

                            //헤더 설정
                            conn.setRequestProperty("User-Agent", "truri-v0.1");
                            conn.setRequestProperty("Content-Type", "text/html");
                            conn.setRequestMethod("POST");
                            conn.setDoOutput(true);

                            //아이디, 비밀번호 데이터
                            JSONObject body = new JSONObject();
                            body.put("userId", userId_text);
                            String body_string = body.toString();

                            //서버 연결 및 토큰 받아오기
                            //파싱 후 전송
                            conn.getOutputStream().write(body_string.getBytes());

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

                                System.out.println("Connection is Successful");
                                System.out.println(response);

                            } else {
                                System.out.println("-----------------connector error");
                                System.out.println(conn.getResponseCode());
                            }

                            if(response != null) {
                                Toast toast = Toast.makeText(getApplicationContext(), "이미 존재하는 아이디입니다.", Toast.LENGTH_SHORT);
                                toast.show();
                                return;
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });

                //폼 데이터 전송
                AsyncTask.execute(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            URL url = new URL("http://10.0.2.2:8080/auth/signup");

                            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

                            //헤더 설정
                            conn.setRequestProperty("User-Agent", "truri-v0.1");
                            conn.setRequestProperty("Content-Type", "text/html");
                            conn.setRequestMethod("POST");
                            conn.setDoOutput(true);

                            //아이디, 비밀번호 데이터
                            JSONObject body = new JSONObject();
                            body.put("userId", userId_text);
                            body.put("password", passwd_text);
                            body.put("nickname", nickname_text);
                            String body_string = body.toString();

                            //서버 연결 및 토큰 받아오기
                            //파싱 후 전송
                            conn.getOutputStream().write(body_string.getBytes());

                            //응답
                            String token = null;
                            if (conn.getResponseCode() == 200) {
                                InputStream responseBody = conn.getInputStream();
                                StringBuilder builder = new StringBuilder();

                                BufferedReader reader = new BufferedReader(new InputStreamReader(responseBody, "UTF-8"));
                                JSONObject data = new JSONObject(reader.readLine());
                                token = data.get("token").toString();
                                reader.close();

                                System.out.println("Connection is Successful");
                                System.out.println(token);

                            } else {
                                System.out.println("-----------------connector error");
                                System.out.println(conn.getResponseCode());
                            }

                            System.out.println("token: " + token);

                            if (token != null) {

                                //토큰 저장
                                SharedPreferences sharedPreferences = getSharedPreferences("JWT", MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putString("token", token);
                                editor.commit();

                                //다음 페이지로 이동
                                startActivity(new Intent(SignUpPage.this, MainActivity.class));
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });

            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()) {
            case android.R.id.home: {
                finish();
                return true;
            }
        }

        return super.onOptionsItemSelected(item);
    }
}