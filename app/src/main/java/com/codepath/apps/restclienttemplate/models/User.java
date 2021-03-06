package com.codepath.apps.restclienttemplate.models;

import android.util.JsonReader;

import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

@Parcel
public class User {
    public String name;
    public String screenName;
    public String publicImageUrl;
    public String profileBannerUrl;
    public String description;
    public Integer followers;
    public Integer following;
    public String id;

    // empty constructor needed by the Parceler library
    public User() {}

    public static User fromJson(JSONObject jsonObject) throws JSONException {
        User user = new User();
        user.name = jsonObject.getString("name");
        user.screenName = "@"+jsonObject.getString("screen_name");
        user.publicImageUrl = jsonObject.getString("profile_image_url_https");
        user.profileBannerUrl = jsonObject.getString("profile_banner_url");
        user.description = jsonObject.getString("description");
        user.followers = jsonObject.getInt("followers_count");
        user.following = jsonObject.getInt("friends_count");
        user.id = jsonObject.getString("id_str");
        return user;
    }
}
