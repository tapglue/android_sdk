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

package com.tapglue.sdk.http;

import java.util.Arrays;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

import com.tapglue.sdk.entities.User;
import com.tapglue.sdk.entities.Post;

import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;

public class PostFeedToListTest {
    @Test
    public void setsUsersToPosts() {
        String userId = "userId";
        Post post = mock(Post.class);
        when(post.getUserId()).thenReturn(userId);
        User user = mock(User.class);
        List<Post> posts = Arrays.asList(post);
        Map<String, User> userMap = new HashMap<>();
        userMap.put(userId, user);

        PostListFeed feed = new PostListFeed();
        feed.posts = posts;
        feed.users = userMap;

        PostFeedToList converter = new PostFeedToList();
        converter.call(feed);

        verify(post).setUser(user);
    }
    @Test
    public void returnsPostsFromFeed() {
        String userId = "userId";
        Post post = mock(Post.class);
        when(post.getUserId()).thenReturn(userId);
        User user = mock(User.class);
        List<Post> posts = Arrays.asList(post);
        Map<String, User> userMap = new HashMap<>();
        userMap.put(userId, user);

        PostListFeed feed = new PostListFeed();
        feed.posts = posts;
        feed.users = userMap;

        PostFeedToList converter = new PostFeedToList();

        List<Post> result = converter.call(feed);

        assertThat(result, equalTo(posts));
    }
}