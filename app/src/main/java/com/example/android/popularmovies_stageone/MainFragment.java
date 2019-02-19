package com.example.android.popularmovies_stageone;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.android.popularmovies_stageone.Utils.NetworkUtils;

import java.net.URL;
import java.util.ArrayList;

public class MainFragment extends Fragment implements LoaderManager.LoaderCallbacks<ArrayList<MovieJsonResponce>> ,
        ImageAdapter.ListItemClickListener {

    private RecyclerView mRecyclerView;
    private ImageAdapter mImageAdapter;
    private int LOADER_MOVIES_ID = 33;
    Context context;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        mRecyclerView = rootView.findViewById(R.id.rv_grid);
        context = getActivity();

        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), calculateNoOfColumns(getContext()));
        mRecyclerView.setLayoutManager(layoutManager);


        return rootView;

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

    @Override
    public void onStop() {
        super.onStop();
        getLoaderManager().destroyLoader(LOADER_MOVIES_ID);
    }

    @Override
    public void onStart() {
        super.onStart();
        getLoaderManager().initLoader(LOADER_MOVIES_ID, null, this).forceLoad();
    }

    @SuppressLint("StaticFieldLeak")
    @NonNull
    @Override
    public Loader<ArrayList<MovieJsonResponce>> onCreateLoader(int i, @Nullable Bundle bundle) {
        // context = getContext();
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        final String pref = sharedPreferences.getString("pref", "popular");

            return new AsyncTaskLoader<ArrayList<MovieJsonResponce>>(context) {
                @Override
                protected void onStartLoading() {
                    super.onStartLoading();
                }

                @Nullable
                @Override
                public ArrayList<MovieJsonResponce> loadInBackground() {
                    String api_key = "api_key";
                    final String MoviesBaseUrl = getString(R.string.Base_URL);
                    final String API_KEY = getString(R.string.API_KEY);

                    final String MoviesJsonString;
                    try {

                        Uri buildUri = Uri.parse(MoviesBaseUrl).buildUpon().appendEncodedPath(pref)
                                .appendQueryParameter(api_key, API_KEY).build();
                        URL url = new URL(buildUri.toString());
                        Log.v("The url is", buildUri.toString());
                        MoviesJsonString = NetworkUtils.getHttpUrl(url);
                        ArrayList<MovieJsonResponce> Resulturl = NetworkUtils.getMoviesDataFromJson(MoviesJsonString);
                        return Resulturl;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return null;
                }
            };
    }

    @Override
    public void onLoadFinished(@NonNull Loader<ArrayList<MovieJsonResponce>> loader, ArrayList<MovieJsonResponce> movieJsonResponces) {
        mImageAdapter = new ImageAdapter(getContext(),movieJsonResponces,this  );
        if (movieJsonResponces == null){
            Log.e("error", "MovieJsonResponce is = null");
        }
        mRecyclerView.setAdapter(mImageAdapter);
    }
    @Override
    public void onListItemClick(int ClickedItemIndex) {
        Intent intent = new Intent(getContext(),DetailActivity.class);
        MovieJsonResponce movieJsonResponce = mImageAdapter.getItem(ClickedItemIndex);
        intent.putExtra(getString(R.string.parcel_data),movieJsonResponce);
        startActivity(intent);
    }

    @Override
    public void onLoaderReset(@NonNull Loader<ArrayList<MovieJsonResponce>> loader) {

    }

}
