package com.tapglue.android;

import android.app.Application;
import android.test.ApplicationTestCase;

import com.tapglue.android.entities.Connection;
import com.tapglue.android.entities.ConnectionList;
import com.tapglue.android.entities.Follow;
import com.tapglue.android.entities.Friend;
import com.tapglue.android.entities.User;
import com.tapglue.android.http.payloads.SocialConnections;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

import static com.tapglue.android.entities.Connection.Type;
import static org.hamcrest.core.IsCollectionContaining.hasItems;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.IsNot.not;
import static org.hamcrest.core.IsNull.nullValue;
import static org.junit.Assert.assertThat;

public class ConnectionIntegrationTest extends ApplicationTestCase<Application>{

    private static final String PASSWORD = "superSecretPassword";
    private static final String USER_1 = "ConnectionIntegrationTestUser10";
    private static final String USER_2 = "ConnectionIntegrationTestUser20";

    Configuration configuration;
    Tapglue tapglue;

    User user1 = new User(USER_1, PASSWORD);
    User user2 = new User(USER_2, PASSWORD);

    public ConnectionIntegrationTest() {
        super(Application.class);
        configuration = new Configuration(TestData.URL, TestData.TOKEN);
        configuration.setLogging(true);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        createApplication();

        tapglue = new Tapglue(configuration, getContext());

        user1 = tapglue.createUser(user1);
        user2 = tapglue.createUser(user2);
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
        user1 = tapglue.loginWithUsername(USER_1, PASSWORD);

        assertThat(tapglue.retrieveUser(user2.getId()), equalTo(user2));
    }

    public void testRetrieveFollowings() throws IOException {
        user1 = tapglue.loginWithUsername(USER_1, PASSWORD);
        Connection connection = new Follow(user2);
        tapglue.createConnection(connection);
        List<User> fs =  tapglue.retrieveFollowings();

        assertThat(fs, hasItems(user2));
    }

    public void testRetrieveFollowers() throws IOException {
        user1 = tapglue.loginWithUsername(USER_1, PASSWORD);
        Connection connection = new Follow(user2);
        tapglue.createConnection(connection);
        user2 = tapglue.loginWithUsername(USER_2, PASSWORD);
        List<User> fs = tapglue.retrieveFollowers();

        assertThat(fs, hasItems(user1));
    }

    public void testRetrieveFollowingsForUser() throws IOException {
        user1 = tapglue.loginWithUsername(USER_1, PASSWORD);
        Connection connection = new Connection(user2, Connection.Type.FOLLOW, 
            Connection.State.CONFIRMED);
        tapglue.createConnection(connection);

        user2  = tapglue.loginWithUsername(USER_2, PASSWORD);

        List<User> followings = tapglue.retrieveUserFollowings(user1.getId());

        assertThat(followings, hasItems(user2));
    }

    public void testRetrieveFollowersForUser() throws IOException {
        user1 = tapglue.loginWithUsername(USER_1, PASSWORD);
        Connection connection = new Connection(user2, Connection.Type.FOLLOW,
                Connection.State.CONFIRMED);
        tapglue.createConnection(connection);

        List<User> followings = tapglue.retrieveUserFollowers(user2.getId());

        assertThat(followings, hasItems(user1));
    }

    public void testRetrieveFriends() throws IOException {
        tapglue.loginWithUsername("john", PasswordHasher.hashPassword("qwert"));
        List<User> friends = tapglue.retrieveFriends();

        assertThat(friends.size(), equalTo(0));
    }

    public void testRetrieveUserFriends() throws IOException {
        tapglue.loginWithUsername(USER_1, PASSWORD);
        Connection connection = new Connection(user2, Type.FRIEND,
                Connection.State.CONFIRMED);
        tapglue.createConnection(connection);
        List<User> friends = tapglue.retrieveUserFriends(user2.getId());

        assertThat(friends, hasItems(user1));
    }

    public void testCreateConnection() throws IOException {
        user2 = tapglue.loginWithUsername(USER_2, PASSWORD);

        Connection connection = new Connection(user1, Connection.Type.FOLLOW, Connection.State.CONFIRMED);
        tapglue.createConnection(connection);
    }

    public void testDeleteConnection() throws IOException {
        user2 = tapglue.loginWithUsername(USER_2, PASSWORD);

        Connection connection = new Connection(user1, Connection.Type.FOLLOW, Connection.State.CONFIRMED);
        tapglue.createConnection(connection);

        tapglue.deleteConnection(user1.getId(), Connection.Type.FOLLOW);
    }

    public void testRetrievePendingOutgoingConnections() throws IOException {
        user1 = tapglue.loginWithUsername(USER_1,PASSWORD);
        user2  = tapglue.loginWithUsername(USER_2, PASSWORD);

        tapglue.createConnection(new Friend(user1));

        ConnectionList connectionList = tapglue.retrievePendingConnections();

        assertThat(connectionList.getOutgoingConnections().get(0).getUserTo(), equalTo(user1));
    }

    public void testRetrievePendingIncomingConnections() throws IOException {
        user2 = tapglue.loginWithUsername(USER_2, PASSWORD);

        tapglue.createConnection(new Friend(user1));

        tapglue.loginWithUsername(USER_1, PASSWORD);
        ConnectionList connectionList = tapglue.retrievePendingConnections();

        assertThat(connectionList.getIncomingConnections().get(0).getUserFrom(), equalTo(user2));
    }

    public void testRetrieveRejectedConnections() throws IOException {
        //login user 2
        user2 = tapglue.loginWithUsername(USER_2, PASSWORD);

        //user 2 sends friend request to user 1
        tapglue.createConnection(new Friend(user1));

        //login user 1 and retrieve pending connections
        user1 = tapglue.loginWithUsername(USER_1, PASSWORD);
        ConnectionList pending = tapglue.retrievePendingConnections();

        User pendingUser = pending.getIncomingConnections().get(0).getUserFrom();

        //user 1 rejects user 2 friend request
        tapglue.createConnection(new Connection(pendingUser, Connection.Type.FRIEND, Connection.State.REJECTED));

        //login with user 2
        tapglue.loginWithUsername(USER_2, PASSWORD);

        ConnectionList rejected = tapglue.retrieveRejectedConnections();

        assertThat(rejected.getOutgoingConnections().get(0).getUserTo(), equalTo(user1));
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

    public void testUserSearch() throws Exception {
        RxTapglue rxTapglue = new RxTapglue(configuration, getContext());
        user1 = rxTapglue.loginWithUsername(USER_1, PASSWORD).toBlocking().first();

        RxPage<List<User>> users = rxTapglue.searchUsers(USER_2).toBlocking().first();

        assertThat(users.getData(), hasItems(user2));
    }


    public void testUserEmailSearch() throws Exception {
        user2 = tapglue.loginWithUsername(USER_2, PASSWORD);
        user2.setEmail("user@domain.com");
        tapglue.updateCurrentUser(user2);

        tapglue.loginWithUsername(USER_1, PASSWORD);

        List<String> emails = Arrays.asList("user@domain.com");
        List<User> users = tapglue.searchUsersByEmail(emails);

        assertThat(users, hasItems(user2));
    }

    public void testUserSocialSearch() throws Exception {
        user2 = tapglue.loginWithUsername(USER_2, PASSWORD);
        Map<String, String> socialIds = new HashMap<>();
        String platform = "facebook";
        socialIds.put(platform, "id24");
        user2.setSocialIds(socialIds);
        user2 = tapglue.updateCurrentUser(user2);

        tapglue.loginWithUsername(USER_1, PASSWORD);

        List<String> socialIdsArray = Arrays.asList("id24");
        List<User> users = tapglue.searchUsersBySocialIds(platform, socialIdsArray);

        assertThat(users, hasItems(user2));
    }
}