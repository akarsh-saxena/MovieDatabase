package com.application.akarsh.moviedatabase;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.application.akarsh.moviedatabase.models.CastModel;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Akarsh on 22-07-2017.
 */

public class CastAdapter extends RecyclerView.Adapter<CastAdapter.Items>{

    Context context;
    private List<CastModel> castModels = new ArrayList<>();
    Activity activity;


    public CastAdapter(Context context, Activity activity, List<CastModel> castModels){

        this.context = context;
        this.castModels = castModels;
        this.activity = activity;
    }

    @Override
    public Items onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.cast_recycler_item, parent, false);
        //Toast.makeText(context, "In createView", Toast.LENGTH_SHORT).show();
        return new Items(view);
    }

    @Override
    public void onBindViewHolder(final Items holder, int position) {
        holder.castCharacter.setText(castModels.get(position).getChracter());
        holder.castName.setText(castModels.get(position).getName());
        if(!castModels.get(position).getProfile_path().equals("null"))
            Picasso.with(context).load("https://image.tmdb.org/t/p/w500" + castModels.get(position).getProfile_path()).into(holder.castImage, new Callback() {
                @Override
                public void onSuccess() {
                    holder.castName.setVisibility(View.VISIBLE);
                    holder.castCharacter.setVisibility(View.VISIBLE);
                }

                @Override
                public void onError() {
                    holder.castName.setVisibility(View.VISIBLE);
                    holder.castCharacter.setVisibility(View.VISIBLE);
                }
            });
        else
             Picasso.with(context).load(R.mipmap.noimage).into(holder.castImage, new Callback() {
                 @Override
                 public void onSuccess() {
                     holder.castName.setVisibility(View.VISIBLE);
                     holder.castCharacter.setVisibility(View.VISIBLE);
                 }

                 @Override
                 public void onError() {
                     holder.castName.setVisibility(View.VISIBLE);
                     holder.castCharacter.setVisibility(View.VISIBLE);
                 }
             });
//        Log.d("ImagePath", "https://image.tmdb.org/t/p/w500/" + castModels.get(position).getProfile_path());

       // holder.view.getLayoutParams().height = holder.castImage.getLayoutParams().height;
       // holder.view.getLayoutParams().width = holder.castImage.getLayoutParams().width;
    }

    @Override
    public int getItemCount() {
        return castModels.size();
    }


    public class Items extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnTouchListener{

        TextView castName, castCharacter;
        ImageView castImage;
        //View view;

        public Items(View itemView) {
            super(itemView);

            castName = (TextView) itemView.findViewById(R.id.castName);
            castCharacter = (TextView) itemView.findViewById(R.id.castCharacter);
            castImage = (ImageView) itemView.findViewById(R.id.castImage);
            castImage.setOnTouchListener(this);
            castImage.setOnClickListener(this);
            //view = itemView.findViewById(R.id.view);
        }

        @Override
        public boolean onTouch(View v, MotionEvent event) {

            if(event.getAction() == MotionEvent.ACTION_DOWN) {
                castImage.setAlpha((float)0.6);
            }

            if(event.getAction() == MotionEvent.ACTION_UP) {
                castImage.setAlpha((float) 0.9);
                onClick(v);
                return true;
            }

            if(event.getAction() == MotionEvent.ACTION_CANCEL)
                castImage.setAlpha((float) 0.9);

            return true;
        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(context, ActorDetails.class);
            intent.putExtra("actor_id", String.valueOf(castModels.get(getAdapterPosition()).getId()));
            ActivityOptionsCompat options = ActivityOptionsCompat.
                    makeSceneTransitionAnimation(activity, castImage, "castPhoto");
            context.startActivity(intent, options.toBundle());
        }
    }
}
