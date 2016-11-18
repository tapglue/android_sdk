package com.tapglue.android.http;

import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;

public abstract class FlattenableFeed<T> {
    @SerializedName("paging")
    ApiPage page;


    public FlattenableFeed() {

    }

    public abstract T flatten();

    public abstract FlattenableFeed<T> parse(JsonObject jsonObject);

    public String previousPointer() {
        return page.beforePointer;
    }
}