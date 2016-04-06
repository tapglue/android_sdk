package com.tapglue.tapgluesdk;

import com.tapglue.tapgluesdk.entities.User;
import com.tapglue.tapgluesdk.http.ServiceFactory;
import com.tapglue.tapgluesdk.http.TapglueService;
import com.tapglue.tapgluesdk.http.payloads.EmailLoginPayload;
import com.tapglue.tapgluesdk.http.payloads.UsernameLoginPayload;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import rx.Observable;
import rx.observers.TestSubscriber;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsCollectionContaining.hasItems;
import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class NetworkTest {
    private static final String EMAIL = "user@domain.com";
	private static final String USERNAME = "username";
	private static final String PASSWORD = "password";

	@Mock
	ServiceFactory serviceFactory;
	@Mock
	TapglueService service;
    @Mock
    User user;

    //SUT
    Network network;

    @Before
	public void setUp() {
		when(serviceFactory.createTapglueService()).thenReturn(service);
        network = new Network(serviceFactory);
	}

	@Test
	public void loginWithUsernameReturnsUserFromService() {
		when(service.login(isA(UsernameLoginPayload.class))).thenReturn(Observable.just(user));
        TestSubscriber<User> ts = new TestSubscriber<>();

        network.loginWithUsername(USERNAME, PASSWORD).subscribe(ts);

        assertThat(ts.getOnNextEvents(), hasItems(user));
	}

    @Test
    public void loginWithEmailReturnsUserFromService() {
        when(service.login(isA(EmailLoginPayload.class))).thenReturn(Observable.just(user));
        TestSubscriber<User> ts = new TestSubscriber<>();

        network.loginWithEmail(EMAIL, PASSWORD).subscribe(ts);

        assertThat(ts.getOnNextEvents(), hasItems(user));
    }
}