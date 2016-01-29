/*
 * Copyright (c) 2015-2016 Tapglue (https://www.tapglue.com/). All rights reserved.
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
    private List<TGAttachment> attachments;

    @Expose
    @SerializedName("counts")
    private Counts counts;

    @Expose
    @SerializedName("is_liked")
    private Boolean isLiked;

    @Expose
    @SerializedName("tags")
    private List<String> tags;

    @Expose
    @SerializedName("user_id")
    private Long userId;

    @Expose
    @SerializedName("visibility")
    private Integer visibility;

    public TGPost() {
        super(TGCustomCacheObject.TGCacheObjectType.Post);
        visibility = TGVisibility.Public.asValue();
    }

    /**
     * Get attachments
     *
     * @return attachments
     */
    public List<TGAttachment> getAttachments() {
        return attachments;
    }

    /**
     * Set attachments
     *
     * @param attachments New attachments
     *
     * @return Current instance
     */
    @NonNull
    public TGPost setAttachments(List<TGAttachment> attachments) {
        this.attachments = attachments;
        return getThis();
    }

    /**
     * Get amount of comments
     *
     * @return Amount of comments
     */
    @NonNull
    public Integer getCommentsCount() {
        if (counts == null) { return 0; }
        return counts.comments == null ? 0 : counts.comments;
    }

    /**
     * Is post liked by current user?
     *
     * @return Is post liked?
     */
    public Boolean getIsLiked() {
        return isLiked;
    }

    /**
     * Get amount of likes
     *
     * @return Amount of likes
     */
    @NonNull
    public Integer getLikesCount() {
        if (counts == null) { return 0; }
        return counts.likes == null ? 0 : counts.likes;
    }

    /**
     * Get amount of shares
     *
     * @return Amount of shares
     */
    @NonNull
    public Integer getSharesCount() {
        if (counts == null) { return 0; }
        return counts.shares == null ? 0 : counts.shares;
    }

    /**
     * Get tags assigned to post
     *
     * @return Tags
     */
    public List<String> getTags() {
        return tags;
    }

    /**
     * Set tags assigned to post
     *
     * @param tags Tags
     *
     * @return Current instance
     */
    @NonNull
    public TGPost setTags(List<String> tags) {
        this.tags = tags;
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
        return userId;
    }

    /**
     * Get post visibility
     *
     * @return
     */
    @NonNull
    public TGVisibility getVisibility() {
        return TGVisibility.fromValue(visibility);
    }

    /**
     * Set visibility Supported values for api 0.4: PRIVATE PUBLIC CONNECTIONS
     *
     * @param visibility New visibility
     *
     * @return Current instance
     */
    @NonNull
    public TGPost setVisibility(@NonNull TGVisibility visibility) {
        this.visibility = visibility.asValue();
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
