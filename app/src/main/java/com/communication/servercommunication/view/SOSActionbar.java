package com.communication.servercommunication.view;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.communication.servercommunication.R;

/**
 * Created by hwangem on 2017-01-23.
 */

public class SOSActionbar extends LinearLayout {

    /*Back 버튼*/
    private LinearLayout mLLNaviBack;

    /*Actionbar 타이틀용*/
    private TextView mTvToolbarTitle;

    /*1) View를 코드에서 생성할때 호출되는 생성자*/
    public SOSActionbar(Context context) {
        /*this()를 이용해서 두번째 생성자를 호출*/
        this(context, null);
    }

    /*xml에서 내가 SOSActionbar를 만들었을 때
    (inflating) 생성되는 정보들(attributeSet)을 갖기위한 생성자
    (CustomView 생성시 꼭 필요한 생성자)*/
    public SOSActionbar(Context context, AttributeSet attrs) {
        super(context, attrs);

        init();
    }

    private void init() {
        inflate(getContext(), R.layout.layout_toolbar, this);

        mLLNaviBack = (LinearLayout) findViewById(R.id.ll_navi_back);
        mTvToolbarTitle = (TextView) findViewById(R.id.tv_toolbar_title);
    }

    /*Actionbar의 title 셋팅*/
    public void setActionbarTitle(String title) {
        mTvToolbarTitle.setText(title);
    }

    /*Back 버튼*/
    public void setNaviBack(final Context context, int isVisible) {
        mLLNaviBack.setVisibility(isVisible);

        switch (isVisible) {
            case View.VISIBLE:
                mLLNaviBack.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Activity activity = (Activity)context;
                        activity.finish();
                    }
                });

                break;

            case View.GONE:
                mLLNaviBack.setClickable(false);
                break;
        }
    }
}
