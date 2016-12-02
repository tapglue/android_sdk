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
package com.tapglue.android.entities;

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
    @SerializedName("is_friend")
    private boolean isFriend;
    @SerializedName("is_follower")
    private boolean isFollower;
    @SerializedName("is_followed")
    private boolean isFollowed;
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
    private String about;
    private Map<String, Image> images;
    private Map<String, String> metadata;

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

    public long getFriendCount() {
        return friendCount;
    }

    public long getFollowerCount() {
        return followerCount;
    }

    public long getFollowedCount() {
        return followedCount;
    }

    public boolean isFriend() {
        return isFriend;
    }

    public boolean isFollower() {
        return isFollower;
    }

    public boolean isFollowed() {
        return isFollowed;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getLastLogin() {
        return lastLogin;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public Map<String, Image> getImages() {
        return images;
    }

    public void setImages(Map<String, Image> images) {
        this.images = images;
    }

    public Map<String, String> getMetadata() {
        return metadata;
    }

    public void setMetadata(Map<String, String> metadata) {
        this.metadata = metadata;
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

    public static class Image {
        private String url;
        private int height;
        private int width;

        public Image(String url, int height, int width) {
            this.url = url;
            this.height = height;
            this.width = width;
        }

        public String getUrl() {
            return url;
        }

        public int getHeight() {
            return height;
        }

        public int getWidth() {
            return width;
        }
    }
}
