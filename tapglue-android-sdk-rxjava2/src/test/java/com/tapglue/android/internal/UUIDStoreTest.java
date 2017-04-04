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

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import io.reactivex.Observable;
import io.reactivex.functions.Function;
import io.reactivex.observers.TestObserver;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class UUIDStoreTest {

    @Mock
    Context context;
    @Mock
    Store<String> internalStore;
    @Mock
    Function<String, String> storeFunc;

    //SUT
    UUIDStore store;

    @Before
    public void setUp() {
        when(internalStore.store()).thenReturn(storeFunc);

        store = new UUIDStore(context);
        store.store = internalStore;
    }

    @Test
    public void getsFromInternalStore() throws Exception {
        Observable<String> internalObservable = Observable.just("uuid");
        when(internalStore.get()).thenReturn(internalObservable);
        TestObserver<String> to = store.get().test();

        to.assertValue("uuid");
    }

    @Test
    public void whenEmptyUUIDIsGenerated() throws Exception {
        when(internalStore.isEmpty()).thenReturn(true);
        when(internalStore.get()).thenReturn(Observable.just("uuid"));

        TestObserver<String> to = store.get().test();

        assertThat(to.values().size(), equalTo(1));
        verify(internalStore).store();
    }

    @Test
    public void whenEmptyUUIDIsStored() throws Exception {
        Observable<String> internalObservable = Observable.empty();
        when(internalStore.isEmpty()).thenReturn(true);
        when(internalStore.get()).thenReturn(internalObservable);

        store.get().subscribe();

        verify(storeFunc).apply(anyString());
    }
}
