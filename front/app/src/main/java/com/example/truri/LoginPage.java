package com.example.truri;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
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

        //이미 로그인된 상태라면 리디렉션
        SharedPreferences prefs = getSharedPreferences("JWT", MODE_PRIVATE);
        String token = prefs.getString("token", "");
        if(!token.equals("")){
            startActivity(new Intent(LoginPage.this, MainActivity.class));
        }

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

                try {
                    URL url = new URL("http://10.0.2.2:8080/auth/login");

                    //아이디, 비밀번호 데이터
                    JSONObject body = new JSONObject();
                    body.put("userId",userId.getText().toString());
                    body.put("password", passwd.getText().toString());
                    String body_string = body.toString();

                    //서버 연결 및 토큰 받아오기
                    ApiConnector connector = new ApiConnector(url);
                    String token = connector.post(body_string);

                    System.out.println("token : " + token);

                    //토큰 저장
                    SharedPreferences sharedPreferences= getSharedPreferences("JWT", MODE_PRIVATE);
                    SharedPreferences.Editor editor= sharedPreferences.edit();
                    editor.putString("token", token);
                    editor.commit();

                    //다음 페이지로 이동
                    startActivity(new Intent(LoginPage.this, MainActivity.class));

                } catch (Exception e) {
                    e.printStackTrace();
                }

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