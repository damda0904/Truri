package com.example.truri;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.loader.content.AsyncTaskLoader;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.JsonReader;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class LoginPage extends AppCompatActivity {

    private Toolbar toolbar;
    private Button signIn, signUp;
    private EditText userId, passwd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);

        //뒤로가기 버튼 설정
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setTitle("");

        //로그인 버튼
        signIn = findViewById(R.id.signIn_btn);
        userId = findViewById(R.id.userId);
        passwd = findViewById(R.id.passwd);
        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //백엔드 연결
                AsyncTask.execute(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            //로그인 url
                            URL url = new URL("http://10.0.2.2:8080/auth/login");

                            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

                            //헤더 설정
                            conn.setRequestProperty("User-Agent", "truri-v0.1");
                            conn.setRequestMethod("POST");
                            conn.setDoOutput(true);

                            //아이디, 비밀번호 데이터
                            JSONObject body = new JSONObject();
                            body.put("userId",userId.getText().toString());
                            body.put("password", passwd.getText().toString());

                            //파싱 후 전송
                            String body_string = body.toString();
                            conn.getOutputStream().write(body_string.getBytes());

                            //응답
                            if (conn.getResponseCode() == 200) {
                                InputStream responseBody = conn.getInputStream();
                                StringBuilder builder = new StringBuilder();

                                BufferedReader reader = new BufferedReader(new InputStreamReader(responseBody, "UTF-8"));
                                String line;
                                while ((line = reader.readLine()) != null) {
                                    builder.append(line);
                                }

                                String result = builder.toString();

                                System.out.println("----------------------response---------------");
                                System.out.println(result);

                                //다음 페이지로 이동
                                //TODO: 다른 페이지로 수정, 토큰 저장
                                startActivity(new Intent(LoginPage.this, UserPage.class));
                            } else {
                                // Error handling code goes here
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });


            }
        });

        //회원가입 버튼
        signUp = findViewById(R.id.signUp_btn);
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginPage.this, SignUpPage.class));
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