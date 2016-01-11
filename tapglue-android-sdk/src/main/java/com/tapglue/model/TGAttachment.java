package com.tapglue.model;

import android.support.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tapglue.networking.TGCustomCacheObject;

/**
 * Created by adrian on 11.01.16.
 */
public class TGAttachment extends TGBaseObject<TGAttachment> {

    @Expose
    @SerializedName("type")
    private String mType;

    @Expose
    @SerializedName("name")
    private Integer mName;

    @Expose
    @SerializedName("content")
    private Integer mContent;

    TGAttachment(@NonNull TGCustomCacheObject.TGCacheObjectType type) {
        super(type);
    }

    @Override
    protected TGAttachment getThis() {
        return this;
    }

    public String getType() {
        return mType;
    }

    public TGAttachment setType(String mType) {
        this.mType = mType;
        return getThis();
    }

    public Integer getName() {
        return mName;
    }

    public TGAttachment setName(Integer mName) {
        this.mName = mName;
        return getThis();
    }

    public Integer getContent() {
        return mContent;
    }

    public TGAttachment setContent(Integer mContent) {
        this.mContent = mContent;
        return getThis();
    }
}
