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
import com.tapglue.networking.TGCustomCacheObject.TGCacheObjectType;

public class TGComment extends TGBaseObjectWithId<TGComment, Long> {
    @Expose
    @SerializedName("content")
    private String content;

    @Expose
    @SerializedName("post_id")
    private String postId;

    @Expose
    @SerializedName("user_id")
    private Long userId;

    public TGComment() {
        super(TGCacheObjectType.Comment);
    }

    /**
     * Get comment
     *
     * @return Comment
     */
    public String getContent() {
        return content;
    }

    /**
     * Set comment
     *
     * @param content Comment value
     *
     * @return Current instance
     */
    @NonNull
    public TGComment setContent(String content) {
        this.content = content;
        return this;
    }

    /**
     * Get post ID
     *
     * @return Post id
     */
    public String getPostId() {
        return postId;
    }

    /**
     * Set post id
     *
     * @param postId New value
     *
     * @return Current instance
     */
    @NonNull
    public TGComment setPostId(String postId) {
        this.postId = postId;
        return this;
    }

    @NonNull
    @Override
    protected TGComment getThis() {
        return this;
    }

    /**
     * Get post author user id
     *
     * @return User id
     */
    public Long getUserId() {
        return userId;
    }

    /**
     * Set post author user id
     *
     * @param userId New value
     *
     * @return Current instance
     */
    @NonNull
    public TGComment setUserId(Long userId) {
        this.userId = userId;
        return this;
    }
}
