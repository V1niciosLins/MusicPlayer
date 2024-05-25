package com.example.musicplayer;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ContentUris;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.util.Size;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.OptIn;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.media3.common.MediaItem;
import androidx.media3.common.Player;
import androidx.media3.common.util.UnstableApi;
import androidx.media3.common.util.Util;
import androidx.media3.exoplayer.ExoPlayer;
import androidx.recyclerview.widget.RecyclerView;

import com.example.musicplayer.databinding.ActivityMainBinding;
import com.google.android.material.carousel.CarouselLayoutManager;
import com.google.android.material.carousel.CarouselSnapHelper;
import com.google.android.material.carousel.FullScreenCarouselStrategy;
import com.google.android.material.carousel.HeroCarouselStrategy;
import com.google.android.material.carousel.MultiBrowseCarouselStrategy;
import com.google.android.material.carousel.UncontainedCarouselStrategy;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {
ActivityMainBinding binding;
private final int RequestPermissionCode = 1;
private ArrayList<Audio> audiosList;
private ExoPlayer player;
    @OptIn(markerClass = UnstableApi.class)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        
        if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)== PackageManager.PERMISSION_DENIED){
            Toast.makeText(this, "Sem permissão para acessar armazenamento", Toast.LENGTH_SHORT).show();
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},RequestPermissionCode);
        }
        audiosList = new ArrayList<>();
        audiosList = getAudios();

        Toast.makeText(this, "Músicas encontradas: "+audiosList.size(), Toast.LENGTH_SHORT).show();
        List<MediaItem> list = new ArrayList<>();
        for (Audio a : audiosList){
            list.add(MediaItem.fromUri(Uri.parse(a.getData())));
        }

        player = new ExoPlayer.Builder(this).build();

        player.setMediaItems(list,true);

        /*binding.PlayButton.setOnClickListener(click-> {


            boolean shouldShowPlayButton = Util.shouldShowPlayButton(player);

            if (shouldShowPlayButton){
                Util.handlePlayButtonAction(player);
            } else {
                Util.handlePauseButtonAction(player);
            }
            updatePlayPause(shouldShowPlayButton);
        });*/
        CarouselSnapHelper carouselSnapHelper = new CarouselSnapHelper();
        carouselSnapHelper.attachToRecyclerView(binding.Recycler);

        binding.Recycler.setLayoutManager(new CarouselLayoutManager
                (new HeroCarouselStrategy(),CarouselLayoutManager.HORIZONTAL));
        binding.Recycler.setAdapter(
                new RecyclerAdapter(this,
                        audiosList));

        player.addListener(new Player.Listener() {
            @Override
            public void onEvents(Player player, Player.Events events) {
                if (events.containsAny(
                        Player.EVENT_PLAY_WHEN_READY_CHANGED,
                        Player.EVENT_PLAYBACK_STATE_CHANGED,
                        Player.EVENT_PLAYBACK_SUPPRESSION_REASON_CHANGED,
                        Player.EVENT_IS_PLAYING_CHANGED)) {
                    Toast.makeText(MainActivity.this, "Evento aconteceu", Toast.LENGTH_SHORT).show();
                }
                if (events.containsAny(Player.EVENT_REPEAT_MODE_CHANGED)) {
                    Toast.makeText(MainActivity.this, "Repeat", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    void updatePlayPause(boolean b){
            //binding.PlayButton.setText(b? "=":">");
    }

    @SuppressLint("Range")
    ArrayList<Audio> getAudios() {
        String sortOrder = MediaStore.Audio.Media.TITLE + " ASC";
        String selection = MediaStore.Audio.Media.IS_MUSIC + "!= 0";
        ArrayList<Audio> arrayList = new ArrayList<>();
        Bitmap bm;
        @SuppressLint("Recycle") Cursor cursor = getApplicationContext()
                .getContentResolver().
                query(
                        MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                        null,
                        selection,
                        null,
                        sortOrder
                );
        int index =0;
        if (cursor!=null && cursor.getCount()>0) {
            while (cursor.moveToNext()){
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {

                    Uri albumArt = ContentUris.withAppendedId(
                            MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                            cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.Albums._ID))
                    );
                    if (albumArt!=null){
                        try {
                            bm =getApplicationContext().getContentResolver().
                                    loadThumbnail(albumArt, new Size(3000,3000),null);
                        } catch (IOException e) {
                            bm = null;
                        }
                    } else{
                        Toast.makeText(this, "na", Toast.LENGTH_SHORT).show();
                        bm =null;
                    }

                    arrayList.add(new Audio(
                            cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE)),
                            cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST)),
                            cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM)),
                            cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA)),
                            bm
                    ));
                }
                index++;
            }

        }
        if (arrayList.isEmpty()) Toast.makeText(this, "Nada foi adicionado", Toast.LENGTH_SHORT).show();
        return (!arrayList.isEmpty()) ? arrayList: new ArrayList<>();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == RequestPermissionCode){
            if (grantResults[0] == PackageManager.PERMISSION_DENIED){
                Toast.makeText(this,
                        "Infelizente precisamos da permissão para achar suas músicas.\nLamentamos",
                        Toast.LENGTH_SHORT).show();
            }

        }
    }
}

class Audio{
    private String Title, Artista, Album, Data;
    private Bitmap bitmap;

    public Audio() {
        super();
    }

    public Audio(String title, String artista, String album, String data, Bitmap bitmap) {
        Title = title;
        Artista = artista;
        Album = album;
        Data = data;
        this.bitmap = bitmap;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getArtista() {
        return Artista;
    }

    public void setArtista(String artista) {
        Artista = artista;
    }

    public String getAlbum() {
        return Album;
    }

    public void setAlbum(String album) {
        Album = album;
    }

    public String getData() {
        return Data;
    }

    public void setData(String data) {
        Data = data;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }
}

class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.vh>{
private ArrayList<Audio> list = new ArrayList<>();
private Context context;

    public RecyclerAdapter(Context context, ArrayList<Audio> list) {
        this.list = list;
        this.context = context;
    }

    public RecyclerAdapter() {
        super();
    }

    @NonNull
    @Override
    public RecyclerAdapter.vh onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new vh(LayoutInflater.from(context).inflate(R.layout.list_item, null, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerAdapter.vh holder, int position) {
        Bitmap bitmap = list.get(position).getBitmap();
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