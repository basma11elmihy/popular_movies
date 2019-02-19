package com.example.android.popularmovies_stageone;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

public class TrailerRecyclerView extends RecyclerView.Adapter<TrailerRecyclerView.TrailerViewHolder>{
    private Context context;
    private ArrayList<TrailerJsonResponce> responce;

    public TrailerRecyclerView(Context context, ArrayList<TrailerJsonResponce> responce) {
        this.context = context;
        this.responce = responce;
    }

    @NonNull
    @Override
    public TrailerViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.list_item_trailer,viewGroup,false);
        return new TrailerViewHolder(view,responce);
    }

    @Override
    public void onBindViewHolder(@NonNull TrailerViewHolder trailerViewHolder, int i) {
        trailerViewHolder.trailerTv.setText(context.getString(R.string.Trailer)+ " " + ++i); }

    @Override
    public int getItemCount() {
        if (responce == null){
            return 0;
        }
        else {
            return responce.size();
        }
    }

    public class TrailerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView trailerTv;
        private ArrayList<TrailerJsonResponce> responce;

        public TrailerViewHolder(@NonNull View itemView,ArrayList<TrailerJsonResponce> responce) {
            super(itemView);
            trailerTv = itemView.findViewById(R.id.LinkTv);
            itemView.setOnClickListener(this);
            this.responce = responce;

        }
        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            TrailerJsonResponce trailer = this.responce.get(position);
            String url = context.getString(R.string.youtube_base);
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(url+trailer.getSource()));
            view.getContext().startActivity(intent);
        }
    }
}
