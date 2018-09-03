package com.application.akarsh.moviedatabase;

import android.os.AsyncTask;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;

import com.application.akarsh.moviedatabase.models.EpisodeModel;
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
import java.util.ArrayList;
import java.util.List;

public class TvSeasonDetails_old extends AppCompatActivity {

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
    int season_number;
    int episode_count = 1;
    String myId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tv_season_details_old);

        season_number = getIntent().getExtras().getInt("season_number");
        episode_count = getIntent().getExtras().getInt("episode_count");
        myId = getIntent().getExtras().getString("ID");

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.setOffscreenPageLimit(3);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_tv_season_details, menu);
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

        TextView episodeName, airDate, voteCount, overview;
        RatingBar ratingBar;
        ImageView backdropImage;
        ProgressBar backdropImagePB;

        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_tv_season_details_old, container, false);
            episodeName = (TextView) rootView.findViewById(R.id.episodeName);
            airDate = (TextView) rootView.findViewById(R.id.airDate);
            voteCount = (TextView) rootView.findViewById(R.id.voteCount);
            overview = (TextView) rootView.findViewById(R.id.overview);
            ratingBar = (RatingBar) rootView.findViewById(R.id.ratingBar);
            backdropImage = (ImageView) rootView.findViewById(R.id.backdropImage);
            backdropImagePB =(ProgressBar) rootView.findViewById(R.id.backdropImagePB);

    /*        Typeface custom_font = Typeface.createFromAsset(rootView.getContext().getAssets(), "fonts/blackjack.otf");
            episodeName.setTypeface(custom_font);

            Typeface custom_font1 = Typeface.createFromAsset(rootView.getContext().getAssets(), "fonts/Quicksand-Bold.otf");
            airDate.setTypeface(custom_font1);
            voteCount.setTypeface(custom_font1);
            overview.setTypeface(custom_font1);*/

            // episodeName.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));
            TvSeasonDetails_old tvSeasonDetailsOld = (TvSeasonDetails_old)getActivity();
            FetchEpisodeDetails fetchEpisodeDetails = new FetchEpisodeDetails();
            fetchEpisodeDetails.execute(tvSeasonDetailsOld.myId, String.valueOf(tvSeasonDetailsOld.season_number));
            return rootView;
        }

        public class FetchEpisodeDetails extends AsyncTask<String, String, List<EpisodeModel>>{

            @Override
            protected List<EpisodeModel> doInBackground(String... params) {

                String id = params[0];
                String season = params[1];

                try{

                    URL url = new URL("https://api.themoviedb.org/3/tv/"+id+"/season/"+season+"?api_key=dda1dd643a75fd4f2a7239a0daac9c47");
                    HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();

                    httpURLConnection.connect();

                    int response_code = httpURLConnection.getResponseCode();

                    List<EpisodeModel> episodeModels = new ArrayList<>();

                    if(response_code != 200)
                        return null;

                    InputStream inputStream = httpURLConnection.getInputStream();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

                    StringBuilder stringBuilder = new StringBuilder();

                    String line;
                    while((line=bufferedReader.readLine())!= null)
                        stringBuilder.append(line);

                    String finalJSON = stringBuilder.toString();

                    JSONObject parentObject = new JSONObject(finalJSON);
           //         JSONObject imagesObject = parentObject.getJSONObject("images");
           //         JSONArray postersArray = imagesObject.getJSONArray("posters");
                    JSONArray episodeArray = parentObject.getJSONArray("episodes");

                    for(int i=0; i<episodeArray.length(); ++i){

                        JSONObject episodeObject = episodeArray.getJSONObject(i);
                        EpisodeModel episodeModel = new EpisodeModel();

                        episodeModel.setAir_date(episodeObject.getString("air_date"));
                        episodeModel.setName(episodeObject.getString("name"));
                        episodeModel.setOverview(episodeObject.getString("overview"));
                        episodeModel.setStill_path(episodeObject.getString("still_path"));
                        episodeModel.setVote_average(episodeObject.getDouble("vote_average"));
                        episodeModel.setVote_count(episodeObject.getLong("vote_count"));
                        episodeModels.add(episodeModel);
                    }

                    return episodeModels;

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                return null;
            }

            @Override
            public void onPostExecute(List<EpisodeModel> results){

                if(results == null)
                    return;

                if(getArguments().getInt(ARG_SECTION_NUMBER)==1)
                    return;

                int width = getContext().getResources().getDisplayMetrics().widthPixels;

                episodeName.setText(results.get(getArguments().getInt(ARG_SECTION_NUMBER)-2).getName());
                overview.setText(results.get(getArguments().getInt(ARG_SECTION_NUMBER)-2).getOverview());
                voteCount.setText("Total Votes: "+results.get(getArguments().getInt(ARG_SECTION_NUMBER)-2).getVote_count());
                ratingBar.setVisibility(View.VISIBLE);
                ratingBar.setRating((float)(results.get(getArguments().getInt(ARG_SECTION_NUMBER)-2).getVote_average()/2));
                Picasso.with(getContext()).load("http://image.tmdb.org/t/p/w500/"+results.get(getArguments().getInt(ARG_SECTION_NUMBER)-2).getStill_path())
                        .resize(width, (int)(width*0.5625)).into(backdropImage);
                String date[] = results.get(getArguments().getInt(ARG_SECTION_NUMBER)-2).getAir_date().split("-");
                airDate.setText(date[2]+"-"+date[1]+"-"+date[0]);
            }
        }
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
            // Show total pages.
            return episode_count+1;
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