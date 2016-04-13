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

import android.os.Build;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

class HeaderInterceptor implements Interceptor {

    String appToken;
    String sessionToken;
    Base64Encoder encoder = new Base64Encoder();

    HeaderInterceptor(String appToken, String sessionToken) {
        this.appToken = appToken;
        this.sessionToken = sessionToken;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        request = request.newBuilder()
                .addHeader("Authorization", "Basic " + encoder.encode(appToken + ":" + sessionToken))
                .addHeader("Content-Type", "application/json")
                .addHeader("X-Tapglue-OS", "Android")
                .addHeader("X-Tapglue-OSVersion", Build.VERSION.RELEASE)
                .addHeader("X-Tapglue-Manufacturer", Build.MANUFACTURER != null ? Build.MANUFACTURER : "Unknown_manufacturer")
                .addHeader("X-Tapglue-SDKVersion", "")
                .addHeader("X-Tapglue-Model", Build.MODEL != null ? Build.MODEL : "Unknown_model").build();
        return chain.proceed(request);
    }
}