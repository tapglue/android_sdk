package com.tapglue.android.http;

import com.tapglue.android.http.payloads.EmailSearchPayload;

import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

interface PaginatedService {

    @GET("/0.4/users/{id}/likes")
    Observable<LikesFeed> retrieveLikesByUser(@Path("id") String userId);

    @GET("/0.4/me/feed")
    Observable<RawNewsFeed> retrieveNewsFeed();

    @GET("/0.4/users/search")
    Observable<UsersFeed> searchUsers(@Query("q") String searchTerm);

    @POST("/0.4/users/search/emails")
    Observable<UsersFeed> searchUsersByEmail(@Body EmailSearchPayload payload);
}