package com.communication.servercommunication.model;

/**
 * Created by 은미 on 2017-01-19.
 */

/*SOSListData, SOSListData와 같은 데이터 모델들의 기본 틀*/
public class BaseData {

    /*서버 주소*/
    public String mServiceUrl;

    /*서버 주소 뒤에 붙는 파라메터 값*/
    public String mParamData;

    /*ex. Get방식인지 Post방식인지를 구별하는 값*/
    public String mRequestMethod;

    /*SOSListData, SOSListData와 같은 데이터 모델 객체 반환 용*/
    public Object mDataClass;

    /*데이터 모델 객체 이름 반환 용*/
    public String mObjectName;


    public String getmServiceUrl() {
        return mServiceUrl;
    }

    public String getParamData() {
        return mParamData;
    }

    public void setmRequestMethod(String mRequestMethod) {
        this.mRequestMethod = mRequestMethod;
    }

    public String getmRequestMethod() {
        return mRequestMethod;
    }

    public Object getmDataClass() {
        return mDataClass;
    }

    public String getmObjectName() {
        return mObjectName;
    }

}
