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

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;

import static org.hamcrest.CoreMatchers.isA;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsCollectionContaining.hasItem;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ClientFactoryTest {

    private static final String SAMPLE_ENDPOINT = "https://api.tapglue.com";
    private static final String APP_TOKEN = "appToken";
    private static final String SESSION_TOKEN = "sessionToken";

    @Mock
    Configuration configuration;

    @Before
    public void setUp() {
        when(configuration.getBaseUrl()).thenReturn(SAMPLE_ENDPOINT);
        when(configuration.getToken()).thenReturn(APP_TOKEN);
    }

    @Test
    public void addsHeaderInterceptor() {
        OkHttpClient client = ClientFactory.createClient(configuration, SESSION_TOKEN);
        assertThat(client.interceptors(), hasItem(isA(HeaderInterceptor.class)));
    }

    @Test
    public void addsLoggingInterceptor() {
        OkHttpClient client = ClientFactory.createClient(configuration, SESSION_TOKEN);
        assertThat(client.interceptors(), hasItem(isA(HttpLoggingInterceptor.class)));
    }
}
