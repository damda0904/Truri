package com.example.truri;

import android.content.DialogInterface;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class Bookmark_Adapter extends RecyclerView.Adapter<Bookmark_Adapter.CustomViewHolder>{

    private ArrayList<Bookmark_data> arrayList;

    public Bookmark_Adapter(ArrayList<Bookmark_data> arrayList) {
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_bookmark_items, parent, false);
        CustomViewHolder holder = new CustomViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder holder, int position) {
        holder.reliability_icon.setImageResource(arrayList.get(position).getReliability_icon());
        holder.title.setText(arrayList.get(position).getTitle());
        holder.title.setTextColor(Color.parseColor(arrayList.get(position).getColor()));
        holder.content.setText(arrayList.get(position).getContent());

        holder.itemView.setTag(position);
        holder.itemView.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                //url연결
            }
        });

        holder.close.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(holder.itemView.getContext());

                builder.setMessage("정말 삭제하시겠습니까?");
                builder.setPositiveButton("네", new DialogInterface.OnClickListener(){

                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        remove(holder.getAdapterPosition());
                    }
                });

                builder.setNegativeButton("아니오", new DialogInterface.OnClickListener(){

                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });

                builder.show();

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

    public class CustomViewHolder extends RecyclerView.ViewHolder{

        protected ImageView reliability_icon, close;
        protected TextView title, content;

        public CustomViewHolder(@NonNull View itemView) {
            super(itemView);
            this.reliability_icon=(ImageView) itemView.findViewById(R.id.reliability_icon);
            this.title = itemView.findViewById(R.id.title);
            this.content = itemView.findViewById(R.id.content);
            this.close = itemView.findViewById(R.id.close);
        }
    }

}
