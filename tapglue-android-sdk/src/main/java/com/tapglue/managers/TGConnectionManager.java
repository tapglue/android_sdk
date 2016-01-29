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

import com.tapglue.Tapglue;
import com.tapglue.model.TGConnection;
import com.tapglue.model.TGConnection.TGConnectionState;
import com.tapglue.model.TGConnection.TGConnectionType;
import com.tapglue.model.TGPendingConnections;
import com.tapglue.networking.requests.TGRequestCallback;
import com.tapglue.networking.requests.TGRequestErrorType;
import com.tapglue.networking.requests.TGRequestErrorType.ErrorType;


public class TGConnectionManager extends AbstractTGManager implements TGConnectionManagerInterface {

    public TGConnectionManager(Tapglue tgInstance) {
        super(tgInstance);
    }

    @Override
    public void confirmConnection(@NonNull Long userId, @NonNull TGConnectionType connectionType, @NonNull final TGRequestCallback<Boolean> callback) {
        if (tapglue.getUserManager().getCurrentUser() == null) {
            callback.onRequestError(new TGRequestErrorType(ErrorType.USER_NOT_LOGGED_IN));
            return;
        }
        tapglue.createRequest().confirmConnection(userId, connectionType, new TGRequestCallback<TGConnection>() {
            @Override
            public boolean callbackIsEnabled() {
                return callback.callbackIsEnabled();
            }

            @Override
            public void onRequestError(TGRequestErrorType cause) {
                callback.onRequestError(cause);
            }

            @Override
            public void onRequestFinished(TGConnection output, boolean changeDoneOnline) {
                callback.onRequestFinished(true, true);
            }
        });
    }

    /**
     * Follow selected user
     *
     * @param userId
     * @param callback
     */
    @Override
    public void followUser(@NonNull Long userId, @NonNull final TGRequestCallback<Boolean> callback) {
        if (tapglue.getUserManager().getCurrentUser() == null) {
            callback.onRequestError(new TGRequestErrorType(ErrorType.USER_NOT_LOGGED_IN));
            return;
        }
        tapglue.createRequest().createConnection(userId, TGConnectionType.FOLLOW, TGConnectionState.CONFIRMED, new TGRequestCallback<TGConnection>() {
            @Override
            public boolean callbackIsEnabled() {
                return callback.callbackIsEnabled();
            }

            @Override
            public void onRequestError(TGRequestErrorType cause) {
                callback.onRequestError(cause);
            }

            @Override
            public void onRequestFinished(TGConnection output, boolean changeDoneOnline) {
                callback.onRequestFinished(true, true);
            }
        });
    }

    /**
     * Add selected user to friends
     *
     * @param userId   user ID
     * @param callback return method
     */
    @Override
    public void friendUser(@NonNull Long userId, @NonNull final TGRequestCallback<Boolean> callback) {
        if (tapglue.getUserManager().getCurrentUser() == null) {
            callback.onRequestError(new TGRequestErrorType(ErrorType.USER_NOT_LOGGED_IN));
            return;
        }
        tapglue.createRequest().createConnection(userId, TGConnectionType.FRIEND, TGConnectionState.PENDING, new TGRequestCallback<TGConnection>() {
            @Override
            public boolean callbackIsEnabled() {
                return callback.callbackIsEnabled();
            }

            @Override
            public void onRequestError(TGRequestErrorType cause) {
                callback.onRequestError(cause);
            }

            @Override
            public void onRequestFinished(TGConnection output, boolean changeDoneOnline) {
                callback.onRequestFinished(true, true);
            }
        });
    }

    /**
     * Get pending Connections
     */
    @Override
    public void getPendingConnections(@NonNull final TGRequestCallback<TGPendingConnections> callback) {
        if (tapglue.getUserManager().getCurrentUser() == null) {
            callback.onRequestError(new TGRequestErrorType(ErrorType.USER_NOT_LOGGED_IN));
            return;
        }
        tapglue.createRequest().createPendingConnectionsRequest(callback);
    }

    @Override
    public void retrieveConfirmedConnectionsForCurrentUser(@NonNull final TGRequestCallback<TGPendingConnections> callback) {
        if (tapglue.getUserManager().getCurrentUser() == null) {
            callback.onRequestError(new TGRequestErrorType(ErrorType.USER_NOT_LOGGED_IN));
            return;
        }
        tapglue.createRequest().createConfirmedConnectionsRequest(callback);
    }

    @Override
    public void retrieveRejectedConnectionsForCurrentUser(@NonNull final TGRequestCallback<TGPendingConnections> callback) {
        if (tapglue.getUserManager().getCurrentUser() == null) {
            callback.onRequestError(new TGRequestErrorType(ErrorType.USER_NOT_LOGGED_IN));
            return;
        }
        tapglue.createRequest().createRejectedConnectionsRequest(callback);
    }

    /**
     * Stop following selected user
     *
     * @param userId   user ID
     * @param callback return method
     */
    @Override
    public void unfollowUser(@NonNull Long userId, @NonNull final TGRequestCallback<Boolean> callback) {
        if (tapglue.getUserManager().getCurrentUser() == null) {
            callback.onRequestError(new TGRequestErrorType(ErrorType.USER_NOT_LOGGED_IN));
            return;
        }
        tapglue.createRequest().removeConnection(userId, TGConnectionType.FOLLOW, new TGRequestCallback<Object>() {
            @Override
            public boolean callbackIsEnabled() {
                return callback.callbackIsEnabled();
            }

            @Override
            public void onRequestError(TGRequestErrorType cause) {
                callback.onRequestError(cause);
            }

            @Override
            public void onRequestFinished(Object output, boolean changeDoneOnline) {
                callback.onRequestFinished(true, true);
            }
        });
    }

    /**
     * Remove user from friends
     *
     * @param callback return method
     * @param userId   ID of user
     */
    @Override
    public void unfriendUser(@NonNull Long userId, @NonNull final TGRequestCallback<Boolean> callback) {
        if (tapglue.getUserManager().getCurrentUser() == null) {
            callback.onRequestError(new TGRequestErrorType(ErrorType.USER_NOT_LOGGED_IN));
            return;
        }
        tapglue.createRequest().removeConnection(userId, TGConnectionType.FRIEND, new TGRequestCallback<Object>() {
            @Override
            public boolean callbackIsEnabled() {
                return callback.callbackIsEnabled();
            }

            @Override
            public void onRequestError(TGRequestErrorType cause) {
                callback.onRequestError(cause);
            }

            @Override
            public void onRequestFinished(Object output, boolean changeDoneOnline) {
                callback.onRequestFinished(true, true);
            }
        });
    }
}
