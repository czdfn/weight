package com.chengsi.weightcalc.fragment.index;


import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alibaba.fastjson.TypeReference;
import com.chengsi.weightcalc.R;
import com.chengsi.weightcalc.activity.CheckListActivity;
import com.chengsi.weightcalc.activity.DoctorAdviceActivity;
import com.chengsi.weightcalc.activity.TaskDetailActivity;
import com.chengsi.weightcalc.activity.TreatHistoryActivity;
import com.chengsi.weightcalc.bean.BaseBean;
import com.chengsi.weightcalc.bean.CheckResultBean;
import com.chengsi.weightcalc.fragment.BaseFragment;
import com.chengsi.weightcalc.http.JDHttpClient;
import com.chengsi.weightcalc.http.JDHttpResponseHandler;
import com.chengsi.weightcalc.widget.JDLoadingView;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 */
public class TreatFragment extends BaseFragment {

    @InjectView(R.id.viewpager)
    ViewPager viewPager;

    MyPageAdapter adpter;

    public static List<CheckResultBean> checkResultBeanList = new ArrayList<>();

    public TreatFragment() {
        // Required empty public constructor
    }

    @Override
    public View getContentView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_treat, container, false);
        ButterKnife.inject(this, view);
        adpter = new MyPageAdapter(getFragmentManager());
        viewPager.setAdapter(adpter);
        initDataSource();

        return view;
    }

    public void initDataSource() {
        setLoadingState(JDLoadingView.STATE_LOADING);
        JDHttpClient.getInstance().reqOvulation(getActivity(), new JDHttpResponseHandler<List<CheckResultBean>>(new TypeReference<BaseBean<List<CheckResultBean>>>() {
        }) {
            @Override
            public void onRequestCallback(BaseBean<List<CheckResultBean>> result) {
                super.onRequestCallback(result);
                if (result.isSuccess()) {
                    dismissLoadingView();
                    checkResultBeanList = result.getData();
                    adpter.notifyDataSetChanged();
                } else {
                    setLoadingState(JDLoadingView.STATE_FAILED);
                }
            }
        });
    }

    private boolean checkIsBind(){
//        if (fanrApp.userManager.getUserBean().getHospital() == null) {
//
//            final FanrAlertDialog dialog = FanrAlertDialog.getInstance();
//            dialog.showAlertContent(getFragmentManager(), "需要绑定生殖中心才能继续使用该功能，确定要绑定么？", new OnContinuousClickListener() {
//                @Override
//                public void onContinuousClick(View v) {
//                    dialog.dismiss();
//                    startActivity(new Intent(getActivity(), BindHospitalActivity.class));
//                }
//            });
//            return false;
//        }

        return true;
    }

    @OnClick(R.id.item_my_tasks)
    void showMyTasks() {
        if (checkIsBind()){
            Intent intent = new Intent(getActivity(), TaskDetailActivity.class);
            intent.putExtra(TaskDetailActivity.KEY_IS_TODAY_TASK, true);
            startActivity(intent);
        }
    }

    @OnClick(R.id.item_my_treat_history)
    void showMyTreatHistory() {
        if (checkIsBind()) {
            Intent intent = new Intent(getActivity(), TreatHistoryActivity.class);
//            intent.putExtra("token",fanrApp.userManager.getUserBean().getToken());
            startActivity(intent);
        }
    }

    @OnClick(R.id.item_my_prescription)
    void showAdvice() {
        if (checkIsBind()){
            Intent intent = new Intent(getActivity(), DoctorAdviceActivity.class);
            startActivity(intent);
        }
    }

    @OnClick(R.id.item_my_check)
    void showCheckHistory() {
        if (checkIsBind()){
            Intent intent = new Intent(getActivity(), CheckListActivity.class);
//            intent.putExtra("token",fanrApp.userManager.getUserBean().getToken());
            startActivity(intent);
        }
    }

    private class MyPageAdapter extends FragmentPagerAdapter {

        public MyPageAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public android.support.v4.app.Fragment getItem(int position) {
            android.support.v4.app.Fragment fragment = new LineChartFragment();
            Bundle bundle = new Bundle();
            bundle.putInt("position", position);
            fragment.setArguments(bundle);
            return fragment;
        }

        @Override
        public int getCount() {
//            return fanrApp.userManager.getUserBean().getHospital() == null || ListUtils.isEmpty(checkResultBeanList) ? 0 : 8;
            return 0;
        }
    }

//    @Override
//    public void onUserChanged(UserBean userBean) {
//        super.onUserChanged(userBean);
//        if (userBean != null){
//            adpter.notifyDataSetChanged();
//        }
//    }
}
