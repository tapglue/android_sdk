package com.tapglue.tapgluesdk;

import android.app.Application;
import android.test.ApplicationTestCase;

import com.tapglue.tapgluesdk.entities.User;

import rx.Observable;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class TapglueIntegrationTest extends ApplicationTestCase<Application> {

    Configuration configuration;
    Tapglue tapglue;

    public TapglueIntegrationTest() {
        super(Application.class);
        configuration = new Configuration();
        configuration.setToken("1ecd50ce4700e0c8f501dee1fb271344");
        tapglue = new Tapglue(configuration);
    }

    public void testLoginWithUsername() {
        IntegrationObserver<User> ts = new IntegrationObserver<User>() {
            @Override
            public void onNext(User user) {
                assertThat(user.getEmail(), equalTo("john@text.com"));
            }
        };

        tapglue.loginWithUsername("john", PasswordHasher.hashPassword("qwert"))
                .toBlocking().subscribe(ts);
    }

    public void testLoginWithEmail() {
        IntegrationObserver<User> ts = new IntegrationObserver<User>() {
            @Override
            public void onNext(User user) {
                assertThat(user.getEmail(), equalTo("john@text.com"));
            }
        };
        tapglue.loginWithEmail("john@text.com", PasswordHasher.hashPassword("qwert"))
                .toBlocking().subscribe(ts);
    }

    public void testLogout() {
        final IntegrationObserver<Void> ts = new IntegrationObserver<Void>() {
            @Override
            public void onNext(Void aVoid) {

            }
        };

        tapglue.loginWithUsername("john", PasswordHasher.hashPassword("qwert"))
            .subscribe();

        tapglue.logout().subscribe(ts);
    }

    public void testCurrentUserSetAfterLogin() {
        tapglue.loginWithUsername("john", PasswordHasher.hashPassword("qwert"))
                .toBlocking().subscribe();

        IntegrationObserver<User> ts = new IntegrationObserver<User>() {
            @Override
            public void onNext(User user) {
                assertThat(user.getEmail(), equalTo("john@text.com"));
            }
        };

        tapglue.getCurrentUser().subscribe(ts);
    }
}