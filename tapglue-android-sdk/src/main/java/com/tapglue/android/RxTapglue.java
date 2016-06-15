/**
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
package com.tapglue.android;

import android.content.Context;

import java.util.List;

import com.tapglue.android.entities.Comment;
import com.tapglue.android.entities.Connection;
import com.tapglue.android.entities.Connection.Type;
import com.tapglue.android.entities.ConnectionList;
import com.tapglue.android.entities.Event;
import com.tapglue.android.entities.Like;
import com.tapglue.android.entities.NewsFeed;
import com.tapglue.android.entities.Post;
import com.tapglue.android.entities.User;
import com.tapglue.android.http.Network;
import com.tapglue.android.http.ServiceFactory;
import com.tapglue.android.http.payloads.SocialConnections;
import com.tapglue.android.internal.UserStore;
import com.tapglue.android.sims.TapglueSims;

import java.util.concurrent.atomic.AtomicBoolean;

import rx.Observable;
import rx.exceptions.OnErrorNotImplementedException;
import rx.functions.Action0;

public class RxTapglue {

    private static TapglueSims sims;
    private static AtomicBoolean firstInstance = new AtomicBoolean(true);
    private Network network;
    private UserStore currentUser;

    /**
     * @param configuration configuration of the tapglue instance
     * @param context the context will be used for persisting session token and current user
     */
    public RxTapglue(Configuration configuration, Context context) {
        this.network = new Network(new ServiceFactory(configuration), context);
        this.currentUser = new UserStore(context);
        initializeSims(configuration, context);
        if(firstInstance.compareAndSet(true, false)) {
            try {
                network.sendAnalytics().subscribeOn(TapglueSchedulers.analytics()).subscribe();
            } catch(OnErrorNotImplementedException e) {
                firstInstance.set(true);
            }
        }
    }

    /**
     * Logs in user with username and password. The user is persisted and can be requested by calling
     * {@link #getCurrentUser() getCurrentUser} and will be persisted until explicit log out.
     * @param username username of the user to be logged in
     * @param password password of the user to be logged in
     * @return the {@link com.tapglue.android.entities.User user} that was logged in
     * @see com.tapglue.android.http.TapglueError
     */
    public Observable<User> loginWithUsername(String username, String password) {
        return network.loginWithUsername(username, password).map(currentUser.store())
                .doOnCompleted(new SimsSessionTokenNotifier());
    }

    /**
     * Logs in user with email and password. The user is persisted and can be requested by calling
     * {@link #getCurrentUser() getCurrentUser} and will be persisted until explicit log out.
     * @param email email of the user to be logged in
     * @param password password of the user to be logged in
     * @return the {@link com.tapglue.android.entities.User user} that was logged in
     * @see com.tapglue.android.http.TapglueError
     */
    public Observable<User> loginWithEmail(String email, String password) {
        return network.loginWithEmail(email, password).map(currentUser.store())
                .doOnCompleted(new SimsSessionTokenNotifier());
    }

    /**
     * Logs out user. This will delete the persisted current user.
     */
    public Observable<Void> logout() {
        return network.logout().doOnCompleted(currentUser.clear());
    }

    /**
     * Gets the persisted current user. Will only be available after a successful login.
     * @return the current {@link com.tapglue.android.entities.User user}
     */
    public Observable<User> getCurrentUser() {
        return currentUser.get();
    }

    /**
     * Creates a user.
     * @param user the user to create
     * @return the created {@link com.tapglue.android.entities.User user}.
     */
    public Observable<User> createUser(User user) {
        return network.createUser(user);
    }

    /**
     * Deletes current user.
     */
    public Observable<Void> deleteCurrentUser() {
        return network.deleteCurrentUser().doOnCompleted(currentUser.clear());
    }

    /**
     * Updates current user
     * @param user The updated user
     * @return updated {@link com.tapglue.android.entities.User user}.
     */
    public Observable<User> updateCurrentUser(User user) {
        return network.updateCurrentUser(user).map(currentUser.store())
                .doOnCompleted(new SimsSessionTokenNotifier());
    }

    /**
     * refreshses the persisted current user. After a successful call the refreshed user will be
     * persisted and available at {@link #getCurrentUser() getCurrentUser}
     * @return refreshed current {@link com.tapglue.android.entities.User user}.
     */
    public Observable<User> refreshCurrentUser() {
        return network.refreshCurrentUser().map(currentUser.store())
                .doOnCompleted(new SimsSessionTokenNotifier());
    }

    /**
     * Retrieve user.
     * @param id user id of the wanted user
     * @return the {@link com.tapglue.android.entities.User user}.
     */
    public Observable<User> retrieveUser(String id) {
        return network.retrieveUser(id);
    }

    /**
     * retrieve the users followed by the current user
     * @return List of followed {@link com.tapglue.android.entities.User users}.
     */
    public Observable<List<User>> retrieveFollowings() {
        return network.retrieveFollowings();
    }

    /**
     * retrieve the users following the current user.
     * @return List of {@link com.tapglue.android.entities.User users} following the current user.
     */
    public Observable<List<User>> retrieveFollowers() {
        return network.retrieveFollowers();
    }

    /**
     * retrieves users followed by a user
     * @param  userId user id of the user of whom we want the followings
     * @return        list of users followed
     */
    public Observable<List<User>> retrieveUserFollowings(String userId) {
        return network.retrieveUserFollowings(userId);
    }

    /**
     * retrieves users following a user
     * @param  userId user id of the users of whom we want the followers
     * @return        list of users following
     */
    public Observable<List<User>> retrieveUserFollowers(String userId) {
        return network.retrieveUserFollowers(userId);
    }

    /**
     * Retrieve friends of the current user.
     * @return list of {@link com.tapglue.android.entities.User friends}.
     */
    public Observable<List<User>> retrieveFriends() {
        return network.retrieveFriends();
    }

    /**
     * retrieves the list of friends of a user.
     * @param  userId user id of the user of whom we want the friend list
     * @return        list of friends
     */
    public Observable<List<User>> retrieveUserFriends(String userId) {
        return network.retrieveUserFriends(userId);
    }

    /**
     * @return list of {@link com.tapglue.android.entities.Connection connections} in a pending state.
     */
    public Observable<ConnectionList> retrievePendingConnections() {
        return network.retrievePendingConnections();
    }

    /**
     * @return list of {@link com.tapglue.android.entities.Connection connections} in a rejected state.
     */
    public Observable<ConnectionList> retrieveRejectedConnections() {
        return network.retrieveRejectedConnections();
    }

    /**
     * @param connection {@link com.tapglue.android.entities.Connection connection} to be created
     * @return the created {@link com.tapglue.android.entities.Connection connection}
     */
    public Observable<Connection> createConnection(Connection connection) {
        return network.createConnection(connection);
    }

    /**
     * create connections with users retrieved from other social networks
     * @param connections the {@link com.tapglue.android.http.payloads.SocialConnections connections}
     * @return list of users to whom connections were created
     */
    public Observable<List<User>> createSocialConnections(SocialConnections connections) {
        return network.createSocialConnections(connections);
    }

    public Observable<Void> deleteConnection(String userId, Type type) {
        return network.deleteConnection(userId, type);
    }

    /**
     * Search will be conducted as in specified in the web documentation
     * @param searchTerm
     * @return search result as a list of {@link com.tapglue.android.entities.User users}.
     */
    public Observable<List<User>> searchUsers(String searchTerm) {
        return network.searchUsers(searchTerm);
    }

    /**
     * Search for users on tapglue by email.
     * @param emails emails to search for.
     * @return search result as a list of {@link com.tapglue.android.entities.User users}.
     */
    public Observable<List<User>> searchUsersByEmail(List<String> emails) {
        return network.searchUsersByEmail(emails);
    }

    /**
     * Search for users on tapglue by social ids belonging to another social platform.
     * @param platform the platform the ids belong to.
     * @param socialIds the userIds to search for.
     * @return search result as a list of {@link com.tapglue.android.entities.User users}.
     */
    public Observable<List<User>> searchUsersBySocialIds(String platform, List<String> socialIds) {
        return network.searchUsersBySocialIds(platform, socialIds);
    }

    /**
     * @param post {@link com.tapglue.android.entities.Post post} to be created.
     * @return created post.
     */
    public Observable<Post> createPost(Post post) {
        return network.createPost(post);
    }

    /**
     * @param id id of the post to be retrieved.
     * @return the retrieved {@link com.tapglue.android.entities.Post post}.
     */
    public Observable<Post> retrievePost(String id) {
        return network.retrievePost(id);
    }

    /**
     * @param id id of the {@link com.tapglue.android.entities.Post post} to be updated.
     * @param post new post that will replace the old post.
     * @return the updated post.
     */
    public Observable<Post> updatePost(String id, Post post) {
        return network.updatePost(id, post);
    }

    /**
     * @param id id of the {@link com.tapglue.android.entities.Post post} to be deleted.
     */
    public Observable<Void> deletePost(String id) {
        return network.deletePost(id);
    }

    /**
     * @return all available {@link com.tapglue.android.entities.Post posts} on the network.
     */
    public Observable<List<Post>> retrievePosts() {
        return network.retrievePosts();
    }

    /**
     * retrive all posts by a user.
     * @param userId id of the user of whom the posts are.
     * @return posts created by the user defined by userId
     */
    public Observable<List<Post>> retrievePostsByUser(String userId) {
        return network.retrievePostsByUser(userId);
    }

    /**
     * creates a like event on a post.
     * @param postId id of the post to be liked.
     * @return created like event.
     */
    public Observable<Like> createLike(String postId) {
        return network.createLike(postId);
    }

    /**
     * Deletes like.
     * @param postId id of the post that was liked.
     */
    public Observable<Void> deleteLike(String postId) {
        return network.deleteLike(postId);
    }

    /**
     * Retrieve all likes for a post.
     * @param postId id for which the likes will be retrieved.
     * @return likes.
     */
    public Observable<List<Like>> retrieveLikesForPost(String postId) {
        return network.retrieveLikesForPost(postId);
    }

    /**
     * @param postId id of the post to be commented.
     * @param comment {@link com.tapglue.android.entities.Comment comment}
     * @return created comment.
     */
    public Observable<Comment> createComment(String postId, Comment comment) {
        return network.createComment(postId, comment);
    }

    /**
     * delete comment.
     * @param postId id of the post that was commented.
     * @param commentId id of the comment to be deleted.
     */
    public Observable<Void> deleteComment(String postId, String commentId) {
        return network.deleteComment(postId, commentId);
    }

    /**
     * Update comment.
     * @param postId id of the post that was commented.
     * @param commentId id of the comment to be updated.
     * @param comment {@link com.tapglue.android.entities.Comment comment} to replace the old comment.
     * @return updated comment.
     */
    public Observable<Comment> updateComment(String postId, String commentId, Comment comment) {
        return network.updateComment(postId, commentId, comment);
    }

    /**
     * retrieves all comments for a post.
     * @param postId id of the post for which the comments will be retrieved.
     * @return comments
     */
    public Observable<List<Comment>> retrieveCommentsForPost(String postId) {
        return network.retrieveCommentsForPost(postId);
    }

    /**
     * Retrieve current users post feed.
     * @return list of {@link com.tapglue.android.entities.Post posts}.
     */
    public Observable<List<Post>> retrievePostFeed() {
        return network.retrievePostFeed();
    }

    /**
     * Retrieve current users event feed.
     * @return list of {@link com.tapglue.android.entities.Event events}.
     */
    public Observable<List<Event>> retrieveEventFeed() {
        return network.retrieveEventFeed();
    }

    /**
     * Retrieve current users news feed.
     * @return {@link com.tapglue.android.entities.NewsFeed news feed}.
     */
    public Observable<NewsFeed> retrieveNewsFeed() {
        return network.retrieveNewsFeed();
    }

    private void initializeSims(Configuration configuration, Context context) {
        if(sims == null) {
            synchronized(RxTapglue.class) {
                if(sims == null) {
                    sims = new TapglueSims(configuration, context);
                }
            }
        }
    }

    private static class SimsSessionTokenNotifier implements Action0 {
        @Override
        public void call() {
            sims.sessionTokenChanged();
        }
    }
}
