package com.tapglue.tapgluesdk;

import com.tapglue.tapgluesdk.entities.User;
import com.tapglue.tapgluesdk.http.ServiceFactory;
import com.tapglue.tapgluesdk.http.TapglueService;
import com.tapglue.tapgluesdk.http.payloads.EmailLoginPayload;
import com.tapglue.tapgluesdk.http.payloads.UsernameLoginPayload;

import rx.Observable;

/**
 * Created by John on 3/29/16.
 */
class Network {

    TapglueService service;

    public Network(ServiceFactory serviceFactory) {
        service = serviceFactory.createTapglueService();
    }

    public Observable<User> loginWithUsername(String username, String password) {
        UsernameLoginPayload payload = new UsernameLoginPayload(username, password);
        return service.login(payload);
    }

    public Observable<User> loginWithEmail(String email, String password) {
        EmailLoginPayload payload = new EmailLoginPayload(email, password);
        return service.login(payload);
    }
}
