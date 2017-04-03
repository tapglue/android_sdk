package com.tapglue.android.internal;

import android.content.Context;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import io.reactivex.Observable;
import io.reactivex.functions.Function;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class NotificationServiceIdStoreTest {

    private static final String ID = "someId";
    @Mock
    Context context;
    @Mock
    Store<String> internalStore;
    @Mock
    Function<String, String> storeFunc;
    @Mock
    Observable<String> getObservable;

    NotificationServiceIdStore store;

    @Before
    public void setUp() {
        store = new NotificationServiceIdStore(context);
        store.store = internalStore;
    }

    @Test
    public void storeStoresOnInternalStore() throws Exception {
        when(internalStore.store()).thenReturn(storeFunc);

        store.store(ID);

        verify(storeFunc).apply(ID);
    }

    @Test
    public void getGetsFromInternalStore() {
        when(internalStore.get()).thenReturn(getObservable);

        assertThat(store.get(), equalTo(getObservable));
    }
}
