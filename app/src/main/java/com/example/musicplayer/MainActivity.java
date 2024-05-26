package com.example.musicplayer;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.OptIn;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.media3.common.util.UnstableApi;

import com.example.musicplayer.Adapters.VP2MainAdapter;
import com.example.musicplayer.Fragments.FavoritesFragment;
import com.example.musicplayer.Fragments.MusicsListFragment;
import com.example.musicplayer.Fragments.PlaylistsFragment;
import com.example.musicplayer.databinding.ActivityMainBinding;

import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity {
ActivityMainBinding binding;
private final int RequestPermissionCode = 1;

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
        binding.ViewPager2.setUserInputEnabled(false);

        binding.ViewPager2.setAdapter(
                new VP2MainAdapter(this,
                        new ArrayList<>(Arrays.asList(
                                new MusicsListFragment(),
                                new PlaylistsFragment(),
                                new FavoritesFragment()))));

        binding.BottomNavView.setOnItemSelectedListener(menuItem -> {
            switch (menuItem.getTitle().charAt(0)){
                case 'M': binding.ViewPager2.setCurrentItem(0); break;
                case 'P': binding.ViewPager2.setCurrentItem(1); break;
                case 'F': binding.ViewPager2.setCurrentItem(2); break;
            }
            return true;
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