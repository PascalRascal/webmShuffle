package stohio.webmshuffle;

import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.StrikethroughSpan;
import android.text.style.StyleSpan;
import android.text.style.TypefaceSpan;
import android.text.style.UnderlineSpan;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Blaise on 12/31/2015.
 */
public class ChanPost {
    String comm, ext, filename, md5, name, now, semantic_url, sub, tim, time, number;
    int tn_h, tn_w, w, h, fsize, images, replies,resto;
    JSONObject original;
    Boolean selected;
    Bitmap thumb;

    public ChanPost(JSONObject jo) {
        //Made every entry it's own try loop, there should be a better way of doing this
        //Look into doing this a better way
        try {
            comm = jo.getString("com");
        } catch (JSONException e) {
        }
        try {
            ext = jo.getString("ext");
        } catch (JSONException e) {
        }
        try{
            number = jo.getString("no");
        }catch(JSONException e){
        }
        try {
            filename = jo.getString("filename");
        } catch (JSONException e) {
        }
        try {
            md5 = jo.getString("md5");
        } catch (JSONException e) {

        }
        try {
            name = jo.getString("name");
        } catch (JSONException e) {

        }
        try {
            now = jo.getString("now");
        } catch (JSONException e) {

        }
        try {
            semantic_url = jo.getString("semantic_url");
        } catch (JSONException e) {

        }
        try {
            sub = jo.getString("sub");
        } catch (JSONException e) {

        }
        try {
            tim = jo.getString("tim");
        } catch (JSONException e) {

        }
        try {
            time = jo.getString("time");
        } catch (JSONException e) {

        }
        try {
            tn_h = jo.getInt("tn_h");
        } catch (JSONException e) {

        }
        try {
            tn_w = jo.getInt("tn_w");
        } catch (JSONException e) {

        }
        try {
            w = jo.getInt("w");
        } catch (JSONException e) {

        }
        try {
            h = jo.getInt("h");
        } catch (JSONException e) {

        }
        try {
            fsize = jo.getInt("fsize");
        } catch (JSONException e) {

        }
        try {
            images = jo.getInt("images");
        } catch (JSONException e) {

        }

        try {
            resto = jo.getInt("resto");
        } catch (JSONException e) {
        }
        selected = false;

        if (this.comm != null) {
            String comment = comm.replace("<wbr>", "");
            comm = Jsoup.parseBodyFragment(comment).text();
        }
        if (this.sub != null){
            sub = sub.replace("<wbr>","");
            sub = Jsoup.parseBodyFragment(sub).text();
        }
        if(this.filename != null){
            filename = Jsoup.parseBodyFragment(filename).text();
        }
        selected = false;

    }

    public String getThumbnailUrl() {
        return "http://www.i.4cdn.org/wsg/" + this.tim + "s.jpg";
    }

    public String getContentUrl() {
        return "http://i.4cdn.org/wsg/" + tim + ext;
    }

    public String getThreadUrl() {
        return "http://a.4cdn.org/wsg/thread/" + number + ".json";
    }

    public void setSelected() {
        if (selected == false) {
            selected = true;
        } else {
            selected = false;
        }
    }
}