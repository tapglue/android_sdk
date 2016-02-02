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

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.security.InvalidParameterException;
import java.util.Arrays;
import java.util.List;

public class TGQuery {

    @Expose
    @SerializedName("object")
    private TGQueryObjectField object;

    @Expose
    @SerializedName("tg_object_id")
    private TGQueryScalarField tgObjectId;

    @Expose
    @SerializedName("type")
    private TGQueryScalarField type;

    @NonNull
    public static <T> TGQueryScalarField eq(@NonNull T value) {
        return new TGQueryScalarField<>().setEq(value);
    }

    @NonNull
    public static <T> TGQueryScalarField in(@NonNull List<T> value) {
        return new TGQueryScalarField<T>().addIn(value);
    }

    @SafeVarargs
    @NonNull
    public static <T> TGQueryScalarField in(T... values) {
        return new TGQueryScalarField<T>().addIn(Arrays.asList(values));
    }

    @NonNull
    public static TGQueryObjectField object(@NonNull TGQueryTGObject object) {
        TGQueryTGObject field = new TGQueryTGObject();
        field.setName("object");
        if (object.getId() != null) field.setId(object.getId());
        if (object.getType() != null) field.setType(object.getType());
        return field;
    }

    @NonNull
    public static TGQueryTGObject objectId(@NonNull TGQueryScalarField field) {
        TGQueryTGObject myField = new TGQueryTGObject();
        field.setName("id");
        myField.setId(field);
        return myField;
    }

    @NonNull
    public static TGQueryTGObject objectType(@NonNull TGQueryScalarField field) {
        TGQueryTGObject myField = new TGQueryTGObject();
        field.setName("type");
        myField.setType(field);
        return myField;
    }

    @NonNull
    public static <T> TGQueryScalarField tgObjectId(@NonNull TGQueryScalarField<T> tgObjectId) {
        TGQueryScalarField<T> field = new TGQueryScalarField<>();
        field.setName("tgObjectId");
        if (tgObjectId.getEq() != null) field.setEq(tgObjectId.getEq());
        if (tgObjectId.getIn() != null) field.addIn(tgObjectId.getIn());
        return field;
    }

    @NonNull
    public static <T> TGQueryScalarField type(@NonNull TGQueryScalarField<T> type) {
        TGQueryScalarField<T> field = new TGQueryScalarField<>();
        field.setName("type");
        if (type.getEq() != null) field.setEq(type.getEq());
        if (type.getIn() != null) field.addIn(type.getIn());
        return field;
    }

    @NonNull
    public TGQuery addConstraint(@NonNull TGQueryScalarField field) throws InvalidParameterException {
        switch (field.getName()) {
            case "tgObjectId": {
                tgObjectId = TGQueryScalarField.merge(tgObjectId, field);
            }
            break;

            case "type": {
                type = TGQueryScalarField.merge(type, field);
            }
            break;

            default:
                throw new InvalidParameterException(String.format("Field %s not found", field.getName()));
        }
        return this;
    }

    @NonNull
    public TGQuery addConstraint(@NonNull TGQueryObjectField field) throws RuntimeException {
        switch (field.getName()) {
            case "object": {
                if (!(field instanceof TGQueryTGObject)) {
                    throw new InvalidParameterException(String.format("Field %s is of unexpected type", field.getName()));
                }
                object = TGQueryTGObject.merge((TGQueryTGObject) object, (TGQueryTGObject) field);
            }
            break;

            default:
                throw new InvalidParameterException(String.format("Field %s not found", field.getName()));
        }

        return this;
    }
}
