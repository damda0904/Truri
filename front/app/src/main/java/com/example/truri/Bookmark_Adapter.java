package com.example.truri;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.loader.content.AsyncTaskLoader;
import androidx.recyclerview.widget.RecyclerView;

import com.example.truri.Network.ServerHost;
import com.example.truri.middleware.Connector;

import org.w3c.dom.Text;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class Bookmark_Adapter extends RecyclerView.Adapter<Bookmark_Adapter.CustomViewHolder>{

    private ArrayList<Bookmark_data> arrayList;
    private Context context;

    private Connector connector = new Connector();

    public Bookmark_Adapter(ArrayList<Bookmark_data> arrayList, Context context) {

        this.arrayList = arrayList;
        this.context = context;
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

        String long_url = arrayList.get(position).getUrl();
        String url;
        if(long_url.length() > 40) {
            url = long_url.substring(0, 38) + "...";
        } else {
            url = long_url;
        }
        holder.url.setText(url);

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
                        System.out.println(arrayList.get(position).getId());
                        remove(holder.getAdapterPosition(), arrayList.get(position).getId());
                    }
                });

                builder.setNegativeButton("아니오", new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) { }
                });

                builder.show();

            }
        });

        //웹뷰 이동
        holder.layout.setClickable(true);
        holder.layout.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(position != RecyclerView.NO_POSITION){
                    String link = holder.url.getText().toString();

                    Intent intent= new Intent(context, WebPage.class);
                    intent.putExtra("link", link);
                    context.startActivity(intent.addFlags(FLAG_ACTIVITY_NEW_TASK));
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return (null != arrayList ? arrayList.size() : 0);
    }

    public void remove(int position, long bno){

        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    String localhost = new ServerHost().getHost_url("spring");
                    URL url = new URL(localhost + "/bookmark?bno=" + bno);
                    connector.delete(url);
                } catch (Exception e) {
                    e.printStackTrace();
                    return;
                }
            }
        });

        arrayList.remove(position);
        notifyItemRemoved(position);
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder{

        protected ImageView reliability_icon, close;
        protected TextView title, content, url;
        protected LinearLayout layout;

        public CustomViewHolder(@NonNull View itemView) {
            super(itemView);
            this.reliability_icon=(ImageView) itemView.findViewById(R.id.reliability_icon);
            this.title = itemView.findViewById(R.id.title);
            this.content = itemView.findViewById(R.id.content);
            this.close = itemView.findViewById(R.id.close);
            url = itemView.findViewById(R.id.url);
            layout = itemView.findViewById(R.id.layout);
        }
    }

}
