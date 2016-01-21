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

public class TGAttachment extends TGBaseObject<TGAttachment> {

    @Expose
    @SerializedName("content")
    private String mContent;

    @Expose
    @SerializedName("name")
    private String mName;

    @Expose
    @SerializedName("type")
    private String mType;

    TGAttachment(@NonNull TGCustomCacheObject.TGCacheObjectType type) {
        super(type);
    }

    /**
     * Get attachment content
     *
     * @return
     */
    public String getContent() {
        return mContent;
    }

    /**
     * Set attachment content
     *
     * @param mContent New attachment content
     *
     * @return Current instance
     */
    @NonNull
    public TGAttachment setContent(String mContent) {
        this.mContent = mContent;
        return getThis();
    }

    /**
     * Get attachment name
     *
     * @return Attachment name
     */
    public String getName() {
        return mName;
    }

    /**
     * Set attachment name
     *
     * @param mName New attachment name
     *
     * @return Current instance
     */
    @NonNull
    public TGAttachment setName(String mName) {
        this.mName = mName;
        return getThis();
    }

    @NonNull
    @Override
    protected TGAttachment getThis() {
        return this;
    }

    /**
     * Get attachment type
     *
     * @return Attachment type
     */
    public String getType() {
        return mType;
    }

    /**
     * Set attachment type
     *
     * @param mType New Attachment type
     *
     * @return Current instance
     */
    @NonNull
    public TGAttachment setType(String mType) {
        this.mType = mType;
        return getThis();
    }
}
