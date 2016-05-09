package com.tapglue.sdk;

import android.app.Application;
import android.test.ApplicationTestCase;

import com.tapglue.sdk.entities.Post;
import com.tapglue.sdk.entities.User;

import java.util.List;

import static com.tapglue.sdk.entities.Post.Visibility;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsCollectionContaining.hasItems;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.IsNull.notNullValue;

public class PostIntegrationTest extends ApplicationTestCase<Application> {

    private static final String PASSWORD = "superSecretPassword";
    private static final String USER_1 = "user1";
    private static final String USER_2 = "user2";

    Configuration configuration;
    Tapglue tapglue;

    User user1 = new User(USER_1, PASSWORD);
    User user2 = new User(USER_2, PASSWORD);


    public PostIntegrationTest() {
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

    public void testCreateAndDeletePost() throws Exception {
        tapglue.loginWithUsername(USER_1, PASSWORD);
        Post post = new Post(Visibility.PRIVATE);
        post = tapglue.createPost(post);

        tapglue.deletePost(post.getId());
    }

    public void testRetrievePost() throws Exception {
        tapglue.loginWithUsername(USER_1, PASSWORD);
        Post post = new Post(Visibility.PUBLIC);
        post = tapglue.createPost(post);

        String id = post.getId();

        tapglue.loginWithUsername(USER_2, PASSWORD);
        Post retrievedPost = tapglue.retrievePost(id);
        assertThat(retrievedPost, equalTo(post));

        tapglue.loginWithUsername(USER_1, PASSWORD);
        tapglue.deletePost(id);
    }

    public void testUpdatePost() throws Exception {
        tapglue.loginWithUsername(USER_1, PASSWORD);
        Post post = new Post(Visibility.PRIVATE);
        post = tapglue.createPost(post);

        Post secondPost = new Post(Visibility.PUBLIC);

        Post updatedPost = tapglue.updatePost(post.getId(), secondPost);

        assertThat(updatedPost.getVisibility(), equalTo(Visibility.PUBLIC));
    }

    public void testRetrievePostsRetrievesPosts() throws Exception {
        tapglue.loginWithUsername(USER_1, PASSWORD);
        Post post = new Post(Visibility.PUBLIC);
        post = tapglue.createPost(post);
        List<Post> posts = tapglue.retrievePosts();

        assertThat(posts, hasItems(post));
    }

    public void testRetrievePostsPopulatesUser() throws Exception {
        tapglue.loginWithUsername(USER_1, PASSWORD);
        Post post = new Post(Visibility.PUBLIC);
        post = tapglue.createPost(post);
        List<Post> posts = tapglue.retrievePosts();

        assertThat(posts.get(0).getUser(), notNullValue());
    }

    public void testRetrievePostsByUser() throws Exception {
        user1 = tapglue.loginWithUsername(USER_1, PASSWORD);
        Post post = new Post(Visibility.PUBLIC);
        post = tapglue.createPost(post);
        String user1Id = user1.getId();

        tapglue.loginWithUsername(USER_2, PASSWORD);
        List<Post> posts = tapglue.retrievePostsByUser(user1Id);

        assertThat(posts, hasItems(post));
    }
}
