package com.application.akarsh.moviedatabase;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import android.graphics.Typeface;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.application.akarsh.moviedatabase.models.MovieModel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

class MyAdapter extends RecyclerView.Adapter<MyAdapter.Items>{

    private Context context;
    private List<MovieModel> movieModels = new ArrayList<>();
    private boolean showDate, isTvSeries, isFavorite;

    MyAdapter(Context context, List<MovieModel> movieModels, boolean showDate, boolean isTvSeries, boolean isFavorite){
        this.context = context;
        this.movieModels = movieModels;
        this.showDate = showDate;
        this.isTvSeries = isTvSeries;
        this.isFavorite = isFavorite;
    }

    @Override
    public Items onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_items, parent, false);
        return new Items(view);
    }

    @Override
    public void onBindViewHolder(Items holder, int position) {

        if(!movieModels.get(position).getRelease_date().equals(""))
            holder.movieName.setText(movieModels.get(position).getTitle()+" ("+getMovieYear(position)+")");
        else
            holder.movieName.setText(movieModels.get(position).getTitle());

        if(showDate)
            holder.releaseDate.setVisibility(TextView.VISIBLE);
        else
            holder.releaseDate.setVisibility(TextView.GONE);

        holder.custom_font = Typeface.createFromAsset(context.getAssets(), "fonts/DancingScript-Bold.ttf");
        holder.movieName.setTypeface(holder.custom_font);

        holder.ratingBar.setRating(movieModels.get(position).getVote_average()/2);


        if(TextUtils.isEmpty(movieModels.get(position).getRelease_date())){
            holder.releaseDate.setVisibility(TextView.GONE);
        }
        else {
            String date[] = movieModels.get(position).getRelease_date().split("-");
            String movieDate = "Release Date: "+date[2]+"-"+date[1]+"-"+date[0];
            holder.releaseDate.setText(movieDate);
        }

        holder.voteCount.setText(movieModels.get(position).getVote_count()+"");

        if(!isFavorite) {
            String path = movieModels.get(position).getPosterPath();
            if (!path.equals("null")) {
                Picasso.with(context).load("https://image.tmdb.org/t/p/w300/" + path).into(holder.posterImage);
            } else {
                Picasso.with(context).load(R.mipmap.noimage).resize(300, 450).into(holder.posterImage);
            }
        }else
            Picasso.with(context).load(R.mipmap.noimage).resize(300, 450).into(holder.posterImage);
    }

    @Override
    public int getItemCount() {
        return movieModels.size();
    }

    private String getMovieYear(int position){
        String movieYear = movieModels.get(position).getRelease_date();
        String[] dates = movieYear.split("-");
        return dates[0];
    }


    class Items extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView movieName, voteCount, tvDetails, releaseDate;
        ProgressBar progressBarSmall;
        ImageView posterImage;
        RatingBar ratingBar;
        Typeface custom_font;

        Items(View itemView) {
            super(itemView);

            movieName = (TextView) itemView.findViewById(R.id.castName);
            ratingBar = (RatingBar) itemView.findViewById(R.id.ratingBar);
            posterImage = (ImageView) itemView.findViewById(R.id.posterImage);
            progressBarSmall = (ProgressBar) itemView.findViewById(R.id.progressBarSmall);
            voteCount = (TextView) itemView.findViewById(R.id.voteCount);
            tvDetails = (TextView) itemView.findViewById(R.id.tvDetails);
            releaseDate = (TextView) itemView.findViewById(R.id.releaseDate);
            releaseDate.setVisibility(TextView.GONE);
            posterImage.setOnClickListener(this);
            movieName.setOnClickListener(this);
            tvDetails.setOnClickListener(this);
            movieName.setTypeface(custom_font);
        }

        Intent intent;

        @Override
        public void onClick(View v) {
            if(v == tvDetails || v == movieName) {
                if(!isTvSeries) {
                    Toast.makeText(context, "This is a movie!!", Toast.LENGTH_SHORT).show();
                    //intent = new Intent(context, ScrollingActivity.class);
                    intent = new Intent(context, MovieDetail.class);

                }
                else {
                    Toast.makeText(context, "This is a TV Series!!", Toast.LENGTH_SHORT).show();
                    intent = new Intent(context, TvSeriesDetails.class);
                }

                intent.putExtra("ID", movieModels.get(getAdapterPosition()).getId());
                intent.putExtra("Name", movieModels.get(getAdapterPosition()).getTitle());
                intent.putExtra("Vote_avg", movieModels.get(getAdapterPosition()).getVote_average());
                intent.putExtra("Vote_count", movieModels.get(getAdapterPosition()).getVote_count());
                intent.putExtra("Image", movieModels.get(getAdapterPosition()).getPosterPath());
                intent.putExtra("Release", movieModels.get(getAdapterPosition()).getRelease_date());
                intent.putExtra("Image", movieModels.get(getAdapterPosition()).getPosterPath());

                context.startActivity(intent);
            }
            else
                if(v == posterImage){
                    ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation((Activity) context, posterImage, "shared_element_transition");
                    Intent intent1 = new Intent(context, ImageViewer.class);
                    intent1.putExtra("Image_Path", movieModels.get(getAdapterPosition()).getPosterPath());
                    context.startActivity(intent1, optionsCompat.toBundle());
                }
        }
    }
}
