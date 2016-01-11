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
    private Long mPostId;
    @Expose
    @SerializedName("user_id")
    private String mUserId;

    public TGLike() {
        super(TGCustomCacheObject.TGCacheObjectType.Like);
    }

    @Override
    protected TGLike getThis() {
        return this;
    }

    public Long getPostId() {
        return mPostId;
    }

    public TGLike setPostId(Long mPostId) {
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
