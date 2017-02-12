package com.communication.servercommunication.activities;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.Toast;

import com.communication.servercommunication.R;
import com.communication.servercommunication.adapter.SOSAdapter;
import com.communication.servercommunication.common.Constant;
import com.communication.servercommunication.common.DBContract;
import com.communication.servercommunication.common.DBManager;
import com.communication.servercommunication.common.HttpMultiProtocol;
import com.communication.servercommunication.common.Utils;
import com.communication.servercommunication.model.BaseData;
import com.communication.servercommunication.model.DBData;
import com.communication.servercommunication.model.SOSListData;
import com.communication.servercommunication.view.SOSActionbar;

import java.util.ArrayList;
import java.util.List;

public class AActivity extends AppCompatActivity {

    private SOSActionbar mActionbar;

    private Button mBTNDownload;

    private DBManager mDBManager;

    private SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_a);
       /*액션바 셋팅*/
        mActionbar = (SOSActionbar) findViewById(R.id.view_actionbar);
        mActionbar.setActionbarTitle("AA Activity");
        mActionbar.setNaviBack(AActivity.this, View.VISIBLE);

        mDBManager = new DBManager(AActivity.this);

        mBTNDownload = (Button) findViewById(R.id.btn_download);
        mBTNDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDataList();
            }
        });
    }

    /*서버로부터 데이터를 가져와서 어댑터에 셋팅*/
    public void setDataList() {
        SOSListData listData = new SOSListData(Constant.METHOD_POST, Constant.GET_BOARD_LIST_URL, "count=2&currentPageNo=3");
        HttpMultiProtocol protocol = new HttpMultiProtocol(new Utils.NetworkCheckCallback() {
            @Override
            public BaseData onSuccess(BaseData data) {
                if (data != null) {
                    SOSListData.SOSListRequest request = (SOSListData.SOSListRequest) data.mDataClass;
                    if ("true".equals(request.isSuccess)) { // 성공한 경우
                        if (request.data != null) {
                            Log.d("TEST", String.valueOf(request.message));

                            Cursor tempCursorValue = null;

                            List<DBData> tempList = null;

                            int count = 0;

                            /*해당 데이터가 DB에 존재하는지 검색*/
                            int[] tempInt = new int[request.data.size()];

                            if(DBManager.getInstance(AActivity.this).getSOSData().getCount()  < 0) {
                                tempList = new ArrayList<>();
                                tempList.addAll(DBManager.getInstance(AActivity.this).getSOSListData());

                            }else {

                                try {
                                    for (int i = 0; i < request.data.size(); i++) {
                                        tempInt[i] = request.data.get(i).ansim_info_seq;

                                        tempCursorValue = DBManager.getInstance(AActivity.this).getKeywordSOSData(request.data.get(i).ansim_info_seq);

                                        tempCursorValue.moveToFirst();

                                        int ss = 0;

                                        while (tempCursorValue.moveToNext()) {
                                            ss = tempCursorValue.getInt(tempCursorValue.getColumnIndex(DBContract.SOSListItem.COLUMN_NAME_SOS_ANSIM_INFO_SEQ));
                                            break;
                                        }

                                        if (ss != tempInt[i]) {
                                            /*DB에 저장*/
                                            mDBManager.insertSOSData(request.data.get(i));
                                            Log.d("TEST", ss + ", " + tempInt[i]);
                                            count++;
                                        } else {
                                            Log.d("TEST", "SS");
                                        }
                                    }
                                    Toast.makeText(AActivity.this, count + " 개의 데이터를 DB에 저장했습니다.", Toast.LENGTH_SHORT).show();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                } finally {
                                }
                            }

                        } else {
                            Toast.makeText(AActivity.this, "불러올 데이터가 없습니다.", Toast.LENGTH_SHORT).show();
                            return data;
                        }
                    }
                }

                /*null을 리턴하는것은 좋지 않음,
                 * 차라리 빈 컬렉션 리턴하기*/
                return data;
            }

            @Override
            public BaseData onFail(BaseData data) {
                if (data != null) {
                    SOSListData.SOSListRequest request = (SOSListData.SOSListRequest) data.mDataClass;
                    if ("false".equals(request.isSuccess)) { // 실패한 경우
                        if (request.data != null) {
                            Toast.makeText(AActivity.this, String.valueOf(request.message), Toast.LENGTH_SHORT).show();

                        } else {
                            Toast.makeText(AActivity.this, "불러올 데이터가 없습니다.", Toast.LENGTH_SHORT).show();
                            return data;
                        }
                    }
                }
                return data;
            }
        });

        protocol.execute(listData);
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
