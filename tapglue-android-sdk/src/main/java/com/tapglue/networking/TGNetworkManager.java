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

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.telephony.TelephonyManager;
import android.util.Base64;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Protocol;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.internal.Util;
import com.squareup.okhttp.logging.HttpLoggingInterceptor;
import com.tapglue.Tapglue;
import com.tapglue.model.TGBaseObject;
import com.tapglue.model.TGComment;
import com.tapglue.model.TGCommentsList;
import com.tapglue.model.TGConnection;
import com.tapglue.model.TGConnectionUsersList;
import com.tapglue.model.TGErrorList;
import com.tapglue.model.TGEvent;
import com.tapglue.model.TGEventsList;
import com.tapglue.model.TGFeed;
import com.tapglue.model.TGFeedCount;
import com.tapglue.model.TGLike;
import com.tapglue.model.TGLikesList;
import com.tapglue.model.TGLoginUser;
import com.tapglue.model.TGPendingConnections;
import com.tapglue.model.TGPost;
import com.tapglue.model.TGPostsList;
import com.tapglue.model.TGSearchCriteria;
import com.tapglue.model.TGSocialConnections;
import com.tapglue.model.TGSocialId;
import com.tapglue.model.TGUser;
import com.tapglue.networking.requests.TGRequestCallback;
import com.tapglue.networking.requests.TGRequestErrorType;
import com.tapglue.networking.requests.TGRequestType;
import com.tapglue.utils.TGLog;

import java.io.IOException;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;
import java.util.Timer;
import java.util.TimerTask;

import retrofit.Call;
import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;

import static com.tapglue.utils.TGErrorUtil.sendErrorToCallbacks;

public class TGNetworkManager {

    private static final String KEY_AMOUNT = "CACHE_AMOUNT";
    private static final String KEY_QUEUE = "CACHE_QUEUE";
    /**
     * Current library version - string used in requests
     */
    @NonNull
    private final String currentLibraryVersion = "1.0";
    /**
     * Api object
     */
    private final TGApi mApi;
    /**
     * Configuration object
     */
    @NonNull
    private final Tapglue.TGConfiguration mConfiguration;
    /**
     * Request factory
     */
    @NonNull
    private final TGRequestFactory mFactory;
    /**
     * Logging tool
     */
    @NonNull
    private final Tapglue tapglue;
    /**
     * Was analytics request already sent?
     */
    private boolean analyticsSent = false;
    /**
     * Flush timer
     */
    @Nullable
    private Timer flushTimer;

    /**
     * Check if at least one callback is not outdated
     *
     * @param callback list of callbacks
     *
     * @return true if at least one callback is outdated
     */
    static private <OUTOBJECT extends TGBaseObject> boolean hasOutdatedCallback(@Nullable List<TGRequestCallback<OUTOBJECT>> callback) {
        if (callback == null || callback.size() == 0) return false;

        for (int i = 0; i < callback.size(); i++) {
            if (callback.get(i) != null && callback.get(i).callbackIsEnabled()) {
                return true;
            }
        }
        return false;
    }

    public TGNetworkManager(@NonNull Tapglue.TGConfiguration configuration, @NonNull final Tapglue tapglue) {
        mConfiguration = configuration;
        this.tapglue = tapglue;
        mFactory = new TGRequestFactory(this);
        final Context context = tapglue.getContext();
        final String appVersion = getAppVersion(context);

        final String appName = context.getString(context.getApplicationInfo().labelRes);
        final String userAgent = appName + "/" + context.getApplicationInfo().packageName + "; " +
            appVersion +
            " (" + (Build.MODEL != null ? Build.MODEL : "Unknown_model") +
            "; Android " + Build.VERSION.RELEASE + ") Tapglue-SDK/" + currentLibraryVersion;

        OkHttpClient client = new OkHttpClient();
        client.setProtocols(new ArrayList<>(Util.immutableList(Protocol.HTTP_1_1)));
        final HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        // should we enable debugging to console?
        if (mConfiguration.isDebugMode()) { client.interceptors().add(interceptor); }

        // we add custom headers to each request
        client.interceptors().add(new Interceptor() {
            @Override
            public com.squareup.okhttp.Response intercept(@NonNull Chain chain) throws IOException {
                Request currentRequest = chain.request();
                boolean analyticsRequest = currentRequest.httpUrl().toString().contains("analytics");
                Request.Builder builder = currentRequest.newBuilder()
                                                        .addHeader("Authorization", "Basic " + createAuthorizationString())
                                                        .addHeader("User-Agent", userAgent)
                                                        .addHeader("Content-Type", "application/json");

                if (analyticsRequest) {
                    TelephonyManager manager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
                    String carrierName = manager.getNetworkOperatorName();
                    Calendar cal = Calendar.getInstance();
                    TimeZone tz = cal.getTimeZone();

                    builder.addHeader("X-Tapglue-App", appName)
                           .addHeader("X-Tapglue-Appversion", appVersion)
                           .addHeader("X-Tapglue-Carrier", carrierName != null ? carrierName : "Unknown_carrier")
                           .addHeader("X-Tapglue-Manufacturer", Build.MANUFACTURER != null ? Build.MANUFACTURER : "Unknown_manufacturer")
                           .addHeader("X-Tapglue-Model", Build.MODEL != null ? Build.MODEL : "Unknown_model")
                           .addHeader("X-Tapglue-AndroidID", Build.SERIAL != null ? Build.SERIAL : "Unknown_serial")
                           .addHeader("X-Tapglue-OS", Build.VERSION.RELEASE)
                           .addHeader("X-Tapglue-OSVersion", String.valueOf(Build.VERSION.SDK_INT))
                           .addHeader("X-Tapglue-SDKVersion", currentLibraryVersion)
                           .addHeader("X-Tapglue-Timezone", tz.getDisplayName());
                }

                Request req = builder.build();
                return chain.proceed(req);
            }
        });
        Retrofit mRetrofit = new Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create(new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create()))
            .baseUrl(configuration.getApiUrl())
            .client(client)
            .build();
        mApi = mRetrofit.create(TGApi.class);
        createPendingFlush();
    }

    /**
     * Add request to cache
     *
     * @param request Request to be added in cache
     */
    synchronized private void addToCache(@NonNull TGRequest request) {
        getLogger().log("Adding request to cache " + request.toString());
        // first check if object exists in cache - if yes, then if new version is different from cached one, overwrite old one
        SharedPreferences cacheFile = tapglue.getContext().getSharedPreferences(TGNetworkManager.class.toString(), Context.MODE_PRIVATE);

        Integer amount = cacheFile.getInt(KEY_AMOUNT, 0);
        if (amount == 0) {
            String requestString = TGCustomCacheObject.serialize(request);
            cacheFile.edit().putString(KEY_QUEUE + "_0", requestString).putInt(KEY_AMOUNT, 1).apply();
        }
        else {
            String requestString = TGCustomCacheObject.serialize(request);
            cacheFile.edit().putString(KEY_QUEUE + "_" + amount, requestString).putInt(KEY_AMOUNT, amount + 1).apply();
        }
        createPendingFlush();
    }

    /**
     * Create authorization string
     *
     * @return Authorization string
     */
    private String createAuthorizationString() {
        try {
            String auth = String.format("%s:%s",
                mConfiguration.getToken(),
                tapglue.getUserManager().getCurrentUser() != null ? tapglue.getUserManager().getCurrentUser().getSessionToken() : "");
            byte[] data = auth.getBytes("UTF-8");
            return Base64.encodeToString(data, Base64.NO_WRAP);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Create flush request
     */
    synchronized private void createPendingFlush() {
        // check if timer is already working
        if (flushTimer != null) { return; }
        flushTimer = new Timer();
        flushTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                flushCache();
                flushTimer = null;
            }
        }, tapglue.getConfiguration().getFlushInterval());
    }

    /**
     * Create network request
     *
     * @return Network request creation interface
     */
    @NonNull
    public TGNetworkRequests createRequest() {
        return mFactory;
    }

    /**
     * Flush all pending requests from cache
     */
    synchronized private void flushCache() {
        if (!isCacheEnabled() || !isNetworkAvailable()) return;

        getLogger().log("Trying to flush cache");
        SharedPreferences cacheFile = tapglue.getContext().getSharedPreferences(TGNetworkManager.class.toString(), Context.MODE_PRIVATE);

        Integer amount = cacheFile.getInt(KEY_AMOUNT, 0);
        if (amount <= 0) {
            if (flushTimer != null) {
                flushTimer.cancel();
            }
            flushTimer = null;
            getLogger().log("Nothing to flush");
        }
        else {
            TGRequest cachedRequest = TGCustomCacheObject.deserialize(cacheFile.getString(KEY_QUEUE + "_0", null));

            // move cache by 1 backward
            getLogger().log("Flushing cache");
            for (int i = 1; i < amount; i++) {
                cacheFile.edit().putString(KEY_QUEUE + "_" + (i - 1), cacheFile.getString(KEY_QUEUE + "_" + (i), null)).apply();
            }

            cacheFile.edit().remove(KEY_QUEUE + "_" + (amount - 1)).putInt(KEY_AMOUNT, amount - 1).apply();
            if (amount - 1 <= 0) {
                if (flushTimer != null) {
                    flushTimer.cancel();
                }
                flushTimer = null;
            }
            performRequest(cachedRequest, false);
            if (amount - 1 > 0) {
                flushCache();
            }
        }
    }

    /**
     * Get current application version
     *
     * @param context Context used to read this information
     *
     * @return Application version
     */
    private String getAppVersion(@NonNull Context context) {
        String appVersion = "unknown-version";
        try {
            PackageManager manager = context.getPackageManager();
            PackageInfo info = manager.getPackageInfo(
                context.getPackageName(), 0);
            appVersion = info.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return appVersion;
    }

    /**
     * Get logging tool
     *
     * @return
     */
    private TGLog getLogger() {
        return tapglue.getLogger();
    }

    /**
     * Internal info if library should cache requests
     *
     * @return is caching enabled or not
     */
    private boolean isCacheEnabled() {
        return mConfiguration.isCacheEnabled();
    }

    /**
     * Is internet accessible at this moment?
     *
     * @return is the network available
     */
    private boolean isNetworkAvailable() {
        ConnectivityManager cm =
            (ConnectivityManager) tapglue.getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnected();

    }

    /**
     * Perform request and flush data if possible
     *
     * @param request Request to be performed
     */
    void performRequest(@NonNull final TGRequest request) {
        performRequest(request, true);
    }

    /**
     * Actually perform request
     *
     * @param request         Request to be performed
     * @param flushIfPossible Flush all pending requests from cache if there are any available
     */
    private void performRequest(@NonNull final TGRequest request, boolean flushIfPossible) {
        tryToSendAnalytics();
        if (request.getObject() == null
            && request.getRequestType() != TGRequestType.LOGOUT) {
            sendErrorToCallbacks(request.getCallback(), TGRequestErrorType.ErrorType.NULL_INPUT);
            return;
        }
        // check if we got valid user or request is not a login/creation one
        if (tapglue.getUserManager().getCurrentUser() == null &&
            !(request.getRequestType() == TGRequestType.LOGIN
                || (request.getRequestType() == TGRequestType.CREATE && request.getObject() instanceof TGUser)
            )) {
            sendErrorToCallbacks(request.getCallback(), TGRequestErrorType.ErrorType.USER_NOT_LOGGED_IN);
            return;
        }
        // check if request is not outdated
        if (!hasOutdatedCallback(request.getCallback())) return;

        if (!isNetworkAvailable()) {
            // check if request required to be done only when internet is accessible
            if (request.needToBeDoneLive()) {
                sendErrorToCallbacks(request.getCallback(), TGRequestErrorType.ErrorType.NO_NETWORK);
                return;
            }
            else {
                if (isCacheEnabled()) {
                    addToCache(request);
                    for (int i = 0; i < request.getCallback().size(); i++) {
                        ((TGRequestCallback<?>) request.getCallback().get(i)).onRequestFinished(null, false);
                    }
                    return;
                }
                else {
                    // cache and network are not available
                    sendErrorToCallbacks(request.getCallback(), TGRequestErrorType.ErrorType.NO_NETWORK);
                    return;
                }
            }
        }
        else {
            if (flushIfPossible) { flushCache(); }
        }
        // different actions based on request parameters
        switch (request.getRequestType()) {
            case SEARCH:
                if (request.getObject() == null || (!(request.getObject() instanceof TGSearchCriteria))) {
                    sendErrorToCallbacks(request.getCallback(), TGRequestErrorType.ErrorType.UNSUPPORTED_INPUT);
                }
                else {
                    // decode search criteria
                    //                    StringBuilder searchString = new StringBuilder().append("_TG_S2_");
//                    Type token = new TypeToken<List<TGSocialId>>(){}.getType();
                    String criteria = ((TGSearchCriteria) request.getObject()).getSearchCriteria();
                    List<String> criteriaEmail = ((TGSearchCriteria) request.getObject()).getEmailsSearchCriteria();
                    List<TGSocialId> criteriaSocial = ((TGSearchCriteria) request.getObject()).getSocialSearchCriteria();
                    if (criteria!=null) {
                        Call<TGConnectionUsersList> searchRequest = mApi.search(criteria);
                        searchRequest.enqueue(new TGNetworkRequestWithErrorHandling<>(this, request));
                    }else if (criteriaEmail!=null){
                        Call<TGConnectionUsersList> searchRequest = mApi.searchWithEmails(criteriaEmail);
                        searchRequest.enqueue(new TGNetworkRequestWithErrorHandling<>(this, request));
                    }else {
                        List<String> ids = new ArrayList<>();
                        String platform = null;
                        if (criteriaSocial.size()==0){
                            sendErrorToCallbacks(request.getCallback(), TGRequestErrorType.ErrorType.UNSUPPORTED_INPUT);
                            return;
                        }
                        for (int i=0;i<criteriaSocial.size();i++)
                        {
                            ids.add(criteriaSocial.get(i).getId());
                            if (i==0)
                                platform = criteriaSocial.get(i).getPlatform();
                            else{
                                if (!platform.equalsIgnoreCase(criteriaSocial.get(i).getPlatform())){
                                    sendErrorToCallbacks(request.getCallback(), TGRequestErrorType.ErrorType.UNSUPPORTED_INPUT);
                                    return;
                                }
                            }
                        }
                        Call<TGConnectionUsersList> searchRequest = mApi.searchWithSocialIds(ids,platform);
                        searchRequest.enqueue(new TGNetworkRequestWithErrorHandling<>(this, request));
                    }
                }
                break;
            case LOGOUT:
                Call<Object> userRequest = mApi.logout();
                userRequest.enqueue(new TGNetworkRequestWithErrorHandling<>(this, request));
                break;
            case LOGIN:
                if (request.getObject() instanceof TGLoginUser) {
                    Call<TGUser> userRequestLogin = mApi.login((TGLoginUser) request.getObject());
                    userRequestLogin.enqueue(new TGNetworkRequestWithErrorHandling<>(this, request));
                }
                else {
                    sendErrorToCallbacks(request.getCallback(), TGRequestErrorType.ErrorType.UNSUPPORTED_INPUT);
                }
                break;
            case CREATE:
                if (request.getObject() instanceof TGUser) {
                    // user request
                    Call<TGUser> userRequestCreateUser = mApi.createUser((TGUser) request.getObject());
                    userRequestCreateUser.enqueue(new TGNetworkRequestWithErrorHandling<>(this, request));
                }
                else if (request.getObject() instanceof TGConnection) {
                    // connection request
                    TGConnection connectionCreateObject = (TGConnection) request.getObject();
                    if (connectionCreateObject.getCacheObjectType() == null) {
                        sendErrorToCallbacks(request.getCallback(), TGRequestErrorType.ErrorType.UNSUPPORTED_INPUT);
                    }
                    else {
                        TGConnection connection = new TGConnection()
                            .setUserToId(connectionCreateObject.getUserToId())
                            .setUserFromId(connectionCreateObject.getUserFromId())
                            .setType(connectionCreateObject.getType())
                            .setState(connectionCreateObject.getState());
                        Call<TGConnection> createConnectionRequest = mApi.createConnection(connection);
                        createConnectionRequest.enqueue(new TGNetworkRequestWithErrorHandling<>(this, request));
                    }

                }
                else if (request.getObject() instanceof TGEvent) {
                    // event request
                    Call<TGEvent> createEventRequest = mApi.createEvent((TGEvent) request.getObject());
                    createEventRequest.enqueue(new TGNetworkRequestWithErrorHandling<>(this, request));
                } else if (request.getObject() instanceof TGPost){
                    Call<TGPost> createRequest = mApi.createPost((TGPost) request.getObject());
                    createRequest.enqueue(new TGNetworkRequestWithErrorHandling<>(this,request));
                    return;
                } else if (request.getObject() instanceof TGLike){
                    Call<TGLike> createRequest = mApi.likePost(((TGLike) request.getObject()).getPostId());
                    createRequest.enqueue(new TGNetworkRequestWithErrorHandling<>(this,request));
                    return;
                } else if (request.getObject() instanceof TGComment){
                    Call<TGComment> createRequest = mApi.createComment(((TGComment) request.getObject()).getPostId(), (TGComment) request.getObject());
                    createRequest.enqueue(new TGNetworkRequestWithErrorHandling<>(this,request));
                    return;
                }
                else {
                    sendErrorToCallbacks(request.getCallback(), TGRequestErrorType.ErrorType.UNSUPPORTED_INPUT);
                    return;
                }
                break;
            case READ:
                if (request.getObject() instanceof TGUser) {
                    // user request
                    Call<TGUser> userRequestReadUser = mApi.getUser(((TGUser)request.getObject()).getReadRequestObjectId());
                    userRequestReadUser.enqueue(new TGNetworkRequestWithErrorHandling<>(this, request));
                }
                else if (request.getObject() instanceof TGConnection) {
                    // connection request
                    TGConnection readConnectionObject = (TGConnection) request.getObject();
                    if (readConnectionObject.getUserFromId() == null) {
                        // read connections from current user
                        if (readConnectionObject.getType() == null) {
                            if (readConnectionObject.getState() == TGConnection.TGConnectionState.CONFIRMED){
                                // read confirmed
                                Call<TGPendingConnections> getFollowedForCurrentUserRequest = mApi.getConfirmedConnections();
                                getFollowedForCurrentUserRequest.enqueue(new TGNetworkRequestWithErrorHandling<>(this, request));
                            }else if (readConnectionObject.getState() == TGConnection.TGConnectionState.REJECTED){
                                // read rejected
                                Call<TGPendingConnections> getFollowedForCurrentUserRequest = mApi.getRejectedConnections();
                                getFollowedForCurrentUserRequest.enqueue(new TGNetworkRequestWithErrorHandling<>(this, request));
                            }else {
                                // read followers
                                Call<TGConnectionUsersList> getFollowedForCurrentUserRequest = mApi.getFollowed();
                                getFollowedForCurrentUserRequest.enqueue(new TGNetworkRequestWithErrorHandling<>(this, request));
                            }
                        }
                        else if (readConnectionObject.getType() == TGConnection.TGConnectionType.FOLLOW) {
                            // get followed
                            Call<TGConnectionUsersList> getFollowsForCurrentUserRequest = mApi.getFollows();
                            getFollowsForCurrentUserRequest.enqueue(new TGNetworkRequestWithErrorHandling<>(this, request));
                        }
                        else if (readConnectionObject.getType() == TGConnection.TGConnectionType.FRIEND) {
                            // get friends
                            Call<TGConnectionUsersList> getFriendsForCurrentUserRequest = mApi.getFriends();
                            getFriendsForCurrentUserRequest.enqueue(new TGNetworkRequestWithErrorHandling<>(this, request));
                        }
                        else {
                            // option possible only if library would be extended without checking this
                            sendErrorToCallbacks(request.getCallback(), TGRequestErrorType.ErrorType.UNSUPPORTED_INPUT);
                        }
                    }
                    else {
                        // create connection for other user
                        if (readConnectionObject.getType() == null) {
                            // read followers
                            Call<TGConnectionUsersList> getFollowedForUserRequest = mApi.getFollowedForUser(readConnectionObject.getUserFromId());
                            getFollowedForUserRequest.enqueue(new TGNetworkRequestWithErrorHandling<>(this, request));
                        }
                        else if (readConnectionObject.getType() == TGConnection.TGConnectionType.FOLLOW) {
                            // get followed
                            Call<TGConnectionUsersList> getFollowsForUserRequest = mApi.getFollowsForUser(readConnectionObject.getUserFromId());
                            getFollowsForUserRequest.enqueue(new TGNetworkRequestWithErrorHandling<>(this, request));
                        }
                        else if (readConnectionObject.getType() == TGConnection.TGConnectionType.FRIEND) {
                            // get friends
                            Call<TGConnectionUsersList> getFriendsForUserRequest = mApi.getFriendsForUser(readConnectionObject.getUserFromId());
                            getFriendsForUserRequest.enqueue(new TGNetworkRequestWithErrorHandling<>(this, request));
                        }
                        else {
                            // option possible only if library would be extended without checking this
                            sendErrorToCallbacks(request.getCallback(), TGRequestErrorType.ErrorType.UNSUPPORTED_INPUT);
                        }
                    }
                }
                else if (request.getObject() instanceof TGEvent) {
                    // event request
                    if (request.getObject().getReadRequestUserId() == null) {
                        Call<TGEvent> readEventRequest = mApi.getEvent(((TGEvent)request.getObject()).getReadRequestObjectId());
                        readEventRequest.enqueue(new TGNetworkRequestWithErrorHandling<>(this, request));
                    }
                    else {
                        // read event for selected user
                        Call<TGEvent> readEventRequest = mApi.getEvent(request.getObject().getReadRequestUserId(), ((TGEvent)request.getObject()).getReadRequestObjectId());
                        readEventRequest.enqueue(new TGNetworkRequestWithErrorHandling<>(this, request));
                    }
                    return;
                }
                else if (request.getObject() instanceof TGFeed){
                    // for feed
                    if ((((TGFeed) request.getObject()).getUnreadCount() == null) || (((TGFeed) request.getObject()).getUnreadCount() != 1)) {
                        // get feed
                        Call<TGFeed> feedRequest = mApi.getFeed();
                        feedRequest.enqueue(new TGNetworkRequestWithErrorHandling<>(this, request));
                    }
                    else {
                        // get unread feed
                        Call<TGEventsList> unreadFeedRequest = mApi.getUnreadFeed();
                        unreadFeedRequest.enqueue(new TGNetworkRequestWithErrorHandling<>(this, request));
                    }
                 return;
                }
                else if (request.getObject() instanceof TGEventsList) {
                    // for events list
                    if (request.getObject().getReadRequestUserId() == null) {
                        Call<TGEventsList> readEventsRequest = mApi.getEvents();
                        readEventsRequest.enqueue(new TGNetworkRequestWithErrorHandling<>(this, request));
                    }
                    else {
                        // read events from selected user
                        Call<TGEventsList> readEventsRequest = mApi.getEvents(request.getObject().getReadRequestUserId());
                        readEventsRequest.enqueue(new TGNetworkRequestWithErrorHandling<>(this, request));
                    }
                    return;
                }
                else if (request.getObject() instanceof TGFeedCount) {
                    // feed count request
                    Call<TGFeedCount> countRequest = mApi.getUnreadFeedCount();
                    countRequest.enqueue(new TGNetworkRequestWithErrorHandling<>(this, request));
                }
                else if (request.getObject() instanceof TGPendingConnections) {
                    Call<TGPendingConnections> connectionsRequest = mApi.getPendingConnections();
                    connectionsRequest.enqueue(new TGNetworkRequestWithErrorHandling<>(this, request));
                }else if (request.getObject() instanceof TGPost){
                    if (request.getObject().getReadRequestUserId()==null) {
                        Call<TGPost> req = mApi.getPost(request.getObject().getReadRequestObjectStringId());
                        req.enqueue(new TGNetworkRequestWithErrorHandling<>(this, request));
                    }else if (request.getObject().getReadRequestUserId().longValue() == TGRequestFactory.POST_READ_ID_GET_ALL.longValue()){
                        Call<TGPostsList> req = mApi.getPosts();
                        req.enqueue(new TGNetworkRequestWithErrorHandling<>(this, request));
                    }else if (request.getObject().getReadRequestUserId().longValue() == TGRequestFactory.POST_READ_ID_GET_FEED.longValue()){
                        Call<TGPostsList> req = mApi.getFeedPosts();
                        req.enqueue(new TGNetworkRequestWithErrorHandling<>(this, request));
                    }else if (request.getObject().getReadRequestUserId().longValue() == TGRequestFactory.POST_READ_ID_GET_MY.longValue()) {
                        Call<TGPostsList> req = mApi.getMyPosts();
                        req.enqueue(new TGNetworkRequestWithErrorHandling<>(this, request));
                    }else if (request.getObject().getReadRequestUserId().longValue() == TGRequestFactory.POST_READ_ID_USER.longValue()) {
                        Call<TGPostsList> req = mApi.getUserPosts(request.getObject().getReadRequestObjectId());
                        req.enqueue(new TGNetworkRequestWithErrorHandling<>(this, request));
                    }
                    return;
                } else if (request.getObject() instanceof TGLikesList){
                    Call<TGLikesList> req = mApi.getPostLikes(request.getObject().getReadRequestObjectStringId());
                    req.enqueue(new TGNetworkRequestWithErrorHandling<>(this, request));
                    return;
                } else if (request.getObject() instanceof TGCommentsList){
                    Call<TGCommentsList> req = mApi.getCommentsForPost(request.getObject().getReadRequestObjectStringId());
                    req.enqueue(new TGNetworkRequestWithErrorHandling<>(this, request));
                    return;
                }
                else {
                    sendErrorToCallbacks(request.getCallback(), TGRequestErrorType.ErrorType.UNSUPPORTED_INPUT);
                    return;

                }
                break;
            case UPDATE:
                if (request.getObject() instanceof TGSocialConnections) {
                    Call<TGConnectionUsersList> socialRequest = mApi.socialConnections((TGSocialConnections) request.getObject());
                    socialRequest.enqueue(new TGNetworkRequestWithErrorHandling<>(this, request));
                }
                else if (request.getObject() instanceof TGUser) {
                    // user request
                    Call<TGUser> userRequestUpdate = mApi.updateUser((TGUser) request.getObject());
                    userRequestUpdate.enqueue(new TGNetworkRequestWithErrorHandling<>(this, request));
                    return;
                }
                else if (request.getObject() instanceof TGEvent) {
                    // event request
                    Call<TGEvent> eventUpdateRequest = mApi.updateEvent(((TGEvent) request.getObject()).getID(), (TGEvent) request.getObject());
                    eventUpdateRequest.enqueue(new TGNetworkRequestWithErrorHandling<>(this, request));
                    return;
                }
                else if (request.getObject() instanceof TGConnection) {
                    // connection request
                    sendErrorToCallbacks(request.getCallback(), TGRequestErrorType.ErrorType.UNSUPPORTED_INPUT);
                    return;
                }else if (request.getObject() instanceof TGPost){
                    Call<TGPost> updateRequest = mApi.updatePost(((TGPost) request.getObject()).getReadRequestObjectStringId(), (TGPost) request.getObject());
                    updateRequest.enqueue(new TGNetworkRequestWithErrorHandling<>(this,request));
                    return;
                } else if (request.getObject() instanceof TGComment){
                    Call<TGComment> updateReq = mApi.updatePostComment(((TGComment) request.getObject()).getPostId(),
                            ((TGComment) request.getObject()).getID(), (TGComment) request.getObject());
                    updateReq.enqueue(new TGNetworkRequestWithErrorHandling<>(this,request));
                    return;
                }
                else {
                    sendErrorToCallbacks(request.getCallback(), TGRequestErrorType.ErrorType.UNSUPPORTED_INPUT);
                    return;
                }
            case DELETE:
                if (request.getObject() instanceof TGUser) {
                    // user request
                    Call<Object> userRequestDelete = mApi.deleteUser();
                    userRequestDelete.enqueue(new TGNetworkRequestWithErrorHandling<>(this, request));
                }
                else if (request.getObject() instanceof TGConnection) {
                    // connection request
                    TGConnection connectionCreateObject = (TGConnection) request.getObject();
                    if (connectionCreateObject.getCacheObjectType() == null) {
                        sendErrorToCallbacks(request.getCallback(), TGRequestErrorType.ErrorType.UNSUPPORTED_INPUT);
                    }
                    else {
                        Call<Object> removeConnectionRequest = mApi.removeConnection(connectionCreateObject.getUserToId(), connectionCreateObject.getType().toString());
                        removeConnectionRequest.enqueue(new TGNetworkRequestWithErrorHandling<>(this, request));
                    }
                }
                else if (request.getObject() instanceof TGEvent) {
                    // event request
                    Call<Object> removeEventRequest = mApi.removeEvent(((TGEvent) request.getObject()).getID());
                    removeEventRequest.enqueue(new TGNetworkRequestWithErrorHandling<>(this, request));
                }else if (request.getObject() instanceof TGPost){
                    Call<Object> removePostRequest = mApi.removePost(((TGPost) request.getObject()).getReadRequestObjectStringId());
                    removePostRequest.enqueue(new TGNetworkRequestWithErrorHandling<>(this,request));
                } else if (request.getObject() instanceof TGLike){
                    Call<Object> removePostRequest = mApi.unlikePost(((TGLike) request.getObject()).getPostId());
                    removePostRequest.enqueue(new TGNetworkRequestWithErrorHandling<>(this,request));
                } else if (request.getObject() instanceof TGComment){
                    Call<Object> removePostRequest = mApi.removePostComment(((TGComment) request.getObject()).getPostId(),
                            ((TGComment)request.getObject()).getReadRequestObjectId());
                    removePostRequest.enqueue(new TGNetworkRequestWithErrorHandling<>(this, request));
                }
                else {
                    sendErrorToCallbacks(request.getCallback(), TGRequestErrorType.ErrorType.UNSUPPORTED_INPUT);
                    return;
                }
                break;
        }
    }

    /**
     * Try to send analytics request - if not possible or already done, sleep until next
     * possibility
     */
    private void tryToSendAnalytics() {
        if (analyticsSent || !mConfiguration.isAnalyticsEnabled()) { return; }
        if (!isNetworkAvailable()) { return; }
        getLogger().log("Trying to send analytics");
        analyticsSent = true;
        mApi.sendAnalytics().enqueue(new Callback<Object>() {
            @Override
            public void onFailure(Throwable t) {
                analyticsSent = false;
                getLogger().log("Analytics sending fail, will retry in future");
            }

            @Override
            public void onResponse(Response<Object> response, Retrofit retrofit) {
                // do nothing
                getLogger().log("Analytics sent");
            }
        });
    }

    /**
     * Extended network request handling - only for use with TGNetworkManager
     *
     * @param <OBJECT>
     */
    private static class TGNetworkRequestWithErrorHandling<OBJECT extends TGBaseObject, OUTOBJECT extends TGBaseObject> implements Callback<OUTOBJECT> {

        @NonNull
        private final WeakReference<TGNetworkManager> mNetManager;
        private final TGRequest<OBJECT, OUTOBJECT> mRequest;

        public TGNetworkRequestWithErrorHandling(TGNetworkManager netManager, TGRequest<OBJECT, OUTOBJECT> request) {
            mRequest = request;
            mNetManager = new WeakReference<>(netManager);
        }

        @Override
        public void onFailure(@NonNull Throwable t) {
            // check if request is not outdated
            if (!hasOutdatedCallback(mRequest.getCallback())) { return; }
            mNetManager.get().getLogger().logE(t);
            if (mRequest.needToBeDoneLive() || !mNetManager.get().isCacheEnabled()) {
                for (int i = 0; i < mRequest.getCallback().size(); i++) {
                    (mRequest.getCallback().get(i)).onRequestError(new TGRequestErrorType(TGRequestErrorType.ErrorType.SERVER_ERROR));
                }
            }
            else {
                mNetManager.get().addToCache(mRequest);
                mNetManager.get().getLogger().log("Request added to cache");
            }
        }

        @Override
        public void onResponse(@NonNull Response<OUTOBJECT> response, Retrofit retrofit) {
            // check if request is not outdated
            if (!hasOutdatedCallback(mRequest.getCallback())) { return; }
            // interpret error code
            if (response.errorBody() != null) {
                int intCh;
                try {
                    Reader stream = response.errorBody().charStream();
                    StringBuilder builder = new StringBuilder();
                    while ((intCh = stream.read()) != -1) {
                        builder.append((char) intCh);
                    }

                    String stringResponse = builder.toString();
                    TGErrorList error = new Gson().fromJson(stringResponse, new TypeToken<TGErrorList>() {}.getType());

                    for (int i = 0; i < error.getErrors().size(); i++) {
                        sendErrorToCallbacks(mRequest.getCallback(), error.getErrors().get(i).getErrorCode(), error.getErrors().get(i).getMessage());
                    }

                    return;
                } catch (IOException e) {
                    TGRequestErrorType.ErrorType errorType = TGRequestErrorType.ErrorType.get(response.code());
                    if (errorType == null) {
                        errorType = TGRequestErrorType.ErrorType.UNKNOWN_ERROR;
                    }
                    sendErrorToCallbacks(mRequest.getCallback(), errorType);
                    return;
                }
            }

            for (int i = 0; i < mRequest.getCallback().size(); i++) {
                mRequest.getCallback().get(i).onRequestFinished(response.body(), true);
            }
        }
    }
}
