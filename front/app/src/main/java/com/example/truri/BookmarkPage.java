package com.example.truri;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.truri.middleware.AsyncGet;
import com.example.truri.middleware.LevelCheck;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.ExecutionException;

public class BookmarkPage extends AppCompatActivity {

    private Toolbar toolbar;
    private ArrayList<Bookmark_data> data;
    private Bookmark_Adapter adapter;
    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;

    private String blue = "#13A6BA";
    private String yellow = "#FBB743";
    private String red = "#F33362";

    private LevelCheck levelCheck = new LevelCheck();
    private String token;

    protected void getToken() {
        //토큰 꺼내오기
        if(token == null){
            SharedPreferences prefs = getSharedPreferences("JWT", MODE_PRIVATE);
            this.token = prefs.getString("token", "");
            if(token.equals("")){
                System.out.println("토큰이 없습니다.");
                startActivity(new Intent(BookmarkPage.this, LoginPage.class));
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookmark_page);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setTitle("북마크 관리");

        recyclerView = (RecyclerView)findViewById(R.id.recyclerView);
        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);

        data = new ArrayList<>();

        adapter = new Bookmark_Adapter(data);
        recyclerView.setAdapter(adapter);

        //토큰 꺼내오기
        SharedPreferences prefs = getSharedPreferences("JWT", MODE_PRIVATE);
        String token = prefs.getString("token", "");
        if(token.equals("")){
            System.out.println("토큰이 없습니다.");
            startActivity(new Intent(BookmarkPage.this, LoginPage.class));
        }

        //데이터 가져오기
        String url = "http://10.0.2.2:8080/bookmark/";
        getToken();
        JSONObject result = null;
        try {
            result = new AsyncGet().execute(url, token).get();

            Iterator<String> keys = result.keys();

            //가져온 데이터 변환해서 저장
            while(keys.hasNext()) {
                JSONObject item = (JSONObject) result.get(keys.next());

                //아이콘 선택
                int level = Integer.parseInt(item.get("level").toString());
                int icon = levelCheck.icon(level);
                String color = levelCheck.stringColor(level);

                Bookmark_data bookmark = new Bookmark_data(Long.valueOf(item.get("bookmarkId").toString()), icon, item.get("title").toString(),
                        item.get("preview").toString(), color);
                data.add(bookmark);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
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