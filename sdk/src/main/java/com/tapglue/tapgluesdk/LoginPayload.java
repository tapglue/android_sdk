package com.tapglue.tapgluesdk;

import com.google.gson.annotations.SerializedName;

/**
 * Created by John on 3/30/16.
 */
public class LoginPayload {
    @SerializedName("user_name")
    String userName;
    String password;

    public LoginPayload(String email, String password) {
        this.userName = email;
        this.password = password;
    }
}
