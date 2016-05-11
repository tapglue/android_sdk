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

package com.tapglue.android.http;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.Request;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.IsNot.not;
import static org.hamcrest.core.IsNull.nullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.notNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class HeaderInterceptorTest {

    private static final String APP_TOKEN = "appToken";
    private static final String SESSION_TOKEN = "sessionToken";
    private static final String UUID = "clientUUID";
    private static final String URL = "https://api.tapglue.com";
    private static final String ENCODED_STRING = "encodedString";

    @Captor
    ArgumentCaptor<Request> requestCaptor;

    @Mock
    Base64Encoder encoder;

    @Mock
    Interceptor.Chain chain;

    Headers headers;

    //SUT
    HeaderInterceptor interceptor = new HeaderInterceptor(APP_TOKEN, SESSION_TOKEN, UUID);

    @Before
    public void setUp() throws Exception {
        interceptor.encoder = encoder;
        Request request = new Request.Builder()
                .url(URL).build();
        when(encoder.encode(APP_TOKEN + ":" + SESSION_TOKEN)).thenReturn(ENCODED_STRING);
        when(chain.request()).thenReturn(request);

        interceptor.intercept(chain);

        verify(chain).proceed(requestCaptor.capture());
        Request modifiedRequest = requestCaptor.getValue();
        headers = modifiedRequest.headers();
    }

    @Test
    public void addsAuthorizationHeader() throws Exception {
        assertThat(headers.get("Authorization"), equalTo("Basic " + ENCODED_STRING));
    }

    @Test
    public void addsContentType() {
        assertThat(headers.get("Content-Type"), equalTo("application/json"));
    }

    @Test
    public void addsOperativeSystem() {
        assertThat(headers.get("X-Tapglue-OS"), equalTo("Android"));
    }

    @Test
    public void addsOperativeSystemVersion() {
        assertThat(headers.get("X-Tapglue-OSVersion"), not(nullValue()));
    }

    @Test
    public void addsUUID() {
        assertThat(headers.get("X-Tapglue-AndroidID"), equalTo(UUID));
    }
}
