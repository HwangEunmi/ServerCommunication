package com.communication.servercommunication.activities;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.communication.servercommunication.R;
import com.communication.servercommunication.adapter.SOSAdapter;
import com.communication.servercommunication.common.Constant;
import com.communication.servercommunication.common.DBManager;
import com.communication.servercommunication.common.Utils;
import com.communication.servercommunication.model.DBData;
import com.communication.servercommunication.view.SOSActionbar;

import java.util.ArrayList;
import java.util.List;

/*서버로부터 POST방식으로 리스트 가져와서 뿌리기*/
public class BActivity extends AppCompatActivity {

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_b);

        mDBManager = new DBManager(BActivity.this);

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
        mActionbar.setNaviBack(BActivity.this, View.VISIBLE);
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
                        Toast.makeText(BActivity.this, "데이터가 존재하지 않습니다", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(BActivity.this, "먼저 조회를 해주세요", Toast.LENGTH_SHORT).show();
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
                Intent intent = new Intent(BActivity.this, CActivity.class);
                /*boardSeq를 넘김*/
                intent.putExtra(Constant.INTENT_BOARDSEQ_FLAG, data.ansim_info_seq);
                startActivity(intent);
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

                    Toast.makeText(BActivity.this, data.size() + " 개가 조회되었습니다", Toast.LENGTH_SHORT).show();

                } else {
                    Toast.makeText(BActivity.this, "데이터가 존재하지 않습니다", Toast.LENGTH_SHORT).show();
                }
            }
        });
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
