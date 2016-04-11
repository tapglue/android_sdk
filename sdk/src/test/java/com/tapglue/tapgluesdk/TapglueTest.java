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
package com.tapglue.tapgluesdk;

import android.content.Context;

import com.tapglue.tapgluesdk.entities.User;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import rx.Observable;
import rx.functions.Action0;
import rx.functions.Func1;
import rx.observers.TestSubscriber;

import static org.hamcrest.core.IsCollectionContaining.hasItems;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class TapglueTest {
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
    Network network;
    @Mock
    UserStore currentUser;
    @Mock
    Exception e;
    @Mock
    Action0 clearAction;


    @Mock
    User user;

    //SUT
    Tapglue tapglue;

    @Before
    public void setUp() {
        when(currentUser.clear()).thenReturn(clearAction);
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
        tapglue = new Tapglue(configuration, context);
        tapglue.network = network;
        tapglue.currentUser = currentUser;
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
}
