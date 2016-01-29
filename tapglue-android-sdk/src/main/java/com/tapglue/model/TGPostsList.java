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

import java.util.List;

public class TGPostsList extends TGBaseObject<TGPostsList> {
    @Expose
    @SerializedName("posts")
    private List<TGPost> posts;

    @Expose
    @SerializedName("posts_count")
    private Integer postsCount;

    public TGPostsList() {
        super(TGCacheObjectType.PostList);
    }

    /**
     * Get posts count
     *
     * @return Posts Count
     */
    public Integer getCount() {
        return postsCount;
    }

    /**
     * Get posts
     *
     * @return Posts
     */
    public List<TGPost> getPosts() {
        return posts;
    }

    @NonNull
    @Override
    protected TGPostsList getThis() {
        return this;
    }

}
