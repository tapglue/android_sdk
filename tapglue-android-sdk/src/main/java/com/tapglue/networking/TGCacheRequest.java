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

package com.tapglue.networking;

import android.support.annotation.NonNull;

import com.tapglue.model.TGBaseObject;
import com.tapglue.networking.requests.TGRequestCallback;
import com.tapglue.networking.requests.TGRequestErrorType;
import com.tapglue.networking.requests.TGRequestType;

class TGCacheRequest<T extends TGBaseObject> {
    private static final TGRequestCallback dummyCallback = new TGRequestCallback() {
        @Override
        public boolean callbackIsEnabled() {
            return true;
        }

        @Override
        public void onRequestError(TGRequestErrorType cause) {

        }

        @Override
        public void onRequestFinished(Object output, boolean changeDoneOnline) {

        }
    };

    /**
     * Request object
     */
    private final T object;

    /**
     * Request type
     */
    private final TGRequestType type;

    public TGCacheRequest(@NonNull TGRequest<T, ?> req) {
        type = req.getRequestType();
        object = req.getObject();
    }

    /**
     * Convert cache request to standard request Those requests will always receive generic
     * callback
     * method, that is never outdated, but it also won't do any changes in UI due to always
     * outdated
     * old callbacks
     *
     * @return Converted Tapglue request
     */
    @NonNull
    public TGRequest<T, ?> toTGRequest() {
        return new TGRequest(object, type, true, dummyCallback);
    }
}
