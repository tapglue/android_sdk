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

public class TGConnection extends TGBaseObjectWithId<TGConnection, Long> {

    @Expose
    @SerializedName("confirmed_at")
    private String mConfirmationDate;

    @Expose
    @SerializedName("state")
    private String mState;

    @Nullable
    @Expose
    @SerializedName("type")
    private String mType;

    @Expose
    @SerializedName("user_from_id")
    private Long mUserFromId;

    @Expose
    @SerializedName("user_to_id")
    private Long mUserToId;


    /**
     * State of connection
     */
    public enum TGConnectionState {
        PENDING("pending"), CONFIRMED("confirmed"), REJECTED("rejected");

        private final String mValue;

        /**
         * Parse from string representation
         *
         * @param val string representation
         *
         * @return Parsed object, by default of type PENDING
         */
        @NonNull
        public static TGConnectionState fromString(String val) {
            for (TGConnectionState value : values()) {
                if (value.toString().equalsIgnoreCase(val)) { return value; }
            }
            return PENDING;
        }

        TGConnectionState(String value) {
            mValue = value;
        }

        /**
         * Get string representation
         *
         * @return String representation
         */
        @Override
        public String toString() {
            return mValue;
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
         * @param txt string representation of object
         *
         * @return Parsed object, by default of type Follow
         */
        @NonNull
        static public TGConnectionType fromString(@Nullable String txt) {
            if (txt == null) return FOLLOW;
            if (txt.equalsIgnoreCase(FRIEND.toString())) return FRIEND;
            return FOLLOW;
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
        super(TGCustomCacheObject.TGCacheObjectType.Connection);
    }

    /**
     * Get connection state
     *
     * @return Connection state
     */
    @NonNull
    public TGConnectionState getState() {
        return TGConnectionState.fromString(mState);
    }

    /**
     * Change connection state
     *
     * @param pending
     *
     * @return Current object
     */
    @NonNull
    public TGConnection setState(@NonNull TGConnectionState pending) {
        mState = pending.toString();
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
        return TGConnectionType.fromString(mType);
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
        mType = type != null ? type.toString() : null;
        return this;
    }

    /**
     * Get ID of user which started connection
     *
     * @return ID of user
     */
    public Long getUserFromId() {
        return mUserFromId;
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
        mUserFromId = id;
        return this;
    }

    /**
     * Get ID of user to which connection is made for
     *
     * @return ID of user
     */
    public Long getUserToId() {
        return mUserToId;
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
        mUserToId = id;
        return this;
    }
}
