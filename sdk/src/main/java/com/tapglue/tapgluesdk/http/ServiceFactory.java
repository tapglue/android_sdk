package com.tapglue.tapgluesdk.http;

import com.tapglue.tapgluesdk.entities.Configuration;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by John on 3/30/16.
 */
public class ServiceFactory {
    Configuration configuration;

    public ServiceFactory(Configuration configuration) {
        this.configuration = configuration;
    }

    public TapglueService createTapglueService() {
        OkHttpClient client = ClientFactory.createClient(configuration);

        Retrofit retrofit = new Retrofit.Builder().client(client)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(configuration.getBaseUrl()).build();
        return retrofit.create(TapglueService.class);
    }
}
