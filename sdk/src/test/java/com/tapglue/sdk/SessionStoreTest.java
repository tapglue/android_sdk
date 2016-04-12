package com.tapglue.sdk;

import android.content.Context;

import com.tapglue.sdk.entities.User;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockito.Mock;

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
