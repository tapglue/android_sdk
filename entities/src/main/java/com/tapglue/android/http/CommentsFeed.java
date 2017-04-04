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
import com.tapglue.android.entities.Comment;
import com.tapglue.android.entities.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CommentsFeed extends FlattenableFeed<List<Comment>> {
    List<Comment> comments;
    Map<String, User> users;

    @Override
    public List<Comment> flatten() {
        if(comments == null) {
            return new ArrayList<>();
        }
        for(Comment comment: comments) {
            comment.setUser(users.get(comment.getUserId()));
        }
        return comments;
    }

    @Override
    FlattenableFeed<List<Comment>> constructDefaultFeed() {
        CommentsFeed feed = new CommentsFeed();
        feed.comments = new ArrayList<>();
        return feed;
    }

    @Override
    FlattenableFeed<List<Comment>> parseJson(JsonObject jsonObject) {
        Gson g = new Gson();
        CommentsFeed feed = g.fromJson(jsonObject, CommentsFeed.class);
        return feed;
    }
}
