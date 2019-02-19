package com.example.android.popularmovies_stageone.Database;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import com.example.android.popularmovies_stageone.MovieJsonResponce;

import java.util.List;

public class FavRepository {
    private FavouriteDao favouriteDao;
    private LiveData<List<MovieJsonResponce>> mAlMovies;
    FavRepository(Application application){
        FavRoomDatabase db = FavRoomDatabase.getDatabase(application);
        favouriteDao = db.favouriteDao();
        mAlMovies = favouriteDao.getAllMovies();
    }
    public LiveData<List<MovieJsonResponce>> getmAlMovies(){
        return mAlMovies;
    }



    public void insert(MovieJsonResponce favourite){
        new insertAsyncTask(favouriteDao).execute(favourite);
    }
    public void delete(MovieJsonResponce favourite){
        new DeleteAsyncTask(favouriteDao).execute(favourite);
    }


    private static class insertAsyncTask extends AsyncTask<MovieJsonResponce,Void,Void>{
        private FavouriteDao mAsyncFavouriteDao;

        insertAsyncTask(FavouriteDao dao){
            mAsyncFavouriteDao = dao;
        }

        @Override
        protected Void doInBackground(MovieJsonResponce... favourites) {
            mAsyncFavouriteDao.insert(favourites[0]);
            return null;
        }
    }
    private static class DeleteAsyncTask extends AsyncTask<MovieJsonResponce,Void,Void>{
        private FavouriteDao mAsyncFavouriteDao;

        DeleteAsyncTask(FavouriteDao dao){
            mAsyncFavouriteDao = dao;
        }

        @Override
        protected Void doInBackground(MovieJsonResponce... favourites) {
            mAsyncFavouriteDao.deleteMovie(favourites[0]);
            return null;
        }
    }
}
