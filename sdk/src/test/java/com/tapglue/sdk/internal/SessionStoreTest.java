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

package com.tapglue.sdk.internal;

import android.content.Context;

import com.tapglue.sdk.entities.User;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import rx.Observable;
import rx.functions.Action0;
import rx.functions.Func1;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class SessionStoreTest {

    @Mock
    Context context;
    @Mock
    Store<User> internalStore;
    @Mock
    Func1<User,User> storeFunc;
    @Mock
    Observable<User> getObservable;
    @Mock
    Action0 clearAction;

    //SUT
    SessionStore store;

    @Before
    public void setUp() {
        store = new SessionStore(context);
        store.store = internalStore;
    }

    @Test
    public void storeCallsInternal() {
        when(internalStore.store()).thenReturn(storeFunc);

        assertThat(store.store(), equalTo(internalStore.store()));
    }

    @Test
    public void getCallsInternal() {
        when(internalStore.get()).thenReturn(getObservable);

        assertThat(store.get(), equalTo(getObservable));
    }

    @Test
    public void clearCallsInternal() {
        when(internalStore.clear()).thenReturn(clearAction);

        assertThat(store.clear(), equalTo(clearAction));
    }
}
