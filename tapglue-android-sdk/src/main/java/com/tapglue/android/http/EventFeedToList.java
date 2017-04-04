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

import com.tapglue.android.entities.Event;
import com.tapglue.android.entities.Post;
import com.tapglue.android.entities.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import rx.functions.Func1;

public class EventFeedToList implements Func1<EventListFeed, List<Event>> {
    @Override
    public List<Event> call(EventListFeed feed) {
        if(feed == null || feed.events == null) {
            return new ArrayList<>();
        }
        Map<String, User> users = feed.users;
        Map<String, Post> posts = feed.posts;
        for(Event event : feed.events) {
            event.setUser(users.get(event.getUserId()));
            event.setPost(posts.get(event.getPostId()));
        }
        return feed.events;
    }
}
