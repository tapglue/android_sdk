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

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ServiceFactoryTest {

    @Mock
    Configuration configuration;

    //SUT
    ServiceFactory serviceFactory;

    @Before public void setup() {
        when(configuration.getBaseUrl()).thenReturn("https://someapi.tapglue.com");
        serviceFactory = new ServiceFactory(configuration);
    }

    @Test
    public void serviceForIsNotNull() {
        TapglueService service = serviceFactory.createTapglueService();
        assertThat(service, notNullValue());
    }

    @Test
    public void createServiceAsksForBaseUrl() {
        serviceFactory.createTapglueService();
        verify(configuration).getBaseUrl();
    }
}
