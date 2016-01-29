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

public class TGLikesList extends TGBaseObject<TGLikesList> {
    @Expose
    @SerializedName("likes")
    private List<TGLike> mLikes;

    @Expose
    @SerializedName("likes_count")
    private Integer mLikesCount;

    @Expose
    @SerializedName("post")
    private TGPost mPost;

    public TGLikesList() {
        super(TGCustomCacheObject.TGCacheObjectType.LikesList);
    }

    /**
     * Get list of likes
     *
     * @return Likes details
     */
    public List<TGLike> getLikes() {
        return mLikes;
    }

    /**
     * Get amount of likes
     *
     * @return Likes counter
     */
    public Integer getLikesCount() {
        return mLikesCount;
    }

    /**
     * Get post to which likes are assigned to
     *
     * @return Post
     */
    public TGPost getPost() {
        return mPost;
    }

    @NonNull
    @Override
    protected TGLikesList getThis() {
        return this;
    }
}
