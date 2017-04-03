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

package com.tapglue.android.internal.rx2;

import android.content.Context;
import android.content.SharedPreferences;

import com.tapglue.android.entities.User;

import io.reactivex.Observable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Function;

public class SessionStore {

    Store<User> store;

    public SessionStore(Context context) {
        SharedPreferences prefs = context.getSharedPreferences("sessionToken", Context.MODE_PRIVATE);
        store = new Store<>(prefs, User.class);
    }

    public Observable<User> get() {
        return store.get();
    }

    public Function<User, User> store() {
        return store.store();
    }

    public Action clear() {
        return store.clear();
    }
}