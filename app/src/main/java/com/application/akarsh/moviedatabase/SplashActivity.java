package com.application.akarsh.moviedatabase;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class SplashActivity extends AppCompatActivity {

    TextView textView;
    private static int SPLASH_TIME_OUT = 2500;
    Handler handler;
    Runnable runnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        textView = (TextView) findViewById(R.id.textView);

        Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/GameOfThrones.ttf");
        textView.setTypeface(typeface);

        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                Intent i = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(i);
                finish();
            }
        };
        handler.postDelayed(runnable, SPLASH_TIME_OUT);
    }

    @Override
    public void onBackPressed(){
        finish();
        handler.removeCallbacks(runnable);
    }
}
