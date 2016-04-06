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
import com.tapglue.model.TGConnection.TGConnectionType;
import com.tapglue.networking.TGCustomCacheObject.TGCacheObjectType;

import java.util.List;

public class TGSocialConnections extends TGBaseObject<TGSocialConnections> {
    @Expose
    @SerializedName("connection_ids")
    private List<String> connectionIds;

    @Expose
    @SerializedName("platform")
    private String platform;

    @Expose
    @SerializedName("platform_user_id")
    private String platformId;

    @Expose
    @SerializedName("type")
    private String type;

    public TGSocialConnections() {
        super(TGCacheObjectType.SocialConnections);
    }


    public String getPlatform() {
        return platform;
    }

    @NonNull
    public TGSocialConnections setPlatform(String platform) {
        this.platform = platform;
        return this;
    }

    public List<String> getPlatformConnectionsId() {
        return connectionIds;
    }

    public String getPlatformId() {
        return platformId;
    }

    @NonNull
    public TGSocialConnections setPlatformId(String id) {
        platformId = id;
        return this;
    }

    @NonNull
    @Override
    protected TGSocialConnections getThis() {
        return this;
    }

    @Nullable
    public TGConnectionType getType() {
        if (type == null) { return null; }
        return TGConnectionType.valueOf(type);
    }

    @NonNull
    public TGSocialConnections setType(@NonNull TGConnectionType type) {
        this.type = type.toString();
        return this;
    }

    @NonNull
    public TGSocialConnections setConnectionsIds(List<String> connectionIds) {
        this.connectionIds = connectionIds;
        return this;
    }
}
