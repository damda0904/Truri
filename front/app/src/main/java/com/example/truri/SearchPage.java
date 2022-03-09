package com.example.truri;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.truri.middleware.AsyncGet;
import com.example.truri.middleware.LevelCheck;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.ExecutionException;

public class SearchPage extends AppCompatActivity {
    private RecyclerView recyclerView;
    private Search_Adapter adapter;
    private ArrayList<Search_data> items = new ArrayList<>();
    private String keyword;

    private boolean isLoading = false;

    private LevelCheck levelCheck = new LevelCheck();

    private int page = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_page);

        recyclerView = findViewById(R.id.recyclerView);

        //검색 키워드 받아오기
        Intent intent = getIntent();
        this.keyword = intent.getStringExtra("keyword");

        populateData();
        initAdapter();
        initScrollListener();
    }

    //데이터 불러오기
    private void populateData() {
        String url = "http://10.0.2.2:5000/search/" + keyword + "/" + page;
        JSONObject result = null;
        try {
            result = new AsyncGet().execute(url, "").get();
            System.out.println("----------------------");
            System.out.println(result);
        } catch (Exception e) {
            e.printStackTrace();
        }

        Iterator<String> keys = result.keys();

        while(keys.hasNext()) {
            try {
                JSONObject item = (JSONObject) result.get(keys.next());

                int score = Integer.parseInt(item.get("score").toString());
                int icon = levelCheck.icon(score);
                String color = levelCheck.stringColor(score);

                items.add(new Search_data(icon,
                        item.get("link").toString(),
                        item.get("title").toString(),
                        item.get("date").toString(),
                        item.get("preview").toString(),
                        color));

                page++;

            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    private void initAdapter() {
        adapter = new Search_Adapter(items);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(layoutManager);
    }

    private void initScrollListener() {
        recyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();

                if (!isLoading) {
                    if (layoutManager != null && layoutManager.findLastCompletelyVisibleItemPosition() == items.size() - 1) {
                        //리스트 마지막
                        loadMore();
                        isLoading = true;
                    }
                }
            }
        });
    }

    private void loadMore() {
        items.add(null);
        adapter.notifyItemInserted(items.size() - 1);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                items.remove(items.size() - 1);
                int scrollPosition = items.size();
                adapter.notifyItemRemoved(scrollPosition);
                int currentSize = scrollPosition;
                int nextLimit = currentSize + 30;

                populateData();

                adapter.notifyDataSetChanged();
                isLoading = false;
            }
        }, 2000); //todo 스크롤 로딩시간 (현재 2초)
    }
}