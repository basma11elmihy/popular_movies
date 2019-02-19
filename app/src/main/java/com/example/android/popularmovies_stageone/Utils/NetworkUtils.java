package com.example.android.popularmovies_stageone.Utils;

import android.util.Log;

import com.example.android.popularmovies_stageone.MovieJsonResponce;
import com.example.android.popularmovies_stageone.ReviewJsonResponce;
import com.example.android.popularmovies_stageone.TrailerJsonResponce;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class NetworkUtils {
    private static HttpURLConnection urlConnection = null;
    private static BufferedReader reader = null;
    private static String JsonString;

    public static String getHttpUrl(URL Baseurl) {
        try {
            //create the request and open the connection
            urlConnection = (HttpURLConnection) Baseurl.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            //Read the input stream into a string
            InputStream inputStream = urlConnection.getInputStream();
            StringBuilder buffer = new StringBuilder();
            if (inputStream == null) {
                return null;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while ((line = reader.readLine()) != null) {
                buffer.append(line).append("\n");
            }
            if (buffer.length() == 0) {
                return null;
            }
            JsonString = buffer.toString();
            return JsonString;

        } catch (Exception e) {
            Log.e("error", "error msg", e);
            return null;
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();

                } catch (Exception e) {
                    Log.e("error", "error msg", e);
                }
            }
        }

    }

    public static ArrayList<MovieJsonResponce> getMoviesDataFromJson (String moviesString) throws JSONException{
        //name of values to be fetched
        String Result = "results";
        String VoteAverage = "vote_average";
        String Title = "title";
        String PosterPath = "poster_path";
        String Date = "release_date";
        String Overview = "overview";
        String ID = "id";

        JSONObject jsonObject = new JSONObject(moviesString);
        JSONArray MoviesArray = jsonObject.getJSONArray(Result);
        ArrayList<MovieJsonResponce> res = new ArrayList<>();
        String[] ImageResult = new String[MoviesArray.length()];
        for (int i = 0; i < MoviesArray.length(); i++) {
            JSONObject MovieDetails = MoviesArray.getJSONObject(i);
            String title = MovieDetails.getString(Title);
            String voteAverage = MovieDetails.getString(VoteAverage);
            String poster = MovieDetails.getString(PosterPath);
            String date = MovieDetails.getString(Date);
            String overview = MovieDetails.getString(Overview);
            String id = MovieDetails.getString(ID);
            res.add(new MovieJsonResponce(title, poster, overview, voteAverage, date,id));

            ImageResult[i] = "https://image.tmdb.org/t/p/w500" + poster;
        }
        for (String s : ImageResult) {
            Log.v("images", "imagesPath: " + s);
        }
        return res;
    }

    public static ArrayList<ReviewJsonResponce> getReviewsDataFromJson(String reviewString) throws JSONException{
        String Result = "results";
        String author =  "author";
        String content = "content";

        JSONObject jsonObject = new JSONObject(reviewString);
        JSONArray reviewsArray = jsonObject.getJSONArray(Result);
        ArrayList<ReviewJsonResponce> res = new ArrayList<>();
        for (int i = 0; i <reviewsArray.length() ; i++){
            JSONObject reviewDetails = reviewsArray.getJSONObject(i);
            String authorDetails = reviewDetails.getString(author);
            String contentDetails = reviewDetails.getString(content);
            res.add(new ReviewJsonResponce(authorDetails,contentDetails));
        }
        return res;
    }

    public static ArrayList<TrailerJsonResponce> getTrailersDataFromJson (String trailersString) throws JSONException{
        String name = "name";
        String source = "source";
        String youtube = "youtube";

        JSONObject jsonObject = new JSONObject(trailersString);
        JSONArray trailerArray = jsonObject.getJSONArray(youtube);
        ArrayList<TrailerJsonResponce> res = new ArrayList<>();
        for (int i = 0; i<trailerArray.length();i++){
            JSONObject trailerDetails = trailerArray.getJSONObject(i);
            String nameDetails = trailerDetails.getString(name);
            String sourceDetails = trailerDetails.getString(source);
            res.add(new TrailerJsonResponce(nameDetails,sourceDetails));
        }
        return res;
    }
}
