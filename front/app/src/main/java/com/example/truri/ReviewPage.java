package com.example.truri;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;

import java.util.ArrayList;

public class ReviewPage extends AppCompatActivity {

    private Toolbar toolbar;
    private ArrayList<Review_data> data;
    private Review_adapter adapter;
    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;

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

        adapter = new Review_adapter(data);
        recyclerView.setAdapter(adapter);

        //더미 데이터
        for (int i = 0; i < 10; i++) {
            Review_data dummy = new Review_data(R.drawable.baseline_verified_20, R.drawable.red_circle,
                    "test title",
                    "이것은 테스트 리뷰입니다",
                    "#13A6BA");
            data.add(dummy);
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