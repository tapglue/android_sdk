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
import com.tapglue.model.queries.TGQuery;
import com.tapglue.networking.TGCustomCacheObject;

import java.util.List;

public class TGPostsList extends TGBaseObject<TGPostsList> {
    @Expose
    @SerializedName("posts")
    private List<TGPost> mPosts;

    @Expose
    @SerializedName("posts_count")
    private Integer mPostsCount;

    public TGPostsList() {
        super(TGCustomCacheObject.TGCacheObjectType.PostList);
    }

    @Override
    protected TGPostsList getThis() {
        return this;
    }

    /**
     * Get posts
     * @return Posts
     */
    public List<TGPost> getPosts() {
        return mPosts;
    }

    /**
     * Get posts count
     * @return Posts Count
     */
    public Integer getCount() {
        return mPostsCount;
    }

}
