package com.application.akarsh.moviedatabase;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.NavUtils;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.application.akarsh.moviedatabase.models.MovieModel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class DisplayPage extends AppCompatActivity {

    RecyclerView recyclerView;
    GridLayoutManager gridLayoutManager;
    String name="", tag, formattedDate;
    ProgressBar displayPageProgress;
    Spinner spinnerSort;
    CheckBox cbVideos, cbOnTheAir;
    TextView tvSort;
    EditText etMinimumVotes;
    ConstraintLayout sortConstraint;
    Button btnDone;
    SwipeRefreshLayout swipeRefresh;
    int sortOptionSelected = 0;
    boolean cbSelected, cbOnTheAirSelected, isTvSeries = false;
    int total_pages = 1;
    int myCurrentPage = 1;
    int min_votes = 0;
    List<MovieModel> myMovieModels = new ArrayList<>();
    MyAdapter myAdapter = new MyAdapter(this, myMovieModels, false, false, false);
    private boolean loading = true;
    int previousTotal = 0, visibleThreshold = 5, firstVisibleItem, visibleItemCount, totalItemCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_page);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        sortConstraint = (ConstraintLayout) findViewById(R.id.sortConstraint);
        etMinimumVotes = (EditText) findViewById(R.id.etMinimumVotes);
        btnDone = (Button) findViewById(R.id.btnDone);
        displayPageProgress = (ProgressBar) findViewById(R.id.displayPageProgress);
        cbVideos = (CheckBox) findViewById(R.id.cbVideos);
        cbOnTheAir = (CheckBox) findViewById(R.id.cbOnTheAir);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        gridLayoutManager = new GridLayoutManager(DisplayPage.this, 1);
        recyclerView.setLayoutManager(gridLayoutManager);
        tvSort = (TextView) findViewById(R.id.tvSort);
        etMinimumVotes.requestFocus();
        spinnerSort = (Spinner) findViewById(R.id.spinnerSort);
        swipeRefresh = (SwipeRefreshLayout) findViewById(R.id.swipeRefresh);

        tag =  getIntent().getExtras().getString("Intent_Tag");

        switch(tag){

            case "get_popular_movies":
                Toast.makeText(this, "Popular in Display", Toast.LENGTH_SHORT).show();
                tvSort.setVisibility(TextView.GONE);
                spinnerSort.setVisibility(Spinner.GONE);
                cbVideos.setVisibility(CheckBox.GONE);
                cbOnTheAir.setVisibility(GONE);
                break;

            case "get_new_movies":
                tvSort.setVisibility(GONE);
                spinnerSort.setVisibility(GONE);
                cbVideos.setVisibility(GONE);
                myAdapter = new MyAdapter(this, myMovieModels, true, false, false);
                cbOnTheAir.setVisibility(GONE);
                break;

            case "get_highest_rated_movies":
                tvSort.setVisibility(GONE);
                spinnerSort.setVisibility(GONE);
                cbVideos.setVisibility(GONE);
                cbOnTheAir.setVisibility(GONE);
                break;

            case "search_movie":
                name = getIntent().getExtras().getString("Intent_Value");
                etMinimumVotes.setVisibility(GONE);
                btnDone.setVisibility(GONE);
                sortConstraint.setClipBounds(sortConstraint.getClipBounds());
                cbOnTheAir.setVisibility(GONE);
                break;

            case "discover_tv_series":
                cbVideos.setVisibility(GONE);
                cbOnTheAir.setVisibility(VISIBLE);
                etMinimumVotes.setVisibility(GONE);
                btnDone.setVisibility(GONE);
                isTvSeries = true;
                myAdapter = new MyAdapter(this, myMovieModels, false, true, false);
                sortConstraint.setClipBounds(sortConstraint.getClipBounds());
                break;

            case "search_tv_series":
                cbOnTheAir.setVisibility(GONE);
                cbVideos.setVisibility(GONE);
                name = getIntent().getExtras().getString("Intent_Value");
                etMinimumVotes.setVisibility(GONE);
                btnDone.setVisibility(GONE);
                isTvSeries = true;
                myAdapter = new MyAdapter(this, myMovieModels, false, true, false);
                sortConstraint.setClipBounds(sortConstraint.getClipBounds());
                break;

            default:
                Toast.makeText(this, "Entered Default", Toast.LENGTH_SHORT).show();

        }

        cbSelected = false;
        cbOnTheAirSelected = false;

        btnDone.setOnClickListener(
                new Button.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        if(etMinimumVotes.getText().toString().equals("")) {
                            Toast.makeText(DisplayPage.this, "Setting Minimum Number of Votes to 0", Toast.LENGTH_SHORT).show();
                            etMinimumVotes.setText("0");
                            min_votes = 0;
                        }
                        else
                            min_votes = Integer.parseInt(etMinimumVotes.getText().toString());

                        myMovieModels.clear();
                        myCurrentPage = 1;
                        getMovies();
                    }
                }
        );

        spinnerSort.setOnItemSelectedListener(
                new Spinner.OnItemSelectedListener(){
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        sortOptionSelected = position;
                        if(position != 0) {
                            myMovieModels.clear();
                            myCurrentPage = 1;
                            getMovies();
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                    }
                }
        );


        cbVideos.setOnClickListener(
                new CheckBox.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        if(cbVideos.isChecked()) {
                            cbSelected = true;
                            myMovieModels.clear();
                            myCurrentPage = 1;
                            Toast.makeText(DisplayPage.this, "Including Videos", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            cbSelected = false;
                            myMovieModels.clear();
                            myCurrentPage = 1;
                            Toast.makeText(DisplayPage.this, "Excluding Videos", Toast.LENGTH_SHORT).show();
                        }
                     getMovies();
                    }
                }
        );

        cbOnTheAir.setOnClickListener(
                new CheckBox.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        if(cbOnTheAir.isChecked()) {
                            cbOnTheAirSelected = true;
                            btnDone.setVisibility(GONE);
                            spinnerSort.setVisibility(GONE);
                            etMinimumVotes.setVisibility(GONE);
                            myMovieModels.clear();
                            myCurrentPage = 1;
                            Toast.makeText(DisplayPage.this, "Displaying on the air shows", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            cbOnTheAirSelected = false;
                            btnDone.setVisibility(View.GONE);
                            spinnerSort.setVisibility(VISIBLE);
                            etMinimumVotes.setVisibility(View.GONE);
                            cbOnTheAir.setVisibility(VISIBLE);
                            myMovieModels.clear();
                            myCurrentPage = 1;
                        }
                    }
                }
        );

        recyclerView.addOnScrollListener(
                new RecyclerView.OnScrollListener() {
                    @Override
                    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {

                        if(dy > 0) {
                            visibleItemCount = recyclerView.getChildCount();
                            totalItemCount = gridLayoutManager.getItemCount();
                            firstVisibleItem = gridLayoutManager.findFirstVisibleItemPosition();

                            if (loading) {
                                if (totalItemCount > previousTotal) {
                                    loading = false;
                                    previousTotal = totalItemCount;
                                    getMovies();
                                }
                            }

                            if (!loading && (totalItemCount - visibleItemCount)
                                    <= (firstVisibleItem + visibleThreshold)) {
                                // End has been reached
                                Toast.makeText(DisplayPage.this, "Loading More", Toast.LENGTH_SHORT).show();
                                loading = true;
                            }
                        }

                    }
                }
        );
        recyclerView.setAdapter(myAdapter);
        getMovies();

        swipeRefresh.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        refreshList();
                        swipeRefresh.setRefreshing(false);
                    }
                }
        );
    }

    @Override
    public void onBackPressed(){
        if(gridLayoutManager.findFirstCompletelyVisibleItemPosition() == 0)
            super.onBackPressed();
        else
            recyclerView.smoothScrollToPosition(0);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void refreshList(){
        getMovies();
    }

    void getMovies(){

        MovieFetcher movieFetcher = new MovieFetcher();
        movieFetcher.execute(String.valueOf(sortOptionSelected), String.valueOf(cbSelected), String.valueOf(myCurrentPage), String.valueOf(min_votes));
        myCurrentPage++;
    }

    public class MovieFetcher extends AsyncTask<String, String, List<MovieModel>> {

        @Override
        protected void onPreExecute(){
            displayPageProgress.setVisibility(VISIBLE);
        }

        @Override
        protected List<MovieModel> doInBackground(String... params) {

            try {
                String myURL;
                int sortOptionSelected = Integer.parseInt(params[0]);
                boolean cbSelected = Boolean.parseBoolean(params[1]);
                int current_page = Integer.parseInt(params[2]);
                final int minimum_votes = Integer.parseInt(params[3]);

                char[] myName;

                switch(tag){

                    case "get_popular_movies":
                        myURL = "http://api.themoviedb.org/3/discover/movie?api_key=dda1dd643a75fd4f2a7239a0daac9c47&sort_by=popularity.desc&vote_count.gte="+minimum_votes;
                        break;

                    case "get_new_movies":
                        Calendar cal = Calendar.getInstance();
                        cal.add(Calendar.DATE, 1);
                        SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
                        formattedDate = format1.format(cal.getTime());
                        myURL = "http://api.themoviedb.org/3/discover/movie?api_key=dda1dd643a75fd4f2a7239a0daac9c47&sort_by=release_date.desc&release_date.lte="+formattedDate+"&vote_count.gte="+minimum_votes;
                        Log.d("myDate: ", formattedDate);
                        break;

                    case "get_highest_rated_movies":
                        myURL = "http://api.themoviedb.org/3/discover/movie?api_key=dda1dd643a75fd4f2a7239a0daac9c47&sort_by=vote_average.desc&include_video=false&vote_count.gte="+minimum_votes;
                        break;

                    case "search_movie":
                        myName = name.toCharArray();
                        for(int i=0; i<name.length(); ++i)
                            if(myName[i]==' ')
                                myName[i]='+';
                        myURL = "https://api.themoviedb.org/3/search/movie?api_key=dda1dd643a75fd4f2a7239a0daac9c47&query=" + String.valueOf(myName);
                        break;

                    case "discover_tv_series":
                        myURL = "https://api.themoviedb.org/3/discover/tv?api_key=dda1dd643a75fd4f2a7239a0daac9c47";
                        break;

                    case "search_tv_series":
                        myName = name.toCharArray();
                        for(int i=0; i<name.length(); ++i)
                            if(myName[i]==' ')
                                myName[i]='+';
                        myURL = "https://api.themoviedb.org/3/search/tv?api_key=dda1dd643a75fd4f2a7239a0daac9c47&query=" + String.valueOf(myName);
                        break;

                    default:
                        myURL="";
                        Log.d("Default", "Entered Default in tag case");
                        Toast.makeText(DisplayPage.this, "Entered Default", Toast.LENGTH_SHORT).show();
                }

                final List<MovieModel> movieModels = new ArrayList<>();

                URL url = new URL(myURL + "&page=" + current_page);


                 //   Log.d("URL IS", "https://api.themoviedb.org/3/search/movie?api_key=dda1dd643a75fd4f2a7239a0daac9c47&query=" + String.valueOf(myName)+"&page="+j);

                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.connect();

                int code = httpURLConnection.getResponseCode();
                if(HttpURLConnection.HTTP_CLIENT_TIMEOUT == code || HttpURLConnection.HTTP_GATEWAY_TIMEOUT == code){
                    Toast.makeText(DisplayPage.this, "Check your internet connection", Toast.LENGTH_SHORT).show();
                    finish();
                }

                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

                StringBuilder stringBuilder = new StringBuilder();

                String line ;
                while ((line = reader.readLine()) != null) {
                    stringBuilder.append(line);
                }

                final String finalJSON = stringBuilder.toString();

                final JSONObject parentObject = new JSONObject(finalJSON);
                final JSONArray parentArray = parentObject.getJSONArray("results");

                int total_results = parentObject.getInt("total_results");

                if(total_results == 0){
                runOnUiThread(new Runnable() {
                                  @Override
                                  public void run() {
                                          Toast.makeText(DisplayPage.this, "No results", Toast.LENGTH_SHORT).show();
                                  }
                              });
                    finish();
                }

                total_pages = parentObject.getInt("total_pages");

                for (int i = 0; i < parentArray.length(); ++i) {
                    JSONObject finalObject = parentArray.getJSONObject(i);

                    if (!cbSelected && !isTvSeries)
                        if(finalObject.getBoolean("video"))
                            continue;

                    MovieModel movieModel = new MovieModel();

                    if(isTvSeries){
                        movieModel.setTitle(finalObject.getString("name"));
                        movieModel.setRelease_date(finalObject.getString("first_air_date"));
                    }
                    else{
                        movieModel.setTitle(finalObject.getString("title"));
                        movieModel.setRelease_date(finalObject.getString("release_date"));
                    }

                    movieModel.setVote_average((float) finalObject.getDouble("vote_average"));
                    movieModel.setStory(finalObject.getString("overview"));
                    movieModel.setId(finalObject.getLong("id"));
                    movieModel.setPosterPath(finalObject.getString("poster_path"));
                    movieModel.setBackdropImage(finalObject.getString("backdrop_path"));
                    movieModel.setVote_count(NumberFormat.getNumberInstance(Locale.US).format(finalObject.getLong("vote_count")));
                    movieModel.setPopularity(finalObject.getDouble("popularity"));
                    movieModels.add(movieModel);
                }

                switch(sortOptionSelected){
                    case 0:
                        break;

                    case 1:
                        Collections.sort(movieModels, MovieModel.MoviePopularityComparator);
                        break;

                    case 2:
                        Collections.sort(movieModels, MovieModel.MovieRatingComparator);
                        break;

                    case 3:
                        Collections.sort(movieModels, MovieModel.MovieDateComparatorNewest);
                        break;

                    case 4:
                        Collections.sort(movieModels, MovieModel.MovieDateComparatorOldest);
                        break;

                    default: Log.d("switchcase", "Entered Default");
                }
                return movieModels;

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(DisplayPage.this, "Check your internet connection", Toast.LENGTH_SHORT).show();
                    }
                });
                e.printStackTrace();
            } catch (Exception e){
                e.printStackTrace();
            }
            Log.d("Return new","new arrayist returned");
            return new ArrayList<>();
        }

    @Override
    protected void onPostExecute(List<MovieModel> result){
        super.onPostExecute(result);

        displayPageProgress.setVisibility(GONE);
        myMovieModels.addAll(result);
        myAdapter.notifyDataSetChanged();
        //myAdapter.notifyItemInserted(myMovieModels.size()-1);
    }
    }
}
