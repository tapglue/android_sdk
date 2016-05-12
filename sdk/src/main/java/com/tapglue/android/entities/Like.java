package com.tapglue.android.entities;

import com.google.gson.annotations.SerializedName;

public class Like {
    private String id;
    @SerializedName("post_id")
    private String postId;
    @SerializedName("user_id")
    private String userId;
    private String createdAt;
    private String updatedAt;
    private User user;

    public void setUser(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    public String getUserId() {
        return userId;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Like like = (Like) o;

        return id != null ? id.equals(like.id) : like.id == null;

    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}