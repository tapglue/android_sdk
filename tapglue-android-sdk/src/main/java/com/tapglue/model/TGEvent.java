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
import com.tapglue.Tapglue;
import com.tapglue.networking.TGCustomCacheObject;
import com.tapglue.networking.TGCustomCacheObject.TGCacheObjectType;

public class TGEvent extends TGBaseObjectWithId<TGEvent, Long> {

    @Expose
    @SerializedName("language")
    private String language;

    @Expose
    @SerializedName("latitude")
    private Float latitude;

    @Expose
    @SerializedName("location")
    private String location;

    @Expose
    @SerializedName("longitude")
    private Float longitude;

    @Expose
    @SerializedName("object")
    private TGEventObject object;

    @Expose
    @SerializedName("priority")
    private String priority;

    @Expose
    @SerializedName("target")
    private TGEventObject target;

    @Expose
    @SerializedName("type")
    private String type;

    @Expose
    @SerializedName("user_id")
    private Long userId;

    @Expose
    @SerializedName("visibility")
    private Integer visibility;

    @Expose
    @SerializedName("tg_object_id")
    private String objectId;

    public TGEvent() {
        super(TGCacheObjectType.Event);
    }

    /**
     * Constructor setting user data as current user if tapglue instance is provided
     *
     * @param tapglue Instance for current user data
     */
    public TGEvent(@Nullable Tapglue tapglue) {
        super(TGCacheObjectType.Event);
        if (tapglue != null && tapglue.getUserManager().getCurrentUser() != null) {
            userId = tapglue.getUserManager().getCurrentUser().getID();
        }
    }

    /**
     * Constructor copying all data from provided object, setting userID as current user if tapglue
     * instance is provided
     *
     * @param tapglue Instance for current user data
     * @param event   Object which values should be copied to current one
     */
    public TGEvent(@Nullable Tapglue tapglue, @NonNull TGEvent event) {
        super(TGCacheObjectType.Event);
        if (tapglue != null && tapglue.getUserManager().getCurrentUser() != null) {
            userId = tapglue.getUserManager().getCurrentUser().getID();
        }
        type = event.getType();
        setVisibility(event.getVisibility());
        language = event.getLanguage();
        priority = event.getPriority();
        location = event.getLocation();
        latitude = event.getLatitude();
        longitude = event.getLongitude();
        setMetadata(event.getMetadata());
        object = event.getObject();
        target = event.getTarget();
    }

    /**
     * Get language
     *
     * @return language
     */
    public String getLanguage() {
        return language;
    }

    /**
     * Set language
     *
     * @param newValue new language
     *
     * @return Current object
     */
    @NonNull
    public TGEvent setLanguage(String newValue) {
        language = newValue;
        return this;
    }

    /**
     * Get event latitude
     *
     * @return latitude
     */
    public Float getLatitude() {
        return latitude;
    }

    /**
     * Set event latitude
     *
     * @param newValue new latitude value
     *
     * @return Current object
     */
    @NonNull
    public TGEvent setLatitude(Float newValue) {
        latitude = newValue;
        return this;
    }

    /**
     * Get event location
     *
     * @return location
     */
    public String getLocation() {
        return location;
    }

    /**
     * Set event location
     *
     * @param newValue new location value
     *
     * @return Current object
     */
    @NonNull
    public TGEvent setLocation(String newValue) {
        location = newValue;
        return this;
    }

    /**
     * Get event longitude
     *
     * @return longitude
     */
    public Float getLongitude() {
        return longitude;
    }

    /**
     * Set longitude of event
     *
     * @param newValue new longitude value
     *
     * @return Current object
     */
    @NonNull
    public TGEvent setLongitude(Float newValue) {
        longitude = newValue;
        return this;
    }

    /**
     * Get object assigned to event
     *
     * @return object assigned to event
     */
    public TGEventObject getObject() {
        return object;
    }

    /**
     * Set object assigned to event
     *
     * @param newValue new object
     *
     * @return Current object
     */
    @NonNull
    public TGEvent setObject(TGEventObject newValue) {
        object = newValue;
        return this;
    }

    /**
     * Get object id
     *
     * @return
     */
    public String getObjectId() {
        return objectId;
    }

    /**
     * Set object id
     *
     * @param objectId
     *
     * @return
     */
    @NonNull
    public TGEvent setObjectId(String objectId) {
        this.objectId = objectId;
        return this;
    }

    public String getPriority() {
        return priority;
    }

    /**
     * Set event priority
     *
     * @param newValue new priority value
     *
     * @return Current object
     */
    @NonNull
    public TGEvent setPriority(String newValue) {
        priority = newValue;
        return this;
    }

    /**
     * Get event target
     *
     * @return event target
     */
    public TGEventObject getTarget() {
        return target;
    }

    /**
     * Set event target
     *
     * @param newValue new target value
     *
     * @return Current object
     */
    @NonNull
    public TGEvent setTarget(TGEventObject newValue) {
        target = newValue;
        return this;
    }

    @NonNull
    @Override
    protected TGEvent getThis() {
        return this;
    }

    /**
     * Get event type
     *
     * @return event type
     */
    public String getType() {
        return type;
    }

    /**
     * Set event type
     *
     * @param newValue
     *
     * @return Current object
     */
    @NonNull
    public TGEvent setType(String newValue) {
        type = newValue;
        return this;
    }

    /**
     * Get ID of owner
     *
     * @return ID of user (Owner)
     */
    public Long getUserId() {
        return userId;
    }

    /**
     * Get event visibility
     *
     * @return Event visibility
     */
    @NonNull
    public TGVisibility getVisibility() {
        return TGVisibility.fromValue(visibility);
    }

    /**
     * Change event visibility
     *
     * @param newValue new visibility value
     *
     * @return Current object
     */
    @NonNull
    public TGEvent setVisibility(@NonNull TGVisibility newValue) {
        visibility = newValue.asValue();
        return this;
    }
}
