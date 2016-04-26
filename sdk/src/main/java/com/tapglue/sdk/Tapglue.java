/*
 *  Copyright (c) 2015-2016 Tapglue (https://www.tapglue.com/). All rights reserved.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package com.tapglue.sdk;

import android.content.Context;

import com.tapglue.sdk.entities.Connection;
import com.tapglue.sdk.entities.User;

import java.io.IOException;
import java.util.List;

public class Tapglue {

    private RxTapglue rxTapglue;

    public Tapglue(Configuration configuration, Context context) {
        rxTapglue = new RxTapglue(configuration, context);
    }

    public User loginWithUsername(String username, String password) throws IOException {
        return new RxWrapper<User>().unwrap(rxTapglue.loginWithUsername(username, password));
    }

    public User loginWithEmail(String email, String password) throws IOException {
        return new RxWrapper<User>().unwrap(rxTapglue.loginWithEmail(email, password));
    }

    public void logout() throws IOException {
        new RxWrapper<Void>().unwrap(rxTapglue.logout());
    }

    public User getCurrentUser() throws IOException {
        return new RxWrapper<User>().unwrap(rxTapglue.getCurrentUser());
    }

    public User createUser(User user) throws IOException {
        return new RxWrapper<User>().unwrap(rxTapglue.createUser(user));
    }

    public void deleteCurrentUser() throws IOException {
        new RxWrapper<Void>().unwrap(rxTapglue.deleteCurrentUser());
    }

    public User updateCurrentUser(User updatedUser) throws IOException {
        return new RxWrapper<User>().unwrap(rxTapglue.updateCurrentUser(updatedUser));
    }

    public User refreshCurrentUser() throws IOException {
        return new RxWrapper<User>().unwrap(rxTapglue.refreshCurrentUser());
    }

    public User retrieveUser(String id) throws IOException {
        return new RxWrapper<User>().unwrap(rxTapglue.retrieveUser(id));
    }

    public List<User> retrieveFollowings() throws IOException {
        return new RxWrapper<List<User>>().unwrap(rxTapglue.retrieveFollowings());
    }

    public List<User> retrieveFollowers() throws IOException {
        return new RxWrapper<List<User>>().unwrap(rxTapglue.retrieveFollowers());
    }

    public List<User> retrieveFriends() throws IOException {
        return new RxWrapper<List<User>>().unwrap(rxTapglue.retrieveFriends());
    }

    public Connection createConnection(Connection connection) throws IOException {
        return new RxWrapper<Connection>().unwrap(rxTapglue.createConnection(connection));
    }
}
