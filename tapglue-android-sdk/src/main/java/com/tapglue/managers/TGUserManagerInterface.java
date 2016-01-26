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

import com.tapglue.model.TGConnectionUsersList;
import com.tapglue.model.TGSocialConnections;
import com.tapglue.model.TGUser;
import com.tapglue.networking.requests.TGRequestCallback;

import java.util.List;

public interface TGUserManagerInterface {
    void createAndLoginUser(@NonNull TGUser user, @NonNull final TGRequestCallback<Boolean> callback);

    void createAndLoginUserWithUsernameAndMail(String userName, @NonNull String password, String email, @NonNull final TGRequestCallback<Boolean> callback);

    void createAndLoginUserWithUsername(@NonNull String username, @NonNull String password, @NonNull final TGRequestCallback<Boolean> callback);

    void createAndLoginUserWithEmail(@NonNull String email, @NonNull String password, @NonNull final TGRequestCallback<Boolean> callback);

    void deleteCurrentUser(@NonNull final TGRequestCallback<Boolean> callback);

    @Nullable
    TGUser getCurrentUser();

    void login(@NonNull String userName, @NonNull String password, @NonNull final TGRequestCallback<Boolean> callback);

    void loginWithUsernameOrEmailAndUnhashedPassword(String userName, @NonNull String password, String email, @NonNull final TGRequestCallback<Boolean> callback);

    void login(@NonNull TGUser user, @NonNull final TGRequestCallback<Boolean> callback);

    void logout(@NonNull final TGRequestCallback<Boolean> callback);

    void retrieveFollowersForCurrentUser(@NonNull final TGRequestCallback<TGConnectionUsersList> callback);

    void retrieveFollowersForUser(@NonNull Long userId, @NonNull final TGRequestCallback<TGConnectionUsersList> callback);

    void retrieveFollowsForCurrentUser(@NonNull final TGRequestCallback<TGConnectionUsersList> callback);

    void retrieveFollowsForUser(@NonNull Long userId, @NonNull final TGRequestCallback<TGConnectionUsersList> callback);

    void retrieveFriendsForCurrentUser(@NonNull final TGRequestCallback<TGConnectionUsersList> callback);

    void retrieveFriendsForUser(@NonNull Long userId, @NonNull final TGRequestCallback<TGConnectionUsersList> callback);

    void saveChangesToCurrentUser(@NonNull TGUser updated, @NonNull final TGRequestCallback<Boolean> callback);

    void search(@NonNull String searchCriteria, @NonNull final TGRequestCallback<TGConnectionUsersList> callback);

    void searchUsersWithSocialUserIds(@NonNull String socialPlatform, List<String> socialIds, @NonNull final TGRequestCallback<TGConnectionUsersList> callback);

    void searchWithEmails(@NonNull List<String> searchCriteria, @NonNull final TGRequestCallback<TGConnectionUsersList> callback);

    void socialConnections(@NonNull TGSocialConnections socialData, @NonNull final TGRequestCallback<TGConnectionUsersList> callback);
}
