package com.communication.servercommunication.model;


import com.google.gson.annotations.SerializedName;

/**
 * Created by 은미 on 2017-01-19.
 */

public class SOSContentData extends BaseData {

    private String mTitle;

    private String mContent;

    private String mShortKey;

    private SOSContentRequest mSosContentRequest = new SOSContentRequest();

    public SOSContentData(String requestMethod, String serviceUrl, int paramData) {
        this.mRequestMethod = requestMethod;
        this.mServiceUrl = serviceUrl;
        this.mParamData = String.valueOf(paramData);
        mDataClass = mSosContentRequest;
    }

    /*SOSContentData*/
    public class SOSContentRequest extends DefaultData {

        @SerializedName("shortKey")
        public String shortKey;

        @SerializedName("title")
        public String title;

        @SerializedName("content")
        public String content;

        @SerializedName("imgPath0")
        public String imgPath0;

        @SerializedName("imgPath1")
        public String imgPath1;

        @SerializedName("imgPath2")
        public String imgPath2;
    }

    public String getmTitle() {
        return mTitle;
    }

    public void setmTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public String getmContent() {
        return mContent;
    }

    public void setmContent(String mContent) {
        this.mContent = mContent;
    }

    public String getmShortKey() {
        return mShortKey;
    }

    public void setmShortKey(String mShortKey) {
        this.mShortKey = mShortKey;
    }
}
