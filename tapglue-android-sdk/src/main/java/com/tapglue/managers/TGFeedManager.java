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

package com.tapglue.managers;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.tapglue.model.TGEventsList;
import com.tapglue.model.TGFeed;
import com.tapglue.model.TGFeedCount;
import com.tapglue.model.TGPostsList;
import com.tapglue.model.queries.TGQuery;
import com.tapglue.networking.requests.TGRequestCallback;

public interface TGFeedManager {

    /**
     * Get feed from cache
     *
     * @param callback
     */
    void cachedFeedForCurrentUser(@NonNull final TGRequestCallback<TGFeed> callback);

    /**
     * Return cached feed
     *
     * @param callback
     */
    void getCachedFeedIfAvailable(@NonNull final TGRequestCallback<TGFeed> callback);

    /**
     * Get all events and posts associated with current user
     *
     * @param callback
     */
    void retrieveEventsFeedForCurrentUser(@NonNull final TGRequestCallback<TGEventsList> callback);

    /**
     * Retrieve the event feed for the current user
     *
     * @param whereParameters
     * @param callback
     */
    void retrieveEventsFeedForCurrentUser(@Nullable TGQuery whereParameters, @NonNull final TGRequestCallback<TGEventsList> callback);

    /**
     * Get all events associated with current user Warning, Posts in TGEventsList output object
     * will
     * be empty as this method returns only events!
     *
     * @param callback
     */
    void retrieveEventsForCurrentUser(@NonNull final TGRequestCallback<TGEventsList> callback);

    /**
     * Retrieve the events for the current user
     *
     * @param whereParameters
     * @param callback
     */
    void retrieveEventsForCurrentUser(@Nullable TGQuery whereParameters, @NonNull final TGRequestCallback<TGEventsList> callback);

    /**
     * Get all events associated with user Warning, Posts in TGEventsList output object will be
     * empty as this method returns only events!
     *
     * @param userId
     * @param callback
     */
    void retrieveEventsForUser(@NonNull Long userId, @NonNull final TGRequestCallback<TGEventsList> callback);

    /**
     * Retrieve the events for a certain user
     *
     * @param userId
     * @param whereParameters
     * @param callback
     */
    void retrieveEventsForUser(@NonNull Long userId, @Nullable TGQuery whereParameters, @NonNull final TGRequestCallback<TGEventsList> callback);

    /**
     * Get live feed for current user
     *
     * @param callback
     */
    void retrieveNewsFeedForCurrentUser(@NonNull final TGRequestCallback<TGFeed> callback);

    /**
     * Retrieve the news feed of the current user
     *
     * @param whereParameters
     * @param callback
     */
    void retrieveNewsFeedForCurrentUser(@NonNull TGQuery whereParameters, @NonNull final TGRequestCallback<TGFeed> callback);

    /**
     * Get all events and posts associated with current user
     *
     * @param callback
     */
    void retrievePostsFeedForCurrentUser(@NonNull final TGRequestCallback<TGPostsList> callback);

    /**
     * Get all posts associated with current user
     *
     * @param callback
     */
    void retrievePostsForCurrentUser(@NonNull final TGRequestCallback<TGPostsList> callback);

    /**
     * Get all posts associated with user
     *
     * @param userId
     * @param callback
     */
    void retrievePostsForUser(@NonNull Long userId, @NonNull final TGRequestCallback<TGPostsList> callback);

    /**
     * Get count of unread feed elements
     *
     * @param callback
     */
    void retrieveUnreadCountForCurrentUser(@NonNull final TGRequestCallback<TGFeedCount> callback);

    /**
     * Get all unread feed
     *
     * @param callback
     */
    void retrieveUnreadFeedForCurrentUser(@NonNull final TGRequestCallback<TGFeed> callback);
}
