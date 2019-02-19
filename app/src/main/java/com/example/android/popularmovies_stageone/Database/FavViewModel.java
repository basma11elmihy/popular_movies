package com.example.android.popularmovies_stageone.Database;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;

import com.example.android.popularmovies_stageone.MovieJsonResponce;

import java.util.List;

public class FavViewModel extends AndroidViewModel {
    private FavRepository mRepository;
    private LiveData<List<MovieJsonResponce>> mAllMovies;

    public FavViewModel(Application application){
        super(application);
        mRepository = new FavRepository(application);
        mAllMovies = mRepository.getmAlMovies();
    }

    public LiveData<List<MovieJsonResponce>> getAllMovies() {
        return mAllMovies;
    }


    public void insert(MovieJsonResponce favourite){
        mRepository.insert(favourite);
    }
    public void delete(MovieJsonResponce favourite){
        mRepository.delete(favourite);
    }

}
