package com.application.akarsh.moviedatabase;

import android.content.Context;
import android.content.Intent;
import android.graphics.Movie;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TabLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.application.akarsh.moviedatabase.models.CastModel;
import com.application.akarsh.moviedatabase.models.CrewModel;
import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class MovieDetail extends AppCompatActivity implements View.OnClickListener{

    static ImageView backdropImage;
    CollapsingToolbarLayout toolbar_layout;
    AppBarLayout appbar;
    static String myId = "";
   // static TextView castTextViews[];
    static View.OnClickListener myOnClickListener;
    static LinearLayout castListView;
    static ArrayAdapter<String> castListAdapter;
    static List<CastModel> castList = new ArrayList<>();
    static CastAdapter castAdapter;
    static RecyclerView castRecycler;
    static GridLayoutManager gridLayoutManager;

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        //  Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //    setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.setOffscreenPageLimit(3);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        backdropImage = (ImageView) findViewById(R.id.backdropImage);
        toolbar_layout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
        appbar = (AppBarLayout) findViewById(R.id.appbar);
      //  toolbar_layout.setTitleEnabled(true);
      //  toolbar_layout.setTitle("Despicable");
        gridLayoutManager = new GridLayoutManager(this, calculateNoOfColumns(this));
        castList.clear();

        myId = getIntent().getExtras().get("ID").toString();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        myOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MovieDetail.this, ActorDetails.class);
                intent.putExtra("actor_id", String.valueOf(v.getTag()));
                startActivity(intent);
            }
        };

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
        getMenuInflater().inflate(R.menu.menu_movie_detail, menu);
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

    @Override
    public void onClick(View v) {

    }


    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
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
            // Show 3 total pages.
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "DETAILS";
                case 1:
                    return "CAST";
                case 2:
                    return "SEE ALSO";
            }
            return null;
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

        public PlaceholderFragment() {
        }

        TextView tvTagLine, tvVoteCount, tvAdult, tvStory, tvDirector, tvProducer, tvReleaseDate, tvRuntime, tvMovieBudget, tvMovieRevenue;
        RatingBar ratingBar;
        RelativeLayout backgroundLayout;

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {

            View rootView = null;

            switch(getArguments().getInt(ARG_SECTION_NUMBER)){

                case 1:
                    rootView = inflater.inflate(R.layout.fragment_movie_detail, container, false);
                    tvVoteCount = (TextView) rootView.findViewById(R.id.tvVoteCount);
                    tvTagLine = (TextView) rootView.findViewById(R.id.tvTagLine);
                    tvAdult = (TextView) rootView.findViewById(R.id.tvAdult);
                    tvStory = (TextView) rootView.findViewById(R.id.tvStory);
                    tvDirector = (TextView) rootView.findViewById(R.id.tvDirector);
                    tvProducer = (TextView) rootView.findViewById(R.id.tvProducer);
                    tvReleaseDate = (TextView) rootView.findViewById(R.id.tvReleaseDate);
                    tvRuntime = (TextView) rootView.findViewById(R.id.tvRuntime);
                    tvMovieBudget = (TextView) rootView.findViewById(R.id.tvMovieBudget);
                    tvMovieRevenue = (TextView) rootView.findViewById(R.id.tvMovieRevenue);
                    ratingBar = (RatingBar) rootView.findViewById(R.id.ratingBar);
                    backgroundLayout = (RelativeLayout) rootView.findViewById(R.id.backgroundLayout);
                    getDetails();
                    break;

                case 2:
                    rootView = inflater.inflate(R.layout.fragment_cast, container, false);
                    castRecycler = (RecyclerView) rootView.findViewById(R.id.castRecycler);
                    castRecycler.setLayoutManager(gridLayoutManager);
                    //castListView = (LinearLayout) rootView.findViewById(R.id.castListView);
                    //castListView.setClickable(false);
                    //castListAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, castList);
//                    castAdapter = new CastAdapter(getContext(), castList);
                    getCast();
                 //   castRecycler.setAdapter(castAdapter);

                    break;

                case 3:
                    rootView = inflater.inflate(R.layout.fragment_recommended, container, false);
                    getRecommended();
                    break;
            }

            return rootView;
        }

        public void getDetails(){
            Toast.makeText(getContext(), "Details", Toast.LENGTH_SHORT).show();

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
                                /*JSONArray videosArray = videosObject.getJSONArray("results");

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
*/
                                ratingBar.setVisibility(VISIBLE);
                                ratingBar.setRating((Float.parseFloat(parentObject.getString("vote_average")))/2);

                                tvVoteCount.setText("Number of Votes: "+NumberFormat.getNumberInstance(Locale.US).format(parentObject.getLong("vote_count")));

                                if(!parentObject.getString("tagline").equals("")) {
                                    tvTagLine.setVisibility(VISIBLE);
                                    tvTagLine.setText("\"" + parentObject.getString("tagline") + "\"");
                                }
                                else
                                    tvTagLine.setVisibility(GONE);

                                if(backdropImage==null)
                                    Log.d("Image", parentObject.getString("backdrop_path")+"  NULL");

                                if(!parentObject.getString("backdrop_path").equals("null"))
                                    Glide.with(getContext()).load("https://image.tmdb.org/t/p/w500"+parentObject.getString("backdrop_path"))
                                            //        .resize(backdropImage.getWidth(), (int)(backdropImage.getWidth()*0.5625)).into(backdropImage);
                                            .override(backdropImage.getWidth(), (int)(backdropImage.getWidth()*0.5625)).into(backdropImage);

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
                            Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                            error.printStackTrace();
                        }
                    }
            );

            VolleySingleton.getInstance(getContext()).addToRequestQueue(jsonObjectRequest);
        }

        public void getCast(){
            Toast.makeText(getContext(), "Cast", Toast.LENGTH_SHORT).show();
            final JsonObjectRequest castRequest = new JsonObjectRequest(Request.Method.GET,
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
                                    myCastModel.setProfile_path(finalObject.getString("profile_path"));
                                    castModels.add(myCastModel);
                                }
                                castList.addAll(castModels);
                                Toast.makeText(getContext(), "CastList size: "+castList.size(), Toast.LENGTH_SHORT).show();
                                castAdapter = new CastAdapter(getContext(), getActivity(), castList);
                                castRecycler.setAdapter(castAdapter);
                                /*castTextViews = new TextView[castModels.size()];

                                for(int i=0; i<castModels.size(); ++i){
                                    final TextView mTextView = new TextView(getContext());
                                    mTextView.setText(castModels.get(i).getChracter()+"-"+castModels.get(i).getName());
                                    mTextView.setClickable(true);
                                    mTextView.setTag(castModels.get(i).getId());
                                    mTextView.setOnClickListener(myOnClickListener);
                                    castListView.addView(mTextView);
                                    castTextViews[i] = mTextView;
                                }
                                castListAdapter.addAll(castList);*/

                                /*JSONArray crewArray = parentObject.getJSONArray("crew");
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
                                }*/

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

                                /*for(int i=0; i<crewModels.size(); ++i){
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
                                tvProducer.setText(producer);*/

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.d("EROR", error.getLocalizedMessage());
                        }
                    });

            VolleySingleton.getInstance(getContext()).addToRequestQueue(castRequest);
        }

        public void getRecommended(){
            Toast.makeText(getContext(), "Recommended", Toast.LENGTH_SHORT).show();
        }
    }
}
