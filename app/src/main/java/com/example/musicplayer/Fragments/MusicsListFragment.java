package com.example.musicplayer.Fragments;

import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.musicplayer.Adapters.AlbumsRecyclerAdapter;
import com.example.musicplayer.Adapters.MusicsRecyclerAdapter;
import com.example.musicplayer.R;
import com.example.musicplayer.Secondaries.Extractor;
import com.example.musicplayer.Secondaries.Music;
import com.example.musicplayer.databinding.FragmentMusicsListBinding;
import com.google.android.material.carousel.CarouselLayoutManager;
import com.google.android.material.carousel.CarouselSnapHelper;
import com.google.android.material.carousel.HeroCarouselStrategy;
import com.google.android.material.color.ThemeUtils;

import java.util.ArrayList;

public class MusicsListFragment extends Fragment {
FragmentMusicsListBinding binding;
    private ArrayList<Music> audiosList;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentMusicsListBinding.inflate(inflater,container,false);
        int currentTheme = getResources().getConfiguration()
                .uiMode & Configuration.UI_MODE_NIGHT_MASK;
        if (currentTheme == Configuration.UI_MODE_NIGHT_NO) {
            binding.back.setBackgroundColor(getResources().getColor(R.color.Rosa50));
            binding.s.setBackgroundColor(getResources().getColor(R.color.Rosa50));
        }

        audiosList = new Extractor(getActivity().getApplicationContext()).getAudios(false);

        CarouselSnapHelper carouselSnapHelper = new CarouselSnapHelper();
        carouselSnapHelper.attachToRecyclerView(binding.Recycler);

        binding.Recycler.setLayoutManager(new CarouselLayoutManager
                (new HeroCarouselStrategy(),CarouselLayoutManager.HORIZONTAL));
        binding.Recycler.setAdapter(
                new AlbumsRecyclerAdapter(getContext(),
                        new Extractor(getContext()).catchAlbumArt(audiosList, false)));

        binding.MusicsRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.MusicsRecycler.setAdapter(
                new MusicsRecyclerAdapter(
                        getContext(),audiosList
                ));
        return binding.getRoot();
    }
}