package com.tapglue.tapgluesdk.http.payloads;

/**
 * Created by John on 4/6/16.
 */
public class EmailLoginPayload {

    private final String email;
    private final String password;

    public EmailLoginPayload(String email, String password) {
        this.email = email;
        this.password = password;
    }
}
