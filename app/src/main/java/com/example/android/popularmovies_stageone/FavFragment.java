package com.example.android.popularmovies_stageone;


import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.android.popularmovies_stageone.Database.FavViewModel;

import java.util.List;

public class FavFragment extends Fragment {

    private FavViewModel viewModel;
    private RecyclerView recyclerView;
    private FavListAdapter adapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootview = inflater.inflate(R.layout.fragment_fav, container, false);
        recyclerView = rootview.findViewById(R.id.fav_rvGrid);

        recyclerView.setLayoutManager(new GridLayoutManager(getContext(),calculateNoOfColumns(getContext())));

        viewModel = ViewModelProviders.of(this).get(FavViewModel.class);
        viewModel.getAllMovies().observe(this, new Observer<List<MovieJsonResponce>>() {
            @Override
            public void onChanged(@Nullable List<MovieJsonResponce> favourites) {
              adapter = new FavListAdapter(getContext(),favourites);
                recyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }
        });
        return rootview;

    }
    //from previous udacity reviews suggestions
    public static int calculateNoOfColumns(Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        float dpWidth = displayMetrics.widthPixels / displayMetrics.density;
        int scalingFactor = 200;
        int noOfColumns = (int) (dpWidth / scalingFactor);
        if(noOfColumns < 2)
            noOfColumns = 2;
        return noOfColumns;
    }

}
