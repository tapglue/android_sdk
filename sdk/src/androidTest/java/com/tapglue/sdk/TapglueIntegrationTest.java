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

import android.app.Application;
import android.test.ApplicationTestCase;

import com.tapglue.sdk.entities.User;

import rx.observers.TestSubscriber;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;

public class TapglueIntegrationTest extends ApplicationTestCase<Application> {

    Configuration configuration;
    Tapglue tapglue;

    public TapglueIntegrationTest() {
        super(Application.class);
        configuration = new Configuration();
        configuration.setToken("1ecd50ce4700e0c8f501dee1fb271344");
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        createApplication();

        tapglue = new Tapglue(configuration, getContext());
    }

    public void testLoginWithUsername() {
        IntegrationObserver<User> ts = new IntegrationObserver<User>() {
            @Override
            public void onNext(User user) {
                assertThat(user.getEmail(), equalTo("john@text.com"));
            }
        };

        tapglue.loginWithUsername("john", PasswordHasher.hashPassword("qwert"))
                .toBlocking().subscribe(ts);
    }

    public void testLoginWithEmail() {
        IntegrationObserver<User> ts = new IntegrationObserver<User>() {
            @Override
            public void onNext(User user) {
                assertThat(user.getEmail(), equalTo("john@text.com"));
            }
        };
        tapglue.loginWithEmail("john@text.com", PasswordHasher.hashPassword("qwert"))
                .toBlocking().subscribe(ts);
    }

    public void testLogout() {
        final IntegrationObserver<Void> ts = new IntegrationObserver<Void>() {
            @Override
            public void onNext(Void aVoid) {

            }
        };

        tapglue.loginWithUsername("john", PasswordHasher.hashPassword("qwert"))
            .subscribe();

        tapglue.logout().subscribe(ts);
    }

    public void testCurrentUserSetAfterLogin() {
        tapglue.loginWithUsername("john", PasswordHasher.hashPassword("qwert"))
                .toBlocking().subscribe();

        IntegrationObserver<User> ts = new IntegrationObserver<User>() {
            @Override
            public void onNext(User user) {
                assertThat(user.getEmail(), equalTo("john@text.com"));
            }
        };

        tapglue.getCurrentUser().subscribe(ts);
    }

    public void testCurrentUserPersisted() {
        tapglue.loginWithUsername("john", PasswordHasher.hashPassword("qwert"))
                .toBlocking().subscribe();

        TestSubscriber<User> originalTs = new TestSubscriber<>();
        
        tapglue.getCurrentUser().subscribe(originalTs);
        final User originalUser = originalTs.getOnNextEvents().get(0);


        Tapglue alternativeTapglue = new Tapglue(configuration, getContext());

        IntegrationObserver<User> ts = new IntegrationObserver<User>() {
            @Override
            public void onNext(User user) {
                assertThat(user, equalTo(originalUser));
            }
        };

        alternativeTapglue.getCurrentUser().subscribe(ts);
    }

    public void testLoggedOutUserDeleted() {
        tapglue.loginWithUsername("john", PasswordHasher.hashPassword("qwert"))
                .toBlocking().subscribe();
        tapglue.logout().toBlocking().subscribe();

        TestSubscriber<User> ts = new TestSubscriber<>();

        tapglue.getCurrentUser().subscribe(ts);

        assertThat(ts.getOnNextEvents().size(), equalTo(0));
    }
}