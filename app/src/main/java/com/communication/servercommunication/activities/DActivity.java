package com.communication.servercommunication.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.communication.servercommunication.R;
import com.communication.servercommunication.common.Constant;
import com.communication.servercommunication.common.Utils;
import com.communication.servercommunication.view.SOSActionbar;

public class DActivity extends Activity {

    private SOSActionbar mActionbar;

    private EditText mEtValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_d);
        /*액션바 셋팅*/
        mActionbar = (SOSActionbar)findViewById(R.id.view_actionbar);
        mActionbar.setActionbarTitle("D Activity");
        mActionbar.setNaviBack(DActivity.this, View.VISIBLE);

        mEtValue = (EditText) findViewById(R.id.et_result);

         /*Toast 띄우는 메소드*/
        Utils.getInstance().setFloatingToast(DActivity.this);
    }

    /*MainActivity의 onActivityResult()가
    * 이곳의 onStop()보다 먼저 호출됨
    * (처음에 여기에 저 setResult()내용을 넣고, 종료시 바로 보내려고 했는데
    * onActivityResult()가 먼저 호출되므로 안됨)*/
    @Override
    protected void onStop() {
        super.onStop();
        Log.d("TEST", "Call onStop!!");
    }

    @Override
    public void onBackPressed() {

         /*MainActivity의 onActivityResult()로 결과값을 넘겨줌*/
        String inputResult = mEtValue.getText().toString();
        Intent intent = new Intent();
        intent.putExtra(Constant.INTENT_REQUEST_CODE, inputResult);

        setResult(RESULT_OK, intent);
        Log.d("TEST", "value: " + inputResult);

        /*값 셋팅 다 한 후, onBackPressed() 호출해서 finish()시켜야 함*/
        super.onBackPressed();
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
