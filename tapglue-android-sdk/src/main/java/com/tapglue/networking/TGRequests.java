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
import android.support.annotation.Nullable;

import com.tapglue.model.TGComment;
import com.tapglue.model.TGCommentsList;
import com.tapglue.model.TGConnection;
import com.tapglue.model.TGConnection.TGConnectionState;
import com.tapglue.model.TGConnection.TGConnectionType;
import com.tapglue.model.TGUsersList;
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
import com.tapglue.model.TGSocialConnections;
import com.tapglue.model.TGUser;
import com.tapglue.model.queries.TGQuery;
import com.tapglue.networking.requests.TGRequestCallback;

import java.util.List;

public interface TGRequests {

    /**
     * Confirm connection
     *
     * @param userId   id of the user the connection is confirmed for
     * @param type     the type of connection being confirmed
     * @param callback result of the callback
     */
    void confirmConnection(@NonNull Long userId, TGConnectionType type, @NonNull TGRequestCallback<TGConnection> callback);

    /**
     * Get confirmed connections for current user
     *
     * @param callback
     */
    void createConfirmedConnectionsRequest(@NonNull TGRequestCallback<TGPendingConnections> callback);

    /**
     * Create connection
     *
     * @param userId   user to which connection should be made
     * @param type     type of connection
     * @param state    state of connection
     * @param callback return callback
     */
    void createConnection(@NonNull Long userId, @NonNull TGConnectionType type, @NonNull TGConnectionState state, @NonNull TGRequestCallback<TGConnection> callback);

    /**
     * Create event for current user
     *
     * @param input    event to be created
     * @param callback return callback
     */
    void createEvent(@NonNull TGEvent input, @NonNull TGRequestCallback<TGEvent> callback);

    /**
     * Create pending connection with selected type
     *
     * @param callback return callback
     */
    void createPendingConnectionsRequest(@NonNull TGRequestCallback<TGPendingConnections> callback);

    /**
     * Create post
     *
     * @param post
     * @param callback
     */
    void createPost(@NonNull TGPost post, @NonNull TGRequestCallback<TGPost> callback);

    /**
     * Create new comment for post
     *
     * @param comment
     * @param postId
     * @param callback
     */
    void createPostComment(@NonNull TGComment comment, @NonNull String postId, @NonNull TGRequestCallback<TGComment> callback);

    /**
     * Get rejected connections of current user
     *
     * @param callback
     */
    void createRejectedConnectionsRequest(@NonNull TGRequestCallback<TGPendingConnections> callback);

    /**
     * Create user using all user data
     *
     * @param user     User data
     * @param callback return callback
     */
    void createUser(@NonNull TGUser user, @NonNull TGRequestCallback<TGUser> callback);

    /**
     * Get users followed by current user
     *
     * @param callback return callback
     */
    void getCurrentUserFollowed(@NonNull TGRequestCallback<TGUsersList> callback);

    /**
     * Get followers for current user
     *
     * @param callback return callback
     */
    void getCurrentUserFollowers(@NonNull TGRequestCallback<TGUsersList> callback);

    /**
     * Get friends of current user
     *
     * @param callback return callback
     */
    void getCurrentUserFriends(@NonNull TGRequestCallback<TGUsersList> callback);

    /**
     * Get event by ID
     *
     * @param eventID  event ID
     * @param callback return callback
     */
    void getEvent(@NonNull Long eventID, @NonNull TGRequestCallback<TGEvent> callback);

    /**
     * Get event from selected user
     *
     * @param userId   user ID
     * @param eventId  event ID
     * @param callback return callback
     */
    void getEvent(@NonNull Long userId, @NonNull Long eventId, @NonNull TGRequestCallback<TGEvent> callback);

    /**
     * Get all events from current user
     *
     * @param callback return callback
     */
    void getEvents(@Nullable TGQuery whereParameters, @NonNull TGRequestCallback<TGEventsList> callback);

    /**
     * Get all events from selected user
     *
     * @param userId   user ID
     * @param callback return callback
     */
    void getEvents(@NonNull Long userId, @Nullable TGQuery whereParameters, @NonNull TGRequestCallback<TGEventsList> callback);

    /**
     * Get feed of current user
     *
     * @param callback return callback
     */
    void getFeed(@Nullable TGQuery whereParameters, @NonNull TGRequestCallback<TGFeed> callback);

    /**
     * Get count on current user feed
     *
     * @param callback return callback
     */
    void getFeedCount(@NonNull TGRequestCallback<TGFeedCount> callback);

    /**
     * Get all posts from feed
     *
     * @param callback
     */
    void getFeedPosts(@NonNull TGRequestCallback<TGPostsList> callback);

    /**
     * Get all my posts
     *
     * @param callback
     */
    void getMyPosts(@NonNull TGRequestCallback<TGPostsList> callback);

    /**
     * Get post by id
     *
     * @param postId
     * @param callback
     */
    void getPost(@NonNull String postId, @NonNull TGRequestCallback<TGPost> callback);

    /**
     * Get post comments
     *
     * @param postId
     * @param callback
     */
    void getPostComments(@NonNull String postId, @NonNull TGRequestCallback<TGCommentsList> callback);

    /**
     * Get likes details for post
     *
     * @param postId
     * @param callback
     */
    void getPostLikes(@NonNull String postId, @NonNull TGRequestCallback<TGLikesList> callback);

    /**
     * Get all posts
     *
     * @param callback
     */
    void getPosts(@NonNull TGRequestCallback<TGPostsList> callback);

    /**
     * Get unread feed of current user
     *
     * @param callback return callback
     */
    void getUnreadFeed(@NonNull TGRequestCallback<TGFeed> callback);

    /**
     * Get user by user ID
     *
     * @param id       user ID
     * @param callback return callback
     */
    void getUserByID(@NonNull Long id, @NonNull TGRequestCallback<TGUser> callback);

    /**
     * Get users followed by selected user
     *
     * @param userId   id of user
     * @param callback return callback
     */
    void getUserFollowed(@NonNull Long userId, @NonNull TGRequestCallback<TGUsersList> callback);

    /**
     * Get users following current user
     *
     * @param userId   id of user
     * @param callback return callback
     */
    void getUserFollowers(@NonNull Long userId, @NonNull TGRequestCallback<TGUsersList> callback);

    /**
     * Get friends of selected user
     *
     * @param userId   id of user
     * @param callback return callback
     */
    void getUserFriends(@NonNull Long userId, @NonNull TGRequestCallback<TGUsersList> callback);

    /**
     * Get posts of user with id
     *
     * @param userId
     * @param callback
     */
    void getUserPosts(@NonNull Long userId, @NonNull TGRequestCallback<TGPostsList> callback);

    /**
     * Like post with id
     *
     * @param postId
     * @param callback
     */
    void likePost(@NonNull String postId, @NonNull TGRequestCallback<TGLike> callback);

    /**
     * Try to perform login
     *
     * @param user     User basic data
     * @param callback return callback
     */
    void login(@NonNull TGLoginUser user, @NonNull TGRequestCallback<TGUser> callback);

    /**
     * Try to perform logout
     *
     * @param callback return callback
     */
    void logout(@NonNull TGRequestCallback<Object> callback);

    /**
     * Reject connection
     *
     * @param userId   id of user the connection is confirmed for
     * @param type     the type of connection being confirmed
     * @param callback return callback
     */
    void rejectConnection(@NonNull Long userId, @NonNull TGConnectionType type, @NonNull TGRequestCallback<TGConnection> callback);

    /**
     * Remove(cancel) connection
     *
     * @param userId   id of user the connection is made with
     * @param type     type of connection
     * @param callback return callback
     */
    void removeConnection(@NonNull Long userId, @NonNull TGConnectionType type, @NonNull TGRequestCallback<Object> callback);

    /**
     * Remove event of current user
     *
     * @param eventID  ID of event to be removed
     * @param callback return callback
     */
    void removeEvent(@NonNull Long eventID, @NonNull TGRequestCallback<Object> callback);

    /**
     * Remove post by id
     *
     * @param postId
     * @param callback
     */
    void removePost(@NonNull String postId, @NonNull TGRequestCallback<Object> callback);

    /**
     * Remove comment from post
     *
     * @param postId
     * @param commentId
     * @param callback
     */
    void removePostComments(@NonNull String postId, @NonNull Long commentId, @NonNull TGRequestCallback<Object> callback);

    /**
     * Remove user from server
     *
     * @param user     User data
     * @param callback return callback
     */
    void removeUser(@NonNull TGUser user, @NonNull TGRequestCallback<Object> callback);

    /**
     * Do a search query for users
     *
     * @param searchCriteria Search phrase
     * @param callback       return callback
     */
    void search(@NonNull String searchCriteria, @NonNull TGRequestCallback<TGUsersList> callback);

    void search(@NonNull String socialPlatform, @NonNull List<String> socialIds, @NonNull TGRequestCallback<TGUsersList> callback);

    void searchEmails(@NonNull List<String> searchCriteria, @NonNull TGRequestCallback<TGUsersList> callback);

    /**
     * Update social connections
     *
     * @param socialData social connections information
     * @param callback   return callback
     */
    void socialConnections(@NonNull TGSocialConnections socialData, @NonNull TGRequestCallback<TGUsersList> callback);

    /**
     * Unlike post with id
     *
     * @param postId
     * @param callback
     */
    void unlikePost(@NonNull String postId, @NonNull TGRequestCallback<Object> callback);

    /**
     * Update event of current user
     *
     * @param input    event to be updated
     * @param callback return callback
     */
    void updateEvent(@NonNull TGEvent input, @NonNull TGRequestCallback<TGEvent> callback);

    /**
     * Update post
     *
     * @param post
     * @param callback
     */
    void updatePost(@NonNull TGPost post, @NonNull TGRequestCallback<TGPost> callback);

    /**
     * Update post comment
     *
     * @param comment
     * @param callback
     */
    void updatePostComments(@NonNull TGComment comment, @NonNull TGRequestCallback<TGComment> callback);

    /**
     * Update user data on server
     *
     * @param user     User data
     * @param callback return callback
     */
    void updateUser(@NonNull TGUser user, @NonNull TGRequestCallback<TGUser> callback);
}
