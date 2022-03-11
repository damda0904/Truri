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

import com.example.truri.middleware.AsyncPost;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutionException;

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
                JSONObject id_body = new JSONObject();
                try {
                    id_body.put("userId", userId_text);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                String id_body_string = id_body.toString();

                String url = "http://10.0.2.2:8080/auth/me";
                JSONObject result = null;
                try {
                    result = new AsyncPost().execute(url, id_body_string, null).get();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                if(result != null) {
                    Toast toast = Toast.makeText(getApplicationContext(), "이미 존재하는 아이디입니다.", Toast.LENGTH_SHORT);
                    toast.show();
                    return;
                }

                //폼 데이터 전송
                JSONObject form_body = new JSONObject();
                try {
                    form_body.put("userId", userId_text);
                    form_body.put("password", passwd_text);
                    form_body.put("nickname", nickname_text);
                } catch(Exception e){
                    e.printStackTrace();
                }
                String form_body_string = form_body.toString();

                String signup_url = "http://10.0.2.2:8080/auth/signup";
                String token = null;
                try {
                    JSONObject signup_result = new AsyncPost().execute(signup_url, form_body_string, null).get();
                    token = signup_result.get("token").toString();
                } catch (Exception e) {
                    e.printStackTrace();
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