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

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tapglue.networking.TGCustomCacheObject;

import java.util.List;

public class TGSocialConnections extends TGBaseObject<TGSocialConnections> {
    @Expose
    @SerializedName("connection_ids")
    private List<String> mConnectionIds;

    @Expose
    @SerializedName("platform")
    private String mPlatform;

    @Expose
    @SerializedName("platform_user_id")
    private String mPlatformId;

    @Expose
    @SerializedName("type")
    private String mType;

    public TGSocialConnections() {
        super(TGCustomCacheObject.TGCacheObjectType.SocialConnections);
    }


    public String getPlatform() {
        return mPlatform;
    }

    @NonNull
    public TGSocialConnections setPlatform(String platform) {
        mPlatform = platform;
        return this;
    }

    public List<String> getPlatformConnectionsId() {
        return mConnectionIds;
    }

    public String getPlatformId() {
        return mPlatformId;
    }

    @NonNull
    public TGSocialConnections setPlatformId(String id) {
        mPlatformId = id;
        return this;
    }

    @NonNull
    @Override
    protected TGSocialConnections getThis() {
        return this;
    }

    @Nullable
    public TGConnection.TGConnectionType getType() {
        if (mType == null) { return null; }
        return TGConnection.TGConnectionType.valueOf(mType);
    }

    @NonNull
    public TGSocialConnections setType(@NonNull TGConnection.TGConnectionType type) {
        mType = type.toString();
        return this;
    }

    @NonNull
    public TGSocialConnections setConnectionsIds(List<String> connectionIds) {
        mConnectionIds = connectionIds;
        return this;
    }
}
