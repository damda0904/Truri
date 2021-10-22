package com.example.truri;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;

import java.util.ArrayList;

public class BookmarkPage extends AppCompatActivity {

    private Toolbar toolbar;
    private ArrayList<Bookmark_data> data;
    private Bookmark_Adapter adapter;
    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;

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

        //더미 데이터
        for(int i = 0; i < 10; i++) {
            Bookmark_data dummy = new Bookmark_data(R.drawable.baseline_dangerous_24, "test_title",
                    "안녕하세요 가은이에요- 최근 해산물이 당겨서 근방에서 알아준다는 신림 맛집으로 걸음했는데요. 청결한...",
                    "#F33362");
            data.add(dummy);
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