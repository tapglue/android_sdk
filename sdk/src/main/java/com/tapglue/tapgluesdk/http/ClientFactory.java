package com.tapglue.tapgluesdk.http;

import com.tapglue.tapgluesdk.Configuration;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;

/**
 * Created by John on 4/6/16.
 */
class ClientFactory {

    static OkHttpClient createClient(Configuration configuration) {
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        return new OkHttpClient.Builder()
                .addInterceptor(new HeaderInterceptor(configuration.getToken()))
                .addInterceptor(loggingInterceptor)
                .build();
    }
}
