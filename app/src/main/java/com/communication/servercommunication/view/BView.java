package com.communication.servercommunication.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.communication.servercommunication.R;
import com.communication.servercommunication.activities.BActivity;
import com.communication.servercommunication.activities.CActivity;
import com.communication.servercommunication.activities.ViewPagerActivity;
import com.communication.servercommunication.adapter.SOSAdapter;
import com.communication.servercommunication.common.Constant;
import com.communication.servercommunication.common.DBManager;
import com.communication.servercommunication.model.DBData;

import java.util.ArrayList;
import java.util.List;

public class BView extends LinearLayout implements View.OnClickListener {

    private SOSActionbar mActionbar;

    private ListView mListView;

    /* DB를 위한 툴바 */
    private LinearLayout mLLDBMode;

    /* DB 삭제모드 툴바 */
    private LinearLayout mLLDBDeleteMode;

    /* DB 조회 버튼 */
    private TextView mTvDBSearch;

    /* DB 삭제모드 전환 버튼 */
    private TextView mTvDBDeleteMode;

    /* DB 삭제버튼 */
    private TextView mDBDeleteText;

    /* DB 삭제모드 취소버튼 */
    private LinearLayout mLLCancel;

    /* 체크박스 전체 선택 뷰 */
    private ImageView mIvAllClick;

    /* SOSListData용 어댑터 */
    private SOSAdapter mSosAdapter;

    private DBManager mDBManager;

    private LayoutInflater inflater;

    private ViewPagerActivity mActivity;

    public BView(Context context, ViewPagerActivity mActivity) {
        super(context);
        this.inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.mActivity = mActivity;

        init();
    }

    private void init() {
        inflate(getContext(), R.layout.activity_b, this);
        mDBManager = new DBManager(mActivity);
        Log.d("TEST", "onCreate B");
        /* 액션바 셋팅 */
        mActionbar = (SOSActionbar)findViewById(R.id.view_actionbar);
        mActionbar.setActionbarTitle("B Activity");
        mActionbar.setNaviBack(mActivity, View.VISIBLE);

        mDBDeleteText = (TextView)findViewById(R.id.tv_db_delete);
        mIvAllClick = (ImageView)findViewById(R.id.iv_all_select);
        mLLDBMode = (LinearLayout)findViewById(R.id.ll_db_toolbar);
        mTvDBSearch = (TextView)findViewById(R.id.tv_db_search);
        mTvDBDeleteMode = (TextView)findViewById(R.id.tv_db_mode);
        mLLDBDeleteMode = (LinearLayout)findViewById(R.id.ll_cv_mode);
        mLLCancel = (LinearLayout)findViewById(R.id.ll_db_mode_cancel);

        mListView = (ListView)findViewById(R.id.lv_sos);
        mListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        mSosAdapter = new SOSAdapter();
        mListView.setAdapter(mSosAdapter);
        mLLDBMode.setVisibility(View.VISIBLE);

        mTvDBDeleteMode.setOnClickListener(this);
        mIvAllClick.setOnClickListener(this);
        mDBDeleteText.setOnClickListener(this);
        mTvDBSearch.setOnClickListener(this);

        /* 어댑터용 클릭리스너 */
        mSosAdapter.setSOSClickListener(new SOSAdapter.OnSOSClickListener() {

            @Override
            public void onSOSClick(DBData data) {
                mActivity.shortKeyData = data.ansim_info_seq;
                Log.d("TEST", "mActivity.shortKeyData: "+mActivity.shortKeyData);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            // (리스트)뷰
            case R.id.lv_sos:
                mIvAllClick.setSelected(false);
                break;

            // DB 모드로 전환
            case R.id.tv_db_mode:
                List<DBData> data = mDBManager.getSOSListData();

                if (mListView.getCount() != 0) {
                    if (data != null && data.size() != 0) {
                        mIvAllClick.setSelected(true);
                        mLLDBMode.setVisibility(View.GONE);
                        mLLDBDeleteMode.setVisibility(View.VISIBLE);
                        mSosAdapter.setIsDeleteView();

                        for (int i = 0; i < mSosAdapter.getCount(); i++) {
                            mListView.setItemChecked(i, true);
                        }

                        /* 취소버튼 클릭시 화면 나가기 */
                        mLLCancel.setVisibility(View.VISIBLE);
                        mLLCancel.setOnClickListener(new View.OnClickListener() {

                            @Override
                            public void onClick(View v) {
                                mSosAdapter.setIsListView();
                                mLLDBDeleteMode.setVisibility(View.GONE);
                                mLLDBMode.setVisibility(View.VISIBLE);
                            }
                        });
                    } else {
                        Toast.makeText(mActivity, "데이터가 존재하지 않습니다", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(mActivity, "먼저 조회를 해주세요", Toast.LENGTH_SHORT).show();
                }
                break;

            // (전체 선택을 위한) 체크박스 이미지뷰
            case R.id.iv_all_select:
                if (mIvAllClick.isSelected()) {
                    mIvAllClick.setSelected(false); // 이미지교체로 변경(SELECTED 빼고

                } else {
                    mIvAllClick.setSelected(true);
                }

                for (int i = 0; i < mSosAdapter.getCount(); i++) {
                    mListView.setItemChecked(i, mIvAllClick.isSelected());
                }
                break;

            // DB 삭제 버튼
            case R.id.tv_db_delete:
                if (mListView.getChoiceMode() == ListView.CHOICE_MODE_MULTIPLE) {
                    SparseBooleanArray array = mListView.getCheckedItemPositions();
                    List<DBData> removeList = new ArrayList<DBData>();

                    int count = 0;

                    for (int i = 0; i < array.size(); i++) {
                        int position = array.keyAt(i);

                        if (array.get(position)) {
                            Log.d("TEST", "DF: " + array.get(position));
                            DBData dbData = (DBData)mListView.getItemAtPosition(position);

                            /* DB에서 삭제 */
                            mDBManager.deleteSOSData(dbData);

                            removeList.add(dbData);

                            count++;
                        }
                    }

                    for (DBData d : removeList) {
                        mSosAdapter.remove(d);
                    }

                    mListView.clearChoices();
                    /* 리스트모드로 뷰 전환 */
                    mSosAdapter.setIsListView();

                    /* DB삭제 클릭시 체크박스에 선택된 아이템이 아무것도 없을때 */
                    if (count == 0) {
                        mSosAdapter.setIsDeleteView();
                    }
                }
                break;

            // DB 내용 조회 버튼
            case R.id.tv_db_search:
                List<DBData> listData = mDBManager.getSOSListData();

                if (listData != null && listData.size() != 0) {
                    mSosAdapter.clear();
                    mSosAdapter.addAll(listData);

                    Toast.makeText(mActivity, listData.size() + " 개가 조회되었습니다", Toast.LENGTH_SHORT).show();

                } else {
                    Toast.makeText(mActivity, "데이터가 존재하지 않습니다", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
}
