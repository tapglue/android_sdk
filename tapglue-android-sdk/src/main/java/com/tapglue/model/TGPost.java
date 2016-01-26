/*
 * Copyright (c) 2015 Tapglue (https://www.tapglue.com/). All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
package com.tapglue.model;

import android.support.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tapglue.networking.TGCustomCacheObject;

import java.util.List;

public class TGPost extends TGBaseObjectWithId<TGPost, String> {

    @Expose
    @SerializedName("attachments")
    private List<TGAttachment> mAttachments;

    @Expose
    @SerializedName("counts")
    private Counts mCounts;

    @Expose
    @SerializedName("is_liked")
    private Boolean mIsLiked;

    @Expose
    @SerializedName("tags")
    private List<String> mTags;

    @Expose
    @SerializedName("user_id")
    private Long mUserId;

    @Expose
    @SerializedName("visibility")
    private Integer mVisibility;

    public TGPost() {
        super(TGCustomCacheObject.TGCacheObjectType.Post);
        mVisibility = TGVisibility.Public.asValue();
    }

    /**
     * Get attachments
     *
     * @return attachments
     */
    public List<TGAttachment> getAttachments() {
        return mAttachments;
    }

    /**
     * Set attachments
     *
     * @param mAttachments New attachments
     *
     * @return Current instance
     */
    @NonNull
    public TGPost setAttachments(List<TGAttachment> mAttachments) {
        this.mAttachments = mAttachments;
        return getThis();
    }

    /**
     * Get amount of ccomments
     *
     * @return Amount of comments
     */
    @NonNull
    public Integer getCommentsCount() {
        if (mCounts == null) { return 0; }
        return mCounts.comments == null ? 0 : mCounts.comments;
    }

    /**
     * Is post liked by current user?
     *
     * @return Is post liked?
     */
    public Boolean getIsLiked() {
        return mIsLiked;
    }

    /**
     * Get amount of likes
     *
     * @return Amount of likes
     */
    @NonNull
    public Integer getLikesCount() {
        if (mCounts == null) { return 0; }
        return mCounts.likes == null ? 0 : mCounts.likes;
    }

    /**
     * Get amount of shares
     *
     * @return Amount of shares
     */
    @NonNull
    public Integer getSharesCount() {
        if (mCounts == null) { return 0; }
        return mCounts.shares == null ? 0 : mCounts.shares;
    }

    /**
     * Get tags assigned to post
     *
     * @return Tags
     */
    public List<String> getTags() {
        return mTags;
    }

    /**
     * Set tags assigned to post
     *
     * @param mTags Tags
     *
     * @return Current instance
     */
    @NonNull
    public TGPost setTags(List<String> mTags) {
        this.mTags = mTags;
        return getThis();
    }

    @NonNull
    @Override
    protected TGPost getThis() {
        return this;
    }

    /**
     * Get ID of user
     *
     * @return
     */
    public Long getUserId() {
        return mUserId;
    }

    /**
     * Get post visibility
     *
     * @return
     */
    public TGVisibility getVisibility() {
        return TGVisibility.fromValue(mVisibility);
    }

    /**
     * Set visibility Supported values for api 0.4: PRIVATE PUBLIC CONNECTIONS
     *
     * @param mVisibility New visibility
     *
     * @return Current instance
     */
    @NonNull
    public TGPost setVisibility(@NonNull TGVisibility mVisibility) {
        this.mVisibility = mVisibility.asValue();
        return getThis();
    }

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

}
