package com.tapglue.tapgluesdk;

import com.tapglue.tapgluesdk.entities.Configuration;
import com.tapglue.tapgluesdk.entities.User;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import rx.Observable;
import rx.observers.TestSubscriber;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.core.IsCollectionContaining.hasItems;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

/**
 * Created by John on 3/29/16.
 */
@RunWith(MockitoJUnitRunner.class)
public class TapglueTest {
    @Mock Configuration configuration;
    @Mock Network network;
    @Mock User user;


    private static final String TOKEN = "sampleToken";
    private static final String BASE_URL = "https://api.tapglue.com";
    private static final String USERNAME = "username";
    private static final String PASSWORD = "password";

    //SUT
    Tapglue tapglue;

    @Before public void setUp() {
        when(configuration.getToken()).thenReturn(TOKEN);
        when(configuration.getBaseUrl()).thenReturn(BASE_URL);
        tapglue = new Tapglue(configuration);
    }

    @Test public void tokenCorrectlyUp() {
        assertThat(tapglue.getToken(), equalTo(TOKEN));
    }

    @Test public void loginCallsNetwork() {
        when(network.login(USERNAME, PASSWORD)).thenReturn(Observable.just(user));
        Tapglue tapglue = new Tapglue(configuration);
        tapglue.network = network;
        TestSubscriber<User> ts = new TestSubscriber<>();

        tapglue.loginWithUsername(USERNAME, PASSWORD).subscribe(ts);

        assertThat(ts.getOnNextEvents(), hasItems(user));
    }
}
