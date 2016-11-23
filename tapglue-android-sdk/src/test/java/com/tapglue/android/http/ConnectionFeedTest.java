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

package com.tapglue.android.http;

import com.tapglue.android.entities.Connection;
import com.tapglue.android.entities.User;
import com.tapglue.android.entities.ConnectionList;
import com.tapglue.android.http.ConnectionFeedToList;
import com.tapglue.android.http.ConnectionsFeed;

import org.junit.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Arrays;
import java.util.ArrayList;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsNot.not;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ConnectionFeedTest {

    @Test
    public void nullFeedReturnsEmptyIncoming() {
        ConnectionList connections = new ConnectionsFeed().flatten();

        assertThat(connections.getIncomingConnections(), notNullValue());
    }

    @Test
    public void nullIncomingInFeedReturnsEmptyList() {
        ConnectionsFeed feed = new ConnectionsFeed();
        feed.incoming = null;
        feed.outgoing = new ArrayList<>();
        feed.users = new ArrayList<>();

        ConnectionList connections = feed.flatten();
        assertThat(connections.getIncomingConnections(), notNullValue());
    }

    @Test
    public void nullOutgoingInFeedReturnsEmptyList() {
        ConnectionsFeed feed = new ConnectionsFeed();
        feed.incoming = new ArrayList<>();
        feed.outgoing = null;
        feed.users = new ArrayList<>();

        ConnectionList connections = feed.flatten();
        assertThat(connections.getOutgoingConnections(), notNullValue());
    }

    @Test
    public void nullUsersInFeedReturns() {
        ConnectionsFeed feed = new ConnectionsFeed();
        feed.incoming = new ArrayList<>();
        feed.outgoing = new ArrayList<>();
        feed.users = null;

        feed.flatten();
    }
    @Test
    public void nullFeedReturnsEmptyOutgoing() {
        ConnectionList connections = new ConnectionFeedToList().call(null);

        assertThat(connections.getOutgoingConnections(), notNullValue());
    }

    @Test
    public void returnsIncomingConnectionsFromFeed() {
        List<Connection> incomingConnections = Arrays.asList(mock(Connection.class));
        ConnectionsFeed feed = new ConnectionsFeed();

        feed.incoming = incomingConnections;
        feed.outgoing = new ArrayList<Connection>();
        feed.users = new ArrayList<User>();

        ConnectionList list = feed.flatten();

        assertThat(list.getIncomingConnections(), equalTo(incomingConnections));
    }

    @Test
    public void returnsOutgoingConnectionsFromFeed() {
        List<Connection> outgoingConnections = Arrays.asList(mock(Connection.class));
        ConnectionsFeed feed = new ConnectionsFeed();

        feed.incoming = new ArrayList<>();
        feed.outgoing = outgoingConnections;
        feed.users = new ArrayList<>();

        ConnectionList list = feed.flatten();

        assertThat(list.getOutgoingConnections(), equalTo(outgoingConnections));
    }
    @Test
    public void populatesIncomingConnectionUsers() {
        Connection connection = mock(Connection.class);
        when(connection.getUserFromId()).thenReturn("id");
        User userFrom = mock(User.class);
        when(userFrom.getId()).thenReturn("id");
        ConnectionsFeed feed = new ConnectionsFeed();

        feed.users = Arrays.asList(userFrom);
        feed.incoming =  Arrays.asList(connection);
        feed.outgoing = new ArrayList<>();

        feed.flatten();

        verify(connection).setUserFrom(userFrom);
    }

    @Test
    public void populatesOutgoingConnectionUsers() {
        Connection connection = mock(Connection.class);
        when(connection.getUserToId()).thenReturn("id");
        User userTo = mock(User.class);
        when(userTo.getId()).thenReturn("id");
        ConnectionsFeed feed = new ConnectionsFeed();

        feed.users = Arrays.asList(userTo);
        feed.outgoing =  Arrays.asList(connection);
        feed.incoming = new ArrayList<>();

        feed.flatten();

        verify(connection).setUserTo(userTo);
    }
}