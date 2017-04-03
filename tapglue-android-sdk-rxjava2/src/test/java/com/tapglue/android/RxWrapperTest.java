/*
 *  Copyright (c) 2015-2016 Tapglue (https://www.tapglue.com/). All rights reserved.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package com.tapglue.android;

import com.tapglue.android.internal.TestEntity;

import org.junit.Test;

import java.io.IOException;

import io.reactivex.Observable;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.IsNull.nullValue;

public class RxWrapperTest {

    RxWrapper<TestEntity> wrapper = new RxWrapper<>();

    @Test
    public void unwrapsObservable() throws IOException {
        TestEntity entity = new TestEntity(10);

        assertThat(wrapper.unwrap(Observable.just(entity)), equalTo(entity));
    }

    @Test (expected = IOException.class)
    public void throwsExceptionOnError() throws IOException {
        wrapper.unwrap(Observable.<TestEntity>error(new IOException()));
    }

    @Test
    public void emptyObservableReturnsNull() throws IOException {
        TestEntity entity = wrapper.unwrap(Observable.<TestEntity>empty());

        assertThat(entity, nullValue());
    }
}