package com.tapglue.android.entities;

public class Follow extends Connection {
    public Follow(User user) {
        super(user, Type.FOLLOW, State.CONFIRMED);
    }
}