package com.application.akarsh.moviedatabase;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
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
import com.application.akarsh.moviedatabase.models.SeasonModel;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
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

public class TvSeriesDetails extends YouTubeBaseActivity implements AppCompatCallback {

    List<String> myDetails = new ArrayList<>();
    List<String> castList = new ArrayList<>();
    LinearLayout castListView;
    TextView tvMovieName, tvFirstAirDate, tvTotalEpisodes, tvTotalSeasons, tvVoteCount, tvHomepage, tvAdult, tvStory;
    TextView castHeading, tvBackdrops, tvPosters, tvLastAirDate, tvDirector, tvProducer, seasonsHeading;
    RatingBar ratingBar;
    ImageView backdropImage;
    ProgressBar mainProgress;
    RecyclerView seasonsRecyclerView, backdropsRecyclerView, postersRecyclerView;
    GridLayoutManager gridLayoutManagerSeasons, gridLayoutManagerPosters, gridLayoutManagerImages;
    ArrayAdapter<String> castListAdapter;
    FrameLayout frameLayoutBackdrop;
    Typeface custom_font, custom_font2;
    YouTubePlayerView youTubePlayerView;
    YouTubePlayer.OnInitializedListener onInitializedListener;
    String Youtube_key = "AIzaSyBaIgqSaeySzd7BcHnZbal7P8RldBLv2tE";
    String Youtube_URL = "";
    TextView castTextViews[];
    View.OnClickListener myOnClickListener;
    String finalJSONSeasons = "";
    String myId;
    AppCompatDelegate delegate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tv_series_details);

        delegate = AppCompatDelegate.create(this, this);
        delegate.onCreate(savedInstanceState);
        delegate.setContentView(R.layout.activity_tv_series_details);

        Toolbar toolbar = (Toolbar) findViewById(R.id.my_awesome_toolbar);
        delegate.setSupportActionBar(toolbar);
        delegate.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setTitle(R.string.television_series);

        castListView = (LinearLayout) findViewById(R.id.castListView);
        castListView.setClickable(false);
        castListAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, castList);
        tvMovieName = (TextView) findViewById(R.id.tvMovieName);
        tvFirstAirDate = (TextView) findViewById(R.id.tvFirstAirDate);
        tvTotalEpisodes = (TextView) findViewById(R.id.tvTotalEpisodes);
        tvLastAirDate = (TextView) findViewById(R.id.tvLastAirDate);
        tvTotalSeasons = (TextView) findViewById(R.id.tvTotalSeasons);
        tvVoteCount = (TextView) findViewById(R.id.tvVoteCount);
        tvHomepage = (TextView) findViewById(R.id.tvHomepage);
        tvHomepage.setVisibility(GONE);
        tvAdult = (TextView) findViewById(R.id.tvAdult);
        tvStory = (TextView) findViewById(R.id.tvStory);
        ratingBar = (RatingBar) findViewById(R.id.ratingBar);
        backdropImage = (ImageView) findViewById(R.id.backdropImage);
        castHeading = (TextView) findViewById(R.id.castHeading);
        castHeading.setVisibility(GONE);
        mainProgress = (ProgressBar) findViewById(R.id.mainProgress);
        tvBackdrops = (TextView) findViewById(R.id.tvBackdrops);
        tvBackdrops.setVisibility(GONE);
        tvPosters = (TextView) findViewById(R.id.tvPosters);
        tvPosters.setVisibility(GONE);
        tvDirector = (TextView) findViewById(R.id.tvDirector);
        tvDirector.setVisibility(GONE);
        tvProducer = (TextView) findViewById(R.id.tvProducer);
        tvProducer.setVisibility(GONE);
        seasonsHeading = (TextView) findViewById(R.id.seasonsHeading);
        seasonsRecyclerView = (RecyclerView) findViewById(R.id.seasonsRecyclerView);
        backdropsRecyclerView = (RecyclerView) findViewById(R.id.backdropsRecyclerView);
        postersRecyclerView = (RecyclerView) findViewById(R.id.postersRecyclerView);
        gridLayoutManagerSeasons = new GridLayoutManager(this, 1, GridLayoutManager.HORIZONTAL, false);
        gridLayoutManagerImages = new GridLayoutManager(this, 2, GridLayoutManager.HORIZONTAL, false);
        gridLayoutManagerPosters = new GridLayoutManager(this, 2, GridLayoutManager.HORIZONTAL, false);
        seasonsRecyclerView.setLayoutManager(gridLayoutManagerSeasons);
        backdropsRecyclerView.setLayoutManager(gridLayoutManagerImages);
        postersRecyclerView.setLayoutManager(gridLayoutManagerPosters);
        frameLayoutBackdrop = (FrameLayout) findViewById(R.id.frameLayoutBackdrop);

        custom_font = Typeface.createFromAsset(getAssets(), "fonts/Pacifico.ttf");

        Typeface custom_font1 = Typeface.createFromAsset(getAssets(), "fonts/blackjack.otf");
        tvMovieName.setTypeface(custom_font1, Typeface.BOLD);

        custom_font2 = Typeface.createFromAsset(getAssets(), "fonts/Quicksand-Bold.otf");
        tvAdult.setTypeface(custom_font2);
        tvTotalEpisodes.setTypeface(custom_font2);
        tvLastAirDate.setTypeface(custom_font2);
        tvVoteCount.setTypeface(custom_font2);
        tvFirstAirDate.setTypeface(custom_font2);
        tvTotalSeasons.setTypeface(custom_font2);
        tvDirector.setTypeface(custom_font2);
        tvProducer.setTypeface(custom_font2);
        tvStory.setTypeface(custom_font2);
        tvBackdrops.setTypeface(custom_font);
        tvPosters.setTypeface(custom_font);

        myId = Long.toString(getIntent().getExtras().getLong("ID"));

        TvSeriesDetailsFetcher TvSeriesDetailsFetcher = new TvSeriesDetailsFetcher(this);
        TvSeriesDetailsFetcher.executeOnExecutor(AsyncTask.SERIAL_EXECUTOR, myId);

        myOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            /*    Intent intent = new Intent(MovieDetails.this, DisplayPage.class);
                intent.putExtra("Id", id);
                startActivity(intent);*/
                Intent intent = new Intent(TvSeriesDetails.this, ActorDetails.class);
                intent.putExtra("actor_id", String.valueOf(v.getTag()));
                startActivity(intent);
                // Toast.makeText(MovieDetails.this, "Id: "+castTextViews[v.findViewWithTag("Id")].getText().toString(), Toast.LENGTH_SHORT).show();
            }
        };
    }

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

    public class TvSeriesDetailsFetcher extends AsyncTask<String, String, List<List<String>>> implements View.OnClickListener {

        Context context;

        TvSeriesDetailsFetcher(Context context) {
            this.context = context;
        }

        String image;
        String video_url = null;
        int code;

        @Override
        protected void onPreExecute() {
            mainProgress.setVisibility(VISIBLE);
        }

        @Override
        protected List<List<String>> doInBackground(String... params) {

            long id = Long.parseLong(params[0]);
            List<List<String>> finalList = new ArrayList<>();

            try {
                URL url = new URL("https://api.themoviedb.org/3/tv/" + id + "?api_key=dda1dd643a75fd4f2a7239a0daac9c47&append_to_response=videos,images,credits");
                // For Series Details
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.connect();

                code = httpURLConnection.getResponseCode();

                if (code != 200) {
                    myDetails.add(String.valueOf(code));
                    return finalList;
                }

                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                StringBuilder stringBuilder = new StringBuilder();

                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    stringBuilder.append(line);
                }

                final String finalJSON = stringBuilder.toString();

                JSONObject parentObject = new JSONObject(finalJSON);
                JSONObject videosObject = parentObject.getJSONObject("videos");
                JSONArray videosArray = videosObject.getJSONArray("results");

                if (!videosArray.equals("")) {
                    for (int i = 0; i < videosArray.length(); ++i) {
                        JSONObject movieVideoObject = videosArray.getJSONObject(i);
                        if (movieVideoObject.getString("type").equals("Trailer"))
                            video_url = movieVideoObject.getString("key");
                    }
                } else
                    video_url = "";

                myDetails.add(parentObject.getString("name"));
                myDetails.add(parentObject.getString("overview"));

                if (!parentObject.getString("first_air_date").equals(""))                                    //2
                    myDetails.add(parentObject.getString("first_air_date"));
                else
                    myDetails.add(null);

                if (parentObject.getInt("number_of_seasons") != 0)                                           //3
                    myDetails.add("Number of Seasons: " + parentObject.getString("number_of_seasons"));
                else
                    myDetails.add(null);

                if (parentObject.getInt("number_of_episodes") != 0)                                          //4
                    myDetails.add("Number of Episodes: " + parentObject.getLong("number_of_episodes"));
                else
                    myDetails.add(null);

                myDetails.add(parentObject.getString("vote_average"));                                      //5
                myDetails.add("Number of Votes: " + NumberFormat.getNumberInstance(Locale.US).format(parentObject.getLong("vote_count")));        //6

                if (!parentObject.getString("homepage").equals(""))                                          //7
                    myDetails.add(parentObject.getString("homepage"));
                else
                    myDetails.add(null);

                myDetails.add(parentObject.getString("backdrop_path"));                                     //8

                if (!parentObject.getString("last_air_date").equals(""))                                     //9
                    myDetails.add("Last air date: " + parentObject.getString("last_air_date"));
                else
                    myDetails.add(null);

                myDetails.add(finalJSON);                                                                   //10

                finalList.add(myDetails);                                                                   //<--- myDetails Ended  (index 0)

                JSONObject imagesObject = parentObject.getJSONObject("images");
                JSONArray backdropsArray = imagesObject.getJSONArray("backdrops");
                JSONArray postersArray = imagesObject.getJSONArray("posters");

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

                finalList.add(backdropsAddress);                                                            // index 1
                finalList.add(postersAddress);                                                              // index 2

                List<String> castNames = new ArrayList<>();
                List<String> castCharacters = new ArrayList<>();
                List<String> castId = new ArrayList<>();

                JSONObject creditsObject = parentObject.getJSONObject("credits");
                JSONArray castArray = creditsObject.getJSONArray("cast");

                List<CastModel> castModels = new ArrayList<>();

                //Adding cast to list
                for (int i = 0; i < castArray.length(); ++i) {
                    JSONObject finalObject = castArray.getJSONObject(i);
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
                finalList.add(castCharacters);                                                                 // index 3
                finalList.add(castNames);                                                                      // index 4
                finalList.add(castId);                                                                         // index 5

                JSONArray crewArray = creditsObject.getJSONArray("crew");
                List<CrewModel> crewModels = new ArrayList<>();
                List<String> crewJobs = new ArrayList<>();
                List<String> crewNames = new ArrayList<>();
                List<String> crewId = new ArrayList<>();

                //Adding crew to list
                for (int i = 0; i < crewArray.length(); ++i) {
                    JSONObject crewObject = crewArray.getJSONObject(i);
                    CrewModel myCrewModel = new CrewModel();

                    myCrewModel.setJob(crewObject.getString("job"));
                    myCrewModel.setName(crewObject.getString("name"));
                    myCrewModel.setId(crewObject.getLong("id"));
                    crewModels.add(myCrewModel);
                }

                for (int i = 0; i < crewModels.size(); ++i) {
                    crewJobs.add(crewModels.get(i).getJob());
                    crewNames.add(crewModels.get(i).getName());
                    crewId.add(String.valueOf(crewModels.get(i).getId()));
                }
                finalList.add(crewJobs);                                                                        // index 6
                finalList.add(crewNames);                                                                       // index 7
                finalList.add(crewId);                                                                          // index 8

                return finalList;
            } catch (final IOException e) {
                Log.d("Error", "IOException entered");
                runOnUiThread(
                        new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(TvSeriesDetails.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                );
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return finalList;
        }

        @Override
        protected void onPostExecute(List<List<String>> finalList) {

            List<String> results = finalList.get(0);

            if (results.get(0).equals(String.valueOf(404))) {
                Toast.makeText(TvSeriesDetails.this, "Details not found", Toast.LENGTH_SHORT).show();
                ((Activity) context).finish();
                return;
            }

            List<String> backdropsList = finalList.get(1);
            List<String> postersList = finalList.get(2);
            List<String> characters = finalList.get(3);
            List<String> names = finalList.get(4);
            List<String> id = finalList.get(5);
            List<String> crew_jobs = finalList.get(6);
            final List<String> crew_names = finalList.get(7);

            finalJSONSeasons = results.get(10);

            mainProgress.setVisibility(GONE);
            tvMovieName.setText(results.get(0));
            tvStory.setText(results.get(1));

            if (results.get(2).equals("null")) {
                tvFirstAirDate.setVisibility(GONE);
            } else {
                String date[] = results.get(2).split("-");
                String movieDate = "First Air Date: " + date[2] + "-" + date[1] + "-" + date[0];
                tvFirstAirDate.setText(movieDate);
            }

            if (TextUtils.isEmpty(results.get(3))) {
                tvTotalSeasons.setVisibility(GONE);
            } else {
                tvTotalSeasons.setText(results.get(3));
            }

            if (TextUtils.isEmpty(results.get(4))) {
                tvTotalEpisodes.setVisibility(GONE);
            } else {
                tvTotalEpisodes.setText(results.get(4));
            }

            tvVoteCount.setText(results.get(6));

            if (TextUtils.isEmpty(results.get(8))) {
            } else {
                tvHomepage.setVisibility(VISIBLE);
                tvHomepage.setText(results.get(7));
            }

            ratingBar.setVisibility(VISIBLE);
            ratingBar.setRating(Float.parseFloat(results.get(5)) / 2);

            if (!results.get(8).equals("null"))
                Picasso.with(TvSeriesDetails.this).load("https://image.tmdb.org/t/p/w500" + results.get(8)).resize(backdropImage.getWidth(), (int) (backdropImage.getWidth() * 0.5625)).into(backdropImage);
            else
                frameLayoutBackdrop.setVisibility(GONE);

            image = results.get(8);
            backdropImage.setOnClickListener(this);

            if (TextUtils.isEmpty(results.get(9))) {
                tvLastAirDate.setVisibility(GONE);
            } else {
                tvLastAirDate.setText(results.get(9));
            }
            Youtube_URL = video_url;
            youTubePlayerView = (YouTubePlayerView) findViewById(R.id.youtubePlayerView);
            onInitializedListener = new YouTubePlayer.OnInitializedListener() {
                @Override
                public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
                    if (!TextUtils.isEmpty(Youtube_URL)) {
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

            SeasonFetcher seasonFetcher = new SeasonFetcher();
            seasonFetcher.execute(results.get(10));

            if (characters.size() == 0 && names.size() == 0) {
            } else {
                castHeading.setVisibility(VISIBLE);
                castHeading.setTypeface(custom_font);

                for (int i = 0; i < characters.size(); ++i) {
                    castList.add(Html.fromHtml("<i>" + characters.get(i) + "</i>") + " - " + names.get(i));
                }

                castTextViews = new TextView[castList.size()];

                for (int i = 0; i < castList.size(); ++i) {
                    final TextView mTextView = new TextView(TvSeriesDetails.this);
                    mTextView.setText(castList.get(i));
                    mTextView.setClickable(true);
                    mTextView.setTag(id.get(i));
                    mTextView.setTypeface(custom_font2);
                    mTextView.setOnClickListener(myOnClickListener);
                    castListView.addView(mTextView);
                    castTextViews[i] = mTextView;
                }
            }

            castListAdapter.addAll(castList);
            String director = "Director(s): ";
            String producer = "Producer(s): ";
            boolean more_than_1_dir = false;
            boolean more_than_1_pro = false;

            if (crew_jobs.size() == 0 && crew_names.size() == 0) {
            } else {
                for (int i = 0; i < crew_jobs.size(); ++i) {
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
                        if (more_than_1_pro)
                            producer = producer.concat(", " + crew_names.get(i));
                        else
                            producer = producer.concat(crew_names.get(i));
                        more_than_1_pro = true;
                    }
                }
                tvDirector.setText(director);
                tvProducer.setText(producer);
            }

            if (backdropsList.size() == 0) {
            } else {
                tvBackdrops.setVisibility(VISIBLE);
                BackdropsAdapter backdropsAdapter = new BackdropsAdapter(getApplicationContext(), backdropsList);
                backdropsRecyclerView.setAdapter(backdropsAdapter);
            }

            if (postersList.size() == 0) {
            } else {
                tvPosters.setVisibility(VISIBLE);
                PostersAdapter postersAdapter = new PostersAdapter(getApplicationContext(), postersList);
                postersRecyclerView.setAdapter(postersAdapter);
            }
        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(getApplicationContext(), ImageViewer.class);
            intent.putExtra("Image_Path", image);
            startActivity(intent);
        }
    }

    public class SeasonFetcher extends AsyncTask<String, String, List<SeasonModel>> {

        @Override
        protected List<SeasonModel> doInBackground(String... params) {

            List<SeasonModel> myList = new ArrayList<>();

            try {
                String finalJSON = params[0];

                JSONObject parentObject = new JSONObject(finalJSON);
                JSONArray seasonsArray = parentObject.getJSONArray("seasons");


                for (int i = 0; i < seasonsArray.length(); ++i) {

                    SeasonModel seasonModel = new SeasonModel();
                    JSONObject seasonsObject = seasonsArray.getJSONObject(i);

                    seasonModel.setPoster_path(seasonsObject.getString("poster_path"));
                    seasonModel.setSeason_number(seasonsObject.getInt("season_number"));
                    seasonModel.setEpisode_count(seasonsObject.getInt("episode_count"));
                    seasonModel.setAir_date(seasonsObject.getString("air_date"));
                    seasonModel.setId(seasonsObject.getLong("id"));
                    myList.add(seasonModel);
                }

                return myList;

            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        public void onPostExecute(List<SeasonModel> results) {

            if (results == null)
                return;

            seasonsHeading.setTypeface(custom_font);
            SeasonsAdapter seasonsAdapter = new SeasonsAdapter(TvSeriesDetails.this, results, myId);
            seasonsRecyclerView.setAdapter(seasonsAdapter);
            seasonsHeading.setVisibility(VISIBLE);
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

 /*   public class CastFetcher extends AsyncTask<String, String, List<List<String>>> {

        public CastFetcher(){}

        @Override
        protected List<List<String>> doInBackground(String... params) {
            String id = params[0];

            try {
                List<List<String>> finalList = new ArrayList<>();

                URL castURL = new URL("https://api.themoviedb.org/3/tv/" + id + "/credits?api_key=dda1dd643a75fd4f2a7239a0daac9c47");
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
                                Toast.makeText(TvSeriesDetails.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
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

            if(characters.size()==0 && names.size()==0){
            }
            else{
                castHeading.setVisibility(VISIBLE);
                castHeading.setTypeface(custom_font);

                for (int i = 0; i < characters.size(); ++i) {
                    castList.add(Html.fromHtml("<i>"+characters.get(i)+"</i>")+" - "+names.get(i));
                }

                castTextViews = new TextView[castList.size()];

                for(int i=0; i<castList.size(); ++i){
                    final TextView mTextView = new TextView(TvSeriesDetails.this);
                    mTextView.setText(castList.get(i));
                    mTextView.setClickable(true);
                    mTextView.setTag(id.get(i));
                    mTextView.setTypeface(custom_font2);
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
    }*/
/*
    class ImageFetcher extends AsyncTask<String, String, List<List<String>>>{

        @Override
        protected List<List<String>> doInBackground(String... params) {

            String id = params[0];
            List<List<String>> finalList = new ArrayList<>();

            try {
                URL imageIRL = new URL("https://api.themoviedb.org/3/tv/" + id + "/images?api_key=dda1dd643a75fd4f2a7239a0daac9c47");
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
    }*/

