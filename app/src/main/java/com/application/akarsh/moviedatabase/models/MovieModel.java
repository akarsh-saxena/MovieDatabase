package com.application.akarsh.moviedatabase.models;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;
import java.util.Locale;

public class MovieModel {                        //"results"

    private String title;
    private String story;
    private String release_date;
    private long id;
    private String posterPath = null;
    private String backdropImage;
    private float vote_average;
    private String vote_count;
    private Double popularity;
    byte[] image;

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public String getBackdropImage() {
        return backdropImage;
    }

    public void setBackdropImage(String backdropImage) {
        this.backdropImage = backdropImage;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public String getRelease_date() {
        return release_date;
    }

    public void setRelease_date(String release_date) {
        this.release_date = release_date;
    }

    public String getStory() {
        return story;
    }

    public void setStory(String story) {
        this.story = story;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public float getVote_average() {
        return vote_average;
    }

    public void setVote_average(float vote_average) {
        this.vote_average = vote_average;
    }

    public String getVote_count() {
        return vote_count;
    }

    public void setVote_count(String vote_count) {
        this.vote_count = vote_count;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    private Double getPopularity() {
        return popularity;
    }

    public void setPopularity(Double popularity) {
        this.popularity = popularity;
    }
    public static Comparator<MovieModel> MoviePopularityComparator = new Comparator<MovieModel>() {

        @Override
        public int compare(MovieModel m1, MovieModel m2) {

            Double m1popularity = m1.getPopularity();
            Double m2popularity = m2.getPopularity();

            if(m1popularity==0 && m2popularity==0)
                return 0;
            else
            if(m1popularity == 0)
                return 1;
            else
            if(m2popularity == 0)
                return -1;

            return m2popularity.compareTo(m1popularity);
        }
    };

    public static Comparator<MovieModel> MovieRatingComparator = new Comparator<MovieModel>() {
        @Override
        public int compare(MovieModel m1, MovieModel m2) {

            Float m1rating = m1.getVote_average();
            Float m2rating = m2.getVote_average();

            if(m1rating==0 && m2rating==0)
                return 0;
            else
                if(m1rating == 0)
                    return 1;
                else
                    if(m2rating == 0)
                    return -1;

            return m2rating.compareTo(m1rating);
        }
    };

    public static Comparator<MovieModel> MovieDateComparatorNewest = new Comparator<MovieModel>() {
        @Override
        public int compare(MovieModel m1, MovieModel m2) {

            String m1releasedate = m1.getRelease_date();
            String m2releasedate = m2.getRelease_date();

            if(m1releasedate.equals("") && m2releasedate.equals(""))
                return 0;
            else
                if(m1releasedate.equals("") && !m2releasedate.equals(""))
                    return 1;
                else
                    if(!m1releasedate.equals("") && m2releasedate.equals(""))
                        return -1;

            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            Date date1 = null, date2 = null;
            try {
                date1 = simpleDateFormat.parse(m1releasedate);
                date2 = simpleDateFormat.parse(m2releasedate);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            return date2.compareTo(date1);
        }
    };

    public static Comparator<MovieModel> MovieDateComparatorOldest = new Comparator<MovieModel>() {
        @Override
        public int compare(MovieModel m1, MovieModel m2) {

            String m1releasedate = m1.getRelease_date();
            String m2releasedate = m2.getRelease_date();

            if(m1releasedate.equals("") && m2releasedate.equals(""))
                return 0;
            else
            if(m1releasedate.equals("") && !m2releasedate.equals(""))
                return 1;
            else
            if(!m1releasedate.equals("") && m2releasedate.equals(""))
                return -1;

            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            Date date1 = null, date2 = null;
            try {
                date1 = simpleDateFormat.parse(m1releasedate);
                date2 = simpleDateFormat.parse(m2releasedate);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            return date1.compareTo(date2);
        }
    };
}


