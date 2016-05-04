package com.tapglue.sdk.entities;

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
    private User user;

    public Post(Visibility visibility) {
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

    public void setUser(User user) {
        this.user = user;
    }

    public enum Visibility {
        PRIVATE(10), CONNECTION(20), PUBLIC(30);

        private int visibility;

        Visibility(int visibility) {
            this.visibility = visibility;
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