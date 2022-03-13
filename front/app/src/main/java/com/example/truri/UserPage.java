package com.example.truri;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.truri.middleware.AsyncGet;
import com.example.truri.middleware.Connector;
import com.example.truri.middleware.LevelCheck;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Iterator;
import java.util.concurrent.ExecutionException;

public class UserPage extends AppCompatActivity {

    Toolbar toolbar;
    TextView userId, bookmark_count, review_count;
    TextView[] bookmarks = new TextView[5];
    TextView[] review_titles = new TextView[5];
    ImageView[] circles = new ImageView[5];
    TextView[] review_contents = new TextView[5];
    RelativeLayout bookmark_layout, review_layout;
    Button logout;


    Connector connector = new Connector();
    LevelCheck levelCheck = new LevelCheck();
    String token = null;

    protected void getToken() {
        //토큰 꺼내오기
        if(token == null){
            SharedPreferences prefs = getSharedPreferences("JWT", MODE_PRIVATE);
            this.token = prefs.getString("token", "");
            if(token.equals("")){
                System.out.println("토큰이 없습니다.");
                startActivity(new Intent(UserPage.this, LoginPage.class));
            }
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_page);

        //뒤로가기 버튼 설정
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setTitle("");

        //로그아웃 버튼 설정
        logout = (Button) findViewById(R.id.logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences jwt =  getSharedPreferences("JWT", MODE_PRIVATE);
                SharedPreferences.Editor editor = jwt.edit();
                editor.remove("token");
                editor.commit();
                startActivity(new Intent(UserPage.this, MainActivity.class));
            }
        });

        //닉네임 표시
        userId = findViewById(R.id.userId);
        getToken();

        String nickname_url = "http://10.0.2.2:8080/auth/nickname";
        JSONObject nickname_result = null;
        String nickname = null;
        try {
            nickname_result = new AsyncGet().execute(nickname_url, token).get();
            nickname = nickname_result.get("nickname").toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        userId.setText(nickname);

        //북마크 레이아웃(클릭으로 더보기 들어가기)
        bookmark_layout = (RelativeLayout)findViewById(R.id.bookmark_layout);
        bookmark_layout.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                startActivity(new Intent(UserPage.this, BookmarkPage.class));
            }
        });

        //북마크 5개까지 가져오기
        bookmark_count = findViewById(R.id.bookmark_count);
        bookmarks[0] = findViewById(R.id.bookmark_content1);
        bookmarks[1] = findViewById(R.id.bookmark_content2);
        bookmarks[2] = findViewById(R.id.bookmark_content3);
        bookmarks[3] = findViewById(R.id.bookmark_content4);
        bookmarks[4] = findViewById(R.id.bookmark_content5);


        String bookmark_url = "http://10.0.2.2:8080/bookmark/";
        JSONObject bookmark_result = null;
        try {
            bookmark_result = new AsyncGet().execute(bookmark_url, token).get();
        } catch (Exception e) {
            e.printStackTrace();
        }

        Iterator<String> bookmark_keys = bookmark_result.keys();
        int bookmark_size = 0;
        for(int i = 0; i < 5 ; i++){

            if(bookmark_keys.hasNext()){
                try {
                    JSONObject bookmark = (JSONObject) bookmark_result.get(bookmark_keys.next());
                    bookmarks[i].setText(bookmark.get("title").toString());

                    String level = bookmark.get("level").toString();
                    bookmarks[i].setTextColor(levelCheck.intColor(level));

                    bookmark_size++;
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                bookmarks[i].setText("");
            }

        }

        bookmark_count.setText(Integer.toString(bookmark_size));


        //리뷰 레이아웃
        review_layout = findViewById(R.id.review_layout);
        review_layout.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                startActivity(new Intent(UserPage.this, ReviewPage.class));
            }
        });

        //리뷰 5개까지 가져오기
        review_count = findViewById(R.id.review_count);
        review_titles[0] = findViewById(R.id.review_content1);
        review_titles[1] = findViewById(R.id.review_content2);
        review_titles[2] = findViewById(R.id.review_content3);
        review_titles[3] = findViewById(R.id.review_content4);
        review_titles[4] = findViewById(R.id.review_content5);
        circles[0] = findViewById(R.id.review_content1_circle);
        circles[1] = findViewById(R.id.review_content2_circle);
        circles[2] = findViewById(R.id.review_content3_circle);
        circles[3] = findViewById(R.id.review_content4_circle);
        circles[4] = findViewById(R.id.review_content5_circle);
        review_contents[0] = findViewById(R.id.review_content1_text);
        review_contents[1] = findViewById(R.id.review_content2_text);
        review_contents[2] = findViewById(R.id.review_content3_text);
        review_contents[3] = findViewById(R.id.review_content4_text);
        review_contents[4] = findViewById(R.id.review_content5_text);

        String review_url = "http://10.0.2.2:8080/opinion";
        JSONObject review_result = null;
        int review_size = 0;
        try {
            review_result = new AsyncGet().execute(review_url, token).get();
        } catch (Exception e) {
            e.printStackTrace();
        }

        Iterator<String> review_keys = review_result.keys();

        try {
            for(int i = 0; i < 5; i++) {
                if(review_keys.hasNext()){
                    JSONObject item = (JSONObject) review_result.get(review_keys.next());

                    String level = item.get("newLevel").toString();
                    int circle = levelCheck.circle(level);

                    review_titles[i].setText(item.get("title").toString());
                    circles[i].setImageResource(circle);
                    review_contents[i].setText(item.get("content").toString());

                    review_size++;
                } else {
                    review_titles[i].setText("");
                    review_contents[i].setText("");
                }
            }
        } catch(Exception e) {
            e.printStackTrace();
        }

        review_count.setText(Integer.toString(review_size));

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