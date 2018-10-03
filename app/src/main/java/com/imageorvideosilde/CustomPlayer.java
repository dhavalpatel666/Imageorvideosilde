package com.imageorvideosilde;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import android.widget.Toast;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

import static com.google.android.youtube.player.YouTubePlayer.ErrorReason;
import static com.google.android.youtube.player.YouTubePlayer.OnInitializedListener;
import static com.google.android.youtube.player.YouTubePlayer.PlaybackEventListener;
import static com.google.android.youtube.player.YouTubePlayer.PlayerStateChangeListener;
import static com.google.android.youtube.player.YouTubePlayer.PlayerStyle;
import static com.google.android.youtube.player.YouTubePlayer.Provider;

public class CustomPlayer extends YouTubeBaseActivity implements OnInitializedListener {
    AutoScrollViewPager viewPager;
    MyCustomPagerAdapter myCustomPagerAdapter;
    JSONArray array;
    private ArrayList<bennerlistdat> bennerlistdats;
    private YouTubePlayer mPlayer;
    String VIDEO_ID;

    private View mPlayButtonLayout;
    private TextView mPlayTimeTextView;

    private Handler mHandler = null;
    private SeekBar mSeekBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        viewPager = (AutoScrollViewPager) findViewById(R.id.viewpager);
        bennerlistdats = new ArrayList<>();
        getimageloader();
    }

    public void getimageloader() {
        final ProgressDialog progress = new ProgressDialog(this);
        progress.setMessage("Loading..");
        progress.show();
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.getCache().clear();

        String url = "http://www.json-generator.com/api/json/get/bTOomIWJbC?indent=2";
        // String url = "http://www.json-generator.com/api/json/get/cfZNoXtkVu?indent=2";
        //arraylist1 = new ArrayList<HashMap<String, String>>();
        //Creating a json array request to get the json from our api
        // Log.e("URL", "URL" + url);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        progress.dismiss();
                        try {
                            array = response.getJSONArray("DATA");
                            for (int i = 0; i < array.length(); i++) {
                                JSONObject c = array.getJSONObject(i);
                                bennerlistdat videosge = new bennerlistdat();
                                String urur = c.getString("url");
                                String checkidid = c.getString("checkit");
                                videosge.setUrrl(urur);
                                videosge.setEvenid(checkidid);
                                bennerlistdats.add(videosge);
                            }
                            myCustomPagerAdapter = new MyCustomPagerAdapter(CustomPlayer.this, bennerlistdats);
                            viewPager.setAdapter(myCustomPagerAdapter);


                        } catch (JSONException e) {
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }
                }
        );
        queue.getCache().clear();
        //Creating a request queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        //Adding our request to the queue
        requestQueue.add(jsonObjectRequest);
    }

    public class MyCustomPagerAdapter extends PagerAdapter {
        Context context;
        // int images[];

        ArrayList<String> imagezz1;
        //  ArrayList<String> imagezz2;
        LayoutInflater layoutInflater;
        private List<bennerlistdat> musicsList;

        public MyCustomPagerAdapter(Context context, List<bennerlistdat> musicslist) {
            this.context = context;
            //   this.images = images;
            // imagezz1 = arraylist;
            this.musicsList = musicslist;
            // imagezz2 = arraylist1;
            layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        }

        @Override
        public int getCount() {
            return musicsList.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == ((LinearLayout) object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, final int position) {
            View itemView = layoutInflater.inflate(R.layout.cell, container, false);

            ImageView imageView = (ImageView) itemView.findViewById(R.id._image);

            YouTubePlayerView playerView = (YouTubePlayerView)itemView.findViewById(R.id.youTubePlayerView);

            // TextView textop =(TextView)itemView.findViewById(R.id.textop);
            //imageView.setImageResource(Integer.parseInt(imagezz1.get(position)));


            ImageButton play_video =  (ImageButton)itemView.findViewById(R.id.play_video);
            ImageButton pause_video =  (ImageButton)itemView.findViewById(R.id.pause_video);
            LinearLayout video_control = (LinearLayout)itemView.findViewById(R.id.video_control);

            mPlayTimeTextView = (TextView)itemView.findViewById(R.id.play_time);
            mSeekBar = (SeekBar)itemView.findViewById(R.id.video_seekbar);
            mSeekBar.setOnSeekBarChangeListener(mVideoSeekBarChangeListener);



            bennerlistdat musics = musicsList.get(position);




            if (musics.getEvenid().equalsIgnoreCase("yes")) {
                imageView.setVisibility(View.VISIBLE);
                playerView.setVisibility(View.GONE);
                video_control.setVisibility(View.GONE);
                Glide.with(context)
                        .load(musics.getUrrl())
                        .into(imageView);
            } else if (musics.getEvenid().equalsIgnoreCase("no")) {
                playerView.initialize(getString(R.string.DEVELOPER_KEY), CustomPlayer.this);
                playerView.setVisibility(View.VISIBLE);
                video_control.setVisibility(View.VISIBLE);
                imageView.setVisibility(View.GONE);
                VIDEO_ID = musics.getUrrl();
                mHandler = new Handler();
            }

            play_video.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    forpaly();
                }
            });
            pause_video.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    forpush();
                }
            });

            container.addView(itemView);


            return itemView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((LinearLayout) object);
        }
    }

    @Override
    public void onInitializationFailure(Provider provider, YouTubeInitializationResult result) {
        Toast.makeText(this, "Failed to initialize.", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onInitializationSuccess(Provider provider, YouTubePlayer player, boolean wasRestored) {
        if (null == player) return;
        mPlayer = player;

        displayCurrentTime();

        // Start buffering
        if (!wasRestored) {
            player.cueVideo(VIDEO_ID);
        }

        player.setPlayerStyle(PlayerStyle.CHROMELESS);

        // Add listeners to YouTubePlayer instance
        player.setPlayerStateChangeListener(mPlayerStateChangeListener);
        player.setPlaybackEventListener(mPlaybackEventListener);
    }


    PlaybackEventListener mPlaybackEventListener = new PlaybackEventListener() {
        @Override
        public void onBuffering(boolean arg0) {
        }

        @Override
        public void onPaused() {
            mHandler.removeCallbacks(runnable);
        }

        @Override
        public void onPlaying() {
            mHandler.postDelayed(runnable, 100);
            displayCurrentTime();
        }

        @Override
        public void onSeekTo(int arg0) {
            mHandler.postDelayed(runnable, 100);
        }

        @Override
        public void onStopped() {
            mHandler.removeCallbacks(runnable);
        }
    };

    PlayerStateChangeListener mPlayerStateChangeListener = new PlayerStateChangeListener() {
        @Override
        public void onAdStarted() {
        }

        @Override
        public void onError(ErrorReason arg0) {
        }

        @Override
        public void onLoaded(String arg0) {
        }

        @Override
        public void onLoading() {
        }

        @Override
        public void onVideoEnded() {
        }

        @Override
        public void onVideoStarted() {
            displayCurrentTime();
        }
    };

    SeekBar.OnSeekBarChangeListener mVideoSeekBarChangeListener = new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            long lengthPlayed = (mPlayer.getDurationMillis() * progress) / 100;
            mPlayer.seekToMillis((int) lengthPlayed);
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {

        }
    };



    private void displayCurrentTime() {
        if (null == mPlayer) return;
        String formattedTime = formatTime(mPlayer.getDurationMillis() - mPlayer.getCurrentTimeMillis());
        mPlayTimeTextView.setText(formattedTime);
    }

    private String formatTime(int millis) {
        int seconds = millis / 1000;
        int minutes = seconds / 60;
        int hours = minutes / 60;

        return (hours == 0 ? "--:" : hours + ":") + String.format("%02d:%02d", minutes % 60, seconds % 60);
    }


    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            displayCurrentTime();
            mHandler.postDelayed(this, 100);
        }
    };

    void forpaly(){
        if (null != mPlayer && !mPlayer.isPlaying())
            mPlayer.play();
    }
    void forpush(){
        if (null != mPlayer && mPlayer.isPlaying())
            mPlayer.pause();
    }
}

