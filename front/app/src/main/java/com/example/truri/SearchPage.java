package com.example.truri;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;

import android.widget.Spinner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class SearchPage extends AppCompatActivity implements AdapterView.OnItemSelectedListener {


    private SearchView search;
    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    private ArrayList<Search_data> data;
    private Search_Adapter adapter;

    int bookmark_click = 0;
    int grade_click = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_page);

        //검색 키워드 가져오기
        Intent intent = getIntent();
        String keyword = intent.getStringExtra("keyword");
        System.out.println(keyword);
        search = findViewById(R.id.search);
        search.setQueryHint(keyword);


        // 리사이클러뷰 설정
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);

        data = new ArrayList<>();

        adapter = new Search_Adapter(data);
        recyclerView.setAdapter(adapter);

        //더미 데이터
        for (int i = 0; i < 1; i++) {
            Search_data dummy = new Search_data(R.drawable.baseline_verified_20,
                    "http://localhost:8080",
                    "이것은 더미입니다",
                    "2021.12.16",
                    "더미더미더미더미더미더미더미더미더미더미더미더미더미더미더미더미더미더미더미더미...",
                    "#13A6BA");
            data.add(dummy);


        }

        //데이터 불러오기
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL("http://10.0.2.2:8000/truri/search/" + keyword+"/");

                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();

                    //헤더 설정
                    conn.setRequestProperty("User-Agent", "truri-v0.1");
                    conn.setRequestMethod("GET");

                    //응답
                    if (conn.getResponseCode() == 200) {
                        InputStream responseBody = conn.getInputStream();
                        StringBuilder builder = new StringBuilder();

                        BufferedReader reader = new BufferedReader(new InputStreamReader(responseBody, "UTF-8"));
                        String line;
                        while ((line = reader.readLine()) != null) {
                            builder.append(line);
                        }

                        String response = builder.toString();
                        System.out.println(response);
                        String[] res_list = response.split(", [{]");
                        System.out.println(res_list[1]);
                        List<JSONObject> item_list = new ArrayList<>();
                        for(String item : res_list){
                            System.out.println("------------");
                            System.out.println(item.indexOf(0));
                            if(item.indexOf("{") < 0){
                                item = "{" + item;
                            }
                            JSONObject tmp = new JSONObject(item);
                            item_list.add(tmp);
                            System.out.println(tmp);
                        }

                        System.out.println("Connection is Successful");
                        System.out.println(builder);

                        //데이터 View에 추가
                        List<Search_data> newItem_list = new ArrayList<>();
                        for (JSONObject item: item_list) {
                            int icon;
                            String color;

                            //score에 따른 아이콘, 색상 변화
                            if(Double.parseDouble(item.get("score").toString()) > 70) {
                                icon = R.drawable.baseline_verified_20;
                                color = "#13A6BA";
                            } else if(Double.parseDouble(item.get("score").toString()) < 40) {
                                icon = R.drawable.baseline_dangerous_24;
                                color= "#F33362";
                            } else {
                                icon = R.drawable.baseline_report_problem_24;
                                color = "#FF9F3E";
                            }


                            Search_data newItem = new Search_data(icon,
                                    item.get("link").toString(),
                                    item.get("title").toString(),
                                    item.get("date").toString(),
                                    item.get("preview").toString(),
                                    color);
                            data.add(newItem);
                            newItem_list.add(newItem);
                        }

                        adapter.addItem(newItem_list, adapter.position);

                    } else {
                        System.out.println("-----------------connector error");
                        System.out.println(conn.getResponseCode());
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
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