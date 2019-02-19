package com.example.android.popularmovies_stageone.Database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.example.android.popularmovies_stageone.MovieJsonResponce;

import java.util.List;

@Dao
public interface FavouriteDao {

    @Insert
    void insert(MovieJsonResponce favourite);

    @Query("SELECT * FROM fav_table")
    LiveData<List<MovieJsonResponce>>getAllMovies();

    @Delete
    void deleteMovie(MovieJsonResponce favourite);

    @Query("SELECT favId FROM fav_table WHERE favId = :id")
    LiveData<String> loadMovieById(String id);




}
