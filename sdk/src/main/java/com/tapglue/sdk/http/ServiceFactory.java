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

import com.tapglue.sdk.Configuration;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class ServiceFactory {
    String sessionToken = "";
    Configuration configuration;

    public ServiceFactory(Configuration configuration) {
        this.configuration = configuration;
    }

    public TapglueService createTapglueService() {
        OkHttpClient client = ClientFactory.createClient(configuration, sessionToken);

        Retrofit retrofit = new Retrofit.Builder().client(client)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(configuration.getBaseUrl()).build();
        return retrofit.create(TapglueService.class);
    }

    public void setSessionToken(String token) {
        this.sessionToken = token;
    }
}
