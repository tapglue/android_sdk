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

public class EventListFeedTest {
    @Test
    public void nullFeedReturnsEmptyList() {
        List<Event> events = new EventListFeed().flatten();

        assertThat(events, notNullValue());
    }

    @Test
    public void returnsEventsFromFeed() {
        Event event = mock(Event.class);
        List<Event> events = Arrays.asList(event);
        EventListFeed feed = new EventListFeed();
        feed.events = events;
        feed.users = new HashMap<>();
        feed.posts = new HashMap<>();

        List<Event> result = feed.flatten();

        assertThat(result, equalTo(events));
    }

    @Test
    public void populatesUsersToEvents() {
        String id = "userId";
        Event event = mock(Event.class);
        List<Event> events = Arrays.asList(event);
        when(event.getUserId()).thenReturn(id);
        Map<String, User> users = new HashMap<>();
        User user = mock(User.class);
        users.put(id, user);
        EventListFeed feed = new EventListFeed();
        feed.events = events;
        feed.users = users;
        feed.posts = new HashMap<>();

        feed.flatten();

        verify(event).setUser(user);
    }

    @Test
    public void populatesPostsToEvents() {
        String postId = "postId";
        Post post = mock(Post.class);

        Event event = mock(Event.class);
        when(event.getPostId()).thenReturn(postId);
        EventListFeed feed = new EventListFeed();
        feed.events =  Arrays.asList(event);
        Map<String, Post> postMap = new HashMap<String, Post>();
        postMap.put(postId, post);
        feed.posts = postMap;
        feed.users = new HashMap<>();

        feed.flatten();

        verify(event).setPost(post);
    }
}
