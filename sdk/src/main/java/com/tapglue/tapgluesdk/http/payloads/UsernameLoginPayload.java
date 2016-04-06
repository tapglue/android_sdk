package com.tapglue.tapgluesdk.http.payloads;

import com.google.gson.annotations.SerializedName;

/**
 * Created by John on 3/30/16.
 */
public class UsernameLoginPayload {
    @SerializedName("user_name")
    private final String username;
    private final String password;

    public UsernameLoginPayload(String username, String password) {
        this.username = username;
        this.password = password;
    }
}
