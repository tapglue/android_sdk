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
package com.tapglue.android.http;

import android.content.Context;

import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.tapglue.android.RxPage;
import com.tapglue.android.internal.SessionStore;
import com.tapglue.android.internal.UUIDStore;
import com.tapglue.android.entities.Comment;
import com.tapglue.android.entities.Connection;
import com.tapglue.android.entities.Connection.Type;
import com.tapglue.android.entities.ConnectionList;
import com.tapglue.android.entities.Event;
import com.tapglue.android.entities.Like;
import com.tapglue.android.entities.NewsFeed;
import com.tapglue.android.entities.Post;
import com.tapglue.android.entities.User;
import com.tapglue.android.http.payloads.SocialConnections;
import com.tapglue.android.http.payloads.EmailLoginPayload;
import com.tapglue.android.http.payloads.EmailSearchPayload;
import com.tapglue.android.http.payloads.SocialSearchPayload;
import com.tapglue.android.http.payloads.UsernameLoginPayload;

import java.util.List;
import java.util.ArrayList;

import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func1;

public class Network {

    TapglueService service;
    PaginatedService paginatedService;
    private ServiceFactory serviceFactory;
    private SessionStore sessionStore;
    private UUIDStore uuidStore;

    public Network(ServiceFactory serviceFactory, Context context) {
        this.serviceFactory = serviceFactory;
        service = serviceFactory.createTapglueService();
        paginatedService = serviceFactory.createPaginatedService();
        sessionStore = new SessionStore(context);
        uuidStore = new UUIDStore(context);
        uuidStore.get().doOnNext(new UUIDAction()).subscribe();
        sessionStore.get().map(new SessionTokenExtractor());
    }

    public Observable<User> loginWithUsername(String username, String password) {
        UsernameLoginPayload payload = new UsernameLoginPayload(username, password);
        return service.login(payload).map(new SessionTokenExtractor()).map(sessionStore.store());
    }

    public Observable<User> loginWithEmail(String email, String password) {
        EmailLoginPayload payload = new EmailLoginPayload(email, password);
        return service.login(payload).map(new SessionTokenExtractor()).map(sessionStore.store());
    }

    public Observable<Void> logout() {
        return service.logout().doOnCompleted(sessionStore.clear());
    }

    public Observable<User> createUser(User user) {
        return service.createUser(user);
    }

    public Observable<Void> deleteCurrentUser() {
        return service.deleteCurrentUser().doOnCompleted(sessionStore.clear());
    }

    public Observable<User> updateCurrentUser(User user) {
        return service.updateCurrentUser(user)
               .map(new SessionTokenExtractor()).map(sessionStore.store());
    }

    public Observable<User> retrieveUser(String id) {
        return service.retrieveUser(id);
    }

    public Observable<User> refreshCurrentUser() {
        return service.refreshCurrentUser()
               .map(new SessionTokenExtractor()).map(sessionStore.store());
    }

    public Observable<List<User>> retrieveFollowings() {
        return service.retrieveFollowings().map(new UsersExtractor());
    }

    public Observable<List<User>> retrieveFollowers() {
        return service.retrieveFollowers().map(new UsersExtractor());
    }

    public Observable<List<User>> retrieveUserFollowings(String userId) {
        return service.retrieveUserFollowings(userId).map(new UsersExtractor());
    }

    public Observable<List<User>> retrieveUserFollowers(String userId) {
        return service.retrieveUserFollowers(userId).map(new UsersExtractor());
    }

    public Observable<List<User>> retrieveFriends() {
        return service.retrieveFriends().map(new UsersExtractor());
    }

    public Observable<List<User>> retrieveUserFriends(String userId) {
        return service.retrieveUserFriends(userId).map(new UsersExtractor());
    }

    public Observable<Connection> createConnection(Connection connection) {
        return service.createConnection(connection);
    }

    public Observable<List<User>> createSocialConnections(SocialConnections connections) {
        return service.createSocialConnections(connections).map(new UsersExtractor());
    }

    public Observable<Void> deleteConnection(String userId, Type type) {
        return service.deleteConnection(userId, type);
    }

    public Observable<List<User>> searchUsers(String searchTerm) {
        return service.searchUsers(searchTerm).map(new UsersExtractor());
    }

    public Observable<List<User>> searchUsersByEmail(List<String> emails) {
        return service.searchUsersByEmail(new EmailSearchPayload(emails)).map(new UsersExtractor());
    }

    public Observable<List<User>> searchUsersBySocialIds(String platform, List<String> socialIds) {
        return service.searchUsersBySocialIds(platform, new SocialSearchPayload(socialIds)).map(new UsersExtractor());
    }

    public Observable<ConnectionList> retrievePendingConnections() {
        return service.retrievePendingConnections().map(new ConnectionFeedToList());
    }

    public Observable<ConnectionList> retrieveRejectedConnections() {
        return service.retrieveRejectedConnections().map(new ConnectionFeedToList());
    }

    public Observable<Post> createPost(Post post) {
        return service.createPost(post);
    }

    public Observable<Post> retrievePost(String id) {
        return service.retrievePost(id);
    }

    public Observable<Post> updatePost(String id, Post post) {
        return service.updatePost(id, post);
    }

    public Observable<Void> deletePost(String id) {
        return service.deletePost(id);
    }

    public Observable<List<Post>> retrievePosts() {
        return service.retrievePosts().map(new PostFeedToList());
    }

    public Observable<List<Post>> retrievePostsByUser(String id) {
        return service.retrievePostsByUser(id).map(new PostFeedToList());
    }

    public Observable<Like> createLike(String id) {
        return service.createLike(id);
    }

    public Observable<Void> deleteLike(String id) {
        return service.deleteLike(id);
    }

    public Observable<List<Like>> retrieveLikesForPost(String id) {
        return service.retrieveLikesForPost(id).map(new LikesFeedToList());
    }

    public Observable<RxPage<List<Like>>> retrieveLikesByUser(String userId) {
        return paginatedService.retrieveLikesByUser(userId).map(new RxPageCreator<List<Like>>(this));
    }

    public Observable<Comment> createComment(String postId, Comment comment) {
        return service.createComment(postId, comment);
    }

    public Observable<Void> deleteComment(String postId, String commentId) {
        return service.deleteComment(postId, commentId);
    }

    public Observable<Comment> updateComment(String postId, String commentId, Comment comment) {
        return service.updateComment(postId, commentId, comment);
    }

    public Observable<Void> sendAnalytics() {
        return service.sendAnalytics();
    }

    public Observable<List<Comment>> retrieveCommentsForPost(String postId) {
        return service.retrieveCommentsForPost(postId).map(new CommentsFeedToList());
    }

    public Observable<List<Post>> retrievePostFeed() {
        return service.retrievePostFeed().map(new PostFeedToList());
    }

    public Observable<List<Event>> retrieveEventsByUser(String userId) {
        return service.retrieveEventsByUser(userId).map(new EventFeedToList());
    }

    public Observable<List<Event>> retrieveEventFeed() {
        return service.retrieveEventFeed().map(new EventFeedToList());
    }

    public Observable<RxPage<NewsFeed>> retrieveNewsFeed() {
        return paginatedService.retrieveNewsFeed().map(new RxPageCreator<NewsFeed>(this));
    }

    public Observable<List<Event>> retrieveMeFeed() {
        return service.retrieveMeFeed().map(new EventFeedToList());
    }

    public Observable<JsonObject> paginatedGet(String pointer) {
        return service.paginatedGet(pointer);
    }

    private class SessionTokenExtractor implements Func1<User, User> {

        @Override
        public User call(User user) {
            serviceFactory.setSessionToken(user.getSessionToken());
            service = serviceFactory.createTapglueService();
            paginatedService = serviceFactory.createPaginatedService();
            return user;
        }
    }

    private class UsersExtractor implements Func1<UsersFeed, List<User>> {
        @Override
        public List<User> call(UsersFeed feed) {
            if(feed == null || feed.getUsers() == null) {
                return new ArrayList<>();
            }
            return feed.getUsers();
        }
    }

    private class UUIDAction implements Action1<String> {

        @Override
        public void call(String uuid) {
            serviceFactory.setUserUUID(uuid);
            service = serviceFactory.createTapglueService();
            paginatedService = serviceFactory.createPaginatedService();
        }
    }

    private static class RxPageCreator<T> implements Func1<FlattenableFeed<T>, RxPage<T>> {
        Network network;

        RxPageCreator(Network network) {
            this.network = network;
        }

        @Override
        public RxPage<T> call(FlattenableFeed<T> feed) {
            return new RxPage<>(feed, network);
        }

    }
}
