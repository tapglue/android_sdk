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

import com.google.gson.annotations.SerializedName;

import java.io.IOException;

/**
 * Specifies errors returned by the web API.
 */
public class TapglueError extends IOException{
    @SerializedName("code")
    private final int code;
    @SerializedName("message")
    private final String message;

    public TapglueError(int code, String message) {
        this.code = code;
        this.message = message;
    }

    /**
     * The error code specifies what the error means. For more documentation
     * please refer to the documentation of the web API.
     * @return error code returned by the web API
     * @see  <a href="https://developers.tapglue.com/docs/error-handling">error handling documentation</a>
     */
    public int getCode() {
        return code;
    }

    /**
     * @return the error message from the API.
     */
    @Override
    public String getMessage() {
        return message;
    }
}
