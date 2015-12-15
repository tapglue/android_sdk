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

import android.support.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tapglue.Tapglue;
import com.tapglue.networking.TGCustomCacheObject;

public class TGEvent extends TGBaseObjectWithId<TGEvent> {

    @Expose
    @SerializedName("language")
    private String mLanguage;
    @Expose
    @SerializedName("latitude")
    private Float mLatitude;
    @Expose
    @SerializedName("mLocation")
    private String mLocation;
    @Expose
    @SerializedName("longitude")
    private Float mLongitude;
    @Expose
    @SerializedName("metadata")
    private String mMetadata;
    @Expose
    @SerializedName("object")
    private TGEventObject mObject;
    @Expose
    @SerializedName("mPriority")
    private String mPriority;
    @Expose
    @SerializedName("target")
    private TGEventObject mTarget;
    @Expose
    @SerializedName("type")
    private String mType;
    @Expose
    @SerializedName("user_id")
    private Long mUserId;
    @Expose
    @SerializedName("visibility")
    private Integer mVisibility;


    public enum TGEventVisibility {
        Private(10), Connections(20), Public(30), Global(40);

        private final int mValue;

        public static TGEventVisibility fromValue(Integer valueToFind) {
            if (valueToFind == null) { return Private; }
            for (TGEventVisibility value : values()) {
                if (value.asValue().intValue() == valueToFind.intValue()) { return value; }
            }
            return Private;
        }

        TGEventVisibility(int realValue) {
            mValue = realValue;
        }

        public Integer asValue() {
            return mValue;
        }
    }

    public TGEvent() {
        super(TGCustomCacheObject.TGCacheObjectType.Event);
    }

    /**
     * Constructor setting user data as current user if tapglue instance is provided
     *
     * @param tapglue Instance for current user data
     */
    public TGEvent(Tapglue tapglue) {
        super(TGCustomCacheObject.TGCacheObjectType.Event);
        if (tapglue != null && tapglue.getUserManager().getCurrentUser() != null) {
            mUserId = tapglue.getUserManager().getCurrentUser().getID();
        }
    }

    /**
     * Constructor copying all data from provided object, setting userID as current user if tapglue
     * instance is provided
     *
     * @param tapglue Instance for current user data
     * @param event   Object which values should be copied to current one
     */
    public TGEvent(Tapglue tapglue, TGEvent event) {
        super(TGCustomCacheObject.TGCacheObjectType.Event);
        if (tapglue != null && tapglue.getUserManager().getCurrentUser() != null) {
            mUserId = tapglue.getUserManager().getCurrentUser().getID();
        }
        mType = event.getType();
        setVisibility(event.getVisibility());
        mLanguage = event.getLanguage();
        mPriority = event.getPriority();
        mLocation = event.getLocation();
        mLatitude = event.getLatitude();
        mLongitude = event.getLongitude();
        mMetadata = event.getMetadata();
        mObject = event.getObject();
        mTarget = event.getTarget();
    }

    /**
     * Get language
     *
     * @return language
     */
    public String getLanguage() {
        return mLanguage;
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
        mLanguage = newValue;
        return this;
    }

    /**
     * Get event latitude
     *
     * @return latitude
     */
    public Float getLatitude() {
        return mLatitude;
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
        mLatitude = newValue;
        return this;
    }

    /**
     * Get event location
     *
     * @return location
     */
    public String getLocation() {
        return mLocation;
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
        mLocation = newValue;
        return this;
    }

    /**
     * Get event longitude
     *
     * @return longitude
     */
    public Float getLongitude() {
        return mLongitude;
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
        mLongitude = newValue;
        return this;
    }

    /**
     * Get metadata Metadata can contain more complex objects, all that is needed to support it is
     * serialization and deserialization of them to json/xml/custom string format
     *
     * @return metadata
     */
    public String getMetadata() {
        return mMetadata;
    }

    /**
     * Set metadata Metadata can contain more complex objects, all that is needed to support it is
     * serialization and deserialization of them to json/xml/custom string format
     *
     * @param newValue new metadata value
     *
     * @return Current object
     */
    @NonNull
    public TGEvent setMetadata(String newValue) {
        mMetadata = newValue;
        return this;
    }

    /**
     * Get object assigned to event
     *
     * @return object assigned to event
     */
    public TGEventObject getObject() {
        return mObject;
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
        mObject = newValue;
        return this;
    }

    public String getPriority() {
        return mPriority;
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
        mPriority = newValue;
        return this;
    }

    /**
     * Get event target
     *
     * @return event target
     */
    public TGEventObject getTarget() {
        return mTarget;
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
        mTarget = newValue;
        return this;
    }

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
        return mType;
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
        mType = newValue;
        return this;
    }

    /**
     * Get ID of owner
     *
     * @return ID of user (Owner)
     */
    public Long getUserId() {
        return mUserId;
    }

    /**
     * Get event visibility
     *
     * @return Event visibility
     */
    public TGEventVisibility getVisibility() {
        return TGEventVisibility.fromValue(mVisibility);
    }

    /**
     * Change event visibility
     *
     * @param newValue new visibility value
     *
     * @return Current object
     */
    @NonNull
    public TGEvent setVisibility(TGEventVisibility newValue) {
        mVisibility = newValue.asValue();
        return this;
    }
}
