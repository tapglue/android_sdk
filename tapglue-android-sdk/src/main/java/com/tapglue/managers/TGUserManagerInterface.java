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

import android.support.annotation.Nullable;

import com.tapglue.model.TGConnectionUsersList;
import com.tapglue.model.TGSocialConnections;
import com.tapglue.model.TGUser;
import com.tapglue.networking.requests.TGRequestCallback;

import java.util.List;

public interface TGUserManagerInterface {
    void createAndLoginUser(TGUser user, TGRequestCallback<Boolean> callback);

    void createAndLoginUserWithUsernameAndMail(String userName, String password, String email, TGRequestCallback<Boolean> callback);

    void deleteCurrentUser(TGRequestCallback<Boolean> output);

    @Nullable
    TGUser getCurrentUser();

    void login(String userName, String password, TGRequestCallback<Boolean> output);

    void loginWithUsernameOrEmailAndUnhashedPassword(String userName, String password, String email, TGRequestCallback<Boolean> callback);

    void logout(TGRequestCallback<Boolean> output);

    void retrieveFollowersForCurrentUser(TGRequestCallback<TGConnectionUsersList> returnMethod);

    void retrieveFollowersForUser(Long userId, TGRequestCallback<TGConnectionUsersList> returnMethod);

    void retrieveFollowsForCurrentUser(TGRequestCallback<TGConnectionUsersList> returnMethod);

    void retrieveFollowsForUser(Long userId, TGRequestCallback<TGConnectionUsersList> returnMethod);

    void retrieveFriendsForCurrentUser(TGRequestCallback<TGConnectionUsersList> returnMethod);

    void retrieveFriendsForUser(Long userId, TGRequestCallback<TGConnectionUsersList> returnMethod);

    void saveChangesToCurrentUser(TGUser updated, TGRequestCallback<Boolean> output);

    void search(String searchCriteria, TGRequestCallback<TGConnectionUsersList> output);

    void searchUsersWithSocialUserIds(String socialPlatform, List<String> socialIds, TGRequestCallback<TGConnectionUsersList> output);

    void searchWithEmails(List<String> searchCriteria, TGRequestCallback<TGConnectionUsersList> output);

    void socialConnections(TGSocialConnections socialData, TGRequestCallback<TGConnectionUsersList> output);
}
