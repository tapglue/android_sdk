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
    public void createComment(String postid, TGComment comment, TGRequestCallback<TGComment> returnMethod) {
        if (tapglue.getUserManager().getCurrentUser() == null) {
            returnMethod.onRequestError(new TGRequestErrorType(TGRequestErrorType.ErrorType.USER_NOT_LOGGED_IN));
            return;
        }
        tapglue.createRequest().createPostComment(comment, postid, returnMethod);
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
    public void getPost(String postid, TGRequestCallback<TGPost> returnMethod) {
        if (tapglue.getUserManager().getCurrentUser() == null) {
            returnMethod.onRequestError(new TGRequestErrorType(TGRequestErrorType.ErrorType.USER_NOT_LOGGED_IN));
            return;
        }
        tapglue.createRequest().getPost(postid, returnMethod);
    }

    @Override
    public void getPostComments(String postid, TGRequestCallback<TGCommentsList> returnMethod) {
        if (tapglue.getUserManager().getCurrentUser() == null) {
            returnMethod.onRequestError(new TGRequestErrorType(TGRequestErrorType.ErrorType.USER_NOT_LOGGED_IN));
            return;
        }
        tapglue.createRequest().getPostComments(postid, returnMethod);
    }

    @Override
    public void getPostLikes(String postid, TGRequestCallback<TGLikesList> returnMethod) {
        if (tapglue.getUserManager().getCurrentUser() == null) {
            returnMethod.onRequestError(new TGRequestErrorType(TGRequestErrorType.ErrorType.USER_NOT_LOGGED_IN));
            return;
        }
        tapglue.createRequest().getPostLikes(postid, returnMethod);
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
    public void getUserPosts(Long userId, TGRequestCallback<TGPostsList> returnMethod) {
        if (tapglue.getUserManager().getCurrentUser() == null) {
            returnMethod.onRequestError(new TGRequestErrorType(TGRequestErrorType.ErrorType.USER_NOT_LOGGED_IN));
            return;
        }
        tapglue.createRequest().getUserPosts(userId, returnMethod);
    }

    @Override
    public void likePost(String postid, TGRequestCallback<TGLike> returnMethod) {
        if (tapglue.getUserManager().getCurrentUser() == null) {
            returnMethod.onRequestError(new TGRequestErrorType(TGRequestErrorType.ErrorType.USER_NOT_LOGGED_IN));
            return;
        }
        tapglue.createRequest().likePost(postid, returnMethod);
    }

    @Override
    public void removePost(String postid, TGRequestCallback<Object> returnMethod) {
        if (tapglue.getUserManager().getCurrentUser() == null) {
            returnMethod.onRequestError(new TGRequestErrorType(TGRequestErrorType.ErrorType.USER_NOT_LOGGED_IN));
            return;
        }
        tapglue.createRequest().removePost(postid, returnMethod);
    }

    @Override
    public void removePostComment(String postid, Long commentId, TGRequestCallback<Object> returnMethod) {
        if (tapglue.getUserManager().getCurrentUser() == null) {
            returnMethod.onRequestError(new TGRequestErrorType(TGRequestErrorType.ErrorType.USER_NOT_LOGGED_IN));
            return;
        }
        tapglue.createRequest().removePostComments(postid, commentId, returnMethod);
    }

    @Override
    public void unlikePost(String postid, TGRequestCallback<Object> returnMethod) {
        if (tapglue.getUserManager().getCurrentUser() == null) {
            returnMethod.onRequestError(new TGRequestErrorType(TGRequestErrorType.ErrorType.USER_NOT_LOGGED_IN));
            return;
        }
        tapglue.createRequest().unlikePost(postid, returnMethod);
    }

    @Override
    public void updatePost(TGPost post, TGRequestCallback<TGPost> returnMethod) {
        if (tapglue.getUserManager().getCurrentUser() == null) {
            returnMethod.onRequestError(new TGRequestErrorType(TGRequestErrorType.ErrorType.USER_NOT_LOGGED_IN));
            return;
        }
        tapglue.createRequest().updatePost(post, returnMethod);
    }

    @Override
    public void updatePostComment(String postid, TGComment comment, TGRequestCallback<TGComment> returnMethod) {
        if (tapglue.getUserManager().getCurrentUser() == null) {
            returnMethod.onRequestError(new TGRequestErrorType(TGRequestErrorType.ErrorType.USER_NOT_LOGGED_IN));
            return;
        }
        tapglue.createRequest().updatePostComments(comment.setPostId(postid), returnMethod);
    }
}
