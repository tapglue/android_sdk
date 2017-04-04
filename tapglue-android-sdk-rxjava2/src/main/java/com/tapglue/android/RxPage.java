package com.tapglue.android;

import com.google.gson.JsonObject;
import com.tapglue.android.http.FlattenableFeed;
import com.tapglue.android.http.Network;

import io.reactivex.Observable;
import io.reactivex.functions.Function;
import okhttp3.RequestBody;


public class RxPage<T> {
    FlattenableFeed<T> feed;
    Network network;
    RequestBody payload;

    public RxPage(FlattenableFeed<T> feed, Network network) {
        this.feed = feed;
        this.network = network;
    }

    public RxPage(FlattenableFeed<T> feed, Network network, RequestBody payload) {
        this.feed = feed;
        this.network = network;
        this.payload = payload;
    }

    public T getData() {
        return feed.flatten();
    }

    public Observable<RxPage<T>> getPrevious() {
        if(payload == null) {
            return network.paginatedGet(feed.previousPointer()).map(new PreviousPageGenerator());
        } else {
            return network.paginatedPost(feed.previousPointer(), payload)
                .map(new PreviousPageGenerator());
        }
    }

    private class PreviousPageGenerator implements Function<JsonObject, RxPage<T>> {

        @Override
        public RxPage<T> apply(JsonObject jsonObject) {
            FlattenableFeed<T> previousFeed = feed.parse(jsonObject);
            if(payload == null) {
                return new RxPage<>(previousFeed, network);
            } else {
                return new RxPage<>(previousFeed, network, payload);
            }
        }
    }
}
