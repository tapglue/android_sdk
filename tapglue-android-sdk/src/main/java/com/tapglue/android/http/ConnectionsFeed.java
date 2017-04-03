package com.tapglue.android.http;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import com.tapglue.android.entities.Connection;
import com.tapglue.android.entities.ConnectionList;
import com.tapglue.android.entities.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ConnectionsFeed extends FlattenableFeed<ConnectionList> {
    List<Connection> incoming;
    List<Connection> outgoing;
    List<User> users;

    @Override
    public ConnectionList flatten() {
        if(incoming == null) {
            incoming = new ArrayList<>();
        }
        if(outgoing == null) {
            outgoing = new ArrayList<>();
        }
        if(users == null) {
            users = new ArrayList<>();
        }
        Map<String, User> userMap = new HashMap<>();
        for(User user: users) {
            userMap.put(user.getId(), user);
        }
        for(Connection connection: incoming){
            User user = userMap.get(connection.getUserFromId());
            connection.setUserFrom(user);
        }
        for(Connection connection: outgoing) {
            User user = userMap.get(connection.getUserToId());
            connection.setUserTo(user);
        }
        return new ConnectionList(incoming, outgoing);
    }

    @Override
    FlattenableFeed<ConnectionList> constructDefaultFeed() {
        ConnectionsFeed feed = new ConnectionsFeed();
        feed.incoming = new ArrayList<>();
        feed.outgoing = new ArrayList<>();
        return feed;
    }

    @Override
    FlattenableFeed<ConnectionList> parseJson(JsonObject jsonObject) {
        Gson g = new Gson();
        return g.fromJson(jsonObject, ConnectionsFeed.class);
    }
}