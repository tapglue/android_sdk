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

import com.tapglue.android.entities.Like;
import com.tapglue.android.entities.Post;
import com.tapglue.android.entities.User;

import org.junit.Test;

import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import java.util.HashMap;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class LikesFeedTest {
    @Test
    public void nullFeedReturnsEmptyList() {
        List<Like> likes = new LikesFeed().flatten();

        assertThat(likes, notNullValue());
    }
    @Test
    public void returnsLikes() {
        List<Like> likes = new ArrayList<>();
        Map<String, User> users = new HashMap<>();
        LikesFeed feed = new LikesFeed();
        feed.likes = likes;
        feed.users = users;
        feed.posts = new HashMap<>();

        assertThat(feed.flatten(), equalTo(likes));
    }

    @Test
    public void populatesUsersToLikes() {
        String id = "someId";
        Like like = mock(Like.class);
        List<Like> likes = Arrays.asList(like);
        Map<String, User> userMap = new HashMap<>();
        User user = mock(User.class);
        userMap.put(id, user);

        when(like.getUserId()).thenReturn(id);

        LikesFeed feed = new LikesFeed();
        feed.likes = likes;
        feed.users = userMap;
        feed.posts = new HashMap<>();

        feed.flatten();

        verify(like).setUser(user);
    }

    @Test
    public void populatesPostsToLikes() {
        String id = "someId";
        Like like = mock(Like.class);
        List<Like> likes = Arrays.asList(like);
        Map<String, Post> postMap = new HashMap<>();
        Post post = mock(Post.class);
        postMap.put(id, post);

        when(like.getPostId()).thenReturn(id);

        LikesFeed feed = new LikesFeed();
        feed.likes = likes;
        feed.users = new HashMap<>();
        feed.posts = postMap;

        feed.flatten();

        verify(like).setPost(post);
    }
}
