package com.tapglue.sdk.http;

import com.tapglue.sdk.entities.Post;
import com.tapglue.sdk.entities.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import rx.functions.Func1;

public class PostFeedToList implements Func1<PostListFeed, List<Post>> {

    @Override
    public List<Post> call(PostListFeed feed) {
        if(feed == null || feed.posts == null) {
            return new ArrayList<>();
        }
        List<Post> posts = feed.posts;
        Map<String, User> users = feed.users;
        for(Post post: posts) {
            post.setUser(users.get(post.getUserId()));
        }
        return posts;
    }
}