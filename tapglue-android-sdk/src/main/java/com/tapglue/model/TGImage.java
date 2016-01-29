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
import com.tapglue.networking.TGCustomCacheObject.TGCacheObjectType;

public class TGImage extends TGBaseObject<TGImage> {
    @Expose
    @SerializedName("height")
    int height;

    @Expose
    @SerializedName("type")
    String type;

    @Expose
    @SerializedName("url")
    String url;

    @Expose
    @SerializedName("width")
    int width;

    TGImage() {
        super(TGCacheObjectType.Connection);
    }

    /**
     * Get image height
     *
     * @return image height
     */
    public int getHeight() {
        return height;
    }

    /**
     * Set image height
     *
     * @param height new height value
     *
     * @return Current object
     */
    @NonNull
    public TGImage setHeight(int height) {
        this.height = height;
        return this;
    }

    @NonNull
    @Override
    protected TGImage getThis() {
        return this;
    }

    /**
     * Get image type
     *
     * @return image type
     */
    public String getType() {
        return type;
    }

    /**
     * Set image type
     *
     * @param type new type value
     *
     * @return Current object
     */
    @NonNull
    public TGImage setType(String type) {
        this.type = type;
        return this;
    }

    /**
     * Get image url
     *
     * @return image url
     */
    public String getURL() {
        return url;
    }

    /**
     * Set image url
     *
     * @param URL new url value
     *
     * @return Current object
     */
    @NonNull
    public TGImage setURL(String URL) {
        this.url = URL;
        return this;
    }

    /**
     * Get image width
     *
     * @return image width
     */
    public int getWidth() {
        return width;
    }

    /**
     * Set image width
     *
     * @param width new width value
     *
     * @return Current object
     */
    @NonNull
    public TGImage setWidth(int width) {
        this.width = width;
        return this;
    }
}
