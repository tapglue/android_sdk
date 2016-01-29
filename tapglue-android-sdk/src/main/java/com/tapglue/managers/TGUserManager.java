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

import com.tapglue.model.TGSocialConnections;
import com.tapglue.model.TGUser;
import com.tapglue.model.TGUsersList;
import com.tapglue.networking.requests.TGRequestCallback;

import java.util.List;

public interface TGUserManager {

    /**
     * Create user and login into Tapglue library
     *
     * @param user     Custom user data
     * @param callback Return callback
     */
    void createAndLoginUser(@NonNull TGUser user, @NonNull final TGRequestCallback<Boolean> callback);

    /**
     * Create user with selected params and login into Tapglue library This will send the password
     * encrypted with the PBKDF2 encryption
     *
     * @param email
     * @param password
     * @param callback
     */
    void createAndLoginUserWithEmail(@NonNull String email, @NonNull String password, @NonNull final TGRequestCallback<Boolean> callback);

    /**
     * Create user with selected params and login into Tapglue library This will send the password
     * encrypted with the PBKDF2 encryption
     *
     * @param userName
     * @param password
     * @param callback
     */
    void createAndLoginUserWithUsername(@NonNull String username, @NonNull String password, @NonNull final TGRequestCallback<Boolean> callback);

    /**
     * Create user with selected params and login into Tapglue library This will send the password
     * encrypted with the PBKDF2 encryption
     *
     * @param userName
     * @param password
     * @param email
     * @param callback
     */
    void createAndLoginUserWithUsernameAndMail(String userName, @NonNull String password, String email, @NonNull final TGRequestCallback<Boolean> callback);

    /**
     * Delete current user and logout
     *
     * @param callback
     */
    void deleteCurrentUser(@NonNull final TGRequestCallback<Boolean> callback);

    /**
     * Get current Tapglue user
     *
     * @return The current user, if present else null
     */
    @Nullable
    TGUser getCurrentUser();

    /**
     * Try to login user into Tapglue This will encrypt the password with PBKDF2 before sending it
     * over the wire
     *
     * @param userName
     * @param password
     * @param callback
     */
    void login(@NonNull String userName, @NonNull String password, @NonNull final TGRequestCallback<Boolean> callback);

    /**
     * Try to login user into Tapglue This will send the password as it's received, without any
     * further encryption
     *
     * @param user
     * @param callback
     */
    void login(@NonNull TGUser user, @NonNull final TGRequestCallback<Boolean> callback);

    /**
     * Try to login user into Tapglue This will send the password as it's received, without any
     * further encryption
     *
     * @param userName
     * @param password
     * @param callback
     */
    void loginWithUsernameOrEmailAndUnhashedPassword(String userName, @NonNull String password, String email, @NonNull final TGRequestCallback<Boolean> callback);

    /**
     * Logout from system
     *
     * @param callback
     */
    void logout(@NonNull final TGRequestCallback<Boolean> callback);

    /**
     * Get list of users who follow current user
     *
     * @param callback
     */
    void retrieveFollowersForCurrentUser(@NonNull final TGRequestCallback<TGUsersList> callback);

    /**
     * Get followers of selected user
     *
     * @param userId
     * @param callback
     */
    void retrieveFollowersForUser(@NonNull Long userId, @NonNull final TGRequestCallback<TGUsersList> callback);

    /**
     * Get list of users current user follows
     *
     * @param callback
     */
    void retrieveFollowsForCurrentUser(@NonNull final TGRequestCallback<TGUsersList> callback);

    /**
     * Get list of who selected user follows
     *
     * @param userId
     * @param callback
     */
    void retrieveFollowsForUser(@NonNull Long userId, @NonNull final TGRequestCallback<TGUsersList> callback);

    /**
     * Get friends of current user
     *
     * @param callback
     */
    void retrieveFriendsForCurrentUser(@NonNull final TGRequestCallback<TGUsersList> callback);

    /**
     * Get friends for selected user
     *
     * @param userId
     * @param callback
     */
    void retrieveFriendsForUser(@NonNull Long userId, @NonNull final TGRequestCallback<TGUsersList> callback);

    /**
     * Save changes to current user
     *
     * @param updated
     * @param callback
     */
    void saveChangesToCurrentUser(@NonNull TGUser updated, @NonNull final TGRequestCallback<Boolean> callback);

    /**
     * Search request
     *
     * @param searchCriteria
     * @param callback
     */
    void search(@NonNull String searchCriteria, @NonNull final TGRequestCallback<TGUsersList> callback);

    /**
     * Search with social platform ids
     *
     * @param socialIds
     * @param socialPlatform
     * @param callback
     */
    void searchUsersWithSocialUserIds(@NonNull String socialPlatform, List<String> socialIds, @NonNull final TGRequestCallback<TGUsersList> callback);

    /**
     * Search with emails
     *
     * @param searchCriteria
     * @param callback
     */
    void searchWithEmails(@NonNull List<String> searchCriteria, @NonNull final TGRequestCallback<TGUsersList> callback);

    /**
     * Update social connections
     *
     * @param socialData
     * @param callback
     */
    void socialConnections(@NonNull TGSocialConnections socialData, @NonNull final TGRequestCallback<TGUsersList> callback);
}
