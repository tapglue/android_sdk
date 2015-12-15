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

public class TGFeed extends TGBaseObject<TGFeed> {

    /**
     * Value used for information if object is taken from feed server object or just events list
     */
    Boolean mIsFeed = true;
    @Expose
    @SerializedName("events")
    private List<TGEvent> mEvents;
    @Expose
    @SerializedName("unread_events_count")
    private Long mUnreadCounter;


    public TGFeed() {
        super(TGCustomCacheObject.TGCacheObjectType.Feed);
    }

    /**
     * Get events assigned to feed
     *
     * @return events
     */
    public List<TGEvent> getEvents() {
        return mEvents;
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
     * Is object created from feed object or just as list of events? Feed contains events from
     * different users, where events list is always from one
     *
     * @return Is object deserialized from feed?
     */
    public Boolean isFeed() {
        return mIsFeed;
    }

    /**
     * Set information if object is pure-feed one or just list of objects
     *
     * @param isFeed
     *
     * @return Current object
     */
    @NonNull
    public TGFeed setIsFeed(boolean isFeed) {
        mIsFeed = isFeed;
        return this;
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
