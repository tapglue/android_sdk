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

public interface TGFeedManagerInterface {
    void cachedFeedForCurrentUser(@NonNull final TGRequestCallback<TGFeed> callback);

    void getCachedFeedIfAvailable(@NonNull final TGRequestCallback<TGFeed> callback);

    void retrieveEventsFeedForCurrentUser(@NonNull final TGRequestCallback<TGEventsList> callback);

    void retrieveEventsFeedForCurrentUser(@Nullable TGQuery whereParameters, @NonNull final TGRequestCallback<TGEventsList> callback);

    void retrieveEventsForCurrentUser(@NonNull final TGRequestCallback<TGEventsList> callback);

    void retrieveEventsForCurrentUser(@Nullable TGQuery whereParameters, @NonNull final TGRequestCallback<TGEventsList> callback);

    void retrieveEventsForUser(@NonNull Long userId, @NonNull final TGRequestCallback<TGEventsList> callback);

    void retrieveEventsForUser(@NonNull Long userId, @Nullable TGQuery whereParameters, @NonNull final TGRequestCallback<TGEventsList> callback);

    void retrieveNewsFeedForCurrentUser(@NonNull final TGRequestCallback<TGFeed> callback);

    void retrieveNewsFeedForCurrentUser(@NonNull TGQuery whereParameters, @NonNull final TGRequestCallback<TGFeed> callback);

    void retrievePostsFeedForCurrentUser(@NonNull final TGRequestCallback<TGPostsList> callback);

    void retrievePostsForCurrentUser(@NonNull final TGRequestCallback<TGPostsList> callback);

    void retrievePostsForUser(@NonNull Long userId, @NonNull final TGRequestCallback<TGPostsList> callback);

    void retrieveUnreadCountForCurrentUser(@NonNull final TGRequestCallback<TGFeedCount> callback);

    void retrieveUnreadFeedForCurrentUser(@NonNull final TGRequestCallback<TGFeed> callback);
}
