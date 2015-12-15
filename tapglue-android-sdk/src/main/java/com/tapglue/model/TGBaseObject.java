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
import android.support.annotation.Nullable;

import com.tapglue.networking.TGCustomCacheObject;

import java.io.Serializable;

public abstract class TGBaseObject<T extends TGBaseObject<T>> implements Serializable {

    /**
     * Type of object for caching purposes
     */
    Integer mCacheObjectType;
    /**
     * If of object used for requests
     */
    private Long mReadObjectId;
    /**
     * User ID that will be used only for reading purposes inside library
     */
    private Long mReadUserId;

    TGBaseObject(@NonNull TGCustomCacheObject.TGCacheObjectType type) {
        mCacheObjectType = type.toCode();
    }

    /**
     * Get type of cached object
     *
     * @return Cache object type
     */
    @Nullable
    public TGCustomCacheObject.TGCacheObjectType getCacheObjectType() {
        return TGCustomCacheObject.TGCacheObjectType.fromCode(mCacheObjectType);
    }

    /**
     * Get id of object used for requests - for internal usage only
     *
     * @return ID of object
     */
    public Long getReadRequestObjectId() {
        return mReadObjectId;
    }

    /**
     * Set id of object for requests - internal usage only
     *
     * @param id
     *
     * @return Current object
     */
    public T setReadRequestObjectId(Long id) {
        mReadObjectId = id;
        return getThis();
    }

    /**
     * Get read request user Id - for internal usage only WARNING! will be overwritten by library
     *
     * @return read request user ID
     */
    final public Long getReadRequestUserId() {
        return mReadUserId;
    }

    /**
     * Set read request user Id - for internal usage only WARNING! will be overwritten by library
     *
     * @param userId
     *
     * @return Current object
     */
    @NonNull
    final public T setReadRequestUserId(Long userId) {
        this.mReadUserId = userId;
        return getThis();
    }

    protected abstract T getThis();
}
