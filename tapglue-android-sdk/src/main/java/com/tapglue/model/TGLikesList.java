package com.tapglue.model;

import android.support.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tapglue.networking.TGCustomCacheObject;

import java.util.List;

/**
 * Created by adrian on 11.01.16.
 */
public class TGLikesList extends TGBaseObject<TGLikesList> {
    @Expose
    @SerializedName("likes")
    private List<TGLike> mLikes;

    @Expose
    @SerializedName("post")
    private TGPost mPost;

    @Expose
    @SerializedName("likes_count")
    private Integer mLikesCount;

    TGLikesList(@NonNull TGCustomCacheObject.TGCacheObjectType type) {
        super(type);
    }

    @Override
    protected TGLikesList getThis() {
        return this;
    }

    public List<TGLike> getLikes() {
        return mLikes;
    }

    public TGPost getPost() {
        return mPost;
    }

    public Integer getLikesCount() {
        return mLikesCount;
    }
}
