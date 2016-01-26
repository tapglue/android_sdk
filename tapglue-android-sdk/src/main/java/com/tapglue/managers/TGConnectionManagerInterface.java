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

import com.tapglue.model.TGConnection;
import com.tapglue.model.TGPendingConnections;
import com.tapglue.networking.requests.TGRequestCallback;

public interface TGConnectionManagerInterface {
    void confirmConnection(Long userId, TGConnection.TGConnectionType TGConnectionType, TGRequestCallback<Boolean> callback);

    void followUser(Long userId, TGRequestCallback<Boolean> callback);

    void friendUser(Long userId, TGRequestCallback<Boolean> callback);

    void getPendingConnections(TGRequestCallback<TGPendingConnections> callback);

    void retrieveConfirmedConncetionsForCurrentUser(TGRequestCallback<TGPendingConnections> callback);

    void retrieveRejectedConncetionsForCurrentUser(TGRequestCallback<TGPendingConnections> callback);

    void unfollowUser(Long userId, TGRequestCallback<Boolean> callback);

    void unfriendUser(Long userId, TGRequestCallback<Boolean> callback);
}
