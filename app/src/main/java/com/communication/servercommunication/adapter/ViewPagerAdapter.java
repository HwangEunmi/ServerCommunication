package com.communication.servercommunication.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ProviderInfo;
import android.database.Cursor;
import android.support.v4.view.PagerAdapter;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.communication.servercommunication.R;
import com.communication.servercommunication.activities.AActivity;
import com.communication.servercommunication.activities.ViewPagerActivity;
import com.communication.servercommunication.common.Constant;
import com.communication.servercommunication.common.DBContract;
import com.communication.servercommunication.common.DBManager;
import com.communication.servercommunication.common.HttpMultiProtocol;
import com.communication.servercommunication.common.Utils;
import com.communication.servercommunication.model.BaseData;
import com.communication.servercommunication.model.DBData;
import com.communication.servercommunication.model.SOSListData;
import com.communication.servercommunication.view.AView;
import com.communication.servercommunication.view.BView;
import com.communication.servercommunication.view.CView;
import com.communication.servercommunication.view.SOSActionbar;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 은미 on 2017-02-09.
 */

public class ViewPagerAdapter extends PagerAdapter {

    private ViewPagerActivity activity;

    public ViewPagerAdapter(ViewPagerActivity activity) {
        this.activity = activity;
    }

    @Override
    public Object instantiateItem(final ViewGroup container, int position) {
        View view = null;

        if (position == 0) {
            view = new AView(container.getContext(), activity);

            container.addView(view);

        } else if (position == 1) {
            view = new BView(container.getContext(), activity);

            container.addView(view);
        } else {
            view = new CView(container.getContext(), activity, activity.shortKeyData);

            container.addView(view);
        }

        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        View view = (View)object;
        container.removeView(view);
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public float getPageWidth(int position) {
        return 1;
    }


    /*뷰 갱신을 위한 메소드(notifyDataSetChanged();)
     * But, 별로 좋은 방법은 아님 */
    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }
}
