package com.example.truri;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

public class Search_items extends AppCompatActivity {

    private ImageButton bookmark_icon;
    private ImageButton grade_icon;
    int bookmark_click = 0;
    int grade_click = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_items);

        //북마크버튼 클릭 설정
        bookmark_icon = (ImageButton)findViewById(R.id.bookmark_icon);
        bookmark_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(bookmark_click == 0)
                {
                    bookmark_icon.setSelected(true);
                    bookmark_click = 1;
                }
                else
                {
                    bookmark_icon.setSelected(false);
                    bookmark_click = 0;
                }
            }
        });

        //평가버튼 클릭 설정
        grade_icon = (ImageButton)findViewById(R.id.grade_icon);
        grade_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });

        //평가버튼 페이지 이동
        grade_icon = findViewById(R.id.grade_icon);
        grade_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ReviewGradePage.class);
                startActivity(intent);
            }
        });
    }
}