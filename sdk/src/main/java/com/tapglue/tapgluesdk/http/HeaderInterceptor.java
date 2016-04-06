package com.tapglue.tapgluesdk.http;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by John on 4/6/16.
 */
class HeaderInterceptor implements Interceptor {

    String appToken;
    Base64Encoder encoder = new Base64Encoder();

    HeaderInterceptor(String token) {
        this.appToken = token;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {

        Request request = chain.request();
        request = request.newBuilder()
                .addHeader("Authorization", "Basic " + encoder.encode(appToken))
                .addHeader("Content-Type", "application/json").build();
        return chain.proceed(request);
    }
}