package com.communication.servercommunication.activities;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.communication.servercommunication.MyApplication;
import com.communication.servercommunication.R;
import com.communication.servercommunication.adapter.ViewPagerAdapter;
import com.communication.servercommunication.common.DBManager;
import com.communication.servercommunication.view.SOSActionbar;

public class ViewPagerActivity extends Activity {

    ViewPager pager;

    ViewPagerAdapter mAdapter;

    private DBManager mDBManger;

    private SOSActionbar mActionbar;

    private Intent mIntent;

    private LinearLayout llIvTab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_pager);
        pager = (ViewPager)findViewById(R.id.pager);
        mActionbar = (SOSActionbar)findViewById(R.id.view_actionbar);

        mAdapter = new ViewPagerAdapter(ViewPagerActivity.this, mDBManger);
        pager.setAdapter(mAdapter);
        pager.setCurrentItem(0); // 첫번째 페이지로 초기화
        pager.setOffscreenPageLimit(3); // 3개의 페이지를 그려놓고 준비
        mDBManger = new DBManager(MyApplication.getContext());

        ImageView ivOne = (ImageView)findViewById(R.id.iv_one);
        ImageView ivTwo = (ImageView)findViewById(R.id.iv_two);
        ImageView ivThree = (ImageView)findViewById(R.id.iv_three);
        llIvTab = (LinearLayout)findViewById(R.id.ll_iv_tab);

        ivOne.setOnClickListener(movePageListener);
        ivOne.setTag(0); // 첫번째화면 호출
        ivTwo.setOnClickListener(movePageListener);
        ivTwo.setTag(1); // 두번째화면 호출
        ivThree.setOnClickListener(movePageListener);
        ivThree.setTag(2); // 세번째화면 호출

        ivOne.setSelected(true); // 앱 처음 실행때, 첫번째 탭이 선택되어있어야하니까

        /* 현재 뷰가 탭의 Tag와 같으면, selected표시 */
        /* addOnPageChangeListener : ViewPager 내부의 페이지 변화가 있을때 호출됨 */
        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            // onPageScrolled : 스크롤(Swipe) 효과가 나는동안 계속해서 호출됨
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            // onPageSelected : 현재 선택된 페이지를 알려줌
            @Override
            public void onPageSelected(int position) {
                int i = 0;
                while (i < 3) {
                    if (position == i) {
                        // findViewWithTag : Tag로 뷰를 찾음 (단, Object.equals() 비교를 함)
                        llIvTab.findViewWithTag(i).setSelected(true);

                    } else {
                        llIvTab.findViewWithTag(i).setSelected(false);
                    }

                    i++;
                }
            }

            // onPageScrolled : 포지션(페이지 위치)와는 상관 x,
            // 매개변수값으로 state(페이지의 상태자체)를 받아옴
            // 0 : SCROLL_STATE_IDLE, 1 : SCROLL_STATE_DRAGGING, 2 : SCROLL_STATE_SETTLING
            // ex. 첫번째화면에서 세번째 탭을 누르면 0 -> 2 이렇게 로그 찍힘
            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    /*
     * 탭 선택했을때의 처리과정 (다음에는 그냥 버튼마다 따로 switch문으로 처리하기)
     */
    View.OnClickListener movePageListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            int currentTag = (int)v.getTag();

            int i = 0;
            while (i < 3) {
                if (currentTag == i) {
                    llIvTab.findViewWithTag(i).setSelected(true);
                }

                else {
                    llIvTab.findViewWithTag(i).setSelected(false);
                }

                i++;
            }
            pager.setCurrentItem(currentTag);
        }
    };

}
