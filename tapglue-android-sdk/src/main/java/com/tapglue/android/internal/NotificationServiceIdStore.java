package com.tapglue.android.internal;

import android.content.Context;
import android.content.SharedPreferences;

import rx.Observable;

public class NotificationServiceIdStore {
    private static final String NOTIFICATION_ID = "notificationServiceId";
    Store<String> store;

    public NotificationServiceIdStore(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(NOTIFICATION_ID, Context.MODE_PRIVATE);
        store = new Store<>(prefs, String.class);
    }

    public void store(String id) {
        store.store().call(id);
    }

    public Observable<String> get() {
        return store.get();
    }
}
