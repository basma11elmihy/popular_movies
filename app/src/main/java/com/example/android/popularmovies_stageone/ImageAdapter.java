package com.example.android.popularmovies_stageone;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ImageViewHolder> {

    private final Context context;
    public ImageView mImageView;
    ArrayList<MovieJsonResponce> urlArray;
    ListItemClickListener listItemClickListener;

    public ImageAdapter(Context context, ArrayList<MovieJsonResponce> urlArray, ListItemClickListener clickListener) {
        this.context = context;
        this.urlArray = urlArray;
        this.listItemClickListener = clickListener;
    }
    public interface ListItemClickListener{
        void onListItemClick(int ClickedItemIndex);
    }

    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        Context context = viewGroup.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.list_item_grid,viewGroup,false);
        ImageViewHolder holder = new ImageViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder imageViewHolder, int i) {
        //picasso
        Picasso.with(context).load(context.getString(R.string.image_url)+urlArray.get(i).getMoviePoster()).into(mImageView);
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        if (urlArray == null)
        return 0;
        else
            return urlArray.size();
    }
    public MovieJsonResponce getItem(int index){
        return urlArray.get(index);
    }

    public class ImageViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{


        public ImageViewHolder(@NonNull View itemView) {
            super(itemView);
            mImageView = itemView.findViewById(R.id.Image_Grid);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int ClickedPosition = getAdapterPosition();
            listItemClickListener.onListItemClick(ClickedPosition);
        }
    }
}
