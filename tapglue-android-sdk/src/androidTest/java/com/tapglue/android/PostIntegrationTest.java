package com.tapglue.android;

import android.app.Application;
import android.test.ApplicationTestCase;

import com.tapglue.android.entities.Post;
import com.tapglue.android.entities.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.tapglue.android.entities.Post.Visibility;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsCollectionContaining.hasItems;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.IsNull.notNullValue;

public class PostIntegrationTest extends ApplicationTestCase<Application> {

    private static final String PASSWORD = "superSecretPassword";
    private static final String USER_1 = "user11";
    private static final String USER_2 = "user21";

    Configuration configuration;
    Tapglue tapglue;

    User user1 = new User(USER_1, PASSWORD);
    User user2 = new User(USER_2, PASSWORD);
    List<Post.Attachment> attachments = new ArrayList<>();


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
        Post post = new Post(attachments, Visibility.PRIVATE);
        post = tapglue.createPost(post);

        tapglue.deletePost(post.getId());
    }

    public void testRetrievePost() throws Exception {
        tapglue.loginWithUsername(USER_1, PASSWORD);
        Post post = new Post(attachments, Visibility.PUBLIC);
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
        Post post = new Post(attachments, Visibility.PRIVATE);
        post = tapglue.createPost(post);

        Post secondPost = new Post(attachments, Visibility.PUBLIC);

        Post updatedPost = tapglue.updatePost(post.getId(), secondPost);

        assertThat(updatedPost.getVisibility(), equalTo(Visibility.PUBLIC));
    }

    public void testRetrievePostsRetrievesPostsPage() throws Exception {
        RxTapglue rxTapglue = new RxTapglue(configuration, getContext());
        rxTapglue.loginWithUsername(USER_1, PASSWORD).toBlocking().first();
        Post post = new Post(attachments, Visibility.PUBLIC);
        post = rxTapglue.createPost(post).toBlocking().first();
        List<Post> posts = rxTapglue.retrievePosts().toBlocking().first().getData();

        assertThat(posts, hasItems(post));
    }

    public void testRetrievePostsPopulatesUserPage() throws Exception {
        RxTapglue rxTapglue = new RxTapglue(configuration, getContext());
        rxTapglue.loginWithUsername(USER_1, PASSWORD).toBlocking().first();
        Post post = new Post(attachments, Visibility.PUBLIC);
        post = rxTapglue.createPost(post).toBlocking().first();
        List<Post> posts = rxTapglue.retrievePosts().toBlocking().first().getData();
        Post result = null;
        for(Post p: posts) {
            if(p.getId().equals(post.getId())) {
                result = p;
            }
        }

        assertThat(result.getUser(), notNullValue());
    }

    public void testRetrievePostsByUser() throws Exception {
        user1 = tapglue.loginWithUsername(USER_1, PASSWORD);
        Post post = new Post(attachments, Visibility.PUBLIC);
        post = tapglue.createPost(post);
        String user1Id = user1.getId();

        tapglue.loginWithUsername(USER_2, PASSWORD);
        List<Post> posts = tapglue.retrievePostsByUser(user1Id);

        assertThat(posts, hasItems(post));
    }

    public void testAttachmentsRetrieved() throws Exception {
        user1 = tapglue.loginWithUsername(USER_1, PASSWORD);
        Map<String, String> contents = new HashMap<>();
        contents.put("en-US", "contents");
        Post.Attachment attachment = new Post.Attachment(contents, Post.Attachment.Type.TEXT,
                "myAttachment");
        attachments.add(attachment);
        Post post = tapglue.createPost(new Post(attachments, Visibility.PUBLIC));

        assertThat(post.getAttachments().size(), equalTo(1));
        Map<String, String> resultContent = post.getAttachments().get(0).getContents();
        assertThat(resultContent.get("en-US"), equalTo("contents"));
    }
}
