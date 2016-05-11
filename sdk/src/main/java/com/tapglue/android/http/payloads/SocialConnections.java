/*
 *  Copyright (c) 2015-2016 Tapglue (https://www.tapglue.com/). All rights reserved.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package com.tapglue.android.http.payloads;

import java.util.List;

import com.google.gson.annotations.SerializedName;

import static com.tapglue.android.entities.Connection.Type;

public class SocialConnections {
    @SerializedName("platform")
    private final String platform;
    @SerializedName("type")
    private final Type type;
    @SerializedName("platform_user_id")
    private final String userSocialId;
    @SerializedName("connection_ids")
    private final List<String> socialIds;

    public SocialConnections(String platform, Type type, String userSocialId, List<String> socialIds) {
        this.platform = platform;
        this.type = type;
        this.userSocialId = userSocialId;
        this.socialIds = socialIds;
    }
}