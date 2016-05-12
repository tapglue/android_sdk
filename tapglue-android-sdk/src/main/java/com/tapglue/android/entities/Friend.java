package com.tapglue.android.entities;

public class Friend extends Connection {
    public Friend(User user) {
        super(user, Type.FRIEND, State.PENDING);
    }
}