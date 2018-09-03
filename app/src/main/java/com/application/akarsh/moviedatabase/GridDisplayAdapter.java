package com.application.akarsh.moviedatabase;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.application.akarsh.moviedatabase.models.MovieModel;
import com.application.akarsh.moviedatabase.models.MoviesModel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Akarsh on 06-08-2017.
 */

public class GridDisplayAdapter extends RecyclerView.Adapter<GridDisplayAdapter.Items> {

    Context context;
    List<MoviesModel.ResultsBean> movieModels = new ArrayList<>();

    public GridDisplayAdapter(Context context, List<MoviesModel.ResultsBean> movieModels) {
        this.movieModels = movieModels;
        this.context = context;
    }

    @Override
    public Items onCreateViewHolder(ViewGroup parent, int viewType) {
        //Toast.makeText(context, "GridDisplayAdapter called...", Toast.LENGTH_SHORT).show();
        View view = LayoutInflater.from(context).inflate(R.layout.item_movie, parent, false);
        return new Items(view);
    }

    @Override
    public void onBindViewHolder(Items holder, int position) {
        //String path = movieModels.get(position).getPosterPath();
        String path = movieModels.get(position).getPoster_path();
        /*DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        float dpWidth = displayMetrics.widthPixels / displayMetrics.density;
        Resources r = context.getResources();
        float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpWidth, displayMetrics);
        holder.moviePoster.requestLayout();
        holder.moviePoster.getLayoutParams().width = (int)dpWidth;
        holder.moviePoster.getLayoutParams().height = (int)(dpWidth*1.5);
        holder.moviePoster.setColorFilter(Color.WHITE);

        Log.d("myheight", "HEIGHT: "+(dpWidth*1.5));
        Log.d("myheight", "WIDTH: "+dpWidth);
        Log.d("myheight", "Path: "+"https://image.tmdb.org/t/p/original/" + path);
        Toast.makeText(context, "https://image.tmdb.org/t/p/original/" + path, Toast.LENGTH_SHORT).show();*/
        if (!path.equals("null")) {
            Picasso.with(context).load("https://image.tmdb.org/t/p/w500/" + path).into(holder.moviePoster);
        } else {
            Picasso.with(context).load(R.mipmap.noimage).into(holder.moviePoster);
        }
        //holder.alphaView.getLayoutParams().height = holder.moviePoster.getLayoutParams().height;
        //holder.alphaView.getLayoutParams().width = holder.moviePoster.getLayoutParams().width;
    }

    @Override
    public int getItemCount() {
        return movieModels.size();
    }

    public class Items extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnTouchListener{

        ImageView moviePoster;
        //View alphaView;

        public Items(View itemView) {
            super(itemView);

            moviePoster = (ImageView) itemView.findViewById(R.id.moviePoster);
            //alphaView = itemView.findViewById(R.id.alphaView);
            //alphaView.setOnClickListener(this);
            moviePoster.setOnClickListener(this);
            moviePoster.setOnTouchListener(this);
        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(context, MovieDetail.class);
            intent.putExtra("ID", movieModels.get(getAdapterPosition()).getId());
            intent.putExtra("Name", movieModels.get(getAdapterPosition()).getTitle());
            intent.putExtra("Vote_avg", movieModels.get(getAdapterPosition()).getVote_average());
            intent.putExtra("Vote_count", movieModels.get(getAdapterPosition()).getVote_count());
            intent.putExtra("Image", movieModels.get(getAdapterPosition()).getPoster_path());
            intent.putExtra("Release", movieModels.get(getAdapterPosition()).getRelease_date());
            intent.putExtra("Image", movieModels.get(getAdapterPosition()).getPoster_path());
            context.startActivity(intent);
        }

        @Override
        public boolean onTouch(View v, MotionEvent event) {

            if(event.getAction() == MotionEvent.ACTION_DOWN) {
                moviePoster.setAlpha((float)0.6);
            }

            if(event.getAction() == MotionEvent.ACTION_UP) {
                moviePoster.setAlpha((float) 0.9);
                onClick(v);
                return true;
            }

            if(event.getAction() == MotionEvent.ACTION_CANCEL)
                    moviePoster.setAlpha((float) 0.9);

            return true;
        }
    }
}
