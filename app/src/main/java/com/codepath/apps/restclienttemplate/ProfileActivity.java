package com.codepath.apps.restclienttemplate;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.Adapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.codepath.apps.restclienttemplate.models.Tweet;
import com.codepath.apps.restclienttemplate.models.User;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Headers;

public class ProfileActivity extends AppCompatActivity {

    public static String TAG = "ProfileActivity";

    ImageView ivBanner;
    ImageView ivProfileP;
    TextView tvNameProfile;
    TextView tvScreenNameProfile;
    TextView tvDescription;
    TextView tvNoFollowing;
    TextView tvNoFollowers;
    RecyclerView rvUserTweets;
    TwitterClient client;
    User user;
    List<Tweet> userTweets;
    TweetsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        client = TwitterApp.getRestClient(this); // Client to get data from API
        user = Parcels.unwrap(getIntent().getParcelableExtra(User.class.getSimpleName()));

        // Finding the views
        ivBanner = findViewById(R.id.ivBanner);
        ivProfileP = findViewById(R.id.ivProfileP);
        tvNameProfile = findViewById(R.id.tvNameProfile);
        tvScreenNameProfile = findViewById(R.id.tvScreenNameProfile);
        tvDescription = findViewById(R.id.tvDescription);
        tvNoFollowers = findViewById(R.id.tvNoFollowers);
        tvNoFollowing = findViewById(R.id.tvNoFollowing);
        rvUserTweets = findViewById(R.id.rvUserTweets);

        // Load images
        Glide.with(this).load(user.publicImageUrl).circleCrop().into(ivProfileP);
        Glide.with(this).load(user.profileBannerUrl).into(ivBanner);
        tvNameProfile.setText(user.name);
        // Set text for profile
        tvScreenNameProfile.setText(user.screenName);
        tvDescription.setText(user.description);
        tvNoFollowing.setText(String.valueOf(user.following));
        tvNoFollowers.setText(String.valueOf(user.followers));

        // Initialize the list of tweets and adapter
        userTweets = new ArrayList<>();
        adapter = new TweetsAdapter(this, userTweets);
        rvUserTweets.setLayoutManager(new LinearLayoutManager(this));
        rvUserTweets.setAdapter(adapter);

        populateUserTweets();
    }

    private void populateUserTweets() {
        client.getUserTimeline(user.id, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Headers headers, JSON json) {
                Log.i(TAG, "OnSuccess!" + json.toString());
                JSONArray jsonArray = json.jsonArray;
                try {
                    adapter.clear();
                    userTweets.addAll(Tweet.fromJsonArray(jsonArray));
                    adapter.addAll(userTweets);
                    adapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    Log.e(TAG, "Json exception", e);
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                Log.i(TAG, "OnFailure", throwable);
            }
        });
    }
}