/**
 * Copyright (c) 2015-2016 Tapglue (https://www.tapglue.com/). All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
package com.tapglue.sdk;

import android.content.Context;

import com.tapglue.sdk.entities.User;
import com.tapglue.sdk.http.ServiceFactory;
import com.tapglue.sdk.http.TapglueService;
import com.tapglue.sdk.http.UsersFeed;
import com.tapglue.sdk.http.payloads.EmailLoginPayload;
import com.tapglue.sdk.http.payloads.UsernameLoginPayload;

import java.util.List;
import java.util.ArrayList;

import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func1;

class Network {

    TapglueService service;
    private ServiceFactory serviceFactory;
    private SessionStore sessionStore;
    private UUIDStore uuidStore;

    public Network(ServiceFactory serviceFactory, Context context) {
        this.serviceFactory = serviceFactory;
        service = serviceFactory.createTapglueService();
        sessionStore = new SessionStore(context);
        uuidStore = new UUIDStore(context);
        uuidStore.get().doOnNext(new UUIDAction()).subscribe();
        sessionStore.get().map(new SessionTokenExtractor());
    }

    public Observable<User> loginWithUsername(String username, String password) {
        UsernameLoginPayload payload = new UsernameLoginPayload(username, password);
        return service.login(payload).map(new SessionTokenExtractor()).map(sessionStore.store());
    }

    public Observable<User> loginWithEmail(String email, String password) {
        EmailLoginPayload payload = new EmailLoginPayload(email, password);
        return service.login(payload).map(new SessionTokenExtractor()).map(sessionStore.store());
    }

    public Observable<Void> logout() {
        return service.logout().doOnCompleted(sessionStore.clear());
    }

    public Observable<User> createUser(User user) {
        return service.createUser(user);
    }

    public Observable<Void> deleteCurrentUser() {
        return service.deleteCurrentUser().doOnCompleted(sessionStore.clear());
    }

    public Observable<User> updateCurrentUser(User user) {
        return service.updateCurrentUser(user).map(new SessionTokenExtractor()).map(sessionStore.store());
    }

    public Observable<User> retrieveUser(String id) {
        return service.retrieveUser(id);
    }

    public Observable<User> refreshCurrentUser() {
        return service.refreshCurrentUser().map(new SessionTokenExtractor()).map(sessionStore.store());
    }

    public Observable<List<User>> retrieveFollowings() {
        return service.retrieveFollowings().map(new UsersExtractor());
    }

    public Observable<List<User>> retrieveFollowers() {
        return service.retrieveFollowers().map(new UsersExtractor());
    }

    public Observable<List<User>> retrieveFriends() {
        return service.retrieveFriends().map(new UsersExtractor());
    }

    public Observable<Void> sendAnalytics() {
        return service.sendAnalytics();
    }

    private class SessionTokenExtractor implements Func1<User, User> {

        @Override
        public User call(User user) {
            serviceFactory.setSessionToken(user.getSessionToken());
            service = serviceFactory.createTapglueService();
            return user;
        }
    }

    private class UsersExtractor implements Func1<UsersFeed, List<User>> {
        @Override
        public List<User> call(UsersFeed feed) {
            if(feed == null || feed.getUsers() == null) {
                return new ArrayList<User>();
            }
            return feed.getUsers();
        }
    }

    private class UUIDAction implements Action1<String> {

        @Override
        public void call(String uuid) {
            serviceFactory.setUserUUID(uuid);
            service = serviceFactory.createTapglueService();
        }
    }
}
