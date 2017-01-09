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
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.core.IsCollectionContaining.hasItems;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.IsNot.not;
import static org.hamcrest.core.IsNull.nullValue;
import static org.junit.Assert.assertThat;

public class ConnectionIntegrationTest extends ApplicationTestCase<Application>{

    private static final String PASSWORD = "superSecretPassword";
    private static final String USER_1 = "ConnectionIntegrationTestUser10";
    private static final String USER_2 = "ConnectionIntegrationTestUser20";
    private static final String USER_3 = "ConnectionIntegrationTestUser30";
    private static final String USER_4 = "ConnectionIntegrationTestUser40";
    private static final String USER_5 = "ConnectionIntegrationTestUser50";

    Configuration configuration;
    Tapglue tapglue;

    User user1 = new User(USER_1, PASSWORD);
    User user2 = new User(USER_2, PASSWORD);
    User user3 = new User(USER_3, PASSWORD);
    User user4 = new User(USER_4, PASSWORD);
    User user5 = new User(USER_5, PASSWORD);

    public ConnectionIntegrationTest() {
        super(Application.class);
        configuration = new Configuration(TestData.URL, TestData.TOKEN);
        configuration.setLogging(true);
        configuration.setPageSize(1);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        createApplication();

        tapglue = new Tapglue(configuration, getContext());

        user1 = tapglue.createUser(user1);
        user2 = tapglue.createUser(user2);
        user3 = tapglue.createUser(user3);
        user4 = tapglue.createUser(user4);
        user5 = tapglue.createUser(user5);
    }

    @Override
    protected void tearDown() throws Exception {
        tapglue.loginWithUsername(USER_1, PASSWORD);
        tapglue.deleteCurrentUser();
        tapglue.loginWithUsername(USER_2, PASSWORD);
        tapglue.deleteCurrentUser();
        tapglue.loginWithUsername(USER_3, PASSWORD);
        tapglue.deleteCurrentUser();
        tapglue.loginWithUsername(USER_4, PASSWORD);
        tapglue.deleteCurrentUser();
        tapglue.loginWithUsername(USER_5, PASSWORD);
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
        RxTapglue rxTapglue = new RxTapglue(configuration, getContext());
        List<User> fs =  rxTapglue.retrieveFollowings().toBlocking().first().getData();

        assertThat(fs, hasItems(user2));
    }

    public void testRetrieveFollowers() throws IOException {
        user1 = tapglue.loginWithUsername(USER_1, PASSWORD);
        Connection connection = new Follow(user2);
        tapglue.createConnection(connection);
        user2 = tapglue.loginWithUsername(USER_2, PASSWORD);
        RxTapglue rxTapglue = new RxTapglue(configuration, getContext());
        List<User> fs = rxTapglue.retrieveFollowers().toBlocking().first().getData();

        assertThat(fs, hasItems(user1));
    }

    public void testRetrieveFollowingsForUser() throws IOException {
        user1 = tapglue.loginWithUsername(USER_1, PASSWORD);
        Connection connection = new Connection(user2, Connection.Type.FOLLOW, 
            Connection.State.CONFIRMED);
        tapglue.createConnection(connection);

        user2  = tapglue.loginWithUsername(USER_2, PASSWORD);

        RxTapglue rxTapglue = new RxTapglue(configuration, getContext());
        List<User> followings = rxTapglue.retrieveUserFollowings(user1.getId()).toBlocking().first().getData();

        assertThat(followings, hasItems(user2));
    }

    public void testRetrieveFollowersForUser() throws IOException {
        user1 = tapglue.loginWithUsername(USER_1, PASSWORD);
        Connection connection = new Connection(user2, Connection.Type.FOLLOW,
                Connection.State.CONFIRMED);
        tapglue.createConnection(connection);

        RxTapglue rxTapglue = new RxTapglue(configuration, getContext());
        List<User> followings = rxTapglue.retrieveUserFollowers(user2.getId()).toBlocking().first().getData();

        assertThat(followings, hasItems(user1));
    }

    public void testRetrieveFriendsPage() throws IOException {
        RxTapglue rxTapglue =  new RxTapglue(configuration, getContext());
        user1 = rxTapglue.loginWithUsername(USER_1, PASSWORD).toBlocking().first();
        Connection connection = new Connection(user2, Type.FRIEND,
                Connection.State.CONFIRMED);
        rxTapglue.createConnection(connection).toBlocking().first();
        user2 = rxTapglue.loginWithUsername(USER_2, PASSWORD).toBlocking().first();
        List<User> fs = rxTapglue.retrieveFriends().toBlocking().first().getData();

        assertThat(fs, hasItems(user1));
    }

    public void testRetrieveUserFriendsPage() throws IOException {
        RxTapglue rxTapglue = new RxTapglue(configuration, getContext());
        rxTapglue.loginWithUsername(USER_1, PASSWORD).toBlocking().first();
        Connection connection = new Connection(user2, Type.FRIEND,
                Connection.State.CONFIRMED);
        rxTapglue.createConnection(connection).toBlocking().first();
        List<User> friends = rxTapglue.retrieveUserFriends(user2.getId()).toBlocking().first().getData();

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

    public void testRetrievePendingOutgoingConnectionsPage() throws IOException {
        RxTapglue rxTapglue = new RxTapglue(configuration, getContext());
        user1 = rxTapglue.loginWithUsername(USER_1,PASSWORD).toBlocking().first();
        user2 = rxTapglue.loginWithUsername(USER_2, PASSWORD).toBlocking().first();

        rxTapglue.createConnection(new Friend(user1)).toBlocking().first();

        ConnectionList connectionList = rxTapglue.retrievePendingConnections()
            .toBlocking().first().getData();

        assertThat(connectionList.getOutgoingConnections().get(0).getUserTo(), equalTo(user1));
    }

    public void testRetrievePendingIncomingConnectionsPage() throws IOException {
        RxTapglue rxTapglue = new RxTapglue(configuration, getContext());
        user2 = rxTapglue.loginWithUsername(USER_2, PASSWORD).toBlocking().first();

        rxTapglue.createConnection(new Friend(user1)).toBlocking().first();

        rxTapglue.loginWithUsername(USER_1, PASSWORD).toBlocking().first();
        ConnectionList connectionList = rxTapglue.retrievePendingConnections()
            .toBlocking().first().getData();

        assertThat(connectionList.getIncomingConnections().get(0).getUserFrom(), equalTo(user2));
    }

    public void testRetrievePendingConnectionsPagination() throws IOException {
        configuration.setPageSize(2);
        RxTapglue rxTapglue = new RxTapglue(configuration, getContext());
        user2 = rxTapglue.loginWithUsername(USER_2, PASSWORD).toBlocking().first();

        rxTapglue.createConnection(new Friend(user1)).toBlocking().first();

        user3 = rxTapglue.loginWithUsername(USER_3, PASSWORD).toBlocking().first();
        rxTapglue.createConnection(new Friend(user2)).toBlocking().first();

        user2 = rxTapglue.loginWithUsername(USER_2, PASSWORD).toBlocking().first();

        rxTapglue.createConnection(new Friend(user4)).toBlocking().first();

        user5 = rxTapglue.loginWithUsername(USER_5, PASSWORD).toBlocking().first();
        rxTapglue.createConnection(new Friend(user2)).toBlocking().first();


        user2 = rxTapglue.loginWithUsername(USER_2, PASSWORD).toBlocking().first();
        RxPage<ConnectionList> firstPage = rxTapglue.retrievePendingConnections()
                .toBlocking().first();
        RxPage<ConnectionList> secondPage = firstPage.getPrevious().toBlocking().first();
        RxPage<ConnectionList> thirdPage = secondPage.getPrevious().toBlocking().first();

        ConnectionList first = firstPage.getData();
        ConnectionList second = secondPage.getData();
        ConnectionList third = thirdPage.getData();

        assertThat(first.getIncomingConnections().get(0).getUserFrom(), equalTo(user5));
        assertThat(first.getOutgoingConnections().get(0).getUserTo(), equalTo(user4));

        assertThat(second.getIncomingConnections().get(0).getUserFrom(), equalTo(user3));
        assertThat(second.getOutgoingConnections().get(0).getUserTo(), equalTo(user1));
    }

    public void testRetrieveRejectedConnections() throws IOException {
        RxTapglue rxTapglue = new RxTapglue(configuration, getContext());

        //login user 2
        user2 = rxTapglue.loginWithUsername(USER_2, PASSWORD).toBlocking().first();

        //user 2 sends friend request to user 1
        rxTapglue.createConnection(new Friend(user1)).toBlocking().first();

        //login user 1 and retrieve pending connections
        user1 = rxTapglue.loginWithUsername(USER_1, PASSWORD).toBlocking().first();
        ConnectionList pending = rxTapglue.retrievePendingConnections().toBlocking().first().getData();

        User pendingUser = pending.getIncomingConnections().get(0).getUserFrom();

        //user 1 rejects user 2 friend request
        rxTapglue.createConnection(new Connection(pendingUser, Connection.Type.FRIEND, Connection.State.REJECTED)).toBlocking().first();

        //login with user 2
        rxTapglue.loginWithUsername(USER_2, PASSWORD).toBlocking().first();

        ConnectionList rejected = rxTapglue.retrieveRejectedConnections().toBlocking().first().getData();

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

    public void testUserSearchPagination() throws Exception {
        RxTapglue rxTapglue = new RxTapglue(configuration, getContext());
        user1 = rxTapglue.loginWithUsername(USER_1, PASSWORD).toBlocking().first();

        RxPage<List<User>> firstPage = rxTapglue.searchUsers("Connection").toBlocking().first();
        RxPage<List<User>> secondPage = firstPage.getPrevious().toBlocking().first();
        List<User> first = firstPage.getData();
        List<User> second = secondPage.getData();

        assertThat(first.size(), equalTo(1));
        assertThat(second.size(), equalTo(1));
    }

    public void testUserEmailSearchPage() throws Exception {
        RxTapglue rxTapglue = new RxTapglue(configuration, getContext());
        user2 = rxTapglue.loginWithUsername(USER_2, PASSWORD).toBlocking().first();
        user2.setEmail("user@domain.com");
        rxTapglue.updateCurrentUser(user2).toBlocking().first();

        rxTapglue.loginWithUsername(USER_1, PASSWORD).toBlocking().first();

        List<String> emails = Arrays.asList("user@domain.com");
        List<User> users = rxTapglue.searchUsersByEmail(emails).toBlocking().first().getData();

        assertThat(users, hasItems(user2));
    }

    public void testUserEmailSearchSecondPage() throws Exception {
        RxTapglue rxTapglue = new RxTapglue(configuration, getContext());
        user2 = rxTapglue.loginWithUsername(USER_2, PASSWORD).toBlocking().first();
        user2.setEmail("user@domain.com");
        rxTapglue.updateCurrentUser(user2).toBlocking().first();

        rxTapglue.loginWithUsername(USER_1, PASSWORD).toBlocking().first();

        List<String> emails = Arrays.asList("user@domain.com");
        RxPage<List<User>> firstPage = rxTapglue.searchUsersByEmail(emails)
            .toBlocking().first();
        RxPage<List<User>> secondPage = firstPage.getPrevious().toBlocking().first();

        assertThat(firstPage.getData(), hasItems(user2));
    }

    public void testUserSocialSearchPage() throws Exception {
        RxTapglue rxTapglue = new RxTapglue(configuration, getContext());
        user2 = rxTapglue.loginWithUsername(USER_2, PASSWORD).toBlocking().first();
        Map<String, String> socialIds = new HashMap<>();
        String platform = "facebook";
        socialIds.put(platform, "id24");
        user2.setSocialIds(socialIds);
        user2 = rxTapglue.updateCurrentUser(user2).toBlocking().first();

        rxTapglue.loginWithUsername(USER_1, PASSWORD).toBlocking().first();

        List<String> socialIdsArray = Arrays.asList("id24");
        List<User> users = rxTapglue.searchUsersBySocialIds(platform, socialIdsArray).toBlocking().first().getData();

        assertThat(users, hasItems(user2));
    }

    public void testUserSocialSearchSecondPage() throws Exception {
        RxTapglue rxTapglue = new RxTapglue(configuration, getContext());
        user2 = rxTapglue.loginWithUsername(USER_2, PASSWORD).toBlocking().first();
        Map<String, String> socialIds = new HashMap<>();
        String platform = "facebook";
        socialIds.put(platform, "id24");
        user2.setSocialIds(socialIds);
        user2 = rxTapglue.updateCurrentUser(user2).toBlocking().first();

        rxTapglue.loginWithUsername(USER_1, PASSWORD).toBlocking().first();

        List<String> socialIdsArray = Arrays.asList("id24");
        RxPage<List<User>> users = rxTapglue.searchUsersBySocialIds(platform, socialIdsArray)
                .toBlocking().first();
        RxPage<List<User>> secondPage = users.getPrevious().toBlocking().first();

        assertThat(secondPage.getData(), is(not(nullValue())));
    }
}