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
package com.tapglue.model.queries;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TGQueryObject {

    @Expose
    @SerializedName("id")
    private String id;

    @Expose
    @SerializedName("type")
    private String type;

    /**
     * Get query object id
     *
     * @return
     */
    public String getId() {
        return id;
    }

    /**
     * Set query object id
     *
     * @param id
     *
     * @return
     */
    @NonNull
    public TGQueryObject setId(String id) {
        this.id = id;
        return this;
    }

    /**
     * Get query object type
     *
     * @return
     */
    @Nullable
    public TGQueryType getType() {
        return TGQueryType.fromString(type);
    }

    /**
     * Set query object type
     *
     * @param type
     *
     * @return
     */
    @NonNull
    public TGQueryObject setType(@NonNull TGQueryType type) {
        this.type = type.getStringRepresentation();
        return this;
    }
}
