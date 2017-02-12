package com.communication.servercommunication.model;


import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by 은미 on 2017-01-19.
 */

public class SOSListData extends BaseData implements Serializable {

    private String mCategory;

    private int mAnsim_info_seq;

    private String mShort_key;

    private String mContent;

    private String mDetail_content;

    private int mAnsim_detail_seq;

    private String mType;

    private String mTitle;

    private SOSListRequest mSosListRequest = new SOSListRequest();

    /*기본 생성자 형태*/
    public SOSListData(String requestMethod, String serviceUrl, String paramData) {
        /*부모클래스인 BaseData에 대입*/
        this.mRequestMethod = requestMethod;
        this.mServiceUrl = serviceUrl;
        this.mParamData = paramData;
        mDataClass = mSosListRequest;
    }

    /*List<SOSContentData>*/
    /*데이터클래스에는 static 붙이는것이 좋음*/
    public class SOSListRequest extends DefaultData {

        /*build.gradle에 gson 추가시 사용가능*/
        @SerializedName("boardListLength")
        public int boardListLength;

        @SerializedName("boardList")
        public List<DBData> data;

}

    public String getmCategory() {
        return mCategory;
    }

    public void setmCategory(String category) {
        this.mCategory = category;
    }

    public int getmAnsim_info_seq() {
        return mAnsim_info_seq;
    }

    public void setmAnsim_info_seq(int mAnsim_info_seq) {
        this.mAnsim_info_seq = mAnsim_info_seq;
    }

    public String getmShort_key() {
        return mShort_key;
    }

    public void setmShort_key(String mShort_key) {
        this.mShort_key = mShort_key;
    }

    public String getmContent() {
        return mContent;
    }

    public void setmContent(String mContent) {
        this.mContent = mContent;
    }

    public String getmDetail_content() {
        return mDetail_content;
    }

    public void setmDetail_content(String mDetail_content) {
        this.mDetail_content = mDetail_content;
    }

    public int getmAnsim_detail_seq() {
        return mAnsim_detail_seq;
    }

    public void setmAnsim_detail_seq(int mAnsim_detail_seq) {
        this.mAnsim_detail_seq = mAnsim_detail_seq;
    }

    public String getmType() {
        return mType;
    }

    public void setmType(String type) {
        this.mType = type;
    }

    public String getmTitle() {
        return mTitle;
    }

    public void setmTitle(String title) {
        this.mTitle = title;
    }
}
