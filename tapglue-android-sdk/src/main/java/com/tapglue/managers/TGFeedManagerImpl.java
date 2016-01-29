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

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tapglue.Tapglue;
import com.tapglue.model.TGEventsList;
import com.tapglue.model.TGFeed;
import com.tapglue.model.TGFeedCount;
import com.tapglue.model.TGPostsList;
import com.tapglue.model.queries.TGQuery;
import com.tapglue.networking.requests.TGRequestCallback;
import com.tapglue.networking.requests.TGRequestErrorType;

public class TGFeedManagerImpl extends AbstractTGManager implements TGFeedManager {

    private static final String CACHE_KEY = "FEED_CACHE";

    public TGFeedManagerImpl(Tapglue tgInstance) {
        super(tgInstance);
    }

    /**
     * Get feed from cache
     *
     * @param callback
     */
    @Override
    public void cachedFeedForCurrentUser(@NonNull final TGRequestCallback<TGFeed> callback) {
        if (tapglue.getUserManager().getCurrentUser() == null) {
            callback.onRequestError(new TGRequestErrorType(TGRequestErrorType.ErrorType.USER_NOT_LOGGED_IN));
            return;
        }
        getCachedFeedIfAvailable(callback);
    }

    /**
     * Return cached feed
     *
     * @param callback
     */
    @Override
    public void getCachedFeedIfAvailable(@NonNull final TGRequestCallback<TGFeed> callback) {
        SharedPreferences cache = tapglue.getContext().getSharedPreferences(TGFeedManagerImpl.class.toString(), Context.MODE_PRIVATE);
        if (!cache.contains(CACHE_KEY)) {
            callback.onRequestError(new TGRequestErrorType(TGRequestErrorType.ErrorType.NO_CACHE_OBJECT));
            return;
        }

        TGFeed feed = new Gson().fromJson(cache.getString(CACHE_KEY, null), new TypeToken<TGFeed>() {}.getType());
        callback.onRequestFinished(feed, false);
    }

    @Override
    public void retrieveEventsFeedForCurrentUser(@NonNull final TGRequestCallback<TGEventsList> callback) {
        retrieveEventsFeedForCurrentUser(null, callback);
    }

    @Override
    public void retrieveEventsFeedForCurrentUser(@Nullable TGQuery whereParameters, @NonNull final TGRequestCallback<TGEventsList> callback) {
        if (tapglue.getUserManager().getCurrentUser() == null) {
            callback.onRequestError(new TGRequestErrorType(TGRequestErrorType.ErrorType.USER_NOT_LOGGED_IN));
            return;
        }
        tapglue.createRequest().getEvents(whereParameters, callback);
    }

    @Override
    public void retrieveEventsForCurrentUser(@NonNull final TGRequestCallback<TGEventsList> callback) {
        retrieveEventsForCurrentUser(null, callback);
    }

    @Override
    public void retrieveEventsForCurrentUser(@Nullable TGQuery whereParameters, @NonNull final TGRequestCallback<TGEventsList> callback) {

        if (tapglue.getUserManager().getCurrentUser() == null) {
            callback.onRequestError(new TGRequestErrorType(TGRequestErrorType.ErrorType.USER_NOT_LOGGED_IN));
            return;
        }
        tapglue.createRequest().getEvents(whereParameters, callback);
    }

    @Override
    public void retrieveEventsForUser(@NonNull Long userId, @NonNull final TGRequestCallback<TGEventsList> callback) {
        retrieveEventsForUser(userId, null, callback);
    }

    @Override
    public void retrieveEventsForUser(@NonNull Long userId, TGQuery whereParameters, @NonNull final TGRequestCallback<TGEventsList> callback) {
        if (userId == null) {
            callback.onRequestError(new TGRequestErrorType(TGRequestErrorType.ErrorType.NULL_INPUT));
            return;
        }
        else if (tapglue.getUserManager().getCurrentUser() == null) {
            callback.onRequestError(new TGRequestErrorType(TGRequestErrorType.ErrorType.USER_NOT_LOGGED_IN));
            return;
        }
        tapglue.createRequest().getEvents(userId, whereParameters, callback);
    }

    @Override
    public void retrieveNewsFeedForCurrentUser(@NonNull final TGRequestCallback<TGFeed> callback) {
        retrieveNewsFeedForCurrentUser(null, callback);
    }

    @Override
    public void retrieveNewsFeedForCurrentUser(@Nullable TGQuery whereParameters, @NonNull final TGRequestCallback<TGFeed> callback) {
        tapglue.createRequest().getFeed(whereParameters, new TGRequestCallback<TGFeed>() {
            @Override
            public boolean callbackIsEnabled() {
                return callback.callbackIsEnabled();
            }

            @Override
            public void onRequestError(TGRequestErrorType cause) {
                callback.onRequestError(cause);
            }

            @Override
            public void onRequestFinished(TGFeed output, boolean changeDoneOnline) {
                saveFeedToCache(output);
                callback.onRequestFinished(output, changeDoneOnline);
            }
        });
    }

    @Override
    public void retrievePostsFeedForCurrentUser(@NonNull final TGRequestCallback<TGPostsList> callback) {
        if (tapglue.getUserManager().getCurrentUser() == null) {
            callback.onRequestError(new TGRequestErrorType(TGRequestErrorType.ErrorType.USER_NOT_LOGGED_IN));
            return;
        }
        tapglue.createRequest().getMyPosts(callback);
    }

    @Override
    public void retrievePostsForCurrentUser(@NonNull final TGRequestCallback<TGPostsList> callback) {
        if (tapglue.getUserManager().getCurrentUser() == null) {
            callback.onRequestError(new TGRequestErrorType(TGRequestErrorType.ErrorType.USER_NOT_LOGGED_IN));
            return;
        }
        tapglue.createRequest().getMyPosts(callback);
    }

    @Override
    public void retrievePostsForUser(@NonNull Long userId, @NonNull final TGRequestCallback<TGPostsList> callback) {
        if (userId == null) {
            callback.onRequestError(new TGRequestErrorType(TGRequestErrorType.ErrorType.NULL_INPUT));
            return;
        }
        else if (tapglue.getUserManager().getCurrentUser() == null) {
            callback.onRequestError(new TGRequestErrorType(TGRequestErrorType.ErrorType.USER_NOT_LOGGED_IN));
            return;
        }
        tapglue.createRequest().getUserPosts(userId, callback);
    }

    @Override
    public void retrieveUnreadCountForCurrentUser(@NonNull final TGRequestCallback<TGFeedCount> callback) {
        if (tapglue.getUserManager().getCurrentUser() == null) {
            callback.onRequestError(new TGRequestErrorType(TGRequestErrorType.ErrorType.USER_NOT_LOGGED_IN));
            return;
        }
        tapglue.createRequest().getFeedCount(callback);
    }

    @Override
    public void retrieveUnreadFeedForCurrentUser(@NonNull final TGRequestCallback<TGFeed> callback) {
        if (tapglue.getUserManager().getCurrentUser() == null) {
            callback.onRequestError(new TGRequestErrorType(TGRequestErrorType.ErrorType.USER_NOT_LOGGED_IN));
            return;
        }
        tapglue.createRequest().getUnreadFeed(callback);
    }

    /**
     * Save feed to cache
     *
     * @param output
     */
    private void saveFeedToCache(@Nullable TGFeed output) {
        SharedPreferences cache = tapglue.getContext().getSharedPreferences(TGFeedManagerImpl.class.toString(), Context.MODE_PRIVATE);
        if (output == null) {
            if (cache.contains(CACHE_KEY)) {
                cache.edit().remove(CACHE_KEY).apply();
            }
        }
        else {
            cache.edit().putString(CACHE_KEY, new Gson().toJson(output, new TypeToken<TGFeed>() {
            }.getType())).apply();
        }
    }
}
