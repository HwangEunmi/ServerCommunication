package com.communication.servercommunication.view;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.util.AttributeSet;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.communication.servercommunication.MyApplication;
import com.communication.servercommunication.R;
import com.communication.servercommunication.activities.BActivity;
import com.communication.servercommunication.activities.CActivity;
import com.communication.servercommunication.activities.ViewPagerActivity;
import com.communication.servercommunication.adapter.SOSAdapter;
import com.communication.servercommunication.common.Constant;
import com.communication.servercommunication.common.DBContract;
import com.communication.servercommunication.common.DBManager;
import com.communication.servercommunication.common.HttpMultiProtocol;
import com.communication.servercommunication.common.Utils;
import com.communication.servercommunication.model.BaseData;
import com.communication.servercommunication.model.DBData;
import com.communication.servercommunication.model.SOSListData;

import java.util.ArrayList;
import java.util.List;

public class BView extends LinearLayout {

    private SOSActionbar mActionbar;

    private ListView mListView;

    /*DB를 위한 툴바*/
    private LinearLayout mLLDBMode;

    /*DB 삭제모드 툴바*/
    private LinearLayout mLLDBDeleteMode;

    /*DB 조회 버튼*/
    private TextView mTvDBSearch;

    /*DB 삭제모드 전환 버튼*/
    private TextView mTvDBDeleteMode;

    /*DB 삭제버튼*/
    private TextView mDBDeleteText;

    /*DB 삭제모드 취소버튼*/
    private LinearLayout mLLCancel;

    /*체크박스 전체 선택 뷰*/
    private ImageView mIvAllClick;

    private boolean[] mIsClicked;

    /*SOSListData용 어댑터*/
    private SOSAdapter mSosAdapter;

    private DBManager mDBManager;

    private LayoutInflater inflater;

    private ViewPagerActivity activity;

    public BView(Context context, ViewPagerActivity activity) {
        super(context);
        this.inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.activity = activity;

        init();
    }

    private void init() {
        inflate(getContext(), R.layout.activity_b, this);

        mDBManager = new DBManager(MyApplication.getContext());

        mDBDeleteText = (TextView) findViewById(R.id.tv_db_delete);
        mIvAllClick = (ImageView) findViewById(R.id.iv_all_select);
        mLLDBMode = (LinearLayout) findViewById(R.id.ll_db_toolbar);
        mTvDBSearch = (TextView) findViewById(R.id.tv_db_search);
        mTvDBDeleteMode = (TextView) findViewById(R.id.tv_db_mode);
        mLLDBDeleteMode = (LinearLayout) findViewById(R.id.ll_cv_mode);
        mLLCancel = (LinearLayout) findViewById(R.id.ll_db_mode_cancel);

        mListView = (ListView) findViewById(R.id.lv_sos);
        mListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        mSosAdapter = new SOSAdapter();
        mListView.setAdapter(mSosAdapter);

        /*액션바 셋팅*/
        mActionbar = (SOSActionbar) findViewById(R.id.view_actionbar);
        mActionbar.setActionbarTitle("B Activity");
        mActionbar.setNaviBack(MyApplication.getContext(), View.VISIBLE);
        mLLDBMode.setVisibility(View.VISIBLE);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mIvAllClick.setSelected(false);
            }
        });

        mTvDBDeleteMode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<DBData> data = mDBManager.getSOSListData();

                if (mListView.getCount() != 0) {
                    if (data != null && data.size() != 0) {
                        mLLDBMode.setVisibility(View.GONE);
                        mLLDBDeleteMode.setVisibility(View.VISIBLE);
                        mSosAdapter.setIsDeleteView();

                        /*취소버튼 클릭시 화면 나가기*/
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
                        Toast.makeText(MyApplication.getContext(), "데이터가 존재하지 않습니다", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(MyApplication.getContext(), "먼저 조회를 해주세요", Toast.LENGTH_SHORT).show();
                }
            }
        });

        mIvAllClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mIvAllClick.isSelected()) {
                    mIvAllClick.setSelected(false); // 이미지교체로 변경(SELECTED 빼고

                } else {
                    mIvAllClick.setSelected(true);
                }

                for (int i = 0; i < mSosAdapter.getCount(); i++) {
                    mListView.setItemChecked(i, mIvAllClick.isSelected());
                }
            }
        });

        // 삭제
        mDBDeleteText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListView.getChoiceMode() == ListView.CHOICE_MODE_MULTIPLE) {
                    SparseBooleanArray array = mListView.getCheckedItemPositions();
                    List<DBData> removeList = new ArrayList<DBData>();

                    int count = 0;

                    for (int i = 0; i < array.size(); i++) {
                        int position = array.keyAt(i);

                        if (array.get(position)) {
                            Log.d("TEST", "DF: " + array.get(position));
                            DBData data = (DBData) mListView.getItemAtPosition(position);

                            /*DB에서 삭제*/
                            mDBManager.deleteSOSData(data);

                            removeList.add(data);

                            count++;
                        }
                    }

                    for (DBData d : removeList) {
                        mSosAdapter.remove(d);
                    }

                    mListView.clearChoices();
                    /*리스트모드로 뷰 전환*/
                    mSosAdapter.setIsListView();

                    /*DB삭제 클릭시 체크박스에 선택된 아이템이 아무것도 없을때*/
                    if (count == 0) {
                        mSosAdapter.setIsDeleteView();
                    }
                }
            }
        });

        /*어댑터용 클릭리스너*/
        mSosAdapter.setSOSClickListener(new SOSAdapter.OnSOSClickListener() {
            @Override
            public void onSOSClick(DBData data) {
                Intent intent = new Intent(MyApplication.getContext(), CActivity.class);
                /*boardSeq를 넘김*/
                intent.putExtra(Constant.INTENT_BOARDSEQ_FLAG, data.ansim_info_seq);
                Activity activity = (Activity)MyApplication.getContext();
                activity.startActivity(intent);
            }
        });

        /*DB에 데이터가 있으면 어댑터에 추가*/
        mTvDBSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<DBData> data = mDBManager.getSOSListData();

                if (data != null && data.size() != 0) {
                    mSosAdapter.clear();
                    mSosAdapter.addAll(data);
                    mIsClicked = new boolean[data.size()]; // 필요없을 삘

                    Toast.makeText(MyApplication.getContext(), data.size() + " 개가 조회되었습니다", Toast.LENGTH_SHORT).show();

                } else {
                    Toast.makeText(MyApplication.getContext(), "데이터가 존재하지 않습니다", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
