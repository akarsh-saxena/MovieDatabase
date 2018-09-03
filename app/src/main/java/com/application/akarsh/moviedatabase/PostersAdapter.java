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

public class PostersAdapter extends RecyclerView.Adapter<PostersAdapter.Items>{

    Context context;
    List<String> postersAddress = new ArrayList<>();

    public PostersAdapter(Context context, List<String> postersAddress) {
        this.context = context;
        this.postersAddress.addAll(postersAddress);
    }

    @Override
    public Items onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.postersrecycler, parent, false);
        return new Items(view);
    }

    @Override
    public void onBindViewHolder(final Items holder, int position) {

        Picasso.with(context).load("https://image.tmdb.org/t/p/w185"+postersAddress.get(position)).into(holder.postersImage, new Callback() {
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
        return postersAddress.size();
    }


    public class Items extends RecyclerView.ViewHolder implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(context, ImageViewer.class);
            intent.putExtra("Image_Path", postersAddress.get(getAdapterPosition()));
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        }

        ImageView postersImage;
        ProgressBar myProgressBar;

        public Items(View itemView) {
            super(itemView);

            postersImage = (ImageView) itemView.findViewById(R.id.postersImage);
            myProgressBar = (ProgressBar) itemView.findViewById(R.id.myProgressBar);
            postersImage.setOnClickListener(this);
        }
    }
}
