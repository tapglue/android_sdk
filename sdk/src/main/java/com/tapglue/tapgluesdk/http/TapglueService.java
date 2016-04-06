package com.tapglue.tapgluesdk.http;

import com.tapglue.tapgluesdk.LoginPayload;
import com.tapglue.tapgluesdk.entities.User;

import retrofit2.http.Body;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by John on 3/30/16.
 */
public interface TapglueService {
    @POST("/0.4/users/login")
    Observable<User> login(@Body LoginPayload payload);
}
