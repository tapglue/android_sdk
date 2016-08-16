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

import android.os.Build;

import java.io.IOException;
import java.util.Calendar;
import java.util.TimeZone;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

class HeaderInterceptor implements Interceptor {

    private static final String VERSION = "2.1.1";
    String appToken;
    String sessionToken;
    String uuid;

    Base64Encoder encoder = new Base64Encoder();
    TimeZone timeZone;

    HeaderInterceptor(String appToken, String sessionToken, String uuid) {
        this.appToken = appToken;
        this.sessionToken = sessionToken;
        this.uuid = uuid;
        Calendar cal = Calendar.getInstance();
        timeZone = cal.getTimeZone();
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        request = request.newBuilder()
                .addHeader("Authorization", "Basic " + encoder.encode(appToken + ":" + sessionToken))
                .addHeader("Content-Type", "application/json")
                .addHeader("X-Tapglue-OS", "Android")
                .addHeader("X-Tapglue-OSVersion", Build.VERSION.RELEASE != null ? Build.VERSION.RELEASE:"unkown")
                .addHeader("X-Tapglue-Manufacturer", Build.MANUFACTURER != null ? Build.MANUFACTURER : "Unknown_manufacturer")
                .addHeader("X-Tapglue-Model", Build.MODEL != null ? Build.MODEL : "Unknown_model")
                .addHeader("X-Tapglue-SDKVersion", VERSION)
                .addHeader("X-Tapglue-AndroidID", uuid)
                .addHeader("X-Tapglue-Timezone", timeZone.getID())
                .build();
        return chain.proceed(request);
    }
}