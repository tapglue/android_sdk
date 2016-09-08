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

package com.tapglue.android;

import android.app.Application;
import android.test.ApplicationTestCase;

import com.tapglue.android.entities.Like;
import com.tapglue.android.entities.Post;
import com.tapglue.android.entities.User;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsCollectionContaining.hasItems;
import static org.hamcrest.core.IsEqual.equalTo;

public class LikeIntegrationTest extends ApplicationTestCase<Application> {

    private static final String PASSWORD = "superSecretPassword";
    private static final String USER_1 = "user14";
    private static final String USER_2 = "user24";

    Configuration configuration;
    Tapglue tapglue;

    User user1 = new User(USER_1, PASSWORD);
    User user2 = new User(USER_2, PASSWORD);
    List<Post.Attachment> attachments = new ArrayList<>();

    public LikeIntegrationTest() {
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

    public void testCreateAndDeleteLike() throws Exception {
        user1 = tapglue.loginWithUsername(USER_1, PASSWORD);
        Post post = new Post(attachments, Post.Visibility.PUBLIC);
        post = tapglue.createPost(post);

        tapglue.createLike(post.getId());

        tapglue.deleteLike(post.getId());
    }

    public void testRetrieveLikes() throws Exception {
        user1 = tapglue.loginWithUsername(USER_1, PASSWORD);
        Post post = new Post(attachments, Post.Visibility.PUBLIC);
        post = tapglue.createPost(post);

        Like like = tapglue.createLike(post.getId());

        List<Like> likes = tapglue.retrieveLikesForPost(post.getId());

        assertThat(likes, hasItems(like));
    }

    public void testRetrieveLikesPopulatesUser() throws Exception {
        user1 = tapglue.loginWithUsername(USER_1, PASSWORD);
        Post post = new Post(attachments, Post.Visibility.PUBLIC);
        post = tapglue.createPost(post);

        tapglue.createLike(post.getId());

        List<Like> likes = tapglue.retrieveLikesForPost(post.getId());

        assertThat(likes.get(0).getUser(), equalTo(user1));
    }

    public void testRetrieveLikesByUser() throws Exception {
        user1 = tapglue.loginWithUsername(USER_1, PASSWORD);
        Post post = new Post(attachments, Post.Visibility.PUBLIC);
        post = tapglue.createPost(post);

        Like like = tapglue.createLike(post.getId());

        List<Like> likes = tapglue.retrieveLikesByUser(user1.getId());

        assertThat(likes, hasItems(like));
    }

    public void testRetrieveLikesByUserPopulatesUser() throws Exception {
        user1 = tapglue.loginWithUsername(USER_1, PASSWORD);
        Post post = new Post(attachments, Post.Visibility.PUBLIC);
        post = tapglue.createPost(post);

        tapglue.createLike(post.getId());

        List<Like> likes = tapglue.retrieveLikesByUser(user1.getId());

        assertThat(likes.get(0).getUser(), equalTo(user1));
    }

    public void testRetrieveLikesByUserPopulatesPost() throws Exception {
        user1 = tapglue.loginWithUsername(USER_1, PASSWORD);
        Post post = new Post(attachments, Post.Visibility.PUBLIC);
        post = tapglue.createPost(post);

        tapglue.createLike(post.getId());

        List<Like> likes = tapglue.retrieveLikesByUser(user1.getId());

        assertThat(likes.get(0).getPost(), equalTo(post));
    }
}
