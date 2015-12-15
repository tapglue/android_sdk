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

import android.support.annotation.NonNull;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

public class TGRequestErrorType {

    /**
     * Error code taken from server
     */
    @NonNull
    private final Long mErrorCode;
    /**
     * Error message taken from server
     */
    @NonNull
    private final String mErrorMessage;
    /**
     * Error type
     */
    private final ErrorType mErrorType;

    /**
     * Type of error for server errors
     */
    public enum ErrorType {
        /**
         * When we have an error that we don't know how to handle
         */
        UNKNOWN_ERROR(-1),

        /**
         * Network not available, and request require to be done live
         */
        NO_NETWORK(1),

        /**
         * Input object null
         */
        NULL_INPUT(2),

        /**
         * Custom server is not accessible at this moment
         */
        SERVER_ERROR(500),

        /**
         * Unsupported TGBaseObject based class input
         */
        UNSUPPORTED_INPUT(3),

        /**
         * Called request requiring user logged in
         */
        USER_NOT_LOGGED_IN(4),

        NO_TOKEN_FOUND(5),
        NO_CACHE_OBJECT(6),

        UNAUTHORIZED(401),
        FORBIDDEN(403),
        NOT_FOUND(404),
        METHOD_NOT_ALLOWED(405),
        NOT_ACCESSIBLE(406),
        CONFLICT(409),
        UNSUPPORTED_MEDIA_TYPE(415),
        UNPROCESSABLE_ENTITY(422),
        TOO_MANY_REQUESTS(429),
        SERVICE_UNAVAILABLE(503),
        GATEWAY_TIMEOUT(504),

        OTHER(999);

        private static final Map<Integer, ErrorType> lookup = new HashMap<>();
        private final int code;

        static {
            for (ErrorType s : EnumSet.allOf(ErrorType.class)) {
                lookup.put(s.getCode(), s);
            }
        }

        public static ErrorType get(int code) {
            return lookup.get(code);
        }

        ErrorType(int code) {
            this.code = code;
        }

        public int getCode() { return code; }
    }

    public TGRequestErrorType(ErrorType type) {
        mErrorType = type;
        mErrorMessage = "";
        mErrorCode = 0L;
    }

    public TGRequestErrorType(@NonNull Long code, @NonNull String message) {
        mErrorCode = code;
        mErrorMessage = message;
        mErrorType = ErrorType.OTHER;
    }

    /**
     * Get error code
     *
     * @return error code
     */
    @NonNull
    public Long getCode() {
        return mErrorCode;
    }

    /**
     * Get error message
     *
     * @return error message
     */
    @NonNull
    public String getMessage() {
        return mErrorMessage;
    }

    /**
     * Get error type
     *
     * @return error type
     */
    public ErrorType getType() {
        return mErrorType;
    }
}
