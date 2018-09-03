package com.application.akarsh.moviedatabase;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.application.akarsh.moviedatabase.models.SeasonModel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Akarsh on 13-06-2017.
 */

public class SeasonsAdapter extends RecyclerView.Adapter<SeasonsAdapter.Seasons>{

    Context context;
    List<SeasonModel> myList = new ArrayList<>();
    String myId;

    public SeasonsAdapter(Context context, List<SeasonModel> myList, String myId){
        this.context = context;
        this.myList.addAll(myList);
        this.myId = myId;
    }

    @Override
    public Seasons onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.seasonsrecycler, parent, false);
        return new Seasons(view);
    }

    @Override
    public void onBindViewHolder(Seasons holder, int position) {

        holder.seasonImage.setVisibility(View.VISIBLE);

        if(!myList.get(position).getPoster_path().equals("null"))
            Picasso.with(context).load("https://image.tmdb.org/t/p/w300"+myList.get(position).getPoster_path()).into(holder.seasonImage);
        else
            Picasso.with(context).load(R.mipmap.noimage).resize(300, 450).into(holder.seasonImage);
        holder.seasonNumber.setText("Season: "+myList.get(position).getSeason_number());
        holder.totalEpisodes.setText("Total Episodes: "+myList.get(position).getEpisode_count());
        holder.airDate.setText("Air Date: "+myList.get(position).getAir_date());
    }

    @Override
    public int getItemCount() {
        return myList.size();
    }

    public class Seasons extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView seasonImage;
        TextView seasonNumber, totalEpisodes, airDate, details;
        ProgressBar seasonImagePB;

        public Seasons(View itemView) {
            super(itemView);

            seasonImage = (ImageView) itemView.findViewById(R.id.seasonImage);
            seasonNumber = (TextView) itemView.findViewById(R.id.seasonNumber);
            totalEpisodes = (TextView) itemView.findViewById(R.id.totalEpisodes);
            airDate = (TextView) itemView.findViewById(R.id.airDate);
            details = (TextView) itemView.findViewById(R.id.details);
            seasonImagePB = (ProgressBar) itemView.findViewById(R.id.seasonImagePB);
            seasonNumber.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if(v == seasonNumber) {
                Intent intent = new Intent(context, TvSeasonDetails_old.class);
                intent.putExtra("ID", myId);
                intent.putExtra("season_number", myList.get(getAdapterPosition()).getSeason_number());
                intent.putExtra("episode_count", myList.get(getAdapterPosition()).getEpisode_count());
                context.startActivity(intent);
            }
        }
    }
}
