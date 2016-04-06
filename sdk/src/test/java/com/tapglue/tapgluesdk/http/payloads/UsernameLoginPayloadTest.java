package com.tapglue.tapgluesdk.http.payloads;

import com.google.gson.Gson;

import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;

/**
 * Created by John on 4/6/16.
 */
public class UsernameLoginPayloadTest {

    @Test
    public void constructsCorrectJson() {
        String expectedJson = "{\"user_name\":\"user\",\"password\":\"password\"}";
        String json = new Gson().toJson(new UsernameLoginPayload("user", "password"));
        assertThat(json, equalTo(expectedJson));
    }
}
