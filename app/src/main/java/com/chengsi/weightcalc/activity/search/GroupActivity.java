package com.chengsi.weightcalc.activity.search;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.View;

import com.chengsi.weightcalc.activity.BaseActivity;
import com.chengsi.weightcalc.fragment.BaseFragment;
import com.chengsi.weightcalc.fragment.searchread.BackMeasurementFragment;
import com.chengsi.weightcalc.fragment.searchread.BeforeMeasurementFragment;
import com.chengsi.weightcalc.fragment.searchread.CheckReportFragment;
import com.chengsi.weightcalc.fragment.searchread.ConstantMeasureFragment;
import com.chengsi.weightcalc.fragment.searchread.MidMeasurementFragment;
import com.chengsi.weightcalc.R;

import butterknife.ButterKnife;

//import com.chengsi.pregnancy.fragment.group.GroupChatFragment;

public class GroupActivity extends BaseActivity {
    public static int SUCCES = 0;
    public static int ERROR = 1;

    public static String check_id;
    public int checkType;
    public static final int[] TAB_ID_ARR = {R.id.tab_front_measure, R.id.tab_mid_measure, R.id.tab_back_measure,R.id.tab_constant_measure, R.id.tab_report};
    private BaseFragment mCurFragment;

    private BeforeMeasurementFragment mBeforeMeasurementFragment;
    private MidMeasurementFragment mMidMeasurementFragment;
    private BackMeasurementFragment mBackMeasurementFragment;
    private CheckReportFragment checkReportFragment;
    private ConstantMeasureFragment mconstantMeasureFragment;

    public GroupActivity() {
        // Required empty public constructor
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group);
        Intent intent = getIntent();
        hideToolbar();
        check_id = intent.getStringExtra("check_id");
        checkType = intent.getIntExtra("check_type", 1);

        mBeforeMeasurementFragment = new BeforeMeasurementFragment();
//        mMidMeasurementFragment = new MidMeasurementFragment();
//        mBackMeasurementFragment = new BackMeasurementFragment();
//        changeTab(findViewById(TAB_ID_ARR[Integer.valueOf(checkType)-1]));
        changeTabIndex(checkType-1);
        ButterKnife.inject(this);

    }
    public void changeTabIndex(int index){
        if (index < TAB_ID_ARR.length && index >= 0){
            changeTab(findViewById(TAB_ID_ARR[index]));
        }
    }

    public void changeTab(View view) {
        switch (view.getId()) {
            case R.id.tab_front_measure: {
                switchContent(mCurFragment, mBeforeMeasurementFragment);
                break;
            }
            case R.id.tab_mid_measure: {
                if (mMidMeasurementFragment == null) {
                    mMidMeasurementFragment = new MidMeasurementFragment();
                }
                switchContent(mCurFragment, mMidMeasurementFragment);
                break;
            }
            case R.id.tab_back_measure: {
                if (mBackMeasurementFragment == null) {
                    mBackMeasurementFragment = new BackMeasurementFragment();
                }
                switchContent(mCurFragment, mBackMeasurementFragment);
                break;
            }
            case R.id.tab_constant_measure: {
                if (mconstantMeasureFragment == null) {
                    mconstantMeasureFragment = new ConstantMeasureFragment();
                }
                switchContent(mCurFragment, mconstantMeasureFragment);
                break;
            }
            case R.id.tab_report: {
                if (checkReportFragment == null) {
                    checkReportFragment = new CheckReportFragment();
                }
                switchContent(mCurFragment, checkReportFragment);
                break;
            }
        }
        for (int id : TAB_ID_ARR) {
            if (view.getId() == id) {
                view.setSelected(true);
            } else {
                findViewById(id).setSelected(false);
            }
        }
    }

    public BaseFragment getCurFragment() {
        return mCurFragment;
    }

    public void switchContent(BaseFragment from, BaseFragment to) {
        if (mCurFragment != to) {
            mCurFragment = to;
            FragmentTransaction transaction = getSupportFragmentManager()
                    .beginTransaction();
            if (from != null) {
                if (!to.isAdded()) { // 先判断是否被add过
                    transaction.hide(from).add(R.id.tab_container, to).commitAllowingStateLoss(); // 隐藏当前的fragment，add下一个到Activity中
                } else {
                    transaction.hide(from).show(to).commitAllowingStateLoss();
                }
            } else {
                if (!to.isAdded()) { // 先判断是否被add过
                    transaction.add(R.id.tab_container, to).commitAllowingStateLoss(); // 隐藏当前的fragment，add下一个到Activity中
                } else {
                    transaction.show(to).commitAllowingStateLoss();
                }
            }
        }
    }


}
