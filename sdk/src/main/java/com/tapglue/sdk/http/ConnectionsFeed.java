package com.tapglue.sdk.http;

import com.tapglue.sdk.entities.Connection;
import com.tapglue.sdk.entities.User;

import java.util.List;

public class ConnectionsFeed {
    public List<Connection> incoming;
    public List<Connection> outgoing;
    public List<User> users;
}