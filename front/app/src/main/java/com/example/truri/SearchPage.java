package com.example.truri;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.truri.Network.ServerHost;
import com.example.truri.middleware.AsyncGet;
import com.example.truri.middleware.LevelCheck;
import com.example.truri.middleware.ManageSharedPref;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.ExecutionException;

public class SearchPage extends AppCompatActivity {
    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private View drawerView;
    private Button info_btn, bookmark, review, signin_out, member;
    ;
    private RecyclerView recyclerView;
    private Search_Adapter adapter;
    private ArrayList<Search_data> items = new ArrayList<>();
    private SearchView search;
    private ImageView logo;
    private String keyword;

    private boolean isLoading = false;

    private LevelCheck levelCheck = new LevelCheck();

    private int page = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_page);


        recyclerView = findViewById(R.id.recyclerView);

        //sharedPreferences 불러오기
        SharedPreferences jwt = getSharedPreferences("JWT", MODE_PRIVATE);
        SharedPreferences history = getSharedPreferences("History", MODE_PRIVATE);


        //토큰 불러오기
        String token = jwt.getString("token", "");

        //drawer-------------------------------
        //drawer 설정
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer);
        drawerView = (View) findViewById(R.id.menu);

        drawerLayout.setDrawerListener(listener);
        drawerView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return true;
            }
        });

        //drawer 열기 기능 설정
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(drawerView);
            }
        });

        //북마크 페이지 이동
        bookmark = (Button) findViewById(R.id.bookmark);
        bookmark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (token.equals("")) {
                    startActivity(new Intent(SearchPage.this, LoginPage.class));
                } else {
                    startActivity(new Intent(SearchPage.this, BookmarkPage.class));
                }
            }
        });

        //평가 관리 페이지 이동
        review = (Button) findViewById(R.id.review);
        review.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (token.equals("")) {
                    startActivity(new Intent(SearchPage.this, LoginPage.class));
                } else {
                    startActivity(new Intent(SearchPage.this, ReviewPage.class));
                }
            }
        });

        //토큰이 있을 경우 로그인, 없을 경우 로그아웃 버튼으로 전환
        signin_out = (Button) findViewById(R.id.signin_out);
        if (token.equals("")) {
            signin_out.setText("로그인");
        } else {
            signin_out.setText("로그아웃");
        }

        signin_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (token.equals("")) {
                    startActivity(new Intent(SearchPage.this, LoginPage.class));
                } else {
                    SharedPreferences.Editor editor = jwt.edit();
                    editor.remove("token");
                    editor.commit();
                    startActivity(new Intent(SearchPage.this, MainActivity.class));
                }
            }
        });

        //토큰이 있을 경우 회원가입, 없을 경우 회원탈퇴 버튼으로 전환
        member = (Button) findViewById(R.id.member);
        if (token.equals("")) {
            member.setText("회원가입");
        } else {
            member.setText("회원탈퇴");
        }
        member.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (token.equals("")) {
                    startActivity(new Intent(SearchPage.this, SignUpPage.class));
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

                if (token.equals("")) {
                    startActivity(new Intent(SearchPage.this, LoginPage.class));
                } else {
                    startActivity(new Intent(SearchPage.this, UserPage.class));
                }
                return false;
            }
        });

        //Search-------------------------------------
        //검색 키워드 받아오기
        Intent intent = getIntent();
        this.keyword = intent.getStringExtra("keyword");

        //검색어 설정
        search = findViewById(R.id.search);
        search.setQueryHint(keyword);

        //검색
        search.setOnClickListener(new View.OnClickListener(){
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View view) {
                SearchView search = (SearchView) view;

                //검색어가 존재하는지 확인
                String newKeyword = search.getQuery().toString();
                if (newKeyword.equals("")) {
                    Toast toast = Toast.makeText(getApplicationContext(), "검색어를 입력해주세요", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.TOP, 0, 0);
                    toast.show();
                } else {
                    //검색 히스토리 기록하기
                    ManageSharedPref manage = new ManageSharedPref();
                    manage.setSearchHist(history, newKeyword);

                    Intent intent = new Intent(getApplicationContext(), SearchPage.class);
                    intent.putExtra("keyword", newKeyword);
                    startActivity(intent);
                }
            }
        });

        //Toolbar---------------------------------
        //메인으로 돌아가는 버튼
        logo = findViewById(R.id.logo);
        logo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SearchPage.this, MainActivity.class));
            }
        });


        LoadingTask task = new LoadingTask();
        task.execute();
    }

    //ProgressDialog 로딩 화면
    private class LoadingTask extends AsyncTask<Void, Void, Void> {   //AsyncTask 클래스

        ProgressDialog asyncDialog = new ProgressDialog(SearchPage.this);

        @Override
        protected void onPreExecute() {         //ProgressDialog 생성, 시작
            asyncDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            asyncDialog.setMessage("검색 결과를 가져오고 있습니다.");

            asyncDialog.show();
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... arg0) {       //ProgressDialog 진행 정도
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {         //ProgressDialog 종료
            populateData();
            initAdapter();
            initScrollListener();
            asyncDialog.dismiss();
            super.onPostExecute(result);
        }
    }


    //데이터 불러오기
    private void populateData() {
        //dummy
//        items.add(new Search_data(
//                R.drawable.baseline_verified_20,
//                "https://www.naver.com",
//                "dummy ",
//                "2022-03-11",
//                "test data",
//                "#F33362",
//                3,
//                "https://t1.daumcdn.net/cfile/tistory/994BEF355CD0313D05"
//        ));

        String localhost = new ServerHost().getHost_url("flask");
        String url = localhost + "/search/" + keyword + "/" + page;
        JSONObject result = null;
        try {
            result = new AsyncGet().execute(url, "").get();
        } catch (Exception e) {
            e.printStackTrace();
        }

        Iterator<String> keys = result.keys();

        while(keys.hasNext()) {
            try {
                JSONObject item = (JSONObject) result.get(keys.next());

                int level = levelCheck.setLevel(item.get("score").toString());
                int icon = levelCheck.icon(level);
                String color = levelCheck.stringColor(level);

                items.add(new Search_data(icon,
                        item.get("link").toString(),
                        item.get("title").toString(),
                        item.get("date").toString(),
                        item.get("preview").toString(),
                        color, level,
                        item.get("preview_image").toString()));
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        page++;

    }

    private void initAdapter() {
        adapter = new Search_Adapter(items, getApplicationContext(), search.getQueryHint().toString());
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(layoutManager);
    }

    private void initScrollListener() {
        recyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();

                if (!isLoading) {
                    if (layoutManager != null && layoutManager.findLastCompletelyVisibleItemPosition() == items.size() - 1) {
                        //리스트 마지막
                        loadMore();
                        isLoading = true;
                    }
                }
            }
        });
    }

    private void loadMore() {
        items.add(null);
        adapter.notifyItemInserted(items.size() - 1);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                items.remove(items.size() - 1);
                int scrollPosition = items.size();
                adapter.notifyItemRemoved(scrollPosition);
                int currentSize = scrollPosition;
                int nextLimit = currentSize + 30;

                populateData();

                adapter.notifyDataSetChanged();
                isLoading = false;
            }
        }, 2000); //todo 스크롤 로딩시간 (현재 2초)
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
}