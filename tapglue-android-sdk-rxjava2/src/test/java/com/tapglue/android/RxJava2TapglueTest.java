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
import android.content.SharedPreferences;
import android.content.res.Resources;

import com.tapglue.android.entities.Comment;
import com.tapglue.android.entities.Connection;
import com.tapglue.android.entities.Connection.Type;
import com.tapglue.android.entities.Event;
import com.tapglue.android.entities.Like;
import com.tapglue.android.entities.Post;
import com.tapglue.android.entities.User;
import com.tapglue.android.http.Network;
import com.tapglue.android.http.payloads.SocialConnections;
import com.tapglue.android.internal.UserStore;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import io.reactivex.Observable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Function;
import io.reactivex.observers.TestObserver;

import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.whenNew;

@RunWith(PowerMockRunner.class)
@PrepareForTest({RxTapglue.class, TapglueSchedulers.class})
public class RxJava2TapglueTest {
    private static final String TOKEN = "sampleToken";
    private static final String BASE_URL = "https://api.tapglue.com";
    private static final String USERNAME = "username";
    private static final String EMAIL = "user@domain.com";
    private static final String PASSWORD = "password";

    @Mock
    Configuration configuration;
    @Mock
    Context context;
    @Mock
    SharedPreferences prefs;
    @Mock
    Network network;
    @Mock
    UserStore currentUser;
    @Mock
    Exception e;
    @Mock
    Action clearAction;
    @Mock
    AtomicBoolean firstInstance;
    @Mock
    Resources resources;


    @Mock
    User user;
    @Mock
    List<User> users;

    @Mock
    RxPage<List<User>> userPage;

    //SUT
    RxTapglue tapglue;

    @Before
    public void setUp() throws Exception{
        whenNew(Network.class).withAnyArguments().thenReturn(network);
        whenNew(UserStore.class).withArguments(context).thenReturn(currentUser);
        Whitebox.setInternalState(RxTapglue.class, firstInstance);

        when(firstInstance.compareAndSet(true, false)).thenReturn(true);
        when(context.getSharedPreferences(anyString(), anyInt())).thenReturn(prefs);
        when(context.getResources()).thenReturn(resources);
        when(resources.getConfiguration()).thenReturn(mock(android.content.res.Configuration.class));
        when(currentUser.clear()).thenReturn(clearAction);
        when(network.sendAnalytics()).thenReturn(Observable.<Void>empty());
        when(network.loginWithEmail(EMAIL, PASSWORD)).thenReturn(Observable.just(user));
        when(network.loginWithUsername(USERNAME, PASSWORD)).thenReturn(Observable.just(user));
        when(currentUser.store()).thenReturn(new Function<User, User>() {
            @Override
            public User apply(User user) {
                return user;
            }
        });
        when(configuration.getToken()).thenReturn(TOKEN);
        when(configuration.getBaseUrl()).thenReturn(BASE_URL);
        tapglue = new RxTapglue(configuration, context);
    }

    @Test
    public void loginWithUserNameCallsNetwork() {
        TestObserver<User> to = tapglue.loginWithUsername(USERNAME, PASSWORD).test();

        to.assertValue(user);
    }

    @Test
    public void loginWithEmailCallsNetwork() {
        TestObserver<User> to = tapglue.loginWithEmail(EMAIL, PASSWORD).test();

        to.assertValue(user);
    }

    @Test
    public void loginUserWithUsernameStoresCurrentUser() {
        tapglue.loginWithUsername(USERNAME, PASSWORD).test();

        verify(currentUser).store();
    }

    @Test
    public void loginUserWithEmailStoresCurrentUser() {
        tapglue.loginWithEmail(EMAIL, PASSWORD).test();

        verify(currentUser).store();
    }

    @Test
    public void logoutCallsNetwork() {
        when(network.logout()).thenReturn(Observable.<Void>empty());
        TestObserver<Void> to = tapglue.logout().test();

        to.assertComplete();
        to.assertNoErrors();
    }

    @Test
    public void logoutClearsUserStoreOnSuccess() throws Exception {
        when(network.logout()).thenReturn(Observable.<Void>empty());
        tapglue.logout().test();

        verify(clearAction).run();
    }

    @Test
    public void logoutDoesntClearsUserStoreOnError() throws Exception {
        when(currentUser.clear()).thenReturn(clearAction);
        when(network.logout()).thenReturn(Observable.<Void>error(e));

        tapglue.logout().test();

        verify(clearAction, never()).run();
    }


    @Test
    public void getCurrentUserGetsFromStore() {
        when(currentUser.get()).thenReturn(Observable.just(user));
        TestObserver<User> to = tapglue.getCurrentUser().test();

        to.assertValue(user);
    }

    @Test
    public void createUserCallsNetwork() {
        when(network.createUser(user)).thenReturn(Observable.just(user));
        TestObserver<User> to = tapglue.createUser(user).test();

        to.assertValue(user);
    }

    @Test
    public void deleteUserCallsNetwork() throws Exception {
        when(network.deleteCurrentUser()).thenReturn(Observable.<Void>empty());
        TestObserver<Void> to = tapglue.deleteCurrentUser().test();

        to.assertComplete();
        to.assertNoErrors();
    }

    @Test
    public void deleteCurrentUserClearsUserStoreOnSuccess() throws Exception {
        when(network.deleteCurrentUser()).thenReturn(Observable.<Void>empty());
        tapglue.deleteCurrentUser().test();

        verify(clearAction).run();
    }

    @Test
    public void deleteCurrentUserDoesntClearsUserStoreOnError() throws Exception {
        when(currentUser.clear()).thenReturn(clearAction);
        when(network.deleteCurrentUser()).thenReturn(Observable.<Void>error(e));
        tapglue.deleteCurrentUser().test();

        verify(clearAction, never()).run();
    }

    @Test
    public void updateCurrentUserCallsNetwork() {
        when(network.updateCurrentUser(user)).thenReturn(Observable.just(user));
        TestObserver<User> to = tapglue.updateCurrentUser(user).test();

        to.assertValue(user);
    }

    @Test
    public void updateCurrentUserUpdatesCurrentUser() {
        when(network.updateCurrentUser(user)).thenReturn(Observable.just(user));
        tapglue.updateCurrentUser(user).test();

        verify(currentUser).store();
    }

    @Test
    public void refreshCurrentUserCallsNetwork() {
        when(network.refreshCurrentUser()).thenReturn(Observable.just(user));
        TestObserver<User> to = tapglue.refreshCurrentUser().test();

        to.assertValue(user);
    }

    @Test
    public void refreshCurrentUserUpdatesCurrentUser() {
        when(network.refreshCurrentUser()).thenReturn(Observable.just(user));
        tapglue.refreshCurrentUser().test();

        verify(currentUser).store();
    }

    @Test
    public void retrieveUserCallsNetwork() {
        String id = "someId";
        when(network.retrieveUser(id)).thenReturn(Observable.just(user));
        TestObserver<User> to = tapglue.retrieveUser(id).test();

        to.assertValue(user);
    }

    @Test
    public void retrieveFollowingsCallsNetwork() {
        when(network.retrieveFollowings()).thenReturn(Observable.just(userPage));
        TestObserver<RxPage<List<User>>> to = tapglue.retrieveFollowings().test();

        to.assertValue(userPage);
    }

    @Test
    public void retrieveFollowersCallsNetwork() {
        when(network.retrieveFollowers()).thenReturn(Observable.just(userPage));
        TestObserver<RxPage<List<User>>> to = tapglue.retrieveFollowers().test();

        to.assertValue(userPage);
    }

    @Test
    public void retrieveUserFollowingsCallsNetwork() {
        String id = "userId";
        when(network.retrieveUserFollowings(id)).thenReturn(Observable.just(userPage));
        TestObserver<RxPage<List<User>>> to = tapglue.retrieveUserFollowings(id).test();

        to.assertValue(userPage);
    }

    @Test
    public void retrieveUserFollowersCallsNetwork() {
        String id = "userId";
        when(network.retrieveUserFollowers(id)).thenReturn(Observable.just(userPage));
        TestObserver<RxPage<List<User>>> to = tapglue.retrieveUserFollowers(id).test();

        to.assertValue(userPage);
    }

    @Test
    public void createConnectionCallsNetwork() {
        Connection connection = mock(Connection.class);
        when(network.createConnection(connection)).thenReturn(Observable.just(connection));
        TestObserver<Connection> to = tapglue.createConnection(connection).test();

        to.assertValue(connection);
    }

    @Test
    public void createSocialConnectionsCallsNetwork() {
        SocialConnections connections = mock(SocialConnections.class);
        when(network.createSocialConnections(connections)).thenReturn(Observable.just(users));
        TestObserver<List<User>> to = tapglue.createSocialConnections(connections).test();

        to.assertValue(users);
    }

    @Test
    public void deleteConnectionCallsNetwork() {
        String id = "userId";
        Type type = Type.FOLLOW;
        when(network.deleteConnection(id, type)).thenReturn(Observable.<Void>empty());
        TestObserver<Void> to = tapglue.deleteConnection(id, type).test();

        to.assertNoErrors();
        to.assertComplete();
    }

    @Test
    public void createPostCallsNetwork() {
        Post post = mock(Post.class);
        when(network.createPost(post)).thenReturn(Observable.just(post));
        TestObserver<Post> to = tapglue.createPost(post).test();

        to.assertValue(post);
    }

    @Test
    public void retrievePostCallsNetwork() {
        Post post = mock(Post.class);
        String id = "id";
        when(network.retrievePost(id)).thenReturn(Observable.just(post));
        TestObserver<Post> to = tapglue.retrievePost(id).test();

        to.assertValue(post);
    }

    @Test
    public void updatePostCallsNetwork() {
        Post post = mock(Post.class);
        String id = "id";
        when(network.updatePost(id, post)).thenReturn(Observable.just(post));
        TestObserver<Post> to = tapglue.updatePost(id, post).test();

        to.assertValue(post);
    }

    @Test
    public void deletePostCallsNetwork() {
        String id = "id24";
        when(network.deletePost(id)).thenReturn(Observable.<Void>empty());
        TestObserver<Void> to = tapglue.deletePost(id).test();

        to.assertNoErrors();
        to.assertComplete();
    }

    @Test
    public void createLikeCallsNetwork() {
        String id = "postId";
        Like like = mock(Like.class);
        when(network.createLike(id)).thenReturn(Observable.just(like));
        TestObserver<Like> to = tapglue.createLike(id).test();

        to.assertValue(like);
    }

    @Test
    public void deleteLikeCallsNetwork() {
        String id = "postId";
        when(network.deleteLike(id)).thenReturn(Observable.<Void>empty());
        TestObserver<Void> to = tapglue.deleteLike(id).test();

        to.assertNoErrors();
        to.assertComplete();
    }

    @Test
    public void createCommentCallsNetwork() {
        String id = "postId";
        Comment comment = mock(Comment.class);
        when(network.createComment(id, comment)).thenReturn(Observable.just(comment));
        TestObserver<Comment> to = tapglue.createComment(id, comment).test();

        to.assertValue(comment);
    }

    @Test
    public void deleteCommentCallsNetwork() {
        String postId = "postId";
        String commentId = "commentId";
        when(network.deleteComment(postId, commentId)).thenReturn(Observable.<Void>empty());
        TestObserver<Void> to = tapglue.deleteComment(postId, commentId).test();

        to.assertNoErrors();
        to.assertComplete();
    }

    @Test
    public void updateCommentCallsNetwork() {
        String postId = "postId";
        String commentId = "commentID";
        Comment comment = mock(Comment.class);
        when(network.updateComment(postId, commentId, comment)).thenReturn(Observable.just(comment));
        TestObserver<Comment> to = tapglue.updateComment(postId, commentId, comment).test();

        to.assertValue(comment);
    }

    @Test
    public void retrieveEventFeedCallsNetwork() {
        List<Event> events = mock(List.class);
        when(network.retrieveEventFeed()).thenReturn(Observable.just(events));
        TestObserver<List<Event>> to = tapglue.retrieveEventFeed().test();

        to.assertValue(events);
    }
}
