/*
 *  Copyright (c) 2015-2016 Tapglue (https://www.tapglue.com/). All rights reserved.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package com.tapglue.sdk.http;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Protocol;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

import static junit.framework.Assert.fail;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.powermock.api.mockito.PowerMockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ErrorInterceptorTest {

    private static final String URL = "https://api.tapglue.com";

    @Mock
    Interceptor.Chain chain;

    ErrorInterceptor interceptor = new ErrorInterceptor();

    @Test
    public void throwsExceptionWhenError() throws Exception {
        Request request = new Request.Builder()
                .url(URL).build();
        MediaType appJson = MediaType.parse("application/json");
        Response response = new Response.Builder()
                .code(401)
                .protocol(Protocol.HTTP_1_1)
                .request(request)
                .body(ResponseBody.create(appJson, getSampleBodyStringForErrorCode(1000)))
                .build();

        when(chain.proceed(any(Request.class))).thenReturn(response);
        try {
            interceptor.intercept(chain);
            fail("did not throw exception");
        } catch(TapglueError e) {
            assertThat(e.getCode(), equalTo(1000));
        }
    }

    private String getSampleBodyStringForErrorCode(int errorCode) {
        return "{\n" +
                "    \"data\": {},\n" +
                "    \"errors\": [\n" +
                "        {\n" +
                "            \"code\": " + errorCode + "\n" +
                "        }\n" +
                "    ]\n" +
                "}";
    }
}
