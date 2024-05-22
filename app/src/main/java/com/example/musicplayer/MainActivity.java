package com.example.musicplayer;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.media.AudioAttributes;
import android.media.AudioRecord;
import android.media.MediaPlayer;
import android.media.MediaScannerConnection;
import android.media.browse.MediaBrowser;
import android.net.Uri;
import android.os.Bundle;
import android.os.storage.StorageManager;
import android.provider.MediaStore;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.OptIn;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.media3.common.MediaItem;
import androidx.media3.common.util.UnstableApi;
import androidx.media3.exoplayer.ExoPlayer;
import androidx.media3.exoplayer.SimpleExoPlayer;

import com.example.musicplayer.databinding.ActivityMainBinding;
import com.google.common.collect.ImmutableList;

import java.io.IOException;
import java.security.Permission;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MainActivity extends AppCompatActivity {
ActivityMainBinding binding;
private final int RequestPermissionCode = 1;
private ArrayList<Audio> audiosList;

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
        audiosList = getAudios();
        List<MediaItem> list = new ArrayList<>();
        for (Audio a : audiosList){
            list.add(MediaItem.fromUri(Uri.parse(a.getData())));
        }

        ExoPlayer player = new ExoPlayer.Builder(this).build();
        binding.player.setPlayer(player);

        player.setMediaItems(list,true);
        player.play();

    }


    @SuppressLint("Range")
    ArrayList<Audio> getAudios(){
        String sortOrder = MediaStore.Audio.Media.TITLE + " ASC";
        String selection = MediaStore.Audio.Media.IS_MUSIC + "!= 0";
        ArrayList<Audio> arrayList = new ArrayList<>();
        @SuppressLint("Recycle") Cursor cursor = getApplicationContext()
                .getContentResolver().
                query(
                        MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                        null,
                        selection,
                        null,
                        sortOrder
                );
        if (cursor!=null && cursor.getCount()>0) {
            while (cursor.moveToNext()){
                arrayList.add(new Audio(
                        cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE)),
                        cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST)),
                        cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM)),
                        cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA))

                ));
            }

        }
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

    public Audio() {
        super();
    }

    public Audio(String title, String artista, String album, String data) {
        Title = title;
        Artista = artista;
        Album = album;
        Data = data;
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
}