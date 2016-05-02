package com.tapglue.sdk;

import android.app.Application;
import android.test.ApplicationTestCase;

import com.tapglue.sdk.entities.Connection;
import com.tapglue.sdk.entities.ConnectionList;
import com.tapglue.sdk.entities.Friend;
import com.tapglue.sdk.entities.User;
import com.tapglue.sdk.http.payloads.SocialConnections;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

import static com.tapglue.sdk.entities.Connection.Type;
import static org.hamcrest.core.IsCollectionContaining.hasItems;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.IsNot.not;
import static org.hamcrest.core.IsNull.nullValue;
import static org.junit.Assert.assertThat;

public class ConnectionIntegrationTest extends ApplicationTestCase<Application>{

    private static final String PASSWORD = "superSecretPassword";
    private static final String USER_1 = "user1";
    private static final String USER_2 = "user2";

    Configuration configuration;
    Tapglue tapglue;

    User user1 = new User(USER_1, PASSWORD);
    User user2 = new User(USER_2, PASSWORD);

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

        tapglue.createUser(user1);
        tapglue.createUser(user2);
    }

    @Override
    protected void tearDown() throws Exception {
        tapglue.loginWithUsername(USER_1, PASSWORD);
        tapglue.deleteCurrentUser();
        tapglue.loginWithUsername(USER_2, PASSWORD);
        tapglue.deleteCurrentUser();

        super.tearDown();
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
        //create user 1
        User user1 = new User("retrieveRejected1", PASSWORD);
        user1 = tapglue.createUser(user1);

        //create and login user 2
        User user2 = new User("retrieveRejected2", PASSWORD);
        tapglue.createUser(user2);
        user2 = tapglue.loginWithUsername("retrieveRejected2", PASSWORD);

        //user 2 sends friend request to user 1
        tapglue.createConnection(new Friend(user1));

        //login user 1 and retrieve pending connections
        tapglue.loginWithUsername("retrieveRejected1", PASSWORD);
        ConnectionList pending = tapglue.retrievePendingConnections();

        User pendingUser = pending.getIncomingConnections().get(0).getUserFrom();

        //user 1 rejects user 2 friend request
        tapglue.createConnection(new Connection(pendingUser, Connection.Type.FRIEND, Connection.State.REJECTED));

        //login with user 2
        tapglue.loginWithUsername("retrieveRejected2", PASSWORD);

        ConnectionList rejected = tapglue.retrieveRejectedConnections();

        assertThat(rejected.getOutgoingConnections().get(0).getUserTo(), equalTo(user1));

        tapglue.deleteCurrentUser();
        tapglue.loginWithUsername("retrieveRejected1", PASSWORD);
        tapglue.deleteCurrentUser();
    }

    public void testSocialConnections () throws Exception {
        Map<String, String> socialIds = new HashMap<>();
        String platform = "platformName";
        socialIds.put(platform, "id1");
        user1.setSocialIds(socialIds);
        tapglue.loginWithUsername(USER_1, PASSWORD);
        user1 = tapglue.updateCurrentUser(user1);

        socialIds.put(platform, "id2");
        user2.setSocialIds(socialIds);
        tapglue.loginWithUsername(USER_2, PASSWORD);
        user2 = tapglue.updateCurrentUser(user2);

        List<String> socialIdsArray = Arrays.asList("id1");
        SocialConnections connections = new SocialConnections(platform, Type.FOLLOW, user2.getSocialIds().get(platform), socialIdsArray);

        assertThat(tapglue.createSocialConnections(connections), hasItems(user1));
    }

    public void testRefresh() throws Exception {
        tapglue.loginWithUsername(USER_1, PASSWORD);
        user1 = tapglue.refreshCurrentUser();

        assertThat(user1.getSessionToken(), not(nullValue()));
    }
}