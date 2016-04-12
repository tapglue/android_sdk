package com.tapglue.sdk;

import android.content.Context;
import android.content.SharedPreferences;

import com.tapglue.sdk.entities.User;

import rx.Observable;
import rx.functions.Action0;
import rx.functions.Func1;

public class SessionStore {

    Store<User> store;

    public SessionStore(Context context) {
        SharedPreferences prefs = context.getSharedPreferences("sessionToken", Context.MODE_PRIVATE);
        store = new Store<>(prefs, User.class);
    }

    public Observable<User> get() {
        return store.get();
    }

    public Func1<User, User> store() {
        return store.store();
    }

    public Action0 clear() {
        return store.clear();
    }
}