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

import com.tapglue.sdk.entities.Event;
import com.tapglue.sdk.entities.NewsFeed;
import com.tapglue.sdk.entities.Post;

import java.util.ArrayList;
import java.util.List;

import rx.functions.Func1;

public class RawNewsFeedToFeed implements Func1<RawNewsFeed, NewsFeed> {

    public NewsFeed call(RawNewsFeed rawFeed) {
        if(rawFeed == null) {
            return new NewsFeed(new ArrayList<Event>(), new ArrayList<Post>());
        }
        EventListFeed eventFeed = new EventListFeed();
        eventFeed.events = rawFeed.events;
        eventFeed.users = rawFeed.users;
        List<Event> events = new EventFeedToList().call(eventFeed);

        PostListFeed postFeed = new PostListFeed();
        postFeed.posts = rawFeed.posts;
        postFeed.users = rawFeed.users;
        List<Post> posts = new PostFeedToList().call(postFeed);
        return new NewsFeed(events, posts);
    }
}
