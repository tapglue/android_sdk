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
package com.tapglue.sdk.http;

import com.tapglue.sdk.entities.User;
import com.tapglue.sdk.entities.Connection;
import com.tapglue.sdk.http.payloads.EmailLoginPayload;
import com.tapglue.sdk.http.payloads.SocialConnections;
import com.tapglue.sdk.http.payloads.UsernameLoginPayload;

import retrofit2.http.Body;
import retrofit2.http.Path;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Query;
import rx.Observable;

public interface TapglueService {
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

    @GET("/0.4/me/friends")
    Observable<UsersFeed> retrieveFriends();

    @PUT("/0.4/me/connections")
    Observable<Connection> createConnection(@Body Connection connection);

    @POST("/0.4/me/connections/social")
    Observable<UsersFeed> createSocialConnections(@Body SocialConnections connections);

    @GET("/0.4/me/connections/pending")
    Observable<ConnectionsFeed> retrievePendingConnections();

    @GET("/0.4/me/connections/rejected")
    Observable<ConnectionsFeed> retrieveRejectedConnections();

    @GET("/0.4/users/search")
    Observable<UsersFeed> searchUsers(@Query("q") String searchTerm);
}
