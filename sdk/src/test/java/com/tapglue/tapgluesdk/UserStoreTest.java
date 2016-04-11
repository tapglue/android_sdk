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

package com.tapglue.tapgluesdk;

import com.google.gson.Gson;
import com.tapglue.tapgluesdk.entities.User;

import android.content.SharedPreferences;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.junit.Test;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import rx.Observable;
import rx.observers.TestSubscriber;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsCollectionContaining.hasItems;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class UserStoreTest {

    @Mock
    public SharedPreferences prefs;
    @Mock
    public SharedPreferences.Editor editor;
    public User user = new User();

    //SUT
    UserStore store;

    @Before
    public void setUp() {
        when(prefs.edit()).thenReturn(editor);

        store = new UserStore(prefs);
    }

    @Test
    public void storeUser() {
        TestSubscriber<User> ts = new TestSubscriber<>();

        Observable.just(user).map(store.store()).subscribe();

        store.get().subscribe(ts);

        assertThat(ts.getOnNextEvents(), hasItems(user));
    }

    @Test
    public void nullUserReturnsEmptyObservable() {
        TestSubscriber<User> ts = new TestSubscriber<>();

        store.get().subscribe(ts);

        ts.assertNoErrors();
        ts.assertCompleted();
    }

    @Test
    public void userGetsPersisted() {
        Observable.just(user).map(store.store()).subscribe();

        InOrder inOrder = inOrder(editor);

        inOrder.verify(editor).putString(eq("user"), eq(new Gson().toJson(user)));
        inOrder.verify(editor).apply();
    }

    @Test
    public void getUserReturnsPersistedWhenNull() {
        when(prefs.getString("user", "{}")).thenReturn(new Gson().toJson(user));
        TestSubscriber<User> ts = new TestSubscriber<>();

        store.get().subscribe(ts);

        assertThat(ts.getOnNextEvents(), hasItems(user));
    }
}
