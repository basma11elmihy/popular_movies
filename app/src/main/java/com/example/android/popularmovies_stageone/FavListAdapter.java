package com.example.android.popularmovies_stageone;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class FavListAdapter extends RecyclerView.Adapter<FavListAdapter.FavViewHolder> {

    private List<MovieJsonResponce> mfavourites;
    Context context;

    public FavListAdapter(Context context, List<MovieJsonResponce> mfavourites) {
        this.context = context;
        this.mfavourites = mfavourites;
    }

    @NonNull
    @Override
    public FavViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.list_item_fav,viewGroup,false);
        return new FavViewHolder(view,mfavourites);
    }

    @Override
    public void onBindViewHolder(@NonNull FavViewHolder favViewHolder, int i) {
        if (mfavourites != null){
            MovieJsonResponce current = mfavourites.get(i);
        //help with getting image from file from https://stackoverflow.com/questions/4181774/show-image-view-from-file-path
            Bitmap bitmap = BitmapFactory.decodeFile(current.getMoviePoster());
            favViewHolder.movieFavImage.setImageBitmap(bitmap);
            favViewHolder.name.setText(current.getMovieTitle());



        }

    }

    @Override
    public int getItemCount() {
        if (mfavourites != null){
            return mfavourites.size();
        }
        else{
            return 0;
        }
    }

    class FavViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private final ImageView movieFavImage;
        private final TextView name;
        private List<MovieJsonResponce> mfavourites;

        public FavViewHolder(@NonNull View itemView, List<MovieJsonResponce> mfavourites) {
            super(itemView);
            movieFavImage = itemView.findViewById(R.id.fav_Image_Grid);
            name = itemView.findViewById(R.id.movieNameFav);
            itemView.setOnClickListener(this);
            this.mfavourites = mfavourites;

        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            MovieJsonResponce current = this.mfavourites.get(position);
            Intent intent = new Intent(context,DetailActivity.class);
            intent.putExtra(context.getString(R.string.parcel_data),current);
            context.startActivity(intent);

        }
    }

}
