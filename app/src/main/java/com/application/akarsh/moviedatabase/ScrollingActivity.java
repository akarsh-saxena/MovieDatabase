package com.application.akarsh.moviedatabase;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatCallback;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.application.akarsh.moviedatabase.models.CastModel;
import com.application.akarsh.moviedatabase.models.CrewModel;
import com.application.akarsh.moviedatabase.models.EpisodeModel;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class ScrollingActivity extends AppCompatActivity implements AppCompatCallback {

    List<String> myDetails = new ArrayList<>();
    List<String> castList = new ArrayList<>();
    LinearLayout castListView;
    TextView tvReleaseDate, tvMovieBudget, tvRuntime, tvVoteCount, tvTagline, tvAdult, tvStory;
    TextView  castHeading, tvBackdrops, tvPosters, tvMovieRevenue, tvDirector, tvProducer;
    RatingBar ratingBar;
    ImageView backdropImage;
    ProgressBar detailsProgress;
    RecyclerView backdropsRecyclerView, postersRecyclerView;
    GridLayoutManager gridLayoutManagerPosters, gridLayoutManagerImages;
    ArrayAdapter<String> castListAdapter;
    Typeface custom_font, custom_font2;
    String Youtube_URL = "";
    TextView castTextViews[];
    View.OnClickListener myOnClickListener;
    CollapsingToolbarLayout collapsingToolbarLayout;
    TabLayout tabLayout;
    ViewPager mViewPager;
    private SectionsPagerAdapter mSectionsPagerAdapter;
    private static final String ARG_SECTION_NUMBER = "section_number";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrolling);

        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);

        mSectionsPagerAdapter = new ScrollingActivity.SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.viewPager);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.setOffscreenPageLimit(3);

        setContentView(R.layout.activity_scrolling);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        tabLayout.setupWithViewPager(mViewPager);

        castListView = (LinearLayout) findViewById(R.id.castListView);
        castListView.setClickable(false);
        castListAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, castList);
        tvReleaseDate = (TextView) findViewById(R.id.tvRelease_date);
        tvMovieBudget = (TextView) findViewById(R.id.tvMovieBudget);
        tvMovieRevenue = (TextView) findViewById(R.id.tvMovieRevenue);
        tvRuntime = (TextView) findViewById(R.id.tvRuntime);
        tvVoteCount = (TextView) findViewById(R.id.tvVoteCount);
        tvTagline = (TextView) findViewById(R.id.tvTagLine);
        tvTagline.setVisibility(GONE);
        tvAdult = (TextView) findViewById(R.id.tvAdult);
        tvStory = (TextView) findViewById(R.id.tvStory);
        ratingBar = (RatingBar) findViewById(R.id.ratingBar);
        backdropImage = (ImageView) findViewById(R.id.backdropImage);
        detailsProgress = (ProgressBar) findViewById(R.id.detailsProgress);
        castHeading = (TextView) findViewById(R.id.castHeading);
        castHeading.setVisibility(GONE);
        tvBackdrops = (TextView) findViewById(R.id.tvBackdrops);
        tvBackdrops.setVisibility(GONE);
        tvPosters = (TextView) findViewById(R.id.tvPosters);
        tvPosters.setVisibility(GONE);
        tvDirector = (TextView) findViewById(R.id.tvDirector);
        tvDirector.setVisibility(GONE);
        tvProducer = (TextView) findViewById(R.id.tvProducer);
        tvProducer.setVisibility(GONE);
        backdropsRecyclerView = (RecyclerView) findViewById(R.id.backdropsRecyclerView);
        postersRecyclerView = (RecyclerView) findViewById(R.id.postersRecyclerView);
        gridLayoutManagerImages = new GridLayoutManager(this, 2, GridLayoutManager.HORIZONTAL, false);
        gridLayoutManagerPosters = new GridLayoutManager(this, 2, GridLayoutManager.HORIZONTAL, false);
        backdropsRecyclerView.setLayoutManager(gridLayoutManagerImages);
        postersRecyclerView.setLayoutManager(gridLayoutManagerPosters);

        custom_font = Typeface.createFromAsset(getAssets(), "fonts/Pacifico.ttf");

        Typeface custom_font1 = Typeface.createFromAsset(getAssets(), "fonts/blackjack.otf");
        collapsingToolbarLayout.setCollapsedTitleTypeface(custom_font1);

        custom_font2 = Typeface.createFromAsset(getAssets(), "fonts/Quicksand-Bold.otf");
     /*   tvAdult.setTypeface(custom_font2);
        tvMovieBudget.setTypeface(custom_font2);
        tvMovieRevenue.setTypeface(custom_font2);
        tvVoteCount.setTypeface(custom_font2);
        tvReleaseDate.setTypeface(custom_font2);
        tvRuntime.setTypeface(custom_font2);
        tvCast.setTypeface(custom_font2);
        tvDirector.setTypeface(custom_font2);
        tvProducer.setTypeface(custom_font2); */
        tvBackdrops.setTypeface(custom_font);
        tvPosters.setTypeface(custom_font);

        final String myId = getIntent().getExtras().get("ID").toString();
        final String myName = getIntent().getExtras().getString("Name");
        final float vote_avg = Float.parseFloat(getIntent().getExtras().get("Vote_avg").toString());
        final String vote_count = getIntent().getExtras().get("Vote_count").toString();
        final String release = getIntent().getExtras().get("Release").toString();
        final String poster_path = getIntent().getExtras().get("Image").toString();
        collapsingToolbarLayout.setTitle(myName);

        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);

        DatabaseHelper databaseHelper = new DatabaseHelper(ScrollingActivity.this);

        if(databaseHelper.isFavorite(Integer.parseInt(myId), true))
            fab.setImageResource(R.drawable.favorite_added);
        else
            fab.setImageResource(R.drawable.favorite_removed);

        //ScrollingActivity.MovieDetailsFetcher movieDetailsFetcher = new ScrollingActivity.MovieDetailsFetcher(this);
      //  ScrollingActivity.CastFetcher castFetcher = new ScrollingActivity.CastFetcher();
        ScrollingActivity.ImageFetcher imageFetcher = new ScrollingActivity.ImageFetcher();

        //movieDetailsFetcher.executeOnExecutor(AsyncTask.SERIAL_EXECUTOR, myId);
     //   castFetcher.executeOnExecutor(AsyncTask.SERIAL_EXECUTOR, myId);
        imageFetcher.executeOnExecutor(AsyncTask.SERIAL_EXECUTOR, myId);

        myOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ScrollingActivity.this, ActorDetails.class);
                intent.putExtra("actor_id", String.valueOf(v.getTag()));
                startActivity(intent);
            }
        };

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseHelper databaseHelper = new DatabaseHelper(ScrollingActivity.this);
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                try {
                    if (!databaseHelper.isFavorite(Integer.parseInt(myId), true)) {
                        if (databaseHelper.insertFavorite(Integer.parseInt(myId), myName, vote_avg, vote_count, release, true, poster_path, ScrollingActivity.this)) {
                            fab.setImageResource(R.drawable.favorite_added);
                            Toast.makeText(ScrollingActivity.this, "Added to favorites", Toast.LENGTH_SHORT).show();
                        } else
                            Toast.makeText(ScrollingActivity.this, "Error adding in favorites", Toast.LENGTH_SHORT).show();
                    } else {
                        if (databaseHelper.removeFavorite(Integer.parseInt(myId), true)) {
                            fab.setImageResource(R.drawable.favorite_removed);
                            Toast.makeText(ScrollingActivity.this, "Removed from favorites", Toast.LENGTH_SHORT).show();
                        } else
                            Toast.makeText(ScrollingActivity.this, "Error removing from favorites", Toast.LENGTH_SHORT).show();
                    }
                } catch(IOException io){
                    io.printStackTrace();
                }

            }
        });

        tvAdult.setOnClickListener(
                new TextView.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(ScrollingActivity.this, Favorites.class);
                        startActivity(intent);
                    }
                }
        );

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,
                "http://api.themoviedb.org/3/movie/" + myId + "?api_key=dda1dd643a75fd4f2a7239a0daac9c47&append_to_response=videos",
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject parentObject) {

                        try {
                            tvStory.setText(parentObject.getString("overview"));

                            if(!parentObject.getString("release_date").equals("")) {
                                String date[] = parentObject.getString("release_date").split("-");
                                String movieDate = "Release Date: "+date[2]+"-"+date[1]+"-"+date[0];
                                tvReleaseDate.setText(movieDate);
                            }
                            else
                                tvReleaseDate.setVisibility(GONE);

                            if(parentObject.getInt("runtime") != 0) {
                                String finalTime;
                                int minutes = Integer.parseInt(parentObject.getString("runtime"));
                                int hours = minutes/60;
                                if(minutes%60 != 0)
                                    finalTime = "Runtime: "+minutes+" minutes ("+hours+"h "+minutes%60+"m)";
                                else
                                    finalTime = "Runtime: "+minutes+" minutes ("+hours+" hours)";
                                tvRuntime.setText(finalTime);
                            }
                            else
                                tvRuntime.setVisibility(GONE);

                            if(parentObject.getLong("budget") != 0)
                                tvMovieBudget.setText("Budget: "+ NumberFormat.getCurrencyInstance(Locale.US).format(parentObject.getLong("budget")));
                            else
                                tvMovieBudget.setVisibility(GONE);

                            tvAdult.setText("Adult: "+parentObject.getString("adult"));

                            JSONObject videosObject = parentObject.getJSONObject("videos");
                            JSONArray videosArray = videosObject.getJSONArray("results");

                            String video_url="";

                            if(!videosArray.equals("")) {
                                for(int i=0; i<videosArray.length(); ++i) {
                                    JSONObject movieVideoObject = videosArray.getJSONObject(i);
                                    if(movieVideoObject.getString("type").equals("Trailer"))
                                        video_url = movieVideoObject.getString("key");
                                }
                            }
                            else
                                video_url = "";

                            YoutubeFragment fragment;
                            if(TextUtils.isEmpty(video_url))
                                findViewById(R.id.main_container).setVisibility(GONE);
                            else {
                                fragment = YoutubeFragment.newInstance(Youtube_URL);
                                FragmentManager manager = getSupportFragmentManager();
                                manager.beginTransaction()
                                        .replace(R.id.main_container, fragment)
                                        .commit();
                            }

                            ratingBar.setVisibility(VISIBLE);
                            ratingBar.setRating((Float.parseFloat(parentObject.getString("vote_average")))/2);

                            tvVoteCount.setText("Number of Votes: "+NumberFormat.getNumberInstance(Locale.US).format(parentObject.getLong("vote_count")));

                            if(!parentObject.getString("tagline").equals("")) {
                                tvTagline.setVisibility(VISIBLE);
                                tvTagline.setText("\"" + parentObject.getString("tagline") + "\"");
                            }
                            else
                                tvTagline.setVisibility(GONE);

                            if(!parentObject.getString("backdrop_path").equals("null"))
                            Picasso.with(ScrollingActivity.this).load("https://image.tmdb.org/t/p/w500"+parentObject.getString("backdrop_path"))
                                    .resize(backdropImage.getWidth(), (int)(backdropImage.getWidth()*0.5625)).into(backdropImage);

                            if(parentObject.getLong("revenue") != 0)
                                tvMovieRevenue.setText("Revenue: "+ NumberFormat.getCurrencyInstance(Locale.US).format(parentObject.getLong("revenue")));
                            else
                                tvMovieRevenue.setVisibility(GONE);

                            } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(ScrollingActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                        error.printStackTrace();
                    }
                }
        );

        VolleySingleton.getInstance(ScrollingActivity.this).addToRequestQueue(jsonObjectRequest);

        JsonObjectRequest castRequest = new JsonObjectRequest(Request.Method.GET,
                "https://api.themoviedb.org/3/movie/" + myId + "/credits?api_key=dda1dd643a75fd4f2a7239a0daac9c47",
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject parentObject) {

                        try {
                            JSONArray parentArray = parentObject.getJSONArray("cast");

                            List<CastModel> castModels = new ArrayList<>();

                            for (int i = 0; i < parentArray.length(); ++i) {
                                JSONObject finalObject = parentArray.getJSONObject(i);
                                CastModel myCastModel = new CastModel();
                                myCastModel.setChracter(finalObject.getString("character"));
                                myCastModel.setName(finalObject.getString("name"));
                                myCastModel.setId(finalObject.getLong("id"));
                                castModels.add(myCastModel);
                            }

                            castTextViews = new TextView[castModels.size()];

                            for(int i=0; i<castModels.size(); ++i){
                                final TextView mTextView = new TextView(ScrollingActivity.this);
                                mTextView.setText(castModels.get(i).getChracter()+"-"+castModels.get(i).getName());
                                mTextView.setClickable(true);
                                mTextView.setTag(castModels.get(i).getId());
                                mTextView.setOnClickListener(myOnClickListener);
                                castListView.addView(mTextView);
                                castTextViews[i] = mTextView;
                            }
                            castListAdapter.addAll(castList);

                            JSONArray crewArray = parentObject.getJSONArray("crew");
                            List<CrewModel> crewModels = new ArrayList<>();
                            String director="Director(s): ";
                            String producer="Producer(s): ";
                            boolean more_than_1_dir = false;
                            boolean more_than_1_pro = false;

                            for(int i=0; i<crewArray.length(); ++i){
                                JSONObject crewObject = crewArray.getJSONObject(i);
                                CrewModel myCrewModel = new CrewModel();

                                myCrewModel.setJob(crewObject.getString("job"));
                                myCrewModel.setName(crewObject.getString("name"));
                                myCrewModel.setId(crewObject.getLong("id"));
                                crewModels.add(myCrewModel);
                            }

                            /*for(int i = 0; i < crew_jobs.size(); ++i) {
                                if (crew_jobs.get(i).equals("Director") && !crew_names.get(i).equals("")) {

                                    tvDirector.setVisibility(VISIBLE);
                                    if (more_than_1_dir)
                                        director = director.concat(", " + crew_names.get(i));
                                    else
                                        director = director.concat(crew_names.get(i));
                                    more_than_1_dir = true;
                                }

                                if (crew_jobs.get(i).equals("Producer") && !crew_names.get(i).equals("")) {
                                    tvProducer.setVisibility(VISIBLE);
                                    if(more_than_1_pro)
                                        producer = producer.concat(", "+crew_names.get(i));
                                    else
                                        producer = producer.concat(crew_names.get(i));
                                    more_than_1_pro = true;
                                }
                            }
                            tvDirector.setText(director);
                            tvProducer.setText(producer);*/

                            for(int i=0; i<crewModels.size(); ++i){
                                if(crewModels.get(i).getJob().equals("Director") && !crewModels.get(i).getName().equals("")){
                                    tvDirector.setVisibility(VISIBLE);
                                    if (more_than_1_dir)
                                        director = director.concat(", " + crewModels.get(i).getName());
                                    else
                                        director = director.concat(crewModels.get(i).getName());
                                    more_than_1_dir = true;
                                }
                                if(crewModels.get(i).getJob().equals("Producer") && !crewModels.get(i).getName().equals("")){
                                    tvDirector.setVisibility(VISIBLE);
                                    if(more_than_1_pro)
                                        producer = producer.concat(", "+crewModels.get(i).getName());
                                    else
                                        producer = producer.concat(crewModels.get(i).getName());
                                    more_than_1_pro = true;
                                }
                            }

                            tvDirector.setText(director);
                            tvProducer.setText(producer);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });

        VolleySingleton.getInstance(ScrollingActivity.this).addToRequestQueue(castRequest);
    }



    /*public class MovieDetailsFetcher extends AsyncTask<String, String, List<String>> implements View.OnClickListener{

        Context context;
        MovieDetailsFetcher(Context context){
            this.context = context;
        }

        String image;
        String video_url = null;
        int code;

        @Override
        protected List<String> doInBackground(String... params) {

            long id = Long.parseLong(params[0]);

            try {
                URL url = new URL("https://api.themoviedb.org/3/movie/"+id+"?api_key=dda1dd643a75fd4f2a7239a0daac9c47&append_to_response=videos");

                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.connect();

                code = httpURLConnection.getResponseCode();

                if(code != 200) {
                    myDetails.add(String.valueOf(code));
                    return myDetails;
                }

                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                StringBuilder stringBuilder = new StringBuilder();

                String line;
                while((line=bufferedReader.readLine())!=null){
                    stringBuilder.append(line);
                }

                final String finalJSON = stringBuilder.toString();

                Log.d("Error", finalJSON);

                JSONObject parentObject = new JSONObject(finalJSON);
                JSONObject videosObject = parentObject.getJSONObject("videos");
                JSONArray videosArray = videosObject.getJSONArray("results");

                if(!videosArray.equals("")) {
                    for(int i=0; i<videosArray.length(); ++i) {
                        JSONObject movieVideoObject = videosArray.getJSONObject(i);
                        if(movieVideoObject.getString("type").equals("Trailer"))
                            video_url = movieVideoObject.getString("key");
                    }
                }
                else
                    video_url = "";

                myDetails.add(parentObject.getString("title"));
                myDetails.add(parentObject.getString("overview"));                  Done

                if(!parentObject.getString("release_date").equals(""))              Done
                    myDetails.add(parentObject.getString("release_date"));
                else
                    myDetails.add(null);

                if(parentObject.getInt("runtime") != 0)                             Done
                    myDetails.add(parentObject.getString("runtime"));
                else
                    myDetails.add(null);

                if(parentObject.getLong("budget") != 0)                             Done
                    myDetails.add("Budget: "+ NumberFormat.getCurrencyInstance(Locale.US).format(parentObject.getLong("budget")));
                else
                    myDetails.add(null);

                myDetails.add("Adult: "+parentObject.getString("adult"));           Done
                myDetails.add(parentObject.getString("vote_average"));              Done
                myDetails.add("Number of Votes: "+NumberFormat.getNumberInstance(Locale.US).format(parentObject.getLong("vote_count")));        Done

                if(!parentObject.getString("tagline").equals(""))                   Done
                    myDetails.add("\""+parentObject.getString("tagline")+"\"");
                else
                    myDetails.add(null);

                myDetails.add(parentObject.getString("backdrop_path"));             Done

                if(parentObject.getLong("revenue") != 0)
                    myDetails.add("Revenue: "+ NumberFormat.getCurrencyInstance(Locale.US).format(parentObject.getLong("revenue")));
                else
                    myDetails.add(null);

                return myDetails;
            }  catch (final IOException e) {
                Log.d("Error", "IOException entered");
                runOnUiThread(
                        new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(ScrollingActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                );
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return myDetails;
        }

        @Override
        protected void onPostExecute(List<String> results){

            if(results.get(0).equals(String.valueOf(404))) {
                Toast.makeText(ScrollingActivity.this, "Details not found", Toast.LENGTH_SHORT).show();
                ((Activity)context).finish();
                return;
            }

            tvStory.setText(results.get(1));

            if(TextUtils.isEmpty(results.get(2))){
                tvReleaseDate.setVisibility(GONE);
            }
            else {
                String date[] = results.get(2).split("-");
                String movieDate = "Release Date: "+date[2]+"-"+date[1]+"-"+date[0];
                tvReleaseDate.setText(movieDate);
            }

            if(TextUtils.isEmpty(results.get(3))){
                tvRuntime.setVisibility(GONE);
            }
            else {
                String finalTime;
                int minutes = Integer.parseInt(results.get(3));
                int hours = minutes/60;
                if(minutes%60 != 0)
                    finalTime = "Runtime: "+minutes+" minutes ("+hours+"h "+minutes%60+"m)";
                else
                    finalTime = "Runtime: "+minutes+" minutes ("+hours+" hours)";
                tvRuntime.setText(finalTime);
            }

            if(TextUtils.isEmpty(results.get(4))){
                tvMovieBudget.setVisibility(GONE);
            }
            else {
                tvMovieBudget.setText(results.get(4));
            }

            tvAdult.setText(results.get(5));
            tvVoteCount.setText(results.get(7));

            if(TextUtils.isEmpty(results.get(8))){
            }
            else {
                tvTagline.setVisibility(VISIBLE);
                tvTagline.setText(results.get(8));
            }

            ratingBar.setVisibility(VISIBLE);
            ratingBar.setRating(Float.parseFloat(results.get(6))/2);

            if(!results.get(9).equals("null"))
                Picasso.with(ScrollingActivity.this).load("https://image.tmdb.org/t/p/w500"+results.get(9)).resize(backdropImage.getWidth(), (int)(backdropImage.getWidth()*0.5625)).into(backdropImage, new Callback() {
                    @Override
                    public void onSuccess() {
                        detailsProgress.setVisibility(GONE);
                    }

                    @Override
                    public void onError() {
                        detailsProgress.setVisibility(GONE);
                    }
                });
            else
                detailsProgress.setVisibility(GONE);
            image = results.get(9);
            backdropImage.setOnClickListener(this);

            if(TextUtils.isEmpty(results.get(10))){
                tvMovieRevenue.setVisibility(GONE);
            }
            else{
                tvMovieRevenue.setText(results.get(10));
            }
            Youtube_URL = video_url;
        *//*    youTubePlayerView = (YouTubePlayerView) findViewById(R.id.youtubePlayerView);
            onInitializedListener = new YouTubePlayer.OnInitializedListener() {
                @Override
                public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
                    if(!TextUtils.isEmpty(Youtube_URL)) {
                        youTubePlayerView.setVisibility(VISIBLE);
                        if (!b)
                            youTubePlayer.cueVideo(Youtube_URL);
                    }
                }

                @Override
                public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
                    Toast.makeText(context, "Install/Update Official Youtube App", Toast.LENGTH_SHORT).show();
                }
            };
            youTubePlayerView.initialize(Youtube_key, onInitializedListener);*//*

            YoutubeFragment fragment;
            if(TextUtils.isEmpty(Youtube_URL))
                findViewById(R.id.main_container).setVisibility(GONE);
            else {
                fragment = YoutubeFragment.newInstance(Youtube_URL);
                FragmentManager manager = getSupportFragmentManager();
                manager.beginTransaction()
                        .replace(R.id.main_container, fragment)
                        .commit();
            }
        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(getApplicationContext(), ImageViewer.class);
            intent.putExtra("Image_Path", image);
            startActivity(intent);
        }
    }*/

/*
    public class CastFetcher extends AsyncTask<String, String, List<List<String>>> {

        public CastFetcher(){}

        @Override
        protected List<List<String>> doInBackground(String... params) {
            String id = params[0];

            try {
                List<List<String>> finalList = new ArrayList<>();

                URL castURL = new URL("https://api.themoviedb.org/3/movie/" + id + "/credits?api_key=dda1dd643a75fd4f2a7239a0daac9c47");
                HttpURLConnection castConnection = (HttpURLConnection) castURL.openConnection();
                castConnection.connect();

                InputStream inputStream = castConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

                StringBuilder stringBuilder = new StringBuilder();

                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    stringBuilder.append(line);
                }

                String finalJSON = stringBuilder.toString();

                JSONObject parentObject = new JSONObject(finalJSON);
                JSONArray parentArray = parentObject.getJSONArray("cast");

                List<String> castNames = new ArrayList<>();
                List<String> castCharacters = new ArrayList<>();
                List<String> castId = new ArrayList<>();

                List<CastModel> castModels = new ArrayList<>();

                //Adding cast to list
                for (int i = 0; i < parentArray.length(); ++i) {
                    JSONObject finalObject = parentArray.getJSONObject(i);
                    CastModel myCastModel = new CastModel();

                    myCastModel.setChracter(finalObject.getString("character"));
                    myCastModel.setName(finalObject.getString("name"));
                    myCastModel.setId(finalObject.getLong("id"));
                    castModels.add(myCastModel);
                }

                for (int i = 0; i < castModels.size(); ++i) {
                    castNames.add(castModels.get(i).getName());
                    castCharacters.add(castModels.get(i).getChracter());
                    castId.add(String.valueOf(castModels.get(i).getId()));
                }
                finalList.add(castCharacters);
                finalList.add(castNames);
                finalList.add(castId);

                JSONArray crewArray = parentObject.getJSONArray("crew");
                List<CrewModel> crewModels = new ArrayList<>();
                List<String> crewJobs = new ArrayList<>();
                List<String> crewNames = new ArrayList<>();
                List<String> crewId = new ArrayList<>();

                //Adding crew to list
                for(int i=0; i<crewArray.length(); ++i){
                    JSONObject crewObject = crewArray.getJSONObject(i);
                    CrewModel myCrewModel = new CrewModel();

                    myCrewModel.setJob(crewObject.getString("job"));
                    myCrewModel.setName(crewObject.getString("name"));
                    myCrewModel.setId(crewObject.getLong("id"));
                    crewModels.add(myCrewModel);
                }

                for(int i=0; i<crewModels.size(); ++i){
                    crewJobs.add(crewModels.get(i).getJob());
                    crewNames.add(crewModels.get(i).getName());
                    crewId.add(String.valueOf(crewModels.get(i).getId()));
                }
                finalList.add(crewJobs);
                finalList.add(crewNames);
                finalList.add(crewId);

                return finalList;

            } catch (final IOException e) {
                runOnUiThread(
                        new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(ScrollingActivity.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                );
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        public void onPostExecute(List<List<String>> results) {

            if(results==null)
                return;

            List<String> characters = results.get(0);
            List<String> names = results.get(1);
            List<String> id = results.get(2);
            List<String> crew_jobs = results.get(3);
            final List<String> crew_names = results.get(4);
            List<String> crew_id = results.get(5);

            if(characters.size()==0 && names.size()==0){
            }
            else{
                castHeading.setVisibility(VISIBLE);
                castHeading.setTypeface(custom_font);

                for (int i = 0; i < characters.size(); ++i) {
            */
/*        tvCast.append(Html.fromHtml("<i>"+characters.get(i)+"</i>"));
                    tvCast.append(" - ");
                    tvCast.append(names.get(i));
                    tvCast.append("\n");*//*

                    castList.add(Html.fromHtml("<i>"+characters.get(i)+"</i>")+" - "+names.get(i));
                }

                castTextViews = new TextView[castList.size()];

                for(int i=0; i<castList.size(); ++i){
                    final TextView mTextView = new TextView(ScrollingActivity.this);
                    mTextView.setText(castList.get(i));
                    mTextView.setClickable(true);
                    mTextView.setTag(id.get(i));
                    //    mTextView.setTypeface(custom_font2);
                    mTextView.setOnClickListener(myOnClickListener);
                    castListView.addView(mTextView);
                    castTextViews[i] = mTextView;
                }
            }

            castListAdapter.addAll(castList);
            String director="Director(s): ";
            String producer="Producer(s): ";
            boolean more_than_1_dir = false;
            boolean more_than_1_pro = false;

            if(crew_jobs.size()==0 && crew_names.size()==0){
            }
            else{
                for(int i = 0; i < crew_jobs.size(); ++i) {
                    if (crew_jobs.get(i).equals("Director") && !crew_names.get(i).equals("")) {

                        tvDirector.setVisibility(VISIBLE);
                        if (more_than_1_dir)
                            director = director.concat(", " + crew_names.get(i));
                        else
                            director = director.concat(crew_names.get(i));
                        more_than_1_dir = true;
                    }

                    if (crew_jobs.get(i).equals("Producer") && !crew_names.get(i).equals("")) {
                        tvProducer.setVisibility(VISIBLE);
                        if(more_than_1_pro)
                            producer = producer.concat(", "+crew_names.get(i));
                        else
                            producer = producer.concat(crew_names.get(i));
                        more_than_1_pro = true;
                    }
                }
                tvDirector.setText(director);
                tvProducer.setText(producer);
            }
        }
    }
*/

    class ImageFetcher extends AsyncTask<String, String, List<List<String>>>{

        @Override
        protected List<List<String>> doInBackground(String... params) {

            String id = params[0];
            List<List<String>> finalList = new ArrayList<>();

            try {
                URL imageIRL = new URL("https://api.themoviedb.org/3/movie/" + id + "/images?api_key=dda1dd643a75fd4f2a7239a0daac9c47");
                HttpURLConnection imageHttpUrlConnection = (HttpURLConnection) imageIRL.openConnection();
                imageHttpUrlConnection.connect();

                InputStream inputStream = imageHttpUrlConnection.getInputStream();

                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                StringBuilder stringBuilder = new StringBuilder();

                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    stringBuilder.append(line);
                }

                String finalJSON = stringBuilder.toString();
                JSONObject parentObject = new JSONObject(finalJSON);

                Log.d("ParentArray", finalJSON);

                JSONArray backdropsArray = parentObject.getJSONArray("backdrops");
                JSONArray postersArray = parentObject.getJSONArray("posters");

                List<String> backdropsAddress = new ArrayList<>();
                List<String> postersAddress = new ArrayList<>();

                for (int i = 0; i < backdropsArray.length(); ++i) {
                    JSONObject backdropsObject = backdropsArray.getJSONObject(i);
                    backdropsAddress.add(backdropsObject.getString("file_path"));
                }

                for (int i = 0; i < postersArray.length(); ++i) {
                    JSONObject postersObject = postersArray.getJSONObject(i);
                    postersAddress.add(postersObject.getString("file_path"));
                }

                finalList.add(backdropsAddress);
                finalList.add(postersAddress);

                return finalList;

            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        public void onPostExecute(List<List<String>> results){

            if(results==null)
                return;

            List<String> backdropsList = results.get(0);
            List<String> postersList = results.get(1);

            if(backdropsList.size()==0){
            }
            else{
                tvBackdrops.setVisibility(VISIBLE);
                BackdropsAdapter backdropsAdapter = new BackdropsAdapter(getApplicationContext(), backdropsList);
                backdropsRecyclerView.setAdapter(backdropsAdapter);
            }

            if(postersList.size()==0) {
            }
            else{
                tvPosters.setVisibility(VISIBLE);
                PostersAdapter postersAdapter = new PostersAdapter(getApplicationContext(), postersList);
                postersRecyclerView.setAdapter(postersAdapter);
            }
        }
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

    @Nullable
    @Override
    public ActionMode onWindowStartingSupportActionMode(ActionMode.Callback callback) {
        return null;
    }

    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */

        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }

        TextView helloTv;

        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState){

            View rootView = inflater.inflate(R.layout.scrolling_activity_tab, container, false);
            helloTv = (TextView) rootView.findViewById(R.id.helloTv);
            helloTv.setText("Section: "+getArguments().getInt(ARG_SECTION_NUMBER));
            return rootView;
        }
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return PlaceholderFragment.newInstance(position + 1);
        }

        @Override
        public int getCount() {
            // Show total pages.
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {

            if(position == 0)
                return "Overview";

            String episode_title = "Episode ";
            //int episode_number = position+1;

            return episode_title+position;
        }
    }
}
