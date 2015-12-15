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

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tapglue.networking.TGCustomCacheObject;

public class TGConnectionUser extends TGBaseObjectWithId<TGConnectionUser> {


    @Expose
    @SerializedName("is_followed")
    private boolean isFollowed;
    @Expose
    @SerializedName("is_follower")
    private boolean isFollower;
    @Expose
    @SerializedName("is_friend")
    private boolean isFriend;
    @Expose
    @SerializedName("first_name")
    private String mFirstName;
    @Expose
    @SerializedName("last_name")
    private String mLastName;
    @Expose
    @SerializedName("user_name")
    private String mUserName;

    public TGConnectionUser() {
        super(TGCustomCacheObject.TGCacheObjectType.ConnectionUser);
    }

    /**
     * Get user first name
     *
     * @return First name
     */
    public String getFirstName() {
        return mFirstName;
    }

    /**
     * Get user last name
     *
     * @return Last name
     */
    public String getLastName() {
        return mLastName;
    }

    @Override
    protected TGConnectionUser getThis() {
        return this;
    }

    /**
     * Get username
     *
     * @return username
     */
    public String getUserName() {
        return mUserName;
    }

    /**
     * Information if user is followed by current user
     *
     * @return Is user followed by current user?
     */
    public boolean isFollowed() {
        return isFollowed;
    }

    /**
     * Information if user if follower of current user
     *
     * @return Is user follower of current user?
     */
    public boolean isFollower() {
        return isFollower;
    }

    /**
     * Information if user is friend of current user
     *
     * @return Is user friend of current user?
     */
    public boolean isFriend() {
        return isFriend;
    }

    /**
     * Set user followed status - it won't change anything on server, this is for app UI usage only
     *
     * @param newValue Is user followed by current user?
     *
     * @return Current object
     */
    public TGConnectionUser setIsFollowed(boolean newValue) {
        isFollowed = newValue;
        return this;
    }
}
