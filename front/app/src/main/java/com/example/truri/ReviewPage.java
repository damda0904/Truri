package com.example.truri;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.truri.middleware.AsyncGet;
import com.example.truri.middleware.Connector;
import com.example.truri.middleware.LevelCheck;

import org.json.JSONObject;

import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.ExecutionException;

public class ReviewPage extends AppCompatActivity {

    private Toolbar toolbar;
    private ArrayList<Review_data> data;
    private Review_adapter adapter;
    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;

    LevelCheck levelCheck = new LevelCheck();
    String token;

    protected void getToken() {
        //토큰 꺼내오기
        if(token == null){
            SharedPreferences prefs = getSharedPreferences("JWT", MODE_PRIVATE);
            this.token = prefs.getString("token", "");
            if(token.equals("")){
                System.out.println("토큰이 없습니다.");
                startActivity(new Intent(ReviewPage.this, LoginPage.class));
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_page);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setTitle("리뷰 신뢰도 평가 관리 ");

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);

        data = new ArrayList<>();

        adapter = new Review_adapter(getApplicationContext(), data);
        recyclerView.setAdapter(adapter);

        String url = "http://10.0.2.2:8080/opinion/";
        JSONObject result = null;
        getToken();
        try {
            result = new AsyncGet().execute(url, token).get();

            System.out.println(result);

            Iterator<String> keys = result.keys();

            while(keys.hasNext()){
                JSONObject item = (JSONObject) result.get(keys.next());

                int origin = Integer.parseInt(item.get("originalLevel").toString());
                int icon = levelCheck.icon(origin);

                int newLevel = Integer.parseInt(item.get("newLevel").toString());
                int circle = levelCheck.circle(newLevel);
                String color = levelCheck.stringColor(newLevel);

                Review_data review = new Review_data(
                        Long.valueOf(item.get("opinionId").toString()),
                        icon, circle,
                        item.get("title").toString(),
                        item.get("content").toString(),
                        color);

                data.add(review);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: {
                finish();
                return true;
            }
        }

        return super.onOptionsItemSelected(item);
    }

}