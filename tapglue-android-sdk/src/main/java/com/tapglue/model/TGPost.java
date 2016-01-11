package com.tapglue.model;

import android.content.Intent;
import android.support.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tapglue.networking.TGCustomCacheObject;

import java.util.List;

/**
 * Created by adrian on 11.01.16.
 */
public class TGPost extends TGBaseObjectWithId<TGPost> {

    @Expose
    @SerializedName("user_id")
    private Integer mUserId;
    @Expose
    @SerializedName("visibility")
    private Integer mVisibility;
    @Expose
    @SerializedName("tags")
    private List<String> mTags;
    @Expose
    @SerializedName("attachments")
    private List<TGAttachment> mAttachments;
    @Expose
    @SerializedName("counts")
    private Counts mCounts;
    @Expose
    @SerializedName("isLiked")
    private Boolean mIsLiked;

    public static class Counts {
        @Expose
        @SerializedName("comments")
        public Integer comments;
        @Expose
        @SerializedName("likes")
        public Integer likes;
        @Expose
        @SerializedName("shares")
        public Integer shares;
    }

    public TGPost() {
        super(TGCustomCacheObject.TGCacheObjectType.Post);
    }

    @Override
    protected TGPost getThis() {
        return this;
    }

    public Integer getUserId() {
        return mUserId;
    }

    public Integer getVisibility() {
        return mVisibility;
    }

    public TGPost setVisibility(Integer mVisibility) {
        this.mVisibility = mVisibility;
        return getThis();
    }

    public List<String> getTags() {
        return mTags;
    }

    public TGPost setTags(List<String> mTags) {
        this.mTags = mTags;
        return getThis();
    }

    public List<TGAttachment> getAttachments() {
        return mAttachments;
    }

    public TGPost setAttachments(List<TGAttachment> mAttachments) {
        this.mAttachments = mAttachments;
        return getThis();
    }

    public Integer getCommentsCount() {
        if (mCounts == null)
            return 0;
        return mCounts.comments == null ? 0 : mCounts.comments;
    }

    public Integer getLikesCount() {
        if (mCounts == null)
            return 0;
        return mCounts.likes == null ? 0 : mCounts.likes;
    }

    public Integer getSharesCount() {
        if (mCounts == null)
            return 0;
        return mCounts.shares == null ? 0 : mCounts.shares;
    }

    public Boolean getIsLiked() {
        return mIsLiked;
    }
}
