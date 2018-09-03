package com.application.akarsh.moviedatabase;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

public class TvSeries extends AppCompatActivity {

    TextView tvDiscover, tvSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tv_series);

        tvDiscover = (TextView) findViewById(R.id.tvDiscover);
        tvSearch = (TextView) findViewById(R.id.tvSearch);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        tvDiscover.setOnClickListener(
                new TextView.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(TvSeries.this, DisplayPage.class);
                        intent.putExtra("Intent_Tag", "discover_tv_series");
                        startActivity(intent);
                    }
                }
        );

        tvSearch.setOnClickListener(
                new TextView.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(TvSeries.this, SearchTvSeries.class);
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
