package com.tapglue.tapgluesdk.http;

import com.tapglue.tapgluesdk.Configuration;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by John on 3/30/16.
 */
@RunWith(MockitoJUnitRunner.class)
public class ServiceFactoryTest {

    @Mock
    Configuration configuration;

    //SUT
    ServiceFactory serviceFactory;

    @Before public void setup() {
        when(configuration.getBaseUrl()).thenReturn("https://someapi.tapglue.com");
        serviceFactory = new ServiceFactory(configuration);
    }

    @Test
    public void serviceForIsNotNull() {
        TapglueService service = serviceFactory.createTapglueService();
        assertThat(service, notNullValue());
    }

    @Test
    public void createServiceAsksForBaseUrl() {
        serviceFactory.createTapglueService();
        verify(configuration).getBaseUrl();
    }
}
