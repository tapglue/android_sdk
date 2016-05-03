package com.tapglue.sdk.http.payloads;

import java.util.List;

public class EmailSearchPayload {
    List<String> emails;

    public EmailSearchPayload(List<String> emails) {
        this.emails = emails;
    }

}