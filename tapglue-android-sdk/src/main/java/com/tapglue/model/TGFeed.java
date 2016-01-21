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
import com.tapglue.model.queries.TGQuery;
import com.tapglue.networking.TGCustomCacheObject;

import java.util.List;

public class TGFeed extends TGBaseObject<TGFeed> {

    @Expose
    @SerializedName("events")
    private List<TGEvent> mEvents;

    @Expose
    @SerializedName("posts")
    private List<TGPost> mPosts;

    @Expose
    @SerializedName("posts_count")
    private Integer mPostsCount;

    @Expose
    @SerializedName("query")
    private TGQuery mSearchQuery;

    @Expose
    @SerializedName("unread_events_count")
    private Long mUnreadCounter;

    public TGFeed() {
        super(TGCustomCacheObject.TGCacheObjectType.EventsList);
    }

    /**
     * Get events assigned to feed
     *
     * @return events
     */
    public List<TGEvent> getEvents() {
        return mEvents;
    }

    /**
     * Get posts
     *
     * @return Posts
     */
    public List<TGPost> getPosts() {
        return mPosts;
    }

    /**
     * Get posts count
     *
     * @return Posts Count
     */
    public Integer getPostsCount() {
        return mPostsCount;
    }

    /**
     * Get search parameters from this query This is used only on read for library internal purposes
     * Library will overwrite any values entered manually
     *
     * @return
     */
    @Deprecated
    public TGQuery getSearchQuery() {
        return mSearchQuery;
    }

    /**
     * Set search parameters for this query This is used only on read for library internal purposes
     * Library will overwrite any values entered manually
     *
     * @param query
     *
     * @return
     */
    @Deprecated
    public TGFeed setSearchQuery(TGQuery query) {
        this.mSearchQuery = query;
        return this;
    }

    @Override
    protected TGFeed getThis() {
        return this;
    }

    /**
     * Get unread events count
     *
     * @return unread events count
     */
    public Long getUnreadCount() {
        return mUnreadCounter;
    }

    /**
     * Set amount of unread - used also internally by requests
     *
     * @param count
     *
     * @return Current object
     */
    @NonNull
    public TGBaseObject setUnreadCount(Long count) {
        mUnreadCounter = count;
        return this;
    }
}
