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

package com.tapglue.utils;

import android.test.suitebuilder.annotation.SmallTest;

import junit.framework.TestCase;

public class TGPasswordHasherTest extends TestCase {

    @SmallTest
    public void test01() {
        assertEquals(TGPasswordHasher.hashPassword("password"), "89b1af261b009d79687506151b0367edabaae9ae");
        assertEquals(TGPasswordHasher.hashPassword("LongPasswordWithMoreText123"), "21cf9aa2a27f3bd5b9dce6b9b4cd903d2997c314");
        assertEquals(TGPasswordHasher.hashPassword("viJyFK%XuW=&K6mEh8mgA>eVjMAMFUzGcnn7yv"), "19b38a4d4fd0fbb198828da47f6585523d472feb");
    }

}