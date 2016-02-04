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

import java.util.ArrayList;
import java.util.List;

class TGQueryScalarField<T> {
    @Nullable
    @Expose
    @SerializedName("eq")
    private T eq;

    @Nullable
    @Expose
    @SerializedName("in")
    private List<T> in;

    @NonNull
    private transient String name = "";

    @Nullable
    static TGQueryScalarField merge(@Nullable TGQueryScalarField myField, @Nullable TGQueryScalarField newField) {
        if (myField != null && newField != null) {
            Object fieldEq = newField.getEq();
            if (fieldEq != null) myField.setEq(fieldEq);

            List fieldIn = newField.getIn();
            if (fieldIn != null) myField.addIn(fieldIn);
        }
        else if (newField != null) {
            myField = newField;
        }

        return myField;
    }

    @NonNull
    TGQueryScalarField addIn(@NonNull List<T> value) {
        if (in == null) {
            in = new ArrayList<>();
        }
        in.addAll(value);

        return this;
    }

    @Nullable
    T getEq() {
        return eq;
    }

    @NonNull
    TGQueryScalarField setEq(@NonNull T eq) {
        this.eq = eq;
        return this;
    }

    @Nullable
    List<T> getIn() {
        return in;
    }

    @NonNull
    String getName() {
        return name;
    }

    @NonNull
    TGQueryScalarField setName(@NonNull String name) {
        this.name = name;
        return this;
    }
}
