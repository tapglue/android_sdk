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
package com.tapglue.model.queries;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

class TGQueryTGObject extends TGQueryObjectField {

    @Expose
    @Nullable
    @SerializedName("id")
    private TGQueryScalarField id;

    @Expose
    @Nullable
    @SerializedName("type")
    private TGQueryScalarField type;

    @Nullable
    static TGQueryTGObject merge(@Nullable TGQueryTGObject myObject, @Nullable TGQueryTGObject newObject) {
        if (myObject == null) return newObject;
        if (newObject == null) return myObject;

        TGQueryScalarField existingId = myObject.getId();
        TGQueryScalarField newId = newObject.getId();
        myObject.setId(TGQueryScalarField.merge(existingId, newId));

        TGQueryScalarField existingType = myObject.getType();
        TGQueryScalarField newType = newObject.getType();
        myObject.setType(TGQueryScalarField.merge(existingType, newType));

        return myObject;
    }

    /**
     * Get query object id
     *
     * @return
     */
    @Nullable
    TGQueryScalarField getId() {
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
    TGQueryTGObject setId(@NonNull TGQueryScalarField id) {
        this.id = id;
        return this;
    }

    /**
     * Get the type to query for
     *
     * @return
     */
    @Nullable
    TGQueryScalarField getType() {
        return type;
    }

    /**
     * Set the type to query for
     *
     * @param type
     *
     * @return
     */
    @NonNull
    TGQueryTGObject setType(@NonNull TGQueryScalarField type) {
        this.type = type;
        return this;
    }
}
