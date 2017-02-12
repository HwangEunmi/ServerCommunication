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

    private DBManager mDBManager;

    public ViewPagerAdapter(ViewPagerActivity activity, DBManager mDBManager) {
        this.activity = activity;
        this.mDBManager = mDBManager;
    }

    List<LinearLayout> scrapped = new ArrayList<>();

    @Override
    public Object instantiateItem(final ViewGroup container, int position) {
        View view = null;

        if (scrapped.size() > 0) {
            view = scrapped.remove(0);

        } else {
            if (position == 0) {
                view = new AView(container.getContext(), activity, mDBManager);

                container.addView(view);

            } else if (position == 1) {

            } else {
                view = new BView(container.getContext(), activity);

                container.addView(view);            }
        }

        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        View view = (View)object;
        container.removeView(view);
        scrapped.add((LinearLayout)view);
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

}
