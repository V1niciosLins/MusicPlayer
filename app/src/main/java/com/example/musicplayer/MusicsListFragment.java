package com.example.musicplayer;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.musicplayer.Adapters.AlbumsRecyclerAdapter;
import com.example.musicplayer.Secondaries.Extractor;
import com.example.musicplayer.Secondaries.Music;
import com.example.musicplayer.databinding.FragmentMusicsListBinding;
import com.google.android.material.carousel.CarouselLayoutManager;
import com.google.android.material.carousel.CarouselSnapHelper;
import com.google.android.material.carousel.HeroCarouselStrategy;

import java.util.ArrayList;

public class MusicsListFragment extends Fragment {
FragmentMusicsListBinding binding;
    private ArrayList<Music> audiosList;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentMusicsListBinding.inflate(inflater,container,false);

        audiosList = new Extractor(getActivity().getApplicationContext()).getAudios(false);

        CarouselSnapHelper carouselSnapHelper = new CarouselSnapHelper();
        carouselSnapHelper.attachToRecyclerView(binding.Recycler);

        binding.Recycler.setLayoutManager(new CarouselLayoutManager
                (new HeroCarouselStrategy(),CarouselLayoutManager.HORIZONTAL));
        binding.Recycler.setAdapter(
                new AlbumsRecyclerAdapter(getContext(),
                        new Extractor(getContext()).catchAlbumArt(audiosList, false)));
        return binding.getRoot();
    }
}