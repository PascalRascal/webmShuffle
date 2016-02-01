package stohio.webmshuffle;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.SystemClock;
import android.support.v4.os.AsyncTaskCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.daprlabs.cardstack.SwipeDeck;
import com.devbrackets.android.exomedia.EMVideoView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class WebmDeck extends AppCompatActivity {
    Intent intent;
    JSONArray ja;
    ArrayList<ChanPost> webmList;
    RequestQueue queue;
    int totalThreads;
    private MediaPlayer webmPlayer;
    int threadCounter = 0;

    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        webmList = new ArrayList<ChanPost>();
        setContentView(R.layout.activity_webm_deck);
        final Context c = this;

        setupData();
        queue.addRequestFinishedListener(new RequestQueue.RequestFinishedListener<String>() {
            @Override
            public void onRequestFinished(Request<String> request) {
                threadCounter++;
                System.out.println("Request Finished! " + threadCounter + "/" + totalThreads + " threads loaded!");
                if (threadCounter == totalThreads) {
                    Collections.shuffle(webmList, new Random(System.nanoTime()));
                    setupWebmPlayer();
            }
        }

    });
    }

    private void setupWebmPlayer(){
        final VideoView webmView = (VideoView) findViewById(R.id.textureView);
        final WebmPosition wp = new WebmPosition();
        final TextView webmTitle = (TextView)findViewById(R.id.webmTitle);
        final MediaController webmController = new MediaController(this){
            @Override
            public void show(int timeout){
                if(timeout == 3000) timeout = 90000;
                super.show(timeout);
            }
        };
        webmController.setKeepScreenOn(true);
        webmController.setPrevNextListeners(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("Next!");
                int pos = wp.increasePosition();
                webmView.setVideoURI(Uri.parse(webmList.get(pos).getContentUrl()));
                webmTitle.setText(webmList.get(pos).filename);




            }
        }, new View.OnClickListener(){
            @Override
            public void onClick(View v){
                System.out.println("Back!");
                int pos = wp.decreasePosition();
                webmView.setVideoURI(Uri.parse(webmList.get(pos).getContentUrl()));
                webmTitle.setText(webmList.get(pos).filename);


            }

        });
        webmView.setMediaController(webmController);
        webmView.setVideoURI(Uri.parse(webmList.get(0).getContentUrl()));
        webmTitle.setText(webmList.get(0).filename);
        webmView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                webmController.show();
                webmView.start();
            }
        });
        webmView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                webmView.seekTo(0);
                webmView.resume();
            }
        });


    }


    //Iterates through a thread formatted JSON, creating a list of ChanPost objects that contain
    //the webm extensions
    private void addWebmPostsFromThreadJSON(String jsonString) throws JSONException{
        JSONObject jo = new JSONObject(jsonString);
        JSONArray posts = jo.getJSONArray("posts");

        for(int i = 0; i < posts.length();i++){
            ChanPost p = new ChanPost(posts.getJSONObject(i));
            if(p.ext != null) {
                if (p.ext.equalsIgnoreCase(".webm")) {
                    webmList.add(p);
                    System.out.println("Webm post added!");
                }
            }
        }

    }

    //Sets up the data to be sent through the volley queue
    private void setupData(){
        queue= Volley.newRequestQueue(getBaseContext());
        intent = getIntent();

        String data = intent.getStringExtra("data");
        System.out.println("This is the data stored in the intent: " + data);
        try {
            ja = new JSONArray(data);
        }catch(JSONException e){
            e.printStackTrace();
        }

        //This code looks redudant but it works, might remove later
        ArrayList<String> urls = new ArrayList<>();
        for(int i = 0; i < ja.length(); i++){
            try {
                urls.add(ja.getString(i));
            }catch(JSONException e){
                e.printStackTrace();
            }
        }
        totalThreads = urls.size();
        for(int i = 0; i < urls.size(); i++){
            StringRequest jsonArrayRequest = new StringRequest(
                    urls.get(i), new Response.Listener<String>() {


                @Override
                public void onResponse(String response) {
                    try{
                        System.out.println("Thread loaded!");
                        addWebmPostsFromThreadJSON(response);
                    }catch(JSONException e){

                    }

                }

            }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    // TODO Auto-generated method stub
                    error.printStackTrace();

                }
            });
            System.out.println("Sending request to " + urls.get(i));
            queue.add(jsonArrayRequest);
            SystemClock.sleep(5000);
            System.out.println("Request added to que!");


        }
    }

    Response.Listener<String> webmListener = new Response.Listener<String>() {


        @Override
        public void onResponse(String response) {
            try{
                System.out.println("Thread loaded!");
                addWebmPostsFromThreadJSON(response);
            }catch(JSONException e){

            }

        }

    };

}

class WebmPosition{
        int position;
        public WebmPosition(){
            position = 0;
        }
        public int increasePosition(){
            position++;
            return position;
        }
    public int decreasePosition(){
        position--;
        return position;
    }

}

