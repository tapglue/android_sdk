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

package com.tapglue.sdk;

import android.app.Application;
import android.test.ApplicationTestCase;

import com.tapglue.sdk.entities.Comment;
import com.tapglue.sdk.entities.Post;
import com.tapglue.sdk.entities.User;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsCollectionContaining.hasItems;
import static org.hamcrest.core.IsEqual.equalTo;

public class CommentIntegrationTest extends ApplicationTestCase<Application> {
    private static final String PASSWORD = "superSecretPassword";
    private static final String USER_1 = "user1";
    private static final String USER_2 = "user2";

    Configuration configuration;
    Tapglue tapglue;

    User user1 = new User(USER_1, PASSWORD);
    User user2 = new User(USER_2, PASSWORD);

    public CommentIntegrationTest() {
        super(Application.class);
        configuration = new Configuration();
        configuration.setToken("1ecd50ce4700e0c8f501dee1fb271344");
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

    public void testCreateAndDeleteComment() throws IOException {
        user1 = tapglue.loginWithUsername(USER_1, PASSWORD);
        Post post = new Post(Post.Visibility.PUBLIC);
        post = tapglue.createPost(post);
        Map<String, String> contents = new HashMap<>();
        contents.put("en-US", "these are the contents");
        Comment comment = new Comment(contents);

        comment = tapglue.createComment(post.getId(), comment);
        String content = comment.getContents().get("en-US");
        tapglue.deleteComment(post.getId(), comment.getId());
        assertThat(content, equalTo("these are the contents"));
    }

    public void testUpdateComment() throws IOException {
        user1 = tapglue.loginWithUsername(USER_1, PASSWORD);
        Post post = new Post(Post.Visibility.PUBLIC);
        post = tapglue.createPost(post);
        Map<String, String> contents = new HashMap<>();
        contents.put("en-US", "original content");
        Comment comment = new Comment(contents);

        //create first comment
        comment = tapglue.createComment(post.getId(), comment);
        contents.put("en-US", "updated content");
        Comment newComment = new Comment(contents);
        //update comment with new content
        comment = tapglue.updateComment(post.getId(), comment.getId(), newComment);

        assertThat(comment.getContents().get("en-US"), equalTo("updated content"));
    }

    public void testRetrieveComments() throws IOException {
        user1 = tapglue.loginWithUsername(USER_1, PASSWORD);
        Post post = new Post(Post.Visibility.PUBLIC);
        post = tapglue.createPost(post);
        Map<String, String> contents = new HashMap<>();
        contents.put("en-US", "new content");
        Comment comment = tapglue.createComment(post.getId(), new Comment(contents));

        List<Comment> comments = tapglue.retrieveCommentsForPost(post.getId());
        assertThat(comments, hasItems(comment));
    }
}
