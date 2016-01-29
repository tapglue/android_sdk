/*
 * Copyright (c) 2015-2016 Tapglue (https://www.tapglue.com/). All rights reserved.
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

package com.tapglue;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.tapglue.managers.TGConnectionManagerImpl;
import com.tapglue.managers.TGConnectionManager;
import com.tapglue.managers.TGEventManagerImpl;
import com.tapglue.managers.TGEventManager;
import com.tapglue.managers.TGFeedManagerImpl;
import com.tapglue.managers.TGFeedManager;
import com.tapglue.managers.TGPostManagerImpl;
import com.tapglue.managers.TGPostManager;
import com.tapglue.managers.TGUserManagerImpl;
import com.tapglue.managers.TGUserManager;
import com.tapglue.networking.TGNetworkManager;
import com.tapglue.networking.TGRequests;
import com.tapglue.utils.TGLog;

public class Tapglue {
    /**
     * Hidden Singleton pattern
     */
    static private Tapglue mInstance;

    /**
     * Tapglue configuration
     */
    @NonNull
    private final TGConfiguration mConfig;

    /**
     * Application context to use
     */
    private final Context mContext;

    /**
     * Default logging tool
     */
    @NonNull
    private final TGLog mLogger;

    /**
     * Tapglue network manager
     */
    @NonNull
    private final TGNetworkManager mNetManager;

    /**
     * Connections manager
     */
    private TGConnectionManagerImpl mConnectionManager;

    /**
     * Events manager
     */
    private TGEventManagerImpl mEventManager;

    /**
     * EventsList manager
     */
    private TGFeedManagerImpl mFeedManager;

    /**
     * Posts manager
     */
    private TGPostManagerImpl mPostsManager;

    /**
     * User manager
     */
    private TGUserManagerImpl mUserManager;

    /**
     * Get connections manager
     *
     * @return Connections manager
     */
    static public TGConnectionManager connection() {
        return mInstance.getConnectionManager();
    }

    /**
     * Get events manager
     *
     * @return Events manager
     */
    static public TGEventManager event() {
        return mInstance.getEventManager();
    }

    /**
     * Get feed manager
     *
     * @return EventsList manager
     */
    static public TGFeedManager feed() {
        return mInstance.getFeedManager();
    }

    /**
     * Initialize library
     *
     * @param context      Application context
     * @param tapglueToken Tapglue API Token
     */
    static public void initialize(Context context, String tapglueToken) {
        // library should be initialized only once
        if (mInstance != null && mInstance.mContext != null) { return; }
        initialize(context, new TGConfiguration().setToken(tapglueToken));
    }

    /**
     * Initialize library with configuration
     *
     * @param context       Application Context
     * @param tapglueToken  Tapglue API token
     * @param configuration Tapglue configuration
     */

    static public void initialize(Context context, String tapglueToken, @NonNull TGConfiguration configuration) {
        if (mInstance != null && mInstance.mContext != null) { return; }
        initialize(context, configuration.setToken(tapglueToken));
    }

    /**
     * Initialize library
     *
     * @param context
     * @param configuration
     */
    static public void initialize(Context context, @NonNull TGConfiguration configuration) {
        // library should be initialized only once
        if (mInstance != null && mInstance.mContext != null) { return; }
        mInstance = new Tapglue(context, configuration);
        mInstance.mUserManager = new TGUserManagerImpl(mInstance);
        mInstance.mConnectionManager = new TGConnectionManagerImpl(mInstance);
        mInstance.mEventManager = new TGEventManagerImpl(mInstance);
        mInstance.mFeedManager = new TGFeedManagerImpl(mInstance);
        mInstance.mPostsManager = new TGPostManagerImpl(mInstance);
        mInstance.getUserManager().tryToLoadUserFromCache();
    }

    /**
     * Get posts manager
     *
     * @return Posts manager
     */
    static public TGPostManager posts() {
        return mInstance.getPostManager();
    }

    /**
     * Get user manager
     *
     * @return User manager
     */
    static public TGUserManager user() {
        return mInstance.getUserManager();
    }

    private Tapglue(Context context, @NonNull TGConfiguration configuration) {
        mContext = context;
        mConfig = configuration;

        if (configuration.getToken() == null ||
            configuration.getToken().isEmpty()) {
            throw new RuntimeException("Tapglue token not initialized");
        }

        mLogger = new TGLog(configuration.mDebugMode);
        mNetManager = new TGNetworkManager(configuration, this);
    }

    /**
     * Create network request
     *
     * @return Network requests manager
     */
    @NonNull
    public TGRequests createRequest() {
        return mInstance.mNetManager.createRequest();
    }

    /**
     * Get configuration
     *
     * @return Configuration
     */
    @NonNull
    public TGConfiguration getConfiguration() {
        return mConfig;
    }

    /**
     * Get connections manager
     *
     * @return Connections manager
     */
    private TGConnectionManagerImpl getConnectionManager() {
        return mConnectionManager;
    }

    /**
     * Get used context
     *
     * @return Application context used by library
     */
    public Context getContext() {
        return mContext;
    }

    /**
     * Get event manager
     *
     * @return Event manager
     */
    private TGEventManagerImpl getEventManager() {
        return mEventManager;
    }

    /**
     * Get feed manager
     *
     * @return EventsList manager
     */
    private TGFeedManagerImpl getFeedManager() {
        return mFeedManager;
    }

    /**
     * Get TapGlue logger
     *
     * @return Logger
     */
    @NonNull
    public TGLog getLogger() {
        return mLogger;
    }

    /**
     * Get posts manager
     *
     * @return Posts manager
     */
    private TGPostManagerImpl getPostManager() { return mPostsManager;}

    /**
     * Get user manager
     *
     * @return User manager
     */
    public TGUserManagerImpl getUserManager() {
        return mUserManager;
    }

    /**
     * Check if current configuration is correct (exists with non-null token)
     *
     * @return Is config correct?
     */
    public boolean isCorrectConfig() {
        return (!TextUtils.isEmpty(mConfig.getToken()));
    }

    /**
     * TGConfiguration class with default values
     */
    public static class TGConfiguration {
        public static final String API_VERSION = "0.4";

        private static final String DEFAULT_API_URL = "https://api.tapglue.com/";

        private static final int DEFAULT_FLUSH_INTERVAL = 15 * 1000; // 15s

        private static final int MAX_FLUSH_INTERVAL = 180 * 1000; // 180s

        boolean analyticsEnabled = true;

        @NonNull
        String mApiBaseUrl = DEFAULT_API_URL;

        boolean mDebugMode = false;

        int mFlushIntervalInMs = DEFAULT_FLUSH_INTERVAL;

        @Nullable
        String mToken = null;

        private boolean cacheEnabled = true;

        /**
         * Get final api url
         *
         * @return Composed API url
         */
        @NonNull
        public String getApiUrl() {
            return mApiBaseUrl.charAt(mApiBaseUrl.length() - 1) == '/' ?
                (mApiBaseUrl + API_VERSION + "/") : (mApiBaseUrl + "/" + API_VERSION + "/");
        }

        /**
         * Get flush interval for cached events
         *
         * @return current flush interval in milliseconds
         */
        public long getFlushInterval() {
            return mFlushIntervalInMs;
        }

        /**
         * Set events flush interval
         *
         * @param secs New interval in seconds
         *
         * @return Current object
         */
        @NonNull
        public TGConfiguration setFlushInterval(int secs) {
            mFlushIntervalInMs = secs * 1000;
            if (mFlushIntervalInMs <= 0) { mFlushIntervalInMs = DEFAULT_FLUSH_INTERVAL; }
            else if (mFlushIntervalInMs > MAX_FLUSH_INTERVAL) {
                mFlushIntervalInMs = MAX_FLUSH_INTERVAL;
            }
            return this;
        }

        /**
         * Get library app token
         *
         * @return token
         */
        @Nullable
        public String getToken() {
            return mToken;
        }

        /**
         * Set API token
         *
         * @param tapglueToken
         *
         * @return Current object
         */
        @NonNull
        public TGConfiguration setToken(String tapglueToken) {
            mToken = tapglueToken;
            return this;
        }

        /**
         * Is analytics enabled?
         *
         * @return Information if analytics are enabled
         */
        public boolean isAnalyticsEnabled() {
            return analyticsEnabled;
        }

        public boolean isCacheEnabled() {
            return cacheEnabled;
        }

        @NonNull
        public TGConfiguration setCacheEnabled(boolean cacheEnabled) {
            this.cacheEnabled = cacheEnabled;
            return this;
        }

        /**
         * Is library in debug mode?
         *
         * @return is debug mode enabled?
         */
        public Boolean isDebugMode() {
            return mDebugMode;
        }

        /**
         * Set if analytics should be used - by default analytics are enabled
         *
         * @param analyticsEnabled should analytics be enabled?
         *
         * @return
         */
        @NonNull
        public TGConfiguration setAnalytics(boolean analyticsEnabled) {
            this.analyticsEnabled = analyticsEnabled;
            return this;
        }

        @NonNull
        public TGConfiguration setApiBaseUrl(@Nullable String newUrl) {
            if (newUrl != null) { mApiBaseUrl = newUrl; }
            return this;
        }

        /**
         * Set API URL
         *
         * @param newUrl Api URL
         *
         * @return Current object
         */

        /**
         * Enables debug mode
         *
         * @return Current object
         */
        @NonNull
        public TGConfiguration setDebugMode(boolean debugMode) {
            mDebugMode = debugMode;
            return this;
        }
    }
}
