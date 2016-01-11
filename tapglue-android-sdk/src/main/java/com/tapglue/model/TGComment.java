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

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tapglue.networking.TGCustomCacheObject;

public class TGComment extends TGBaseObjectWithId<TGComment> {
    @Expose
    @SerializedName("content")
    private String mContent;
    @Expose
    @SerializedName("post_id")
    private Long mPostId;
    @Expose
    @SerializedName("user_id")
    private Long mUserId;

    public TGComment() {
        super(TGCustomCacheObject.TGCacheObjectType.Comment);
    }

    @Override
    protected TGComment getThis() {
        return this;
    }

    /**
     * Get comment
     *
     * @return Comment
     */
    public String getContent() {
        return mContent;
    }

    /**
     * Set comment
     *
     * @param mContent Comment value
     * @return Current instance
     */
    public TGComment setContent(String mContent) {
        this.mContent = mContent;
        return this;
    }

    /**
     * Get post ID
     *
     * @return Post id
     */
    public Long getPostId() {
        return mPostId;
    }

    /**
     * Set post id
     *
     * @param mPostId New value
     * @return Current instance
     */
    public TGComment setPostId(Long mPostId) {
        this.mPostId = mPostId;
        return this;
    }

    /**
     * Get post author user id
     *
     * @return User id
     */
    public Long getUserId() {
        return mUserId;
    }

    /**
     * Set post author user id
     *
     * @param mUserId New value
     * @return Current instance
     */
    public TGComment setUserId(Long mUserId) {
        this.mUserId = mUserId;
        return this;
    }
}
