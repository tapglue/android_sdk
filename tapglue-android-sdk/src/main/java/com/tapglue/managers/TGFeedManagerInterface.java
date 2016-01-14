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
    void cachedFeedForCurrentUser(TGRequestCallback<TGFeed> returnMethod);

    void getCachedFeedIfAvailable(TGRequestCallback<TGFeed> returnMethod);

    void retrievePostsForCurrentUser(@NonNull TGRequestCallback<TGPostsList> returnMethod);

    void retrievePostsForUser(@Nullable Long userId, @NonNull TGRequestCallback<TGPostsList> returnMethod);

    void retrieveEventsForCurrentUser(TGRequestCallback<TGEventsList> returnMethod);

    void retrieveNewsFeedForCurrentUser(@NonNull TGRequestCallback<TGFeed> returnMethod);

    void retrievePostsFeedForCurrentUser(@NonNull TGRequestCallback<TGPostsList> returnMethod);

    void retrieveEventsFeedForCurrentUser(@NonNull TGRequestCallback<TGEventsList> returnMethod);

    void retrieveEventsForUser(Long userId, TGRequestCallback<TGEventsList> returnMethod);

    void retrieveUnreadCountForCurrentUser(TGRequestCallback<TGFeedCount> returnMethod);

    void retrieveUnreadFeedForCurrentUser(TGRequestCallback<TGFeed> returnMethod);

    void retrieveEventsFeedForCurrentUser(@NonNull TGRequestCallback<TGEventsList> returnMethod,TGQuery whereParameters);

    void retrieveEventsForUser(Long userId, TGRequestCallback<TGEventsList> returnMethod,TGQuery whereParameters);

    void retrieveEventsForCurrentUser(TGRequestCallback<TGEventsList> returnMethod,TGQuery whereParameters);

    void retrieveNewsFeedForCurrentUser(@NonNull TGRequestCallback<TGFeed> returnMethod,TGQuery whereParameters);
}
