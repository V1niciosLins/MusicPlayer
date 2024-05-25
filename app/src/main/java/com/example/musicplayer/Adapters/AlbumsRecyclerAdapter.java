package com.example.musicplayer.Adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.musicplayer.R;

import java.util.ArrayList;

public class AlbumsRecyclerAdapter extends RecyclerView.Adapter<AlbumsRecyclerAdapter.vh>{
private ArrayList<Bitmap> list = new ArrayList<>();
private Context context;

    public AlbumsRecyclerAdapter(Context context, ArrayList<Bitmap> list) {
        this.list = list;
        this.context = context;
    }

    public AlbumsRecyclerAdapter() {
        super();
    }

    @NonNull
    @Override
    public vh onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new vh(LayoutInflater.from(context).inflate(R.layout.list_item, null, false));
    }

    @Override
    public void onBindViewHolder(@NonNull vh holder, int position) {
        Bitmap bitmap = list.get(position);
        if (bitmap!=null){
            holder.img.setImageBitmap(bitmap);
        } else{
            holder.img.setImageResource(R.drawable.img);
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
    public static class vh extends RecyclerView.ViewHolder{
        ImageView img;
        public vh(@NonNull View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.carousel_image_view);
        }
    }
}
