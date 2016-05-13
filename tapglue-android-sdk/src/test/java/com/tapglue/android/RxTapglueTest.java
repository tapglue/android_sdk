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
import com.tapglue.android.http.payloads.SocialConnections;
import com.tapglue.android.internal.UserStore;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.List;

import rx.Observable;
import rx.functions.Action0;
import rx.functions.Func1;
import rx.observers.TestSubscriber;
import rx.schedulers.Schedulers;

import static org.hamcrest.core.IsCollectionContaining.hasItems;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.whenNew;

@RunWith(PowerMockRunner.class)
@PrepareForTest({RxTapglue.class, TapglueSchedulers.class})
public class RxTapglueTest {
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
    Action0 clearAction;
    @Mock
    AtomicBoolean firstInstance;


    @Mock
    User user;
    @Mock
    List<User> users;

    //SUT
    RxTapglue tapglue;

    @Before
    public void setUp() throws Exception{
        whenNew(Network.class).withAnyArguments().thenReturn(network);
        whenNew(UserStore.class).withArguments(context).thenReturn(currentUser);
        Whitebox.setInternalState(RxTapglue.class, firstInstance);

        when(firstInstance.compareAndSet(true, false)).thenReturn(true);
        when(context.getSharedPreferences(anyString(), anyInt())).thenReturn(prefs);
        when(currentUser.clear()).thenReturn(clearAction);
        when(network.sendAnalytics()).thenReturn(Observable.<Void>empty());
        when(network.loginWithEmail(EMAIL, PASSWORD)).thenReturn(Observable.just(user));
        when(network.loginWithUsername(USERNAME, PASSWORD)).thenReturn(Observable.just(user));
        when(currentUser.store()).thenReturn(new Func1<User, User>() {
            @Override
            public User call(User user) {
                return user;
            }
        });
        when(configuration.getToken()).thenReturn(TOKEN);
        when(configuration.getBaseUrl()).thenReturn(BASE_URL);
        tapglue = new RxTapglue(configuration, context);
    }

    @Test
    public void loginWithUserNameCallsNetwork() {
        TestSubscriber<User> ts = new TestSubscriber<>();

        tapglue.loginWithUsername(USERNAME, PASSWORD).subscribe(ts);

        assertThat(ts.getOnNextEvents(), hasItems(user));
    }

    @Test
    public void loginWithEmailCallsNetwork() {
        TestSubscriber<User> ts = new TestSubscriber<>();

        tapglue.loginWithEmail(EMAIL, PASSWORD).subscribe(ts);

        assertThat(ts.getOnNextEvents(), hasItems(user));
    }

    @Test
    public void loginUserWithUsernameStoresCurrentUser() {
        TestSubscriber<User> ts = new TestSubscriber<>();

        tapglue.loginWithUsername(USERNAME, PASSWORD).subscribe(ts);

        verify(currentUser).store();
    }

    @Test
    public void loginUserWithEmailStoresCurrentUser() {
        TestSubscriber<User> ts = new TestSubscriber<>();

        tapglue.loginWithEmail(EMAIL, PASSWORD).subscribe(ts);

        verify(currentUser).store();
    }

    @Test
    public void logoutCallsNetwork() {
        when(network.logout()).thenReturn(Observable.<Void>empty());
        TestSubscriber<Void> ts = new TestSubscriber<>();

        tapglue.logout().subscribe(ts);

        ts.assertCompleted();
        ts.assertNoErrors();
    }

    @Test
    public void logoutClearsUserStoreOnSuccess() {
        when(network.logout()).thenReturn(Observable.<Void>empty());
        TestSubscriber<Void> ts = new TestSubscriber<>();

        tapglue.logout().subscribe(ts);

        verify(clearAction).call();
    }

    @Test
    public void logoutDoesntClearsUserStoreOnError() {
        when(currentUser.clear()).thenReturn(clearAction);
        when(network.logout()).thenReturn(Observable.<Void>error(e));
        TestSubscriber<Void> ts = new TestSubscriber<>();

        tapglue.logout().subscribe(ts);

        verify(clearAction, never()).call();
    }


    @Test
    public void getCurrentUserGetsFromStore() {
        when(currentUser.get()).thenReturn(Observable.just(user));
        TestSubscriber<User> ts = new TestSubscriber<>();

        tapglue.getCurrentUser().subscribe(ts);

        assertThat(ts.getOnNextEvents(), hasItems(user));
    }

    @Test
    public void createUserCallsNetwork() {
        when(network.createUser(user)).thenReturn(Observable.just(user));
        TestSubscriber<User> ts = new TestSubscriber<>();

        tapglue.createUser(user).subscribe(ts);

        assertThat(ts.getOnNextEvents(), hasItems(user));
    }

    @Test
    public void deleteUserCallsNetwork() {
        when(network.deleteCurrentUser()).thenReturn(Observable.<Void>empty());
        TestSubscriber<Void> ts = new TestSubscriber<>();

        tapglue.deleteCurrentUser().subscribe(ts);

        ts.assertCompleted();
        ts.assertNoErrors();
    }

    @Test
    public void deleteCurrentUserClearsUserStoreOnSuccess() {
        when(network.deleteCurrentUser()).thenReturn(Observable.<Void>empty());
        TestSubscriber<Void> ts = new TestSubscriber<>();

        tapglue.deleteCurrentUser().subscribe(ts);

        verify(clearAction).call();
    }

    @Test
    public void deleteCurrentUserDoesntClearsUserStoreOnError() {
        when(currentUser.clear()).thenReturn(clearAction);
        when(network.deleteCurrentUser()).thenReturn(Observable.<Void>error(e));
        TestSubscriber<Void> ts = new TestSubscriber<>();

        tapglue.deleteCurrentUser().subscribe(ts);

        verify(clearAction, never()).call();
    }

    @Test
    public void updateCurrentUserCallsNetwork() {
        when(network.updateCurrentUser(user)).thenReturn(Observable.just(user));
        TestSubscriber<User> ts = new TestSubscriber<>();

        tapglue.updateCurrentUser(user).subscribe(ts);

        assertThat(ts.getOnNextEvents(), hasItems(user));
    }

    @Test
    public void updateCurrentUserUpdatesCurrentUser() {
        when(network.updateCurrentUser(user)).thenReturn(Observable.just(user));
        TestSubscriber<User> ts = new TestSubscriber<>();

        tapglue.updateCurrentUser(user).subscribe(ts);

        verify(currentUser).store();
    }

    @Test
    public void refreshCurrentUserCallsNetwork() {
        when(network.refreshCurrentUser()).thenReturn(Observable.just(user));
        TestSubscriber<User> ts = new TestSubscriber<>();

        tapglue.refreshCurrentUser().subscribe(ts);

        assertThat(ts.getOnNextEvents(), hasItems(user));
    }

    @Test
    public void refreshCurrentUserUpdatesCurrentUser() {
        when(network.refreshCurrentUser()).thenReturn(Observable.just(user));
        TestSubscriber<User> ts = new TestSubscriber<>();

        tapglue.refreshCurrentUser().subscribe(ts);

        verify(currentUser).store();
    }

    @Test
    public void retrieveUserCallsNetwork() {
        String id = "someId";
        when(network.retrieveUser(id)).thenReturn(Observable.just(user));
        TestSubscriber<User> ts = new TestSubscriber<>();

        tapglue.retrieveUser(id).subscribe(ts);

        assertThat(ts.getOnNextEvents(), hasItems(user));
    }

    @Test
    public void retrieveFollowingsCallsNetwork() {
        when(network.retrieveFollowings()).thenReturn(Observable.just(users));
        TestSubscriber<List<User>> ts = new TestSubscriber<>();

        tapglue.retrieveFollowings().subscribe(ts);

        assertThat(ts.getOnNextEvents(), hasItems(users));
    }

    @Test
    public void retrieveFollowersCallsNetwork() {
        when(network.retrieveFollowers()).thenReturn(Observable.just(users));
        TestSubscriber<List<User>> ts = new TestSubscriber<>();

        tapglue.retrieveFollowers().subscribe(ts);

        assertThat(ts.getOnNextEvents(), hasItems(users));
    }

    @Test
    public void retrieveUserFollowingsCallsNetwork() {
        String id = "userId";
        when(network.retrieveUserFollowings(id)).thenReturn(Observable.just(users));
        TestSubscriber<List<User>> ts = new TestSubscriber<>();

        tapglue.retrieveUserFollowings(id).subscribe(ts);

        assertThat(ts.getOnNextEvents(), hasItems(users));
    }

    @Test
    public void retrieveUserFollowersCallsNetwork() {
        String id = "userId";
        when(network.retrieveUserFollowers(id)).thenReturn(Observable.just(users));
        TestSubscriber<List<User>> ts = new TestSubscriber<>();

        tapglue.retrieveUserFollowers(id).subscribe(ts);

        assertThat(ts.getOnNextEvents(), hasItems(users));
    }

    @Test
    public void retrieveFriendsCallsNetwork() {
        when(network.retrieveFriends()).thenReturn(Observable.just(users));
        TestSubscriber<List<User>> ts = new TestSubscriber<>();

        tapglue.retrieveFriends().subscribe(ts);

        assertThat(ts.getOnNextEvents(), hasItems(users));
    }

    @Test
    public void retrieveUserFriendsCallsNetwork() {
        String id = "userId";
        when(network.retrieveUserFriends(id)).thenReturn(Observable.just(users));
        TestSubscriber<List<User>> ts = new TestSubscriber<>();

        tapglue.retrieveUserFriends(id).subscribe(ts);

        assertThat(ts.getOnNextEvents(), hasItems(users));
    }

    @Test
    public void retrievePendingConnectionsCallsNetwork() {
        ConnectionList connectionList = mock(ConnectionList.class);
        when(network.retrievePendingConnections()).thenReturn(Observable.just(connectionList));
        TestSubscriber<ConnectionList> ts = new TestSubscriber<>();

        tapglue.retrievePendingConnections().subscribe(ts);

        assertThat(ts.getOnNextEvents(), hasItems(connectionList));
    }

    @Test
    public void retrieveRejectedConnectionsCallsNetwork() {
        ConnectionList connectionList = mock(ConnectionList.class);
        when(network.retrieveRejectedConnections()).thenReturn(Observable.just(connectionList));
        TestSubscriber<ConnectionList> ts = new TestSubscriber<>();

        tapglue.retrieveRejectedConnections().subscribe(ts);

        assertThat(ts.getOnNextEvents(), hasItems(connectionList));
    }

    @Test
    public void createConnectionCallsNetwork() {
        Connection connection = mock(Connection.class);
        when(network.createConnection(connection)).thenReturn(Observable.just(connection));
        TestSubscriber<Connection> ts = new TestSubscriber<>();

        tapglue.createConnection(connection).subscribe(ts);

        assertThat(ts.getOnNextEvents(), hasItems(connection));
    }

    @Test
    public void createSocialConnectionsCallsNetwork() {
        SocialConnections connections = mock(SocialConnections.class);
        when(network.createSocialConnections(connections)).thenReturn(Observable.just(users));
        TestSubscriber<List<User>> ts = new TestSubscriber();

        tapglue.createSocialConnections(connections).subscribe(ts);

        assertThat(ts.getOnNextEvents(), hasItems(users));
    }

    @Test
    public void deleteConnectionCallsNetwork() {
        String id = "userId";
        Type type = Type.FOLLOW;
        when(network.deleteConnection(id, type)).thenReturn(Observable.<Void>empty());
        TestSubscriber<Void> ts = new TestSubscriber<>();

        tapglue.deleteConnection(id, type).subscribe(ts);

        ts.assertNoErrors();
        ts.assertCompleted();
    }

    @Test
    public void searchUsersCallsNetwork() {
        when(network.searchUsers("search term")).thenReturn(Observable.just(users));
        TestSubscriber<List<User>> ts = new TestSubscriber<>();

        tapglue.searchUsers("search term").subscribe(ts);

        assertThat(ts.getOnNextEvents(), hasItems(users));
    }

    @Test
    public void searchUsersByEmailCallsNetwork() {
        List<String> emails = mock(List.class);
        when(network.searchUsersByEmail(emails)).thenReturn(Observable.just(users));
        TestSubscriber<List<User>> ts = new TestSubscriber<>();

        tapglue.searchUsersByEmail(emails).subscribe(ts);

        assertThat(ts.getOnNextEvents(), hasItems(users));
    }

    @Test
    public void searchUsersBySocialIdsCallsNetwork() {
        String platform = "platform";
        List<String> socialIds = mock(List.class);
        when(network.searchUsersBySocialIds(platform, socialIds)).thenReturn(Observable.just(users));
        TestSubscriber<List<User>> ts = new TestSubscriber<>();

        tapglue.searchUsersBySocialIds(platform, socialIds).subscribe(ts);

        assertThat(ts.getOnNextEvents(), hasItems(users));
    }

    @Test
    public void createPostCallsNetwork() {
        Post post = mock(Post.class);
        when(network.createPost(post)).thenReturn(Observable.just(post));
        TestSubscriber<Post> ts = new TestSubscriber<>();

        tapglue.createPost(post).subscribe(ts);

        assertThat(ts.getOnNextEvents(), hasItems(post));
    }

    @Test
    public void retrievePostCallsNetwork() {
        Post post = mock(Post.class);
        String id = "id";
        when(network.retrievePost(id)).thenReturn(Observable.just(post));
        TestSubscriber<Post> ts = new TestSubscriber<>();

        tapglue.retrievePost(id).subscribe(ts);

        assertThat(ts.getOnNextEvents(), hasItems(post));
    }

    @Test
    public void updatePostCallsNetwork() {
        Post post = mock(Post.class);
        String id = "id";
        when(network.updatePost(id, post)).thenReturn(Observable.just(post));
        TestSubscriber<Post> ts = new TestSubscriber<>();

        tapglue.updatePost(id, post).subscribe(ts);

        assertThat(ts.getOnNextEvents(), hasItems(post));
    }

    @Test
    public void deletePostCallsNetwork() {
        String id = "id24";
        when(network.deletePost(id)).thenReturn(Observable.<Void>empty());
        TestSubscriber<Void> ts = new TestSubscriber<>();

        tapglue.deletePost(id).subscribe(ts);

        ts.assertNoErrors();
        ts.assertCompleted();
    }

    @Test
    public void retrievePostsCallsNetwork() {
        List<Post> posts = mock(List.class);
        when(network.retrievePosts()).thenReturn(Observable.just(posts));
        TestSubscriber<List<Post>> ts = new TestSubscriber<>();

        tapglue.retrievePosts().subscribe(ts);

        assertThat(ts.getOnNextEvents(), hasItems(posts));
    }

    @Test
    public void retrievePostsByUserCallsNetwork() {
        String id = "userId";
        List<Post> posts = mock(List.class);
        when(network.retrievePostsByUser(id)).thenReturn(Observable.just(posts));
        TestSubscriber<List<Post>> ts = new TestSubscriber<>();

        tapglue.retrievePostsByUser(id).subscribe(ts);

        assertThat(ts.getOnNextEvents(), hasItems(posts));
    }

    @Test
    public void createLikeCallsNetwork() {
        String id = "postId";
        Like like = mock(Like.class);
        when(network.createLike(id)).thenReturn(Observable.just(like));
        TestSubscriber<Like> ts = new TestSubscriber<>();

        tapglue.createLike(id).subscribe(ts);

        assertThat(ts.getOnNextEvents(), hasItems(like));
    }

    @Test
    public void deleteLikeCallsNetwork() {
        String id = "postId";
        when(network.deleteLike(id)).thenReturn(Observable.<Void>empty());
        TestSubscriber<Void> ts = new TestSubscriber<>();

        tapglue.deleteLike(id).subscribe(ts);

        ts.assertNoErrors();
        ts.assertCompleted();
    }

    @Test
    public void retrieveLikesForPostCallsNetwork() {
        String id = "postId";
        List<Like> likes = mock(List.class);
        when(network.retrieveLikesForPost(id)).thenReturn(Observable.just(likes));
        TestSubscriber<List<Like>> ts = new TestSubscriber<>();

        tapglue.retrieveLikesForPost(id).subscribe(ts);

        assertThat(ts.getOnNextEvents(), hasItems(likes));
    }

    @Test
    public void createCommentCallsNetwork() {
        String id = "postId";
        Comment comment = mock(Comment.class);
        when(network.createComment(id, comment)).thenReturn(Observable.just(comment));
        TestSubscriber<Comment> ts = new TestSubscriber<>();

        tapglue.createComment(id, comment).subscribe(ts);

        assertThat(ts.getOnNextEvents(), hasItems(comment));
    }

    @Test
    public void deleteCommentCallsNetwork() {
        String postId = "postId";
        String commentId = "commentId";
        when(network.deleteComment(postId, commentId)).thenReturn(Observable.<Void>empty());
        TestSubscriber<Void> ts = new TestSubscriber<>();

        tapglue.deleteComment(postId, commentId).subscribe(ts);

        ts.assertNoErrors();
        ts.assertCompleted();
    }

    @Test
    public void updateCommentCallsNetwork() {
        String postId = "postId";
        String commentId = "commentID";
        Comment comment = mock(Comment.class);
        when(network.updateComment(postId, commentId, comment)).thenReturn(Observable.just(comment));
        TestSubscriber<Comment> ts = new TestSubscriber<>();

        tapglue.updateComment(postId, commentId, comment).subscribe(ts);

        assertThat(ts.getOnNextEvents(), hasItems(comment));
    }

    @Test
    public void retrieveCommentsCallsNetwork() {
        String postId = "postId";
        List<Comment> comments = mock(List.class);
        when(network.retrieveCommentsForPost(postId)).thenReturn(Observable.just(comments));
        TestSubscriber<List<Comment>> ts = new TestSubscriber<>();

        tapglue.retrieveCommentsForPost(postId).subscribe(ts);

        assertThat(ts.getOnNextEvents(), hasItems(comments));
    }

    @Test
    public void retrievePostFeedCallsNetwork() {
        List<Post> posts = mock(List.class);
        when(network.retrievePostFeed()).thenReturn(Observable.just(posts));
        TestSubscriber<List<Post>> ts = new TestSubscriber<>();

        tapglue.retrievePostFeed().subscribe(ts);

        assertThat(ts.getOnNextEvents(), hasItems(posts));
    }

    @Test
    public void retrieveEventFeedCallsNetwork() {
        List<Event> events = mock(List.class);
        when(network.retrieveEventFeed()).thenReturn(Observable.just(events));
        TestSubscriber<List<Event>> ts = new TestSubscriber<>();

        tapglue.retrieveEventFeed().subscribe(ts);

        assertThat(ts.getOnNextEvents(), hasItems(events));
    }

    @Test
    public void retrieveNewsFeedCallsNetwork() {
        NewsFeed feed = mock(NewsFeed.class);
        when(network.retrieveNewsFeed()).thenReturn(Observable.just(feed));
        TestSubscriber<NewsFeed> ts = new TestSubscriber<>();

        tapglue.retrieveNewsFeed().subscribe(ts);

        assertThat(ts.getOnNextEvents(), hasItems(feed));
    }


    @Test
    public void sendsAnalyticsOnInstantiation() {
        verify(network).sendAnalytics();
    }

    @Test
    public void sendAnalyticsFailureWontCrash() {
        PowerMockito.mockStatic(TapglueSchedulers.class);
        when(TapglueSchedulers.analytics()).thenReturn(Schedulers.immediate());
        when(network.sendAnalytics()).thenReturn(Observable.<Void>error(e));
        tapglue = new RxTapglue(configuration, context);
    }
}
