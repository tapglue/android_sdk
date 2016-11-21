package com.tapglue.android.http;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;
import com.tapglue.android.entities.Event;
import com.tapglue.android.entities.NewsFeed;
import com.tapglue.android.entities.Post;
import com.tapglue.android.entities.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

class RawNewsFeed extends FlattenableFeed<NewsFeed> {
    List<Event> events;
    List<Post> posts;
    Map<String, User> users;
    @SerializedName("post_map")
    Map<String, Post> postMap;

    @Override
    public NewsFeed flatten() {
        return new RawNewsFeedToFeed().call(this);
    }

    @Override
    FlattenableFeed<NewsFeed> constructDefaultFeed() {
        RawNewsFeed feed = new RawNewsFeed();
        feed.events = new ArrayList<>();
        feed.posts = new ArrayList<>();
        return feed;
    }

    @Override
    FlattenableFeed<NewsFeed> parseJson(JsonObject jsonObject) {
        Gson g = new Gson();
        RawNewsFeed feed = g.fromJson(jsonObject, RawNewsFeed.class);
        return feed;
    }
}
