package com.tapglue.android.http;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import com.tapglue.android.entities.Post;
import com.tapglue.android.entities.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

class PostListFeed extends FlattenableFeed<List<Post>> {
    List<Post> posts;
    Map<String, User> users;

    @Override
    public List<Post> flatten() {
        for(Post post: posts) {
            post.setUser(users.get(post.getUserId()));
        }
        return posts;
    }

    @Override
    FlattenableFeed<List<Post>> constructDefaultFeed() {
        PostListFeed feed = new PostListFeed();
        feed.posts = new ArrayList<>();
        return feed;
    }

    @Override
    FlattenableFeed<List<Post>> parseJson(JsonObject jsonObject) {
        Gson g = new Gson();
        PostListFeed feed = g.fromJson(jsonObject, PostListFeed.class);
        return feed;
    }
}