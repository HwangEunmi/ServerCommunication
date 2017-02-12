package com.communication.servercommunication.view;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.communication.servercommunication.R;
import com.communication.servercommunication.activities.ViewPagerActivity;
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

public class AView extends LinearLayout implements View.OnClickListener {

    private SOSActionbar mActionbar;

    private Button mBTNDownload;

    private DBManager mDBManger;

    private LayoutInflater inflater;

    private ViewPagerActivity mActivity;

    private int dbSEQ = 0;

    public AView(Context context, ViewPagerActivity mActivity) {
        super(context);
        this.inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.mActivity = mActivity;

        init();
    }

    private void init() {
        inflater.inflate(R.layout.activity_a, this, true);
        mDBManger = new DBManager(mActivity);
        Log.d("TEST", "onCreate A");

        /* 액션바 셋팅 */
        mActionbar = (SOSActionbar)findViewById(R.id.view_actionbar);
        mActionbar.setActionbarTitle("AA Activity");
        mActionbar.setNaviBack(mActivity, View.VISIBLE);

        mBTNDownload = (Button)findViewById(R.id.btn_download);
        mBTNDownload.setOnClickListener(this);
    }

    /* 서버로부터 데이터를 가져와서 어댑터에 셋팅 */
    public void setDataList(final Context context) {
        SOSListData listData = new SOSListData(Constant.METHOD_POST,
                                               Constant.GET_BOARD_LIST_URL,
                                               "count=2&currentPageNo=3");
        HttpMultiProtocol protocol = new HttpMultiProtocol(new Utils.NetworkCheckCallback() {

            @Override
            public BaseData onSuccess(BaseData data) {
                if (data != null) {
                    SOSListData.SOSListRequest request = (SOSListData.SOSListRequest)data.mDataClass;
                    if ("true".equals(request.isSuccess)) { // 성공한 경우
                        if (request.data != null) {
                            Log.d("TEST", String.valueOf(request.message));

                            Cursor tempCursorValue = null;

                            List<DBData> tempList = null;

                            int count = 0;

                            /* 해당 데이터가 DB에 존재하는지 검색 */
                            int[] tempInt = new int[request.data.size()];

                            if (DBManager.getInstance(mActivity).getSOSData().getCount() < 0) {
                                tempList = new ArrayList<>();
                                tempList.addAll(DBManager.getInstance(mActivity).getSOSListData());

                            } else {

                                for (int i = 0; i < request.data.size(); i++) {
                                    tempInt[i] = request.data.get(i).ansim_info_seq;

                                    tempCursorValue = DBManager.getInstance(mActivity)
                                                               .getKeywordSOSData(request.data.get(i).ansim_info_seq);

//                                    tempCursorValue.moveToFirst();

                                    while (tempCursorValue.moveToNext()) {
                                        dbSEQ =
                                           tempCursorValue.getInt(tempCursorValue.getColumnIndex(DBContract.SOSListItem.COLUMN_NAME_SOS_ANSIM_INFO_SEQ));

                                        break;
                                    }

                                    if (dbSEQ != tempInt[i]) {
                                        /* DB에 저장 */
                                        mDBManger.insertSOSData(request.data.get(i));
                                        Log.d("TEST", dbSEQ + ", " + tempInt[i]);
                                        count++;
                                    } else {
                                        Log.d("TEST", "SS");
                                    }
                                }
                                Toast.makeText(mActivity, count + " 개의 데이터를 DB에 저장했습니다.", Toast.LENGTH_SHORT).show();
                            }

                        } else {
                            Toast.makeText(context, "불러올 데이터가 없습니다.", Toast.LENGTH_SHORT).show();
                            return data;
                        }
                    }
                }

                /*
                 * null을 리턴하는것은 좋지 않음, 차라리 빈 컬렉션 리턴하기
                 */
                return data;
            }

            @Override
            public BaseData onFail(BaseData data) {
                if (data != null) {
                    SOSListData.SOSListRequest request = (SOSListData.SOSListRequest)data.mDataClass;
                    if ("false".equals(request.isSuccess)) { // 실패한 경우
                        if (request.data != null) {
                          // 데이터 불러오기 실패
                        } else {
                            Toast.makeText(mActivity, "불러올 데이터가 없습니다.", Toast.LENGTH_SHORT).show();
                            return data;
                        }
                    }
                }
                return data;
            }
        });

        protocol.execute(listData);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_download:
                setDataList(mActivity);

                break;
        }
    }
}
