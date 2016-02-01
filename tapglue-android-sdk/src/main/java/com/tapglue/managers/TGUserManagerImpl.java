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
import android.text.TextUtils;

import com.google.gson.Gson;
import com.tapglue.Tapglue;
import com.tapglue.model.TGRecommendedUsers.TGRecommendationPeriod;
import com.tapglue.model.TGRecommendedUsers.TGRecommendationType;
import com.tapglue.model.TGSocialConnections;
import com.tapglue.model.TGUser;
import com.tapglue.model.TGUsersList;
import com.tapglue.networking.requests.TGRequestCallback;
import com.tapglue.networking.requests.TGRequestErrorType;

import java.util.List;

public class TGUserManagerImpl extends AbstractTGManager implements TGUserManager {
    private static final String CACHE_KEY = "USER_CACHE";

    /**
     * Currently logged in user
     */
    @Nullable
    private TGUser currentUser;

    public TGUserManagerImpl(Tapglue instance) {
        super(instance);
        tryToLoadUserFromCache();
    }

    @Override
    public void createAndLoginUser(@NonNull TGUser user, @NonNull final TGRequestCallback<Boolean> callback) {
        if ((TextUtils.isEmpty(user.getUserName()) && TextUtils.isEmpty(user.getEmail())) || TextUtils.isEmpty(user.getPassword())) {
            callback.onRequestError(new TGRequestErrorType(TGRequestErrorType.ErrorType.NULL_INPUT));
            return;
        }

        instance.createRequest().createUser(user, new TGRequestCallback<TGUser>() {
            @Override
            public boolean callbackIsEnabled() {
                return callback.callbackIsEnabled();
            }

            @Override
            public void onRequestError(TGRequestErrorType cause) {
                callback.onRequestError(cause);
            }

            @Override
            public void onRequestFinished(TGUser output, boolean changeDoneOnline) {
                currentUser = output;
                saveCurrentUserToCache();
                callback.onRequestFinished(true, true);
            }
        });
    }

    @Override
    public void createAndLoginUserWithEmail(@NonNull String email, @NonNull String password, @NonNull final TGRequestCallback<Boolean> callback) {
        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
            callback.onRequestError(new TGRequestErrorType(TGRequestErrorType.ErrorType.NULL_INPUT));
            return;
        }

        instance.createRequest().createUser(new TGUser().setEmail(email).setPassword(password), new TGRequestCallback<TGUser>() {
            @Override
            public boolean callbackIsEnabled() {
                return callback.callbackIsEnabled();
            }

            @Override
            public void onRequestError(TGRequestErrorType cause) {
                callback.onRequestError(cause);
            }

            @Override
            public void onRequestFinished(TGUser output, boolean changeDoneOnline) {
                currentUser = output;
                saveCurrentUserToCache();
                callback.onRequestFinished(true, true);
            }
        });
    }

    @Override
    public void createAndLoginUserWithUsername(@NonNull String userName, @NonNull String password, @NonNull final TGRequestCallback<Boolean> callback) {
        if (TextUtils.isEmpty(userName) || TextUtils.isEmpty(password)) {
            callback.onRequestError(new TGRequestErrorType(TGRequestErrorType.ErrorType.NULL_INPUT));
            return;
        }

        instance.createRequest().createUser(new TGUser().setUserName(userName).setPassword(password), new TGRequestCallback<TGUser>() {
            @Override
            public boolean callbackIsEnabled() {
                return callback.callbackIsEnabled();
            }

            @Override
            public void onRequestError(TGRequestErrorType cause) {
                callback.onRequestError(cause);
            }

            @Override
            public void onRequestFinished(TGUser output, boolean changeDoneOnline) {
                currentUser = output;
                saveCurrentUserToCache();
                callback.onRequestFinished(true, true);
            }
        });
    }

    @Override
    public void createAndLoginUserWithUsernameAndMail(String userName, @NonNull String password, String email, @NonNull final TGRequestCallback<Boolean> callback) {
        if (TextUtils.isEmpty(userName) || TextUtils.isEmpty(password) || TextUtils.isEmpty(email)) {
            callback.onRequestError(new TGRequestErrorType(TGRequestErrorType.ErrorType.NULL_INPUT));
            return;
        }

        instance.createRequest().createUser(new TGUser().setUserName(userName).setPassword(password).setEmail(email), new TGRequestCallback<TGUser>() {
            @Override
            public boolean callbackIsEnabled() {
                return callback.callbackIsEnabled();
            }

            @Override
            public void onRequestError(TGRequestErrorType cause) {
                callback.onRequestError(cause);
            }

            @Override
            public void onRequestFinished(TGUser output, boolean changeDoneOnline) {
                currentUser = output;
                saveCurrentUserToCache();
                callback.onRequestFinished(true, true);
            }
        });
    }

    @Override
    public void deleteCurrentUser(@NonNull final TGRequestCallback<Boolean> callback) {
        if (currentUser == null) {
            callback.onRequestError(new TGRequestErrorType(TGRequestErrorType.ErrorType.USER_NOT_LOGGED_IN));
            return;
        }
        instance.createRequest().removeUser(currentUser, new TGRequestCallback<Object>() {
            @Override
            public boolean callbackIsEnabled() {
                return callback.callbackIsEnabled();
            }

            @Override
            public void onRequestError(TGRequestErrorType cause) {
                callback.onRequestError(cause);
            }

            @Override
            public void onRequestFinished(Object out, boolean changeDoneOnline) {
                currentUser = null;
                saveCurrentUserToCache();
                callback.onRequestFinished(true, true);
            }
        });
    }

    @Nullable
    @Override
    public TGUser getCurrentUser() {
        return currentUser;
    }

    @Override
    public void getRecommendedUsers(TGRecommendationType type, TGRecommendationPeriod period, @NonNull final TGRequestCallback<TGUsersList> callback) {
        if (currentUser == null) {
            callback.onRequestError(new TGRequestErrorType(TGRequestErrorType.ErrorType.USER_NOT_LOGGED_IN));
            return;
        }

        instance.createRequest().getRecommendedUsers(type, period, callback);
    }

    @Override
    public void login(@NonNull String userName, @NonNull String password, @NonNull final TGRequestCallback<Boolean> callback) {
        login(new TGUser().setUserName(userName).setPassword(password), callback);
    }

    @Override
    public void login(@NonNull TGUser user, @NonNull final TGRequestCallback<Boolean> callback) {
        if (!instance.isCorrectConfig()) {
            callback.onRequestError(new TGRequestErrorType(TGRequestErrorType.ErrorType.NO_TOKEN_FOUND));
            return;
        }

        instance.createRequest().login(user, new TGRequestCallback<TGUser>() {
            @Override
            public boolean callbackIsEnabled() {
                return callback.callbackIsEnabled();
            }

            @Override
            public void onRequestError(TGRequestErrorType cause) {
                callback.onRequestError(cause);
            }

            @Override
            public void onRequestFinished(TGUser userData, boolean changeDoneOnline) {
                currentUser = userData;
                saveCurrentUserToCache();
                callback.onRequestFinished(true, true);
            }
        });
    }

    @Override
    public void loginWithUsernameOrEmailAndUnhashedPassword(String userName, @NonNull String password, @NonNull String email, @NonNull final TGRequestCallback<Boolean> callback) {
        login(new TGUser().setUserName(userName).setEmail(email).setUnhashedPassword(password), callback);
    }

    @Override
    public void logout(@NonNull final TGRequestCallback<Boolean> callback) {
        if (currentUser == null) {
            callback.onRequestError(new TGRequestErrorType(TGRequestErrorType.ErrorType.USER_NOT_LOGGED_IN));
            return;
        }
        instance.createRequest().logout(new TGRequestCallback<Object>() {
            @Override
            public boolean callbackIsEnabled() {
                return callback.callbackIsEnabled();
            }

            @Override
            public void onRequestError(@NonNull TGRequestErrorType cause) {
                if (cause.getCode().intValue() != 1001) {
                    callback.onRequestError(cause);
                    return;
                }

                // "user not found on server"
                currentUser = null;
                saveCurrentUserToCache();
                callback.onRequestFinished(true, true);
            }

            @Override
            public void onRequestFinished(Object out, boolean changeDoneOnline) {
                currentUser = null;
                saveCurrentUserToCache();
                callback.onRequestFinished(true, true);
            }
        });
    }

    @Override
    public void retrieveFollowersForCurrentUser(@NonNull final TGRequestCallback<TGUsersList> callback) {
        if (currentUser == null) {
            callback.onRequestError(new TGRequestErrorType(TGRequestErrorType.ErrorType.USER_NOT_LOGGED_IN));
            return;
        }
        instance.createRequest().getCurrentUserFollowers(callback);
    }

    @Override
    public void retrieveFollowersForUser(@NonNull Long userId, @NonNull final TGRequestCallback<TGUsersList> callback) {
        if (currentUser == null) {
            callback.onRequestError(new TGRequestErrorType(TGRequestErrorType.ErrorType.USER_NOT_LOGGED_IN));
            return;
        }
        instance.createRequest().getUserFollowed(userId, callback);
    }

    @Override
    public void retrieveFollowsForCurrentUser(@NonNull final TGRequestCallback<TGUsersList> callback) {
        if (currentUser == null) {
            callback.onRequestError(new TGRequestErrorType(TGRequestErrorType.ErrorType.USER_NOT_LOGGED_IN));
            return;
        }
        instance.createRequest().getCurrentUserFollowed(callback);
    }

    @Override
    public void retrieveFollowsForUser(@NonNull Long userId, @NonNull final TGRequestCallback<TGUsersList> callback) {
        if (currentUser == null) {
            callback.onRequestError(new TGRequestErrorType(TGRequestErrorType.ErrorType.USER_NOT_LOGGED_IN));
            return;
        }
        instance.createRequest().getUserFollowers(userId, callback);
    }

    @Override
    public void retrieveFriendsForCurrentUser(@NonNull final TGRequestCallback<TGUsersList> callback) {
        if (currentUser == null) {
            callback.onRequestError(new TGRequestErrorType(TGRequestErrorType.ErrorType.USER_NOT_LOGGED_IN));
            return;
        }
        instance.createRequest().getCurrentUserFriends(callback);
    }

    @Override
    public void retrieveFriendsForUser(@NonNull Long userId, @NonNull final TGRequestCallback<TGUsersList> callback) {
        if (currentUser == null) {
            callback.onRequestError(new TGRequestErrorType(TGRequestErrorType.ErrorType.USER_NOT_LOGGED_IN));
            return;
        }
        instance.createRequest().getUserFriends(userId, callback);
    }

    @Override
    public void saveChangesToCurrentUser(@NonNull TGUser updated, @NonNull final TGRequestCallback<Boolean> callback) {
        if (currentUser == null) {
            callback.onRequestError(new TGRequestErrorType(TGRequestErrorType.ErrorType.USER_NOT_LOGGED_IN));
            return;
        }

        if (updated.getID() == 0) {
            callback.onRequestError(new TGRequestErrorType(TGRequestErrorType.ErrorType.NULL_INPUT));
            return;
        }

        if (updated.getID().longValue() != currentUser.getID().longValue()) {
            callback.onRequestError(new TGRequestErrorType(TGRequestErrorType.ErrorType.UNSUPPORTED_INPUT));
            return;
        }

        instance.createRequest().updateUser(updated, new TGRequestCallback<TGUser>() {
            @Override
            public boolean callbackIsEnabled() {
                return callback.callbackIsEnabled();
            }

            @Override
            public void onRequestError(TGRequestErrorType cause) {
                callback.onRequestError(cause);
            }

            @Override
            public void onRequestFinished(TGUser user, boolean changeDoneOnline) {
                currentUser = user;
                saveCurrentUserToCache();
                callback.onRequestFinished(true, true);
            }
        });
    }

    /**
     * Save current user to cache
     */
    private void saveCurrentUserToCache() {
        SharedPreferences cache = instance.getContext().getSharedPreferences(TGUserManagerImpl.class.toString(), Context.MODE_PRIVATE);
        if (currentUser == null) {
            if (cache.contains(CACHE_KEY)) { cache.edit().remove(CACHE_KEY).apply(); }
        }
        else {
            cache.edit().putString(CACHE_KEY, new Gson().toJson(currentUser)).apply();
        }
    }

    @Override
    public void search(@NonNull String searchCriteria, @NonNull final TGRequestCallback<TGUsersList> callback) {
        if (currentUser == null) {
            callback.onRequestError(new TGRequestErrorType(TGRequestErrorType.ErrorType.USER_NOT_LOGGED_IN));
            return;
        }

        if (TextUtils.isEmpty(searchCriteria)) {
            callback.onRequestError(new TGRequestErrorType(TGRequestErrorType.ErrorType.NULL_INPUT));
            return;
        }

        instance.createRequest().search(searchCriteria, callback);
    }

    @Override
    public void searchUsersWithSocialUserIds(@NonNull String socialPlatform, List<String> socialIds, @NonNull final TGRequestCallback<TGUsersList> callback) {
        if (currentUser == null) {
            callback.onRequestError(new TGRequestErrorType(TGRequestErrorType.ErrorType.USER_NOT_LOGGED_IN));
            return;
        }

        if (TextUtils.isEmpty(socialPlatform) || socialIds == null || socialIds.size() == 0) {
            callback.onRequestError(new TGRequestErrorType(TGRequestErrorType.ErrorType.NULL_INPUT));
            return;
        }

        instance.createRequest().search(socialPlatform, socialIds, callback);
    }

    @Override
    public void searchWithEmails(@NonNull List<String> searchCriteria, @NonNull final TGRequestCallback<TGUsersList> callback) {
        if (currentUser == null) {
            callback.onRequestError(new TGRequestErrorType(TGRequestErrorType.ErrorType.USER_NOT_LOGGED_IN));
            return;
        }

        if (searchCriteria.size() == 0) {
            callback.onRequestError(new TGRequestErrorType(TGRequestErrorType.ErrorType.NULL_INPUT));
            return;
        }

        instance.createRequest().searchEmails(searchCriteria, callback);
    }

    @Override
    public void socialConnections(@NonNull TGSocialConnections socialData, @NonNull final TGRequestCallback<TGUsersList> callback) {
        if (currentUser == null) {
            callback.onRequestError(new TGRequestErrorType(TGRequestErrorType.ErrorType.USER_NOT_LOGGED_IN));
            return;
        }

        instance.createRequest().socialConnections(socialData, callback);
    }

    /**
     * Try to load user from cache
     */
    public void tryToLoadUserFromCache() {
        SharedPreferences cache = instance.getContext().getSharedPreferences(TGUserManagerImpl.class.toString(), Context.MODE_PRIVATE);
        if (cache.contains(CACHE_KEY)) {
            currentUser = new Gson().fromJson(cache.getString(CACHE_KEY, null), TGUser.class);
        }
    }
}
