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

import com.tapglue.model.TGConnection.TGConnectionType;
import com.tapglue.model.TGPendingConnections;
import com.tapglue.networking.requests.TGRequestCallback;

public interface TGConnectionManager {

    /**
     * Confirm a connection between the current user and the user
     *
     * @param userId
     * @param TGConnectionType
     * @param callback
     */
    void confirmConnection(@NonNull Long userId, @NonNull TGConnectionType TGConnectionType, @NonNull final TGRequestCallback<Boolean> callback);

    /**
     * Follow selected user
     *
     * @param userId
     * @param callback
     */
    void followUser(@NonNull Long userId, @NonNull final TGRequestCallback<Boolean> callback);

    /**
     * Add selected user to friends
     *
     * @param userId   user ID
     * @param callback return method
     */
    void friendUser(@NonNull Long userId, @NonNull final TGRequestCallback<Boolean> callback);

    /**
     * Get pending Connections
     */
    void getPendingConnections(@NonNull final TGRequestCallback<TGPendingConnections> callback);

    /**
     * Retrieve the confirmed connections for the current user
     *
     * @param callback
     */
    void retrieveConfirmedConnectionsForCurrentUser(@NonNull final TGRequestCallback<TGPendingConnections> callback);

    /**
     * Retrieve the connections in rejected state for the current user
     *
     * @param callback
     */
    void retrieveRejectedConnectionsForCurrentUser(@NonNull final TGRequestCallback<TGPendingConnections> callback);

    /**
     * Stop following selected user
     *
     * @param userId   user ID
     * @param callback return method
     */
    void unfollowUser(@NonNull Long userId, @NonNull final TGRequestCallback<Boolean> callback);

    /**
     * Remove user from friends
     *
     * @param callback return method
     * @param userId   ID of user
     */
    void unfriendUser(@NonNull Long userId, @NonNull final TGRequestCallback<Boolean> callback);
}
