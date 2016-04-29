package com.tapglue.sdk;

import android.app.Application;
import android.test.ApplicationTestCase;

import com.tapglue.sdk.entities.Connection;
import com.tapglue.sdk.entities.ConnectionList;
import com.tapglue.sdk.entities.Friend;
import com.tapglue.sdk.entities.User;

import java.io.IOException;
import java.util.List;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;

public class ConnectionIntegrationTest extends ApplicationTestCase<Application>{

    private static final String PASSWORD = "superSecretPassword";

    Configuration configuration;
    Tapglue tapglue;

    public ConnectionIntegrationTest() {
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

    public void testRetrievePendingOutgoingConnections() throws IOException {
        User user1 = new User("retrievePending1", PASSWORD);
        tapglue.createUser(user1);
        user1 = tapglue.loginWithUsername("retrievePending1", PASSWORD);
        User user2 = new User("retrievePending2", PASSWORD);
        tapglue.createUser(user2);
        tapglue.loginWithUsername("retrievePending2", PASSWORD);

        tapglue.createConnection(new Friend(user1));

        ConnectionList connectionList = tapglue.retrievePendingConnections();

        assertThat(connectionList.getOutgoingConnections().get(0).getUserTo(), equalTo(user1));

        tapglue.deleteCurrentUser();
        tapglue.loginWithUsername("retrievePending1", PASSWORD);
        tapglue.deleteCurrentUser();
    }

    public void testRetrievePendingIncomingConnections() throws IOException {
        User user1 = new User("retrievePendingInc1", PASSWORD);
        tapglue.createUser(user1);
        user1 = tapglue.loginWithUsername("retrievePendingInc1", PASSWORD);
        User user2 = new User("retrievePendingInc2", PASSWORD);
        tapglue.createUser(user2);
        user2 = tapglue.loginWithUsername("retrievePendingInc2", PASSWORD);

        tapglue.createConnection(new Friend(user1));

        tapglue.loginWithUsername("retrievePendingInc1", PASSWORD);
        ConnectionList connectionList = tapglue.retrievePendingConnections();

        assertThat(connectionList.getIncomingConnections().get(0).getUserFrom(), equalTo(user2));

        tapglue.deleteCurrentUser();
        tapglue.loginWithUsername("retrievePendingInc2", PASSWORD);
        tapglue.deleteCurrentUser();
    }

    public void testRetrieveRejectedConnections() throws IOException {
        User user1 = new User("retrieveRejected1", PASSWORD);
        user1 = tapglue.createUser(user1);

        User user2 = new User("retrieveRejected2", PASSWORD);
        tapglue.createUser(user2);
        user2 = tapglue.loginWithUsername("retrieveRejected2", PASSWORD);

        //send friend request
        tapglue.createConnection(new Friend(user1));

        tapglue.loginWithUsername("retrieveRejected1", PASSWORD);
        ConnectionList pending = tapglue.retrievePendingConnections();

        User pendingUser = pending.getIncomingConnections().get(0).getUserFrom();

        //reject friend request
        tapglue.createConnection(new Connection(pendingUser, Connection.Type.FRIEND, Connection.State.REJECTED));

        tapglue.loginWithUsername("retrieveRejected2", PASSWORD);

        ConnectionList rejected = tapglue.retrieveRejectedConnections();

        assertThat(rejected.getOutgoingConnections().get(0).getUserFrom(), equalTo(user1));

        tapglue.deleteCurrentUser();
        tapglue.loginWithUsername("retrieveRejected1", PASSWORD);
        tapglue.deleteCurrentUser();
    }
}