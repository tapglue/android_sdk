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

package com.tapglue.sdk.entities;

import com.google.gson.annotations.SerializedName;

public class Connection {
    @SerializedName("user_to_id_string")
    private final String userToId;
    @SerializedName("type")
    private final String type;
    @SerializedName("state")
    private final String state;

    public Connection(User user, Type type, State state) {
        this.userToId = user.getId();
        this.type = type.toString().toLowerCase();
        this.state = state.toString().toLowerCase();
    }

    public static enum Type {
        FOLLOW, FRIEND
    }
    public static enum State {
        PENDING, CONFIRMED, REJECTED
    }
}