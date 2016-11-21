package com.tapglue.android.http;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.tapglue.android.entities.User;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

class UsersFeed extends FlattenableFeed<List<User>> {
    @SerializedName("users")
    private List<User> users;

    UsersFeed(List<User> users) {
        this.users = users;   
     }

     public List<User> getUsers() {
        return users;
     }

     @Override
     public List<User> flatten() {
        if(getUsers() == null) {
            return new ArrayList<>();
        }
        return getUsers();
     }

     @Override
     FlattenableFeed<List<User>> constructDefaultFeed() {
         return new UsersFeed(new ArrayList<User>());
     }

     @Override
     FlattenableFeed<List<User>> parseJson(JsonObject jsonObject) {
        Gson g = new Gson();
        UsersFeed feed = g.fromJson(jsonObject, UsersFeed.class);
        return feed;
     }
}