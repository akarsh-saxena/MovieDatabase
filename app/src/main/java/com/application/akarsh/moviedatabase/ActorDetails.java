package com.application.akarsh.moviedatabase;

import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

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

public class ActorDetails extends AppCompatActivity {

    ImageView actorImage;
    TextView actorName, tvhomepage, tvbirthDay, tvplaceOfBirth, tvdeathDay, tvbiography, biographyHeading;
    ProgressBar mainProgressBar, imageProgressBar;
    //String image_path="";
    String id
            ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actor_details);

        actorImage = (ImageView) findViewById(R.id.actorImage);
        imageProgressBar = (ProgressBar) findViewById(R.id.imageProgressBar);
        mainProgressBar = (ProgressBar) findViewById(R.id.mainProgressBar);
        actorName = (TextView) findViewById(R.id.actorName);
        tvhomepage= (TextView) findViewById(R.id.tvhomepage);
        tvbirthDay = (TextView) findViewById(R.id.tvbirthDay);
        tvplaceOfBirth = (TextView) findViewById(R.id.tvplaceOfBirth);
        tvdeathDay = (TextView) findViewById(R.id.tvdeathDay);
        tvbiography = (TextView) findViewById(R.id.tvbiography);
        biographyHeading = (TextView) findViewById(R.id.biographyHeading);

        id = getIntent().getExtras().getString("actor_id");

        Typeface custom_font = Typeface.createFromAsset(getAssets(), "fonts/DancingScript-Bold.ttf");
        actorName.setTypeface(custom_font);

        Typeface custom_font1 = Typeface.createFromAsset(getAssets(), "fonts/Quicksand-Bold.otf");
        tvhomepage.setTypeface(custom_font1);
        tvbirthDay.setTypeface(custom_font1);
        tvplaceOfBirth.setTypeface(custom_font1);
        tvdeathDay.setTypeface(custom_font1);
        tvbiography.setTypeface(custom_font1);

        Typeface custom_font2 = Typeface.createFromAsset(getAssets(), "fonts/Pacifico.ttf");
        biographyHeading.setTypeface(custom_font2);


        /*ActorDetailsFetcher actorDetailsFetcher = new ActorDetailsFetcher();
        actorDetailsFetcher.execute(id);*/

        tvhomepage.setOnClickListener(
                new TextView.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(ActorDetails.this, "Opening "+tvhomepage.getText().toString(), Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setData(Uri.parse(tvhomepage.getText().toString()));
                        startActivity(intent);
                    }
                }
        );

        String url = "https://api.themoviedb.org/3/person/"+id+"?api_key=dda1dd643a75fd4f2a7239a0daac9c47&language=en-US";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject parentObject) {

                        try {

                            String profile_path = parentObject.getString("profile_path");
                            String name = parentObject.getString("name");
                            String homepage = parentObject.getString("homepage");
                            String birthday = parentObject.getString("birthday");
                            String place_of_birth = parentObject.getString("place_of_birth");
                            String deathday = parentObject.getString("deathday");
                            String biography = parentObject.getString("biography");

                            if(!profile_path.equals("null"))
                                Picasso.with(ActorDetails.this).load("http://image.tmdb.org/t/p/w500/" + profile_path).into(actorImage);
                            else
                                Picasso.with(ActorDetails.this).load(R.mipmap.noimage).into(actorImage);

                            if(!name.equals("null")){
                                actorName.setVisibility(View.VISIBLE);
                                actorName.setText(name);
                            }

                            if(!homepage.equals("null")){
                                tvhomepage.setVisibility(View.VISIBLE);
                                tvhomepage.setText("Website: "+homepage);
                            }

                            if(!birthday.equals("null")){
                                String bday[] = birthday.split("-");
                                tvbirthDay.setVisibility(View.VISIBLE);
                                tvbirthDay.setText("Birthday: "+bday[2]+"-"+bday[1]+"-"+bday[0]);
                            }

                            if(!place_of_birth.equals("null")){
                                tvplaceOfBirth.setVisibility(View.VISIBLE);
                                tvplaceOfBirth.setText("Place of Birth: "+place_of_birth);
                            }

                            if(!deathday.equals("null")){
                                String dday[] = deathday.split("-");
                                tvdeathDay.setVisibility(View.VISIBLE);
                                tvdeathDay.setText("Died On: "+dday[2]+"-"+dday[1]+"-"+dday[0]);
                            }

                            if(!biography.equals("null")){
                                biographyHeading.setVisibility(View.VISIBLE);
                                tvbiography.setVisibility(View.VISIBLE);
                                tvbiography.setText(biography);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {https://api.themoviedb.org/3/person/" + id + "?api_key=
                        Toast.makeText(ActorDetails.this, ""+Constants.API_KEY+"&language=en-US", Toast.LENGTH_SHORT).show();
                        Toast.makeText(ActorDetails.this, "Not Found: "+error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

        VolleySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest);
    }

   /* class ActorDetailsFetcher extends AsyncTask<String, String, List<String>> implements View.OnClickListener {

        @Override
        public void onPreExecute(){
            mainProgressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected List<String> doInBackground(String... params) {

            String actorId = params[0];
            List<String> actorDetails = new ArrayList<>();

            try {
                URL url = new URL("https://api.themoviedb.org/3/person/"+actorId+"?api_key=dda1dd643a75fd4f2a7239a0daac9c47&language=en-US");

                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();

                httpURLConnection.connect();

                int response_code = httpURLConnection.getResponseCode();

                if(response_code != 200){
                    actorDetails.add(String.valueOf(response_code));
                    return actorDetails;
                }

                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                StringBuilder stringBuilder = new StringBuilder();

                String line;

                while((line=bufferedReader.readLine()) != null)
                    stringBuilder.append(line);

                String finalJSON = stringBuilder.toString();

                JSONObject parentObject = new JSONObject(finalJSON);

                actorDetails.add(parentObject.getString("profile_path"));
                actorDetails.add(parentObject.getString("name"));
                actorDetails.add(parentObject.getString("homepage"));
                actorDetails.add(parentObject.getString("birthday"));
                actorDetails.add(parentObject.getString("place_of_birth"));
                actorDetails.add(parentObject.getString("deathday"));
                actorDetails.add(parentObject.getString("biography"));

                return actorDetails;

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
        public void onPostExecute(final List<String> actorDetails){

            mainProgressBar.setVisibility(View.GONE);

            if(actorDetails.get(0).equals("404")){
                Toast.makeText(ActorDetails.this, "Details Not Found", Toast.LENGTH_SHORT).show();
                return;
            }

            String profile_path = actorDetails.get(0);
            String name = actorDetails.get(1);
            String homepage = actorDetails.get(2);
            String birthday = actorDetails.get(3);
            String place_of_birth = actorDetails.get(4);
            String deathday = actorDetails.get(5);
            String biography = actorDetails.get(6);

            actorImage.setOnClickListener(this);

            boolean isEmpty = true;

            for(int i=0; i<actorDetails.size(); ++i){
                if(i==1)
                    continue;

                if(!actorDetails.get(i).equals("null"))
                    isEmpty = false;
            }

            if(isEmpty) {
                Toast.makeText(ActorDetails.this, "No Details Found", Toast.LENGTH_SHORT).show();
                ActorDetails.this.finish();
            }

            *//*if(!profile_path.equals("null")) {
                Picasso.with(ActorDetails.this).load("http://image.tmdb.org/t/p/w500/" + profile_path).into(actorImage, new Callback() {
                    @Override
                    public void onSuccess() {
                        imageProgressBar.setVisibility(View.GONE);
                        scheduleStartPostponedTransition(actorImage);
                    }

                    @Override
                    public void onError() {
                        actorImage.setVisibility(View.GONE);
                        imageProgressBar.setVisibility(View.GONE);
                    }
                });

                image_path = profile_path;
            }*//*

            if(!name.equals("null")){
                actorName.setVisibility(View.VISIBLE);
                actorName.setText(name);
            }

            if(!homepage.equals("null")){
                tvhomepage.setVisibility(View.VISIBLE);
                tvhomepage.setText("Website: "+homepage);
            }

            if(!birthday.equals("null")){
                String bday[] = birthday.split("-");
                tvbirthDay.setVisibility(View.VISIBLE);
                tvbirthDay.setText("Birthday: "+bday[2]+"-"+bday[1]+"-"+bday[0]);
            }

            if(!place_of_birth.equals("null")){
                tvplaceOfBirth.setVisibility(View.VISIBLE);
                tvplaceOfBirth.setText("Place of Birth: "+place_of_birth);
            }

            if(!deathday.equals("null")){
                String dday[] = deathday.split("-");
                tvdeathDay.setVisibility(View.VISIBLE);
                tvdeathDay.setText("Died On: "+dday[2]+"-"+dday[1]+"-"+dday[0]);
            }

            if(!biography.equals("null")){
                biographyHeading.setVisibility(View.VISIBLE);
                tvbiography.setVisibility(View.VISIBLE);
                tvbiography.setText(biography);
            }
        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(ActorDetails.this, ImageViewer.class);
            intent.putExtra("Image_Path", image_path);
            startActivity(intent);
        }
    }*/






    private void scheduleStartPostponedTransition(final View sharedElement) {
        sharedElement.getViewTreeObserver().addOnPreDrawListener(
                new ViewTreeObserver.OnPreDrawListener() {
                    @Override
                    public boolean onPreDraw() {
                        sharedElement.getViewTreeObserver().removeOnPreDrawListener(this);
                        supportStartPostponedEnterTransition();
                        return true;
                    }
                });
    }

}
