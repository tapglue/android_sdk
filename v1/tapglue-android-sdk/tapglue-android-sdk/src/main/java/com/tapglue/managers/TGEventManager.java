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

package com.tapglue.managers;

import android.support.annotation.NonNull;

import com.tapglue.model.TGEvent;
import com.tapglue.model.TGEventObject;
import com.tapglue.networking.requests.TGRequestCallback;

public interface TGEventManager {
    /**
     * Create event with selected type
     *
     * @param type
     * @param callback
     */
    void createEvent(@NonNull String type, @NonNull final TGRequestCallback<TGEvent> callback);

    /**
     * Create event with selected type and object
     *
     * @param type
     * @param object
     * @param callback
     */
    void createEvent(@NonNull String type, @NonNull TGEventObject object, @NonNull final TGRequestCallback<TGEvent> callback);

    /**
     * Create event with selected type and object ID
     *
     * @param type
     * @param objectId
     * @param callback
     */
    void createEvent(@NonNull String type, @NonNull String objectId, @NonNull final TGRequestCallback<TGEvent> callback);

    /**
     * Create event with custom params
     *
     * @param event
     * @param callback
     */
    void createEvent(@NonNull TGEvent event, @NonNull final TGRequestCallback<TGEvent> callback);

    /**
     * Delete an event by ID
     *
     * @param id
     * @param callback
     */
    void removeEvent(@NonNull Long id, @NonNull final TGRequestCallback<Object> callback);
}
