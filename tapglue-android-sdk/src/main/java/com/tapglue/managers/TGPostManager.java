package com.tapglue.managers;

import com.tapglue.Tapglue;
import com.tapglue.model.TGComment;
import com.tapglue.model.TGCommentsList;
import com.tapglue.model.TGLike;
import com.tapglue.model.TGLikesList;
import com.tapglue.model.TGPost;
import com.tapglue.model.TGPostsList;
import com.tapglue.networking.requests.TGRequestCallback;
import com.tapglue.networking.requests.TGRequestErrorType;

/**
 * Created by adrian on 11.01.16.
 */
public class TGPostManager extends AbstractTGManager implements TGPostManagerInterface {

    public TGPostManager(Tapglue tgInstance) {
        super(tgInstance);
    }

    @Override
    public void createPost(TGPost post, TGRequestCallback<TGPost> returnMethod) {
        if (tapglue.getUserManager().getCurrentUser() == null) {
            returnMethod.onRequestError(new TGRequestErrorType(TGRequestErrorType.ErrorType.USER_NOT_LOGGED_IN));
            return;
        }
        tapglue.createRequest().createPost(post, returnMethod);
    }

    @Override
    public void getPost(Long postId, TGRequestCallback<TGPost> returnMethod) {
        if (tapglue.getUserManager().getCurrentUser() == null) {
            returnMethod.onRequestError(new TGRequestErrorType(TGRequestErrorType.ErrorType.USER_NOT_LOGGED_IN));
            return;
        }
        tapglue.createRequest().getPost(postId,returnMethod);
    }

    @Override
    public void updatePost(TGPost post, TGRequestCallback<TGPost> returnMethod) {
        if (tapglue.getUserManager().getCurrentUser() == null) {
            returnMethod.onRequestError(new TGRequestErrorType(TGRequestErrorType.ErrorType.USER_NOT_LOGGED_IN));
            return;
        }
        tapglue.createRequest().updatePost(post,returnMethod);
    }

    @Override
    public void removePost(Long postId, TGRequestCallback<Object> returnMethod) {
        if (tapglue.getUserManager().getCurrentUser() == null) {
            returnMethod.onRequestError(new TGRequestErrorType(TGRequestErrorType.ErrorType.USER_NOT_LOGGED_IN));
            return;
        }
        tapglue.createRequest().removePost(postId,returnMethod);
    }

    @Override
    public void getPosts(TGRequestCallback<TGPostsList> returnMethod) {
        if (tapglue.getUserManager().getCurrentUser() == null) {
            returnMethod.onRequestError(new TGRequestErrorType(TGRequestErrorType.ErrorType.USER_NOT_LOGGED_IN));
            return;
        }
        tapglue.createRequest().getPosts(returnMethod);
    }

    @Override
    public void getFeedPosts(TGRequestCallback<TGPostsList> returnMethod) {
        if (tapglue.getUserManager().getCurrentUser() == null) {
            returnMethod.onRequestError(new TGRequestErrorType(TGRequestErrorType.ErrorType.USER_NOT_LOGGED_IN));
            return;
        }
        tapglue.createRequest().getFeedPosts(returnMethod);
    }

    @Override
    public void getMyPosts(TGRequestCallback<TGPostsList> returnMethod) {
        if (tapglue.getUserManager().getCurrentUser() == null) {
            returnMethod.onRequestError(new TGRequestErrorType(TGRequestErrorType.ErrorType.USER_NOT_LOGGED_IN));
            return;
        }
        tapglue.createRequest().getMyPosts(returnMethod);
    }

    @Override
    public void getUserPosts(Long userId,TGRequestCallback<TGPostsList> returnMethod) {
        if (tapglue.getUserManager().getCurrentUser() == null) {
            returnMethod.onRequestError(new TGRequestErrorType(TGRequestErrorType.ErrorType.USER_NOT_LOGGED_IN));
            return;
        }
        tapglue.createRequest().getUserPosts(userId,returnMethod);
    }

    @Override
    public void createComment(Long postId, TGComment comment, TGRequestCallback<TGComment> returnMethod) {
        if (tapglue.getUserManager().getCurrentUser() == null) {
            returnMethod.onRequestError(new TGRequestErrorType(TGRequestErrorType.ErrorType.USER_NOT_LOGGED_IN));
            return;
        }
        tapglue.createRequest().createPostComment(comment,postId,returnMethod);
    }

    @Override
    public void getPostComments(Long postId, TGRequestCallback<TGCommentsList> returnMethod) {
        if (tapglue.getUserManager().getCurrentUser() == null) {
            returnMethod.onRequestError(new TGRequestErrorType(TGRequestErrorType.ErrorType.USER_NOT_LOGGED_IN));
            return;
        }
        tapglue.createRequest().getPostComments(postId,returnMethod);
    }

    @Override
    public void updatePostComment(Long postid, TGComment comment, TGRequestCallback<TGComment> returnMethod) {
        if (tapglue.getUserManager().getCurrentUser() == null) {
            returnMethod.onRequestError(new TGRequestErrorType(TGRequestErrorType.ErrorType.USER_NOT_LOGGED_IN));
            return;
        }
        tapglue.createRequest().updatePostComments(comment.setPostId(postid),returnMethod);
    }

    @Override
    public void removePostComment(Long postId, Long commentId, TGRequestCallback<Object> returnMethod) {
        if (tapglue.getUserManager().getCurrentUser() == null) {
            returnMethod.onRequestError(new TGRequestErrorType(TGRequestErrorType.ErrorType.USER_NOT_LOGGED_IN));
            return;
        }
        tapglue.createRequest().removePostComments(postId,commentId,returnMethod);
    }

    @Override
    public void getPostLikes(Long postId, TGRequestCallback<TGLikesList> returnMethod) {
        if (tapglue.getUserManager().getCurrentUser() == null) {
            returnMethod.onRequestError(new TGRequestErrorType(TGRequestErrorType.ErrorType.USER_NOT_LOGGED_IN));
            return;
        }
        tapglue.createRequest().getPostLikes(postId,returnMethod);
    }

    @Override
    public void likePost(Long postId, TGRequestCallback<TGLike> returnMethod) {
        if (tapglue.getUserManager().getCurrentUser() == null) {
            returnMethod.onRequestError(new TGRequestErrorType(TGRequestErrorType.ErrorType.USER_NOT_LOGGED_IN));
            return;
        }
        tapglue.createRequest().likePost(postId,returnMethod);
    }

    @Override
    public void unlikePost(long postId, TGRequestCallback<Object> returnMethod) {
        if (tapglue.getUserManager().getCurrentUser() == null) {
            returnMethod.onRequestError(new TGRequestErrorType(TGRequestErrorType.ErrorType.USER_NOT_LOGGED_IN));
            return;
        }
        tapglue.createRequest().unlikePost(postId,returnMethod);
    }
}
