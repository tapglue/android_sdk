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

package com.tapglue.managers;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.tapglue.Tapglue;
import com.tapglue.model.TGEvent;
import com.tapglue.model.TGEventObject;
import com.tapglue.networking.requests.TGRequestCallback;
import com.tapglue.networking.requests.TGRequestErrorType;

public class TGEventManager extends AbstractTGManager implements TGEventManagerInterface {

    public TGEventManager(Tapglue tgInstance) {
        super(tgInstance);
    }

    /**
     * Create event with selected type
     *
     * @param type
     * @param returnMethod
     */
    @Override
    public void createEvent(String type, @NonNull TGRequestCallback<TGEvent> returnMethod) {
        if (TextUtils.isEmpty(type)) {
            returnMethod.onRequestError(new TGRequestErrorType(TGRequestErrorType.ErrorType.NULL_INPUT));
            return;
        }
        else if (tapglue.getUserManager().getCurrentUser() == null) {
            returnMethod.onRequestError(new TGRequestErrorType(TGRequestErrorType.ErrorType.USER_NOT_LOGGED_IN));
            return;
        }
        TGEvent event = new TGEvent(tapglue).setType(type);
        tapglue.createRequest().createEvent(event, returnMethod);
    }

    /**
     * Create event with selected type and object
     *
     * @param type
     * @param object
     * @param returnMethod
     */
    @Override
    public void createEvent(String type, TGEventObject object, @NonNull TGRequestCallback<TGEvent> returnMethod) {
        if (TextUtils.isEmpty(type)) {
            returnMethod.onRequestError(new TGRequestErrorType(TGRequestErrorType.ErrorType.NULL_INPUT));
            return;
        }
        else if (tapglue.getUserManager().getCurrentUser() == null) {
            returnMethod.onRequestError(new TGRequestErrorType(TGRequestErrorType.ErrorType.USER_NOT_LOGGED_IN));
            return;
        }
        TGEvent event = new TGEvent(tapglue).setType(type).setObject(object);
        tapglue.createRequest().createEvent(event, returnMethod);
    }

    /**
     * Create event with selected type and object ID
     *
     * @param type
     * @param objectId
     * @param returnMethod
     */
    @Override
    public void createEvent(String type, String objectId, @NonNull TGRequestCallback<TGEvent> returnMethod) {
        if (TextUtils.isEmpty(type)) {
            returnMethod.onRequestError(new TGRequestErrorType(TGRequestErrorType.ErrorType.NULL_INPUT));
            return;
        }
        else if (tapglue.getUserManager().getCurrentUser() == null) {
            returnMethod.onRequestError(new TGRequestErrorType(TGRequestErrorType.ErrorType.USER_NOT_LOGGED_IN));
            return;
        }
        TGEvent event = new TGEvent(tapglue).setType(type).setObject(new TGEventObject().setID(objectId));
        tapglue.createRequest().createEvent(event, returnMethod);
    }

    /**
     * Create event with custom params
     *
     * @param event
     * @param returnMethod
     */
    @Override
    public void createEvent(@Nullable TGEvent event, @NonNull TGRequestCallback<TGEvent> returnMethod) {
        if (event == null) {
            returnMethod.onRequestError(new TGRequestErrorType(TGRequestErrorType.ErrorType.NULL_INPUT));
            return;
        }
        else if (tapglue.getUserManager().getCurrentUser() == null) {
            returnMethod.onRequestError(new TGRequestErrorType(TGRequestErrorType.ErrorType.USER_NOT_LOGGED_IN));
            return;
        }
        tapglue.createRequest().createEvent(new TGEvent(tapglue, event), returnMethod);
    }

    @Override
    public void removeEvent(@Nullable Long id, @NonNull TGRequestCallback<Object> returnMethod) {
        if (id == null) {
            returnMethod.onRequestError(new TGRequestErrorType(TGRequestErrorType.ErrorType.NULL_INPUT));
            return;
        }
        else if (tapglue.getUserManager().getCurrentUser() == null) {
            returnMethod.onRequestError(new TGRequestErrorType(TGRequestErrorType.ErrorType.USER_NOT_LOGGED_IN));
            return;
        }
        tapglue.createRequest().removeEvent(id, returnMethod);
    }
}
