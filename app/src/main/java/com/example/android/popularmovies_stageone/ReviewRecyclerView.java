package com.example.android.popularmovies_stageone;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class ReviewRecyclerView extends RecyclerView.Adapter<ReviewRecyclerView.ReviewViewHolder> {
    Context context;
    ArrayList<ReviewJsonResponce> reviewJsonResponce;

    public ReviewRecyclerView(Context context,ArrayList<ReviewJsonResponce> reviewJsonResponces) {
        this.context = context;
        this.reviewJsonResponce = reviewJsonResponces;
    }

    @NonNull
    @Override
    public ReviewViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.list_item_rev,viewGroup,false);

        return new ReviewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewViewHolder reviewViewHolder, int i) {
        ReviewJsonResponce review = reviewJsonResponce.get(i);
        reviewViewHolder.author.setText(review.getAuthor());
        reviewViewHolder.comment.setText(review.getContent());

    }

    @Override
    public int getItemCount() {
        return reviewJsonResponce.size();
    }


    class ReviewViewHolder extends RecyclerView.ViewHolder{
        TextView author;
        TextView comment;
        public ReviewViewHolder(@NonNull View itemView) {
            super(itemView);

            author = itemView.findViewById(R.id.author_tv);
            comment = itemView.findViewById(R.id.reviewTv);
        }
    }
}
