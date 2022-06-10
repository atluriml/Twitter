package com.codepath.apps.restclienttemplate;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.codepath.apps.restclienttemplate.models.Tweet;

import org.parceler.Parcels;

import java.text.ParseException;

public class TweetDetailActivity extends AppCompatActivity {

    public static final String TAG = "TweetDetailActivity";
    Tweet tweet;
    TextView tvDetailUser;
    TextView tvDetailBody;
    TextView tvDetailTimeStamp;
    TextView tvDetailName;
    TextView tvNumRetweets;
    TextView tvNumLikes;
    TextView tvNumQuotes;
    ImageView ivProfileDetail;
    ImageView ivDetailMedia;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tweet_detail);

        tvDetailBody = findViewById(R.id.tvDetailBody);
        tvDetailName = findViewById(R.id.tvDetailName);
        tvDetailUser = findViewById(R.id.tvDetailUser);
        tvDetailTimeStamp = findViewById(R.id.tvDetailTimeStamp);
        tvNumRetweets = findViewById(R.id.tvNumRetweets);
        tvNumLikes = findViewById(R.id.tvNumLikes);
        tvNumQuotes = findViewById(R.id.tvNumQuotes);
        ivDetailMedia = findViewById(R.id.ivDetailMedia);
        ivProfileDetail = findViewById(R.id.ivProfileDetail);

        tweet = Parcels.unwrap(getIntent().getParcelableExtra(Tweet.class.getSimpleName()));
        Log.d(TAG, String.format("Showing details for '%s'", tweet.getBody()));

        tvDetailUser.setText("@" + tweet.getUser().screenName);
        tvDetailBody.setText(tweet.getBody());
        tvDetailName.setText(tweet.getUser().name);
        try {
            tvDetailTimeStamp.setText(tweet.getRelativeTimeAgo(tweet.createdAt));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (tweet.getQuoteTweets() == null){
            tvNumQuotes.setText("0 Quote Tweets");
        }
        else {
            tvNumQuotes.setText(tweet.getQuoteTweets() + " Quote Tweets");
        }
        if (tweet.getRetweets() == null){
            tvNumRetweets.setText("0 Retweets");
        }
        else {
            tvNumRetweets.setText(tweet.getRetweets() + " Retweets");
        }
        if (tweet.getLikes() == null){
            tvNumLikes.setText("0 Likes");
        }
        else {
            tvNumLikes.setText(tweet.getLikes() + " Likes");
        }

        Glide.with(this).load(tweet.user.profileImageUrl).into(ivProfileDetail);
        if (tweet.tweet_URL != "none") {
            Glide.with(this)
                    .load(tweet.tweet_URL).into(ivDetailMedia);
        }
        else {
            ivDetailMedia.setVisibility(View.GONE);
        }
    }
}