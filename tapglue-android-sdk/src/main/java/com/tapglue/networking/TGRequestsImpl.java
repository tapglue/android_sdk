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

package com.tapglue.networking;

import android.support.annotation.NonNull;

import com.tapglue.Tapglue;
import com.tapglue.model.TGBaseObject;
import com.tapglue.model.TGComment;
import com.tapglue.model.TGCommentsList;
import com.tapglue.model.TGConnection;
import com.tapglue.model.TGConnection.TGConnectionState;
import com.tapglue.model.TGConnection.TGConnectionType;
import com.tapglue.model.TGEvent;
import com.tapglue.model.TGEventsList;
import com.tapglue.model.TGFeed;
import com.tapglue.model.TGFeedCount;
import com.tapglue.model.TGLike;
import com.tapglue.model.TGLikesList;
import com.tapglue.model.TGLoginUser;
import com.tapglue.model.TGPendingConnections;
import com.tapglue.model.TGPost;
import com.tapglue.model.TGPostsList;
import com.tapglue.model.TGRecommendedUsers;
import com.tapglue.model.TGRecommendedUsers.TGRecommendationPeriod;
import com.tapglue.model.TGRecommendedUsers.TGRecommendationType;
import com.tapglue.model.TGSearchCriteria;
import com.tapglue.model.TGSocialConnections;
import com.tapglue.model.TGUser;
import com.tapglue.model.TGUsersList;
import com.tapglue.model.queries.TGQuery;
import com.tapglue.networking.requests.TGRequestCallback;
import com.tapglue.networking.requests.TGRequestErrorType;
import com.tapglue.networking.requests.TGRequestType;

import java.util.List;

public class TGRequestsImpl implements TGRequests {

    // FIXME negative long const values, did we ran out positive ints???

    /**
     * Information about reading all objects
     */
    public static final Long POST_READ_ID_GET_ALL = -1L;

    /**
     * Information about reading all objects from feed
     */
    public static final Long POST_READ_ID_GET_FEED = -2L;

    /**
     * Information about reading current user posts
     */
    public static final Long POST_READ_ID_GET_MY = -4L;

    /**
     * Information about reading selected user posts
     */
    public static final Long POST_READ_ID_USER = -3L;

    /**
     * Network manager
     */
    private final TGNetworkManager networkManager;

    public TGRequestsImpl(TGNetworkManager networkManager) {
        this.networkManager = networkManager;
    }

    /**
     * Confirm connection
     *
     * @param userId   id of the user the connection is confirmed for
     * @param type     the type of connection being confirmed
     * @param callback result of the callback
     */
    @Override
    public void confirmConnection(@NonNull Long userId, TGConnectionType type, @NonNull TGRequestCallback<TGConnection> callback) {
        TGUser currentUser = Tapglue.user().getCurrentUser();
        if (currentUser == null) {
            callback.onRequestError(new TGRequestErrorType(TGRequestErrorType.ErrorType.USER_NOT_LOGGED_IN));
            return;
        }
        TGConnection connection = new TGConnection()
            .setUserFromId(currentUser.getID())
            .setState(TGConnectionState.CONFIRMED)
            .setUserToId(userId)
            .setType(type);
        createCreateObjectRequest(connection, false, callback);
    }

    /**
     * Get confirmed connections for current user
     *
     * @param callback
     */
    @Override
    public void createConfirmedConnectionsRequest(@NonNull TGRequestCallback<TGPendingConnections> callback) {
        TGUser currentUser = Tapglue.user().getCurrentUser();
        if (currentUser == null) {
            callback.onRequestError(new TGRequestErrorType(TGRequestErrorType.ErrorType.USER_NOT_LOGGED_IN));
            return;
        }
        TGConnection connection = new TGConnection()
            .setUserFromId(currentUser.getID())
            .setState(TGConnectionState.CONFIRMED);
        networkManager.performRequest(new TGRequest<>(connection, TGRequestType.READ, true, callback));
    }

    /**
     * Create connection
     *
     * @param userId   user to which connection should be made
     * @param type     type of connection
     * @param state    state of connection
     * @param callback return callback
     */
    @Override
    public void createConnection(@NonNull Long userId, @NonNull TGConnectionType type, @NonNull TGConnectionState state, @NonNull TGRequestCallback<TGConnection> callback) {
        TGUser currentUser = Tapglue.user().getCurrentUser();
        if (currentUser == null) {
            callback.onRequestError(new TGRequestErrorType(TGRequestErrorType.ErrorType.USER_NOT_LOGGED_IN));
            return;
        }

        TGConnection connection = new TGConnection()
            .setUserFromId(currentUser.getID())
            .setState(state)
            .setUserToId(userId)
            .setType(type);
        createCreateObjectRequest(connection, false, callback);
    }

    /**
     * Create request for create method
     *
     * @param object            Object of request
     * @param canBeDoneOnlyLive Request should be done only if network is available?
     * @param callback          Output callback
     * @param <T>               Type of object of request
     */
    private <T extends TGBaseObject> void createCreateObjectRequest(T object, boolean canBeDoneOnlyLive, TGRequestCallback<T> callback) {
        networkManager.performRequest(new TGRequest<>(object, TGRequestType.CREATE, canBeDoneOnlyLive, callback));
    }

    /**
     * Create event for current user
     *
     * @param input    event to be created
     * @param callback return callback
     */
    @Override
    public void createEvent(@NonNull TGEvent input, @NonNull TGRequestCallback<TGEvent> callback) {
        createCreateObjectRequest(input, false, callback);
    }

    /**
     * Create pending connection with selected type
     *
     * @param callback return callback
     */
    @Override
    public void createPendingConnectionsRequest(@NonNull TGRequestCallback<TGPendingConnections> callback) {
        networkManager.performRequest(new TGRequest<>(new TGPendingConnections(), TGRequestType.READ, true, callback));
    }

    /**
     * Create post
     *
     * @param post
     * @param callback
     */
    @Override
    public void createPost(@NonNull TGPost post, @NonNull TGRequestCallback<TGPost> callback) {
        createCreateObjectRequest(post, true, callback);
    }

    /**
     * Create new comment for post
     *
     * @param comment
     * @param postId
     * @param callback
     */
    @Override
    public void createPostComment(@NonNull TGComment comment, @NonNull String postId, @NonNull TGRequestCallback<TGComment> callback) {
        createCreateObjectRequest(comment.setPostId(postId), true, callback);
    }

    /**
     * Create request for read method
     *
     * @param object   Object of request
     * @param callback Output callback
     * @param <T>      Type of object of request
     */
    private <T extends TGBaseObject, TO extends TGBaseObject> void createReadObjectRequest(T object, TGRequestCallback<TO> callback) {
        networkManager.performRequest(new TGRequest<>(object, TGRequestType.READ, true, callback));
    }

    /**
     * Get rejected connections for current user
     *
     * @param callback
     */
    @Override
    public void createRejectedConnectionsRequest(@NonNull TGRequestCallback<TGPendingConnections> callback) {
        TGUser currentUser = Tapglue.user().getCurrentUser();
        if (currentUser == null) {
            callback.onRequestError(new TGRequestErrorType(TGRequestErrorType.ErrorType.USER_NOT_LOGGED_IN));
            return;
        }

        TGConnection connection = new TGConnection()
            .setUserFromId(currentUser.getID())
            .setState(TGConnectionState.REJECTED);
        networkManager.performRequest(new TGRequest<>(connection, TGRequestType.READ, true, callback));
    }

    /**
     * Create request for remove method
     *
     * @param object            Object of request
     * @param canBeDoneOnlyLive Request should be done only if network is available?
     * @param callback          Output callback
     */
    private void createRemoveObjectRequest(TGBaseObject object, boolean canBeDoneOnlyLive, TGRequestCallback<Object> callback) {
        networkManager.performRequest(new TGRequest(object, TGRequestType.DELETE, canBeDoneOnlyLive, callback));
    }

    /**
     * Create request for update method
     *
     * @param object   Object of request
     * @param callback Output callback
     * @param <T>      Type of object of request
     */
    private <T extends TGBaseObject> void createUpdateObjectRequest(T object, TGRequestCallback<T> callback) {
        networkManager.performRequest(new TGRequest<>(object, TGRequestType.UPDATE, false, callback));
    }

    /**
     * Create user using all user data
     *
     * @param user     User data
     * @param callback return callback
     */
    @Override
    public void createUser(@NonNull TGUser user, @NonNull TGRequestCallback<TGUser> callback) {
        createCreateObjectRequest(user, true, callback);
    }

    /**
     * Get users followed by current user
     *
     * @param callback return callback
     */
    @Override
    public void getCurrentUserFollowed(@NonNull TGRequestCallback<TGUsersList> callback) {
        createReadObjectRequest(new TGConnection().setType(TGConnectionType.FOLLOW).setUserFromId(null), callback);
    }

    /**
     * Get followers for current user
     *
     * @param callback return callback
     */
    @Override
    public void getCurrentUserFollowers(@NonNull TGRequestCallback<TGUsersList> callback) {
        createReadObjectRequest(new TGConnection().setType(null).setUserFromId(null), callback);
    }

    /**
     * Get friends of current user
     *
     * @param callback return callback
     */
    @Override
    public void getCurrentUserFriends(@NonNull TGRequestCallback<TGUsersList> callback) {
        createReadObjectRequest(new TGConnection().setType(TGConnectionType.FRIEND).setUserFromId(null), callback);
    }

    /**
     * Get event by ID
     *
     * @param eventID  event ID
     * @param callback return callback
     */
    @Override
    public void getEvent(@NonNull Long eventID, @NonNull TGRequestCallback<TGEvent> callback) {
        createReadObjectRequest(new TGEvent(null).setReadRequestObjectId(eventID), callback);
    }

    /**
     * Get event from selected user
     *
     * @param userId   user ID
     * @param eventId  event ID
     * @param callback return callback
     */
    @Override
    public void getEvent(@NonNull Long userId, @NonNull Long eventId, @NonNull TGRequestCallback<TGEvent> callback) {
        createReadObjectRequest(new TGEvent(null).setReadRequestUserId(userId).setReadRequestObjectId(eventId), callback);
    }

    /**
     * Get all events from current user
     *
     * @param callback return callback
     */
    @Override
    public void getEvents(TGQuery whereParameters, @NonNull TGRequestCallback<TGEventsList> callback) {
        createReadObjectRequest(new TGEventsList().setSearchQuery(whereParameters), callback);
    }

    /**
     * Get all events from selected user
     *
     * @param userId   user ID
     * @param callback return callback
     */
    @Override
    public void getEvents(@NonNull Long userId, TGQuery whereParameters, @NonNull TGRequestCallback<TGEventsList> callback) {
        createReadObjectRequest(new TGEventsList().setReadRequestUserId(userId).setSearchQuery(whereParameters), callback);
    }

    /**
     * Get feed of current user
     *
     * @param callback return callback
     */
    @Override
    public void getFeed(TGQuery whereParameters, @NonNull TGRequestCallback<TGFeed> callback) {
        createReadObjectRequest(new TGFeed().setSearchQuery(whereParameters), callback);
    }

    /**
     * Get count on current user feed
     *
     * @param callback return callback
     */
    @Override
    public void getFeedCount(@NonNull TGRequestCallback<TGFeedCount> callback) {
        createReadObjectRequest(new TGFeedCount(), callback);
    }

    /**
     * Get all posts from feed
     *
     * @param callback
     */
    @Override
    public void getFeedPosts(@NonNull TGRequestCallback<TGPostsList> callback) {
        createReadObjectRequest(new TGPost().setReadRequestUserId(POST_READ_ID_GET_FEED), callback);
    }

    /**
     * Get all my posts
     *
     * @param callback
     */
    @Override
    public void getMyPosts(@NonNull TGRequestCallback<TGPostsList> callback) {
        createReadObjectRequest(new TGPost().setReadRequestUserId(POST_READ_ID_GET_MY), callback);
    }

    /**
     * Get post by id
     *
     * @param postId
     * @param callback
     */
    @Override
    public void getPost(@NonNull String postId, @NonNull TGRequestCallback<TGPost> callback) {
        createReadObjectRequest(new TGPost().setReadRequestObjectStringId(postId), callback);
    }

    /**
     * Get post comments
     *
     * @param postId
     * @param callback
     */
    @Override
    public void getPostComments(@NonNull String postId, @NonNull TGRequestCallback<TGCommentsList> callback) {
        createReadObjectRequest(new TGCommentsList().setReadRequestObjectStringId(postId), callback);
    }

    /**
     * Get likes details for post
     *
     * @param postId
     * @param callback
     */
    @Override
    public void getPostLikes(@NonNull String postId, @NonNull TGRequestCallback<TGLikesList> callback) {
        createReadObjectRequest(new TGLikesList().setReadRequestObjectStringId(postId), callback);
    }

    /**
     * Get all posts
     *
     * @param callback
     */
    @Override
    public void getPosts(@NonNull TGRequestCallback<TGPostsList> callback) {
        createReadObjectRequest(new TGPost().setReadRequestUserId(POST_READ_ID_GET_ALL), callback);
    }

    /**
     * Get the recommended active users from the last month
     *
     * @param callback
     */
    @Override
    public void getRecommendedUsers(TGRecommendationType type, TGRecommendationPeriod period, @NonNull TGRequestCallback<TGUsersList> callback) {
        createReadObjectRequest(new TGRecommendedUsers().setType(type).setPeriod(period), callback);
    }

    /**
     * Get unread feed of current user
     *
     * @param callback return callback
     */
    @Override
    public void getUnreadFeed(@NonNull TGRequestCallback<TGFeed> callback) {
        createReadObjectRequest(new TGFeed().setUnreadCount((long) 1), callback);
    }

    /**
     * Get user by user ID
     *
     * @param id       user ID
     * @param callback return callback
     */
    @Override
    public void getUserByID(@NonNull Long id, @NonNull TGRequestCallback<TGUser> callback) {
        createReadObjectRequest(new TGUser().setReadRequestObjectId(id), callback);
    }

    /**
     * Get users followed by selected user
     *
     * @param userId   id of user
     * @param callback return callback
     */
    @Override
    public void getUserFollowed(@NonNull Long userId, @NonNull TGRequestCallback<TGUsersList> callback) {
        createReadObjectRequest(new TGConnection().setType(TGConnectionType.FOLLOW).setUserFromId(userId), callback);
    }

    /**
     * Get users following current user
     *
     * @param userId   id of user
     * @param callback return callback
     */
    @Override
    public void getUserFollowers(@NonNull Long userId, @NonNull TGRequestCallback<TGUsersList> callback) {
        createReadObjectRequest(new TGConnection().setType(null).setUserFromId(userId), callback);
    }

    /**
     * Get friends of selected user
     *
     * @param userId   id of user
     * @param callback return callback
     */
    @Override
    public void getUserFriends(@NonNull Long userId, @NonNull TGRequestCallback<TGUsersList> callback) {
        createReadObjectRequest(new TGConnection().setType(TGConnectionType.FRIEND).setUserFromId(userId), callback);
    }

    /**
     * Get posts of user with id
     *
     * @param userId
     * @param callback
     */
    @Override
    public void getUserPosts(@NonNull Long userId, @NonNull TGRequestCallback<TGPostsList> callback) {
        createReadObjectRequest(new TGPost().setReadRequestUserId(POST_READ_ID_USER).setReadRequestObjectId(userId), callback);
    }

    /**
     * Like post with id
     *
     * @param postId
     * @param callback
     */
    @Override
    public void likePost(@NonNull String postId, @NonNull TGRequestCallback<TGLike> callback) {
        createCreateObjectRequest(new TGLike().setPostId(postId), true, callback);
    }

    /**
     * Try to perform login
     *
     * @param user     User basic data
     * @param callback return callback
     */
    @Override
    public void login(@NonNull TGLoginUser user, @NonNull TGRequestCallback<TGUser> callback) {
        networkManager.performRequest(new TGRequest<>(user, TGRequestType.LOGIN, true, callback));
    }

    /**
     * Try to perform logout
     *
     * @param callback return callback
     */
    @Override
    public void logout(@NonNull TGRequestCallback<Object> callback) {
        networkManager.performRequest(new TGRequest(null, TGRequestType.LOGOUT, true, callback));
    }

    /**
     * Reject connection
     *
     * @param userId   id of user the connection is rejected for
     * @param type     the type of connection being rejected
     * @param callback return callback
     */
    @Override
    public void rejectConnection(@NonNull Long userId, @NonNull TGConnectionType type, @NonNull TGRequestCallback<TGConnection> callback) {
        TGUser currentUser = Tapglue.user().getCurrentUser();
        if (currentUser == null) {
            callback.onRequestError(new TGRequestErrorType(TGRequestErrorType.ErrorType.USER_NOT_LOGGED_IN));
            return;
        }

        TGConnection connection = new TGConnection()
            .setUserFromId(currentUser.getID())
            .setUserToId(userId)
            .setType(type)
            .setState(TGConnectionState.REJECTED);
        createCreateObjectRequest(connection, false, callback);
    }

    /**
     * Remove(cancel) connection
     *
     * @param userId   id of user the connection is made with
     * @param type     type of connection
     * @param callback return callback
     */
    @Override
    public void removeConnection(@NonNull Long userId, @NonNull TGConnectionType type, @NonNull TGRequestCallback<Object> callback) {
        TGUser currentUser = Tapglue.user().getCurrentUser();
        if (currentUser == null) {
            callback.onRequestError(new TGRequestErrorType(TGRequestErrorType.ErrorType.USER_NOT_LOGGED_IN));
            return;
        }

        TGConnection connection = new TGConnection()
            .setUserToId(userId)
            .setType(type)
            .setUserFromId(currentUser.getID());
        createRemoveObjectRequest(connection, false, callback);
    }

    /**
     * Remove event of current user
     *
     * @param eventID  ID of event to be removed
     * @param callback return callback
     */
    @Override
    public void removeEvent(@NonNull Long eventID, @NonNull TGRequestCallback<Object> callback) {
        createRemoveObjectRequest(new TGEvent(null).setReadRequestObjectId(eventID), false, callback);
    }

    /**
     * Remove post by id
     *
     * @param postId
     * @param callback
     */
    @Override
    public void removePost(@NonNull String postId, @NonNull TGRequestCallback<Object> callback) {
        createRemoveObjectRequest(new TGPost().setReadRequestObjectStringId(postId), true, callback);
    }

    /**
     * Remove comment from post
     *
     * @param postId
     * @param commentId
     * @param callback
     */
    @Override
    public void removePostComments(@NonNull String postId, @NonNull Long commentId, @NonNull TGRequestCallback<Object> callback) {
        createRemoveObjectRequest(new TGComment().setPostId(postId).setReadRequestObjectId(commentId), true, callback);
    }

    /**
     * Remove user from server
     *
     * @param user     User data
     * @param callback return callback
     */
    @Override
    public void removeUser(@NonNull TGUser user, @NonNull TGRequestCallback<Object> callback) {
        createRemoveObjectRequest(user, true, callback);
    }

    /**
     * Do a search query for users
     *
     * @param searchCriteria Search phrase
     * @param callback       return callback
     */
    @Override
    public void search(@NonNull String searchCriteria, @NonNull TGRequestCallback<TGUsersList> callback) {
        networkManager.performRequest(new TGRequest<>(new TGSearchCriteria().setSearchCriteria(searchCriteria), TGRequestType.SEARCH, true, callback));
    }

    /**
     * Do a search query for socialConnections
     *
     * @param socialIds      Search phrase
     * @param socialPlatform Platform
     * @param callback       return callback
     */
    @Override
    public void search(@NonNull String socialPlatform, @NonNull List<String> socialIds, @NonNull TGRequestCallback<TGUsersList> callback) {
        networkManager.performRequest(new TGRequest<>(new TGSearchCriteria().setSearchCriteria(socialPlatform, socialIds), TGRequestType.SEARCH, true, callback));
    }

    /**
     * Do a search query for emails
     *
     * @param searchCriteria Search phrase
     * @param callback       return callback
     */
    @Override
    public void searchEmails(@NonNull List<String> searchCriteria, @NonNull TGRequestCallback<TGUsersList> callback) {
        networkManager.performRequest(new TGRequest<>(new TGSearchCriteria().setSearchCriteriaEmails(searchCriteria), TGRequestType.SEARCH, true, callback));
    }

    /**
     * Update social connections
     *
     * @param socialData social connections information
     * @param callback   return callback
     */
    @Override
    public void socialConnections(@NonNull TGSocialConnections socialData, @NonNull TGRequestCallback<TGUsersList> callback) {
        networkManager.performRequest(new TGRequest<>(socialData, TGRequestType.UPDATE, true, callback));
    }

    /**
     * Unlike post with id
     *
     * @param postId
     * @param callback
     */
    @Override
    public void unlikePost(@NonNull String postId, @NonNull TGRequestCallback<Object> callback) {
        createRemoveObjectRequest(new TGLike().setPostId(postId), true, callback);
    }

    /**
     * Update event of current user
     *
     * @param input    event to be updated
     * @param callback return callback
     */
    @Override
    public void updateEvent(@NonNull TGEvent input, @NonNull TGRequestCallback<TGEvent> callback) {
        createUpdateObjectRequest(input, callback);
    }

    /**
     * Update post
     *
     * @param post
     * @param callback
     */
    @Override
    public void updatePost(@NonNull TGPost post, @NonNull TGRequestCallback<TGPost> callback) {
        createUpdateObjectRequest(post, callback);
    }

    /**
     * Update post comment
     *
     * @param comment
     * @param callback
     */
    @Override
    public void updatePostComments(@NonNull TGComment comment, @NonNull TGRequestCallback<TGComment> callback) {
        networkManager.performRequest(new TGRequest<>(comment, TGRequestType.UPDATE, true, callback));
    }

    /**
     * Update user data on server
     *
     * @param user     User data
     * @param callback return callback
     */
    @Override
    public void updateUser(@NonNull TGUser user, @NonNull TGRequestCallback<TGUser> callback) {
        createUpdateObjectRequest(user, callback);
    }
}
