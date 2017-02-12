package com.communication.servercommunication.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by hwangem on 2017-01-31.
 */

/*이 데이터클래스 따로뺌
        * inner클래스는 하나의 경우에만 쓰는건데(네트워크같은)
        * 이건 db도 같이 쓰려고하니까 에러남
        * db용 데이터클래스겸용으로 하나 따로 만듬*/
public class DBData {

   public int sosSEQ;

    @SerializedName("CATEGORY")
    public String category;

    @SerializedName("ANSIM_INFO_SEQ")
    public int ansim_info_seq;

    @SerializedName("SHORT_KEY")
    public String short_key;

    @SerializedName("CONTENT")
    public String content;

    @SerializedName("DETAIL_CONTENT")
    public String detail_content;

    @SerializedName("ANSIM_DETAIL_SEQ")
    public int ansim_detail_seq;

    @SerializedName("TYPE")
    public String type;

    @SerializedName("TITLE")
    public String title;

    public int getSosSEQ() {
        return sosSEQ;
    }

    public void setSosSEQ(int sosSEQ) {
        this.sosSEQ = sosSEQ;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getAnsim_info_seq() {
        return ansim_info_seq;
    }

    public void setAnsim_info_seq(int ansim_info_seq) {
        this.ansim_info_seq = ansim_info_seq;
    }

    public String getShort_key() {
        return short_key;
    }

    public void setShort_key(String short_key) {
        this.short_key = short_key;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDetail_content() {
        return detail_content;
    }

    public void setDetail_content(String detail_content) {
        this.detail_content = detail_content;
    }

    public int getAnsim_detail_seq() {
        return ansim_detail_seq;
    }

    public void setAnsim_detail_seq(int ansim_detail_seq) {
        this.ansim_detail_seq = ansim_detail_seq;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}