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

package com.tapglue.tapgluetests;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.view.View;
import android.widget.ListView;

import com.tapglue.Tapglue;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static com.tapglue.tapgluetests.R.id.test_layout;
import static org.hamcrest.core.IsAnything.anything;

@RunWith(AndroidJUnit4.class)
public class ActivityTest {
    @Rule
    public final ActivityTestRule<TestActivity> mActivityRule =
        new ActivityTestRule<>(TestActivity.class);


    @Test
    public void ensureEverythingWorksTM() throws RuntimeException {
        Tapglue.TGConfiguration config = new Tapglue.TGConfiguration()
            .setDebugMode(true)
            .setToken(null); // TODO FIXME put your token here
        Tapglue.initialize(this.mActivityRule.getActivity(), config);

        mActivityRule.getActivity().doTest(new Runnable() {
            @Override
            public void run() {
                mActivityRule.getActivity()
                    .mTestController.log("Tests finished correctly", false, false);
                final int[] counts = new int[1];
                onView(withId(test_layout)).check(matches(new TypeSafeMatcher<View>() {
                    @Override
                    public void describeTo(Description description) {

                    }

                    @Override
                    public boolean matchesSafely(View view) {
                        ListView listView = (ListView) view;

                        counts[0] = listView.getCount();

                        return true;
                    }
                }));

                if (counts[0] != 45) {
                    throw new RuntimeException("number of items mismatches the expected value");
                }

                onData(anything())
                    .inAdapterView(withId(test_layout))
                    .atPosition(45)
                    .perform(scrollTo())
                    .check(matches(withText("Tests finished correctly")));
            }
        });
    }
}