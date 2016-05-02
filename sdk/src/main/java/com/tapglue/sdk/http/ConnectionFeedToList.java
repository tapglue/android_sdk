package com.tapglue.sdk.http;

import com.tapglue.sdk.entities.ConnectionList;
import com.tapglue.sdk.entities.User;
import com.tapglue.sdk.entities.Connection;
import java.util.Map;
import java.util.HashMap;

import rx.functions.Func1;

public class ConnectionFeedToList implements Func1<ConnectionsFeed, ConnectionList> {

    @Override
    public ConnectionList call(ConnectionsFeed feed) {
        Map<String, User> userMap = new HashMap<>();
        for(User user: feed.users) {
            userMap.put(user.getId(), user);
        }
        for(Connection connection: feed.incoming){
            User user = userMap.get(connection.getUserFromId());
            connection.setUserFrom(user);
        }
        for(Connection connection: feed.outgoing) {
            User user = userMap.get(connection.getUserToId());
            connection.setUserTo(user);
        }
        return new ConnectionList(feed.incoming, feed.outgoing);
    }
}
