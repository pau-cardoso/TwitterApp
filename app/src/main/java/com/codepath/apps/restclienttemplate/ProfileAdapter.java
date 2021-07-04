package com.codepath.apps.restclienttemplate;

import android.content.Context;
import android.content.Intent;
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

import org.jetbrains.annotations.NotNull;
import org.parceler.Parcels;

import java.util.List;

public class ProfileAdapter extends RecyclerView.Adapter<ProfileAdapter.ViewHolder> {

    Context context;
    List<Tweet> userTweets;

    // Pass in the context and list of tweets
    public ProfileAdapter(Context context, List<Tweet> userTweets) {
        this.context = context;
        this.userTweets = userTweets;
    }

    // For each row, inflate the layout for the tweet
    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_tweet, parent, false);
        return new ViewHolder(view);
    }

    // Bind values based on position of the element
    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolder holder, int position) {
        // Get the data at position
        Tweet tweet = userTweets.get(position);
        // Bind tweet with View Holder
        holder.bind(tweet);
    }

    @Override
    public int getItemCount() {
        return userTweets.size() ;
    }


    // Define a ViewHolder
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView ivProfileImage;
        ImageView ivMedia;
        TextView tvCreatedAt;
        TextView tvScreenName;
        TextView tvName;
        TextView tvBody;

        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            ivProfileImage = itemView.findViewById(R.id.ivProfileImage);
            ivMedia = itemView.findViewById(R.id.ivMedia);
            tvScreenName = itemView.findViewById(R.id.tvScreenName);
            tvCreatedAt = itemView.findViewById(R.id.tvCreatedAt);
            tvName = itemView.findViewById(R.id.tvName);
            tvBody = itemView.findViewById(R.id.tvBody);
            itemView.setOnClickListener(this);
        }

        public void bind(Tweet tweet) {
            tvBody.setText(tweet.body);
            tvScreenName.setText(tweet.user.screenName);
            tvName.setText(tweet.user.name);
            tvCreatedAt.setText(tweet.createdAt);
            Glide.with(context).load(tweet.user.publicImageUrl).circleCrop().into(ivProfileImage);
            Log.i("TweetsAdapter", "mediaURL: " + tweet.mediaUrl);
            if (tweet.mediaUrl != null) {
                Glide.with(context).load(tweet.mediaUrl).into(ivMedia);
            } else  {
                ivMedia.setVisibility(View.GONE);
            }
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            if (position != RecyclerView.NO_POSITION) {
                Tweet tweet = userTweets.get(position);
                Intent i = new Intent(context, TweetDetailsActivity.class);
                i.putExtra(Tweet.class.getSimpleName(), Parcels.wrap(tweet)); // serialize the tweet using parceler, use its short name as a key
                context.startActivity(i); // show the activity
            }
        }
    }
}