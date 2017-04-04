package com.tapglue.android.http.payloads;

import com.google.gson.Gson;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;

public class EmailSearchPayloadTest {

    @Test
    public void payloadProducesCorrectPayload() {
        String expectedJson = "{\"emails\":[\"email1\",\"email2\"]}";

        List<String> emails = Arrays.asList("email1", "email2");
        String actualJson = new Gson().toJson(new EmailSearchPayload(emails));

        assertThat(actualJson, equalTo(expectedJson));
    }
}