package com.example.truri;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.truri.Network.ServerHost;
import com.example.truri.middleware.AsyncPost;
import com.example.truri.middleware.LevelCheck;

import org.json.JSONObject;

import java.util.concurrent.ExecutionException;

public class GradeTextPage extends AppCompatActivity {

    private Toolbar toolbar;
    private TextView top_text;
    private Button submit_btn;

    private String link, title;
    private int originalLevel, newLevel;

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
        originalLevel = Integer.parseInt(intent.getStringExtra("originalLevel"));
        newLevel = Integer.parseInt(intent.getStringExtra("newLevel"));
        link = intent.getStringExtra("url");
        title = intent.getStringExtra("title");

        String sb_text = null;
        if(newLevel == 3) sb_text = levelCheck.text(newLevel) + " 으로 평가하신";
        else sb_text = levelCheck.text(newLevel) + " 로 평가하신";
        SpannableStringBuilder sb = new SpannableStringBuilder(sb_text);

        String color_string = levelCheck.stringColor(newLevel);
        ForegroundColorSpan color = new ForegroundColorSpan(Color.parseColor(color_string));
        sb.setSpan(color, 0, 5, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        top_text = findViewById(R.id.grade2_text1);
        top_text.setText(sb);

        //완료버튼 페이지 이동
        submit_btn = findViewById(R.id.submit_btn);
        submit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //sharedPreferences 불러오기
                SharedPreferences jwt = getSharedPreferences("JWT", MODE_PRIVATE);
                SharedPreferences history = getSharedPreferences("History", MODE_PRIVATE);

                Intent preIntent = getIntent();
                String keyword = preIntent.getStringExtra("keyword");

                //토큰 불러오기
                String token = jwt.getString("token", "");

                //의견 제출
                String localhost = new ServerHost().getHost_url("spring");
                String url = localhost + "/opinion/";
                EditText et = findViewById(R.id.editText);
                String content = et.getText().toString();

                try {
                    JSONObject body = new JSONObject();
                    body.put("url", link);
                    body.put("title", title);
                    body.put("originalLevel", originalLevel);
                    body.put("newLevel", newLevel);
                    body.put("content", content);
                    String body_string = body.toString();

                    JSONObject result = new AsyncPost().execute(url, body_string, token).get();
                    if(result != null){
                        Intent intent = new Intent(getApplicationContext(), SearchPage.class);
                        intent.putExtra("keyword", keyword);
                        startActivity(intent);
                    } else {
                        Toast.makeText(getApplicationContext(), "의견 등록에 실패했습니다.", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}