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
package com.tapglue.sdk;

import com.tapglue.sdk.entities.User;
import com.tapglue.sdk.http.ServiceFactory;
import com.tapglue.sdk.http.TapglueService;
import com.tapglue.sdk.http.payloads.EmailLoginPayload;
import com.tapglue.sdk.http.payloads.UsernameLoginPayload;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import rx.Observable;
import rx.observers.TestSubscriber;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsCollectionContaining.hasItems;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class NetworkTest {
    private static final String EMAIL = "user@domain.com";
    private static final String USERNAME = "username";
    private static final String PASSWORD = "password";

    @Mock
    ServiceFactory serviceFactory;
    @Mock
    TapglueService service;
    @Mock
    TapglueService secondService;
    @Mock
    User user;

    //SUT
    Network network;

    @Before
    public void setUp() {
        when(service.login(isA(UsernameLoginPayload.class))).thenReturn(Observable.just(user));
        when(service.login(isA(EmailLoginPayload.class))).thenReturn(Observable.just(user));
        when(serviceFactory.createTapglueService()).thenReturn(service)
        .thenReturn(secondService);
        network = new Network(serviceFactory);
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
}