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

import com.tapglue.managers.TGConnectionManager;
import com.tapglue.managers.TGConnectionManagerImpl;
import com.tapglue.managers.TGEventManager;
import com.tapglue.managers.TGEventManagerImpl;
import com.tapglue.managers.TGFeedManager;
import com.tapglue.managers.TGFeedManagerImpl;
import com.tapglue.managers.TGPostManager;
import com.tapglue.managers.TGPostManagerImpl;
import com.tapglue.managers.TGRecommendationManagerImpl;
import com.tapglue.managers.TGRecommendationManager;
import com.tapglue.managers.TGUserManager;
import com.tapglue.managers.TGUserManagerImpl;
import com.tapglue.networking.TGNetworkManager;
import com.tapglue.networking.TGRequests;
import com.tapglue.utils.TGLog;

public class Tapglue {

    /**
     * Hidden Singleton pattern
     */
    private static Tapglue instance;

    /**
     * Tapglue configuration
     */
    @NonNull
    private final TGConfiguration config;

    /**
     * Application context to use
     */
    private final Context context;

    /**
     * Default logging tool
     */
    @NonNull
    private final TGLog logger;

    /**
     * Tapglue network manager
     */
    @NonNull
    private final TGNetworkManager netManager;

    /**
     * Connections manager
     */
    private TGConnectionManagerImpl connectionManager;

    /**
     * Events manager
     */
    private TGEventManagerImpl eventManager;

    /**
     * EventsList manager
     */
    private TGFeedManagerImpl feedManager;

    /**
     * Posts manager
     */
    private TGPostManagerImpl postsManager;

    /**
     * Recommendation manager
     */
    private TGRecommendationManagerImpl mRecommendationManager;

    /**
     * User manager
     */
    private TGUserManagerImpl userManager;

    /**
     * Get connections manager
     *
     * @return Connections manager
     */
    public static TGConnectionManager connection() {
        return instance.getConnectionManager();
    }

    /**
     * Get events manager
     *
     * @return Events manager
     */
    public static TGEventManager event() {
        return instance.getEventManager();
    }

    /**
     * Get feed manager
     *
     * @return EventsList manager
     */
    public static TGFeedManager feed() {
        return instance.getFeedManager();
    }

    /**
     * Initialize library
     *
     * @param context      Application context
     * @param tapglueToken Tapglue API Token
     */
    public static void initialize(Context context, String tapglueToken) {
        initialize(context, new TGConfiguration().setToken(tapglueToken));
    }

    /**
     * Initialize library with configuration
     *
     * @param context       Application Context
     * @param tapglueToken  Tapglue API token
     * @param configuration Tapglue configuration
     */

    public static void initialize(Context context, String tapglueToken, @NonNull TGConfiguration configuration) {
        initialize(context, configuration.setToken(tapglueToken));
    }

    /**
     * Initialize library
     *
     * @param context
     * @param configuration
     */
    public static void initialize(Context context, @NonNull TGConfiguration configuration) {
        // library should be initialized only once
        if(instance == null || instance.context == null) {
            synchronized (Tapglue.class) {
                if(instance == null || instance.context == null) {
                    instance = new Tapglue(context, configuration);
                    instance.userManager = new TGUserManagerImpl(instance);
                    instance.connectionManager = new TGConnectionManagerImpl(instance);
                    instance.eventManager = new TGEventManagerImpl(instance);
                    instance.feedManager = new TGFeedManagerImpl(instance);
                    instance.postsManager = new TGPostManagerImpl(instance);
                    instance.mRecommendationManager = new TGRecommendationManagerImpl(instance);
                    instance.getUserManager().tryToLoadUserFromCache();
                }
            }
        }
    }

    /**
     * Get posts manager
     *
     * @return Posts manager
     */
    public static TGPostManager posts() {
        return instance.getPostManager();
    }

    public static TGRecommendationManager recommendation() {
        return instance.getRecommendationManager();
    }

    /**
     * Get user manager
     *
     * @return User manager
     */
    public static TGUserManager user() {
        return instance.getUserManager();
    }

    private Tapglue(Context context, @NonNull TGConfiguration configuration) {
        this.context = context;
        config = configuration;

        if (configuration.getToken() == null ||
            configuration.getToken().isEmpty()) {
            throw new RuntimeException("Tapglue token not initialized");
        }

        logger = new TGLog(configuration.debugMode);
        netManager = new TGNetworkManager(configuration, this);
    }

    /**
     * Create network request
     *
     * @return Network requests manager
     */
    @NonNull
    public TGRequests createRequest() {
        return instance.netManager.createRequest();
    }

    /**
     * Get configuration
     *
     * @return Configuration
     */
    @NonNull
    public TGConfiguration getConfiguration() {
        return config;
    }

    /**
     * Get connections manager
     *
     * @return Connections manager
     */
    private TGConnectionManagerImpl getConnectionManager() {
        return connectionManager;
    }

    /**
     * Get used context
     *
     * @return Application context used by library
     */
    public Context getContext() {
        return context;
    }

    /**
     * Get event manager
     *
     * @return Event manager
     */
    private TGEventManagerImpl getEventManager() {
        return eventManager;
    }

    /**
     * Get feed manager
     *
     * @return EventsList manager
     */
    private TGFeedManagerImpl getFeedManager() {
        return feedManager;
    }

    /**
     * Get TapGlue logger
     *
     * @return Logger
     */
    @NonNull
    public TGLog getLogger() {
        return logger;
    }

    /**
     * Get posts manager
     *
     * @return Posts manager
     */
    private TGPostManagerImpl getPostManager() { return postsManager;}

    /**
     * Get user manager
     *
     * @return User manager
     */
    private TGRecommendationManagerImpl getRecommendationManager() {
        return mRecommendationManager;
    }

    /**
     * Get user manager
     *
     * @return User manager
     */
    public TGUserManagerImpl getUserManager() {
        return userManager;
    }

    /**
     * Check if current configuration is correct (exists with non-null token)
     *
     * @return Is config correct?
     */
    public boolean isCorrectConfig() {
        return (!TextUtils.isEmpty(config.getToken()));
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
        String apiBaseUrl = DEFAULT_API_URL;

        boolean debugMode = false;

        int flushIntervalInMs = DEFAULT_FLUSH_INTERVAL;

        @Nullable
        String token = null;

        private boolean cacheEnabled = true;

        /**
         * Get final api url
         *
         * @return Composed API url
         */
        @NonNull
        public String getApiUrl() {
            return apiBaseUrl.charAt(apiBaseUrl.length() - 1) == '/' ?
                (apiBaseUrl + API_VERSION + "/") : (apiBaseUrl + "/" + API_VERSION + "/");
        }

        /**
         * Get flush interval for cached events
         *
         * @return current flush interval in milliseconds
         */
        public long getFlushInterval() {
            return flushIntervalInMs;
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
            flushIntervalInMs = secs * 1000;
            if (flushIntervalInMs <= 0) { flushIntervalInMs = DEFAULT_FLUSH_INTERVAL; }
            else if (flushIntervalInMs > MAX_FLUSH_INTERVAL) {
                flushIntervalInMs = MAX_FLUSH_INTERVAL;
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
            return token;
        }

        /**
         * Set API token
         *
         * @param tapglueToken
         *
         * @return Current object
         */
        @NonNull
        public TGConfiguration setToken(@NonNull String tapglueToken) {
            token = tapglueToken;
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
            return debugMode;
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
            if (newUrl != null) { apiBaseUrl = newUrl; }
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
            this.debugMode = debugMode;
            return this;
        }
    }
}
