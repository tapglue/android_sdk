package com.tapglue.sdk.http;

import com.tapglue.sdk.entities.Event;
import com.tapglue.sdk.entities.Post;
import com.tapglue.sdk.entities.User;

import java.util.List;
import java.util.Map;

public class RawNewsFeed {
    List<Event> events;
    List<Post> posts;
    Map<String, User> users;
}
