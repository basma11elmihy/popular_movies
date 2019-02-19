package com.example.android.popularmovies_stageone;
import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
@Entity(tableName = "fav_table")
public class MovieJsonResponce implements Parcelable{

    private String MovieTitle;
    private String MoviePoster;
    private String overView;
    private String voteAverage;
    private String ReleaseDate;
    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "favId")
    private String id;

    public MovieJsonResponce(String MovieTitle, String MoviePoster, String overView, String voteAverage, String ReleaseDate, @NonNull String id) {
       this.MovieTitle = MovieTitle;
        this.MoviePoster = MoviePoster;
        this.overView = overView;
        this.voteAverage = voteAverage;
        this.ReleaseDate = ReleaseDate;
        this.id = id;
    }

    @Ignore
    private MovieJsonResponce(Parcel in){
        MovieTitle = in.readString();
        MoviePoster = in.readString();
        overView = in.readString();
        voteAverage = in.readString();
        ReleaseDate = in.readString();
        id = in.readString();
    }

    public String getMovieTitle() {
        return MovieTitle;
    }

    public void setMovieTitle(String movieTitle) {
        MovieTitle = movieTitle;
    }

    public String getMoviePoster() {
        return MoviePoster;
    }

    public void setMoviePoster(String moviePoster) {
        MoviePoster = moviePoster;
    }

    public String getOverView() {
        return overView;
    }

    public void setOverView(String overView) {
        this.overView = overView;
    }

    public String getVoteAverage() {
        return voteAverage;
    }

    public void setVoteAverage(String voteAverage) {
        this.voteAverage = voteAverage;
    }

    public String getReleaseDate() {
        return ReleaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        ReleaseDate = releaseDate;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Ignore
    @Override
    public int describeContents() {
        return 0;
    }

    @Ignore
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(MovieTitle);
        dest.writeString(MoviePoster);
        dest.writeString(overView);
        dest.writeString(voteAverage);
        dest.writeString(ReleaseDate);
        dest.writeString(id);
    }

    @Ignore
    public static final Parcelable.Creator<MovieJsonResponce> CREATOR = new Parcelable.Creator<MovieJsonResponce>() {

        @Override
        public MovieJsonResponce createFromParcel(Parcel source) {
            return new MovieJsonResponce(source);
        }

        @Override
        public MovieJsonResponce[] newArray(int size) {
            return new MovieJsonResponce[size];
        }
    };
}
