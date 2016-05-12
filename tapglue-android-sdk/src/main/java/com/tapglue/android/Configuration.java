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
package com.tapglue.android;

/**
 * The configuration class is where things such as base URLs, client token and logging are
 * configured to the SDK
 */
public class Configuration {

    private final String baseUrl;
    private final String token;
    private boolean isLogging = false;

    /**
     * @param baseUrl URL provided by tapglue to be used for all requests done by the SDK
     * @param token Token provided by tapglue for authentication of the client
     */
    public Configuration(String baseUrl, String token) {
        this.token = token;
        this.baseUrl = baseUrl;
    }

    public String getToken() {
        return token;
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    /**
     * Toggles logging
     * @param isLogging if true then output of all http requests will be printed to the log
     */
    public void setLogging(boolean isLogging) {
        this.isLogging = isLogging;
    }
    public boolean isLogging() {
        return isLogging;
    }
}
