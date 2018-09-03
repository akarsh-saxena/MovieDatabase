package com.application.akarsh.moviedatabase;

import android.app.ProgressDialog;
import android.content.res.Configuration;
import android.os.Parcelable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.content.Context;
import android.support.v7.widget.ThemedSpinnerAdapter;
import android.content.res.Resources.Theme;

import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.application.akarsh.moviedatabase.models.MovieModel;
import com.application.akarsh.moviedatabase.models.MoviesModel;
import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexWrap;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class DetailsActivity extends AppCompatActivity {

    private static final String LIST_STATE_KEY = "myKey";
    //static List<MovieModel> movieModels = new ArrayList<>();
    static GridDisplayAdapter gridDisplayAdapter;
    static RecyclerView recyclerView;
    static GridLayoutManager gridLayoutManager;
    static List<MoviesModel.ResultsBean> movies = new ArrayList<>();
    static int current_page = 1;
    static private boolean loading = true;
    static int previousTotal = 0, visibleThreshold = 5, firstVisibleItem, visibleItemCount, totalItemCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        // Setup spinner
        Spinner spinner = (Spinner) findViewById(R.id.spinner);
        spinner.setAdapter(new MyAdapter(
                toolbar.getContext(),
                new String[]{
                        "Popular",
                        "New Release",
                        "Top Rated",
                }));

        spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // When the given dropdown item is selected, show its contents in the
                // container view.
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container, PlaceholderFragment.newInstance(position + 1))
                        .commit();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        gridDisplayAdapter = new GridDisplayAdapter(this, movies);
        movies.clear();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });




    }
    Parcelable mListState;

    @Override
    protected void onSaveInstanceState(Bundle state) {
        super.onSaveInstanceState(state);

        // Save list state
        mListState = gridLayoutManager.onSaveInstanceState();
        state.putParcelable(LIST_STATE_KEY, mListState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle state) {
        super.onRestoreInstanceState(state);

        // Retrieve list state and list/item positions
        if(state != null)
            mListState = state.getParcelable(LIST_STATE_KEY);
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (mListState != null) {
            gridLayoutManager.onRestoreInstanceState(mListState);
        }
    }

    public static int calculateNoOfColumns(Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        float dpWidth = displayMetrics.widthPixels / displayMetrics.density;
        int scalingFactor = 100;
        int noOfColumns = (int) (dpWidth / scalingFactor);
        return noOfColumns;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_details, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    private static class MyAdapter extends ArrayAdapter<String> implements ThemedSpinnerAdapter {
        private final ThemedSpinnerAdapter.Helper mDropDownHelper;

        public MyAdapter(Context context, String[] objects) {
            super(context, android.R.layout.simple_list_item_1, objects);
            mDropDownHelper = new ThemedSpinnerAdapter.Helper(context);
        }

        @Override
        public View getDropDownView(int position, View convertView, ViewGroup parent) {
            View view;

            if (convertView == null) {
                // Inflate the drop down using the helper's LayoutInflater
                LayoutInflater inflater = mDropDownHelper.getDropDownViewInflater();
                view = inflater.inflate(android.R.layout.simple_list_item_1, parent, false);
            } else {
                view = convertView;
            }

            TextView textView = (TextView) view.findViewById(android.R.id.text1);
            textView.setText(getItem(position));

            return view;
        }

        @Override
        public Theme getDropDownViewTheme() {
            return mDropDownHelper.getDropDownViewTheme();
        }

        @Override
        public void setDropDownViewTheme(Theme theme) {
            mDropDownHelper.setDropDownViewTheme(theme);
        }
    }


    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        static String url = "";

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_details, container, false);

            final ProgressDialog p = new ProgressDialog(getContext());
            p.setTitle("Please Wait!!");
            p.setMessage("Loading Movies..");
            p.show();

            recyclerView = (RecyclerView) rootView.findViewById(R.id.movieRecyclerView);
            movies.clear();
            if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
                gridLayoutManager = new GridLayoutManager(getContext(), calculateNoOfColumns(getContext()));
                recyclerView.setLayoutManager(gridLayoutManager);
                Toast.makeText(getContext(), "Rotated Portrait", Toast.LENGTH_SHORT).show();
            }
            else{
                gridLayoutManager = new GridLayoutManager(getContext(), calculateNoOfColumns(getContext()));
                recyclerView.setLayoutManager(gridLayoutManager);
                Toast.makeText(getContext(), "Rotated Horizontal", Toast.LENGTH_SHORT).show();
            }
            recyclerView.setAdapter(gridDisplayAdapter);
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
                                        getMovies(getContext());
                                    }
                                }

                                if (!loading && (totalItemCount - visibleItemCount)
                                        <= (firstVisibleItem + visibleThreshold)) {
                                    // End has been reached
                                    Toast.makeText(getContext(), "Loading More", Toast.LENGTH_SHORT).show();
                                    loading = true;
                                }
                            }

                        }
                    }
            );

            switch(getArguments().getInt(ARG_SECTION_NUMBER)){
                case 1:
                    //url = "http://api.themoviedb.org/3/discover/movie?api_key=dda1dd643a75fd4f2a7239a0daac9c47&sort_by=popularity.desc&page="+current_page;
                    getMovies(getContext());
                    break;

                case 2:
                    Calendar cal = Calendar.getInstance();
                    cal.add(Calendar.DATE, 1);
                    SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
                    String formattedDate = format1.format(cal.getTime());
                    url = "http://api.themoviedb.org/3/discover/movie?api_key=dda1dd643a75fd4f2a7239a0daac9c47&sort_by=release_date.desc&release_date.lte="+formattedDate+"&vote_count.gte=20";
                    break;

                case 3:
                    url = "http://api.themoviedb.org/3/discover/movie?api_key=dda1dd643a75fd4f2a7239a0daac9c47&sort_by=vote_average.desc&include_video=false&vote_count.gte=20";
                    break;

                default:
                    Toast.makeText(getContext(), "Entered Spinner Default", Toast.LENGTH_SHORT).show();
            }

            p.dismiss();
            return rootView;
        }
    }

    public static void getMovies(final Context context){
        String url = "http://api.themoviedb.org/3/discover/movie?api_key=dda1dd643a75fd4f2a7239a0daac9c47&sort_by=popularity.desc&page="+current_page;

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Gson gson = new Gson();
                        MoviesModel moviesModel = gson.fromJson(response, MoviesModel.class);
                        Toast.makeText(context, "Page: "+moviesModel.getPage(), Toast.LENGTH_SHORT).show();
                        Log.d("results", "page: "+current_page);
                        movies.addAll(moviesModel.getResults());
                        gridDisplayAdapter.notifyDataSetChanged();
                        //gridDisplayAdapter = new GridDisplayAdapter(context, movies);
                        //recyclerView.setAdapter(gridDisplayAdapter);
                        current_page++;
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                });

        VolleySingleton.getInstance(context).addToRequestQueue(stringRequest);
    }
}
