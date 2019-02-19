package com.example.android.popularmovies_stageone;

import android.Manifest;
import android.annotation.SuppressLint;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.popularmovies_stageone.Database.FavRoomDatabase;
import com.example.android.popularmovies_stageone.Database.FavViewModel;
import com.example.android.popularmovies_stageone.Utils.NetworkUtils;
import com.squareup.picasso.Picasso;

import java.net.URL;
import java.util.ArrayList;

import static android.support.v4.content.ContextCompat.checkSelfPermission;
import static android.support.v4.content.ContextCompat.getSystemService;


public class DetailFragment extends Fragment  implements LoaderManager.LoaderCallbacks<ArrayList<TrailerJsonResponce>> {
    private String mResponceTitle;
    private String mResponcePoster;
    private String mResponceOverView;
    private String mResponceDate;
    private String mResponceVote;
    public String mResponceId;
    private Button button;
    private ImageView mFavourite;
    public static final int Trailers_Loader_ID = 44;
    public static final int IMAGE_LOADER_ID = 99;
    private RecyclerView mRecyclerView;
    private TrailerRecyclerView adapter;
    private FavViewModel viewModel;
    private FavRoomDatabase database;
    private LiveData<String> Id;
    public TextView TrailersTitle;
    Context context;
    TestImage image;
    String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE};
    int code = 1;





    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_detail, container, false);
        final MovieJsonResponce movieJsonResponce = getActivity().getIntent().getParcelableExtra(getString(R.string.parcel_data));
        context = getContext();
            database = FavRoomDatabase.getDatabase(getContext().getApplicationContext());


            viewModel = ViewModelProviders.of(this).get(FavViewModel.class);


            mResponceTitle = movieJsonResponce.getMovieTitle();
            ((TextView) view.findViewById(R.id.title)).setText(mResponceTitle);

        ImageView posterView = view.findViewById(R.id.poster);
        mResponcePoster = getString(R.string.image_url) + movieJsonResponce.getMoviePoster();

        if (mResponcePoster.contains("storage")) {
                //help with getting image from file from https://stackoverflow.com/questions/4181774/show-image-view-from-file-path
                Bitmap bitmap = BitmapFactory.decodeFile(movieJsonResponce.getMoviePoster());
                posterView.setImageBitmap(bitmap);
            }
        else{
            Picasso.with(getContext()).load(mResponcePoster).into(posterView);
        }


            image = new TestImage(getContext());
            image.setImageUrl(mResponcePoster);

            mResponceVote = movieJsonResponce.getVoteAverage();
            ((TextView) view.findViewById(R.id.vote)).setText(mResponceVote);

            mResponceDate = movieJsonResponce.getReleaseDate();

            button = view.findViewById(R.id.RevsButton);
            mResponceId = movieJsonResponce.getId();
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getContext(), RevActivity.class);
                    intent.putExtra(getString(R.string.movieID), mResponceId);
                    intent.putExtra(getString(R.string.title), mResponceTitle);
                    startActivity(intent);
                }
            });

            mFavourite = view.findViewById(R.id.favImg);
            Id = database.favouriteDao().loadMovieById(mResponceId);

            Id.observe(getActivity(), new Observer<String>() {
                @Override
                public void onChanged(@Nullable String s) {

                    if (s == null) {
                        mFavourite.setImageResource(R.drawable.ic_favorite_border_black_24dp);
                    } else if (s.equals(mResponceId)) {

                        mFavourite.setImageResource(R.drawable.ic_favorite_black_24dp);
                    }
                }
            });
            mFavourite.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    final MovieJsonResponce favourite = new MovieJsonResponce(mResponceTitle, image.getImage_path(), mResponceOverView, mResponceVote,mResponceDate,mResponceId);
                    FavouriteState(favourite);


                }
            });

            ((TextView) view.findViewById(R.id.year)).setText(mResponceDate);
            mResponceOverView = movieJsonResponce.getOverView();
            ((TextView) view.findViewById(R.id.overview)).setText(mResponceOverView);

            mRecyclerView = view.findViewById(R.id.trailer_rv);
            LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
            mRecyclerView.setLayoutManager(layoutManager);
            checkPermission();

            TrailersTitle = view.findViewById(R.id.trailerCaption);
            if (!isNetworkAvailable()){
                TrailersTitle.setVisibility(View.GONE);
                button.setVisibility(View.GONE);

            }

        return view;
        }
    //https://stackoverflow.com/questions/4238921/detect-whether-there-is-an-internet-connection-available-on-android
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }


    public void checkPermission(){

        //permissions :: https://www.youtube.com/watch?v=C8lUdPVSzDk
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.LOLLIPOP) {
            InitLoader();
        }
        if (checkSelfPermission(context,Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                && checkSelfPermission(context,Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
            InitLoader();
        }
        else {
            if (shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE) &&
                    shouldShowRequestPermissionRationale(Manifest.permission.READ_EXTERNAL_STORAGE)){
                Toast.makeText(context,getString(R.string.permissionMsg),Toast.LENGTH_LONG).show();
            }
            requestPermissions(permissions,code);
        }


    }

    private void InitLoader()
    {
        getLoaderManager().initLoader(IMAGE_LOADER_ID,null,image).forceLoad();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (code == requestCode && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            InitLoader();

        }
        else {
            Toast.makeText(context,getString(R.string.permissionMsg),Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        getLoaderManager().destroyLoader(Trailers_Loader_ID);
    }

    @Override
    public void onStart() {
        super.onStart();
        Bundle bundle = new Bundle();
        bundle.putString(getString(R.string.movieID),mResponceId);
        getLoaderManager().initLoader(Trailers_Loader_ID,bundle, DetailFragment.this).forceLoad();
    }

    @SuppressLint("StaticFieldLeak")
    @NonNull
    @Override
    public Loader<ArrayList<TrailerJsonResponce>> onCreateLoader(int i, @Nullable final Bundle bundle) {

        final String movie = bundle.getString(getString(R.string.movieID));
        return new AsyncTaskLoader<ArrayList<TrailerJsonResponce>>(getContext()) {
            @Override
            protected void onStartLoading() {
                super.onStartLoading();
            }

            @Nullable
            @Override

            public ArrayList<TrailerJsonResponce> loadInBackground() {
                String api_key = "api_key";
                String API_KEY = getString(R.string.API_KEY);
                String BASE_URL = getString(R.string.Base_URL);
                String Trailers = "trailers";
                String MovieID = movie;

                final String TrailerJsonString;
                try{
                    Uri uri = Uri.parse(BASE_URL).buildUpon().appendEncodedPath(MovieID)
                            .appendEncodedPath(Trailers).appendQueryParameter(api_key,API_KEY)
                            .build();
                    URL url = new URL(uri.toString());
                    Log.v("the url is:",url.toString());
                    TrailerJsonString = NetworkUtils.getHttpUrl(url);
                    ArrayList<TrailerJsonResponce> res = NetworkUtils.getTrailersDataFromJson(TrailerJsonString);
                    return res;

                }catch (Exception e){
                    e.printStackTrace();
                }
                return null;
            }
        };
    }

    @Override
    public void onLoadFinished(@NonNull Loader<ArrayList<TrailerJsonResponce>> loader, ArrayList<TrailerJsonResponce> trailerJsonResponces) {
        adapter = new TrailerRecyclerView(getContext(),trailerJsonResponces);
        if (trailerJsonResponces == null){
            Log.e("error", "Trailer error");

        }
        else if (trailerJsonResponces.size() == 0){
            TrailersTitle.setVisibility(View.GONE);
        }
        else{
        mRecyclerView.setAdapter(adapter);
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<ArrayList<TrailerJsonResponce>> loader) {

    }
    private void FavouriteState(MovieJsonResponce favourite)
    {

        String value = Id.getValue();
        if(value==null)
        {
           mFavourite.setImageResource(R.drawable.ic_favorite_black_24dp);
            viewModel.insert(favourite);

        }
        else if (value.equals(mResponceId))
        {
            mFavourite.setImageResource(R.drawable.ic_favorite_border_black_24dp);
            viewModel.delete(favourite);
        }
    }



}

