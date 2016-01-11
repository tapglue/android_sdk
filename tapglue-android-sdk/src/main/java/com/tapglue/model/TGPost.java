package com.tapglue.model;

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
    @SerializedName("user")
    private TGUser mUser;
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
    @SerializedName("commentsCount")
    private Integer mCommentsCount;
    @Expose
    @SerializedName("likesCount")
    private Integer mLikesCount;
    @Expose
    @SerializedName("sharesCount")
    private Integer mSharesCount;
    @Expose
    @SerializedName("isLiked")
    private Boolean mIsLiked;

    TGPost(@NonNull TGCustomCacheObject.TGCacheObjectType type) {
        super(type);
    }

    @Override
    protected TGPost getThis() {
        return this;
    }

    public TGUser getUser() {
        return mUser;
    }

    public TGPost setUser(TGUser mUsers) {
        this.mUser = mUsers;
        return getThis();
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
        return mCommentsCount;
    }

    public TGPost setCommentsCount(Integer mCommentsCount) {
        this.mCommentsCount = mCommentsCount;
        return getThis();
    }

    public Integer getLikesCount() {
        return mLikesCount;
    }

    public TGPost setLikesCount(Integer mLikesCount) {
        this.mLikesCount = mLikesCount;
        return getThis();
    }

    public Integer getSharesCount() {
        return mSharesCount;
    }

    public TGPost setSharesCount(Integer mSharesCount) {
        this.mSharesCount = mSharesCount;
        return getThis();
    }

    public Boolean getIsLiked() {
        return mIsLiked;
    }

    public TGPost setIsLiked(Boolean mIsLiked) {
        this.mIsLiked = mIsLiked;
        return getThis();
    }
}
