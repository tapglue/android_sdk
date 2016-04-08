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

package com.tapglue.tapgluesdk;

import com.tapglue.tapgluesdk.entities.User;

import rx.Observable;
import rx.functions.Func1;

public class UserStore {

    private User user;

    public Func1<User, User> store() {
        return new StoreUser(this);
    }

    public Observable<User> get() {
        return Observable.just(user);
    }

    void setUser(User user) {
        this.user = user;
    }

    private static class StoreUser implements Func1<User, User> {

        UserStore userStore;

        public StoreUser(UserStore userStore) {
            this.userStore = userStore;
        }

        @Override
        public User call(User user) {
            userStore.setUser(user);
            return user;
        }
    }
}
