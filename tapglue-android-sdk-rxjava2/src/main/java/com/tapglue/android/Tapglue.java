/*
 *  Copyright (c) 2015-2016 Tapglue (https://www.tapglue.com/). All rights reserved.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package com.tapglue.android;

import android.content.Context;

import com.tapglue.android.entities.Comment;
import com.tapglue.android.entities.Connection;
import com.tapglue.android.entities.Connection.Type;
import com.tapglue.android.entities.Event;
import com.tapglue.android.entities.Like;
import com.tapglue.android.entities.Post;
import com.tapglue.android.entities.User;
import com.tapglue.android.http.payloads.SocialConnections;

import java.io.IOException;
import java.util.List;

/**
 * Depending on the paradigm used all interactions with Tapglue will happen through this class or
 * RxTapglue. Internally this class uses the Rx solution through a unwrapping process.
 * @see RxTapglue
 */
public class Tapglue {

    private RxTapglue rxTapglue;

    /**
     *
     * @param configuration configuration of the tapglue instance
     * @param context the context will be used for persisting session token and current user
     */
    public Tapglue(Configuration configuration, Context context) throws Exception {
        rxTapglue = new RxTapglue(configuration, context);
    }

    /**
     * Logs in user with username and password. The user is persisted and can be requested by calling
     * {@link #getCurrentUser() getCurrentUser} and will be persisted until explicit log out.
     * @param username username of the user to be logged in
     * @param password password of the user to be logged in
     * @return the {@link User user} that was logged in
     * @throws IOException exceptions thrown will be IOExceptions when there are IO issues with the
     * connection it self, or the subclass TapglueError when there was an API error.
     * @see com.tapglue.android.http.TapglueError
     */
    public User loginWithUsername(String username, String password) throws IOException {
        return new RxWrapper<User>().unwrap(rxTapglue.loginWithUsername(username, password));
    }

    /**
     * Logs in user with email and password. The user is persisted and can be requested by calling
     * {@link #getCurrentUser() getCurrentUser} and will be persisted until explicit log out.
     * @param email email of the user to be logged in
     * @param password password of the user to be logged in
     * @return the {@link User user} that was logged in
     * @throws IOException exceptions thrown will be IOExceptions when there are IO issues with the
     * connection it self, or the subclass TapglueError when there was an API error.
     * @see com.tapglue.android.http.TapglueError
     */
    public User loginWithEmail(String email, String password) throws IOException {
        return new RxWrapper<User>().unwrap(rxTapglue.loginWithEmail(email, password));
    }

    /**
     * Logs out user. This will delete the persisted current user.
     * @throws IOException exceptions thrown will be IOExceptions when there are IO issues with the
     * connection it self, or the subclass TapglueError when there was an API error.
     * @see com.tapglue.android.http.TapglueError
     */
    public void logout() throws IOException {
        new RxWrapper<Void>().unwrap(rxTapglue.logout());
    }

    /**
     * Gets the persisted current user. Will only be available after a successful login.
     * @return the current {@link User user}
     * @throws IOException exceptions thrown will be IOExceptions when there are IO issues with the
     * connection it self, or the subclass TapglueError when there was an API error.
     * @see com.tapglue.android.http.TapglueError
     */
    public User getCurrentUser() throws IOException {
        return new RxWrapper<User>().unwrap(rxTapglue.getCurrentUser());
    }

    /**
     * Creates a user.
     * @param user the user to create
     * @return the created {@link com.tapglue.android.entities.User user}.
     * @throws IOException exceptions thrown will be IOExceptions when there are IO issues with the
     * connection it self, or the subclass TapglueError when there was an API error.
     * @see com.tapglue.android.http.TapglueError
     */
    public User createUser(User user) throws IOException {
        return new RxWrapper<User>().unwrap(rxTapglue.createUser(user));
    }

    /**
     * Deletes current user.
     * @throws IOException exceptions thrown will be IOExceptions when there are IO issues with the
     * connection it self, or the subclass TapglueError when there was an API error.
     * @see com.tapglue.android.http.TapglueError
     */
    public void deleteCurrentUser() throws Exception {
        new RxWrapper<Void>().unwrap(rxTapglue.deleteCurrentUser());
    }

    /**
     * Updates current user
     * @param updatedUser The updated user
     * @return updated {@link User user}.
     * @throws IOException exceptions thrown will be IOExceptions when there are IO issues with the
     * connection it self, or the subclass TapglueError when there was an API error.
     * @see com.tapglue.android.http.TapglueError
     */
    public User updateCurrentUser(User updatedUser) throws IOException {
        return new RxWrapper<User>().unwrap(rxTapglue.updateCurrentUser(updatedUser));
    }

    /**
     * refreshses the persisted current user. After a successful call the refreshed user will be
     * persisted and available at {@link #getCurrentUser() getCurrentUser}
     * @return refreshed current {@link User user}.
     * @throws IOException exceptions thrown will be IOExceptions when there are IO issues with the
     * connection it self, or the subclass TapglueError when there was an API error.
     * @see com.tapglue.android.http.TapglueError
     */
    public User refreshCurrentUser() throws IOException {
        return new RxWrapper<User>().unwrap(rxTapglue.refreshCurrentUser());
    }

    /**
     * Retrieve user.
     * @param id user id of the wanted user
     * @return the {@link User user}.
     * @throws IOException exceptions thrown will be IOExceptions when there are IO issues with the
     * connection it self, or the subclass TapglueError when there was an API error.
     * @see com.tapglue.android.http.TapglueError
     */
    public User retrieveUser(String id) throws IOException {
        return new RxWrapper<User>().unwrap(rxTapglue.retrieveUser(id));
    }

    /**
     * @param connection {@link Connection connection} to be created
     * @return the created connection
     * @throws IOException exceptions thrown will be IOExceptions when there are IO issues with the
     * connection it self, or the subclass TapglueError when there was an API error.
     * @see com.tapglue.android.http.TapglueError
     */
    public Connection createConnection(Connection connection) throws IOException {
        return new RxWrapper<Connection>().unwrap(rxTapglue.createConnection(connection));
    }

    /**
     * create connections with users retrieved from other social networks
     * @param connections the {@link SocialConnections connections}
     * @return list of users to whom connections were created
     * @throws IOException exceptions thrown will be IOExceptions when there are IO issues with the
     * connection it self, or the subclass TapglueError when there was an API error.
     * @see com.tapglue.android.http.TapglueError
     */
    public List<User> createSocialConnections(SocialConnections connections) throws IOException {
        return new RxWrapper<List<User>>().unwrap(rxTapglue.createSocialConnections(connections));
    }

    public void deleteConnection(String userId, Type type) throws IOException {
        new RxWrapper<Void>().unwrap(rxTapglue.deleteConnection(userId, type));
    }

    /**
     * @param post {@link Post post} to be created.
     * @return created post.
     * @throws IOException exceptions thrown will be IOExceptions when there are IO issues with the
     * connection it self, or the subclass TapglueError when there was an API error.
     * @see com.tapglue.android.http.TapglueError
     */
    public Post createPost(Post post) throws IOException {
        return new RxWrapper<Post>().unwrap(rxTapglue.createPost(post));
    }

    /**
     * @param postId id of the post to be retrieved.
     * @return the retrieved post.
     * @throws IOException exceptions thrown will be IOExceptions when there are IO issues with the
     * connection it self, or the subclass TapglueError when there was an API error.
     * @see com.tapglue.android.http.TapglueError
     */
    public Post retrievePost(String postId) throws IOException {
        return new RxWrapper<Post>().unwrap(rxTapglue.retrievePost(postId));
    }

    /**
     * @param id id of the post to be updated.
     * @param post new post that will replace the old post.
     * @return the updated post.
     * @throws IOException exceptions thrown will be IOExceptions when there are IO issues with the
     * connection it self, or the subclass TapglueError when there was an API error.
     * @see com.tapglue.android.http.TapglueError
     */
    public Post updatePost(String id, Post post) throws IOException {
        return new RxWrapper<Post>().unwrap(rxTapglue.updatePost(id, post));
    }

    /**
     * @param postId id of the post to be deleted.
     * @throws IOException exceptions thrown will be IOExceptions when there are IO issues with the
     * connection it self, or the subclass TapglueError when there was an API error.
     * @see com.tapglue.android.http.TapglueError
     */
    public void deletePost(String postId) throws IOException {
        new RxWrapper<Void>().unwrap(rxTapglue.deletePost(postId));
    }

    /**
     * creates a like event on a post.
     * @param postId id of the post to be liked.
     * @return created like event.
     * @throws IOException exceptions thrown will be IOExceptions when there are IO issues with the
     * connection it self, or the subclass TapglueError when there was an API error.
     * @see com.tapglue.android.http.TapglueError
     */
    public Like createLike(String postId) throws IOException {
        return new RxWrapper<Like>().unwrap(rxTapglue.createLike(postId));
    }

    /**
     * Deletes like.
     * @param postId id of the post that was liked.
     * @throws IOException exceptions thrown will be IOExceptions when there are IO issues with the
     * connection it self, or the subclass TapglueError when there was an API error.
     * @see com.tapglue.android.http.TapglueError
     */
    public void deleteLike(String postId) throws IOException {
        new RxWrapper<Void>().unwrap(rxTapglue.deleteLike(postId));
    }

    /**
     * @param postId id of the post to be commented.
     * @param comment {@link Comment comment}
     * @return created comment.
     * @throws IOException exceptions thrown will be IOExceptions when there are IO issues with the
     * connection it self, or the subclass TapglueError when there was an API error.
     * @see com.tapglue.android.http.TapglueError
     */
    public Comment createComment(String postId, Comment comment) throws IOException {
        return new RxWrapper<Comment>().unwrap(rxTapglue.createComment(postId, comment));
    }

    /**
     * delete comment.
     * @param postId id of the post that was commented.
     * @param commentId id of the comment to be deleted.
     * @throws IOException exceptions thrown will be IOExceptions when there are IO issues with the
     * connection it self, or the subclass TapglueError when there was an API error.
     * @see com.tapglue.android.http.TapglueError
     */
    public void deleteComment(String postId, String commentId) throws IOException {
        new RxWrapper<Void>().unwrap(rxTapglue.deleteComment(postId,commentId));
    }

    /**
     * Update comment.
     * @param postId id of the post that was commented.
     * @param commentId id of the comment to be updated.
     * @param comment {@link Comment comment} to replace the old comment.
     * @return updated comment.
     * @throws IOException exceptions thrown will be IOExceptions when there are IO issues with the
     * connection it self, or the subclass TapglueError when there was an API error.
     * @see com.tapglue.android.http.TapglueError
     */
    public Comment updateComment(String postId, String commentId, Comment comment) throws IOException {
        return new RxWrapper<Comment>().unwrap(rxTapglue.updateComment(postId, commentId, comment));
    }

    /**
     * Retrieve current users event feed.
     * @return list of {@link Event events}.
     * @throws IOException exceptions thrown will be IOExceptions when there are IO issues with the
     * connection it self, or the subclass TapglueError when there was an API error.
     * @see com.tapglue.android.http.TapglueError
     */
    public List<Event> retrieveEventFeed() throws IOException {
        return new RxWrapper<List<Event>>().unwrap(rxTapglue.retrieveEventFeed());
    }
}
