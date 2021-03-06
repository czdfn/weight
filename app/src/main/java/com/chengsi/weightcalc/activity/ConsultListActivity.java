package com.chengsi.weightcalc.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.util.TypedValue;

import com.chengsi.weightcalc.R;
import com.chengsi.weightcalc.fragment.consult.AllConsultListFragment;
import com.chengsi.weightcalc.fragment.consult.MyConsultListFragment;
import com.chengsi.weightcalc.widget.PagerSlidingTabStrip;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class ConsultListActivity extends BaseActivity {
    @InjectView(R.id.tabs)
    PagerSlidingTabStrip mTabStrip;
    @InjectView(R.id.pager)
    ViewPager mPager;

    AllConsultListFragment allConsultFragment;
    MyConsultListFragment myConsultFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consult_list);
        setMyTitle(R.string.title_activity_consult_list);
        ButterKnife.inject(this);

        initViews();
    }

    private void initViews() {

        mPager.setAdapter(new MyPagerAdapter(getSupportFragmentManager()));
        mTabStrip.setViewPager(mPager);
        setTabStyle();
    }

    private void setTabStyle() {
        DisplayMetrics dm = getResources().getDisplayMetrics();

        // 设置Tab是自动填充满屏幕的
        mTabStrip.setShouldExpand(true);
        // 设置Tab的分割线是透明的
        mTabStrip.setDividerColor(Color.TRANSPARENT);
        // 设置Tab底部线的高度
        mTabStrip.setUnderlineHeight((int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, 2, dm));
        // 设置Tab Indicator的高度
        mTabStrip.setIndicatorHeight((int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, 2, dm));
        // 设置Tab标题文字的大小
        mTabStrip.setTextSize((int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, 16, dm));
        // 设置Tab Indicator的颜色
        mTabStrip.setIndicatorColor(getResources().getColor(R.color.color_theme));
        // 设置选中Tab文字的颜色 (这是我自定义的一个方法)
        mTabStrip.setSelectedTextColor(getResources().getColor(R.color.color_theme));//Color.parseColor("#4169E1")
        mTabStrip.setTextColor(getResources().getColor(R.color.text_74));
        // 取消点击Tab时的背景色
        mTabStrip.setTabBackground(0);

    }

    /**
     * tab页适配器
     */
    private class MyPagerAdapter extends FragmentPagerAdapter {

        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        private final String[] titles = {"我的咨询", "所有咨询"};

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
                    if (myConsultFragment == null) {
                        myConsultFragment = new MyConsultListFragment();
                    }
                    return myConsultFragment;
                case 1:
                    if (allConsultFragment == null) {
                        allConsultFragment = new AllConsultListFragment();
                    }
                    return allConsultFragment;
                default:
                    return null;
            }
        }
    }
}
