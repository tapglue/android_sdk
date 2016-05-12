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

package com.tapglue.networking;

import android.support.annotation.NonNull;

import com.tapglue.model.TGComment;
import com.tapglue.model.TGCommentsList;
import com.tapglue.model.TGConnection;
import com.tapglue.model.TGEvent;
import com.tapglue.model.TGEventsList;
import com.tapglue.model.TGFeed;
import com.tapglue.model.TGFeedCount;
import com.tapglue.model.TGLike;
import com.tapglue.model.TGLikesList;
import com.tapglue.model.TGLoginUser;
import com.tapglue.model.TGPendingConnections;
import com.tapglue.model.TGPost;
import com.tapglue.model.TGPostsList;
import com.tapglue.model.TGSocialConnections;
import com.tapglue.model.TGUser;
import com.tapglue.model.TGUsersList;

import java.util.List;

import retrofit.Call;
import retrofit.http.Body;
import retrofit.http.DELETE;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.PUT;
import retrofit.http.Path;
import retrofit.http.Query;

interface TGApi {

    @NonNull
    @POST("posts/{id}/comments")
    Call<TGComment> createComment(@Path("id") String postId, @Body TGComment post);

    @NonNull
    @PUT("me/connections")
    Call<TGConnection> createConnection(@Body TGConnection userAndType);

    @NonNull
    @POST("me/events")
    Call<TGEvent> createEvent(@Body TGEvent event);

    @NonNull
    @POST("posts")
    Call<TGPost> createPost(@Body TGPost post);

    @NonNull
    @POST("users")
    Call<TGUser> createUser(@Body TGUser user);

    @NonNull
    @DELETE("me")
    Call<Object> deleteUser();

    @NonNull
    @GET("posts/{id}/comments")
    Call<TGCommentsList> getCommentsForPost(@Path("id") String postId);

    @NonNull
    @GET("me/connections/confirmed")
    Call<TGPendingConnections> getConfirmedConnections();

    @NonNull
    @GET("me/events/{id}")
    Call<TGEvent> getEvent(@Path("id") Long id);

    @NonNull
    @GET("users/{userId}/events/{id}")
    Call<TGEvent> getEvent(@Path("userId") Long userId, @Path("id") Long id);

    @NonNull
    @GET("me/events")
    Call<TGEventsList> getEvents();

    @NonNull
    @GET("me/events")
    Call<TGEventsList> getEvents(@Query("where") String query);

    @NonNull
    @GET("users/{userId}/events")
    Call<TGEventsList> getEvents(@Path("userId") Long userId);

    @NonNull
    @GET("users/{userId}/events")
    Call<TGEventsList> getEvents(@Path("userId") Long userId, @Query("where") String query);

    @NonNull
    @GET("me/feed")
    Call<TGFeed> getFeed();

    @NonNull
    @GET("me/feed")
    Call<TGFeed> getFeed(@Query("where") String query);

    @NonNull
    @GET("me/feed/posts")
    Call<TGPostsList> getFeedPosts();

    @NonNull
    @GET("me/followers")
    Call<TGUsersList> getFollowed();

    @NonNull
    @GET("users/{id}/followed")
    Call<TGUsersList> getFollowedForUser(@Path("id") Long userId);

    @NonNull
    @GET("me/follows")
    Call<TGUsersList> getFollows();

    @NonNull
    @GET("users/{id}/follows")
    Call<TGUsersList> getFollowsForUser(@Path("id") Long userId);

    @NonNull
    @GET("me/friends")
    Call<TGUsersList> getFriends();

    @NonNull
    @GET("users/{id}/friends")
    Call<TGUsersList> getFriendsForUser(@Path("id") Long userId);

    @NonNull
    @GET("me/posts")
    Call<TGPostsList> getMyPosts();

    @NonNull
    @GET("me/connections/pending")
    Call<TGPendingConnections> getPendingConnections();

    @NonNull
    @GET("posts/{id}")
    Call<TGPost> getPost(@Path("id") String postId);

    @NonNull
    @GET("posts/{id}/comments/{commentid}")
    Call<TGComment> getPostComment(@Path("id") String postId, @Path("commentid") Long commentId);

    @NonNull
    @GET("posts/{id}/likes")
    Call<TGLikesList> getPostLikes(@Path("id") String postId);

    @NonNull
    @GET("posts")
    Call<TGPostsList> getPosts();

    @NonNull
    @GET("me/connections/rejected")
    Call<TGPendingConnections> getRejectedConnections();

    @NonNull
    @GET("me/feed/unread")
    Call<TGEventsList> getUnreadFeed();

    @NonNull
    @GET("me/feed/unread")
    Call<TGEventsList> getUnreadFeed(@Query("where") String query);

    @NonNull
    @GET("me/feed/unread/count")
    Call<TGFeedCount> getUnreadFeedCount();

    @NonNull
    @GET("users/{id}")
    Call<TGUser> getUser(@Path("id") Long id);

    @NonNull
    @GET("users/{id}/posts")
    Call<TGPostsList> getUserPosts(@Path("id") Long userId);

    @NonNull
    @POST("posts/{id}/likes")
    Call<TGLike> likePost(@Path("id") String postId);

    @NonNull
    @POST("me/login")
    Call<TGUser> login(@Body TGLoginUser user);

    @NonNull
    @DELETE("me/logout")
    Call<Object> logout();

    @NonNull
    @GET("recommendations/users/{type}/{period}")
    Call<TGUsersList> getRecommendedUsers(@Path("type") String type, @Path("period") String period);

    @NonNull
    @DELETE("me/connections/{type}/{id}")
    Call<Object> removeConnection(@Path("id") Long userTo, @Path("type") String type);

    @NonNull
    @DELETE("me/events/{id}")
    Call<Object> removeEvent(@Path("id") Long id);

    @NonNull
    @DELETE("posts/{id}")
    Call<Object> removePost(@Path("id") String postId);

    @NonNull
    @DELETE("posts/{id}/comments/{commentid}")
    Call<Object> removePostComment(@Path("id") String postId, @Path("commentid") Long commentId);

    @NonNull
    @GET("users/search")
    Call<TGUsersList> search(@Query("q") String criteria);

    @NonNull
    @GET("users/search")
    Call<TGUsersList> searchWithEmails(@Query("email") List<String> criteria);

    @NonNull
    @GET("users/search")
    Call<TGUsersList> searchWithSocialIds(@Query("socialid") List<String> ids, @Query("social_platform") String platforms);

    @NonNull
    @POST("/analytics")
    Call<Object> sendAnalytics();

    @NonNull
    @POST("me/connections/social")
    Call<TGUsersList> socialConnections(@Body TGSocialConnections connections);

    @NonNull
    @DELETE("posts/{id}/likes")
    Call<Object> unlikePost(@Path("id") String postId);

    @NonNull
    @PUT("me/events/{id}")
    Call<TGEvent> updateEvent(@Path("id") Long id, @Body TGEvent event);

    @NonNull
    @PUT("posts/{id}")
    Call<TGPost> updatePost(@Path("id") String postId, @Body TGPost data);

    @NonNull
    @PUT("posts/{id}/comments/{commentid}")
    Call<TGComment> updatePostComment(@Path("id") String postId, @Path("commentid") Long commentId, @Body TGComment data);

    @NonNull
    @PUT("me")
    Call<TGUser> updateUser(@Body TGUser user);
}
