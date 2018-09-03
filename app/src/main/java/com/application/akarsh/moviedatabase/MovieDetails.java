package com.application.akarsh.moviedatabase;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatCallback;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.application.akarsh.moviedatabase.models.CastModel;
import com.application.akarsh.moviedatabase.models.CrewModel;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
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
import java.net.URL;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class MovieDetails extends YouTubeBaseActivity implements AppCompatCallback{

    List<String> myDetails = new ArrayList<>();
    List<String> castList = new ArrayList<>();
    LinearLayout castListView;
    TextView tvMovieName, tvReleaseDate, tvMovieBudget, tvRuntime, tvVoteCount, tvTagline, tvAdult, tvStory;
    TextView  castHeading, tvBackdrops, tvPosters, tvMovieRevenue, tvDirector, tvProducer;
    RatingBar ratingBar;
    ImageView backdropImage;
    ProgressBar detailsProgress;
    RecyclerView backdropsRecyclerView, postersRecyclerView;
    GridLayoutManager gridLayoutManagerPosters, gridLayoutManagerImages;
    ArrayAdapter<String> castListAdapter;
    Typeface custom_font, custom_font2;
    YouTubePlayerView youTubePlayerView;
    YouTubePlayer.OnInitializedListener onInitializedListener;
    String Youtube_key = "AIzaSyBaIgqSaeySzd7BcHnZbal7P8RldBLv2tE";
    String Youtube_URL = "";
    TextView castTextViews[];
    View.OnClickListener myOnClickListener;
    AppCompatDelegate delegate;

    @Override
    public void onSupportActionModeStarted(ActionMode mode) {

    }

    @Override
    public void onSupportActionModeFinished(ActionMode mode) {

    }

    @Nullable
    @Override
    public ActionMode onWindowStartingSupportActionMode(ActionMode.Callback callback) {
        return null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);

        delegate = AppCompatDelegate.create(this, this);
        delegate.onCreate(savedInstanceState);
        delegate.setContentView(R.layout.activity_movie_details);

        Toolbar toolbar = (Toolbar) findViewById(R.id.my_awesome_toolbar);
        delegate.setSupportActionBar(toolbar);
        delegate.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
       // delegate.getSupportActionBar().setIcon(R.mipmap.app_icon);
        toolbar.setTitle(R.string.movies);

        castListView = (LinearLayout) findViewById(R.id.castListView);
        castListView.setClickable(false);
        castListAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, castList);
        tvMovieName = (TextView) findViewById(R.id.tvMovieName);
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
        tvMovieName.setTypeface(custom_font1, Typeface.BOLD);

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

        String myId = getIntent().getExtras().get("ID").toString();

        MovieDetailsFetcher movieDetailsFetcher = new MovieDetailsFetcher(this);
        CastFetcher castFetcher = new CastFetcher();
        ImageFetcher imageFetcher = new ImageFetcher();

        movieDetailsFetcher.executeOnExecutor(AsyncTask.SERIAL_EXECUTOR, myId);
        castFetcher.executeOnExecutor(AsyncTask.SERIAL_EXECUTOR, myId);
        imageFetcher.executeOnExecutor(AsyncTask.SERIAL_EXECUTOR, myId);

        myOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MovieDetails.this, ActorDetails.class);
                intent.putExtra("actor_id", String.valueOf(v.getTag()));
                startActivity(intent);
            }
        };
    }

    public class MovieDetailsFetcher extends AsyncTask<String, String, List<String>> implements View.OnClickListener{

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
                myDetails.add(parentObject.getString("overview"));

                if(!parentObject.getString("release_date").equals(""))
                    myDetails.add(parentObject.getString("release_date"));
                else
                    myDetails.add(null);

                if(parentObject.getInt("runtime") != 0)
                    myDetails.add(parentObject.getString("runtime"));
                else
                    myDetails.add(null);

                if(parentObject.getLong("budget") != 0)
                    myDetails.add("Budget: "+ NumberFormat.getCurrencyInstance(Locale.US).format(parentObject.getLong("budget")));
                else
                    myDetails.add(null);

                myDetails.add("Adult: "+parentObject.getString("adult"));
                myDetails.add(parentObject.getString("vote_average"));
                myDetails.add("Number of Votes: "+NumberFormat.getNumberInstance(Locale.US).format(parentObject.getLong("vote_count")));

                if(!parentObject.getString("tagline").equals(""))
                    myDetails.add("\""+parentObject.getString("tagline")+"\"");
                else
                    myDetails.add(null);

                myDetails.add(parentObject.getString("backdrop_path"));

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
                                Toast.makeText(MovieDetails.this, e.getMessage(), Toast.LENGTH_SHORT).show();
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
                Toast.makeText(MovieDetails.this, "Details not found", Toast.LENGTH_SHORT).show();
                ((Activity)context).finish();
                return;
            }

            tvMovieName.setText(results.get(0));
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
                Picasso.with(MovieDetails.this).load("https://image.tmdb.org/t/p/w500"+results.get(9)).resize(backdropImage.getWidth(), (int)(backdropImage.getWidth()*0.5625)).into(backdropImage, new Callback() {
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
            youTubePlayerView = (YouTubePlayerView) findViewById(R.id.youtubePlayerView);
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
            youTubePlayerView.initialize(Youtube_key, onInitializedListener);
        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(getApplicationContext(), ImageViewer.class);
            intent.putExtra("Image_Path", image);
            startActivity(intent);
        }
    }

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
                                Toast.makeText(MovieDetails.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
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
            /*        tvCast.append(Html.fromHtml("<i>"+characters.get(i)+"</i>"));
                    tvCast.append(" - ");
                    tvCast.append(names.get(i));
                    tvCast.append("\n");*/
                    castList.add(Html.fromHtml("<i>"+characters.get(i)+"</i>")+" - "+names.get(i));
                }

                castTextViews = new TextView[castList.size()];

                for(int i=0; i<castList.size(); ++i){
                    final TextView mTextView = new TextView(MovieDetails.this);
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
}
