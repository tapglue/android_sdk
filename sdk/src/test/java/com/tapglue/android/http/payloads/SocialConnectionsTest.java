package com.tapglue.android.http.payloads;

import com.google.gson.Gson;
import com.tapglue.android.entities.Connection;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;

public class SocialConnectionsTest {

    @Test
    public void generatesCorrectJson() {
        String platform = "platform";
        Connection.Type type = Connection.Type.FOLLOW;
        String userSocialId = "id1";
        List<String> socialIds = Arrays.asList("id4", "id5");

        String expectedJson = "{\"platform\":\"platform\",\"type\":\"follow\",\"platform_user_id\":\"id1\",\"connection_ids\":[\"id4\",\"id5\"]}";

        String actualJson = new Gson().toJson(new SocialConnections(platform, type, userSocialId, socialIds));

        assertThat(actualJson, equalTo(expectedJson));
    }
}