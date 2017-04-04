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
package com.tapglue.android.http.payloads;

import com.google.gson.Gson;

import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;

public class UsernameLoginPayloadTest {

    @Test
    public void constructsCorrectJson() {
        String expectedJson = "{\"user_name\":\"user\",\"password\":\"password\"}";
        String json = new Gson().toJson(new UsernameLoginPayload("user", "password"));
        assertThat(json, equalTo(expectedJson));
    }
}
