package com.codepath.apps.restclienttemplate;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.codepath.apps.restclienttemplate.models.Tweet;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;

import org.parceler.Parcels;

import java.text.ParseException;
import java.util.List;

import okhttp3.Headers;

public class TweetsAdapter extends RecyclerView.Adapter<TweetsAdapter.ViewHolder>{

    public static final String TAG = "TweetsAdapter";
    Context context;
    List<Tweet> tweets;
    TwitterClient client;

    // pass in the context and list of tweets
    public TweetsAdapter(Context context, List<Tweet> tweets){
        this.context = context;
        this.tweets = tweets;
    }

    // for each row. inflate the layout
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_tweet, parent, false);
        return new ViewHolder(view);
    }

    // bind values based on the position of the element
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Tweet tweet = tweets.get(position);
        try {
            holder.bind(tweet);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return tweets.size();
    }

    // define a viewholder
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView ivProfileImage;
        TextView tvBody;
        TextView tvName;
        TextView tvScreenName;
        ImageView ivTweetMedia;
        TextView tvTimeStamp;
        ImageButton btnReply;
        ImageButton btnRetweet;
        ImageButton btnLike;
        TextView tvLikes;
        TextView tvReply;
        TextView tvRetweet;

        public ViewHolder(@NonNull View itemView){
            super(itemView);
            ivProfileImage = itemView.findViewById(R.id.ivProfileImage);
            ivTweetMedia = itemView.findViewById(R.id.ivTweetMedia);
            tvBody = itemView.findViewById(R.id.tvBody);
            tvScreenName = itemView.findViewById(R.id.tvScreenName);
            tvTimeStamp = itemView.findViewById(R.id.tvTimeStamp);
            client = TwitterApplication.getRestClient(context);
            tvLikes = itemView.findViewById(R.id.tvLikes);
            tvReply = itemView.findViewById(R.id.tvReply);
            tvRetweet = itemView.findViewById(R.id.tvRetweet);
            tvName = itemView.findViewById(R.id.tvName);

            itemView.setOnClickListener(this);

            // when user wants to reply to a tweet
            btnReply = itemView.findViewById(R.id.btnReply);
            btnReply.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, ComposeActivity.class);
                    intent.putExtra( "tweet_username", tvScreenName.getText());
                    Log.i(TAG, "username that is being replied to: " + tvScreenName.getText());
                    context.startActivity(intent);
                }
            });

            btnRetweet = itemView.findViewById(R.id.btnRetweet);
            btnRetweet.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        Tweet tweet = tweets.get(position);
                        // user is retweeting tweet
                        if(!tweet.isRetweeted) {
                            btnRetweet.setImageResource(R.drawable.ic_vector_retweet);
                            btnRetweet.setColorFilter(Color.parseColor("#ff17bf63"));
                            client.retweet(tweet.user.getName(), tweet.getId(), new JsonHttpResponseHandler() {
                                @Override
                                public void onSuccess(int statusCode, Headers headers, JSON json) {
                                    Log.i(TAG, "onSuccess to retweet tweet");
                                    tweet.changeRetweetStatus();
                                }
                                @Override
                                public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                                    Log.e(TAG, "onFailure to retweet tweet", throwable);
                                }
                            });
                        }
                        // user is unRetweeting tweet
                        else{
                            btnRetweet.setImageResource(R.drawable.ic_vector_retweet_stroke);
                            btnRetweet.setColorFilter(Color.parseColor("#AAB8C2"));
                            client.unRetweet(tweet.user.name, tweet.getId(), new JsonHttpResponseHandler() {
                                @Override
                                public void onSuccess(int statusCode, Headers headers, JSON json) {
                                    Log.i(TAG, "onSuccess to unRetweet tweet");
                                    tweet.changeRetweetStatus();
                                }
                                @Override
                                public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                                    Log.e(TAG, "onFailure to unRetweet tweet", throwable);
                                }
                            });
                        }
                    }
                }
            });

            btnLike = itemView.findViewById(R.id.btnLike);
            btnLike.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        Tweet tweet = tweets.get(position);
                        // user is liking tweet
                        if(!tweet.isLiked) {
                            btnLike.setImageResource(R.drawable.ic_vector_heart);
                            btnLike.setColorFilter(Color.parseColor("#ffe0245e"));
                            client.updateLikeStatus(tweet.getId(), new JsonHttpResponseHandler() {
                                @Override
                                public void onSuccess(int statusCode, Headers headers, JSON json) {
                                    Log.i(TAG, "onSuccess to like tweet");
                                    tweet.changeLikedStatus();
                                }

                                @Override
                                public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                                    Log.e(TAG, "onFailure to like tweet", throwable);
                                }
                            });
                        }
                        // user is unliking tweet
                        else{
                            btnLike.setImageResource(R.drawable.ic_vector_heart_stroke);
                            btnLike.setColorFilter(Color.parseColor("#AAB8C2"));
                            client.updateUnlikeStatus(tweet.getId(), new JsonHttpResponseHandler() {
                                @Override
                                public void onSuccess(int statusCode, Headers headers, JSON json) {
                                    Log.i(TAG, "onSuccess to unlike tweet");
                                    tweet.changeLikedStatus();
                                }
                                @Override
                                public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                                    Log.e(TAG, "onFailure to unlike tweet", throwable);
                                }
                            });
                        }
                    }
                }
            });
        }

        public void bind(Tweet tweet) throws ParseException {
            tvBody.setText(tweet.body);
            tvScreenName.setText("@" + tweet.user.screenName);
            tvName.setText(tweet.user.name);
            tvTimeStamp.setText(tweet.getRelativeTimeAgo(tweet.createdAt));
            Glide.with(context).load(tweet.user.profileImageUrl).into(ivProfileImage);
            if (tweet.tweet_URL == "none") {
                Log.d("No Image if statement", tweet.tweet_URL);
                ivTweetMedia.setVisibility(View.GONE);
            } else {
                Log.d("No Image else statement", tweet.tweet_URL);
                Glide.with(context).load(tweet.tweet_URL).into(ivTweetMedia);
                ivTweetMedia.setVisibility(View.VISIBLE);
            }
            if (tweet.isLiked){
                btnLike.setImageResource(R.drawable.ic_vector_heart);
                btnLike.setColorFilter(Color.parseColor("#ffe0245e"));
            }
            else{
                btnLike.setImageResource(R.drawable.ic_vector_heart_stroke);
                btnLike.setColorFilter(Color.parseColor("#AAB8C2"));
            }
            if (tweet.isRetweeted){
                btnRetweet.setImageResource(R.drawable.ic_vector_retweet);
                btnRetweet.setColorFilter(Color.parseColor("#ff17bf63"));
            }
            else{
                btnRetweet.setImageResource(R.drawable.ic_vector_retweet_stroke);
                btnRetweet.setColorFilter(Color.parseColor("#AAB8C2"));
            }
            tvRetweet.setText(tweet.getRetweets());
            tvLikes.setText(tweet.getLikes());
            tvReply.setText(tweet.getReplies());
        }

        @Override
        public void onClick(View view) {
            Log.i(TAG, "Success with Clicking for Detail View");
            int position = getAdapterPosition();
            if (position != RecyclerView.NO_POSITION) {
                Tweet tweet = tweets.get(position);
                Intent intent = new Intent(context, TweetDetailActivity.class);
                intent.putExtra(Tweet.class.getSimpleName(), Parcels.wrap(tweet));
                context.startActivity(intent);
            }
        }
    }

    public void clear() {
        tweets.clear();
        notifyDataSetChanged();
    }

}
