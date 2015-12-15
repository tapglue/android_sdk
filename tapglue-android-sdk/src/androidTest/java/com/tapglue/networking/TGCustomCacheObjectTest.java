/*
 * Copyright (c) 2015 Tapglue (https://www.tapglue.com/). All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.tapglue.networking;

import android.support.annotation.NonNull;
import android.test.suitebuilder.annotation.SmallTest;

import com.tapglue.model.TGConnection;
import com.tapglue.model.TGConnectionUser;
import com.tapglue.model.TGConnectionUsersList;
import com.tapglue.model.TGEvent;
import com.tapglue.model.TGEventObject;
import com.tapglue.model.TGFeed;
import com.tapglue.model.TGFeedCount;
import com.tapglue.model.TGLoginUser;
import com.tapglue.model.TGPendingConnections;
import com.tapglue.model.TGSearchCriteria;
import com.tapglue.model.TGSocialConnections;
import com.tapglue.model.TGUser;
import com.tapglue.networking.requests.TGRequestType;

import junit.framework.TestCase;

public class TGCustomCacheObjectTest extends TestCase {

    @SmallTest
    public void test01() {
        TGRequest startRequest;
        startRequest = new TGRequest(new TGConnection().setType(TGConnection.TGConnectionType.FOLLOW), TGRequestType.CREATE, true, null);
        testRequest(startRequest);
        startRequest = new TGRequest(new TGSearchCriteria().setSearchCriteria("testCriteria"), TGRequestType.SEARCH, true, null);
        testRequest(startRequest);

        startRequest = new TGRequest(new TGConnectionUser(), TGRequestType.CREATE, true, null);
        testRequest(startRequest);
        startRequest = new TGRequest(new TGConnectionUsersList(), TGRequestType.CREATE, true, null);
        testRequest(startRequest);
        startRequest = new TGRequest(new TGEvent(), TGRequestType.CREATE, true, null);
        testRequest(startRequest);
        startRequest = new TGRequest(new TGEventObject(), TGRequestType.CREATE, true, null);
        testRequest(startRequest);
        startRequest = new TGRequest(new TGFeed(), TGRequestType.CREATE, true, null);
        testRequest(startRequest);
        startRequest = new TGRequest(new TGFeedCount(), TGRequestType.CREATE, true, null);
        testRequest(startRequest);
        startRequest = new TGRequest(new TGLoginUser("name", "email", "pass"), TGRequestType.CREATE, true, null);
        testRequest(startRequest);
        startRequest = new TGRequest(new TGPendingConnections(), TGRequestType.CREATE, true, null);
        testRequest(startRequest);
        startRequest = new TGRequest(new TGSocialConnections(), TGRequestType.CREATE, true, null);
        testRequest(startRequest);
        startRequest = new TGRequest(new TGUser(), TGRequestType.CREATE, true, null);
        testRequest(startRequest);
    }

    private void testRequest(@NonNull TGRequest startRequest) {
        String requestString = TGCustomCacheObject.serialize(startRequest);
        TGRequest endRequest = TGCustomCacheObject.deserialize(requestString);
        assertNotNull(endRequest);
        assertNotNull(endRequest.getObject());
        assertEquals(endRequest.getObject().getCacheObjectType(), startRequest.getObject().getCacheObjectType());
    }
}