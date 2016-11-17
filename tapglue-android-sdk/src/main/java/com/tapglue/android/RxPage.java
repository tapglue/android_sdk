package com.tapglue.android;

import com.tapglue.android.http.FlattenableFeed;

import java.util.List;

/**
 * Created by John on 14/11/16.
 */

public class RxPage<T> {
    FlattenableFeed<T> feed;

    public List<T> getData() {
        return feed.flatten();
    }

    public RxPage<T> getPrevious() {
        return null;
    }
}
