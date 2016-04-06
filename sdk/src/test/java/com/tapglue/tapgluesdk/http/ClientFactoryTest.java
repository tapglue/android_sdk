package com.tapglue.tapgluesdk.http;

import com.tapglue.tapgluesdk.Configuration;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;

import static org.hamcrest.CoreMatchers.isA;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsCollectionContaining.hasItem;
import static org.mockito.Mockito.when;

/**
 * Created by John on 4/6/16.
 */
@RunWith(MockitoJUnitRunner.class)
public class ClientFactoryTest {

    private static final String SAMPLE_ENDPOINT = "https://api.tapglue.com";
    private static final String SAMPLE_TOKEN = "sampleToken";

    @Mock
    Configuration configuration;

    @Before
    public void setUp() {
        when(configuration.getBaseUrl()).thenReturn(SAMPLE_ENDPOINT);
        when(configuration.getToken()).thenReturn(SAMPLE_TOKEN);
    }

    @Test
    public void addsHeaderInterceptor() {
        OkHttpClient client = ClientFactory.createClient(configuration);
        assertThat(client.interceptors(), hasItem(isA(HeaderInterceptor.class)));
    }

    @Test
    public void addsLoggingInterceptor() {
        OkHttpClient client = ClientFactory.createClient(configuration);
        assertThat(client.interceptors(), hasItem(isA(HttpLoggingInterceptor.class)));
    }
}
