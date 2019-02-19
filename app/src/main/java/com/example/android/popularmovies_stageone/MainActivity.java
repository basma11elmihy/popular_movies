package com.example.android.popularmovies_stageone;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {

    private SharedPreferences sharedPreferences;
    private String Pref_key;
    private String Popular;
    private String Favourite;
    private String TopRated;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Pref_key = getString(R.string.pref_key);
        Popular = getString(R.string.popular_value);
        Favourite = getString(R.string.fav_value);
        TopRated = getString(R.string.topRated_value);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        final String pref = sharedPreferences.getString(Pref_key,Popular);
        if (savedInstanceState == null) {
            if (pref.equals(Favourite)) {
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.container, new FavFragment())
                        .commit();
            }

            else if (pref.equals(Popular) || pref.equals(TopRated)){
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.container, new MainFragment())
                        .commit();
            }
            else{
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.container, new MainFragment())
                        .commit();
            }

        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.sort_popular){
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(Pref_key,Popular);
            editor.apply();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, new MainFragment())
                    .commit();
        }
        if (id == R.id.sort_topRated){
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(Pref_key,TopRated);
            editor.apply();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, new MainFragment())
                    .commit();
        }

        if (id == R.id.sort_fav){
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(Pref_key,Favourite);
            editor.apply();
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container, new FavFragment())
                        .commit();
        }
        return super.onOptionsItemSelected(item);
    }
}
