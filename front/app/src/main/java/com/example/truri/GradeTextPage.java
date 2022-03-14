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

import com.example.truri.middleware.LevelCheck;

public class GradeTextPage extends AppCompatActivity {

    private Toolbar toolbar;
    private TextView top_text;
    private Button submit_btn;

    LevelCheck levelCheck = new LevelCheck();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grade_text_page);

        //뒤로가기 버튼 설정
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setTitle("");

        //상단 텍스트 '신뢰도' 설정
        Intent intent = getIntent();
        int level = Integer.parseInt(intent.getStringExtra("level"));

        String sb_text = null;
        if(level == 3) sb_text = levelCheck.text(level) + " 으로 평가하신";
        else sb_text = levelCheck.text(level) + " 로 평가하신";
        SpannableStringBuilder sb = new SpannableStringBuilder(sb_text);

        String color_string = levelCheck.stringColor(level);
        ForegroundColorSpan color = new ForegroundColorSpan(Color.parseColor(color_string));
        sb.setSpan(color, 0, 5, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        top_text = findViewById(R.id.grade2_text1);
        top_text.setText(sb);

        //완료버튼 페이지 이동
        submit_btn = findViewById(R.id.submit_btn);
        submit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent intent = new Intent(getApplicationContext(), SearchPage.class);
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