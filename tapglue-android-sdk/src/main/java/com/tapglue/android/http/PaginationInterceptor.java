package com.tapglue.android.http;

import java.io.IOException;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

class PaginationInterceptor implements Interceptor {
    int pageSize;

    PaginationInterceptor(int pageSize) {
        this.pageSize = pageSize;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request original = chain.request();
        HttpUrl originalUrl = original.url();

        HttpUrl url = originalUrl.newBuilder()
            .addQueryParameter("limit", pageSize + "")
            .build();

        Request.Builder requestBuilder = original.newBuilder()
            .url(url);

        Request request = requestBuilder.build();

        return chain.proceed(request);
    }
}