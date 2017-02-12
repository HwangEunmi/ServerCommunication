package com.communication.servercommunication.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.communication.servercommunication.R;
import com.communication.servercommunication.common.Constant;
import com.communication.servercommunication.common.DBManager;
import com.communication.servercommunication.common.HttpMultiProtocol;
import com.communication.servercommunication.common.Utils;
import com.communication.servercommunication.model.BaseData;
import com.communication.servercommunication.model.SOSListData;
import com.communication.servercommunication.view.SOSActionbar;

public class MainActivity extends Activity implements View.OnClickListener {

    private SOSActionbar mActionbar;

    private Intent mIntent;

    private DBManager mDBManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mDBManager = new DBManager(MainActivity.this);

        /*액션바 셋팅*/
        mActionbar = (SOSActionbar) findViewById(R.id.view_actionbar);
        mActionbar.setActionbarTitle("Main Activity");
        mActionbar.setNaviBack(MainActivity.this, View.GONE);

        Button btnMain = (Button) findViewById(R.id.btn_main);

        btnMain.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            /*ViewPager 액티비티로 이동*/
            case R.id.btn_main:
                mIntent = new Intent(MainActivity.this, ViewPagerActivity.class);
                startActivity(mIntent);
                break;
        }
    }
}
