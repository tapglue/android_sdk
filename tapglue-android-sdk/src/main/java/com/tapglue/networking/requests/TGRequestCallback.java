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

package com.tapglue.networking.requests;

public interface TGRequestCallback<OBJECT> {
    /**
     * Information if request callback is not outdated / is active
     */
    boolean callbackIsEnabled();

    /**
     * Request couldn't be finished At most cases this will happen when no internet connection is
     * available, and request would require to be done live
     *
     * @param cause Cause of error
     */
    void onRequestError(TGRequestErrorType cause);

    /**
     * Request was finished with success
     *
     * @param output           Return object or status object
     * @param changeDoneOnline Flag with information if request was done online or just queued in
     *                         cache
     */
    void onRequestFinished(OBJECT output, boolean changeDoneOnline);
}
