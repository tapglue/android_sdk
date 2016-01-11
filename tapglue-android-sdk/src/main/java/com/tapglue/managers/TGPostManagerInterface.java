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

import com.tapglue.model.TGComment;
import com.tapglue.model.TGCommentsList;
import com.tapglue.model.TGLike;
import com.tapglue.model.TGLikesList;
import com.tapglue.model.TGPost;
import com.tapglue.model.TGPostsList;
import com.tapglue.networking.requests.TGRequestCallback;

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
