package com.tapglue.android;

import com.google.gson.JsonObject;
import com.tapglue.android.http.FlattenableFeed;
import com.tapglue.android.http.Network;

import rx.Observable;
import rx.functions.Func1;

public class RxPage<T> {
    FlattenableFeed<T> feed;
    Network network;

    public RxPage(FlattenableFeed<T> feed, Network network) {
        this.feed = feed;
        this.network = network;
    }

    public T getData() {
        return feed.flatten();
    }

    public Observable<RxPage<T>> getPrevious() {
        return network.paginatedGet(feed.previousPointer()).map(new PreviousPageGenerator());
    }

    private class PreviousPageGenerator implements Func1<JsonObject, RxPage<T>> {

        @Override
        public RxPage<T> call(JsonObject jsonObject) {
            FlattenableFeed<T> previousFeed = feed.parse(jsonObject);
            return new RxPage<>(previousFeed, network);
        }
    }
}
