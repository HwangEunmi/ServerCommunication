package com.communication.servercommunication.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by hwangem on 2017-01-27.
 */

/*공통으로 쓰는 DataModel*/
public class DefaultData {

    @SerializedName("message")
    public String message;

    @SerializedName("isSuccess")
    public String isSuccess;

}
