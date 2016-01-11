package com.tapglue.model;

import android.support.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tapglue.networking.TGCustomCacheObject;

import java.util.List;

/**
 * Created by adrian on 11.01.16.
 */
public class TGComment extends TGBaseObjectWithId<TGComment> {
    @Expose
    @SerializedName("content")
    private String mContent;
    @Expose
    @SerializedName("post_id")
    private String mPostId;
    @Expose
    @SerializedName("user_id")
    private String mUserId;
    TGComment(@NonNull TGCustomCacheObject.TGCacheObjectType type) {
        super(type);
    }

    @Override
    protected TGComment getThis() {
        return this;
    }

    public String getContent() {
        return mContent;
    }

    public TGComment setContent(String mContent) {
        this.mContent = mContent;
        return this;
    }

    public String getPostId() {
        return mPostId;
    }

    public TGComment setPostId(String mPostId) {
        this.mPostId = mPostId;
        return this;
    }

    public String getUserId() {
        return mUserId;
    }

    public TGComment setUserId(String mUserId) {
        this.mUserId = mUserId;
        return this;
    }
}
