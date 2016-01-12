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

package com.tapglue.model;

import android.support.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tapglue.networking.TGCustomCacheObject;

/**
 * Base for all API-synchronized objects containing ID
 */
public abstract class TGBaseObjectWithId<T extends TGBaseObject<T>,IDTYPE> extends TGBaseObject<T> {

    @Expose
    @SerializedName("id")
    private IDTYPE mID;

    TGBaseObjectWithId(@NonNull TGCustomCacheObject.TGCacheObjectType type) {
        super(type);
    }

    /**
     * Get ID used for network connections
     *
     * @return ID or null if no network ID is assigned
     */
    @NonNull
    final public IDTYPE getID() {
        return mID;
    }
}
