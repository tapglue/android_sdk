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

import java.util.Map;

public class TGEventObject extends TGBaseObject<TGEventObject> {

    @Expose
    @SerializedName("display_name")
    private Map<String, String> mDisplayName;

    @NonNull
    @Expose
    @SerializedName("id")
    private String mID = "";

    @Expose
    @SerializedName("type")
    private String mType;

    @Expose
    @SerializedName("url")
    private String mUrl;

    public TGEventObject() {
        super(TGCustomCacheObject.TGCacheObjectType.EventObject);
    }

    /**
     * Get display name
     *
     * @return display name
     */
    public Map<String, String> getDisplayName() {
        return mDisplayName;
    }

    /**
     * Set display name
     *
     * @param newValue new display name value
     *
     * @return Current object
     */
    @NonNull
    public TGEventObject setDisplayName(Map<String, String> newValue) {
        mDisplayName = newValue;
        return this;
    }

    /**
     * Get object custom id
     *
     * @return object id
     */
    @NonNull
    public String getID() {
        return mID;
    }

    /**
     * Set object custom id
     *
     * @param ID new id value
     *
     * @return Current object
     */
    public TGEventObject setID(@NonNull String ID) {
        this.mID = ID;
        return this;
    }

    @Override
    protected TGEventObject getThis() {
        return this;
    }

    /**
     * Get event object type
     *
     * @return object type
     */
    public String getType() {
        return mType;
    }

    /**
     * Set event object type
     *
     * @param newType new object type value
     *
     * @return Current object
     */
    @NonNull
    public TGEventObject setType(String newType) {
        mType = newType;
        return this;
    }

    /**
     * Get url
     *
     * @return url
     */
    public String getUrl() {
        return mUrl;
    }

    /**
     * @param newUrl
     *
     * @return Current object
     */
    @NonNull
    public TGEventObject setUrl(String newUrl) {
        mUrl = newUrl;
        return this;
    }
}
