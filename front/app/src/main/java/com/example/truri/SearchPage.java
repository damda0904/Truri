package com.example.truri;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;

public class SearchPage extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private Spinner spinner;
    private ImageButton bookmark_icon;
    private ImageButton grade_icon;
    int bookmark_click = 0;
    int grade_click = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_page);


        // 검색 정렬순 설정
        spinner= (Spinner)findViewById(R.id.spinner);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.search_sort, R.layout.search_sort_items);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(this);


        // 추후에 수정
        // 북마크버튼 클릭 설정
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




    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        String choice = adapterView.getItemAtPosition(i).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }



}