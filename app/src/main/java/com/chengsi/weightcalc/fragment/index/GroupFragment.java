package com.chengsi.weightcalc.fragment.index;


import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chengsi.weightcalc.R;
import com.chengsi.weightcalc.fragment.BaseFragment;
import com.chengsi.weightcalc.fragment.group.CollectionFragment;
import com.chengsi.weightcalc.fragment.group.ConsulationFragment;
import com.chengsi.weightcalc.fragment.group.DynamicFragment;
//import com.chengsi.pregnancy.fragment.group.GroupChatFragment;
import com.chengsi.weightcalc.widget.PagerSlidingTabStrip;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class GroupFragment extends BaseFragment {

    @InjectView(R.id.tabs)
    PagerSlidingTabStrip mTabStrip;
    @InjectView(R.id.pager)
    ViewPager mPager;

//    private GroupChatFragment chatFragment;
    private MeFragment meFragment;
    private DynamicFragment dynamicFragment;
    private ConsulationFragment consulationFragment;
    private CollectionFragment collectionFragment;

    public GroupFragment() {
        // Required empty public constructor
    }


    @Override
    public View getContentView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_group, container, false);
        ButterKnife.inject(this, view);
        mPager.setAdapter(new MyPagerAdapter(getFragmentManager()));
        mTabStrip.setViewPager(mPager);
        setTabStyle();
        if (getArguments() != null && getArguments().getInt("index", 0) > 0){
            mPager.setCurrentItem(getArguments().getInt("index"));
        }
        return view;
    }

    public void showDynamic(){
        if (mPager != null){
            mPager.setCurrentItem(1);
        }
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

        private final String[] titles = {"前测", "中测", "后测", "报告单"};

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
                    if (meFragment == null) {
                        meFragment = new MeFragment();
                    }
                    return meFragment;
                case 1:
                    if (dynamicFragment == null) {
                        dynamicFragment = new DynamicFragment();
                    }
                    return dynamicFragment;
                case 2:
                    if (consulationFragment == null) {
                        consulationFragment = new ConsulationFragment();
                    }
                    return consulationFragment;
                case 3:
                    if (collectionFragment == null) {
                        collectionFragment = new CollectionFragment();
                    }
                    return collectionFragment;
                default:
                    return null;
            }
        }

    }

}