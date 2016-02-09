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

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tapglue.networking.TGCustomCacheObject;
import com.tapglue.utils.TGPasswordHasher;

import java.util.HashMap;
import java.util.Map;

public class TGUser extends TGLoginUser<TGUser> {

    @Expose
    @SerializedName("images")
    HashMap<String, TGImage> images;

    @Expose
    @SerializedName("activated")
    private Boolean activated;

    @Expose
    @SerializedName("enabled")
    private Boolean enabled;

    @Expose
    @SerializedName("first_name")
    private String firstName;

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
    @SerializedName("last_login")
    private String lastLogin;

    @Expose
    @SerializedName("last_name")
    private String lastName;

    @Expose
    @SerializedName("custom_id")
    private String localId;

    @Expose
    @SerializedName("session_token")
    private String sessionToken;

    @Expose
    @SerializedName("social_ids")
    private Map<String, String> socialIds;

    @Expose
    @SerializedName("friend_count")
    private Long friendCount;

    @Expose
    @SerializedName("follower_count")
    private Long followerCount;

    @Expose
    @SerializedName("followed_count")
    private Long followedCount;

    @Expose
    @SerializedName("url")
    private String url;

    /**
     * This constructor should be used only by automated processes, not by developers
     */
    public TGUser() {
        super(null, null, "");
        cacheObjectType = TGCustomCacheObject.TGCacheObjectType.User.toCode();
    }

    public TGUser(String userName, String email, @NonNull String password) {
        super(userName, email, TGPasswordHasher.hashPassword(password));
        cacheObjectType = TGCustomCacheObject.TGCacheObjectType.User.toCode();
    }

    /**
     * Is user activated?
     *
     * @return Is user activated?
     */
    public Boolean getActivated() {
        return activated;
    }

    /**
     * Get user custom ID
     *
     * @return custom ID
     */
    public String getCustomId() {
        return localId;
    }

    /**
     * Get the followed count of the user
     *
     * @return
     */
    public Long getFollowedCount() {
        return followedCount;
    }

    /**
     * Get the follower count of the user
     *
     * @return
     */
    public Long getFollowerCount() {
        return followerCount;
    }

    /**
     * Get the friends count of the user
     *
     * @return
     */
    public Long getFriendCount() {
        return friendCount;
    }

    /**
     * Set user custom ID
     *
     * @param localId new custom ID value
     *
     * @return User
     */
    @NonNull
    public TGUser setCustomId(String localId) {
        this.localId = localId;
        return this;
    }

    /**
     * Is user enabled?
     *
     * @return is user enabled?
     */
    public Boolean getEnabled() {
        return enabled;
    }

    /**
     * Get user first name
     *
     * @return user first name
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Set user first name
     *
     * @param firstName new first name value
     *
     * @return User
     */
    @NonNull
    public TGUser setFirstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    /**
     * Get user images
     *
     * @return user images
     */
    public HashMap<String, TGImage> getImages() {
        return images;
    }

    /**
     * Set the followed count of the user
     * Does not save the information on the server
     *
     * @param followedCount
     * @return
     */
    public TGUser setFollowedCount(Long followedCount) {
        this.followedCount = followedCount;
        return this;
    }

    /**
     * Set the follower count of the user
     * Does not save the information on the server
     *
     * @param followerCount
     * @return
     */
    public TGUser setFollowerCount(Long followerCount) {
        this.followerCount = followerCount;
        return this;
    }

    /**
     * Set the friend count of the user
     * Does not save the information on the server
     *
     * @param friendCount
     * @return
     */
    public TGUser setFriendCount(Long friendCount) {
        this.friendCount = friendCount;
        return this;
    }

    /**
     * Set user images
     *
     * @param images new images value
     *
     * @return User
     */
    @NonNull
    public TGUser setImages(HashMap<String, TGImage> images) {
        this.images = images;
        return this;
    }

    /**
     * Get last login date taken from server
     *
     * @return Last login date
     */
    public String getLastLogin() {
        return lastLogin;
    }

    /**
     * Get user lasts name
     *
     * @return last name
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Set user last name
     *
     * @param lastName new user last name
     *
     * @return User
     */
    @NonNull
    public TGUser setLastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    /**
     * Get session token
     *
     * @return session token
     */
    public String getSessionToken() {
        return sessionToken;
    }

    /**
     * Get social networking ids
     *
     * @return List of social ids
     */
    public Map<String, String> getSocialIds() {
        return socialIds;
    }

    /**
     * Set social networking ids
     *
     * @param socialIds
     *
     * @return User
     */
    @NonNull
    public TGUser setSocialIds(Map<String, String> socialIds) {
        this.socialIds = socialIds;
        return this;
    }

    /**
     * Get url
     *
     * @return url
     */
    public String getUrl() {
        return url;
    }

    /**
     * Set user url
     *
     * @param url new url value
     *
     * @return
     */
    @NonNull
    public TGUser setUrl(String url) {
        this.url = url;
        return this;
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
     * Set user email
     *
     * @param email new email value
     *
     * @return User
     */
    @NonNull
    public TGUser setEmail(String email) {
        this.email = email;
        return this;
    }

    /**
     * Set user followed status.
     * This won't change anything on server, this is for app UI usage only
     *
     * @param isFollowed Is user followed by current user?
     *
     * @return User
     */
    @NonNull
    public TGUser setIsFollowed(boolean isFollowed) {
        this.isFollowed = isFollowed;
        return this;
    }

    /**
     * Set user friend status.
     * This won't change anything on server, this is for app UI usage only
     *
     * @param isFriend Is user followed by current user?
     *
     * @return User
     */
    @NonNull
    public TGUser setIsFriend(boolean isFriend) {
        this.isFriend = isFriend;
        return this;
    }

    /**
     * Set user password.
     * It will be hashed automatically
     *
     * @param password new password value
     *
     * @return User
     */
    @NonNull
    public TGUser setPassword(@NonNull String password) {
        this.password = TGPasswordHasher.hashPassword(password);
        return this;
    }

    /**
     * Set user password.
     * It won't be hashed.
     *
     * @param password new password value
     *
     * @return User
     */
    @NonNull
    public TGUser setUnhashedPassword(@NonNull String password) {
        this.password = password;
        return this;
    }

    /**
     * Set username
     *
     * @param userName new username value
     *
     * @return User
     */
    @NonNull
    public TGUser setUserName(String userName) {
        this.userName = userName;
        return this;
    }
}
