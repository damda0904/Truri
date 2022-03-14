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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.truri.middleware.LevelCheck;


public class ReviewGradePage extends AppCompatActivity {

    private Toolbar toolbar;
    private TextView top_text, url, title, date, content;
    private Button next_btn;
    private ImageView reliability_icon, image;

    int CHECK_NUM = 0;
    int newLevel = -1;
    LevelCheck levelCheck = new LevelCheck();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_grade_page);

        //뒤로가기 버튼 설정
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setTitle("");

        //앞 페이지에서 데이터 가져오기
        Intent intent = getIntent();
        int level = Integer.parseInt(intent.getStringExtra("level"));
        String link = intent.getStringExtra("link");
        String title_text = intent.getStringExtra("title");
        String date_text = intent.getStringExtra("date");
        String content_text = intent.getStringExtra("content");
        String image_url = intent.getStringExtra("image");

        //상단 텍스트 '신뢰도' 설정
        String sb_text = levelCheck.text(level) + " 입니다";
        SpannableStringBuilder sb = new SpannableStringBuilder(sb_text);

        String colorName = levelCheck.stringColor(level);
        ForegroundColorSpan color = new ForegroundColorSpan(Color.parseColor(colorName));
        sb.setSpan(color, 0, 5, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        top_text = findViewById(R.id.grade1_text1);
        System.out.println(top_text==null);
        top_text.append(sb);

        //카드 내부 정보 설정
        int icon = levelCheck.icon(level);
        reliability_icon = findViewById(R.id.reliability_icon);
        reliability_icon.setImageResource(icon);

        url = findViewById(R.id.url);
        url.setText(link);
        title = findViewById(R.id.title);
        title.setText(title_text);
        date = findViewById(R.id.date);
        date.setText(date_text);
        content = findViewById(R.id.content);
        content.setText(content_text);
        image = findViewById(R.id.image);
        Glide.with(this).load(image_url).into(image);

        //다음버튼 페이지 이동
        next_btn = findViewById(R.id.next_btn);
        next_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(newLevel == -1) {
                    Toast.makeText(getApplicationContext(), "신뢰도를 평가해주세요", Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent(getApplicationContext(), GradeTextPage.class);
                    intent.putExtra("url", link);
                    intent.putExtra("title", title_text);
                    intent.putExtra("originalLevel", Integer.toString(level));
                    intent.putExtra("newLevel", Integer.toString(newLevel));
                    startActivity(intent);
                }
            }
        });

        //평가버튼 클릭
        Button btn1 = (Button) findViewById(R.id.btn_lv1);
        Button btn2 = (Button) findViewById(R.id.btn_lv2);
        Button btn3 = (Button) findViewById(R.id.btn_lv3);

        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!btn1.isSelected() && CHECK_NUM == 0) {
                    btn1.setSelected(true);
                    CHECK_NUM = 1;
                    newLevel = 1;
                }
                else if(!btn1.isSelected() && CHECK_NUM == 1) {
                    btn1.setSelected(true);
                    btn2.setSelected(false);
                    btn3.setSelected(false);
                    newLevel = 1;
                }
                else {
                    btn1.setSelected(false);
                    CHECK_NUM = 0;
                    newLevel = -1;
                }
            }
        });

        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!btn2.isSelected() && CHECK_NUM == 0) {
                    btn2.setSelected(true);
                    CHECK_NUM = 1;
                    newLevel = 2;
                }
                else if(!btn2.isSelected() && CHECK_NUM == 1) {
                    btn1.setSelected(false);
                    btn2.setSelected(true);
                    btn3.setSelected(false);
                    newLevel = 2;
                }
                else {
                    btn2.setSelected(false);
                    CHECK_NUM = 0;
                    newLevel = -1;
                }
            }
        });

        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!btn3.isSelected() && CHECK_NUM == 0) {
                    btn3.setSelected(true);
                    CHECK_NUM = 1;
                    newLevel = 3;
                }
                else if(!btn3.isSelected() && CHECK_NUM == 1) {
                    btn1.setSelected(false);
                    btn2.setSelected(false);
                    btn3.setSelected(true);
                    newLevel = 3;
                }
                else {
                    btn3.setSelected(false);
                    CHECK_NUM = 0;
                    newLevel = -1;
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