package com.example.truri;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.truri.middleware.AsyncGet;
import com.example.truri.middleware.Connector;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class Search_Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;

    private List<Search_data> items;
    private Context context, mContext;

    public Search_Adapter(List<Search_data> items) {
        this.items = items;
        this.mContext = context;
    }



    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_ITEM) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_search_items, parent, false);
            return new ItemViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_loading, parent, false);
            return new LoadingViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ItemViewHolder) {
            populateItemRows((ItemViewHolder) holder, position);
        } else if (holder instanceof LoadingViewHolder) {
            showLoadingView((LoadingViewHolder) holder, position);
        }


        //todo 웹뷰 이동
        holder.itemView.setTag(position); //커스텀 뷰의 각각의 리스트를 의미
        //holder.url.setText(items.get(position).getLink());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //String item_url = (String) holder.url.getText().toString();

                Intent intent;
                intent = new Intent(context, WebPage.class); //웹뷰 화면 연결
                //intent.putExtra("LINK", item_url); //변수값 인텐트로 넘기기
                context.startActivity(intent);
            }
    });
    }

    public void onClickShowAlert() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        builder.setMessage("북마크 기능은 로그인이 필요합니다.\n로그인 하시겠습니까?");
        builder.setPositiveButton("네!", new DialogInterface.OnClickListener(){

            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                context.startActivity(new Intent(context.getApplicationContext(), LoginPage.class));
            }
        });
        builder.setNegativeButton("괜찮아요", null);

        builder.show();
    }

    @Override
    public int getItemViewType(int position) {
        return items.get(position) == null ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;
    }

    @Override
    public int getItemCount() {
        return items == null ? 0 : items.size();
    }

    private void showLoadingView(LoadingViewHolder holder, int position) {

    }

    private void populateItemRows(ItemViewHolder holder, int position) {
        Search_data item = items.get(position);
        holder.setItem(item);

        context = holder.itemView.getContext();

        //sharedPreferences 불러오기
        SharedPreferences jwt =  context.getSharedPreferences("JWT", MODE_PRIVATE);

        //토큰 불러오기
        String token = jwt.getString("token", "");

        //TODO 북마크 테스트
        //북마크버튼 클릭 설정
        holder.bookmark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!holder.bookmark.isSelected())
                {
                    //TODO 이미 북마크 되어있는지 확인
                    //북마크 추가
                    //토큰이 존재하는지 확인
                    if(token.equals("")) {
                        System.out.println("Call show alert");
                        onClickShowAlert();
                        return;
                    }

                    String item_url = (String) holder.url.getText();
                    String title_text = (String) holder.title.getText();
                    String content_text = (String) holder.content.getText();
                    int level = items.get(position).getLevel();

                    JSONObject body = new JSONObject();
                    try {
                        body.put("url", item_url);
                        body.put("title", title_text);
                        body.put("preview", content_text);
                        body.put("level", level);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    String body_string = body.toString();

                    //post 요청
                    String url = "http://10.0.2.2:8080/bookmark/";
                    JSONObject result = null;
                    try {
                        result = new AsyncGet().execute(url, body_string, token).get();

                        //id 저장
                        items.get(position).setId(Long.valueOf(result.get("id").toString()));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    if(result != null){
                        holder.bookmark.setSelected(true);
                    }
                }
                else
                {
                    //북마크 삭제
                    long id = items.get(position).getId();

                    if(id == -1) {
                        return;
                    }

                    try {
                        URL delete_url = new URL("http://10.0.2.2:8080/bookmark?bno=" + id);

                        AsyncTask.execute(new Runnable() {
                            @Override
                            public void run() {
                                Connector connector = new Connector();
                                connector.delete(delete_url);
                            }
                        });
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                        return;
                    }

                    holder.bookmark.setSelected(false);
                }
            }
        });

        //평가버튼 클릭 설정
        holder.review.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(new Intent(context.getApplicationContext(), ReviewGradePage.class));
            }
        });


    }

    private class ItemViewHolder extends RecyclerView.ViewHolder {
        protected ImageView reliability_icon, image;
        protected TextView title, content, url, date;
        protected ImageButton bookmark, review;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.title);
            content = itemView.findViewById(R.id.content);
            url = itemView.findViewById(R.id.url);
            reliability_icon = itemView.findViewById(R.id.reliability_icon);
            date = itemView.findViewById(R.id.date);
            image = itemView.findViewById(R.id.image);

            this.bookmark = itemView.findViewById(R.id.bookmark_icon);
            this.review = itemView.findViewById(R.id.grade_icon);

//            //웹뷰 이동
//            itemView.setClickable(true);
//            itemView.setOnClickListener(new View.OnClickListener(){
//                @Override
//                public void onClick(View v) {
//                    int pos = getAdapterPosition();
//                    if(pos != RecyclerView.NO_POSITION){
//                        Intent intent= new Intent(mContext, WebPage.class);
//                        mContext.startActivity(intent);
//                    }
//                }
//            });
        }

        public void setItem(Search_data item) {
            title.setText(item.getTitle());
            content.setText(item.getContent());
            url.setText(item.getLink());
            reliability_icon.setImageResource(item.getReliability_icon());
            date.setText(item.getDate());
            image.setImageResource(item.getImage());
        }
    }

    private class LoadingViewHolder extends RecyclerView.ViewHolder {
        private ProgressBar progressBar;

        public LoadingViewHolder(@NonNull View itemView) {
            super(itemView);
            progressBar = itemView.findViewById(R.id.progressBar);
        }
    }
}