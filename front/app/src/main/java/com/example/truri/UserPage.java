package com.example.truri;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

public class UserPage extends AppCompatActivity {

    Toolbar toolbar;
    TextView userId, bookmark_count, review_count;
    TextView[] bookmarks = new TextView[5];
    TextView[] review_titles = new TextView[5];
    ImageView[] circles = new ImageView[5];
    TextView[] review_contents = new TextView[5];

    int blue = Color.parseColor("#13A6BA");
    int yellow = Color.parseColor("#FBB743");
    int red = Color.parseColor("#F33362");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_page);

        //뒤로가기 버튼 설정
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //아이디 표시
        userId = findViewById(R.id.userId);
        userId.setText("@maratang01");  //데이터베이스에서 실제 유저 아이디 가져오기


        //북마크 수량
        //추후 백엔드에서 받아오기
        bookmark_count = findViewById(R.id.bookmark_count);
        bookmark_count.setText("5");

        //북마크 제목
        //추후 백엔드에서 받아오기
        bookmarks[0] = findViewById(R.id.bookmark_content1);
        bookmarks[1] = findViewById(R.id.bookmark_content2);
        bookmarks[2] = findViewById(R.id.bookmark_content3);
        bookmarks[3] = findViewById(R.id.bookmark_content4);
        bookmarks[4] = findViewById(R.id.bookmark_content5);

        bookmarks[0].setText("강아지 관절사료, 밥풀이한테 잘 맞는 더마독 애견사료!");
        bookmarks[0].setTextColor(red);
        bookmarks[1].setText("강아지사료 체중을 관리할려면 이걸로!");
        bookmarks[1].setTextColor(red);
        bookmarks[2].setText("애견인 필독 강아지사료추천 슬리밍컨트롤");
        bookmarks[2].setTextColor(blue);
        bookmarks[3].setText("강아지사료 닥터독 시니어 건강하개!");
        bookmarks[3].setTextColor(yellow);
        bookmarks[4].setText("강아지사료 추천 하림 눈물사료");
        bookmarks[4].setTextColor(blue);


        //리뷰 수량
        //추후 백엔드에서 받아오기
        review_count = findViewById(R.id.review_count);

        //리뷰 제목
        review_titles[0] = findViewById(R.id.review_content1);
        review_titles[1] = findViewById(R.id.review_content2);
        review_titles[2] = findViewById(R.id.review_content3);
        review_titles[3] = findViewById(R.id.review_content4);
        review_titles[4] = findViewById(R.id.review_content5);

        review_titles[0].setText("강아지 관절사료, 밥풀이한테 잘 맞는 더마독 애견사료!");
        review_titles[0].setTextColor(red);
        review_titles[1].setText("강아지사료 체중을 관리할려면 이걸로!");
        review_titles[1].setTextColor(red);
        review_titles[2].setText("애견인 필독 강아지사료추천 슬리밍컨트롤");
        review_titles[2].setTextColor(blue);
        review_titles[3].setText("강아지사료 닥터독 시니어 건강하개!");
        review_titles[3].setTextColor(yellow);
        review_titles[4].setText("강아지사료 추천 하림 눈물사료");
        review_titles[4].setTextColor(blue);

        //리뷰 평가
        circles[0] = findViewById(R.id.review_content1_circle);
        circles[0].setImageResource(R.drawable.blue_circle);
        circles[1] = findViewById(R.id.review_content2_circle);
        circles[1].setImageResource(R.drawable.red_circle);
        circles[2] = findViewById(R.id.review_content3_circle);
        circles[2].setImageResource(R.drawable.red_circle);
        circles[3] = findViewById(R.id.review_content4_circle);
        circles[3].setImageResource(R.drawable.yellow_circle);
        circles[4] = findViewById(R.id.review_content5_circle);
        circles[4].setImageResource(R.drawable.blue_circle);

        review_contents[0] = findViewById(R.id.review_content1_text);
        review_contents[0].setText("이것은 샘플 텍스트입니다.");
        review_contents[1] = findViewById(R.id.review_content2_text);
        review_contents[1].setText("이것은 샘플 텍스트입니다.");
        review_contents[2] = findViewById(R.id.review_content3_text);
        review_contents[2].setText("이것은 샘플 텍스트입니다.");
        review_contents[3] = findViewById(R.id.review_content4_text);
        review_contents[3].setText("이것은 샘플 텍스트입니다.");
        review_contents[4] = findViewById(R.id.review_content5_text);
        review_contents[4].setText("이것은 샘플 텍스트입니다.");

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