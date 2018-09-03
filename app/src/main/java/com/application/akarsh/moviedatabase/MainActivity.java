package com.application.akarsh.moviedatabase;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.multidex.MultiDex;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {


    TextView movieTextView, tvSeriesTextView;
    ImageView movieBackground, tvSeriesBackground;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        movieTextView = (TextView) findViewById(R.id.movieTextView);
        tvSeriesTextView = (TextView) findViewById(R.id.tvSeriesTextView);
        movieBackground = (ImageView) findViewById(R.id.movieBackground);
        tvSeriesBackground = (ImageView) findViewById(R.id.tvSeriesBackground);

        Typeface custom_font = Typeface.createFromAsset(getAssets(), "fonts/DancingScript-Bold.ttf");
        movieTextView.setTypeface(custom_font);
        tvSeriesTextView.setTypeface(custom_font);

        movieBackground.setOnClickListener(
                new ImageView.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        startNewActivity(v);
                    }
                }
        );

        tvSeriesBackground.setOnClickListener(
                new ImageView.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        startNewActivity(v);
                    }
                }
        );

        movieBackground.setOnClickListener(
                new ImageView.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        startNewActivity(v);
                    }
                }
        );

        movieBackground.setOnClickListener(
                new ImageView.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        startNewActivity(v);
                    }
                }
        );

    }

    long time1;
    boolean singleBack = false, firstTime = true;

    @Override
    public void onBackPressed() {

        if (!singleBack) {
            if(!firstTime) {
                Toast.makeText(this, "Press again to exit", Toast.LENGTH_SHORT).show();
                time1 = System.currentTimeMillis();
            }
            time1 = System.currentTimeMillis();
            Toast.makeText(this, "Press again to exit", Toast.LENGTH_SHORT).show();
            singleBack = true;
        } else {
            if (System.currentTimeMillis() - time1 <= 2000) {
                singleBack = false;
                super.onBackPressed();
            }
            else {
                singleBack = true;
                Toast.makeText(this, "Press again to exit", Toast.LENGTH_SHORT).show();
                time1 = System.currentTimeMillis();
            }
            }
    }
    void startNewActivity(View view){

        Intent intent;

        if(view == movieBackground || view == movieTextView)
            intent = new Intent(this, Movies.class);
        else
            intent = new Intent(this, TvSeries.class);

        startActivity(intent);
    }

}
