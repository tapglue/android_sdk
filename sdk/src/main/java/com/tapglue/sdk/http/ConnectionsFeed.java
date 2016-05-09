package com.tapglue.sdk.http;

import com.tapglue.sdk.entities.Connection;
import com.tapglue.sdk.entities.User;

import java.util.List;

class ConnectionsFeed {
    List<Connection> incoming;
    List<Connection> outgoing;
    List<User> users;
}