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

package com.tapglue.networking;

import android.support.annotation.NonNull;

import com.tapglue.model.TGBaseObject;
import com.tapglue.networking.requests.TGRequestCallback;
import com.tapglue.networking.requests.TGRequestType;

import java.util.ArrayList;
import java.util.List;

public class TGRequest<OBJECT extends TGBaseObject, OUTOBJECT extends TGBaseObject> {
    /**
     * Is internet required for this request?
     */
    private final boolean mInternetRequired;

    /**
     * Callback for returning data/error info
     */
    private final List<TGRequestCallback<OUTOBJECT>> mReturnCallback = new ArrayList<>();

    /**
     * Request type
     */
    private final TGRequestType mType;

    /**
     * Object on which request will be performed
     */
    private OBJECT mObject;

    /**
     * Create new request
     *
     * @param object            Request object, can't be null
     * @param type              Type of request
     * @param supportedOnlyLive Should request be done only when internet is accessible, or can be
     *                          cached?
     * @param returnMethod      Return callback
     */
    TGRequest(OBJECT object, TGRequestType type, boolean supportedOnlyLive, TGRequestCallback<OUTOBJECT> returnMethod) {
        mObject = object;
        mType = type;
        mInternetRequired = supportedOnlyLive;
        mReturnCallback.add(returnMethod);
    }

    /**
     * Add new callback to list
     *
     * @param newCallback callback to be added
     */
    public void addCallback(TGRequestCallback<OUTOBJECT> newCallback) {
        mReturnCallback.add(newCallback);
    }

    /**
     * Get request callbacks
     *
     * @return callbacks list
     */
    @NonNull
    public List<TGRequestCallback<OUTOBJECT>> getCallbacks() {
        return mReturnCallback;
    }

    /**
     * Get object of request
     *
     * @return Object of request
     */
    public OBJECT getObject() {
        return mObject;
    }

    /**
     * Set object used by request
     *
     * @param object object to be set
     *
     * @return Current object
     */
    @NonNull
    public TGRequest<OBJECT, OUTOBJECT> setObject(OBJECT object) {
        this.mObject = object;
        return this;
    }

    /**
     * Get request type
     *
     * @return request type
     */
    public TGRequestType getRequestType() {
        return mType;
    }

    /**
     * Is request requiring live connection? If yes, then if internet is not accessible at this
     * moment, request won't be queued and callback with error will be called. Otherwise in this
     * case request will be added to cache queue and called again when internet connection will be
     * accessible. but all callbacks will be outdated.
     *
     * @return
     */
    public boolean needToBeDoneLive() {
        return mInternetRequired;
    }
}
