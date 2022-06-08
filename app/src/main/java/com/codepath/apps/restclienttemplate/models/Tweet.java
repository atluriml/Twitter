package com.codepath.apps.restclienttemplate.models;

import android.content.Entity;
import android.util.Log;

import com.bumptech.glide.Glide;
import com.codepath.apps.restclienttemplate.TwitterClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

import java.security.PublicKey;
import java.util.ArrayList;
import java.util.List;

@Parcel
public class Tweet {

    public String body;
    public String createdAt;
    public User user;
    public String tweet_URL;

    // empty constructor needed by Parceler library
    public Tweet() {}

    public static Tweet fromJson(JSONObject jsonObject) throws JSONException {
        Tweet tweet = new Tweet();
        if(jsonObject.has("full_text")) {
            tweet.body = jsonObject.getString("full_text");
        } else {
            tweet.body = jsonObject.getString("text");
        }
        tweet.createdAt = jsonObject.getString("created_at");
        tweet.user = User.fromJson(jsonObject.getJSONObject("user"));

        if (!(jsonObject.getJSONObject("entities").has("media"))){
            Log.d("TWEET", "No pic");
            tweet.tweet_URL = "none";
        }
        else {
            Log.d("TWEET HAS PIC", jsonObject.getJSONObject("entities").getJSONArray("media").getJSONObject(0).getString("media_url"));
            tweet.tweet_URL = jsonObject.getJSONObject("entities").getJSONArray("media").getJSONObject(0).getString("media_url");
        }
        return tweet;
    }

    public static List<Tweet> fromJsonArray(JSONArray jsonArray) throws JSONException {
        List<Tweet> tweets = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++){
            tweets.add(fromJson(jsonArray.getJSONObject(i)));
        }
        return tweets;
    }
}
