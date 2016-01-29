/*
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

package com.tapglue.model;

import android.support.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tapglue.networking.TGCustomCacheObject;
import com.tapglue.networking.TGCustomCacheObject.TGCacheObjectType;

public class TGError extends TGBaseObject<TGError> {
    /**
     * Error code
     */

    @Expose
    @SerializedName("code")
    private Long errorCode;

    @Expose
    @SerializedName("message")
    private String message;

    public TGError() {
        super(TGCacheObjectType.Error);
    }

    /**
     * Get error code
     *
     * @return Error code
     */
    final public Long getErrorCode() {
        return errorCode;
    }


    /**
     * Get error message from server
     *
     * @return
     */
    final public String getMessage() {
        return message;
    }

    @NonNull
    @Override
    protected TGError getThis() {
        return this;
    }
}
