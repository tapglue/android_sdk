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

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tapglue.networking.TGCustomCacheObject;

public class TGAttachment extends TGBaseObject<TGAttachment> {

    @Expose
    @SerializedName("content")
    private String content;

    @Expose
    @SerializedName("name")
    private String name;

    @Expose
    @SerializedName("type")
    private String type;

    TGAttachment(@NonNull TGCustomCacheObject.TGCacheObjectType type) {
        super(type);
    }

    /**
     * Get attachment content
     *
     * @return
     */
    public String getContent() {
        return content;
    }

    /**
     * Set attachment content
     *
     * @param content New attachment content
     *
     * @return Current instance
     */
    @NonNull
    public TGAttachment setContent(String content) {
        this.content = content;
        return getThis();
    }

    /**
     * Get attachment name
     *
     * @return Attachment name
     */
    public String getName() {
        return name;
    }

    /**
     * Set attachment name
     *
     * @param name New attachment name
     *
     * @return Current instance
     */
    @NonNull
    public TGAttachment setName(String name) {
        this.name = name;
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
        return type;
    }

    /**
     * Set attachment type
     *
     * @param type New Attachment type
     *
     * @return Current instance
     */
    @NonNull
    public TGAttachment setType(String type) {
        this.type = type;
        return getThis();
    }
}
