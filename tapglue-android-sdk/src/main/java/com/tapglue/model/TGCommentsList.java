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

public class TGCommentsList extends TGBaseObject<TGCommentsList> {
    @Expose
    @SerializedName("comments")
    private List<TGComment> mComments;

    @Expose
    @SerializedName("comments_count")
    private Integer mCommentsCount;

    @Expose
    @SerializedName("post")
    private TGPost mPost;

    public TGCommentsList() {
        super(TGCustomCacheObject.TGCacheObjectType.CommentsList);
    }

    /**
     * Get comments
     *
     * @return List with comments
     */
    public List<TGComment> getComments() {
        return mComments;
    }

    /**
     * Get amount of comments
     *
     * @return Comments counter
     */
    public Integer getCommentsCount() {
        return mCommentsCount;
    }

    /**
     * Get post which comments are in list
     *
     * @return Post
     */
    public TGPost getPost() {
        return mPost;
    }

    @NonNull
    @Override
    protected TGCommentsList getThis() {
        return this;
    }
}
