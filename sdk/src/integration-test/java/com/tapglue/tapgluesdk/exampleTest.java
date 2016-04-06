package com.tapglue.tapgluesdk;

import com.tapglue.tapgluesdk.Tapglue;

import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;

/**
 * Created by John on 3/29/16.
 */
public class exampleTest {

    @Test
    public void failingTest(){
        assertThat(true, equalTo(true));
    }
}
