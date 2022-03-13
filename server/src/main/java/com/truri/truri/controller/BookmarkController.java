Skip to content
        Search or jump to…
        Pull requests
        Issues
        Marketplace
        Explore

@damda0904 
damda0904
        /
        Truri
        Public
        Code
        Issues
        Pull requests
        Actions
        Projects
        Wiki
        Security
        Insights
        Settings
        Truri/front/app/src/main/java/com/example/truri/SearchPage.java /
@nueeaz
nueeaz dummy searchPage
        Latest commit 7af0618 20 minutes ago
        History
        2 contributors
@nueeaz@damda0904
92 lines (75 sloc)  2.94 KB

        package com.example.truri;

        import android.os.Bundle;
        import android.os.Handler;

        import androidx.annotation.NonNull;
        import androidx.appcompat.app.AppCompatActivity;
        import androidx.recyclerview.widget.LinearLayoutManager;
        import androidx.recyclerview.widget.RecyclerView;

        import java.util.ArrayList;

public class SearchPage extends AppCompatActivity {
    private RecyclerView recyclerView;
    private Search_Adapter adapter;
    private ArrayList<String> items = new ArrayList<>();

    private boolean isLoading = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_page);

        recyclerView = findViewById(R.id.recyclerView);
        populateData();
        initAdapter();
        initScrollListener();
    }

    private void populateData() {
        for (int i=0; i<30; i++) {
            items.add("Item " + i);
        }
    }

    private void initAdapter() {
        adapter = new Search_Adapter(items);
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

                while (currentSize - 1 < nextLimit) {
                    items.add("Item " + currentSize);
                    currentSize++;
                }

                adapter.notifyDataSetChanged();
                isLoading = false;
            }
        }, 2000); //todo 스크롤 로딩시간 (현재 2초)
    }
}
© 2022 GitHub, Inc.
        Terms
        Privacy
        Security
        Status
        Docs
        Contact GitHub
        Pricing
        API
        Training
        Blog
        About
        9 results found.