package com.example.musicplayer.Adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.musicplayer.PlayerActivity;
import com.example.musicplayer.R;
import com.example.musicplayer.Secondaries.Music;

import java.util.ArrayList;

public class MusicsRecyclerAdapter extends RecyclerView.Adapter<MusicsRecyclerAdapter.vh>{
    private ArrayList<Music> list = new ArrayList<>();
    private Context context;

    public MusicsRecyclerAdapter(Context context, ArrayList<Music> list) {
        this.list = list;
        this.context = context;
    }

    public MusicsRecyclerAdapter() {
        super();
    }

    @NonNull
    @Override
    public vh onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new vh(LayoutInflater.from(context).inflate(R.layout.musics_list_item, null, false));
    }

    @Override
    public void onBindViewHolder(@NonNull vh holder, int position) {
        Bitmap bitmap = list.get(position).getBitmap();
        if (bitmap!=null){
            holder.img.setImageBitmap(bitmap);
        } else{
            holder.img.setImageResource(R.drawable.img);
        }

        holder.musicName.setText(list.get(position).getTitle());
        holder.albumName.setText(list.get(position).getAlbum());
        holder.duration.setText(longToMinutes(list.get(position).getDuration()));

        holder.container.setOnClickListener(click->{
            context.startActivity(new Intent(context.getApplicationContext(), PlayerActivity.class)
                    .putExtra("index",position));
        });
    }

    String longToMinutes(long value){
        long minutes = value/ 60000;
        long seconds = (value/ 1000) % 60;

        return String.format("%02d:%02d", minutes, seconds);
    }
    @Override
    public int getItemCount() {
        return list.size();
    }
    public static class vh extends RecyclerView.ViewHolder{
        ImageView img;
        TextView musicName, albumName, duration;
        CardView container;
        public vh(@NonNull View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.MusicAlbum);
            musicName = itemView.findViewById(R.id.MusicName);
            albumName = itemView.findViewById(R.id.AlbumName);
            duration = itemView.findViewById(R.id.duration);
            container = itemView.findViewById(R.id.musicContainer);
        }
    }
}
