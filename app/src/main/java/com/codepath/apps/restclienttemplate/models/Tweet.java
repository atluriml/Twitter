package com.codepath.apps.restclienttemplate.models;

import android.content.Entity;
import android.text.format.DateUtils;
import android.util.Log;

import com.bumptech.glide.Glide;
import com.codepath.apps.restclienttemplate.TwitterClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

import java.security.PublicKey;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Parcel
public class Tweet {

    public static final String TAG = "Tweet Class";
    public String body;
    public String createdAt;
    public User user;
    public String tweet_URL;
    public String retweets;
    public String likes;
    public String quoteTweets;
    public String replies;
    public boolean isLiked;
    public boolean isRetweeted;
    public long id;

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
        if(jsonObject.has("quote_count")) {
            tweet.quoteTweets = jsonObject.getString("quote_count");
        }
        if (jsonObject.has("favorite_count")){
            tweet.likes = jsonObject.getString("favorite_count");

        }
        if (jsonObject.has("retweet_count")) {
            tweet.retweets = jsonObject.getString("retweet_count");
        }
        if (jsonObject.has("reply_count")){
            tweet.replies = jsonObject.getString("reply_count");
        }
        if (!(jsonObject.getJSONObject("entities").has("media"))){
            Log.d("TWEET", "No pic");
            tweet.tweet_URL = "none";
        }
        else {
            Log.d("TWEET HAS PIC", jsonObject.getJSONObject("entities").getJSONArray("media").getJSONObject(0).getString("media_url"));
            tweet.tweet_URL = jsonObject.getJSONObject("entities").getJSONArray("media").getJSONObject(0).getString("media_url");
        }
        tweet.isLiked = jsonObject.getBoolean("favorited");
        tweet.id = jsonObject.getLong("id");
        tweet.isRetweeted = jsonObject.getBoolean("retweeted");
        return tweet;
    }

    public static List<Tweet> fromJsonArray(JSONArray jsonArray) throws JSONException {
        List<Tweet> tweets = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++){
            tweets.add(fromJson(jsonArray.getJSONObject(i)));
        }
        return tweets;
    }

    public String getRelativeTimeAgo(String rawJsonDate) throws ParseException {
        String twitterFormat = "EEE MMM dd HH:mm:ss ZZZZZ yyyy";
        SimpleDateFormat sf = new SimpleDateFormat(twitterFormat, Locale.ENGLISH);
        sf.setLenient(true);

        String relativeDate = "";
        try {
            long dateMillis = sf.parse(rawJsonDate).getTime();
            relativeDate = DateUtils.getRelativeTimeSpanString(dateMillis,
                    System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS).toString();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return relativeDate;
    }

    public String getBody() {
        return body;
    }

    public String getCreatedAt() {
        return createdAt;
    }


    public String getTweet_URL() {
        return tweet_URL;
    }

    public String getLikes() {
        return likes;
    }

    public String getQuoteTweets() {
        return quoteTweets;
    }

    public String getRetweets() {
        return retweets;
    }

    public User getUser() {
        return user;
    }

    public Boolean changeLikedStatus() {
        isLiked = !isLiked;
        return isLiked;
    }

    public Boolean changeRetweetStatus() {
        isRetweeted = !isRetweeted;
        return isRetweeted;
    }

    public long getId() {
        return id;
    }

    public String getReplies() {
        return replies;
    }
}
