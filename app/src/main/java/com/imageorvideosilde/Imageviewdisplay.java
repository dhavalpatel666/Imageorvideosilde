package com.imageorvideosilde;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.provider.SyncStateContract;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

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
import com.google.android.youtube.player.YouTubeThumbnailLoader;
import com.google.android.youtube.player.YouTubeThumbnailView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.hdodenhof.circleimageview.CircleImageView;

public class Imageviewdisplay extends YouTubeBaseActivity implements YouTubePlayer.OnInitializedListener {
    JSONArray array;
    private ArrayList<bennerlistdat> bennerlistdats;
    private YouTubePlayer mPlayer;
    private RecyclerView recyclerView;
    String mVideoId;
    YouTubePlayerView playerView;
    private TouchImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_imageviewdisplay);
        bennerlistdats = new ArrayList<>();
        getimageloader();
        setUpRecyclerView();
        playerView = (YouTubePlayerView) findViewById(R.id.youTubePlayerView);
        imageView = (TouchImageView) findViewById(R.id.imageview);
        imageView.setOnTouchImageViewListener(new TouchImageView.OnTouchImageViewListener() {
            @Override
            public void onMove() {
                imageView.setZoom(imageView);
            }
        });
    }

    private void setUpRecyclerView() {
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);

        //Horizontal direction recycler view
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
    }

    public void getimageloader() {
        final ProgressDialog progress = new ProgressDialog(this);
        progress.setMessage("Loading..");
        progress.show();
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.getCache().clear();

        String url = "http://www.json-generator.com/api/json/get/cqCFjmgRYO?indent=2";
        // http://www.json-generator.com/api/json/get/bVkDnrgtnS?indent=2
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
                            JSONObject c1 = array.getJSONObject(0);
                            String respro = c1.getString("url");


                            for (int i = 0; i < array.length(); i++) {
                                JSONObject c = array.getJSONObject(i);
                                bennerlistdat videosge = new bennerlistdat();
                                String urur = c.getString("url");
                                String checkidid = c.getString("checkit");
                                String videod = c.getString("video");
                                videosge.setUrrl(urur);
                                videosge.setEvenid(checkidid);
                                videosge.setName(videod);
                                bennerlistdats.add(videosge);
                            }
                            populateRecyclerView();
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


    private void populateRecyclerView() {

        final YoutubeVideoAdapter adapter = new YoutubeVideoAdapter(Imageviewdisplay.this, bennerlistdats);
        recyclerView.setAdapter(adapter);
        adapter.setSelectedPosition(0);

        //set click event
        recyclerView.addOnItemTouchListener(new RecyclerViewOnClickListener(this, new RecyclerViewOnClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                bennerlistdat musics = bennerlistdats.get(position);

                if (musics.getEvenid().equalsIgnoreCase("yes")) {
                    adapter.setSelectedPosition(position);
                } else {
                    adapter.setSelectedPosition(position);
                    Log.e("mVideolink", "mVideolin-->" + musics.getUrrl());
                    playerView.initialize(getString(R.string.DEVELOPER_KEY), Imageviewdisplay.this);
                    mVideoId = musics.getName();
                    // mPlayer.cueVideo(mVideoId);
                }

            }
        }));
    }

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean restored) {
       // youTubePlayer.setFullscreenControlFlags(YouTubePlayer.FULLSCREEN_FLAG_CONTROL_ORIENTATION);
      //  youTubePlayer.addFullscreenControlFlag(YouTubePlayer.FULLSCREEN_FLAG_ALWAYS_FULLSCREEN_IN_LANDSCAPE);
    //    youTubePlayer.addFullscreenControlFlag(YouTubePlayer.FULLSCREEN_FLAG_CONTROL_SYSTEM_UI);
        youTubePlayer.setFullscreen(false);
        youTubePlayer.setShowFullscreenButton(false);
        mPlayer = youTubePlayer;
        if (mVideoId != null) {
            if (!restored) {
                mPlayer.play();
                mPlayer.setPlayerStyle(YouTubePlayer.PlayerStyle.DEFAULT);
                mPlayer.cueVideo(mVideoId);
            }
        }
    }

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {

        if (youTubeInitializationResult.isUserRecoverableError()) {
        } else {
        }
    }


    public class YoutubeVideoAdapter extends RecyclerView.Adapter<YoutubeViewHolder> {
        private final String TAG = YoutubeVideoAdapter.class.getSimpleName();
        private Context context;


        //position to check which position is selected
        private int selectedPosition = 0;
        private List<bennerlistdat> musicsList;

        public YoutubeVideoAdapter(Context context, List<bennerlistdat> musicslist) {
            this.context = context;
            // this.youtubeVideoModelArrayList = youtubeVideoModelArrayList;

            this.musicsList = musicslist;
        }

        @Override
        public YoutubeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
            View view = layoutInflater.inflate(R.layout.youtube_video_custom_layout, parent, false);
            return new YoutubeViewHolder(view);
        }

        @SuppressLint("StringFormatInvalid")
        @Override
        public void onBindViewHolder(YoutubeViewHolder holder, final int position) {
            bennerlistdat musics = musicsList.get(position);
            //if selected position is equal to that mean view is selected so change the cardview color
            if (selectedPosition == position) {
                holder.circleImageView.setBorderColor(Color.parseColor("#FF4081"));
                if (musics.getEvenid().equalsIgnoreCase("yes")) {

                    Glide.with(Imageviewdisplay.this)
                            .load(musics.getUrrl())
                            .placeholder(R.drawable.gififi)
                            .into(imageView);
                    imageView.setVisibility(View.VISIBLE);
                    playerView.setVisibility(View.GONE);
                    Log.e("Image", "Image-->" + "Ture");
                    if (mVideoId != null) {
                        mPlayer.pause();
                    }

                } else {
                    Log.e("Image", "Image-->" + "False");
                    imageView.setVisibility(View.GONE);
                    playerView.setVisibility(View.VISIBLE);

                }
            } else {
                holder.circleImageView.setBorderColor(Color.parseColor("#303F9F"));
            }
            holder.circleImageView.setVisibility(View.VISIBLE);
            Glide.with(Imageviewdisplay.this)
                    .load(musics.getUrrl())
                    .placeholder(R.drawable.gififi)
                    .into(holder.circleImageView);

           /* if (musics.getEvenid().equalsIgnoreCase("yes")) {
                holder.circleImageView.setVisibility(View.VISIBLE);
             //   holder.videoThumbnailImageView.setVisibility(View.GONE);
                Glide.with(Imageviewdisplay.this)
                        .load(musics.getUrrl())
                        .placeholder(R.drawable.gififi)
                        .into(holder.circleImageView);
            } else {
                holder.circleImageView.setVisibility(View.GONE);
              //  holder.videoThumbnailImageView.setVisibility(View.VISIBLE);
            }*/


            /*  initialize the thumbnail image view , we need to pass Developer Key */
            /*  initialize the thumbnail image view , we need to pass Developer Key */
        /*    holder.videoThumbnailImageView.initialize(Constants.DEVELOPER_KEY, new YouTubeThumbnailView.OnInitializedListener() {
                @Override
                public void onInitializationSuccess(YouTubeThumbnailView youTubeThumbnailView, final YouTubeThumbnailLoader youTubeThumbnailLoader) {

                    bennerlistdat musics = musicsList.get(position);

                    mVideoId = musics.getUrrl();

                    youTubeThumbnailLoader.setVideo(mVideoId);

                    youTubeThumbnailLoader.setOnThumbnailLoadedListener(new YouTubeThumbnailLoader.OnThumbnailLoadedListener() {
                        @Override
                        public void onThumbnailLoaded(YouTubeThumbnailView youTubeThumbnailView, String s) {
                            //when thumbnail loaded successfully release the thumbnail loader as we are showing thumbnail in adapter
                            youTubeThumbnailLoader.release();
                        }

                        @Override
                        public void onThumbnailError(YouTubeThumbnailView youTubeThumbnailView, YouTubeThumbnailLoader.ErrorReason errorReason) {
                            //print or show error when thumbnail load failed
                            Log.e(TAG, "Youtube Thumbnail Error");
                        }
                    });
                }

                @Override
                public void onInitializationFailure(YouTubeThumbnailView youTubeThumbnailView, YouTubeInitializationResult youTubeInitializationResult) {
                    //print or show error when initialization failed
                    Log.e(TAG, "Youtube Initialization Failure");

                }
            });
*/
        }

        @Override
        public int getItemCount() {
            return musicsList != null ? musicsList.size() : 0;
        }

        /**
         * method the change the selected position when item clicked
         *
         * @param selectedPosition
         */
        public void setSelectedPosition(int selectedPosition) {
            this.selectedPosition = selectedPosition;
            //when item selected notify the adapter
            notifyDataSetChanged();
        }
    }

    public class YoutubeViewHolder extends RecyclerView.ViewHolder {
        // public YouTubeThumbnailView videoThumbnailImageView;
        public CardView youtubeCardView;
        public TextView listtirel;
        private CircleImageView circleImageView;

        public YoutubeViewHolder(View itemView) {
            super(itemView);
            //   videoThumbnailImageView = itemView.findViewById(R.id.video_thumbnail_image_view);
            circleImageView = itemView.findViewById(R.id.imageview);

        }
    }


    public static class RecyclerViewOnClickListener implements RecyclerView.OnItemTouchListener {

        private OnItemClickListener mListener;
        private GestureDetector mGestureDetector;

        public interface OnItemClickListener {
            public void onItemClick(View view, int position);
        }

        public RecyclerViewOnClickListener(Context context, OnItemClickListener listener) {
            mListener = listener;
            mGestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }
            });
        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView view, MotionEvent e) {
            View childView = view.findChildViewUnder(e.getX(), e.getY());
            if (childView != null && mListener != null && mGestureDetector.onTouchEvent(e)) {
                mListener.onItemClick(childView, view.getChildPosition(childView));
                return true;
            }
            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView view, MotionEvent motionEvent) {
        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

        }

    }


}
