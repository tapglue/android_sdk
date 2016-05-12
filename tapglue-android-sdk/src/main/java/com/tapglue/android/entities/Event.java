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

package com.tapglue.android.entities;

import com.google.gson.annotations.SerializedName;

import java.util.Map;

public class Event {
    private String id;
    private String type;
    private String language;
    private String priority;
    private String location;
    private double latitude;
    private double longitude;
    private long visibility;
    private Map<String, Image> images;
    @SerializedName("user_id_string")
    private String userId;
    private User user;
    private String tgObjectId;
    private EventObject target;
    private EventObject object;
    public String getUserId() {
        return userId;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public String getLanguage() {
        return language;
    }

    public String getPriority() {
        return priority;
    }

    public String getLocation() {
        return location;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public long getVisibility() {
        return visibility;
    }

    public Map<String, Image> getImages() {
        return images;
    }

    public User getUser() {
        return user;
    }

    public String getTgObjectId() {
        return tgObjectId;
    }

    public EventObject getTarget() {
        return target;
    }

    public EventObject getObject() {
        return object;
    }

    public static class Image {
        private String type;
        private String url;
        private int height;
        private int width;

        public int getWidth() {
            return width;
        }

        public int getHeight() {
            return height;
        }

        public String getUrl() {
            return url;
        }

        public String getType() {
            return type;
        }
    }

    public static class EventObject {
        private String id;
        private String type;
        private String url;
        @SerializedName("display_name")
        private Map<String, String> displayNames;

        public Map<String, String> getDisplayNames() {
            return displayNames;
        }

        public String getUrl() {
            return url;
        }

        public String getType() {
            return type;
        }

        public String getId() {
            return id;
        }
    }
}
