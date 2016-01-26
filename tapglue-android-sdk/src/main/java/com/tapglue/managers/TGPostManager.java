/*
 * Copyright (c) 2015 Tapglue (https://www.tapglue.com/). All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
package com.tapglue.managers;

import android.support.annotation.NonNull;

import com.tapglue.Tapglue;
import com.tapglue.model.TGComment;
import com.tapglue.model.TGCommentsList;
import com.tapglue.model.TGLike;
import com.tapglue.model.TGLikesList;
import com.tapglue.model.TGPost;
import com.tapglue.model.TGPostsList;
import com.tapglue.networking.requests.TGRequestCallback;
import com.tapglue.networking.requests.TGRequestErrorType;

public class TGPostManager extends AbstractTGManager implements TGPostManagerInterface {

    public TGPostManager(Tapglue tgInstance) {
        super(tgInstance);
    }

    @Override
    public void createComment(@NonNull String postid, @NonNull TGComment comment, @NonNull TGRequestCallback<TGComment> callback) {
        if (tapglue.getUserManager().getCurrentUser() == null) {
            callback.onRequestError(new TGRequestErrorType(TGRequestErrorType.ErrorType.USER_NOT_LOGGED_IN));
            return;
        }
        tapglue.createRequest().createPostComment(comment, postid, callback);
    }

    @Override
    public void createPost(@NonNull TGPost post, @NonNull TGRequestCallback<TGPost> callback) {
        if (tapglue.getUserManager().getCurrentUser() == null) {
            callback.onRequestError(new TGRequestErrorType(TGRequestErrorType.ErrorType.USER_NOT_LOGGED_IN));
            return;
        }
        tapglue.createRequest().createPost(post, callback);
    }

    @Override
    public void getFeedPosts(@NonNull TGRequestCallback<TGPostsList> callback) {
        if (tapglue.getUserManager().getCurrentUser() == null) {
            callback.onRequestError(new TGRequestErrorType(TGRequestErrorType.ErrorType.USER_NOT_LOGGED_IN));
            return;
        }
        tapglue.createRequest().getFeedPosts(callback);
    }

    @Override
    public void getMyPosts(@NonNull TGRequestCallback<TGPostsList> callback) {
        if (tapglue.getUserManager().getCurrentUser() == null) {
            callback.onRequestError(new TGRequestErrorType(TGRequestErrorType.ErrorType.USER_NOT_LOGGED_IN));
            return;
        }
        tapglue.createRequest().getMyPosts(callback);
    }

    @Override
    public void getPost(@NonNull String postid, @NonNull TGRequestCallback<TGPost> callback) {
        if (tapglue.getUserManager().getCurrentUser() == null) {
            callback.onRequestError(new TGRequestErrorType(TGRequestErrorType.ErrorType.USER_NOT_LOGGED_IN));
            return;
        }
        tapglue.createRequest().getPost(postid, callback);
    }

    @Override
    public void getPostComments(@NonNull String postid, @NonNull TGRequestCallback<TGCommentsList> callback) {
        if (tapglue.getUserManager().getCurrentUser() == null) {
            callback.onRequestError(new TGRequestErrorType(TGRequestErrorType.ErrorType.USER_NOT_LOGGED_IN));
            return;
        }
        tapglue.createRequest().getPostComments(postid, callback);
    }

    @Override
    public void getPostLikes(@NonNull String postid, @NonNull TGRequestCallback<TGLikesList> callback) {
        if (tapglue.getUserManager().getCurrentUser() == null) {
            callback.onRequestError(new TGRequestErrorType(TGRequestErrorType.ErrorType.USER_NOT_LOGGED_IN));
            return;
        }
        tapglue.createRequest().getPostLikes(postid, callback);
    }

    @Override
    public void getPosts(@NonNull TGRequestCallback<TGPostsList> callback) {
        if (tapglue.getUserManager().getCurrentUser() == null) {
            callback.onRequestError(new TGRequestErrorType(TGRequestErrorType.ErrorType.USER_NOT_LOGGED_IN));
            return;
        }
        tapglue.createRequest().getPosts(callback);
    }

    @Override
    public void getUserPosts(@NonNull Long userId, @NonNull TGRequestCallback<TGPostsList> callback) {
        if (tapglue.getUserManager().getCurrentUser() == null) {
            callback.onRequestError(new TGRequestErrorType(TGRequestErrorType.ErrorType.USER_NOT_LOGGED_IN));
            return;
        }
        tapglue.createRequest().getUserPosts(userId, callback);
    }

    @Override
    public void likePost(@NonNull String postid, @NonNull TGRequestCallback<TGLike> callback) {
        if (tapglue.getUserManager().getCurrentUser() == null) {
            callback.onRequestError(new TGRequestErrorType(TGRequestErrorType.ErrorType.USER_NOT_LOGGED_IN));
            return;
        }
        tapglue.createRequest().likePost(postid, callback);
    }

    @Override
    public void removePost(@NonNull String postid, @NonNull TGRequestCallback<Object> callback) {
        if (tapglue.getUserManager().getCurrentUser() == null) {
            callback.onRequestError(new TGRequestErrorType(TGRequestErrorType.ErrorType.USER_NOT_LOGGED_IN));
            return;
        }
        tapglue.createRequest().removePost(postid, callback);
    }

    @Override
    public void removePostComment(@NonNull String postid, @NonNull Long commentId, @NonNull TGRequestCallback<Object> callback) {
        if (tapglue.getUserManager().getCurrentUser() == null) {
            callback.onRequestError(new TGRequestErrorType(TGRequestErrorType.ErrorType.USER_NOT_LOGGED_IN));
            return;
        }
        tapglue.createRequest().removePostComments(postid, commentId, callback);
    }

    @Override
    public void unlikePost(@NonNull String postid, @NonNull TGRequestCallback<Object> callback) {
        if (tapglue.getUserManager().getCurrentUser() == null) {
            callback.onRequestError(new TGRequestErrorType(TGRequestErrorType.ErrorType.USER_NOT_LOGGED_IN));
            return;
        }
        tapglue.createRequest().unlikePost(postid, callback);
    }

    @Override
    public void updatePost(@NonNull TGPost post, @NonNull TGRequestCallback<TGPost> callback) {
        if (tapglue.getUserManager().getCurrentUser() == null) {
            callback.onRequestError(new TGRequestErrorType(TGRequestErrorType.ErrorType.USER_NOT_LOGGED_IN));
            return;
        }
        tapglue.createRequest().updatePost(post, callback);
    }

    @Override
    public void updatePostComment(String postid, @NonNull TGComment comment, @NonNull TGRequestCallback<TGComment> callback) {
        if (tapglue.getUserManager().getCurrentUser() == null) {
            callback.onRequestError(new TGRequestErrorType(TGRequestErrorType.ErrorType.USER_NOT_LOGGED_IN));
            return;
        }
        tapglue.createRequest().updatePostComments(comment.setPostId(postid), callback);
    }
}
