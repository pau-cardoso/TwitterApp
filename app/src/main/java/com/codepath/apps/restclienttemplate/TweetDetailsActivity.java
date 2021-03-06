package com.codepath.apps.restclienttemplate;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterInside;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.codepath.apps.restclienttemplate.models.Tweet;
import com.codepath.apps.restclienttemplate.models.User;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;

import org.json.JSONException;
import org.parceler.Parcels;

import okhttp3.Headers;

public class TweetDetailsActivity extends AppCompatActivity {

    public static String TAG = "TweetDetailsActivity";
    Context context;

    Tweet tweet;
    ImageView ivProfile;
    ImageView ivMedia;
    TextView tvName;
    TextView tvScreenName;
    TextView tvBody;
    TextView tvDate;
    Button ibtnLike;
    ImageButton ibtnRetweet;
    TextView tvNoRetweets;
    TextView tvNoLikes;
    TwitterClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tweet_details);

        client = TwitterApp.getRestClient(this);
        ivProfile = findViewById(R.id.ivProfile);
        ivMedia = findViewById(R.id.ivMedia);
        tvName = findViewById(R.id.tvName);
        tvScreenName = findViewById(R.id.tvScreenName);
        tvBody = findViewById(R.id.tvBody);
        tvDate = findViewById(R.id.tvDate);
        ibtnLike = findViewById(R.id.ibtnLike);
        ibtnRetweet = findViewById(R.id.ibtnRetweet);
        tvNoLikes = findViewById(R.id.tvNoLikes);
        tvNoRetweets = findViewById(R.id.tvNoRetweets);

        tweet = Parcels.unwrap(getIntent().getParcelableExtra(Tweet.class.getSimpleName()));

        Glide.with(this).load(tweet.user.publicImageUrl).circleCrop().into(ivProfile);
        Glide.with(this).load(tweet.mediaUrl).transform(new CenterInside(), new RoundedCorners(25)).into(ivMedia);
        tvName.setText(tweet.user.name);
        tvScreenName.setText(tweet.user.screenName);
        tvBody.setText(tweet.body);
        tvDate.setText(tweet.date);
        tvNoLikes.setText(String.valueOf(tweet.favoriteCount));
        tvNoRetweets.setText(String.valueOf(tweet.retweetCount));

        ivProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(TweetDetailsActivity.this, "Looking profile!", Toast.LENGTH_SHORT).show();
                User user = tweet.user;
                Intent i = new Intent(TweetDetailsActivity.this, ProfileActivity.class);
                i.putExtra(User.class.getSimpleName(), Parcels.wrap(user)); // serialize the user using parceler, use its short name as a key
                TweetDetailsActivity.this.startActivity(i); // show the activity
            }
        });

        ibtnLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ibtnLike.setTextColor(getResources().getColor(R.color.inline_action_like));
                Toast.makeText(TweetDetailsActivity.this, "liked!", Toast.LENGTH_SHORT).show();
                client.likeTweet(tweet.idString, new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Headers headers, JSON json) {
                        Log.i(TAG, "OnSuccess to like tweet");
                    }

                    @Override
                    public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                        Log.i(TAG, "OnFailure to like tweet", throwable);
                    }
                });

            }
        });

        ibtnRetweet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ibtnRetweet.setColorFilter(getResources().getColor(R.color.inline_action_retweet));
                client.retweetTweet(tweet.idString, new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Headers headers, JSON json) {
                        Toast.makeText(TweetDetailsActivity.this, "Retweet successful!", Toast.LENGTH_SHORT).show();
                        Log.i(TAG, "OnSuccess to retweet");
                    }

                    @Override
                    public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                        Log.i(TAG, "OnFailure to retweet", throwable);
                    }
                });

            }
        });
    }
}