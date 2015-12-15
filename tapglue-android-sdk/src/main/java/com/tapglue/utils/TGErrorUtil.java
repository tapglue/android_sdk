/*
 * Copyright (c) 2015 Tapglue (https://www.tapglue.com/). All rights reserved.
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

package com.tapglue.utils;

import android.support.annotation.Nullable;

import com.tapglue.networking.requests.TGRequestCallback;
import com.tapglue.networking.requests.TGRequestErrorType;

import java.util.List;

public class TGErrorUtil {
    /**
     * Send an error to a list of callbacks
     *
     * @param callbacks Callback list
     * @param errorType Error type
     */
    public static void sendErrorToCallbacks(
        @Nullable List callbacks,
        TGRequestErrorType.ErrorType errorType
    ) {
        if (callbacks == null) return;

        TGRequestErrorType error = new TGRequestErrorType(errorType);
        for (int i = 0; i < callbacks.size(); i++) {
            Object callback = callbacks.get(i);
            if (!(callback instanceof TGRequestCallback<?>)) {
                continue;
            }
            ((TGRequestCallback<?>) callbacks).onRequestError(error);
        }
    }

    /**
     * Send an error to a list of callbacks
     *
     * @param callbacks    Callback list
     * @param errorCode    Code of the error
     * @param errorMessage Message of the error
     */
    public static void sendErrorToCallbacks(
        @Nullable List callbacks,
        Long errorCode,
        String errorMessage
    ) {
        if (callbacks == null) return;

        TGRequestErrorType error = new TGRequestErrorType(errorCode, errorMessage);
        for (int i = 0; i < callbacks.size(); i++) {
            Object callback = callbacks.get(i);
            if (!(callback instanceof TGRequestCallback<?>)) {
                continue;
            }
            ((TGRequestCallback<?>) callback).onRequestError(error);
        }
    }
}
