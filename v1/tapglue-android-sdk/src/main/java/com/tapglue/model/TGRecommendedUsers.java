/*
 * Copyright (c) 2016 Tapglue (https://www.tapglue.com/). All rights reserved.
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

package com.tapglue.model;

import com.tapglue.networking.TGCustomCacheObject;

public class TGRecommendedUsers extends TGUsersList {

    TGRecommendationPeriod period;

    TGRecommendationType type;

    public enum TGRecommendationPeriod {
        Day(1), Week(2), Month(3);

        /**
         * Id of type
         */
        private int mId = -1;

        static public TGRecommendationPeriod fromCode(int id) {
            for (TGRecommendationPeriod period : values()) {
                if (period.toCode() == id) { return period; }
            }
            return null;
        }

        TGRecommendationPeriod(int id) {
            mId = id;
        }

        /**
         * Get id
         *
         * @return id
         */
        public int toCode() {
            return this.mId;
        }

        @Override
        public String toString() {
            switch (mId) {
                case 1: return "day";
                case 2: return "week";
                case 3: return "month";
                default: return "day";
            }

        }
    }

    public enum TGRecommendationType {
        Active(1);

        /**
         * Id of type
         */
        private int mId = -1;

        static public TGRecommendationType fromCode(int id) {
            for (TGRecommendationType type : values()) {
                if (type.toCode() == id) { return type; }
            }
            return null;
        }

        TGRecommendationType(int id) {
            mId = id;
        }

        /**
         * Get id
         *
         * @return id
         */
        public int toCode() {
            return this.mId;
        }

        @Override
        public String toString() {
            switch (mId) {
                case 1: return "active";
                default: return "active";
            }
        }
    }

    public TGRecommendedUsers() {
        super(TGCustomCacheObject.TGCacheObjectType.ConnectionUserList);
        type = TGRecommendationType.Active;
        period = TGRecommendationPeriod.Day;
    }


    /**
     * Get the recommendation period
     *
     * @return
     */
    public TGRecommendationPeriod getPeriod() {
        return period;
    }

    /**
     * Set the period of the recommendation
     *
     * @param period Recommendation period
     *
     * @return
     */
    public TGRecommendedUsers setPeriod(TGRecommendationPeriod period) {
        this.period = period;
        return this;
    }

    /**
     * Get the type of the recommendation
     *
     * @return
     */
    public TGRecommendationType getType() {
        return type;
    }

    /**
     * Set the type of the recommendation
     *
     * @param type Recommendation type
     *
     * @return
     */
    public TGRecommendedUsers setType(TGRecommendationType type) {
        this.type = type;
        return this;
    }
}
