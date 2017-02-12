package com.communication.servercommunication.activities;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.communication.servercommunication.R;
import com.communication.servercommunication.common.Constant;
import com.communication.servercommunication.common.HttpMultiProtocol;
import com.communication.servercommunication.common.Utils;
import com.communication.servercommunication.model.BaseData;
import com.communication.servercommunication.model.SOSContentData;
import com.communication.servercommunication.view.SOSActionbar;

public class CActivity extends Activity {

    private SOSActionbar mActionbar;

    /*제목*/
    private TextView mTvTitle;

    /*내용*/
    private TextView mTvContent;

    /*바로가기*/
    private TextView mTvShortKey;

    /*이미지1*/
    private ImageView mIvOneURL;

    /*이미지2*/
    private ImageView mIvTwoURL;

    /*이미지3*/
    private ImageView mIvThreeURL;

    /*boardSeq를 담는 변수*/
    private int mBoardSeq;

    /*shortKey를 담는 변수*/
    private String mShortKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_c);
        /*액션바 셋팅*/
        mActionbar = (SOSActionbar) findViewById(R.id.view_actionbar);
        mActionbar.setActionbarTitle("C Activity");
        mActionbar.setNaviBack(CActivity.this, View.VISIBLE);

        /*boardSeq를 가져옴*/
        Intent intent = getIntent();
        mBoardSeq = intent.getIntExtra(Constant.INTENT_BOARDSEQ_FLAG, 0);
        Log.d("TEST", "boardSeq: " + mBoardSeq);

        mTvTitle = (TextView) findViewById(R.id.tv_title);
        mTvContent = (TextView) findViewById(R.id.tv_content);
        mIvOneURL = (ImageView) findViewById(R.id.iv_one_url);
        mIvTwoURL = (ImageView) findViewById(R.id.iv_two_url);
        mIvThreeURL = (ImageView) findViewById(R.id.iv_three_url);
        mTvShortKey = (TextView) findViewById(R.id.tv_short_key);

         /*Toast 띄우는 메소드*/
        Utils.getInstance().setFloatingToast(CActivity.this);

        setDataContent();

        /*바로가기 버튼클릭시 해당 url창을 띄움(shortKey 필요)*/
        mTvShortKey.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent urlIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://sos.openit.co.kr/" + mShortKey + "/m/"));

                startActivity(urlIntent);
            }
        });
    }

    /*서버로부터 데이터를 가져오는 메소드*/
    private void setDataContent() {
        SOSContentData contentData = new SOSContentData(Constant.METHOD_POST, Constant.GET_BOARD_CONTENT_URL, mBoardSeq);
        HttpMultiProtocol protocol = new HttpMultiProtocol(new Utils.NetworkCheckCallback() {
            @Override
            public BaseData onSuccess(BaseData data) {
                if (data != null) {
                    SOSContentData.SOSContentRequest request = (SOSContentData.SOSContentRequest) data.getmDataClass();

                    if ("true".equals(request.isSuccess)) { // 성공한 경우
                        Log.d("TEST", String.valueOf(request.message));

                        mTvTitle.setText(request.title);
                        mTvContent.setText(request.content);

                        Log.d("TEST", "imagePath: " + request.imgPath0);
                        Log.d("TEST", "url: " + Constant.DEFAULT_SOS_URL + request.imgPath0);

                        if (request.imgPath0 != null) {
                            mIvOneURL.setVisibility(View.VISIBLE);

                            /*ImageView 비우기*/
                            mIvOneURL.setImageDrawable(null);

                            Glide.with(CActivity.this)
                                    .load(Constant.DEFAULT_SOS_URL + request.imgPath0)
                                    .into(mIvOneURL);
                        } else {
                            return null;
                        }

                        if (request.imgPath1 != null) {
                            mIvTwoURL.setVisibility(View.VISIBLE);

                            /*ImageView 비우기*/
                            mIvTwoURL.setImageDrawable(null);

                            Glide.with(CActivity.this)
                                    .load(Constant.DEFAULT_SOS_URL + request.imgPath1)
                                    .into(mIvTwoURL);
                        } else {
                            return null;
                        }

                        if (request.imgPath2 != null) {
                            mIvThreeURL.setVisibility(View.VISIBLE);

                            /*ImageView 비우기*/
                            mIvThreeURL.setImageDrawable(null);

                            Glide.with(CActivity.this)
                                    .load(Constant.DEFAULT_SOS_URL + request.imgPath2)
                                    .into(mIvThreeURL);
                        } else {
                            return null;
                        }

                        /*바로가기를 위한 shortKey를 받아 변수에 저장*/
                        if (request.shortKey != null && !request.shortKey.equals("")) {
                            mShortKey = request.shortKey;
                        }
                    }
                }

                return data;
            }

            @Override
            public BaseData onFail(BaseData data) {
                if (data != null) {
                    SOSContentData.SOSContentRequest request = (SOSContentData.SOSContentRequest) data.getmDataClass();
                    if ("false".equals(request.isSuccess)) { // 실패한 경우
                        if (request != null) {
                            Toast.makeText(CActivity.this, String.valueOf(request.message), Toast.LENGTH_SHORT).show();

                        } else {
                            Toast.makeText(CActivity.this, "불러올 데이터가 없습니다.", Toast.LENGTH_SHORT).show();
                            return data;
                        }
                    }
                }
                return data;
            }
        });

        protocol.execute(contentData);
    }

    /*백버튼에 액션달기*/
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
