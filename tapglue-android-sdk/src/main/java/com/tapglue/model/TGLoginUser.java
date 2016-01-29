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

public class TGLoginUser<T extends TGBaseObject<T>> extends TGBaseObjectWithId<T, Long> {
    @Expose
    @SerializedName("email")
    String email;

    @NonNull
    @Expose
    @SerializedName("password")
    String password;

    @Expose
    @SerializedName("user_name")
    String userName;

    public TGLoginUser(String userName, String email, @NonNull String password) {
        super(TGCustomCacheObject.TGCacheObjectType.LoginUser);
        this.userName = userName;
        this.email = email;
        this.password = password;
    }

    /**
     * Get user email
     *
     * @return user email
     */
    public String getEmail() {
        return email;
    }

    /**
     * Get user password
     *
     * @return user password
     */
    @NonNull
    public String getPassword() {
        return password;
    }

    @NonNull
    @Override
    protected T getThis() {
        return (T) this;
    }

    /**
     * Get user name
     *
     * @return user name
     */
    public String getUserName() {
        return userName;
    }
}
