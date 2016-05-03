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

package com.tapglue.sdk;

import android.content.Context;

import java.util.List;

import com.tapglue.sdk.entities.Connection;
import com.tapglue.sdk.entities.ConnectionList;
import com.tapglue.sdk.entities.User;
import com.tapglue.sdk.http.payloads.SocialConnections;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import rx.Observable;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.when;
import static org.powermock.api.mockito.PowerMockito.whenNew;

@RunWith(PowerMockRunner.class)
@PrepareForTest(Tapglue.class)
public class TapglueTest {

    private static final String USERNAME = "username";
    private static final String EMAIL = "user@domain.com";
    private static final String PASSWORD = "password";

    @Mock
    User user;
    @Mock
    Configuration configuration;
    @Mock
    Context context;
    @Mock
    RxTapglue rxTapglue;
    @Mock
    Observable<Void> voidObservable;
    @Mock
    Connection connection;
    @Mock
    ConnectionList connectionList;
    @Mock
    SocialConnections socialConnections;

    @Mock
    RxWrapper<Void> voidWrapper;
    @Mock
    List<User> userList;

    //SUT
    Tapglue tapglue;

    @Before
    public void setUp() throws Exception {
        whenNew(RxTapglue.class).withArguments(configuration, context).thenReturn(rxTapglue);

        tapglue = new Tapglue(configuration, context);
    }

    @Test
    public void usernameLogin() throws Throwable {
        when(rxTapglue.loginWithUsername(USERNAME, PASSWORD)).thenReturn(Observable.just(user));

        assertThat(tapglue.loginWithUsername(USERNAME, PASSWORD), equalTo(user));
    }

    @Test
    public void emailLogin() throws Throwable {
        when(rxTapglue.loginWithEmail(EMAIL, PASSWORD)).thenReturn(Observable.just(user));

        assertThat(tapglue.loginWithEmail(EMAIL, PASSWORD), equalTo(user));
    }

    @Test
    public void logout() throws Throwable {
        whenNew(RxWrapper.class).withNoArguments().thenReturn(voidWrapper);
        when(rxTapglue.logout()).thenReturn(voidObservable);

        tapglue.logout();

        verify(voidWrapper).unwrap(voidObservable);
    }

    @Test
    public void currentUser() throws Throwable {
        when(rxTapglue.getCurrentUser()).thenReturn(Observable.just(user));

        assertThat(tapglue.getCurrentUser(), equalTo(user));
    }

    @Test
    public void createUser() throws Exception {
        when(rxTapglue.createUser(user)).thenReturn(Observable.just(user));

        assertThat(tapglue.createUser(user), equalTo(user));
    }

    @Test
    public void deleteCurrentUser() throws Exception {
        whenNew(RxWrapper.class).withNoArguments().thenReturn(voidWrapper);
        when(rxTapglue.deleteCurrentUser()).thenReturn(voidObservable);

        tapglue.deleteCurrentUser();

        verify(voidWrapper).unwrap(voidObservable);
    }

    @Test
    public void updateCurrentUser() throws Exception {
        when(rxTapglue.updateCurrentUser(user)).thenReturn(Observable.just(user));

        assertThat(tapglue.updateCurrentUser(user), equalTo(user));
    }

    @Test
    public void refreshCurrentUser() throws Exception {
        when(rxTapglue.refreshCurrentUser()).thenReturn(Observable.just(user));

        assertThat(tapglue.refreshCurrentUser(), equalTo(user));
    }

    @Test
    public void retrieveUser() throws Exception {
        String id = "101";
        when(rxTapglue.retrieveUser(id)).thenReturn(Observable.just(user));

        assertThat(tapglue.retrieveUser(id), equalTo(user));
    }

    @Test
    public void retrieveFollowings() throws Exception {
        when(rxTapglue.retrieveFollowings()).thenReturn(Observable.just(userList));

        assertThat(tapglue.retrieveFollowings(), equalTo(userList));
    }

    @Test
    public void retrieveFollowers() throws Exception {
        when(rxTapglue.retrieveFollowers()).thenReturn(Observable.just(userList));

        assertThat(tapglue.retrieveFollowers(), equalTo(userList));
    }

    @Test
    public void retrieveFriends() throws Exception {
        when(rxTapglue.retrieveFriends()).thenReturn(Observable.just(userList));

        assertThat(tapglue.retrieveFriends(), equalTo(userList));
    }

    @Test
    public void retrievePendingConnections() throws Exception {
        when(rxTapglue.retrievePendingConnections()).thenReturn(Observable.just(connectionList));

        assertThat(tapglue.retrievePendingConnections(), equalTo(connectionList));
    }

    @Test
    public void retrieveRejectedConnections() throws Exception {
        when(rxTapglue.retrieveRejectedConnections()).thenReturn(Observable.just(connectionList));

        assertThat(tapglue.retrieveRejectedConnections(), equalTo(connectionList));
    }

    @Test
    public void createConnection() throws Exception {
        when(rxTapglue.createConnection(connection)).thenReturn(Observable.just(connection));

        assertThat(tapglue.createConnection(connection), equalTo(connection));
    }

    @Test
    public void createSocialConnections() throws Exception {
        when(rxTapglue.createSocialConnections(socialConnections)).thenReturn(Observable.just(userList));

        assertThat(tapglue.createSocialConnections(socialConnections), equalTo(userList));
    }

    @Test
    public void searchUsers() throws Exception {
        when(rxTapglue.searchUsers("search term")).thenReturn(Observable.just(userList));

        assertThat(tapglue.searchUsers("search term"), equalTo(userList));
    }

    @Test
    public void searchUsersByEmail() throws Exception {
        List<String> emails = mock(List.class);
        when(rxTapglue.searchUsersByEmail(emails)).thenReturn(Observable.just(userList));

        assertThat(tapglue.searchUsersByEmail(emails), equalTo(userList));
    }
}
