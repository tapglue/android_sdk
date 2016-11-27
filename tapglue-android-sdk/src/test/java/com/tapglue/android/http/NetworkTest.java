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

import com.tapglue.android.internal.SessionStore;
import com.tapglue.android.internal.Store;
import com.tapglue.android.internal.UUIDStore;
import com.tapglue.android.entities.Comment;
import com.tapglue.android.entities.Connection;
import com.tapglue.android.entities.Connection.Type;
import com.tapglue.android.entities.ConnectionList;
import com.tapglue.android.entities.Event;
import com.tapglue.android.entities.Like;
import com.tapglue.android.entities.Post;
import com.tapglue.android.entities.User;
import com.tapglue.android.http.payloads.SocialConnections;
import com.tapglue.android.http.payloads.EmailLoginPayload;
import com.tapglue.android.http.payloads.UsernameLoginPayload;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.List;

import rx.Observable;
import rx.functions.Action0;
import rx.functions.Func1;
import rx.observers.TestSubscriber;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsCollectionContaining.hasItems;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.hamcrest.core.Every.everyItem;
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
    Func1<User, User> storeFunc;
    @Mock
    Store<String> internalUUIDStore;
    @Mock
    ConnectionList connectionList;
    @Mock
    Action0 clearAction;
    @Mock
    UsersFeed usersFeed;
    @Mock
    User user;
    @Mock
    List<User> users;

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
        when(storeFunc.call(user)).thenReturn(user);
        when(sessionStore.clear()).thenReturn(clearAction);
        network = new Network(serviceFactory, context);
    }

    @Test
    public void loginWithUsernameReturnsUserFromService() {
        TestSubscriber<User> ts = new TestSubscriber<>();

        network.loginWithUsername(USERNAME, PASSWORD).subscribe(ts);

        assertThat(ts.getOnNextEvents(), hasItems(user));
    }

    @Test
    public void loginWithEmailReturnsUserFromService() {
        TestSubscriber<User> ts = new TestSubscriber<>();

        network.loginWithEmail(EMAIL, PASSWORD).subscribe(ts);

        assertThat(ts.getOnNextEvents(), hasItems(user));
    }


    @Test
    public void usernameLoginSetsSessionTokenToServiceFactory() {
        when(user.getSessionToken()).thenReturn("sessionToken");
        TestSubscriber<User> ts = new TestSubscriber<>();

        network.loginWithUsername(USERNAME, PASSWORD).subscribe(ts);

        verify(serviceFactory).setSessionToken("sessionToken");
    }

    @Test
    public void emailLoginSetsSessionTokenToServiceFactory() {
        when(user.getSessionToken()).thenReturn("sessionToken");
        TestSubscriber<User> ts = new TestSubscriber<>();

        network.loginWithEmail(EMAIL, PASSWORD).subscribe(ts);

        verify(serviceFactory).setSessionToken("sessionToken");
    }

    @Test
    public void usernameLoginStoresSessionToken() {
        when(user.getSessionToken()).thenReturn("sessionToken");
        TestSubscriber<User> ts = new TestSubscriber<>();

        network.loginWithUsername(USERNAME, PASSWORD).subscribe(ts);

        verify(storeFunc).call(user);
    }

    @Test
    public void emailLoginStoresSessionToken() {
        when(user.getSessionToken()).thenReturn("sessionToken");
        TestSubscriber<User> ts = new TestSubscriber<>();

        network.loginWithEmail(EMAIL, PASSWORD).subscribe(ts);

        verify(storeFunc).call(user);
    }

    @Test
    public void usernameLoginCreatesNewService() {
        when(user.getSessionToken()).thenReturn("sessionToken");
        TestSubscriber<User> ts = new TestSubscriber<>();

        network.loginWithUsername(USERNAME, PASSWORD).subscribe(ts);

        assertThat(network.service, equalTo(secondService));
    }

    @Test
    public void logoutReturnsObservableFromNetwork() {
        when(service.logout()).thenReturn(Observable.<Void>empty());
        TestSubscriber<Void> ts = new TestSubscriber<>();

        network.logout().subscribe(ts);

        ts.assertNoErrors();
        ts.assertCompleted();
    }

    @Test
    public void logoutClearsSession() {
        when(service.logout()).thenReturn(Observable.<Void>empty());
        TestSubscriber<Void> ts = new TestSubscriber<>();

        network.logout().subscribe(ts);

        verify(clearAction).call();
    }

    @Test
    public void createUserReturnsUserFromService() {
        when(service.createUser(user)).thenReturn(Observable.just(user));
        TestSubscriber<User> ts = new TestSubscriber<>();

        network.createUser(user).subscribe(ts);

        assertThat(ts.getOnNextEvents(), hasItems(user));
    }

    @Test
    public void deleteCurrentUserReturnsObservableFromService() {
        when(service.deleteCurrentUser()).thenReturn(Observable.<Void>empty());
        TestSubscriber<Void> ts = new TestSubscriber<>();

        network.deleteCurrentUser().subscribe(ts);

        ts.assertNoErrors();
        ts.assertCompleted();
    }

    @Test
    public void deleteCurrentUserClearsSession() {
        when(service.deleteCurrentUser()).thenReturn(Observable.<Void>empty());
        TestSubscriber<Void> ts = new TestSubscriber<>();

        network.deleteCurrentUser().subscribe(ts);

        verify(clearAction).call();
    }

    @Test
    public void updateCurrentUserReturnsUserFromService() {
        when(service.updateCurrentUser(user)).thenReturn(Observable.just(user));
        TestSubscriber<User> ts = new TestSubscriber<>();

        network.updateCurrentUser(user).subscribe(ts);

        assertThat(ts.getOnNextEvents(), hasItems(user));
    }

    @Test
    public void updateCurrentUserStoresSessionToken() {
        when(service.updateCurrentUser(user)).thenReturn(Observable.just(user));
        when(user.getSessionToken()).thenReturn("sessionToken");
        TestSubscriber<User> ts = new TestSubscriber<>();

        network.updateCurrentUser(user).subscribe(ts);

        verify(storeFunc).call(user);
    }

    @Test
    public void updateCurrentUserCreatesNewService() {
        when(service.updateCurrentUser(user)).thenReturn(Observable.just(user));
        when(user.getSessionToken()).thenReturn("sessionToken");
        TestSubscriber<User> ts = new TestSubscriber<>();

        network.updateCurrentUser(user).subscribe(ts);

        verify(serviceFactory).setSessionToken("sessionToken");
    }

    @Test
    public void updateCurrentUserSetsSessionTokenToServiceFactory() {
        when(service.updateCurrentUser(user)).thenReturn(Observable.just(user));
        when(user.getSessionToken()).thenReturn("sessionToken");
        TestSubscriber<User> ts = new TestSubscriber<>();

        network.updateCurrentUser(user).subscribe(ts);

        assertThat(network.service, equalTo(secondService));
    }

    @Test
    public void refreshCurrentUserReturnsUserFromService() {
        when(service.refreshCurrentUser()).thenReturn(Observable.just(user));
        TestSubscriber<User> ts = new TestSubscriber<>();

        network.refreshCurrentUser().subscribe(ts);

        assertThat(ts.getOnNextEvents(), hasItems(user));
    }

    @Test
    public void refreshCurrentUserStoresSessionToken() {
        when(service.refreshCurrentUser()).thenReturn(Observable.just(user));
        when(user.getSessionToken()).thenReturn("sessionToken");
        TestSubscriber<User> ts = new TestSubscriber<>();

        network.refreshCurrentUser().subscribe(ts);

        verify(storeFunc).call(user);
    }

    @Test
    public void refreshCurrentUserCreatesNewService() {
        when(service.refreshCurrentUser()).thenReturn(Observable.just(user));
        when(user.getSessionToken()).thenReturn("sessionToken");
        TestSubscriber<User> ts = new TestSubscriber<>();

        network.refreshCurrentUser().subscribe(ts);

        verify(serviceFactory).setSessionToken("sessionToken");
    }

    @Test
    public void refreshCurrentUserSetsSessionTokenToServiceFactory() {
        when(service.refreshCurrentUser()).thenReturn(Observable.just(user));
        when(user.getSessionToken()).thenReturn("sessionToken");
        TestSubscriber<User> ts = new TestSubscriber<>();

        network.refreshCurrentUser().subscribe(ts);

        assertThat(network.service, equalTo(secondService));
    }

    @Test
    public void retrieveUserReturnsUserFromService() {
        String id = "someID";
        when(service.retrieveUser(id)).thenReturn(Observable.just(user));
        TestSubscriber<User> ts = new TestSubscriber<>();

        network.retrieveUser(id).subscribe(ts);

        assertThat(ts.getOnNextEvents(), hasItems(user));
    }

    @Test
    public void retrieveFollowignsReturnsUsersFromService() {
        when(usersFeed.getUsers()).thenReturn(users);
        when(service.retrieveFollowings()).thenReturn(Observable.just(usersFeed));
        TestSubscriber<List<User>> ts = new TestSubscriber<>();

        network.retrieveFollowings().subscribe(ts);

        assertThat(ts.getOnNextEvents(), hasItems(users));
    }

    @Test
    public void retrieveFollowersReturnsUsersFromService() {
        when(usersFeed.getUsers()).thenReturn(users);
        when(service.retrieveFollowers()).thenReturn(Observable.just(usersFeed));
        TestSubscriber<List<User>> ts = new TestSubscriber<>();

        network.retrieveFollowers().subscribe(ts);

        assertThat(ts.getOnNextEvents(), hasItems(users));
    }

    @Test
    public void retrieveUserFollowignsReturnsUsersFromService() {
        String id = "userId";
        when(usersFeed.getUsers()).thenReturn(users);
        when(service.retrieveUserFollowings(id)).thenReturn(Observable.just(usersFeed));
        TestSubscriber<List<User>> ts = new TestSubscriber<>();

        network.retrieveUserFollowings(id).subscribe(ts);

        assertThat(ts.getOnNextEvents(), hasItems(users));
    }

    @Test
    public void retrieveUserFollowersReturnsUsersFromService() {
        String id = "userId";
        when(usersFeed.getUsers()).thenReturn(users);
        when(service.retrieveUserFollowers(id)).thenReturn(Observable.just(usersFeed));
        TestSubscriber<List<User>> ts = new TestSubscriber<>();

        network.retrieveUserFollowers(id).subscribe(ts);

        assertThat(ts.getOnNextEvents(), hasItems(users));
    }

    @Test
    public void createConnectionReturnsConnectionFromService() {
        Connection connection = mock(Connection.class);
        when(service.createConnection(connection)).thenReturn(Observable.just(connection));
        TestSubscriber<Connection> ts = new TestSubscriber<>();

        network.createConnection(connection).subscribe(ts);

        assertThat(ts.getOnNextEvents(), hasItems(connection));
    }

    @Test
    public void createSocialConnectionsReturnsUsersFromService() {
        SocialConnections connections = mock(SocialConnections.class);
        when(usersFeed.getUsers()).thenReturn(users);
        when(service.createSocialConnections(connections)).thenReturn(Observable.just(usersFeed));
        TestSubscriber<List<User>> ts = new TestSubscriber<>();

        network.createSocialConnections(connections).subscribe(ts);

        assertThat(ts.getOnNextEvents(), hasItems(users));
    }

    @Test
    public void deleteConnectionReturnsFromService() {
        String id = "userId";
        Type type = Type.FOLLOW;
        when(service.deleteConnection(id, type)).thenReturn(Observable.<Void>empty());
        TestSubscriber<Void> ts = new TestSubscriber<>();

        network.deleteConnection(id, type).subscribe(ts);

        ts.assertNoErrors();
        ts.assertCompleted();
    }

    @Test
    public void createPostReturnsPostFromService() {
        Post post = mock(Post.class);
        when(service.createPost(post)).thenReturn(Observable.just(post));
        TestSubscriber<Post> ts = new TestSubscriber<>();

        network.createPost(post).subscribe(ts);

        assertThat(ts.getOnNextEvents(), hasItems(post));
    }

    @Test
    public void retrievePostReturnsPostFromService() {
        Post post = mock(Post.class);
        String id = "id";
        when(service.retrievePost(id)).thenReturn(Observable.just(post));
        TestSubscriber<Post> ts = new TestSubscriber<>();

        network.retrievePost(id).subscribe(ts);

        assertThat(ts.getOnNextEvents(), hasItems(post));
    }

    @Test
    public void updatePostReturnsPostFromService() {
        Post post = mock(Post.class);
        String id = "id";
        when(service.updatePost(id, post)).thenReturn(Observable.just(post));
        TestSubscriber<Post> ts = new TestSubscriber<>();

        network.updatePost(id, post).subscribe(ts);

        assertThat(ts.getOnNextEvents(), hasItems(post));
    }

    @Test
    public void deletePostReturnsFromService() {
        String id = "id1231";
        when(service.deletePost(id)).thenReturn(Observable.<Void>empty());
        TestSubscriber<Void> ts = new TestSubscriber<>();

        network.deletePost(id).subscribe(ts);

        ts.assertNoErrors();
        ts.assertCompleted();
    }

    @Test
    public void createLikeReturnsLikeFromService() {
        String id = "postId";
        Like like = mock(Like.class);
        when(service.createLike(id)).thenReturn(Observable.just(like));
        TestSubscriber<Like> ts = new TestSubscriber<>();

        network.createLike(id).subscribe(ts);

        assertThat(ts.getOnNextEvents(), hasItems(like));
    }

    @Test
    public void deleteLikeDeletesOnService() {
        String id = "postId";
        when(service.deleteLike(id)).thenReturn(Observable.<Void>empty());
        TestSubscriber<Void> ts = new TestSubscriber<>();

        network.deleteLike(id).subscribe(ts);

        ts.assertNoErrors();
        ts.assertCompleted();
    }

    @Test
    public void createCommentPostsToService() {
        String id = "postId";
        Comment comment = mock(Comment.class);
        when(service.createComment(id, comment)).thenReturn(Observable.just(comment));
        TestSubscriber<Comment> ts = new TestSubscriber<>();

        network.createComment(id, comment).subscribe(ts);

        assertThat(ts.getOnNextEvents(), hasItems(comment));
    }

    @Test
    public void deleteCommentDeletesOnService() {
        String postId = "postId";
        String commentId = "commentiD";
        when(service.deleteComment(postId, commentId)).thenReturn(Observable.<Void>empty());
        TestSubscriber<Void> ts = new TestSubscriber<>();

        network.deleteComment(postId, commentId).subscribe(ts);

        ts.assertNoErrors();
        ts.assertCompleted();
    }

    @Test
    public void updateCommentUpdatesOnService() {
        String postId = "postId";
        String commentId = "commentID";
        Comment comment = mock(Comment.class);
        when(service.updateComment(postId, commentId, comment)).thenReturn(Observable.just(comment));
        TestSubscriber<Comment> ts = new TestSubscriber<>();

        network.updateComment(postId, commentId, comment).subscribe(ts);
    }

    @Test
    public void retrievePostFeedRetrievesFromService() throws Exception {
        List<Post> posts = mock(List.class);
        PostListFeed feed = mock(PostListFeed.class);
        PostFeedToList converter = mock(PostFeedToList.class);
        whenNew(PostFeedToList.class).withNoArguments().thenReturn(converter);
        when(converter.call(feed)).thenReturn(posts);
        when(service.retrievePostFeed()).thenReturn(Observable.just(feed));
        TestSubscriber<List<Post>> ts = new TestSubscriber<>();

        network.retrievePostFeed().subscribe(ts);

        assertThat(ts.getOnNextEvents(), hasItems(posts));
    }

    @Test
    public void retrieveEventFeedRetrievesFromService() throws Exception {
        List<Event> events = mock(List.class);
        EventListFeed feed = mock(EventListFeed.class);
        EventFeedToList converter = mock(EventFeedToList.class);
        whenNew(EventFeedToList.class).withNoArguments().thenReturn(converter);
        when(converter.call(feed)).thenReturn(events);
        when(service.retrieveEventFeed()).thenReturn(Observable.just(feed));
        TestSubscriber<List<Event>> ts = new TestSubscriber<>();

        network.retrieveEventFeed().subscribe(ts);

        assertThat(ts.getOnNextEvents(), hasItems(events));
    }

    @Test
    public void sendAnalyticsCallsService() {
        when(service.sendAnalytics()).thenReturn(Observable.<Void>empty());
        TestSubscriber<Void> ts = new TestSubscriber<>();

        network.sendAnalytics().subscribe(ts);

        ts.assertNoErrors();
        ts.assertCompleted();
    }
}