package com.tapglue.android.http;

import com.google.gson.annotations.SerializedName;
import com.tapglue.android.entities.Event;
import com.tapglue.android.entities.Post;
import com.tapglue.android.entities.User;

import java.util.List;
import java.util.Map;

class RawNewsFeed {
    List<Event> events;
    List<Post> posts;
    Map<String, User> users;
    @SerializedName("post_map")
    Map<String, Post> postMap;
}
