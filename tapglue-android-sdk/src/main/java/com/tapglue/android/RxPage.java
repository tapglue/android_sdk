package com.tapglue.android;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.tapglue.android.http.FlattenableFeed;
import com.tapglue.android.http.Network;

import rx.Observable;
import rx.functions.Func1;

public class RxPage<T> {
    FlattenableFeed<T> feed;
    Network network;
    TypeToken<T> type;

    public RxPage(FlattenableFeed<T> feed, Network network, TypeToken<T> type) {
        this.feed = feed;
        this.network = network;
        this.type = type;
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
            Gson g = new Gson();
            FlattenableFeed<T> previousFeed = g.fromJson(jsonObject, type.getType());
            return new RxPage<>(previousFeed, network, type);
        }
    }
}
