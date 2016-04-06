package com.tapglue.tapgluesdk;

import com.tapglue.tapgluesdk.entities.User;
import com.tapglue.tapgluesdk.http.ServiceFactory;
import com.tapglue.tapgluesdk.http.TapglueService;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import rx.Observable;
import rx.observers.TestSubscriber;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsCollectionContaining.hasItems;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class NetworkTest {

	private static final String USERNAME = "username";
	private static final String PASSWORD = "password";

	@Mock
	ServiceFactory serviceFactory;
	@Mock
	TapglueService service;

	@Before public void setUp() {
		when(serviceFactory.createTapglueService()).thenReturn(service);
	}

	@Test public void loginReturnsUserFromNetwork() {
        User user = new User();
		when(service.login(isA(LoginPayload.class))).thenReturn(Observable.just(user));
		Network network = new Network(serviceFactory);

        TestSubscriber<User> ts = new TestSubscriber<>();
        network.login(USERNAME, PASSWORD).subscribe(ts);
        assertThat(ts.getOnNextEvents(), hasItems(user));
	}
}