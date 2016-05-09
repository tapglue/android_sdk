package com.tapglue.sdk.http;

import com.tapglue.sdk.entities.User;

import com.google.gson.annotations.SerializedName;

import java.util.List;

class UsersFeed {
    @SerializedName("users")
    private List<User> users;

    UsersFeed(List<User> users) {
        this.users = users;   
     }

     public List<User> getUsers() {
        return users;
     }
}