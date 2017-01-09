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

import com.tapglue.android.entities.Comment;
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

public class CommentsFeedTest {

    @Test
    public void nullFeedReturnsEmptyList() {
        List<Comment> comments = new CommentsFeed().flatten();

        assertThat(comments, notNullValue());
    }
    @Test
    public void returnsListOfComments() {
        CommentsFeed feed = new CommentsFeed();
        Comment comment = mock(Comment.class);
        List<Comment> comments = Arrays.asList(comment);
        feed.comments = comments;
        feed.users = new HashMap<>();

        assertThat(feed.flatten(), equalTo(comments));
    }

    @Test
    public void populatesUsersToComments() {
        String id = "userId";
        CommentsFeed feed = new CommentsFeed();
        Comment comment = mock(Comment.class);
        when(comment.getUserId()).thenReturn(id);
        List<Comment> comments = Arrays.asList(comment);

        User user = mock(User.class);
        Map<String, User> users = new HashMap<>();
        users.put(id, user);

        feed.comments = comments;
        feed.users = users;

        feed.flatten();
        verify(comment).setUser(user);
    }
}
