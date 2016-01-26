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
import android.support.annotation.Nullable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tapglue.networking.TGCustomCacheObject;

public class TGLoginUser<T extends TGBaseObject<T>> extends TGBaseObjectWithId<T, Long> {
    @Expose
    @SerializedName("email")
    String mEmail;

    @NonNull
    @Expose
    @SerializedName("password")
    String mPassword;

    @Expose
    @SerializedName("user_name")
    String mUserName;

    public TGLoginUser(String userName, String email, @NonNull String password) {
        super(TGCustomCacheObject.TGCacheObjectType.LoginUser);
        mUserName = userName;
        mEmail = email;
        mPassword = password;
    }

    /**
     * Get user email
     *
     * @return user email
     */
    public String getEmail() {
        return mEmail;
    }

    /**
     * Get user password
     *
     * @return user password
     */
    @Nullable
    public String getPassword() {
        return mPassword;
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
        return mUserName;
    }
}
