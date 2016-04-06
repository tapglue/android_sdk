package com.tapglue.tapgluesdk;

import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;

/**
 * Created by John on 3/29/16.
 */
public class TapglueIntegrationTest {

    @Test
    public void failingTest(){
        Tapglue tapglue = new Tapglue();
        assertThat(true, equalTo(false));
    }
}
