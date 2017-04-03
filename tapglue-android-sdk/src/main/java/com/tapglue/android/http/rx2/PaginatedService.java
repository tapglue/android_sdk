package com.tapglue.android.http.rx2;

import com.tapglue.android.http.CommentsFeed;
import com.tapglue.android.http.ConnectionsFeed;
import com.tapglue.android.http.EventListFeed;
import com.tapglue.android.http.LikesFeed;
import com.tapglue.android.http.PostListFeed;
import com.tapglue.android.http.RawNewsFeed;
import com.tapglue.android.http.UsersFeed;
import com.tapglue.android.http.payloads.EmailSearchPayload;
import com.tapglue.android.http.payloads.SocialSearchPayload;

import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import io.reactivex.Observable;

interface PaginatedService {

    @GET("/0.4/users/{id}/likes")
    Observable<LikesFeed> retrieveLikesByUser(@Path("id") String userId);

    @GET("/0.4/me/feed")
    Observable<RawNewsFeed> retrieveNewsFeed();

    @GET("/0.4/users/search")
    Observable<UsersFeed> searchUsers(@Query("q") String searchTerm);

    @POST("/0.4/users/search/emails")
    Observable<UsersFeed> searchUsersByEmail(@Body EmailSearchPayload payload);

    @POST("/0.4/users/search/{platform}")
    Observable<UsersFeed> searchUsersBySocialIds(@Path("platform") String platform,
                                                 @Body SocialSearchPayload payload);

    @GET("/0.4/me/friends")
    Observable<UsersFeed> retrieveFriends();

    @GET("/0.4/users/{id}/friends")
    Observable<UsersFeed> retrieveUserFriends(@Path("id") String id);

    @GET("/0.4/me/connections/pending")
    Observable<ConnectionsFeed> retrievePendingConnections();

    @GET("/0.4/me/connections/rejected")
    Observable<ConnectionsFeed> retrieveRejectedConnections();

    @GET("/0.4/posts/{id}/comments")
    Observable<CommentsFeed> retrieveCommentsForPost(@Path("id") String postId);

    @GET("/0.4/posts/{id}/likes")
    Observable<LikesFeed> retrieveLikesForPost(@Path("id") String postId);

    @GET("/0.4/posts")
    Observable<PostListFeed> retrievePosts();

    @GET("/0.4/users/{id}/posts")
    Observable<PostListFeed> retrievePostsByUser(@Path("id") String id);

    @GET("/0.4/me/feed/posts")
    Observable<PostListFeed> retrievePostFeed();

    @GET("/0.4/me/feed/notifications/self")
    Observable<EventListFeed> retrieveMeFeed();
}