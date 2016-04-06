package com.tapglue.tapgluesdk.entities;

/**
 * Created by John on 3/29/16.
 */
public class Configuration {

    private String token;

    public String getToken() {
        return token;
    }

    public String getBaseUrl() {
        return "https://api.tapglue.com";
    }

    public void setToken(String token) {
        this.token = token;
    }
}
