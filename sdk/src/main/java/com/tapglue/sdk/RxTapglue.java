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

import java.util.List;

import com.tapglue.sdk.entities.Connection;
import com.tapglue.sdk.entities.ConnectionList;
import com.tapglue.sdk.entities.User;
import com.tapglue.sdk.http.ServiceFactory;
import com.tapglue.sdk.http.payloads.SocialConnections;

import java.util.concurrent.atomic.AtomicBoolean;

import rx.Observable;
import rx.exceptions.OnErrorNotImplementedException;

public class RxTapglue {

    private static AtomicBoolean firstInstance = new AtomicBoolean(true);
    private Network network;
    private UserStore currentUser;

    public RxTapglue(Configuration configuration, Context context) {
        this.network = new Network(new ServiceFactory(configuration), context);
        this.currentUser = new UserStore(context);
        if(firstInstance.compareAndSet(true, false)) {
            try {
                network.sendAnalytics().subscribeOn(TapglueSchedulers.analytics()).subscribe();
            } catch(OnErrorNotImplementedException e) {
                firstInstance.set(true);
            }
        }
    }

    public Observable<User> loginWithUsername(String username, String password) {
        return network.loginWithUsername(username, password).map(currentUser.store());
    }

    public Observable<User> loginWithEmail(String email, String password) {
        return network.loginWithEmail(email, password).map(currentUser.store());
    }

    public Observable<Void> logout() {
        return network.logout().doOnCompleted(currentUser.clear());
    }

    public Observable<User> getCurrentUser() {
        return currentUser.get();
    }

    public Observable<User> createUser(User user) {
        return network.createUser(user);
    }

    public Observable<Void> deleteCurrentUser() {
        return network.deleteCurrentUser().doOnCompleted(currentUser.clear());
    }

    public Observable<User> updateCurrentUser(User user) {
        return network.updateCurrentUser(user).map(currentUser.store());
    }

    public Observable<User> refreshCurrentUser() {
        return network.refreshCurrentUser().map(currentUser.store());
    }

    public Observable<User> retrieveUser(String id) {
        return network.retrieveUser(id);
    }

    public Observable<List<User>> retrieveFollowings() {
        return network.retrieveFollowings();
    }

    public Observable<List<User>> retrieveFollowers() {
        return network.retrieveFollowers();
    }

    public Observable<List<User>> retrieveFriends() {
        return network.retrieveFriends();
    }

    public Observable<ConnectionList> retrievePendingConnections() {
        return network.retrievePendingConnections();
    }

    public Observable<ConnectionList> retrieveRejectedConnections() {
        return network.retrieveRejectedConnections();
    }

    public Observable<Connection> createConnection(Connection connection) {
        return network.createConnection(connection);
    }

    public Observable<List<User>> createSocialConnections(SocialConnections connections) {
        return network.createSocialConnections(connections);
    }

    public Observable<List<User>> searchUsers(String searchTerm) {
        return network.searchUsers(searchTerm);
    }

    public Observable<List<User>> searchUsersByEmail(List<String> emails) {
        return network.searchUsersByEmail(emails);
    }
}
