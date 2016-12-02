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

import java.util.List;
import java.util.Map;

public class Post {
    private String id;
    @SerializedName("visibility")
    private int visibility;
    @SerializedName("user_id")
    private String userId;
    private List<String> tags;
    private List<Attachment> attachments;
    private Counts counts;
    private String createdAt;
    private String updatedAt;
    @SerializedName("is_liked")
    private boolean isLiked;
    private User user;

    public Post(List<Attachment> attachments, Visibility visibility) {
        this.attachments = attachments;
        this.visibility = visibility.getVisibility();
    }

    public Visibility getVisibility() {
        return Visibility.convert(visibility);
    }

    public String getId() {
        return id;
    }

    public String getUserId() {
        return userId;
    }

    public User getUser() {
        return user;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public List<Attachment> getAttachments() {
        return attachments;
    }

    public Counts getCounts() {
        return counts;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public boolean isLiked() {
        return isLiked;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public enum Visibility {
        PRIVATE(10), CONNECTION(20), PUBLIC(30);

        private int rawVisibility;

        Visibility(int visibility) {
            this.rawVisibility = visibility;
        }

        private static Visibility convert(int raw) {
            switch(raw) {
                case 10:
                    return PRIVATE;
                case 20:
                    return CONNECTION;
                case 30:
                    return PUBLIC;
                default:
                    throw new IllegalArgumentException();
            }
        }
        private int getVisibility() {
            return rawVisibility;
        }
    }

    public static class Attachment {
       private Map<String, String> contents;
       private Type type;
       private String name;

       public Attachment(Map<String, String> contents, Type type, String name) {
            this.contents = contents;
            this.type = type;
            this.name = name;
       }

        public enum Type {
            @SerializedName("text")
            TEXT,
            @SerializedName("url")
            URL
        }

        public Map<String, String> getContents() {
            return contents;
        }

        public Type getType() {
            return type;
        }

        public String getName() {
            return name;
        }
    }

    public static class Counts {
        private long comments;
        private long likes;

        public long getLikes() {
            return likes;
        }

        public long getComments() {
            return comments;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Post post = (Post) o;

        return id != null ? id.equals(post.id) : post.id == null;

    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}