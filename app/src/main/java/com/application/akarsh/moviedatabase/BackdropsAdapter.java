package com.application.akarsh.moviedatabase;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Akarsh on 02-06-2017.
 */

public class BackdropsAdapter extends RecyclerView.Adapter<BackdropsAdapter.Items>{

    Context context;
    List<String> backdropsAddress = new ArrayList<>();

    public BackdropsAdapter(Context context, List<String> backdropsAddress) {
        this.context = context;
        this.backdropsAddress.addAll(backdropsAddress);
    }

    @Override
    public Items onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.backdropsrecycler, parent, false);
        return new Items(context, view);
    }

    @Override
    public void onBindViewHolder(final Items holder, int position) {

        Picasso.with(context).load("https://image.tmdb.org/t/p/w500"+backdropsAddress.get(position)).into(holder.backdropsImage, new Callback() {
            @Override
            public void onSuccess() {
                holder.myProgressBar.setVisibility(View.GONE);
            }

            @Override
            public void onError() {

            }
        });
    }

    @Override
    public int getItemCount() {
        return backdropsAddress.size();
    }


    public class Items extends RecyclerView.ViewHolder implements View.OnClickListener{

        Context context;
        ImageView backdropsImage;
        ProgressBar myProgressBar;

        public Items(Context context, View itemView) {
            super(itemView);
            this.context = context;

            backdropsImage = (ImageView) itemView.findViewById(R.id.backdropsImage);
            myProgressBar = (ProgressBar) itemView.findViewById(R.id.myProgressBar);
            backdropsImage.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(context, ImageViewer.class);
            intent.putExtra("Image_Path", backdropsAddress.get(getAdapterPosition()));
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        }
    }
}
