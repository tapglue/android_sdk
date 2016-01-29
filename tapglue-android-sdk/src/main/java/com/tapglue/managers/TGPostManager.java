/*
 * Copyright (c) 2015-2016 Tapglue (https://www.tapglue.com/). All rights reserved.
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

import com.tapglue.model.TGComment;
import com.tapglue.model.TGCommentsList;
import com.tapglue.model.TGLike;
import com.tapglue.model.TGLikesList;
import com.tapglue.model.TGPost;
import com.tapglue.model.TGPostsList;
import com.tapglue.networking.requests.TGRequestCallback;

public interface TGPostManager {
    void createComment(@NonNull String postid, @NonNull TGComment comment, @NonNull final TGRequestCallback<TGComment> callback);

    void createPost(@NonNull TGPost post, @NonNull final TGRequestCallback<TGPost> callback);

    void getFeedPosts(@NonNull final TGRequestCallback<TGPostsList> callback);

    void getMyPosts(@NonNull final TGRequestCallback<TGPostsList> callback);

    void getPost(@NonNull String postid, @NonNull final TGRequestCallback<TGPost> callback);

    void getPostComments(@NonNull String postid, @NonNull final TGRequestCallback<TGCommentsList> callback);

    void getPostLikes(@NonNull String postid, @NonNull final TGRequestCallback<TGLikesList> callback);

    void getPosts(@NonNull final TGRequestCallback<TGPostsList> callback);

    void getUserPosts(@NonNull Long userId, @NonNull final TGRequestCallback<TGPostsList> callback);

    void likePost(@NonNull String postid, @NonNull final TGRequestCallback<TGLike> callback);

    void removePost(@NonNull String postid, @NonNull final TGRequestCallback<Object> callback);

    void removePostComment(@NonNull String postid, @NonNull Long commentId, @NonNull final TGRequestCallback<Object> callback);

    void unlikePost(@NonNull String postid, @NonNull final TGRequestCallback<Object> callback);

    void updatePost(@NonNull TGPost post, @NonNull final TGRequestCallback<TGPost> callback);

    void updatePostComment(@NonNull String postid, @NonNull TGComment comment, @NonNull final TGRequestCallback<TGComment> callback);
}
