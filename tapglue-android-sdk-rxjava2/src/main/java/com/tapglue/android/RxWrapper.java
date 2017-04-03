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

package com.tapglue.android;

import java.io.IOException;

import io.reactivex.Observable;

public class RxWrapper<T> {
    public T unwrap(Observable<T> observable) throws IOException {
        final Holder<T> holder = new Holder<>();
        final Throwable[] throwable = new Throwable[1];

        try {
            holder.obj = observable.blockingFirst();
        } catch (Exception t) {
            throwable[0] = t;
        }

        if(throwable[0] != null) {
            System.out.println(throwable[0]);
            if(throwable[0] instanceof IOException) {
                throw (IOException) throwable[0];
            } else if (throwable[0].getCause() instanceof IOException) {
                throw (IOException) throwable[0].getCause();
            } else {
                // test expected null
                return null;
            }
        } else {
            return holder.obj;
        }
    }

    static class Holder<T> {
        T obj;
    }
}
