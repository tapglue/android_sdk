package com.tapglue.tapgluesdk;

import com.tapglue.tapgluesdk.entities.User;
import com.tapglue.tapgluesdk.http.ServiceFactory;
import com.tapglue.tapgluesdk.http.TapglueService;

import rx.Observable;

/**
 * Created by John on 3/29/16.
 */
class Network {

    TapglueService service;

    public Network(ServiceFactory serviceFactory) {
        service = serviceFactory.createTapglueService();
    }

    public Observable<User> login(String username, String password) {
        LoginPayload payload = new LoginPayload(username, password);
        return service.login(payload);
    }
}
