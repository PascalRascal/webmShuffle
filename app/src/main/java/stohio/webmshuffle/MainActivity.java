package stohio.webmshuffle;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.StateListDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.melnykov.fab.FloatingActionButton;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    String chanURL;
    RequestQueue queue;
    ArrayList<ChanPost> cp;
    int selectedItem;
    ListView gv;
    JSONArray ja = new JSONArray();
    ArrayList<ChanPost> postsSelected;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Boolean[] checkList = new Boolean[151];
        for(int i = 0; i < checkList.length; i++){
            checkList[i] = false;
        }

        //Sets up singleton Imageloader instance
        //Are singleton things good or bad? /g/ told me they were bad but
        // g also hates java which is what im progamming in so wHO CARES
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this).build();
        ImageLoader.getInstance().init(config);

        postsSelected = new ArrayList<>();


        setContentView(R.layout.activity_main);

        final Context c = this;
        gv = (ListView) findViewById(R.id.gridView);
        final ChanAdapter ca = new ChanAdapter(this.getBaseContext(),R.id.gridView,cp);

        gv.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);



        FloatingActionButton createPlaylist = (FloatingActionButton)findViewById(R.id.fab);
        createPlaylist.attachToListView(gv);

        final ChanScraper cs = new ChanScraper("http://a.4cdn.org/wsg/catalog.json", getBaseContext());

        cs.queue.addRequestFinishedListener(new RequestQueue.RequestFinishedListener<String>() {
            @Override
            public void onRequestFinished(Request<String> request) {
               cp = cs.getList();
                System.out.println(cp.get(25).comm);
                System.out.println(cp.size());
                final ChanAdapter ca = new ChanAdapter(c,R.id.gridView,cp);
                gv.setOnItemClickListener(webmClicker);


                gv.setAdapter(ca);

            }
        });
        gv.setOnItemClickListener(webmClicker);
        createPlaylist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ja = new JSONArray();
                int j = 0;
                for (int i = 0; i < cp.size(); i++) {
                    if (cp.get(i).selected) {
                        try {
                            ja.put(j, cp.get(i).getThreadUrl());
                            System.out.println(ja.get(j));
                            j++;
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
                for (int i = 0; i < postsSelected.size(); i++) {
                    System.out.println(postsSelected.get(i).getContentUrl());
                }

                Intent myIntent = new Intent(getBaseContext(), WebmDeck.class);
                System.out.println("This is the data im putitng into the intent: " + ja.toString());
                myIntent.putExtra("data", ja.toString());
                startActivity(myIntent);


            }
        });


    }

    AdapterView.OnItemClickListener webmClicker = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            //Prevents the user from accidentally selecting a null view, resulting in a crash
            //Also stops users from selecting the sticky

                if (cp.get(position).selected == true) {
                    cp.get(position).selected = false;
                    System.out.println("This item was unselected! " + position);
                } else {
                    cp.get(position).selected = true;
                    System.out.println("This item is selected! " + position);
                }
            }

    };

}
