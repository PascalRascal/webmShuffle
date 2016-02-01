package stohio.webmshuffle;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Blaise on 1/1/2016.
 */
public class ChanAdapter extends ArrayAdapter<ChanPost> {
    ArrayList<ChanPost> list;
    Context context;
    int resource;
    TextView title, comment, images;
    ImageView thumbnail;
    Boolean selected;
    ImageLoader imageLoader;

    public ChanAdapter(Context context, int resource, ArrayList<ChanPost> objects) {
        super(context, resource, objects);
        this.list = objects;
        this.context = context;
        this.resource = resource;
        imageLoader = ImageLoader.getInstance();
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final int temp = position;
        View child = convertView;
        LayoutInflater inflater = ((Activity) context).getLayoutInflater();
        child = inflater.inflate(R.layout.chan_oppost, parent, false);

        title = (TextView)child.findViewById(R.id.Title);
        comment = (TextView)child.findViewById(R.id.comment);
        thumbnail = (ImageView)child.findViewById(R.id.thumbnail);
        images = (TextView)child.findViewById(R.id.posts);

        title.setText(list.get(position).sub);
        comment.setText(list.get(position).comm);
        images.setText(list.get(position).images + " image replies");

        //Gets image from 4chan thumbnail
        if(list.get(temp).tim != null && list.get(temp).thumb == null) { //If there is an image associated with the post AND the thumb hasn't been loaded
            String imageURI = new String("http://i.4cdn.org/wsg/" + list.get(temp).tim + "s.jpg");
            imageLoader.loadImage(imageURI, new SimpleImageLoadingListener() {
                @Override
                public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                    thumbnail.setImageBitmap(loadedImage);
                    list.get(temp).thumb = loadedImage;
                }
            });
        }
        if(list.get(temp).thumb != null){
            thumbnail.setImageBitmap(list.get(temp).thumb);
        }

        return child;

    }
}
