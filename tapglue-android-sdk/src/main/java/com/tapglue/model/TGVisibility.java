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

public enum TGVisibility {
    Private(10), Connections(20), Public(30), Global(40);

    private final int value;

    @NonNull
    public static TGVisibility fromValue(@Nullable Integer valueToFind) {
        if (valueToFind == null) {
            return Private;
        }
        for (TGVisibility value : values()) {
            if (value.asValue().intValue() == valueToFind.intValue()) {
                return value;
            }
        }
        return Private;
    }

    TGVisibility(int realValue) {
        value = realValue;
    }

    public Integer asValue() {
        return value;
    }
}
