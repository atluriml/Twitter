package com.codepath.apps.restclienttemplate;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.codepath.apps.restclienttemplate.models.Tweet;

import java.text.ParseException;
import java.util.List;

public class TweetsAdapter extends RecyclerView.Adapter<TweetsAdapter.ViewHolder>{

    Context context;
    List<Tweet> tweets;

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
    public class ViewHolder extends RecyclerView.ViewHolder{

        ImageView ivProfileImage;
        TextView tvBody;
        TextView tvScreenName;
        ImageView ivTweetMedia;
        TextView tvTimeStamp;

        public ViewHolder(@NonNull View itemView){
            super(itemView);
            ivProfileImage = itemView.findViewById(R.id.ivProfileImage);
            ivTweetMedia = itemView.findViewById(R.id.ivTweetMedia);
            tvBody = itemView.findViewById(R.id.tvBody);
            tvScreenName = itemView.findViewById(R.id.tvScreenName);
            tvTimeStamp = itemView.findViewById(R.id.tvTimeStamp);
        }

        public void bind(Tweet tweet) throws ParseException {
            tvBody.setText(tweet.body);
            tvScreenName.setText(tweet.user.screenName);
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
        }
    }

    public void clear() {
        tweets.clear();
        notifyDataSetChanged();
    }

}
