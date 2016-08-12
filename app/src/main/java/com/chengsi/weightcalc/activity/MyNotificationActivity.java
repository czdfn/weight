package com.chengsi.weightcalc.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.util.TypedValue;

import com.chengsi.weightcalc.fragment.notification.CheckFragment;
import com.chengsi.weightcalc.fragment.notification.SysNotification;
import com.chengsi.weightcalc.widget.PagerSlidingTabStrip;
import com.chengsi.weightcalc.R;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class MyNotificationActivity extends BaseActivity {

    @InjectView(R.id.tabs_notification)
    PagerSlidingTabStrip mTabStrip_not;
    @InjectView(R.id.pager_notification)
    ViewPager mPager_not;

    CheckFragment checkFragment;
    SysNotification sysNotificationFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_notification);
        setMyTitle("消息中心");
        ButterKnife.inject(this);

        initViews();
    }

    private void initViews() {
        mPager_not.setAdapter(new MyPagerAdapter(getSupportFragmentManager()));
        mTabStrip_not.setViewPager(mPager_not);
        setTabStyle();
    }

    private void setTabStyle() {
        DisplayMetrics dm = getResources().getDisplayMetrics();

        // 设置Tab是自动填充满屏幕的
        mTabStrip_not.setShouldExpand(true);
        // 设置Tab的分割线是透明的
        mTabStrip_not.setDividerColor(Color.TRANSPARENT);
        // 设置Tab底部线的高度
        mTabStrip_not.setUnderlineHeight((int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, 2, dm));
        // 设置Tab Indicator的高度
        mTabStrip_not.setIndicatorHeight((int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, 2, dm));
        // 设置Tab标题文字的大小
        mTabStrip_not.setTextSize((int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, 16, dm));
        // 设置Tab Indicator的颜色
        mTabStrip_not.setIndicatorColor(getResources().getColor(R.color.color_theme));
        // 设置选中Tab文字的颜色 (这是我自定义的一个方法)
        mTabStrip_not.setSelectedTextColor(getResources().getColor(R.color.color_theme));//Color.parseColor("#4169E1")
        mTabStrip_not.setTextColor(getResources().getColor(R.color.text_74));
        // 取消点击Tab时的背景色
        mTabStrip_not.setTabBackground(0);

    }

    /**
     * tab页适配器
     */
    private class MyPagerAdapter extends FragmentPagerAdapter {

        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        private final String[] titles = {"检查通知", "系统提示"};

        @Override
        public CharSequence getPageTitle(int position) {
            return titles[position];
        }

        @Override
        public int getCount() {
            return titles.length;
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    if (sysNotificationFragment == null) {
                        sysNotificationFragment = new SysNotification();
                    }
                    return sysNotificationFragment;
                case 1:
                    if (checkFragment == null) {
                        checkFragment = new CheckFragment();
                    }
                    return checkFragment;
                default:
                    return null;
            }
        }
    }
}
