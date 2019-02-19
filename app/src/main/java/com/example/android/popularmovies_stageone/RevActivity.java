package com.example.android.popularmovies_stageone;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.NavUtils;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.popularmovies_stageone.Utils.NetworkUtils;

import java.net.URL;
import java.util.ArrayList;

public class RevActivity extends AppCompatActivity implements LoaderCallbacks<ArrayList<ReviewJsonResponce>> {

    private static  String MovieID;
    private static  String MovieTitle;
    private static final int Reviews_Loader_ID = 55;
    private RecyclerView recyclerView;
    private ReviewRecyclerView adapter;
    TextView title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rev);
        Intent intent =  getIntent();
        if (intent.hasExtra(getString(R.string.movieID)) && intent.hasExtra(getString(R.string.title))){
            MovieID = intent.getStringExtra(getString(R.string.movieID));
            MovieTitle = intent.getStringExtra(getString(R.string.title));

        }

        title = findViewById(R.id.RevTitleTV);
        title.setText(MovieTitle);
        recyclerView = findViewById(R.id.rv_rev);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(layoutManager);

        ActionBar actionBar = this.getSupportActionBar();
        if (actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }



        getSupportLoaderManager().initLoader(Reviews_Loader_ID,null,RevActivity.this).forceLoad();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home ){
            Intent intent = new Intent(this,DetailFragment.class);
            NavUtils.navigateUpTo(this,intent);
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressLint("StaticFieldLeak")
    @NonNull
    @Override
    public Loader<ArrayList<ReviewJsonResponce>> onCreateLoader(int i, @Nullable Bundle bundle) {
        return new AsyncTaskLoader<ArrayList<ReviewJsonResponce>>(this) {

            @Override
            protected void onStartLoading() {
                super.onStartLoading();
            }

            @Nullable
            @Override
            public ArrayList<ReviewJsonResponce> loadInBackground() {
                String api_key = "api_key";
                final String REVIEWS_BASE_URL = getString(R.string.Base_URL);
                final String API_KEY = getString(R.string.API_KEY);

                final String ReviewsJsonString;
                try{
                    Uri uri = Uri.parse(REVIEWS_BASE_URL).buildUpon().appendEncodedPath(MovieID)
                            .appendEncodedPath("reviews").appendQueryParameter(api_key,API_KEY).build();
                    URL url = new URL(uri.toString());
                    Log.v("the Reviews URL is",url.toString());
                    ReviewsJsonString = NetworkUtils.getHttpUrl(url);
                    ArrayList<ReviewJsonResponce> responce = NetworkUtils.getReviewsDataFromJson(ReviewsJsonString);
                    return responce;
                }
                catch (Exception e){
                    e.printStackTrace();
                }
                return null;
            }
        };
    }

    @Override
    public void onLoadFinished(@NonNull Loader<ArrayList<ReviewJsonResponce>> loader, ArrayList<ReviewJsonResponce> reviewJsonResponces) {
        adapter = new ReviewRecyclerView(this,reviewJsonResponces);
        if (reviewJsonResponces == null){
            Log.e("Error review","Check the string");
        }
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onLoaderReset(@NonNull Loader<ArrayList<ReviewJsonResponce>> loader) {

    }
}
