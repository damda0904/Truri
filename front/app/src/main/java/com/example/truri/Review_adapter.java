package com.example.truri;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.truri.middleware.Connector;

import org.w3c.dom.Text;

import java.net.URL;
import java.util.ArrayList;

public class Review_adapter extends RecyclerView.Adapter<Review_adapter.CustomViewHolder>{

    private ArrayList<Review_data> arrayList;
    private Context context;

    private Connector connector = new Connector();


    public Review_adapter(Context context, ArrayList<Review_data> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_review_items, parent, false);
        Review_adapter.CustomViewHolder holder = new Review_adapter.CustomViewHolder(view);

        return new CustomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder holder, int position) {
        holder.reliability_icon.setImageResource(arrayList.get(position).getReliability_icon());
        holder.title.setText(arrayList.get(position).getTitle());
        holder.title.setTextColor(Color.parseColor(arrayList.get(position).getColor()));
        holder.review.setText(arrayList.get(position).getReview());
        holder.circle_icon.setImageResource(arrayList.get(position).getCircle_icon());

        holder.itemView.setTag(position);
        holder.itemView.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                //url연결
            }
        });

        holder.more.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                PopupMenu popup = new PopupMenu(holder.itemView.getContext(), holder.more);
                popup.inflate(R.menu.review_menu);

                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.modify:
                                //TODO: 리뷰 수정 페이지로 이동
                                //context.startActivity(new Intent(view.getContext(), ReviewGradePage.class));
                                return true;
                            case R.id.delete:

                                AlertDialog.Builder builder = new AlertDialog.Builder(holder.itemView.getContext());

                                builder.setMessage("정말 삭제하시겠습니까?");
                                builder.setPositiveButton("네", new DialogInterface.OnClickListener(){

                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {

                                        remove(position, arrayList.get(position).getId());
                                    }
                                });

                                builder.setNegativeButton("아니오", new DialogInterface.OnClickListener(){
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) { }
                                });

                                builder.show();

                                return true;
                            default:
                                return false;
                        }
                    }
                });
                popup.show();
            }

        });

    }


    @Override
    public int getItemCount() {
        return (null != arrayList ? arrayList.size() : 0);
    }



    public void remove(int position, long ono) {

        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                try{
                    URL url = new URL("http://10.0.2.2:8080/opinion?ono=" +  Long.toString(ono));
                    connector.delete(url);
                } catch(Exception exception){
                    exception.printStackTrace();
                    return;
                }
            }
        });

        arrayList.remove(position);
        notifyItemRemoved(position);
    }


    public class CustomViewHolder extends RecyclerView.ViewHolder {

        protected ImageView reliability_icon, circle_icon, more;
        protected TextView title, content, review;

        public CustomViewHolder(@NonNull View itemView) {
            super(itemView);

            this.reliability_icon = (ImageView) itemView.findViewById(R.id.reliability_icon);
            this.title = itemView.findViewById(R.id.title);
            this.content = itemView.findViewById(R.id.content);
            this.circle_icon = itemView.findViewById(R.id.circle);
            this.review = itemView.findViewById(R.id.review);
            this.more = itemView.findViewById(R.id.more);

        }

    }
}