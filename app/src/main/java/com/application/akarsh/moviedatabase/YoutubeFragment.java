package com.application.akarsh.moviedatabase;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerSupportFragment;


public class YoutubeFragment extends Fragment {
    private static final String API_KEY = "AIzaSyBaIgqSaeySzd7BcHnZbal7P8RldBLv2tE";


    public static YoutubeFragment newInstance(String url) {
        YoutubeFragment youtubeFragment = new YoutubeFragment();

        Bundle args = new Bundle();
        args.putString("url", url);
        youtubeFragment.setArguments(args);

        return youtubeFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_youtube, container, false);

        YouTubePlayerSupportFragment youTubePlayerFragment = YouTubePlayerSupportFragment.newInstance();

        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.add(R.id.youtube_layout, youTubePlayerFragment).commit();


        final String VIDEO_ID = getArguments().get("url").toString();

        youTubePlayerFragment.initialize(API_KEY, new YouTubePlayer.OnInitializedListener() {

            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer player, boolean wasRestored) {
                if (!wasRestored) {
                    player.setPlayerStyle(YouTubePlayer.PlayerStyle.DEFAULT);
                    player.cueVideo(VIDEO_ID);
                }
            }

            @Override
            public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult error) {
                // YouTube error
                String errorMessage = error.toString();
                if(errorMessage.equals("SERVICE_MISSING"))
                    Toast.makeText(getActivity(), "Install/Update Official Youtube App", Toast.LENGTH_LONG).show();
                else
                    Toast.makeText(getActivity(), errorMessage, Toast.LENGTH_LONG).show();

                Log.d("errorMessage:", errorMessage);
            }
        });

        return rootView;
    }
}