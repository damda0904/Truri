package com.example.truri;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private View drawerView;
    private MaterialToolbar toolbar;
    private TextView main_text;
    private TextView[] historyList = new TextView[7];
    private Button info_btn;
    private Button signin_out, signUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        drawerLayout = (DrawerLayout)findViewById(R.id.drawer);
        drawerView = (View)findViewById(R.id.menu);

        drawerLayout.setDrawerListener(listener);
        drawerView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return true;
            }
        });



        main_text = findViewById(R.id.main_text);
        SpannableStringBuilder builder = new SpannableStringBuilder("믿음가는 리뷰를 찾으세요");
        builder.setSpan(new ForegroundColorSpan(Color.parseColor("#13A6BA")), 0, 7, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        main_text.append(builder);



        toolbar = (MaterialToolbar)findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(drawerView);
            }
        });



        historyList[0] = findViewById(R.id.history1);
        historyList[1] = findViewById(R.id.history2);
        historyList[2] = findViewById(R.id.history3);
        historyList[3] = findViewById(R.id.history4);
        historyList[4] = findViewById(R.id.history5);
        historyList[5] = findViewById(R.id.history6);
        historyList[6] = findViewById(R.id.history7);

        for(TextView h : historyList) {
            h.setText("test text"); //추후 실제 검색 기록으로 대체
        }


        info_btn = (Button)findViewById(R.id.info_btn);


        signin_out = (Button)findViewById(R.id.signin_out);
        //추후 if문으로 로그인/로그아웃 상태에 따라 텍스트 변화주기
        signin_out.setText("로그아웃");

        signUp = (Button)findViewById(R.id.signUp);
        signUp.setVisibility(View.VISIBLE);   //추후 로그인 상태의 역우 INVISIBLE로 바꾸기
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

    public void onClickShowAlert(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

        builder.setMessage("원하는 키워드를 검색하면 빨간색, 노란색, 파란색 제목의 검색 결과가 나옵니다. 빨간색은 '광고성 글, 신뢰 불가', 노란색은 '일부 신뢰', 파란색은 '신뢰 가능'을 뜻합니다.");
        builder.setPositiveButton("알겠어요!", new DialogInterface.OnClickListener(){

            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });

        builder.show();
    }
}