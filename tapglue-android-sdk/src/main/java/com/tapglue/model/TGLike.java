package com.tapglue.model;

import android.support.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tapglue.networking.TGCustomCacheObject;

/**
 * Created by adrian on 11.01.16.
 */
public class TGLike extends TGBaseObjectWithId<TGLike> {
    @Expose
    @SerializedName("post_id")
    private String mPostId;
    @Expose
    @SerializedName("user_id")
    private String mUserId;

    TGLike(@NonNull TGCustomCacheObject.TGCacheObjectType type) {
        super(type);
    }

    @Override
    protected TGLike getThis() {
        return this;
    }

    public String getPostId() {
        return mPostId;
    }

    public TGLike setPostId(String mPostId) {
        this.mPostId = mPostId;
        return this;
    }

    public String getUserId() {
        return mUserId;
    }

    public TGLike setUserId(String mUserId) {
        this.mUserId = mUserId;
        return this;
    }
}
