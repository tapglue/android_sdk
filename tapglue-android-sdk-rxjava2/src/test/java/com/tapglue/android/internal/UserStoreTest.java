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

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import io.reactivex.Observable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Function;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class UserStoreTest {

    @Mock
    public Context context;
    @Mock
    public SharedPreferences prefs;
    @Mock
    public SharedPreferences.Editor editor;
    @Mock
    public Store internalStore;
    @Mock
    public Function<User,User> storeFunc;
    @Mock
    public Observable<User> getObservable;
    @Mock
    public Action clearAction;

    //SUT
    UserStore store;

    @Before
    public void setUp() {
        when(context.getSharedPreferences("user", Context.MODE_PRIVATE)).thenReturn(prefs);
        when(prefs.edit()).thenReturn(editor);

        store = new UserStore(context);
        store.store = internalStore;
    }

    @Test
    public void storeUser() {
        when(internalStore.store()).thenReturn(storeFunc);

        assertThat(store.store(), equalTo(storeFunc));
    }

    @Test
    public void getUserCallsInternalStore() {
        when(internalStore.get()).thenReturn(getObservable);

        assertThat(store.get(), equalTo(getObservable));
    }

    @Test
    public void clearCallsInternalStore() {
        when(internalStore.clear()).thenReturn(clearAction);

        assertThat(store.clear(), equalTo(clearAction));
    }
}
