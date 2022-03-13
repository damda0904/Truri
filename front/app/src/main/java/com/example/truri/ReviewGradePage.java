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
import android.widget.ImageButton;
import android.widget.TextView;

public class ReviewGradePage extends AppCompatActivity {

    private Toolbar toolbar;
    private TextView top_text;
    private Button next_btn;
    int CHECK_NUM = 0;

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

        //평가버튼 클릭
        Button btn1 = (Button) findViewById(R.id.btn_lv1);
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (CHECK_NUM == 0) {
                    btn1.setSelected(true);
                    CHECK_NUM = 1;
                } else
                {
                    btn1.setSelected(false);
                    CHECK_NUM = 0;
                }
            }
        });

        Button btn2 = (Button) findViewById(R.id.btn_lv2);
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (CHECK_NUM == 0) {
                    btn2.setSelected(true);
                    CHECK_NUM = 1;
                } else
                {
                    btn2.setSelected(false);
                    CHECK_NUM = 0;
                }
            }
        });

        Button btn3 = (Button) findViewById(R.id.btn_lv3);
        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (CHECK_NUM == 0) {
                    btn3.setSelected(true);
                    CHECK_NUM = 1;
                } else
                {
                    btn3.setSelected(false);
                    CHECK_NUM = 0;
                }
            }
        });

    }
}