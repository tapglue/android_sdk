package com.tapglue.sdk;

import com.google.gson.Gson;

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
import static org.hamcrest.core.IsEqual.equalTo;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class StoreTest {

    private static final long ID = 10;

    @Mock
    public SharedPreferences prefs;
    @Mock
    public SharedPreferences.Editor editor;

    TestEntity entity = new TestEntity(ID);

    //SUT
    Store<TestEntity> store;
    @Before
    public void setUp() {
        when(prefs.edit()).thenReturn(editor);
        store = new Store<>(prefs, TestEntity.class);
    }

    @Test
    public void storeEntity() {
        TestSubscriber<TestEntity> ts = new TestSubscriber<>();

        Observable.just(entity).map(store.store()).subscribe();

        store.get().subscribe(ts);

        assertThat(ts.getOnNextEvents(), hasItems(entity));
    }

    @Test
    public void nullEntityReturnsEmptyObservable() {
        TestSubscriber<TestEntity> ts = new TestSubscriber<>();

        store.get().subscribe(ts);

        ts.assertNoErrors();
        ts.assertCompleted();
    }

    @Test
    public void entityGetsPersisted() {
        Observable.just(entity).map(store.store()).subscribe();

        InOrder inOrder = inOrder(editor);

        inOrder.verify(editor).putString(eq("object"), eq(new Gson().toJson(entity)));
        inOrder.verify(editor).apply();
    }

    @Test
    public void clearFunctionDeletesEntity() {
        Observable.just(entity).map(store.store()).subscribe();

        store.clear().call();

        TestSubscriber<TestEntity> ts = new TestSubscriber<>();
        store.get().subscribe(ts);

        assertThat(ts.getOnNextEvents().size(), equalTo(0));
    }

    @Test
    public void getEntityReturnsPersistedWhenNull() {
        when(prefs.getString("object", null)).thenReturn(new Gson().toJson(entity));
        TestSubscriber<TestEntity> ts = new TestSubscriber<>();

        store.get().subscribe(ts);

        assertThat(ts.getOnNextEvents(), hasItems(entity));
    }
}