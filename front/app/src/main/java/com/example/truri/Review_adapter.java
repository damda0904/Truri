package com.example.truri;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class Review_adapter extends RecyclerView.Adapter<Review_adapter.CustomViewHolder>{

    private ArrayList<Review_data> arrayList;


    public Review_adapter(ArrayList<Review_data> arrayList) {
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
                                //추후 수정페이지로 이동
                                Toast.makeText(holder.itemView.getContext(), "수정되었습니다", Toast.LENGTH_SHORT).show();
                                return true;
                            case R.id.delete:
                                remove(position);
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



    public void remove(int position){
        try{
            arrayList.remove(position);
            notifyItemRemoved(position);
        } catch(IndexOutOfBoundsException exception){
            exception.printStackTrace();
        }
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