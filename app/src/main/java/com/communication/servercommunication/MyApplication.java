package com.communication.servercommunication;


import android.app.Application;
import android.content.Context;

/**
 * Created by hwangem on 2017-01-25.
 */

/*DB 생성시, context 필요함*/
public class MyApplication extends Application {
    private static MyApplication instance;

    private static Context context;

    public static MyApplication getInstance() {
        if (instance == null) {
            instance = new MyApplication();
        }
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        // TODO : 이 this가 무엇을 뜻하는지 알아보기
        context = this;
    }

    /*Context 반환*/
    // TODO : 왜 여기엔 final 안붙이는지(전역인데)
    public static Context getContext() {
        return context;
    }
}
