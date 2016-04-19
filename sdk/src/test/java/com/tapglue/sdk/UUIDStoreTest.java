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

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import rx.Observable;
import rx.observers.TestSubscriber;
import rx.functions.Func1;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class UUIDStoreTest {

    @Mock
    Context context;
    @Mock
    Store<String> internalStore;
    @Mock
    Func1<String, String> storeFunc;

    //SUT
    UUIDStore store;

    @Before
    public void setUp() {
        when(internalStore.store()).thenReturn(storeFunc);

        store = new UUIDStore(context);
        store.store = internalStore;
    }

    @Test
    public void getsFromInternalStore() {
        Observable<String> internalObservable = Observable.just("uuid");
        when(internalStore.get()).thenReturn(internalObservable);
        TestSubscriber<String> ts = new TestSubscriber<>();

        store.get().subscribe(ts);

        assertThat(ts.getOnNextEvents(), hasItems("uuid"));
    }

    @Test
    public void whenEmptyUUIDIsGenerated () {
        Observable<String> internalObservable = Observable.empty();
        when(internalStore.get()).thenReturn(internalObservable)
                .thenReturn(Observable.just("uuid"));
        TestSubscriber<String> ts = new TestSubscriber<>();

        store.get().subscribe(ts);

        assertThat(ts.getOnNextEvents().size(), equalTo(1));
    }

    @Test
    public void whenEmptyUUIDIsStored () {
        Observable<String> internalObservable = Observable.empty();
        when(internalStore.get()).thenReturn(internalObservable);

        store.get().subscribe();

        verify(storeFunc).call(anyString());
    }
}
