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
import android.support.annotation.Nullable;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tapglue.model.TGConnection;
import com.tapglue.model.TGConnectionUser;
import com.tapglue.model.TGConnectionUsersList;
import com.tapglue.model.TGEvent;
import com.tapglue.model.TGEventObject;
import com.tapglue.model.TGFeed;
import com.tapglue.model.TGFeedCount;
import com.tapglue.model.TGImage;
import com.tapglue.model.TGLoginUser;
import com.tapglue.model.TGPendingConnections;
import com.tapglue.model.TGSearchCriteria;
import com.tapglue.model.TGSocialConnections;
import com.tapglue.model.TGUser;

import java.lang.reflect.Type;
import java.util.Map;

public class TGCustomCacheObject {

    /**
     * Type of cache object - to be used to determine type of generic API object inside requests
     */
    public enum TGCacheObjectType {
        Connection(1), ConnectionUser(2), ConnectionUserList(3), Event(4), EventObject(5), Feed(6),
        FeedCount(7), LoginUser(8), PendingConnections(9), SearchCriteria(10), SocialConnections(11),
        User(12), Error(13), Image(14);

        /**
         * Id of type
         */
        private int mId = -1;

        static public TGCacheObjectType fromCode(int id) {
            for (TGCacheObjectType type : values()) {
                if (type.toCode() == id) { return type; }
            }
            return null;
        }

        TGCacheObjectType(int id) {
            mId = id;
        }

        /**
         * Get id
         *
         * @return id
         */
        public int toCode() {
            return this.mId;
        }
    }

    /**
     * Create type token for cache object
     *
     * @param type Type of which token should be created
     *
     * @return Type token
     */
    static private Type createToken(@NonNull TGCacheObjectType type) {
        Type token;
        switch (type) {
            case Connection:
                token = new TypeToken<TGCacheRequest<TGConnection>>() {
                }.getType();
                break;
            case ConnectionUser:
                token = new TypeToken<TGCacheRequest<TGConnectionUser>>() {
                }.getType();
                break;
            case ConnectionUserList:
                token = new TypeToken<TGCacheRequest<TGConnectionUsersList>>() {
                }.getType();
                break;
            case Event:
                token = new TypeToken<TGCacheRequest<TGEvent>>() {
                }.getType();
                break;
            case EventObject:
                token = new TypeToken<TGCacheRequest<TGEventObject>>() {
                }.getType();
                break;
            case Feed:
                token = new TypeToken<TGCacheRequest<TGFeed>>() {
                }.getType();
                break;
            case FeedCount:
                token = new TypeToken<TGCacheRequest<TGFeedCount>>() {
                }.getType();
                break;
            case LoginUser:
                token = new TypeToken<TGCacheRequest<TGLoginUser>>() {
                }.getType();
                break;
            case PendingConnections:
                token = new TypeToken<TGCacheRequest<TGPendingConnections>>() {
                }.getType();
                break;
            case SearchCriteria:
                token = new TypeToken<TGCacheRequest<TGSearchCriteria>>() {
                }.getType();
                break;
            case SocialConnections:
                token = new TypeToken<TGCacheRequest<TGSocialConnections>>() {
                }.getType();
                break;
            case User:
                token = new TypeToken<TGCacheRequest<TGUser>>() {
                }.getType();
                break;
            case Image:
                token = new TypeToken<TGImage>() {
                }.getType();
                break;
            default:
                throw new RuntimeException("Unknown object type");
        }
        return token;
    }

    /**
     * Deserialize string to a request
     *
     * @param txt String with serialized request
     *
     * @return Deserialized request
     */
    static public TGRequest deserialize(String txt) {
        Map<String, ?> values = new Gson().fromJson(txt, new TypeToken<Map<String, ?>>() {
        }.getType());
        if (values.containsKey("mObject")) {
            values = (Map<String, ?>) values.get("mObject");
            Type token = createToken(TGCacheObjectType.fromCode((int) Double.parseDouble(
                values.get("mCacheObjectType").toString()
            )));
            TGCacheRequest cacheRequest = (new Gson().fromJson(txt, token));
            return cacheRequest.toTGRequest();
        }
        return null;
    }

    /**
     * Serialize request
     *
     * @param obj Request to be serialized
     *
     * @return String with serialized request
     */
    @Nullable
    static public String serialize(@Nullable TGRequest obj) {
        if (obj == null ||
            obj.getObject() == null ||
            obj.getObject().getCacheObjectType() == null) { return null; }

        Type token = createToken(obj.getObject().getCacheObjectType());
        return new Gson().toJson(new TGCacheRequest<>(obj), token);
    }
}
