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
package com.tapglue.android.http;

import com.tapglue.android.Configuration;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;

public class ClientFactory {

    private ClientFactory() {}

    public static OkHttpClient createClient(Configuration configuration, String sessionToken, String uuid) {
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(configuration.isLogging() ? HttpLoggingInterceptor.Level.BODY: HttpLoggingInterceptor.Level.NONE);
        return new OkHttpClient.Builder()
                .addInterceptor(new HeaderInterceptor(configuration.getToken(), sessionToken, uuid))
                .addInterceptor(new ErrorInterceptor())
                .addInterceptor(loggingInterceptor)
                .build();
    }
}
