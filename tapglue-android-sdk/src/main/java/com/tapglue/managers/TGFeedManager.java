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

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tapglue.Tapglue;
import com.tapglue.model.TGConnectionUsersList;
import com.tapglue.model.TGFeed;
import com.tapglue.model.TGFeedCount;
import com.tapglue.networking.requests.TGRequestCallback;
import com.tapglue.networking.requests.TGRequestErrorType;

public class TGFeedManager extends AbstractTGManager implements TGFeedManagerInterface {

    private static final String CACHE_KEY = "FEED_CACHE";

    public TGFeedManager(Tapglue tgInstance) {
        super(tgInstance);
    }

    /**
     * Get feed from cache
     *
     * @param returnMethod
     */
    @Override
    public void cachedFeedForCurrentUser(@NonNull TGRequestCallback<TGFeed> returnMethod) {
        if (tapglue.getUserManager().getCurrentUser() == null) {
            returnMethod.onRequestError(new TGRequestErrorType(TGRequestErrorType.ErrorType.USER_NOT_LOGGED_IN));
            return;
        }
        getCachedFeedIfAvailable(returnMethod);
    }

    /**
     * Return cached feed
     *
     * @param returnMethod
     */
    @Override
    public void getCachedFeedIfAvailable(@NonNull TGRequestCallback<TGFeed> returnMethod) {
        SharedPreferences cache = tapglue.getContext().getSharedPreferences(TGFeedManager.class.toString(), Context.MODE_PRIVATE);
        if (cache.contains(CACHE_KEY)) {
            TGFeed feed = new Gson().fromJson(cache.getString(CACHE_KEY, null), new TypeToken<TGFeed>() {
            }.getType());
            returnMethod.onRequestFinished(feed, false);
        }
        else {
            returnMethod.onRequestError(new TGRequestErrorType(TGRequestErrorType.ErrorType.NO_CACHE_OBJECT));
        }
    }

    /**
     * Get all event associated with current user
     *
     * @param returnMethod
     */
    @Override
    public void retrieveEventsForCurrentUser(@NonNull TGRequestCallback<TGFeed> returnMethod) {
        if (tapglue.getUserManager().getCurrentUser() == null) {
            returnMethod.onRequestError(new TGRequestErrorType(TGRequestErrorType.ErrorType.USER_NOT_LOGGED_IN));
            return;
        }
        tapglue.createRequest().getEvents(returnMethod);
    }

    /**
     * Get all events associated with user
     *
     * @param userId
     * @param returnMethod
     */
    @Override
    public void retrieveEventsForUser(@Nullable Long userId, @NonNull TGRequestCallback<TGFeed> returnMethod) {
        if (userId == null) {
            returnMethod.onRequestError(new TGRequestErrorType(TGRequestErrorType.ErrorType.NULL_INPUT));
            return;
        }
        else if (tapglue.getUserManager().getCurrentUser() == null) {
            returnMethod.onRequestError(new TGRequestErrorType(TGRequestErrorType.ErrorType.USER_NOT_LOGGED_IN));
            return;
        }
        tapglue.createRequest().getEvents(userId, returnMethod);
    }

    /**
     * Get live feed for current user
     *
     * @param returnMethod
     */
    @Override
    public void retrieveFeedForCurrentUser(@NonNull final TGRequestCallback<TGFeed> returnMethod) {
        if (tapglue.getUserManager().getCurrentUser() == null) {
            returnMethod.onRequestError(new TGRequestErrorType(TGRequestErrorType.ErrorType.USER_NOT_LOGGED_IN));
            return;
        }
        tapglue.createRequest().getFeed(new TGRequestCallback<TGFeed>() {
            @Override
            public boolean callbackIsEnabled() {
                return returnMethod.callbackIsEnabled();
            }

            @Override
            public void onRequestError(TGRequestErrorType cause) {
                returnMethod.onRequestError(cause);
            }

            @Override
            public void onRequestFinished(TGFeed output, boolean changeDoneOnline) {
                saveFeedToCache(output);
                returnMethod.onRequestFinished(output, changeDoneOnline);
            }
        });
    }

    /**
     * Get count of unread feed elements
     *
     * @param returnMethod
     */
    @Override
    public void retrieveUnreadCountForCurrentUser(@NonNull TGRequestCallback<TGFeedCount> returnMethod) {
        if (tapglue.getUserManager().getCurrentUser() == null) {
            returnMethod.onRequestError(new TGRequestErrorType(TGRequestErrorType.ErrorType.USER_NOT_LOGGED_IN));
            return;
        }
        tapglue.createRequest().getFeedCount(returnMethod);
    }

    /**
     * Get all unread feed
     *
     * @param returnMethod
     */
    @Override
    public void retrieveUnreadFeedForCurrentUser(@NonNull TGRequestCallback<TGFeed> returnMethod) {
        if (tapglue.getUserManager().getCurrentUser() == null) {
            returnMethod.onRequestError(new TGRequestErrorType(TGRequestErrorType.ErrorType.USER_NOT_LOGGED_IN));
            return;
        }
        tapglue.createRequest().getUnreadFeed(returnMethod);
    }

    /**
     * Save feed to cache
     *
     * @param output
     */
    private void saveFeedToCache(@Nullable TGFeed output) {
        SharedPreferences cache = tapglue.getContext().getSharedPreferences(TGFeedManager.class.toString(), Context.MODE_PRIVATE);
        if (output == null) {
            if (cache.contains(CACHE_KEY)) { cache.edit().remove(CACHE_KEY).apply(); }
        }
        else {
            cache.edit().putString(CACHE_KEY, new Gson().toJson(output, new TypeToken<TGFeed>() {
            }.getType())).apply();
        }
    }
}
