package com.example.musicplayer;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ContentUris;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.util.Size;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.OptIn;
import androidx.appcompat.app.AppCompatActivity;
import androidx.media3.common.MediaItem;
import androidx.media3.common.Player;
import androidx.media3.common.util.UnstableApi;
import androidx.media3.exoplayer.ExoPlayer;

import com.example.musicplayer.Adapters.AlbumsRecyclerAdapter;
import com.example.musicplayer.Secondaries.Music;
import com.example.musicplayer.databinding.ActivityMainBinding;
import com.google.android.material.carousel.CarouselLayoutManager;
import com.google.android.material.carousel.CarouselSnapHelper;
import com.google.android.material.carousel.HeroCarouselStrategy;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import com.example.musicplayer.Secondaries.Extractor;
public class MainActivity extends AppCompatActivity {
ActivityMainBinding binding;
private final int RequestPermissionCode = 1;
private ArrayList<Music> audiosList;
private ExoPlayer player;
    @OptIn(markerClass = UnstableApi.class)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        EdgeToEdge.enable(this);

        
        if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)== PackageManager.PERMISSION_DENIED){
            Toast.makeText(this, "Sem permissão para acessar armazenamento", Toast.LENGTH_SHORT).show();
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},RequestPermissionCode);
        }
        audiosList = new Extractor(getApplicationContext()).getAudios(false);

        List<MediaItem> list = new ArrayList<>();
        for (Music a : audiosList){
            list.add(MediaItem.fromUri(Uri.parse(a.getData())));
        }

        player = new ExoPlayer.Builder(this).build();

        player.setMediaItems(list,true);

        CarouselSnapHelper carouselSnapHelper = new CarouselSnapHelper();
        carouselSnapHelper.attachToRecyclerView(binding.Recycler);

        binding.Recycler.setLayoutManager(new CarouselLayoutManager
                (new HeroCarouselStrategy(),CarouselLayoutManager.HORIZONTAL));
        binding.Recycler.setAdapter(
                new AlbumsRecyclerAdapter(this,
                        new Extractor(this).catchAlbumArt(audiosList, false)));

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