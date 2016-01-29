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
import android.support.annotation.Nullable;

import com.google.gson.JsonElement;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tapglue.networking.TGCustomCacheObject.TGCacheObjectType;

import java.io.Serializable;

public abstract class TGBaseObject<T extends TGBaseObject<T>> implements Serializable {

    /**
     * Type of object for caching purposes
     */
    Integer cacheObjectType;

    @SerializedName("created_at")
    private String createdAt;

    @Expose
    @SerializedName("metadata")
    private JsonElement metadata;

    /**
     * If of object used for requests
     */
    private Long readObjectId;

    /**
     * If of object used for requests
     */
    private String readObjectStringId;

    /**
     * User ID that will be used only for reading purposes inside library
     */
    private Long readUserId;

    @SerializedName("updated_at")
    private String updatedAt;

    TGBaseObject(@NonNull TGCacheObjectType type) {
        cacheObjectType = type.toCode();
    }

    /**
     * Get type of cached object
     *
     * @return Cache object type
     */
    @Nullable
    public TGCacheObjectType getCacheObjectType() {
        return TGCacheObjectType.fromCode(cacheObjectType);
    }

    /**
     * Get date of creation
     *
     * @return Date in string format, taken from server object
     */
    final public String getCreatedAt() {
        return createdAt;
    }

    /**
     * Get Metadata returns a JsonElement which can contain complex objects. The deserialization
     * needs to be handled / implemented based on the custom format.
     *
     * @return metadata
     */
    public JsonElement getMetadata() {
        return metadata;
    }

    /**
     * Set Metadata is a JsonElement and thus can contain more complex objects. The construction of
     * the complex JsonElement or simply JsonPrimitive has to be handled / implemented.
     *
     * @param newValue new metadata value
     *
     * @return Current object
     */
    @NonNull
    public T setMetadata(JsonElement newValue) {
        metadata = newValue;
        return getThis();
    }

    /**
     * Get id of object used for requests - for internal usage only
     *
     * @return ID of object
     */
    public Long getReadRequestObjectId() {
        return readObjectId;
    }

    /**
     * Set id of object for requests - internal usage only
     *
     * @param id
     *
     * @return Current object
     */
    @NonNull
    public T setReadRequestObjectId(Long id) {
        readObjectId = id;
        return getThis();
    }

    /**
     * Get id of object used for requests - for internal usage only
     *
     * @return ID of object
     */
    public String getReadRequestObjectStringId() {
        return readObjectStringId;
    }

    /**
     * Set id of object for requests - internal usage only
     *
     * @param id
     *
     * @return Current object
     */
    @NonNull
    public T setReadRequestObjectStringId(String id) {
        readObjectStringId = id;
        return getThis();
    }

    /**
     * Get read request user Id - for internal usage only WARNING! will be overwritten by library
     *
     * @return read request user ID
     */
    final public Long getReadRequestUserId() {
        return readUserId;
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
        this.readUserId = userId;
        return getThis();
    }

    @NonNull
    protected abstract T getThis();

    /**
     * Get data of last last update on server
     *
     * @return Date in string format, taken from server object
     */
    final public String getUpdatedAt() {
        return updatedAt;
    }
}
