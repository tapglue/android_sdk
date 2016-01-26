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
import android.text.TextUtils;

import com.google.gson.Gson;
import com.tapglue.Tapglue;
import com.tapglue.model.TGConnectionUsersList;
import com.tapglue.model.TGSocialConnections;
import com.tapglue.model.TGUser;
import com.tapglue.networking.requests.TGRequestCallback;
import com.tapglue.networking.requests.TGRequestErrorType;
import com.tapglue.utils.TGPasswordHasher;

import java.util.List;

public class TGUserManager extends AbstractTGManager implements TGUserManagerInterface {
    private static final String CACHE_KEY = "USER_CACHE";

    /**
     * Currently logged in user
     */
    @Nullable
    private TGUser mCurrentUser;

    public TGUserManager(Tapglue tgInstance) {
        super(tgInstance);
        tryToLoadUserFromCache();
    }

    /**
     * Create user and login into Tapglue library
     *
     * @param user     Custom user data
     * @param callback Return callback
     */
    @Override
    public void createAndLoginUser(@Nullable TGUser user, @NonNull final TGRequestCallback<Boolean> callback) {
        if (user == null || (TextUtils.isEmpty(user.getUserName()) && TextUtils.isEmpty(user.getEmail())) || TextUtils.isEmpty(user.getPassword())) {
            callback.onRequestError(new TGRequestErrorType(TGRequestErrorType.ErrorType.NULL_INPUT));
            return;
        }

        tapglue.createRequest().createUser(user, new TGRequestCallback<TGUser>() {
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
                mCurrentUser = output;
                saveCurrentUserToCache();
                callback.onRequestFinished(true, true);
            }
        });
    }

    /**
     * Create user with selected params and login into Tapglue library This will send the password
     * encrypted with the PBKDF2 encryption
     *
     * @param userName
     * @param password
     * @param email
     * @param callback
     */
    @Override
    public void createAndLoginUserWithUsernameAndMail(String userName, @NonNull String password, String email, @NonNull final TGRequestCallback<Boolean> callback) {
        if (TextUtils.isEmpty(userName) || TextUtils.isEmpty(password) || TextUtils.isEmpty(email)) {
            callback.onRequestError(new TGRequestErrorType(TGRequestErrorType.ErrorType.NULL_INPUT));
            return;
        }

        tapglue.createRequest().createUser(new TGUser().setUserName(userName).setPassword(password).setEmail(email), new TGRequestCallback<TGUser>() {
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
                mCurrentUser = output;
                saveCurrentUserToCache();
                callback.onRequestFinished(true, true);
            }
        });
    }

    /**
     * Create user with selected params and login into Tapglue library This will send the password
     * encrypted with the PBKDF2 encryption
     *
     * @param userName
     * @param password
     * @param callback
     */
    @Override
    public void createAndLoginUserWithUsername(String userName, @NonNull String password, @NonNull final TGRequestCallback<Boolean> callback) {
        if (TextUtils.isEmpty(userName) || TextUtils.isEmpty(password)) {
            callback.onRequestError(new TGRequestErrorType(TGRequestErrorType.ErrorType.NULL_INPUT));
            return;
        }

        tapglue.createRequest().createUser(new TGUser().setUserName(userName).setPassword(password), new TGRequestCallback<TGUser>() {
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
                mCurrentUser = output;
                saveCurrentUserToCache();
                callback.onRequestFinished(true, true);
            }
        });
    }

    /**
     * Create user with selected params and login into Tapglue library This will send the password
     * encrypted with the PBKDF2 encryption
     *
     * @param email
     * @param password
     * @param callback
     */
    @Override
    public void createAndLoginUserWithEmail(String email, @NonNull String password, @NonNull final TGRequestCallback<Boolean> callback) {
        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
            callback.onRequestError(new TGRequestErrorType(TGRequestErrorType.ErrorType.NULL_INPUT));
            return;
        }

        tapglue.createRequest().createUser(new TGUser().setEmail(email).setPassword(password), new TGRequestCallback<TGUser>() {
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
                mCurrentUser = output;
                saveCurrentUserToCache();
                callback.onRequestFinished(true, true);
            }
        });
    }

    /**
     * Delete current user and logout
     *
     * @param callback
     */
    @Override
    public void deleteCurrentUser(@NonNull final TGRequestCallback<Boolean> callback) {
        if (mCurrentUser == null) {
            callback.onRequestError(new TGRequestErrorType(TGRequestErrorType.ErrorType.USER_NOT_LOGGED_IN));
            return;
        }
        tapglue.createRequest().removeUser(mCurrentUser, new TGRequestCallback<Object>() {
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
                mCurrentUser = null;
                saveCurrentUserToCache();
                callback.onRequestFinished(true, true);
            }
        });
    }

    /**
     * Get current Tapglue user
     *
     * @return
     */
    @Nullable
    @Override
    public TGUser getCurrentUser() {
        return mCurrentUser;
    }

    /**
     * Try to login user into Tapglue This will encrypt the password with PBKDF2 before sending it
     * over the wire
     *
     * @param userName
     * @param password
     * @param callback
     */
    @Override
    public void login(String userName, @NonNull String password, @NonNull final TGRequestCallback<Boolean> callback) {
        login(new TGUser().setUserName(userName).setPassword(password), callback);
    }

    /**
     * Try to login user into Tapglue This will send the password as it's received, without any
     * further encryption
     *
     * @param userName
     * @param password
     * @param callback
     */
    @Override
    public void loginWithUsernameOrEmailAndUnhashedPassword(String userName, String email, String password, @NonNull final TGRequestCallback<Boolean> callback) {
        login(new TGUser().setUserName(userName).setEmail(email).setUnhashedPassword(password), callback);
    }

    /**
     * Try to login user into Tapglue This will send the password as it's received, without any
     * further encryption
     *
     * @param userName
     * @param password
     * @param callback
     */
    @Override
    public void login(@NonNull TGUser user, @NonNull final TGRequestCallback<Boolean> callback) {
        if (!tapglue.isCorrectConfig()) {
            callback.onRequestError(new TGRequestErrorType(TGRequestErrorType.ErrorType.NO_TOKEN_FOUND));
            return;
        }

        tapglue.createRequest().login(user, new TGRequestCallback<TGUser>() {
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
                mCurrentUser = userData;
                saveCurrentUserToCache();
                callback.onRequestFinished(true, true);
            }
        });
    }

    /**
     * Logout from system
     *
     * @param callback
     */
    @Override
    public void logout(@NonNull final TGRequestCallback<Boolean> callback) {
        if (mCurrentUser == null) {
            callback.onRequestError(new TGRequestErrorType(TGRequestErrorType.ErrorType.USER_NOT_LOGGED_IN));
            return;
        }
        tapglue.createRequest().logout(new TGRequestCallback<Object>() {
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
                mCurrentUser = null;
                saveCurrentUserToCache();
                callback.onRequestFinished(true, true);
            }

            @Override
            public void onRequestFinished(Object out, boolean changeDoneOnline) {
                mCurrentUser = null;
                saveCurrentUserToCache();
                callback.onRequestFinished(true, true);
            }
        });
    }

    /**
     * Get list of users who follow current user
     *
     * @param callback
     */
    @Override
    public void retrieveFollowersForCurrentUser(@NonNull TGRequestCallback<TGConnectionUsersList> callback) {
        if (tapglue.getUserManager().getCurrentUser() == null) {
            callback.onRequestError(new TGRequestErrorType(TGRequestErrorType.ErrorType.USER_NOT_LOGGED_IN));
            return;
        }
        tapglue.createRequest().getCurrentUserFollowers(callback);
    }

    /**
     * Get followers of selected user
     *
     * @param userId
     * @param callback
     */
    @Override
    public void retrieveFollowersForUser(@Nullable Long userId, @NonNull TGRequestCallback<TGConnectionUsersList> callback) {
        if (userId == null) {
            callback.onRequestError(new TGRequestErrorType(TGRequestErrorType.ErrorType.NULL_INPUT));
            return;
        }
        else if (tapglue.getUserManager().getCurrentUser() == null) {
            callback.onRequestError(new TGRequestErrorType(TGRequestErrorType.ErrorType.USER_NOT_LOGGED_IN));
            return;
        }
        tapglue.createRequest().getUserFollowed(userId, callback);
    }

    /**
     * Get list of users current user follows
     *
     * @param callback
     */
    @Override
    public void retrieveFollowsForCurrentUser(@NonNull TGRequestCallback<TGConnectionUsersList> callback) {
        if (tapglue.getUserManager().getCurrentUser() == null) {
            callback.onRequestError(new TGRequestErrorType(TGRequestErrorType.ErrorType.USER_NOT_LOGGED_IN));
            return;
        }
        tapglue.createRequest().getCurrentUserFollowed(callback);
    }

    /**
     * Get list of who selected user follows
     *
     * @param userId
     * @param callback
     */
    @Override
    public void retrieveFollowsForUser(@Nullable Long userId, @NonNull TGRequestCallback<TGConnectionUsersList> callback) {
        if (userId == null) {
            callback.onRequestError(new TGRequestErrorType(TGRequestErrorType.ErrorType.NULL_INPUT));
            return;
        }
        else if (tapglue.getUserManager().getCurrentUser() == null) {
            callback.onRequestError(new TGRequestErrorType(TGRequestErrorType.ErrorType.USER_NOT_LOGGED_IN));
            return;
        }
        tapglue.createRequest().getUserFollowers(userId, callback);
    }

    /**
     * Get friends of current user
     *
     * @param callback
     */
    @Override
    public void retrieveFriendsForCurrentUser(@NonNull TGRequestCallback<TGConnectionUsersList> callback) {
        if (tapglue.getUserManager().getCurrentUser() == null) {
            callback.onRequestError(new TGRequestErrorType(TGRequestErrorType.ErrorType.USER_NOT_LOGGED_IN));
            return;
        }
        tapglue.createRequest().getCurrentUserFriends(callback);
    }

    /**
     * Get friends for selected user
     *
     * @param userId
     * @param callback
     */
    @Override
    public void retrieveFriendsForUser(@Nullable Long userId, @NonNull TGRequestCallback<TGConnectionUsersList> callback) {
        if (userId == null) {
            callback.onRequestError(new TGRequestErrorType(TGRequestErrorType.ErrorType.NULL_INPUT));
            return;
        }
        else if (tapglue.getUserManager().getCurrentUser() == null) {
            callback.onRequestError(new TGRequestErrorType(TGRequestErrorType.ErrorType.USER_NOT_LOGGED_IN));
            return;
        }
        tapglue.createRequest().getUserFriends(userId, callback);
    }

    /**
     * Save changes to current user
     *
     * @param updated
     * @param callback
     */
    @Override
    public void saveChangesToCurrentUser(@Nullable final TGUser updated, @NonNull final TGRequestCallback<Boolean> callback) {
        if (mCurrentUser == null) {
            callback.onRequestError(new TGRequestErrorType(TGRequestErrorType.ErrorType.USER_NOT_LOGGED_IN));
            return;
        }

        if (updated == null || updated.getID() == 0) {
            callback.onRequestError(new TGRequestErrorType(TGRequestErrorType.ErrorType.NULL_INPUT));
            return;
        }

        if (updated.getID().longValue() != mCurrentUser.getID().longValue()) {
            callback.onRequestError(new TGRequestErrorType(TGRequestErrorType.ErrorType.UNSUPPORTED_INPUT));
            return;
        }

        tapglue.createRequest().updateUser(updated, new TGRequestCallback<TGUser>() {
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
                mCurrentUser = user;
                saveCurrentUserToCache();
                callback.onRequestFinished(true, true);
            }
        });
    }

    /**
     * Save current user to cache
     */
    public void saveCurrentUserToCache() {
        SharedPreferences cache = tapglue.getContext().getSharedPreferences(TGUserManager.class.toString(), Context.MODE_PRIVATE);
        if (mCurrentUser == null) {
            if (cache.contains(CACHE_KEY)) { cache.edit().remove(CACHE_KEY).apply(); }
        }
        else {
            cache.edit().putString(CACHE_KEY, new Gson().toJson(mCurrentUser)).apply();
        }
    }

    /**
     * Search request
     *
     * @param searchCriteria
     * @param callback
     */
    @Override
    public void search(String searchCriteria, @NonNull TGRequestCallback<TGConnectionUsersList> callback) {
        if (mCurrentUser == null) {
            callback.onRequestError(new TGRequestErrorType(TGRequestErrorType.ErrorType.USER_NOT_LOGGED_IN));
            return;
        }

        if (TextUtils.isEmpty(searchCriteria)) {
            callback.onRequestError(new TGRequestErrorType(TGRequestErrorType.ErrorType.NULL_INPUT));
            return;
        }

        String searchString = "_TG_S1_" + searchCriteria;
        tapglue.createRequest().search(searchString, callback);
    }

    /**
     * Search with social platform ids
     *
     * @param socialIds
     * @param socialPlatform
     * @param callback
     */
    @Override
    public void searchUsersWithSocialUserIds(String socialPlatform, @Nullable List<String> socialIds, @NonNull TGRequestCallback<TGConnectionUsersList> callback) {
        if (mCurrentUser == null) {
            callback.onRequestError(new TGRequestErrorType(TGRequestErrorType.ErrorType.USER_NOT_LOGGED_IN));
            return;
        }

        if (TextUtils.isEmpty(socialPlatform) || socialIds == null || socialIds.size() == 0) {
            callback.onRequestError(new TGRequestErrorType(TGRequestErrorType.ErrorType.NULL_INPUT));
            return;
        }

        tapglue.createRequest().search(socialPlatform, socialIds, callback);
    }

    /**
     * Search with emails
     *
     * @param searchCriteria
     * @param callback
     */
    @Override
    public void searchWithEmails(@Nullable List<String> searchCriteria, @NonNull TGRequestCallback<TGConnectionUsersList> callback) {
        if (mCurrentUser == null) {
            callback.onRequestError(new TGRequestErrorType(TGRequestErrorType.ErrorType.USER_NOT_LOGGED_IN));
            return;
        }

        if (searchCriteria == null || searchCriteria.size() == 0) {
            callback.onRequestError(new TGRequestErrorType(TGRequestErrorType.ErrorType.NULL_INPUT));
            return;
        }

        tapglue.createRequest().searchEmails(searchCriteria, callback);
    }

    /**
     * Update social connections
     *
     * @param socialData
     * @param callback
     */
    @Override
    public void socialConnections(@Nullable TGSocialConnections socialData, @NonNull TGRequestCallback<TGConnectionUsersList> callback) {
        if (mCurrentUser == null) {
            callback.onRequestError(new TGRequestErrorType(TGRequestErrorType.ErrorType.USER_NOT_LOGGED_IN));
            return;
        }

        if (socialData == null) {
            callback.onRequestError(new TGRequestErrorType(TGRequestErrorType.ErrorType.NULL_INPUT));
            return;
        }

        tapglue.createRequest().socialConnections(socialData, callback);
    }

    /**
     * Try to load user from cache
     */
    public void tryToLoadUserFromCache() {
        SharedPreferences cache = tapglue.getContext().getSharedPreferences(TGUserManager.class.toString(), Context.MODE_PRIVATE);
        if (cache.contains(CACHE_KEY)) {
            mCurrentUser = new Gson().fromJson(cache.getString(CACHE_KEY, null), TGUser.class);
        }
    }
}
