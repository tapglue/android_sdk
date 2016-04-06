package com.tapglue.tapgluesdk.entities;

import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;

/**
 * Created by John on 4/4/16.
 */
public class ConfigurationTest {

    private static final String DEFAULT_URL = "https://api.tapglue.com";

    @Test public void configurationHasCorrectDefaultURL() {
        Configuration configuration = new Configuration();
        assertThat(configuration.getBaseUrl(), equalTo(DEFAULT_URL));
    }
}
