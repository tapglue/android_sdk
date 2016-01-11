package com.tapglue.model;

import android.support.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tapglue.networking.TGCustomCacheObject;

import java.util.List;

/**
 * Created by adrian on 11.01.16.
 */
public class TGCommentsList extends TGBaseObject<TGCommentsList> {
    @Expose
    @SerializedName("comments")
    private List<TGComment> mComments;

    @Expose
    @SerializedName("post")
    private TGPost mPost;

    @Expose
    @SerializedName("comments_count")
    private Integer mCommentsCount;

    public TGCommentsList() {
        super(TGCustomCacheObject.TGCacheObjectType.CommentsList);
    }

    @Override
    protected TGCommentsList getThis() {
        return this;
    }

    public List<TGComment> getComments(){
        return mComments;
    }

    public Integer getCommentsCount(){
        return mCommentsCount;
    }

    public TGPost getPost(){
        return mPost;
    }
}
