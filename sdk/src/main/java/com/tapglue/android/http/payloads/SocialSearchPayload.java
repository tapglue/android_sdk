package com.tapglue.android.http.payloads;

import java.util.List;

public class SocialSearchPayload {
    List<String> ids;

    public SocialSearchPayload(List<String> ids) {
        this.ids = ids;
    }
}