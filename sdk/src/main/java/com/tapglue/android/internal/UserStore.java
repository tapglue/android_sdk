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

package com.tapglue.android.internal;

import android.content.Context;
import android.content.SharedPreferences;

import com.tapglue.android.entities.User;

import rx.Observable;
import rx.functions.Action0;
import rx.functions.Func1;

public class UserStore {

    private static final String USER_TAG = "user";

    Store<User> store;

    public UserStore(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(USER_TAG, Context.MODE_PRIVATE);
        store = new Store(prefs, User.class);
    }

    public Func1<User, User> store() {
        return store.store();
    }

    public Observable<User> get() {
        return store.get();
    }

    public Action0 clear() {
        return store.clear();
    }
}
