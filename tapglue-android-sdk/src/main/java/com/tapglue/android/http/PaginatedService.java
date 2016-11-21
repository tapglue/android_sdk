package com.tapglue.android.http;

import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;

interface PaginatedService {

    @GET("/0.4/users/{id}/likes")
    Observable<LikesFeed> retrieveLikesByUser(@Path("id") String userId);

    @GET("/0.4/me/feed")
    Observable<RawNewsFeed> retrieveNewsFeed();
}