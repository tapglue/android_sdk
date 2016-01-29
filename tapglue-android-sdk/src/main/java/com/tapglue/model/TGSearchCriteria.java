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

package com.tapglue.model;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tapglue.networking.TGCustomCacheObject.TGCacheObjectType;

import java.util.List;

public class TGSearchCriteria extends TGBaseObject<TGSearchCriteria> {

    @Nullable
    @Expose
    @SerializedName("search_criteria")
    private String searchCriteria;

    @NonNull
    @Expose
    @SerializedName("search_emails")
    private List<String> searchCriteriaEmails;

    @Nullable
    @Expose
    @SerializedName("search_social_ids")
    private List<String> searchCriteriaSocial;

    @Expose
    @SerializedName("search_social_platform")
    private String searchCriteriaSocialPlatform;

    public TGSearchCriteria() {
        super(TGCacheObjectType.SearchCriteria);
    }

    /**
     * Get search phrase for emails
     *
     * @return Search criteria
     */
    @Nullable
    public List<String> getEmailsSearchCriteria() {
        return searchCriteriaEmails;
    }

    /**
     * Get search phrase
     *
     * @return Search criteria
     */
    @Nullable
    public String getSearchCriteria() {
        return searchCriteria;
    }

    /**
     * Set search phrase
     *
     * @param criteria search criteria
     *
     * @return Current object
     */
    @NonNull
    public TGSearchCriteria setSearchCriteria(@NonNull String criteria) {
        searchCriteria = criteria;
        searchCriteriaEmails = null;
        searchCriteriaSocial = null;
        return this;
    }

    /**
     * Get search phrase for social connections
     *
     * @return Search criteria
     */
    @Nullable
    public List<String> getSocialSearchCriteriaIds() {
        return searchCriteriaSocial;
    }

    /**
     * Get search phrase for social connections
     *
     * @return Search criteria
     */
    public String getSocialSearchCriteriaPlatform() {
        return searchCriteriaSocialPlatform;
    }

    @NonNull
    @Override
    protected TGSearchCriteria getThis() {
        return this;
    }

    /**
     * Set search phrase
     *
     * @param socialIds      search criteria
     * @param socialPlatform social platform
     *
     * @return Current object
     */
    @NonNull
    public TGSearchCriteria setSearchCriteria(String socialPlatform, List<String> socialIds) {
        searchCriteriaSocial = socialIds;
        searchCriteriaSocialPlatform = socialPlatform;
        return this;
    }

    /**
     * Set search phrase
     *
     * @param criteria search criteria
     *
     * @return Current object
     */
    @NonNull
    public TGSearchCriteria setSearchCriteriaEmails(@NonNull List<String> criteria) {
        searchCriteriaEmails = criteria;
        searchCriteriaSocial = null;
        searchCriteria = null;
        return this;
    }
}
