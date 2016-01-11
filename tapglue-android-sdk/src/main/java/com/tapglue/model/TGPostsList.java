package com.tapglue.model;

import android.support.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tapglue.networking.TGCustomCacheObject;

import java.util.List;

/**
 * Created by adrian on 11.01.16.
 */
public class TGPostsList extends TGBaseObject<TGPostsList> {
    @Expose
    @SerializedName("posts")
    private List<TGPost> mPosts;

    @Expose
    @SerializedName("posts_count")
    private Integer mPostsCount;

    public TGPostsList() {
        super(TGCustomCacheObject.TGCacheObjectType.PostList);
    }

    @Override
    protected TGPostsList getThis() {
        return this;
    }

    public List<TGPost> getPosts() {
        return mPosts;
    }

    public Integer getCount() {
        return mPostsCount;
    }
}
