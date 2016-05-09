/*
 *  Copyright (c) 2015-2016 Tapglue (https://www.tapglue.com/). All rights reserved.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package com.tapglue.sdk.http;

import com.tapglue.sdk.entities.Like;

import java.util.ArrayList;
import java.util.List;

import rx.functions.Func1;

class LikesFeedToList implements Func1<LikesFeed, List<Like>> {
    public List<Like> call(LikesFeed feed) {
        if(feed == null) {
            return new ArrayList<>();
        }
        for(Like like: feed.likes) {
            like.setUser(feed.users.get(like.getUserId()));
        }
        return feed.likes;
    }
}
