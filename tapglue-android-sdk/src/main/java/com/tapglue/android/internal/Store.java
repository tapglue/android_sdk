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

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.google.gson.Gson;

import rx.Observable;
import rx.functions.Action0;
import rx.functions.Func1;

public class Store<T> {
    private static final String TAG = "object";
    SharedPreferences prefs;
    Class<T> cls;
    T obj;

    Store(SharedPreferences prefs, Class<T> cls) {
        this.prefs = prefs;
        this.cls = cls;
    }

    Func1<T,T> store() {
        final Store<T> store = this;
        return new Func1<T, T>() {
            @Override
            public T call(T obj) {
            store.setObject(obj);
            String objJson = new Gson().toJson(obj);
            Editor editor = store.prefs.edit();
            editor.putString(TAG, objJson);
            editor.apply();
            return obj;
            }
        };
    }

    Observable<T> get() {
        if(obj == null) {
            String objJson = prefs.getString(TAG, null);
            obj = new Gson().fromJson(objJson, cls);
        }
        return obj == null ? Observable.<T>empty():Observable.just(obj);
    }

    Action0 clear() {
        return new Action0() {
            @Override
            public void call() {
                obj = null;
                Editor editor = prefs.edit();
                editor.clear();
                editor.apply();
            }
        };
    }

    boolean isEmpty() {
        String objJson = prefs.getString(TAG, null);
        obj = new Gson().fromJson(objJson, cls);
        return obj == null;
    }

    private void setObject(T obj) {
        this.obj = obj;
    }
}