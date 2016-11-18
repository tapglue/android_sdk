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

package com.tapglue.android.http;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;

import com.tapglue.android.entities.Like;
import com.tapglue.android.entities.Post;
import com.tapglue.android.entities.User;

import java.util.List;
import java.util.Map;

class LikesFeed extends FlattenableFeed<List<Like>> {
    List<Like> likes;
    Map<String, User> users;
    @SerializedName("post_map")
    Map<String, Post> posts;

    @Override
    public List<Like> flatten() {
        return new LikesFeedToList().call(this);
    }

    @Override
    public FlattenableFeed<List<Like>> parse(JsonObject jsonObject) {
        Gson g = new Gson();
        LikesFeed feed = g.fromJson(jsonObject, LikesFeed.class);
        return feed;
    }
}
