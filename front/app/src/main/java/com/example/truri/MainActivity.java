package com.example.truri;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;

import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.truri.middleware.ManageSharedPref;

public class  MainActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private View drawerView;
    private Toolbar toolbar;
    private TextView main_text;
    private TextView[] historyList = new TextView[7];
    private Button info_btn, bookmark, review, signin_out, member;
    private ImageButton search_btn;
    private EditText search;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //sharedPreferences 불러오기
        SharedPreferences jwt =  getSharedPreferences("JWT", MODE_PRIVATE);
        SharedPreferences history = getSharedPreferences("History", MODE_PRIVATE);


        //토큰 불러오기
        String token = jwt.getString("token", "");


        //drawer-------------------------------
        //drawer 설정
        drawerLayout = (DrawerLayout)findViewById(R.id.drawer);
        drawerView = (View)findViewById(R.id.menu);

        drawerLayout.setDrawerListener(listener);
        drawerView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return true;
            }
        });

        //drawer 열기 기능 설정
        toolbar = (Toolbar)findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(drawerView);
            }
        });

        //북마크 페이지 이동
        bookmark = (Button)findViewById(R.id.bookmark);
        bookmark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(token.equals("")) {
                    startActivity(new Intent(MainActivity.this, LoginPage.class));
                } else {
                    startActivity(new Intent(MainActivity.this, BookmarkPage.class));
                }
            }
        });

        //평가 관리 페이지 이동
        review = (Button)findViewById(R.id.review);
        review.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(token.equals("")) {
                    startActivity(new Intent(MainActivity.this, LoginPage.class));
                } else {
                    startActivity(new Intent(MainActivity.this, ReviewPage.class));
                }
            }
        });

        //토큰이 있을 경우 로그인, 없을 경우 로그아웃 버튼으로 전환
        signin_out = (Button)findViewById(R.id.signin_out);
        if(token.equals("")) {
            signin_out.setText("로그인");
        } else {
            signin_out.setText("로그아웃");
        }

        signin_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(token.equals("")) {
                    startActivity(new Intent(MainActivity.this, LoginPage.class));
                } else {
                    SharedPreferences.Editor editor = jwt.edit();
                    editor.remove("token");
                    editor.commit();
                    startActivity(new Intent(MainActivity.this, MainActivity.class));
                }
            }
        });

        //토큰이 있을 경우 회원가입, 없을 경우 회원탈퇴 버튼으로 전환
        member = (Button)findViewById(R.id.member);
        if(token.equals("")) {
            member.setText("회원가입");
        } else {
            member.setText("회원탈퇴");
        }
        member.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(token.equals("")) {
                    startActivity(new Intent(MainActivity.this, SignUpPage.class));
                } else {
                    //TODO:회원탈퇴 구현
                    //startActivity(new Intent(MainActivity.this, SignUpPage.class));
                }
            }
        });


        //User page-------------------------------
        //회원 정보 버튼(로그인이 안되어 있다면, 로그인 페이지로)
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                if(token.equals("")) {
                    startActivity(new Intent(MainActivity.this, LoginPage.class));
                } else {
                    startActivity(new Intent(MainActivity.this, UserPage.class));
                }
                return false;
            }
        });


        //Main view-------------------------------
        //메인 텍스트 설정
        main_text = findViewById(R.id.main_text);
        SpannableStringBuilder builder = new SpannableStringBuilder("믿음가는 리뷰를 찾으세요");
        builder.setSpan(new ForegroundColorSpan(Color.parseColor("#13A6BA")), 0, 7, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        main_text.append(builder);


        //검색 버튼 설정
        search_btn = (ImageButton)findViewById(R.id.search_btn);
        search_btn.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {
                search = findViewById(R.id.search);
                String keyword = search.getText().toString();

                //검색어가 존재하는지 확인
                if(keyword.equals("")) {
                    Toast toast = Toast.makeText(getApplicationContext(),"검색어를 입력해주세요", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                } else {
                    //검색 히스토리 기록하기
                    ManageSharedPref manage = new ManageSharedPref();
                    //TODO : 히스토리 없는 경우 예외처리
//                manage.setSearchHist(history, keyword);

                    //TODO : 검색페이지의 검색창도 검색 히스토리 기록 기능 삽입하기

                    Intent intent = new Intent(getApplicationContext(), SearchPage.class);
                    intent.putExtra("keyword", keyword);
                    startActivity(intent);
                }
            }
        });


        //검색기록 설정
        //TODO : 히스토리를 보여주는 노드들 개수가 고정되어 있는 부분 수정 요망
        historyList[0] = findViewById(R.id.history1);
        historyList[1] = findViewById(R.id.history2);
        historyList[2] = findViewById(R.id.history3);
        historyList[3] = findViewById(R.id.history4);
        historyList[4] = findViewById(R.id.history5);
        historyList[5] = findViewById(R.id.history6);
        historyList[6] = findViewById(R.id.history7);

        ManageSharedPref manage = new ManageSharedPref();
        String[] searchHist = manage.getSearchHist(history);
        for (int i = 0; i < 7; i++) {
            historyList[i].setText(searchHist[6-i]);
        }


        //설명 버튼 설정
        info_btn = (Button)findViewById(R.id.info_btn);


    }

    DrawerLayout.DrawerListener listener = new DrawerLayout.DrawerListener() {
        @Override
        public void onDrawerSlide(@NonNull View drawerView, float slideOffset) {

        }

        @Override
        public void onDrawerOpened(@NonNull View drawerView) {

        }

        @Override
        public void onDrawerClosed(@NonNull View drawerView) {

        }

        @Override
        public void onDrawerStateChanged(int newState) {

        }
    };

    public void showDescription() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

        builder.setMessage("원하는 키워드를 검색하면 빨간색, 노란색, 파란색 제목의 검색 결과가 나옵니다.\n빨간색은 '광고성 글, 신뢰 불가', 노란색은 '일부 신뢰', 파란색은 '신뢰 가능'을 뜻합니다.");
        builder.setPositiveButton("알겠어요!", null);

        builder.show();
    }
}