/**
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
package com.tapglue.android.http;

import com.tapglue.android.entities.User;
import com.tapglue.android.entities.Comment;
import com.tapglue.android.entities.Connection;
import com.tapglue.android.entities.Connection.Type;
import com.tapglue.android.entities.Like;
import com.tapglue.android.entities.Post;
import com.tapglue.android.http.payloads.EmailLoginPayload;
import com.tapglue.android.http.payloads.EmailSearchPayload;
import com.tapglue.android.http.payloads.SocialConnections;
import com.tapglue.android.http.payloads.SocialSearchPayload;
import com.tapglue.android.http.payloads.UsernameLoginPayload;

import retrofit2.http.Body;
import retrofit2.http.Path;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Query;
import rx.Observable;

interface TapglueService {
    @POST("/0.4/users/login")
    Observable<User> login(@Body UsernameLoginPayload payload);

    @POST("/0.4/users/login")
    Observable<User> login(@Body EmailLoginPayload payload);

    @DELETE("/0.4/me/logout")
    Observable<Void> logout();

    @POST("/0.4/analytics")
    Observable<Void> sendAnalytics();

    @POST("/0.4/users")
    Observable<User> createUser(@Body User user);

    @DELETE("/0.4/me")
    Observable<Void> deleteCurrentUser();

    @PUT("/0.4/me")
    Observable<User> updateCurrentUser(@Body User user);

    @GET("/0.4/me")
    Observable<User> refreshCurrentUser();

    @GET("/0.4/users/{userId}")
    Observable<User> retrieveUser(@Path("userId") String id);

    @GET("/0.4/me/follows")
    Observable<UsersFeed> retrieveFollowings();

    @GET("/0.4/me/followers")
    Observable<UsersFeed> retrieveFollowers();

    @GET("/0.4/users/{id}/follows")
    Observable<UsersFeed> retrieveUserFollowings(@Path("id") String id);

    @GET("/0.4/users/{id}/followers")
    Observable<UsersFeed> retrieveUserFollowers(@Path("id") String id);

    @GET("/0.4/me/friends")
    Observable<UsersFeed> retrieveFriends();

    @GET("/0.4/users/{id}/friends")
    Observable<UsersFeed> retrieveUserFriends(@Path("id") String id);

    @PUT("/0.4/me/connections")
    Observable<Connection> createConnection(@Body Connection connection);

    @POST("/0.4/me/connections/social")
    Observable<UsersFeed> createSocialConnections(@Body SocialConnections connections);

    @DELETE("/0.4/me/connections/{type}/{id}")
    Observable<Void> deleteConnection(@Path("id") String userId, 
                                      @Path("type") Type type);

    @GET("/0.4/me/connections/pending")
    Observable<ConnectionsFeed> retrievePendingConnections();

    @GET("/0.4/me/connections/rejected")
    Observable<ConnectionsFeed> retrieveRejectedConnections();

    @GET("/0.4/users/search")
    Observable<UsersFeed> searchUsers(@Query("q") String searchTerm);

    @POST("/0.4/users/search/emails")
    Observable<UsersFeed> searchUsersByEmail(@Body EmailSearchPayload payload);

    @POST("/0.4/users/search/{platform}")
    Observable<UsersFeed> searchUsersBySocialIds(@Path("platform") String platform,
                                                 @Body SocialSearchPayload payload);

    @POST("/0.4/posts")
    Observable<Post> createPost(@Body Post post);

    @GET("/0.4/posts/{id}")
    Observable<Post> retrievePost(@Path("id") String id);

    @PUT("/0.4/posts/{id}")
    Observable<Post> updatePost(@Path("id") String id, @Body Post post);

    @DELETE("/0.4/posts/{id}")
    Observable<Void> deletePost(@Path("id") String id);

    @GET("/0.4/posts")
    Observable<PostListFeed> retrievePosts();

    @GET("/0.4/users/{id}/posts")
    Observable<PostListFeed> retrievePostsByUser(@Path("id") String id);

    @POST("/0.4/posts/{id}/likes")
    Observable<Like> createLike(@Path("id") String postId);

    @DELETE("/0.4/posts/{id}/likes")
    Observable<Void> deleteLike(@Path("id") String postId);

    @GET("/0.4/posts/{id}/likes")
    Observable<LikesFeed> retrieveLikesForPost(@Path("id") String postId);

    @POST("/0.4/posts/{id}/comments")
    Observable<Comment> createComment(@Path("id") String postId,
                                      @Body Comment comment);

    @DELETE("/0.4/posts/{postId}/comments/{commentId}")
    Observable<Void> deleteComment(@Path("postId") String postId,
                                   @Path("commentId") String commentId);

    @PUT("/0.4/posts/{postId}/comments/{commentId}")
    Observable<Comment> updateComment(@Path("postId") String postId,
                                      @Path("commentId") String commentId,
                                      @Body Comment comment);

    @GET("/0.4/posts/{id}/comments")
    Observable<CommentsFeed> retrieveCommentsForPost(@Path("id") String postId);

    @GET("/0.4/me/feed/posts")
    Observable<PostListFeed> retrievePostFeed();

    @GET("/0.4/me/feed/events")
    Observable<EventListFeed> retrieveEventFeed();

    @GET("/0.4/me/feed")
    Observable<RawNewsFeed> retrieveNewsFeed();
}
