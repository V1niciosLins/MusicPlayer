package com.example.musicplayer;

import static com.example.musicplayer.Fragments.MusicsListFragment.audiosList;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.musicplayer.Secondaries.Music;
import com.example.musicplayer.databinding.ActivityPlayerBinding;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;

public class PlayerActivity extends AppCompatActivity {
ActivityPlayerBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityPlayerBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main3), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        int index = getIntent().getIntExtra("index", -1);
        if (index ==-1) return;

        binding.albumCover.setImageBitmap(audiosList.get(index).getBitmap());
        binding.artistName.setText(audiosList.get(index).getArtista());
        binding.songName.setText(audiosList.get(index).getTitle());


    }
}