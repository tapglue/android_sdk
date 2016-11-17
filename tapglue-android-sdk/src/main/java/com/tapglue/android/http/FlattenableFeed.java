package com.tapglue.android.http;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public abstract class FlattenableFeed<T> {

    @SerializedName("paginng")
    ApiPage previous;

    public abstract List<T> flatten();
    public abstract String previousPointer();
}