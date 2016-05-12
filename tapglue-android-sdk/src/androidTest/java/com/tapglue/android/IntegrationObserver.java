package com.tapglue.android;

import rx.Observer;

/**
 * Created by John on 4/6/16.
 */
public abstract class IntegrationObserver<T> implements Observer<T> {
    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {
        throw new AssertionError(e);
    }

    @Override
    public abstract void onNext(T t);
}
