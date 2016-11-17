package com.tapglue.android.http;

import com.google.gson.annotations.SerializedName;

public abstract class FlattenableFeed<T> {
    @SerializedName("paging")
    ApiPage page;

    public abstract T flatten();

    public String previousPointer() {
        return page.beforePointer;
    }
}