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

package com.tapglue.networking;

import com.tapglue.model.TGComment;
import com.tapglue.model.TGCommentsList;
import com.tapglue.model.TGConnection;
import com.tapglue.model.TGConnectionUsersList;
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

public interface TGNetworkRequests {

    /**
     * Confirm connection
     *
     * @param userId   id of the user the connection is confirmed for
     * @param type     the type of connection being confirmed
     * @param callback result of the callback
     */
    void confirmConnection(Long userId, TGConnection.TGConnectionType type, TGRequestCallback<TGConnection> callback);

    /**
     * Get confirmed connections for current user
     *
     * @param callback
     */
    void createConfirmedConnectionsRequest(TGRequestCallback<TGPendingConnections> callback);

    /**
     * Create connection
     *
     * @param userId   user to which connection should be made
     * @param type     type of connection
     * @param state    state of connection
     * @param callback return callback
     */
    void createConnection(Long userId, TGConnection.TGConnectionType type, String state, TGRequestCallback<TGConnection> callback);

    /**
     * Create event for current user
     *
     * @param input    event to be created
     * @param callback return callback
     */
    void createEvent(TGEvent input, TGRequestCallback<TGEvent> callback);

    /**
     * Create pending connection with selected type
     *
     * @param callback return callback
     */
    void createPendingConnectionsRequest(TGRequestCallback<TGPendingConnections> callback);

    /**
     * Create post
     *
     * @param post
     * @param callback
     */
    void createPost(TGPost post, TGRequestCallback<TGPost> callback);

    /**
     * Create new comment for post
     *
     * @param comment
     * @param postId
     * @param callback
     */
    void createPostComment(TGComment comment, String postId, TGRequestCallback<TGComment> callback);

    /**
     * Get rejected connections of current user
     *
     * @param callback
     */
    void createRejectedConnectionsRequest(TGRequestCallback<TGPendingConnections> callback);

    /**
     * Create user using all user data
     *
     * @param user     User data
     * @param callback return callback
     */
    void createUser(TGUser user, TGRequestCallback<TGUser> callback);

    /**
     * Get users followed by current user
     *
     * @param callback return callback
     */
    void getCurrentUserFollowed(TGRequestCallback<TGConnectionUsersList> callback);

    /**
     * Get followers for current user
     *
     * @param callback return callback
     */
    void getCurrentUserFollowers(TGRequestCallback<TGConnectionUsersList> callback);

    /**
     * Get friends of current user
     *
     * @param callback return callback
     */
    void getCurrentUserFriends(TGRequestCallback<TGConnectionUsersList> callback);

    /**
     * Get event by ID
     *
     * @param eventID  event ID
     * @param callback return callback
     */
    void getEvent(Long eventID, TGRequestCallback<TGEvent> callback);

    /**
     * Get event from selected user
     *
     * @param userId   user ID
     * @param eventId  event ID
     * @param callback return callback
     */
    void getEvent(Long userId, Long eventId, TGRequestCallback<TGEvent> callback);

    /**
     * Get all events from current user
     *
     * @param callback return callback
     */
    void getEvents(TGQuery whereParameters, TGRequestCallback<TGEventsList> callback);

    /**
     * Get all events from selected user
     *
     * @param userId   user ID
     * @param callback return callback
     */
    void getEvents(Long userId, TGQuery whereParameters, TGRequestCallback<TGEventsList> callback);

    /**
     * Get feed of current user
     *
     * @param callback return callback
     */
    void getFeed(TGQuery whereParameters, TGRequestCallback<TGFeed> callback);

    /**
     * Get count on current user feed
     *
     * @param callback return callback
     */
    void getFeedCount(TGRequestCallback<TGFeedCount> callback);

    /**
     * Get all posts from feed
     *
     * @param callback
     */
    void getFeedPosts(TGRequestCallback<TGPostsList> callback);

    /**
     * Get all my posts
     *
     * @param callback
     */
    void getMyPosts(TGRequestCallback<TGPostsList> callback);

    /**
     * Get post by id
     *
     * @param postId
     * @param callback
     */
    void getPost(String postId, TGRequestCallback<TGPost> callback);

    /**
     * Get post comments
     *
     * @param postId
     * @param callback
     */
    void getPostComments(String postId, TGRequestCallback<TGCommentsList> callback);

    /**
     * Get likes details for post
     *
     * @param postId
     * @param callback
     */
    void getPostLikes(String postId, TGRequestCallback<TGLikesList> callback);

    /**
     * Get all posts
     *
     * @param callback
     */
    void getPosts(TGRequestCallback<TGPostsList> callback);

    /**
     * Get unread feed of current user
     *
     * @param callback return callback
     */
    void getUnreadFeed(TGRequestCallback<TGFeed> callback);

    /**
     * Get user by user ID
     *
     * @param id       user ID
     * @param callback return callback
     */
    void getUserByID(Long id, TGRequestCallback<TGUser> callback);

    /**
     * Get users followed by selected user
     *
     * @param userId   id of user
     * @param callback return callback
     */
    void getUserFollowed(Long userId, TGRequestCallback<TGConnectionUsersList> callback);

    /**
     * Get users following current user
     *
     * @param userId   id of user
     * @param callback return callback
     */
    void getUserFollowers(Long userId, TGRequestCallback<TGConnectionUsersList> callback);

    /**
     * Get friends of selected user
     *
     * @param userId   id of user
     * @param callback return callback
     */
    void getUserFriends(Long userId, TGRequestCallback<TGConnectionUsersList> callback);

    /**
     * Get posts of user with id
     *
     * @param userId
     * @param callback
     */
    void getUserPosts(Long userId, TGRequestCallback<TGPostsList> callback);

    /**
     * Like post with id
     *
     * @param postId
     * @param callback
     */
    void likePost(String postId, TGRequestCallback<TGLike> callback);

    /**
     * Try to perform login
     *
     * @param user     User basic data
     * @param callback return callback
     */
    void login(TGLoginUser user, TGRequestCallback<TGUser> callback);

    /**
     * Try to perform logout
     *
     * @param callback return callback
     */
    void logout(TGRequestCallback<Object> callback);

    /**
     * Reject connection
     *
     * @param userId   id of user the connection is confirmed for
     * @param type     the type of connection being confirmed
     * @param callback return callback
     */
    void rejectConnection(Long userId, TGConnection.TGConnectionType type, TGRequestCallback<TGConnection> callback);

    /**
     * Remove(cancel) connection
     *
     * @param userId   id of user the connection is made with
     * @param type     type of connection
     * @param callback return callback
     */
    void removeConnection(Long userId, TGConnection.TGConnectionType type, TGRequestCallback<Object> callback);

    /**
     * Remove event of current user
     *
     * @param eventID  ID of event to be removed
     * @param callback return callback
     */
    void removeEvent(Long eventID, TGRequestCallback<Object> callback);

    /**
     * Remove post by id
     *
     * @param postId
     * @param callback
     */
    void removePost(String postId, TGRequestCallback<Object> callback);

    /**
     * Remove comment from post
     *
     * @param postId
     * @param commentId
     * @param callback
     */
    void removePostComments(String postId, Long commentId, TGRequestCallback<Object> callback);

    /**
     * Remove user from server
     *
     * @param user     User data
     * @param callback return callback
     */
    void removeUser(TGUser user, TGRequestCallback<Object> callback);

    /**
     * Do a search query for users
     *
     * @param searchCriteria Search phrase
     * @param callback       return callback
     */
    void search(String searchCriteria, TGRequestCallback<TGConnectionUsersList> callback);

    void search(String socialPlatform, List<String> socialIds, TGRequestCallback<TGConnectionUsersList> callback);

    void searchEmails(List<String> searchCriteria, TGRequestCallback<TGConnectionUsersList> callback);

    /**
     * Update social connections
     *
     * @param socialData social connections information
     * @param callback   return callback
     */
    void socialConnections(TGSocialConnections socialData, TGRequestCallback<TGConnectionUsersList> callback);

    /**
     * Unlike post with id
     *
     * @param postId
     * @param callback
     */
    void unlikePost(String postId, TGRequestCallback<Object> callback);

    /**
     * Update event of current user
     *
     * @param input    event to be updated
     * @param callback return callback
     */
    void updateEvent(TGEvent input, TGRequestCallback<TGEvent> callback);

    /**
     * Update post
     *
     * @param post
     * @param callback
     */
    void updatePost(TGPost post, TGRequestCallback<TGPost> callback);

    /**
     * Update post comment
     *
     * @param comment
     * @param callback
     */
    void updatePostComments(TGComment comment, TGRequestCallback<TGComment> callback);

    /**
     * Update user data on server
     *
     * @param user     User data
     * @param callback return callback
     */
    void updateUser(TGUser user, TGRequestCallback<TGUser> callback);
}
