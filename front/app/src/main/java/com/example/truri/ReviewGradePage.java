package com.example.truri;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ReviewGradePage extends AppCompatActivity {

    private Toolbar toolbar;
    private TextView top_text;
    private Button next_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_grade_page);

        //뒤로가기 버튼 설정
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setTitle("");

        //상단 텍스트 '신뢰도' 설정
        top_text = findViewById(R.id.top_text);
        SpannableStringBuilder sp = new SpannableStringBuilder("신뢰 불가 입니다.");

        ForegroundColorSpan color = new ForegroundColorSpan(Color.parseColor("#F33362"));
        sp.setSpan(color, 0, 5, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        top_text.append(sp);


        //다음버튼 페이지 이동
        next_btn = findViewById(R.id.next_btn);
        next_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), GradeTextPage.class);
                startActivity(intent);
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