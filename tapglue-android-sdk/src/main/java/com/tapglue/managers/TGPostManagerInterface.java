package com.tapglue.managers;

import com.tapglue.model.TGComment;
import com.tapglue.model.TGCommentsList;
import com.tapglue.model.TGLike;
import com.tapglue.model.TGLikesList;
import com.tapglue.model.TGPost;
import com.tapglue.model.TGPostsList;
import com.tapglue.networking.requests.TGRequestCallback;

/**
 * Created by adrian on 11.01.16.
 */
public interface TGPostManagerInterface {
    void createPost(TGPost post, TGRequestCallback<TGPost> returnMethod);

    void getPost(Long postId, TGRequestCallback<TGPost> returnMethod);

    void updatePost(TGPost post, TGRequestCallback<TGPost> returnMethod);

    void removePost(Long postId, TGRequestCallback<Object> returnMethod);

    void getPosts(TGRequestCallback<TGPostsList> returnMethod);

    void getFeedPosts(TGRequestCallback<TGPostsList> returnMethod);

    void getMyPosts(TGRequestCallback<TGPostsList> returnMethod);

    void getUserPosts(Long userId,TGRequestCallback<TGPostsList> returnMethod);

    void createComment(Long postId,TGComment comment,TGRequestCallback<TGComment> returnMethod);

    void getPostComments(Long postId,TGRequestCallback<TGCommentsList> returnMethod);

    void updatePostComment(Long postid,TGComment comment,TGRequestCallback<TGComment> returnMethod);

    void removePostComment(Long postId,Long commentId,TGRequestCallback<Object> returnMethod);

    void getPostLikes(Long postId,TGRequestCallback<TGLikesList> returnMethod);

    void likePost(Long postId,TGRequestCallback<TGLike> returnMethod);

    void unlikePost(long postId,TGRequestCallback<Object> returnMethod);
}
