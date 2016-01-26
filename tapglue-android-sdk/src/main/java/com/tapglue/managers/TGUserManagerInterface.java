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
    void createAndLoginUser(TGUser user, TGRequestCallback<Boolean> callback);

    void createAndLoginUserWithUsernameAndMail(String userName, String password, String email, TGRequestCallback<Boolean> callback);

    void createAndLoginUserWithUsername(String username, String password, TGRequestCallback<Boolean> callback);

    void createAndLoginUserWithEmail(String email, String password, TGRequestCallback<Boolean> callback);

    void deleteCurrentUser(TGRequestCallback<Boolean> callback);

    @Nullable
    TGUser getCurrentUser();

    void login(String userName, String password, TGRequestCallback<Boolean> callback);

    void loginWithUsernameOrEmailAndUnhashedPassword(String userName, String password, String email, TGRequestCallback<Boolean> callback);

    void login(@NonNull TGUser user, @NonNull final TGRequestCallback<Boolean> callback);

    void logout(TGRequestCallback<Boolean> callback);

    void retrieveFollowersForCurrentUser(TGRequestCallback<TGConnectionUsersList> callback);

    void retrieveFollowersForUser(Long userId, TGRequestCallback<TGConnectionUsersList> callback);

    void retrieveFollowsForCurrentUser(TGRequestCallback<TGConnectionUsersList> callback);

    void retrieveFollowsForUser(Long userId, TGRequestCallback<TGConnectionUsersList> callback);

    void retrieveFriendsForCurrentUser(TGRequestCallback<TGConnectionUsersList> callback);

    void retrieveFriendsForUser(Long userId, TGRequestCallback<TGConnectionUsersList> callback);

    void saveChangesToCurrentUser(TGUser updated, TGRequestCallback<Boolean> callback);

    void search(String searchCriteria, TGRequestCallback<TGConnectionUsersList> callback);

    void searchUsersWithSocialUserIds(String socialPlatform, List<String> socialIds, TGRequestCallback<TGConnectionUsersList> callback);

    void searchWithEmails(List<String> searchCriteria, TGRequestCallback<TGConnectionUsersList> callback);

    void socialConnections(TGSocialConnections socialData, TGRequestCallback<TGConnectionUsersList> callback);
}
