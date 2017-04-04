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

public class RawNewsFeed extends FlattenableFeed<NewsFeed> {
    List<Event> events;
    List<Post> posts;
    Map<String, User> users;
    @SerializedName("post_map")
    Map<String, Post> postMap;

    @Override
    public NewsFeed flatten() {
        EventListFeed eventFeed = new EventListFeed();
        eventFeed.events = events;
        eventFeed.users = users;
        eventFeed.posts = postMap;
        List<Event> events = new EventFeedToList().apply(eventFeed);

        PostListFeed postFeed = new PostListFeed();
        postFeed.posts = posts;
        postFeed.users = users;
        List<Post> posts = new PostFeedToList().apply(postFeed);
        return new NewsFeed(events, posts);
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
