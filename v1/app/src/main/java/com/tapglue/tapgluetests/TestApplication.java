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

import android.app.Application;

import com.tapglue.Tapglue;

public class TestApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        Tapglue.TGConfiguration config = new Tapglue.TGConfiguration()
            .setDebugMode(true)
            .setToken("ad8acb4d3fb4ff5435a3680802c4f093");
        Tapglue.initialize(this, config);
    }
}
