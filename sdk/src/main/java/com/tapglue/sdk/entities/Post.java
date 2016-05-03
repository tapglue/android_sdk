package com.tapglue.sdk.entities;

import com.google.gson.annotations.SerializedName;

import java.util.List;
import java.util.Map;

public class Post {
    private String id;
    @SerializedName("visibility")
    private int visibilityInt;
    private transient Visibility visibility;
    @SerializedName("user_id")
    private String userId;
    private List<String> tags;
    private List<Attachment> attachments;
    private Counts counts;
    private String createdAt;
    private String updatedAt;

    public Post(Visibility visibility) {
        this.visibility = visibility;
        this.visibilityInt = visibility.getVisibility();
    }

    public Visibility getVisibility() {
        return visibility;
    }

    public String getId() {
        return id;
    }
    public enum Visibility {
        PRIVATE(10), CONNECTION(20), PUBLIC(30);

        private int visibility;

        private Visibility(int visibility) {
            this.visibility = visibility;
        }

        private int getVisibility() {
            return visibility;
        }
    }

    public static class Attachment {
        Map<String, String> contents;
        Type type;
        String name;

        public enum Type {
            TEXT, URL
        }
    }

    public static class Counts {
        private long comments;
        private long likes;
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