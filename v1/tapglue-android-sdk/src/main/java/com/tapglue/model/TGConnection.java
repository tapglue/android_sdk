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
import android.text.TextUtils;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tapglue.networking.TGCustomCacheObject.TGCacheObjectType;

public class TGConnection extends TGBaseObjectWithId<TGConnection, Long> {

    @Expose
    @SerializedName("confirmed_at")
    private String confirmationDate;

    @Expose
    @SerializedName("state")
    private String state;

    @Nullable
    @Expose
    @SerializedName("type")
    private String type;

    @Expose
    @SerializedName("user_from_id")
    private Long userFromId;

    @Expose
    @SerializedName("user_to_id")
    private Long userToId;


    /**
     * State of connection
     */
    public enum TGConnectionState {
        PENDING("pending"), CONFIRMED("confirmed"), REJECTED("rejected");

        private final String value;

        /**
         * Parse from string representation
         *
         * @param value string representation
         *
         * @return Parsed object, by default of type PENDING
         */
        @NonNull
        public static TGConnectionState fromString(String value) {
            for (TGConnectionState val : values()) {
                if (val.toString().equalsIgnoreCase(value)) { return val; }
            }
            return PENDING;
        }

        TGConnectionState(String value) {
            this.value = value;
        }

        /**
         * Get string representation
         *
         * @return String representation
         */
        @Override
        public String toString() {
            return value;
        }
    }

    /**
     * Type of connection
     */
    public enum TGConnectionType {
        FRIEND, FOLLOW;

        /**
         * Create object from string value
         *
         * @param value string representation of object
         *
         * @return Parsed object, by default of type Follow
         */
        @Nullable
        static public TGConnectionType fromString(@Nullable String value) {
            if(FRIEND.toString().equalsIgnoreCase(value)) return FRIEND;
            if(FOLLOW.toString().equalsIgnoreCase(value)) return FOLLOW;
            return null;
        }

        @Override
        public String toString() {
            switch (this) {
                case FRIEND:
                    return "friend";
                default:
                    return "follow";
            }
        }
    }

    public TGConnection() {
        super(TGCacheObjectType.Connection);
    }

    /**
     * Get connection state
     *
     * @return Connection state
     */
    @NonNull
    public TGConnectionState getState() {
        return TGConnectionState.fromString(state);
    }

    /**
     * Change connection state
     *
     * @param state
     *
     * @return Current object
     */
    @NonNull
    public TGConnection setState(@NonNull TGConnectionState state) {
        this.state = state.toString();
        return this;
    }

    @NonNull
    @Override
    protected TGConnection getThis() {
        return this;
    }

    /**
     * Get connection type
     *
     * @return connection type
     */
    @Nullable
    public TGConnectionType getType() {
        return TGConnectionType.fromString(type);
    }

    /**
     * Set connection type
     *
     * @param type Connection type
     *
     * @return Current object
     */
    @NonNull
    public TGConnection setType(@Nullable TGConnectionType type) {
        this.type = type == null ? null : type.toString();
        return this;
    }

    /**
     * Get ID of user which started connection
     *
     * @return ID of user
     */
    public Long getUserFromId() {
        return userFromId;
    }

    /**
     * Change ID of user that starts connection
     *
     * @param id ID of user
     *
     * @return Current object
     */
    @NonNull
    public TGConnection setUserFromId(Long id) {
        userFromId = id;
        return this;
    }

    /**
     * Get ID of user to which connection is made for
     *
     * @return ID of user
     */
    public Long getUserToId() {
        return userToId;
    }

    /**
     * Set ID user that connection is made for
     *
     * @param id ID of user
     *
     * @return Current object
     */
    @NonNull
    public TGConnection setUserToId(Long id) {
        userToId = id;
        return this;
    }
}
