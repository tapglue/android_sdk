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

import java.io.IOException;

import rx.Observable;
import rx.functions.Action1;

public class RxWrapper<T> {
    public T unwrap(Observable<T> observable) throws IOException {
        final Holder<T> holder = new Holder<>();
        final Throwable[] throwable = new Throwable[1];

        observable.toBlocking().subscribe(new Action1<T>() {
            @Override
            public void call(T t) {
                holder.obj = t;
            }
        }, new Action1<Throwable>() {
            @Override
            public void call(Throwable t) {
                throwable[0] = t;
            }
        });

        if(throwable[0] != null) {
            if(throwable[0] instanceof IOException) {
                throw (IOException) throwable[0];
            } else {
                throw new RuntimeException(throwable[0]);
            }
        } else {
            return holder.obj;
        }
    }

    static class Holder<T> {
        T obj;
    }
}
