/**
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
package com.tapglue.sdk.entities;

import java.util.Map;

import com.google.gson.annotations.SerializedName;

public class User {
    @SerializedName("id_string")
    private String id;
    @SerializedName("social_ids")
    private Map<String, String> socialIds;
    @SerializedName("friend_count")
    private long friendCount;
    @SerializedName("follower_count")
    private long followerCount;
    @SerializedName("followed_count")
    private long followedCount;
    @SerializedName("user_name")
    private String username;
    @SerializedName("password")
    private String password;
    @SerializedName("first_name")
    private String firstName;
    @SerializedName("last_name")
    private String lastName;
    @SerializedName("last_login")
    private String lastLogin;
    @SerializedName("session_token")
    private String sessionToken;
    boolean enabled = true;
    private String email;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getId() {
        return id;
    }

    public String getSessionToken() {
        return sessionToken;
    }

    public Map<String,String> getSocialIds() {
        return socialIds;
    }

    public void setSocialIds(Map<String,String> socialIds) {
        this.socialIds = socialIds;
    }

    public String getUserName() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        return id.equals(user.id);

    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}