package com.imageorvideosilde;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
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

public class MainActivity extends YouTubeBaseActivity implements YouTubePlayer.OnInitializedListener {
    AutoScrollViewPager viewPager;
    MyCustomPagerAdapter myCustomPagerAdapter;
    JSONArray array;
    private ArrayList<bennerlistdat> bennerlistdats;
    private YouTubePlayer mPlayer;
    String videourl;

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
                            myCustomPagerAdapter = new MyCustomPagerAdapter(MainActivity.this, bennerlistdats);
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
            bennerlistdat musics = musicsList.get(position);

            if (musics.getEvenid().equalsIgnoreCase("yes")) {
                imageView.setVisibility(View.VISIBLE);
                playerView.setVisibility(View.GONE);
                Glide.with(context)
                        .load(musics.getUrrl())
                        .into(imageView);
            } else if (musics.getEvenid().equalsIgnoreCase("no")) {
                playerView.initialize(getString(R.string.DEVELOPER_KEY), MainActivity.this);
                playerView.setVisibility(View.VISIBLE);
                imageView.setVisibility(View.GONE);
                videourl = musics.getUrrl();
            }

            playerView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (musics.getEvenid().equalsIgnoreCase("no")){
                        Toast.makeText(getApplicationContext(),"Opne full src",Toast.LENGTH_LONG).show();
                    }
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
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean restored) {
        youTubePlayer.setFullscreenControlFlags(YouTubePlayer.FULLSCREEN_FLAG_CONTROL_ORIENTATION);
       // youTubePlayer.addFullscreenControlFlag(YouTubePlayer.FULLSCREEN_FLAG_ALWAYS_FULLSCREEN_IN_LANDSCAPE);
        youTubePlayer.addFullscreenControlFlag(YouTubePlayer.FULLSCREEN_FLAG_CONTROL_SYSTEM_UI);
        mPlayer = youTubePlayer;
        if (videourl != null) {
            if (!restored) {
                mPlayer.play();
                mPlayer.setPlayerStyle(YouTubePlayer.PlayerStyle.DEFAULT);
                mPlayer.cueVideo(videourl);
            }
        }
    }

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {

        if (youTubeInitializationResult.isUserRecoverableError()) {
        } else {
        }
    }

}
