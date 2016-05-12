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

import com.tapglue.android.entities.Event;
import com.tapglue.android.entities.NewsFeed;
import com.tapglue.android.entities.Post;
import com.tapglue.android.entities.User;

import org.junit.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class RawFeedToNewsFeedTest {
    @Test
    public void nullFeedParsesToNotNull() {
        NewsFeed feed = new RawNewsFeedToFeed().call(null);

        assertThat(feed, notNullValue());
    }

    @Test
    public void populatesUsersToEvents(){
        String id = "someId";
        RawNewsFeed rawFeed = new RawNewsFeed();
        Event event = mock(Event.class);
        List<Event> events = Arrays.asList(event);
        when(event.getUserId()).thenReturn(id);
        Map<String, User> users = new HashMap<>();
        User user = mock(User.class);
        users.put(id, user);

        rawFeed.events = events;
        rawFeed.users = users;

        new RawNewsFeedToFeed().call(rawFeed);

        verify(event).setUser(user);
    }

    @Test
    public void returnsEvents() {
        String id = "someId";
        RawNewsFeed rawFeed = new RawNewsFeed();
        Event event = mock(Event.class);
        List<Event> events = Arrays.asList(event);
        when(event.getUserId()).thenReturn(id);
        Map<String, User> users = new HashMap<>();
        User user = mock(User.class);
        users.put(id, user);

        rawFeed.events = events;
        rawFeed.users = users;

        NewsFeed feed = new RawNewsFeedToFeed().call(rawFeed);

        assertThat(feed.getEvents(), equalTo(events));
    }

    @Test
    public void populatesUsersToPosts() {
        String id = "someId";
        RawNewsFeed rawFeed = new RawNewsFeed();
        Post post = mock(Post.class);
        when(post.getUserId()).thenReturn(id);
        List<Post> posts = Arrays.asList(post);
        Map<String, User> users = new HashMap<>();
        User user = mock(User.class);
        users.put(id, user);

        rawFeed.posts = posts;
        rawFeed.users = users;

        new RawNewsFeedToFeed().call(rawFeed);

        verify(post).setUser(user);
    }

    @Test
    public void returnsPosts() {
        String id = "someId";
        RawNewsFeed rawFeed = new RawNewsFeed();
        Post post = mock(Post.class);
        when(post.getUserId()).thenReturn(id);
        List<Post> posts = Arrays.asList(post);
        Map<String, User> users = new HashMap<>();
        User user = mock(User.class);
        users.put(id, user);

        rawFeed.posts = posts;
        rawFeed.users = users;

        NewsFeed feed = new RawNewsFeedToFeed().call(rawFeed);

        assertThat(feed.getPosts(), equalTo(posts));
    }
}
