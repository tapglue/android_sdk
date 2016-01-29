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

package com.tapglue.tapgluetests;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.tapglue.Tapglue;
import com.tapglue.model.TGComment;
import com.tapglue.model.TGConnection;
import com.tapglue.model.TGEvent;
import com.tapglue.model.TGEventsList;
import com.tapglue.model.TGFeed;
import com.tapglue.model.TGLike;
import com.tapglue.model.TGPendingConnections;
import com.tapglue.model.TGPost;
import com.tapglue.model.TGPostsList;
import com.tapglue.model.TGRecommendedUsers.TGRecommendationPeriod;
import com.tapglue.model.TGRecommendedUsers.TGRecommendationType;
import com.tapglue.model.TGUser;
import com.tapglue.model.TGUsersList;
import com.tapglue.model.TGVisibility;
import com.tapglue.networking.requests.TGRequestCallback;
import com.tapglue.networking.requests.TGRequestErrorType;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class TestActivity extends AppCompatActivity {

//    TEST SCENARIOS
//    1.1 Create and Login User A
//    1.2 Update metadata User A
//    1.3 Retrieve User A
//
//    2.1 Create and Login User B
//    2.2 Create Event with User B visibility 10
//            2.3 Create Event with User B visibility 20
//            2.4 Create Event with User B visibility 30
//            2.5 Retrieve `me/events` of User B and check for created events
//    2.6 Create friend connection to User A of state pending
//    2.7 Retrieve me/friends (User B) and check that there are 0 friends
//
//    3.1  Login User A
//    3.2 Retrieve pending connections and check that there is 1
//            3.3 Conform pending connection from User B
//    3.4 Retrieve me/friends (User A) and check for User B as friend
//    3.5 Retrieve me/feed and check for 2 events (visibility 20 and 30)
//    3.6 Delete Connection to User B
//    3.7 Retrieve users/:userBID/events and check to see only 1 events (visibility 30)

//    POSTS & LIKES & COMMENTS API
//    3.7.1 Create post
//    3.7.2 Comment post
//    3.7.3 Like post
//    3.7.4 Check posts
//    3.7.5 Remove post
//    3.7.6 Check posts
//    3.8 Delete User A
//
//    4.1 Login User B
//    4.2 Check /me/friends to be empty
//    4.3 Delete User B

    private static final int TEST_1_1 = 1;

    private static final int TEST_1_2 = 2;

    private static final int TEST_1_3 = 3;

    private static final int TEST_2_1 = 4;

    private static final int TEST_2_2 = 5;

    private static final int TEST_2_3 = 6;

    private static final int TEST_2_4 = 7;

    private static final int TEST_2_5 = 8;

    private static final int TEST_2_6 = 9;

    private static final int TEST_2_7 = 10;

    private static final int TEST_3_1 = 11;

    private static final int TEST_3_2 = 12;

    private static final int TEST_3_3 = 13;

    private static final int TEST_3_4 = 14;

    private static final int TEST_3_5 = 15;

    private static final int TEST_3_6 = 16;

    private static final int TEST_3_7 = 17;

    private static final int TEST_3_7_1 = 18;

    private static final int TEST_3_7_2 = 19;

    private static final int TEST_3_7_3 = 20;

    private static final int TEST_3_7_4 = 21;

    private static final int TEST_3_7_5 = 22;

    private static final int TEST_3_7_6 = 23;

    private static final int TEST_3_8 = 24;

    private static final int TEST_4_1 = 25;

    private static final int TEST_4_2 = 26;

    private static final int TEST_4_3 = 27;

    private static final int TEST_4_4 = 28;

    //    private static final String TEST_METADATA = "Test metadata object";
    private static final int TEST_PREPARE = 0;

    private static boolean initialRun = false;

    public TestController testController;

    private Long createdEventID;

    private JsonElement metadata = computeMetadata();

    @Nullable
    private TGPost post;

    @Nullable
    private TGComment postComment;

    @NonNull
    private Map<String, String> socialMap = new HashMap<>();

    @Nullable
    private TGUser userA;

    @Nullable
    private TGUser userB;

    private static JsonElement computeMetadata() {
        JsonObject result = new JsonObject();
        result.addProperty("metadata", "this is metadata");
        return result.get("metadata");
    }

    public void doTest(@NonNull Runnable runnable) {
        final String randomUserName = "TestUser_" + new Date().getTime();
        final String randomUserName2 = "TestUser2_" + new Date().getTime();
        socialMap.put("facebook", "facebookid");

        doTest(TEST_PREPARE, randomUserName, randomUserName2, runnable);
    }

    private void doTest(int testStage, final String randomUserName, final String randomUserName2, @NonNull final Runnable runnable) {
        if (!initialRun) {
            testController.log("Tests started", false, false);
            initialRun = true;
        }

        switch (testStage) {
            case TEST_PREPARE:
                doTest(TEST_1_1, randomUserName, randomUserName2, runnable);
                break;
            case TEST_1_1:
                Tapglue.user().createAndLoginUserWithUsernameAndMail(randomUserName, randomUserName, randomUserName + "@gmail.com", new TGRequestCallback<Boolean>() {
                    @Override
                    public boolean callbackIsEnabled() {
                        return true;
                    }

                    @Override
                    public void onRequestError(TGRequestErrorType cause) {
                        testController.log("#1.1 finished with error");
                    }

                    @Override
                    public void onRequestFinished(Boolean output, boolean changeDoneOnline) {
                        testController.log("#1.1 finished correctly");
                        doTest(TEST_1_2, randomUserName, randomUserName2, runnable);
                    }
                });
                break;
            case TEST_1_2:
                TGUser currentUser = Tapglue.user().getCurrentUser();
                if (currentUser == null) {
                    testController.log("#1.1 finished with error");
                    break;
                }
                Tapglue.user().saveChangesToCurrentUser(currentUser.setSocialIds(socialMap).setMetadata(metadata), new TGRequestCallback<Boolean>() {
                    @Override
                    public boolean callbackIsEnabled() {
                        return true;
                    }

                    @Override
                    public void onRequestError(TGRequestErrorType cause) {
                        testController.log("#1.2 finished with error");
                    }

                    @Override
                    public void onRequestFinished(Boolean output, boolean changeDoneOnline) {
                        TGUser currentUser = Tapglue.user().getCurrentUser();
                        assert currentUser != null;
                        Tapglue.user().saveChangesToCurrentUser(currentUser.setMetadata(metadata), new TGRequestCallback<Boolean>() {
                            @Override
                            public boolean callbackIsEnabled() {
                                return true;
                            }

                            @Override
                            public void onRequestError(TGRequestErrorType cause) {
                                testController.log("#1.2 finished with error");
                            }

                            @Override
                            public void onRequestFinished(Boolean output, boolean changeDoneOnline) {
                                testController.log("#1.2 finished correctly");
                                doTest(TEST_1_3, randomUserName, randomUserName2, runnable);
                            }
                        });
                    }
                });
                break;
            case TEST_1_3:
                Tapglue.user().logout(new TGRequestCallback<Boolean>() {
                    @Override
                    public boolean callbackIsEnabled() {
                        return true;
                    }

                    @Override
                    public void onRequestError(TGRequestErrorType cause) {
                        testController.log("#1.3 finished with error");
                    }

                    @Override
                    public void onRequestFinished(Boolean output, boolean changeDoneOnline) {
                        Tapglue.user().login(randomUserName, randomUserName, new TGRequestCallback<Boolean>() {
                            @Override
                            public boolean callbackIsEnabled() {
                                return true;
                            }

                            @Override
                            public void onRequestError(TGRequestErrorType cause) {
                                testController.log("#1.3 finished with error");
                            }

                            @Override
                            public void onRequestFinished(Boolean output, boolean changeDoneOnline) {
                                TGUser currentUser = Tapglue.user().getCurrentUser();
                                if (currentUser == null) {
                                    testController.log("#1.3 finished with error");
                                    return;
                                }

                                if (!currentUser.getSocialIds().get("facebook").equalsIgnoreCase(socialMap.get("facebook"))) {
                                    testController.log("#1.3 finished with error");
                                    return;
                                }

                                userA = Tapglue.user().getCurrentUser();
                                testController.log("#1.3 finished correctly");
                                doTest(TEST_2_1, randomUserName, randomUserName2, runnable);
                            }
                        });
                    }
                });
                break;
            case TEST_2_1:
                Tapglue.user().createAndLoginUserWithUsernameAndMail(randomUserName2, randomUserName2, randomUserName2 + "@gmail.com", new TGRequestCallback<Boolean>() {
                    @Override
                    public boolean callbackIsEnabled() {
                        return true;
                    }

                    @Override
                    public void onRequestError(TGRequestErrorType cause) {
                        testController.log("#2.1 finished with error");
                    }

                    @Override
                    public void onRequestFinished(Boolean output, boolean changeDoneOnline) {
                        TGUser currentUser = Tapglue.user().getCurrentUser();
                        if (currentUser == null) {
                            testController.log("#2.1 finished with error");
                            return;
                        }

                        if (!currentUser.getUserName().equalsIgnoreCase(randomUserName2)) {
                            testController.log("#2.1 finished with error");
                            return;
                        }

                        testController.log("#2.1 finished correctly");

                        userB = Tapglue.user().getCurrentUser();
                        assert userB != null;

                        Tapglue.user().saveChangesToCurrentUser(userB.setMetadata(metadata), new TGRequestCallback<Boolean>() {
                            @Override
                            public boolean callbackIsEnabled() {
                                return true;
                            }

                            @Override
                            public void onRequestError(TGRequestErrorType cause) {
                                testController.log("#2.2 finished with error");
                            }

                            @Override
                            public void onRequestFinished(Boolean output, boolean changeDoneOnline) {
                                testController.log("#2.2 finished correctly");
                                doTest(TEST_2_2, randomUserName, randomUserName2, runnable);
                            }
                        });
                    }
                });
                break;
            case TEST_2_2:
                Tapglue.event().createEvent(new TGEvent().setVisibility(TGVisibility.Private).setType("defaultType"), new TGRequestCallback<TGEvent>() {
                    @Override
                    public boolean callbackIsEnabled() {
                        return true;
                    }

                    @Override
                    public void onRequestError(TGRequestErrorType cause) {
                        testController.log("#2.2 finished with error");
                    }

                    @Override
                    public void onRequestFinished(@NonNull TGEvent output, boolean changeDoneOnline) {
                        if (output.getVisibility() != TGVisibility.Private) {
                            testController.log("#2.2 finished with error");
                            return;
                        }

                        testController.log("#2.2 finished correctly");
                        doTest(TEST_2_3, randomUserName, randomUserName2, runnable);
                    }
                });
                break;
            case TEST_2_3:
                Tapglue.event().createEvent(new TGEvent().setVisibility(TGVisibility.Connections).setType("defaultType"), new TGRequestCallback<TGEvent>() {
                    @Override
                    public boolean callbackIsEnabled() {
                        return true;
                    }

                    @Override
                    public void onRequestError(TGRequestErrorType cause) {
                        testController.log("#2.3 finished with error");
                    }

                    @Override
                    public void onRequestFinished(@NonNull TGEvent output, boolean changeDoneOnline) {
                        if (output.getVisibility() != TGVisibility.Connections) {
                            testController.log("#2.3 finished with error");
                            return;
                        }

                        testController.log("#2.3 finished correctly");
                        doTest(TEST_2_4, randomUserName, randomUserName2, runnable);
                    }
                });
                break;
            case TEST_2_4:
                Tapglue.event().createEvent(new TGEvent().setVisibility(TGVisibility.Public).setType("defaultType"), new TGRequestCallback<TGEvent>() {
                    @Override
                    public boolean callbackIsEnabled() {
                        return true;
                    }

                    @Override
                    public void onRequestError(TGRequestErrorType cause) {
                        testController.log("#2.4 finished with error");
                    }

                    @Override
                    public void onRequestFinished(@NonNull TGEvent output, boolean changeDoneOnline) {
                        if (output.getVisibility() != TGVisibility.Public) {
                            testController.log("#2.4 finished with error");
                            return;
                        }

                        testController.log("#2.4 finished correctly");
                        doTest(TEST_2_5, randomUserName, randomUserName2, runnable);
                    }
                });
                break;
            case TEST_2_5:
                Tapglue.feed().retrieveEventsForCurrentUser(new TGRequestCallback<TGEventsList>() {
                    @Override
                    public boolean callbackIsEnabled() {
                        return true;
                    }

                    @Override
                    public void onRequestError(TGRequestErrorType cause) {
                        testController.log("#2.5 finished with error");
                    }

                    @Override
                    public void onRequestFinished(@Nullable TGEventsList output, boolean changeDoneOnline) {
                        if (output == null || output.getEvents() == null || output.getEvents().size() != 3) {
                            testController.log("#2.5 finished with error");
                            return;
                        }

                        testController.log("#2.5 finished correctly");
                        doTest(TEST_2_6, randomUserName, randomUserName2, runnable);
                    }
                });
                break;
            case TEST_2_6:
                assert userA != null;
                Tapglue.connection().friendUser(userA.getID(), new TGRequestCallback<Boolean>() {
                    @Override
                    public boolean callbackIsEnabled() {
                        return true;
                    }

                    @Override
                    public void onRequestError(TGRequestErrorType cause) {
                        testController.log("#2.6 finished with error");
                    }

                    @Override
                    public void onRequestFinished(Boolean output, boolean changeDoneOnline) {
                        testController.log("#2.6 finished correctly");
                        doTest(TEST_2_7, randomUserName, randomUserName2, runnable);
                    }
                });
                break;
            case TEST_2_7:
                Tapglue.user().retrieveFriendsForCurrentUser(new TGRequestCallback<TGUsersList>() {
                    @Override
                    public boolean callbackIsEnabled() {
                        return true;
                    }

                    @Override
                    public void onRequestError(TGRequestErrorType cause) {
                        testController.log("#2.7 finished with error");
                    }

                    @Override
                    public void onRequestFinished(@Nullable TGUsersList output, boolean changeDoneOnline) {
                        if (output != null && (output.getUsers() != null && output.getUsers().size() != 0)) {
                            testController.log("#2.7 finished with error");
                            return;
                        }

                        testController.log("#2.7 finished correctly");
                        doTest(TEST_3_1, randomUserName, randomUserName2, runnable);
                    }
                });
                break;
            case TEST_3_1:
                Tapglue.user().login(randomUserName, randomUserName, new TGRequestCallback<Boolean>() {
                    @Override
                    public boolean callbackIsEnabled() {
                        return true;
                    }

                    @Override
                    public void onRequestError(TGRequestErrorType cause) {
                        testController.log("#3.1 finished with error");
                    }

                    @Override
                    public void onRequestFinished(Boolean output, boolean changeDoneOnline) {
                        testController.log("#3.1 finished correctly");
                        doTest(TEST_3_2, randomUserName, randomUserName2, runnable);
                    }
                });
                break;
            case TEST_3_2:
                Tapglue.connection().getPendingConnections(new TGRequestCallback<TGPendingConnections>() {
                    @Override
                    public boolean callbackIsEnabled() {
                        return true;
                    }

                    @Override
                    public void onRequestError(TGRequestErrorType cause) {
                        testController.log("#3.2 finished with error");
                    }

                    @Override
                    public void onRequestFinished(@Nullable TGPendingConnections output, boolean changeDoneOnline) {
                        if (output == null || output.getIncomingCount() == null || output.getIncomingCount() != 1) {
                            testController.log("#3.2 finished with error");
                            return;
                        }

                        testController.log("#3.2 finished correctly");
                        doTest(TEST_3_3, randomUserName, randomUserName2, runnable);
                    }
                });
                break;
            case TEST_3_3:
                assert userB != null;

                Tapglue.connection().confirmConnection(userB.getID(), TGConnection.TGConnectionType.FRIEND,
                    new TGRequestCallback<Boolean>() {
                        @Override
                        public boolean callbackIsEnabled() {
                            return true;
                        }

                        @Override
                        public void onRequestError(TGRequestErrorType cause) {
                            testController.log("#3.3 finished with error");
                        }

                        @Override
                        public void onRequestFinished(Boolean output, boolean changeDoneOnline) {
                            testController.log("#3.3 finished correctly");
                            doTest(TEST_3_4, randomUserName, randomUserName2, runnable);
                        }
                    });
                break;
            case TEST_3_4:
                Tapglue.user().retrieveFriendsForCurrentUser(new TGRequestCallback<TGUsersList>() {
                    @Override
                    public boolean callbackIsEnabled() {
                        return true;
                    }

                    @Override
                    public void onRequestError(TGRequestErrorType cause) {
                        testController.log("#3.4 finished with error");
                    }

                    @Override
                    public void onRequestFinished(@Nullable TGUsersList output, boolean changeDoneOnline) {
                        if (output == null || output.getUsers() == null || output.getUsers().size() != 1) {
                            testController.log("#3.4 finished with error");
                        }
                        else {
                            TGUser user = output.getUsers().get(0);
                            if (user == null || userB == null || user.getID().longValue() != userB.getID().longValue()) {
                                testController.log("#3.4 finished with error");
                                return;
                            }

                            testController.log("#3.4 finished correctly");
                            doTest(TEST_3_5, randomUserName, randomUserName2, runnable);
                        }
                    }
                });
                break;
            case TEST_3_5:
                Tapglue.feed().retrieveNewsFeedForCurrentUser(new TGRequestCallback<TGFeed>() {
                    @Override
                    public boolean callbackIsEnabled() {
                        return true;
                    }

                    @Override
                    public void onRequestError(TGRequestErrorType cause) {
                        testController.log("#3.5 finished with error");
                    }

                    @Override
                    public void onRequestFinished(@Nullable TGFeed output, boolean changeDoneOnline) {
                        if (output == null || output.getEvents() == null || output.getEvents().size() < 1) {
                            testController.log("#3.5 finished with error");
                            return;
                        }

                        testController.log("#3.5 finished correctly");
                        // TODO: after fix of delete request change this to TEST_3_6
                        doTest(TEST_3_7_1, randomUserName, randomUserName2, runnable);
                    }
                });
                break;
            case TEST_3_6:
                assert userB != null;

                Tapglue.connection().unfriendUser(userB.getID(), new TGRequestCallback<Boolean>() {
                    @Override
                    public boolean callbackIsEnabled() {
                        return true;
                    }

                    @Override
                    public void onRequestError(TGRequestErrorType cause) {
                        testController.log("#3.6 finished with error");
                    }

                    @Override
                    public void onRequestFinished(Boolean output, boolean changeDoneOnline) {
                        testController.log("#3.6 finished correctly");
                        doTest(TEST_3_7, randomUserName, randomUserName2, runnable);
                    }
                });
                break;
            case TEST_3_7:
                Tapglue.feed().retrieveNewsFeedForCurrentUser(new TGRequestCallback<TGFeed>() {
                    @Override
                    public boolean callbackIsEnabled() {
                        return true;
                    }

                    @Override
                    public void onRequestError(TGRequestErrorType cause) {
                        testController.log("#3.7 finished with error");
                    }

                    @Override
                    public void onRequestFinished(@Nullable TGFeed output, boolean changeDoneOnline) {
                        if (output == null || output.getEvents() == null || output.getEvents().size() != 1) {
                            testController.log("#3.7 finished with error");
                            return;
                        }

                        testController.log("#3.7 finished correctly");
                        doTest(TEST_3_7_1, randomUserName, randomUserName2, runnable);
                    }
                });
                break;
            case TEST_3_7_1:
                Tapglue.posts().createPost(new TGPost(), new TGRequestCallback<TGPost>() {
                    @Override
                    public boolean callbackIsEnabled() {
                        return true;
                    }

                    @Override
                    public void onRequestError(TGRequestErrorType cause) {
                        testController.log("#3.7.1 finished with error");
                    }

                    @Override
                    public void onRequestFinished(@Nullable TGPost output, boolean changeDoneOnline) {
                        if (output == null) {
                            testController.log("#3.7.1 finished with error");
                            return;
                        }

                        post = output;
                        testController.log("#3.7.1 finished correctly");
                        doTest(TEST_3_7_2, randomUserName, randomUserName2, runnable);
                    }
                });
                break;
            case TEST_3_7_2:
                assert post != null;

                Tapglue.posts().createComment(post.getID(), new TGComment().setContent("comment test"), new TGRequestCallback<TGComment>() {
                    @Override
                    public boolean callbackIsEnabled() {
                        return true;
                    }

                    @Override
                    public void onRequestError(TGRequestErrorType cause) {
                        testController.log("#3.7.2 finished with error");
                    }

                    @Override
                    public void onRequestFinished(@Nullable TGComment output, boolean changeDoneOnline) {
                        if (output == null) {
                            testController.log("#3.7.2 finished with error");
                            return;
                        }

                        postComment = output;
                        testController.log("#3.7.2 finished correctly");
                        doTest(TEST_3_7_3, randomUserName, randomUserName2, runnable);
                    }
                });
                break;
            case TEST_3_7_3:
                assert post != null;

                Tapglue.posts().likePost(post.getID(), new TGRequestCallback<TGLike>() {
                    @Override
                    public boolean callbackIsEnabled() {
                        return true;
                    }

                    @Override
                    public void onRequestError(TGRequestErrorType cause) {
                        testController.log("#3.7.3 finished with error");
                    }

                    @Override
                    public void onRequestFinished(@Nullable TGLike output, boolean changeDoneOnline) {
                        if (output == null) {
                            testController.log("#3.7.3 finished with error");
                            return;
                        }

                        testController.log("#3.7.3 finished correctly");
                        doTest(TEST_3_7_4, randomUserName, randomUserName2, runnable);
                    }
                });
                break;
            case TEST_3_7_4:
                Tapglue.posts().getMyPosts(new TGRequestCallback<TGPostsList>() {
                    @Override
                    public boolean callbackIsEnabled() {
                        return true;
                    }

                    @Override
                    public void onRequestError(TGRequestErrorType cause) {
                        testController.log("#3.7.4 finished with error");
                    }

                    @Override
                    public void onRequestFinished(@Nullable TGPostsList output, boolean changeDoneOnline) {
                        if (output == null || output.getCount() < 1 || !output.getPosts().get(0).getIsLiked()) {
                            testController.log("#3.7.4 finished with error");
                            return;
                        }

                        testController.log("#3.7.4 finished correctly");
                        doTest(TEST_3_7_5, randomUserName, randomUserName2, runnable);
                    }
                });
                break;
            case TEST_3_7_5:
                assert post != null;

                Tapglue.posts().removePost(post.getID(), new TGRequestCallback<Object>() {
                    @Override
                    public boolean callbackIsEnabled() {
                        return true;
                    }

                    @Override
                    public void onRequestError(TGRequestErrorType cause) {
                        testController.log("#3.7.5 finished with error");
                    }

                    @Override
                    public void onRequestFinished(Object output, boolean changeDoneOnline) {
                        testController.log("#3.7.5 finished correctly");
                        doTest(TEST_3_7_6, randomUserName, randomUserName2, runnable);
                    }
                });
                break;
            case TEST_3_7_6:
                Tapglue.posts().getMyPosts(new TGRequestCallback<TGPostsList>() {
                    @Override
                    public boolean callbackIsEnabled() {
                        return true;
                    }

                    @Override
                    public void onRequestError(TGRequestErrorType cause) {
                        testController.log("#3.7.6 finished with error");
                    }

                    @Override
                    public void onRequestFinished(@Nullable TGPostsList output, boolean changeDoneOnline) {
                        if (output != null && output.getCount() != 0) {
                            testController.log("#3.7.6 finished with error");
                            return;
                        }

                        testController.log("#3.7.6 finished correctly");
                        doTest(TEST_3_8, randomUserName, randomUserName2, runnable);
                    }
                });
                break;
            case TEST_3_8:
                Tapglue.user().deleteCurrentUser(new TGRequestCallback<Boolean>() {
                    @Override
                    public boolean callbackIsEnabled() {
                        return true;
                    }

                    @Override
                    public void onRequestError(TGRequestErrorType cause) {
                        testController.log("#3.8 finished with error");
                    }

                    @Override
                    public void onRequestFinished(Boolean output, boolean changeDoneOnline) {
                        testController.log("#3.8 finished correctly");
                        doTest(TEST_4_1, randomUserName, randomUserName2, runnable);
                    }
                });
                break;
            case TEST_4_1:
                Tapglue.user().login(randomUserName2, randomUserName2, new TGRequestCallback<Boolean>() {
                    @Override
                    public boolean callbackIsEnabled() {
                        return true;
                    }

                    @Override
                    public void onRequestError(TGRequestErrorType cause) {
                        testController.log("#4.1 finished with error");
                    }

                    @Override
                    public void onRequestFinished(Boolean output, boolean changeDoneOnline) {
                        testController.log("#4.1 finished correctly");
                        doTest(TEST_4_2, randomUserName, randomUserName2, runnable);
                    }
                });
                break;
            case TEST_4_2:
                Tapglue.user().retrieveFriendsForCurrentUser(new TGRequestCallback<TGUsersList>() {
                    @Override
                    public boolean callbackIsEnabled() {
                        return true;
                    }

                    @Override
                    public void onRequestError(TGRequestErrorType cause) {
                        testController.log("#4.2 finished with error");
                    }

                    @Override
                    public void onRequestFinished(@Nullable TGUsersList output, boolean changeDoneOnline) {
                        if (output != null && (output.getUsers() != null && output.getUsers().size() != 0)) {
                            testController.log("#4.2 finished with error");
                            return;
                        }

                        testController.log("#4.2 finished correctly");
                        doTest(TEST_4_3, randomUserName, randomUserName2, runnable);
                    }
                });
                break;
            case TEST_4_3:
                Tapglue.recommendation().getUsers(TGRecommendationType.Active, TGRecommendationPeriod.Day, new TGRequestCallback<TGUsersList>() {
                    @Override
                    public boolean callbackIsEnabled() {
                        return true;
                    }

                    @Override
                    public void onRequestError(TGRequestErrorType cause) {
                        testController.log("#4.3 finished with error");
                    }

                    @Override
                    public void onRequestFinished(TGUsersList output, boolean changeDoneOnline) {
                        testController.log("#4.3 finished with correctly");
                        doTest(TEST_4_4, randomUserName, randomUserName2, runnable);
                    }
                });
                break;
            case TEST_4_4:
                Tapglue.user().deleteCurrentUser(new TGRequestCallback<Boolean>() {
                    @Override
                    public boolean callbackIsEnabled() {
                        return true;
                    }

                    @Override
                    public void onRequestError(TGRequestErrorType cause) {
                        testController.log("#4.4 finished with error");
                    }

                    @Override
                    public void onRequestFinished(Boolean output, boolean changeDoneOnline) {
                        testController.log("#4.3 finished correctly");
                        doTest(-1, randomUserName, randomUserName2, runnable);
                    }
                });
                break;
            default:
                runnable.run();
                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        testController = new TestController((ListView) findViewById(R.id.test_layout), getApplicationContext());

        doTest(new Runnable() {
            @Override
            public void run() {
                testController.log("Test suite terminated", false, false);
            }
        });
    }

}
