package stohio.webmshuffle;


import android.content.Context;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Blaise on 12/31/2015.
 */
public class ChanScraper {
    //Gets data from 4chan formatted JSONs
    //Data is assumed to be formatted as per these guidelines:
    //https://github.com/4chan/4chan-API
    //Should be able to give an ArrayList of ChanPost objects
    // ^_^
    String chanURL;
    RequestQueue queue;
    Context context;
    String content;
    Boolean requestComplete;
    ArrayList<ChanPost> cp;

    public ChanScraper(String URL, Context c) {
        this.chanURL = URL;
        this.context = c;
        queue = Volley.newRequestQueue(context);

        //Using volley to get the JSON for the wsg board
        StringRequest jsonArrayRequest = new StringRequest(
                chanURL, new Response.Listener<String>() {


            @Override
            public void onResponse(String response) {
                content = response;
                requestComplete = true;
                System.out.println("Check it!" + content);
            }

        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                // TODO Auto-generated method stub
                error.printStackTrace();

            }
        });
        queue.add(jsonArrayRequest);
    }



    public ArrayList<ChanPost> getList(){
        JSONArray ja;
        ArrayList<ChanPost> list = new ArrayList<>();
        try {
            ja = new JSONArray(content);
            for(int i = 0; i < ja.length(); i++){
                JSONArray threads = ja.getJSONObject(i).getJSONArray("threads");
                for(int j = 0; j < threads.length(); j++){
                    System.out.println("Looking at page " + i + " thread " + j);
                    list.add(new ChanPost(threads.getJSONObject(j)));
                }

            }
        }catch(JSONException e){
            e.printStackTrace();
        }
        return list;
    }


}
