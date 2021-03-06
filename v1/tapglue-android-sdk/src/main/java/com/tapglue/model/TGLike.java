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

public class TGLike extends TGBaseObjectWithId<TGLike, Long> {
    @Expose
    @SerializedName("post_id")
    private String postId;

    @Expose
    @SerializedName("user_id")
    private String userId;

    public TGLike() {
        super(TGCacheObjectType.Like);
    }

    /**
     * Get ID of post
     *
     * @return post id
     */
    public String getPostId() {
        return postId;
    }

    /**
     * Set ID of post Setting this won't change server value - use only for ui updates if needed
     *
     * @param postId
     *
     * @return
     */
    @NonNull
    public TGLike setPostId(String postId) {
        this.postId = postId;
        return this;
    }

    @NonNull
    @Override
    protected TGLike getThis() {
        return this;
    }

    /**
     * Get ID of user
     *
     * @return
     */
    public String getUserId() {
        return userId;
    }

    /**
     * Set id of user assigned to like Setting this won't change server value - use only for ui
     * updates if needed
     *
     * @param userId
     *
     * @return
     */
    @NonNull
    public TGLike setUserId(String userId) {
        this.userId = userId;
        return this;
    }
}
