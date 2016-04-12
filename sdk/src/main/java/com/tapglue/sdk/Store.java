package com.tapglue.sdk;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.google.gson.Gson;

import rx.Observable;
import rx.functions.Action0;
import rx.functions.Func1;

class Store<T> {
    private static final String TAG = "object";
    SharedPreferences prefs;
    Class<T> cls;
    T obj;

    Store(SharedPreferences prefs, Class<T> cls) {
        this.prefs = prefs;
        this.cls = cls;
    }

    Func1<T,T> store() {
        final Store<T> store = this;
        return new Func1<T, T>() {
            @Override
            public T call(T obj) {
            store.setObject(obj);
            String objJson = new Gson().toJson(obj);
            Editor editor = store.prefs.edit();
            editor.putString(TAG, objJson);
            editor.apply();
            return obj;
            }
        };
    }

    Observable<T> get() {
        if(obj == null) {
            String objJson = prefs.getString(TAG, null);
            obj = new Gson().fromJson(objJson, cls);
        }
        return obj == null ? Observable.<T>empty():Observable.just(obj);
    }

    Action0 clear() {
        return new Action0() {
            @Override
            public void call() {
                obj = null;
                Editor editor = prefs.edit();
                editor.clear();
                editor.apply();
            }
        };
    }

    private void setObject(T obj) {
        this.obj = obj;
    }
}