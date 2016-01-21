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

package com.tapglue.utils;

import android.support.annotation.NonNull;
import android.util.Log;

public class TGLog {

    private static final String LOG_TAG = "Tapglue";

    /**
     * Should logger do its work?
     */
    private boolean mIsEnabled = false;

    public TGLog(boolean enabled) {
        mIsEnabled = enabled;
    }

    /**
     * Log information
     *
     * @param what What should be logged
     */
    public void log(String what) {
        if (!mIsEnabled) { return; }
        Log.d(LOG_TAG, what);
    }

    /**
     * Log error
     *
     * @param t Throwable to log
     */
    public void logE(@NonNull Throwable t) {
        if (!mIsEnabled) { return; }
        Log.e(LOG_TAG, t.getMessage());
        t.printStackTrace();
    }

    /**
     * Log warning
     *
     * @param what What should be logged
     */
    public void logW(String what) {
        Log.w(LOG_TAG, what);
    }
}
