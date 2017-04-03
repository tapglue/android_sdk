package com.tapglue.android.http;

import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;

public abstract class FlattenableFeed<T> {
    @SerializedName("paging")
    ApiPage page;

    public FlattenableFeed() {

    }

    public abstract T flatten();

    public final FlattenableFeed<T> parse(JsonObject jsonObject) {
        if(jsonObject == null) {
            FlattenableFeed<T> feed = constructDefaultFeed();
            feed.page = page;
            return feed;
        }
        return parseJson(jsonObject);
    }

    public String previousPointer() {
        return page.beforePointer;
    }

    abstract FlattenableFeed<T> constructDefaultFeed();
    abstract FlattenableFeed<T> parseJson(JsonObject jsonObject);
}