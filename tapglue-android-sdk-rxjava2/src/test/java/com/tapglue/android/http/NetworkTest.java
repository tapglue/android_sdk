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
import android.content.SharedPreferences;

import com.tapglue.android.RxPage;
import com.tapglue.android.entities.Comment;
import com.tapglue.android.entities.Connection;
import com.tapglue.android.entities.ConnectionList;
import com.tapglue.android.entities.Event;
import com.tapglue.android.entities.Like;
import com.tapglue.android.entities.Post;
import com.tapglue.android.entities.User;
import com.tapglue.android.http.payloads.EmailLoginPayload;
import com.tapglue.android.http.payloads.SocialConnections;
import com.tapglue.android.http.payloads.UsernameLoginPayload;
import com.tapglue.android.internal.SessionStore;
import com.tapglue.android.internal.Store;
import com.tapglue.android.internal.UUIDStore;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Function;
import io.reactivex.observers.TestObserver;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.whenNew;

@RunWith(PowerMockRunner.class)
@PrepareForTest(Network.class)
public class NetworkTest {
    private static final String EMAIL = "user@domain.com";
    private static final String USERNAME = "username";
    private static final String PASSWORD = "password";


    @Mock
    Context context;
    @Mock
    SharedPreferences prefs;
    @Mock
    ServiceFactory serviceFactory;
    @Mock
    TapglueService service;
    @Mock
    TapglueService secondService;
    @Mock
    SessionStore sessionStore;
    @Mock
    UUIDStore uuidStore;
    @Mock
    Function<User, User> storeFunc;
    @Mock
    Store<String> internalUUIDStore;
    @Mock
    ConnectionList connectionList;
    @Mock
    Action clearAction;
    @Mock
    UsersFeed usersFeed;
    @Mock
    User user;
    List<User> users = new ArrayList<User>();

    //SUT
    Network network;

    @Before
    public void setUp() throws Exception {
        when(context.getSharedPreferences(anyString(), anyInt())).thenReturn(prefs);
        when(service.login(isA(UsernameLoginPayload.class))).thenReturn(Observable.just(user));
        when(service.login(isA(EmailLoginPayload.class))).thenReturn(Observable.just(user));
        when(serviceFactory.createTapglueService()).thenReturn(service)
        .thenReturn(secondService);

        whenNew(UUIDStore.class).withAnyArguments().thenReturn(uuidStore);
        whenNew(SessionStore.class).withAnyArguments().thenReturn(sessionStore);
        when(uuidStore.get()).thenReturn(Observable.<String>empty());

        when(sessionStore.get()).thenReturn(Observable.<User>empty());
        when(sessionStore.store()).thenReturn(storeFunc);
        when(storeFunc.apply(user)).thenReturn(user);
        when(sessionStore.clear()).thenReturn(clearAction);
        network = new Network(serviceFactory, context);
    }

    @Test
    public void loginWithUsernameReturnsUserFromService() {
        TestObserver<User> to = network.loginWithUsername(USERNAME, PASSWORD).test();
        to.assertValue(user);
    }

    @Test
    public void loginWithEmailReturnsUserFromService() {
        TestObserver<User> to = network.loginWithEmail(EMAIL, PASSWORD).test();

        to.assertValue(user);
    }


    @Test
    public void usernameLoginSetsSessionTokenToServiceFactory() {
        when(user.getSessionToken()).thenReturn("sessionToken");
        network.loginWithUsername(USERNAME, PASSWORD).test();

        verify(serviceFactory).setSessionToken("sessionToken");
    }

    @Test
    public void emailLoginSetsSessionTokenToServiceFactory() {
        when(user.getSessionToken()).thenReturn("sessionToken");

        network.loginWithEmail(EMAIL, PASSWORD).test();

        verify(serviceFactory).setSessionToken("sessionToken");
    }

    @Test
    public void usernameLoginStoresSessionToken() throws Exception {
        when(user.getSessionToken()).thenReturn("sessionToken");

        network.loginWithUsername(USERNAME, PASSWORD).test();

        verify(storeFunc).apply(user);
    }

    @Test
    public void emailLoginStoresSessionToken() throws Exception {
        when(user.getSessionToken()).thenReturn("sessionToken");

        network.loginWithEmail(EMAIL, PASSWORD).test();

        verify(storeFunc).apply(user);
    }

    @Test
    public void usernameLoginCreatesNewService() {
        when(user.getSessionToken()).thenReturn("sessionToken");
        TestObserver<User> ts = new TestObserver<>();

        network.loginWithUsername(USERNAME, PASSWORD).test();

        assertThat(network.service, equalTo(secondService));
    }

    @Test
    public void logoutReturnsObservableFromNetwork() {
        when(service.logout()).thenReturn(Observable.<Void>empty());
        TestObserver<Void> to = network.logout().test();

        to.assertNoErrors();
        to.assertComplete();
    }

    @Test
    public void logoutClearsSession() throws Exception {
        when(service.logout()).thenReturn(Observable.<Void>empty());

        network.logout().test();

        verify(clearAction).run();
    }

    @Test
    public void createUserReturnsUserFromService() {
        when(service.createUser(user)).thenReturn(Observable.just(user));
        TestObserver<User> to = network.createUser(user).test();

        to.assertValue(user);
    }

    @Test
    public void deleteCurrentUserReturnsObservableFromService() {
        when(service.deleteCurrentUser()).thenReturn(Observable.<Void>empty());
        TestObserver<Void> to = network.deleteCurrentUser().test();

        to.assertNoErrors();
        to.assertComplete();
    }

    @Test
    public void deleteCurrentUserClearsSession() throws Exception {
        when(service.deleteCurrentUser()).thenReturn(Observable.<Void>empty());

        network.deleteCurrentUser().test();

        verify(clearAction).run();
    }

    @Test
    public void updateCurrentUserReturnsUserFromService() {
        when(service.updateCurrentUser(user)).thenReturn(Observable.just(user));
        TestObserver<User> to = network.updateCurrentUser(user).test();

        to.assertValue(user);
    }

    @Test
    public void updateCurrentUserStoresSessionToken() throws Exception {
        when(service.updateCurrentUser(user)).thenReturn(Observable.just(user));
        when(user.getSessionToken()).thenReturn("sessionToken");

        network.updateCurrentUser(user).test();

        verify(storeFunc).apply(user);
    }

    @Test
    public void updateCurrentUserCreatesNewService() {
        when(service.updateCurrentUser(user)).thenReturn(Observable.just(user));
        when(user.getSessionToken()).thenReturn("sessionToken");

        network.updateCurrentUser(user).test();

        verify(serviceFactory).setSessionToken("sessionToken");
    }

    @Test
    public void updateCurrentUserSetsSessionTokenToServiceFactory() {
        when(service.updateCurrentUser(user)).thenReturn(Observable.just(user));
        when(user.getSessionToken()).thenReturn("sessionToken");

        network.updateCurrentUser(user).test();

        assertThat(network.service, equalTo(secondService));
    }

    @Test
    public void refreshCurrentUserReturnsUserFromService() {
        when(service.refreshCurrentUser()).thenReturn(Observable.just(user));
        TestObserver<User> to = network.refreshCurrentUser().test();

        to.assertValue(user);
    }

    @Test
    public void refreshCurrentUserStoresSessionToken() throws Exception {
        when(service.refreshCurrentUser()).thenReturn(Observable.just(user));
        when(user.getSessionToken()).thenReturn("sessionToken");

        network.refreshCurrentUser().test();

        verify(storeFunc).apply(user);
    }

    @Test
    public void refreshCurrentUserCreatesNewService() {
        when(service.refreshCurrentUser()).thenReturn(Observable.just(user));
        when(user.getSessionToken()).thenReturn("sessionToken");

        network.refreshCurrentUser().test();

        verify(serviceFactory).setSessionToken("sessionToken");
    }

    @Test
    public void refreshCurrentUserSetsSessionTokenToServiceFactory() {
        when(service.refreshCurrentUser()).thenReturn(Observable.just(user));
        when(user.getSessionToken()).thenReturn("sessionToken");

        network.refreshCurrentUser().test();

        assertThat(network.service, equalTo(secondService));
    }

    @Test
    public void retrieveUserReturnsUserFromService() {
        String id = "someID";
        when(service.retrieveUser(id)).thenReturn(Observable.just(user));
        TestObserver<User> to = network.retrieveUser(id).test();

        to.assertValue(user);
    }

    @Test
    public void retrieveFollowignsReturnsUsersFromService() {
        when(usersFeed.getUsers()).thenReturn(users);
        when(service.retrieveFollowings()).thenReturn(Observable.just(usersFeed));
        TestObserver<RxPage<List<User>>> to = network.retrieveFollowings().test();

        List<User> result = to.values().get(0).getData();

        assertThat(result, equalTo(users));


    }

    @Test
    public void retrieveFollowersReturnsUsersFromService() {
        when(usersFeed.getUsers()).thenReturn(users);
        when(service.retrieveFollowers()).thenReturn(Observable.just(usersFeed));
        TestObserver<RxPage<List<User>>> ts = network.retrieveFollowers().test();

        List<User> result = ts.values().get(0).getData();

        assertThat(result, equalTo(users));
    }

    @Test
    public void retrieveUserFollowignsReturnsUsersFromService() {
        String id = "userId";
        when(usersFeed.getUsers()).thenReturn(users);
        when(service.retrieveUserFollowings(id)).thenReturn(Observable.just(usersFeed));
        TestObserver<RxPage<List<User>>> to = network.retrieveUserFollowings(id).test();

        List<User> result = to.values().get(0).getData();

        assertThat(result, equalTo(users));
    }

    @Test
    public void retrieveUserFollowersReturnsUsersFromService() {
        String id = "userId";
        when(usersFeed.getUsers()).thenReturn(users);
        when(service.retrieveUserFollowers(id)).thenReturn(Observable.just(usersFeed));
        TestObserver<RxPage<List<User>>> to = network.retrieveUserFollowers(id).test();

        List<User> result = to.values().get(0).getData();

        assertThat(result, equalTo(users));
    }

    @Test
    public void createConnectionReturnsConnectionFromService() {
        Connection connection = mock(Connection.class);
        when(service.createConnection(connection)).thenReturn(Observable.just(connection));
        TestObserver<Connection> to = network.createConnection(connection).test();

        to.assertValue(connection);
    }

    @Test
    public void createSocialConnectionsReturnsUsersFromService() {
        SocialConnections connections = mock(SocialConnections.class);
        when(usersFeed.getUsers()).thenReturn(users);
        when(service.createSocialConnections(connections)).thenReturn(Observable.just(usersFeed));
        TestObserver<List<User>> to = network.createSocialConnections(connections).test();

        to.assertValue(users);
    }

    @Test
    public void deleteConnectionReturnsFromService() {
        String id = "userId";
        Connection.Type type = Connection.Type.FOLLOW;
        when(service.deleteConnection(id, type)).thenReturn(Observable.<Void>empty());
        TestObserver<Void> to = network.deleteConnection(id, type).test();

        to.assertNoErrors();
        to.assertComplete();
    }

    @Test
    public void createPostReturnsPostFromService() {
        Post post = mock(Post.class);
        when(service.createPost(post)).thenReturn(Observable.just(post));
        TestObserver<Post> to = network.createPost(post).test();

        to.assertValue(post);
    }

    @Test
    public void retrievePostReturnsPostFromService() {
        Post post = mock(Post.class);
        String id = "id";
        when(service.retrievePost(id)).thenReturn(Observable.just(post));
        TestObserver<Post> to = network.retrievePost(id).test();

        to.assertValue(post);
    }

    @Test
    public void updatePostReturnsPostFromService() {
        Post post = mock(Post.class);
        String id = "id";
        when(service.updatePost(id, post)).thenReturn(Observable.just(post));
        TestObserver<Post> to = network.updatePost(id, post).test();

        to.assertValue(post);
    }

    @Test
    public void deletePostReturnsFromService() {
        String id = "id1231";
        when(service.deletePost(id)).thenReturn(Observable.<Void>empty());
        TestObserver<Void> ts = network.deletePost(id).test();

        ts.assertNoErrors();
        ts.assertComplete();
    }

    @Test
    public void createLikeReturnsLikeFromService() {
        String id = "postId";
        Like like = mock(Like.class);
        when(service.createLike(id)).thenReturn(Observable.just(like));
        TestObserver<Like> to = network.createLike(id).test();

        to.assertValue(like);
    }

    @Test
    public void deleteLikeDeletesOnService() {
        String id = "postId";
        when(service.deleteLike(id)).thenReturn(Observable.<Void>empty());
        TestObserver<Void> to = network.deleteLike(id).test();

        to.assertNoErrors();
        to.assertComplete();
    }

    @Test
    public void createCommentPostsToService() {
        String id = "postId";
        Comment comment = mock(Comment.class);
        when(service.createComment(id, comment)).thenReturn(Observable.just(comment));
        TestObserver<Comment> to = network.createComment(id, comment).test();

        to.assertValue(comment);
    }

    @Test
    public void deleteCommentDeletesOnService() {
        String postId = "postId";
        String commentId = "commentiD";
        when(service.deleteComment(postId, commentId)).thenReturn(Observable.<Void>empty());
        TestObserver<Void> to = network.deleteComment(postId, commentId).test();

        to.assertNoErrors();
        to.assertComplete();
    }

    @Test
    public void updateCommentUpdatesOnService() {
        String postId = "postId";
        String commentId = "commentID";
        Comment comment = mock(Comment.class);
        when(service.updateComment(postId, commentId, comment)).thenReturn(Observable.just(comment));
        TestObserver<Comment> to = network.updateComment(postId, commentId, comment).test();

        to.assertNoErrors();
        to.assertComplete();
    }

    @Test
    public void retrieveEventFeedRetrievesFromService() throws Exception {
        List<Event> events = mock(List.class);
        EventListFeed feed = mock(EventListFeed.class);
        EventFeedToList converter = mock(EventFeedToList.class);
        whenNew(EventFeedToList.class).withNoArguments().thenReturn(converter);
        when(converter.apply(feed)).thenReturn(events);
        when(service.retrieveEventFeed()).thenReturn(Observable.just(feed));
        TestObserver<List<Event>> ts = network.retrieveEventFeed().test();

        ts.assertValue(events);
    }

    @Test
    public void sendAnalyticsCallsService() {
        when(service.sendAnalytics()).thenReturn(Observable.<Void>empty());
        TestObserver<Void> to = network.sendAnalytics().test();

        to.assertNoErrors();
        to.assertComplete();
    }
}