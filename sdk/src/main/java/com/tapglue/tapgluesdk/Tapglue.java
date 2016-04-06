package com.tapglue.tapgluesdk;

import com.tapglue.tapgluesdk.entities.Configuration;
import com.tapglue.tapgluesdk.entities.User;
import com.tapglue.tapgluesdk.http.ServiceFactory;

import rx.Observable;

/**
 * Created by John on 3/29/16.
 */
public class Tapglue {

    Configuration configuration;
    Network network;

    public Tapglue(Configuration configuration) {
        this.configuration = configuration;
        this.network = new Network(new ServiceFactory(configuration));
    }

    public Observable<User> loginWithUsername(String username, String password) {
        return network.login(username, password);
    }

    public String getToken() {
        return configuration.getToken();
    }
}
