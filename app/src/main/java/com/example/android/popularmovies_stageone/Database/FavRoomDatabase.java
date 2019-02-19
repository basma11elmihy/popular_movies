package com.example.android.popularmovies_stageone.Database;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.migration.Migration;
import android.content.Context;
import android.support.annotation.NonNull;

import com.example.android.popularmovies_stageone.MovieJsonResponce;

@Database(entities = {MovieJsonResponce.class},version = 1,exportSchema = false)
public abstract class FavRoomDatabase extends RoomDatabase {

    public abstract FavouriteDao favouriteDao();
    private static volatile FavRoomDatabase INSTANCE;

    public static FavRoomDatabase getDatabase(final Context context){
        if (INSTANCE == null){
            synchronized (FavRoomDatabase.class){
                if (INSTANCE == null){
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),FavRoomDatabase.class
                    ,"favo_database").build();
                }
            }
        }
        return INSTANCE;
    }

}
