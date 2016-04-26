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

import com.tapglue.sdk.entities.Connection;
import com.tapglue.sdk.entities.User;
import com.tapglue.sdk.http.TapglueError;

import java.io.IOException;
import java.util.List;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.IsNull.nullValue;
import static org.junit.Assert.assertThat;

public class LoginIntegrationTest extends ApplicationTestCase<Application> {

    private static final String PASSWORD = "superSecretPassword";

    Configuration configuration;
    Tapglue tapglue;

    public LoginIntegrationTest() {
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

    public void testLoginWithUsername() throws Throwable {
        User user = tapglue.loginWithUsername("john", PasswordHasher.hashPassword("qwert"));

        assertThat(user.getEmail(), equalTo("john@text.com"));
    }

    public void testLoginWithEmail() throws Throwable {
        User user = tapglue.loginWithEmail("john@text.com", PasswordHasher.hashPassword("qwert"));

        assertThat(user.getEmail(), equalTo("john@text.com"));
    }


    public void testLoginWithWrongCredentials() throws IOException {
        try {
            tapglue.loginWithUsername("johnnnn", PasswordHasher.hashPassword("qwert"));
            fail("did not throw TapglueError on wrong credentials");
        } catch(TapglueError e) {

        }
    }

    public void testGetCurrentUserAfterLogin() throws IOException {
        tapglue.loginWithUsername("john", PasswordHasher.hashPassword("qwert"));
        User user = tapglue.getCurrentUser();

        assertThat(user.getEmail(), equalTo("john@text.com"));
    }

    public void testGetCurrentUserAfterLogout() throws IOException {
        tapglue.loginWithUsername("john", PasswordHasher.hashPassword("qwert"));
        tapglue.logout();
        User user = tapglue.getCurrentUser();

        assertThat(user, nullValue());
    }

    public void testCreateAndDeleteUser() throws IOException {
        User user = new User("newUser", PASSWORD);
        User createdUser = tapglue.createUser(user);

        assertThat(createdUser.getUserName(), equalTo("newUser"));

        tapglue.loginWithUsername("newUser", PASSWORD);

        tapglue.deleteCurrentUser();

        assertThat(tapglue.getCurrentUser(), nullValue());
    }

    public void testUpdateUser() throws IOException {
        User user = new User("updateUserTest", PASSWORD);
        User createdUser = tapglue.createUser(user);

        User loggedInUser = tapglue.loginWithUsername("updateUserTest", PASSWORD);

        loggedInUser.setEmail("some@email.com");

        User updatedUser = tapglue.updateCurrentUser(loggedInUser);

        assertThat(updatedUser.getEmail(), equalTo("some@email.com"));

        tapglue.deleteCurrentUser();

        assertThat(tapglue.getCurrentUser(), nullValue());
    }

    public void testRetrieveUser() throws IOException {
        User user = new User("retrieveUserTest", PASSWORD);
        User createdUser = tapglue.createUser(user);
        tapglue.loginWithUsername("retrieveUserTest", PASSWORD);

        assertThat(tapglue.retrieveUser(createdUser.getId()), equalTo(createdUser));

        tapglue.deleteCurrentUser();
    }

    public void testRetrieveFollowings() throws IOException {
        tapglue.loginWithUsername("john", PasswordHasher.hashPassword("qwert"));

        List<User> followings = tapglue.retrieveFollowings();

        assertThat(followings.size(), equalTo(5));
    }

    public void testRetrieveFollowers() throws IOException {
        tapglue.loginWithUsername("john", PasswordHasher.hashPassword("qwert"));
        List<User> followers = tapglue.retrieveFollowers();

        assertThat(followers.size(), equalTo(6));
    }

    public void testRetrieveFriends() throws IOException {
        tapglue.loginWithUsername("john", PasswordHasher.hashPassword("qwert"));
        List<User> friends = tapglue.retrieveFriends();

        assertThat(friends.size(), equalTo(0));
    }

    public void testCreateConnection() throws IOException {
        User user1 = new User("createConnectionUser1", PASSWORD);
        user1 = tapglue.createUser(user1);

        User user2 = new User("createConnectionUser2", PASSWORD);
        tapglue.createUser(user2);

        tapglue.loginWithUsername("createConnectionUser2", PASSWORD);

        Connection connection = new Connection(user1, Connection.Type.FOLLOW, Connection.State.CONFIRMED);
        Connection createdConnection = tapglue.createConnection(connection);

        tapglue.deleteCurrentUser();
        tapglue.loginWithUsername("createConnectionUser1", PASSWORD);
        tapglue.deleteCurrentUser();
    }
}
