package com.application.akarsh.moviedatabase;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class Movies extends AppCompatActivity {

    TextView moviesHeading, popularTextView, highestRatedTextView, newTextView, searchTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movies);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        moviesHeading = (TextView) findViewById(R.id.moviesHeading);
        popularTextView = (TextView) findViewById(R.id.popularTextView);
        highestRatedTextView = (TextView) findViewById(R.id.highestRatedTextView);
        newTextView = (TextView) findViewById(R.id.newTextView);
        searchTextView = (TextView) findViewById(R.id.searchTextView);

        Typeface custom_font = Typeface.createFromAsset(getAssets(), "fonts/TheyPerished.ttf");
        Typeface custom_font1 = Typeface.createFromAsset(getAssets(), "fonts/DancingScript-Bold.ttf");

        moviesHeading.setTypeface(custom_font);
        popularTextView.setTypeface(custom_font1);
        highestRatedTextView.setTypeface(custom_font1);
        newTextView.setTypeface(custom_font1);
        searchTextView.setTypeface(custom_font1);

        popularTextView.setOnClickListener(
                new TextView.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(Movies.this, "Popular", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(Movies.this, DisplayPage.class);
                        intent.putExtra("Intent_Tag", "get_popular_movies");
                        startActivity(intent);
                    }
                }
        );


        newTextView.setOnClickListener(
                new TextView.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(Movies.this, DisplayPage.class);
                        intent.putExtra("Intent_Tag", "get_new_movies");
                        startActivity(intent);
                    }
                }
        );

        highestRatedTextView.setOnClickListener(
                new TextView.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(Movies.this, DisplayPage.class);
                        intent.putExtra("Intent_Tag", "get_highest_rated_movies");
                        startActivity(intent);
                    }
                }
        );

        searchTextView.setOnClickListener(
                new TextView.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(Movies.this, SearchMovie.class);
                        startActivity(intent);
                    }
                }
        );
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem){
        if(menuItem.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        else
            return false;
    }
}
