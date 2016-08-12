package com.chengsi.weightcalc.fragment.index;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.chengsi.weightcalc.activity.AddCompanyActivity;
import com.chengsi.weightcalc.activity.AddMaterialActivity;
import com.chengsi.weightcalc.activity.AddUserActivity;
import com.chengsi.weightcalc.activity.MyNotificationActivity;
import com.chengsi.weightcalc.activity.SettingsActivity;
import com.chengsi.weightcalc.activity.SynActivity;
import com.chengsi.weightcalc.activity.UserInfoActivity;
import com.chengsi.weightcalc.fragment.BaseFragment;
//import com.chengsi.pregnancy.manager.UserManager;
import com.chengsi.weightcalc.widget.PreferenceRightDetailView;
import com.chengsi.weightcalc.R;
import com.umeng.update.UmengUpdateAgent;
import com.umeng.update.UmengUpdateListener;
import com.umeng.update.UpdateResponse;
import com.umeng.update.UpdateStatus;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class MeFragment extends BaseFragment implements View.OnClickListener{

    @InjectView(R.id.iv_user_head)
    ImageView ivUserHead;
    @InjectView(R.id.iv_gender)
    ImageView ivGender;
    @InjectView(R.id.tv_user_nickname)
    TextView tvNickName;
    @InjectView(R.id.panel_user_info)
    View panelUserInfo;
    @InjectView(R.id.item_my_doctor)
    PreferenceRightDetailView itemMyDoctor;
    @InjectView(R.id.item_my_hospital)
    PreferenceRightDetailView itemMyHospital;
    @InjectView(R.id.item_my_hospital_list)
    PreferenceRightDetailView itemMyHospitalList;
    @InjectView(R.id.item_my_doctor_list)
    PreferenceRightDetailView itemMyDoctorList;
    @InjectView(R.id.item_sync)
    PreferenceRightDetailView sync;
    @InjectView(R.id.item_my_settings)
    PreferenceRightDetailView itemSettings;
    @InjectView(R.id.item_version)
    PreferenceRightDetailView itemVersion;
    @InjectView(R.id.item_my_notification)
    PreferenceRightDetailView notification;

//    private MyOnClickListener myOnClickListener;
    public MeFragment() {
    }

    @Override
    public View getContentView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_me, container, false);
        ButterKnife.inject(this, view);
//        fanrApp.userManager.registerOnUserChanged(this);
//        myOnClickListener = new MyOnClickListener(getActivity());
        itemMyDoctor.setOnClickListener(this);
        itemMyDoctorList.setOnClickListener(this);
        sync.setOnClickListener(this);
        itemMyHospital.setOnClickListener(this);
        itemSettings.setOnClickListener(this);
        panelUserInfo.setOnClickListener(this);
        itemVersion.setOnClickListener(this);
        notification.setOnClickListener(this);
        itemMyHospitalList.setOnClickListener(this);
        initViews();
        initDataSource();
        UmengUpdateAgent.setUpdateListener(new UmengUpdateListener() {

            @Override
            public void onUpdateReturned(int arg0, UpdateResponse arg1) {
                switch (arg0) {
                    case UpdateStatus.No: // has no update
                        showToast("当前已是最新版本");
                        break;
                    case UpdateStatus.NoneWifi: // none wifi
                        showToast("没有wifi连接， 只在wifi下更新");
                        break;
                    case UpdateStatus.Timeout: // time out
                        showToast("无法连接网络服务器");
                        break;
                }
            }
        });
        return view;
    }

    @Override
    public void onClick(View v) {
        doClick(v);
    }

    private void initViews() {
        String versionName = fanrApp.getVersionName();
        if(versionName != null) {
            itemVersion.setContent("当前版本：" + versionName);
        }
//        itemMyHospitalList.setContent("中山一院生殖中心");
//        itemMyHospitalList.setAccessImageVisible(View.GONE);
//        if (fanrApp.userManager.isLogin()){
//            if (fanrApp.userManager.getUserBean().getDoctorBean() != null){
//                itemMyDoctor.setContent(fanrApp.userManager.getUserBean().getDoctorBean().getRealName());
//            }else{
//                itemMyDoctor.setContent(null);
//            }
//            tvNickName.setText(fanrApp.userManager.getUserBean().getNickname());
//            ivGender.setVisibility(View.VISIBLE);
//            if (fanrApp.userManager.getUserBean().getSex() == 2){
//                ivGender.setImageResource(R.drawable.icon_boy);
//            }else if (fanrApp.userManager.getUserBean().getSex() == 1){
//                ivGender.setImageResource(R.drawable.icon_girl);
//            }else{
//                ivGender.setVisibility(View.GONE);
//            }
//            ImageLoaderUtils.displayImageForDefaultHead(ivUserHead, JDUtils.getRemoteImagePath(fanrApp.userManager.getUserBean().getHeadImg()));
//        }else{
//            ivGender.setVisibility(View.GONE);
//            tvNickName.setText("未登录");
//            ivUserHead.setImageResource(R.drawable.avatar_def);
//        }
//        notification.setContent("111");
    }

    @Override
    protected void initDataSource(){

//        JDHttpClient.getInstance().reqUserInfo(getActivity() , new JDHttpResponseHandler<UserBean>(new TypeReference<BaseBean<UserBean>>(){}){
//            @Override
//            public void onRequestCallback(BaseBean<UserBean> result) {
//                super.onRequestCallback(result);
//                if (result.isSuccess()){
////                    fanrApp.userManager.resetUser(result.getData());
//                }
//            }
//        });
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden){
            initDataSource();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!isHidden()){
            initDataSource();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
//        fanrApp.userManager.unRegisterOnUserChanged(this);
    }

//    @Override
//    public void onUserChanged(UserBean userBean) {
//        initViews();
//    }

//    private class MyOnClickListener {
//
//        public MyOnClickListener(Activity activity) {
//            super(activity);
//        }
//
//        @Override
        public void doClick(View v) {
            Intent intent;
            switch (v.getId()){
                case R.id.panel_user_info:
                    intent = new Intent(getActivity(), UserInfoActivity.class);
//                    intent.putExtra(UserInfoActivity.KEY_USER_INFO, fanrApp.userManager.getUserBean());
                    startActivity(intent);
                    break;
                case R.id.item_my_doctor:
                    intent = new Intent(getActivity(), AddCompanyActivity.class);
                    startActivity(intent);
                    break;
                case R.id.item_my_doctor_list:
                    break;
                case R.id.item_sync:
                    intent = new Intent(getActivity(), SynActivity.class);
                    startActivity(intent);
                    break;
                case R.id.item_my_hospital:
                    intent = new Intent(getActivity(), AddUserActivity.class);
                    startActivity(intent);
                    break;
                case R.id.item_my_hospital_list:
                    intent = new Intent(getActivity(), AddMaterialActivity.class);
                    startActivity(intent);
                    break;
                case R.id.item_my_settings:
                    intent = new Intent(getActivity(), SettingsActivity.class);
                    startActivity(intent);
                    break;
                case R.id.item_my_notification:
                    intent = new Intent(getActivity(), MyNotificationActivity.class);
                    startActivity(intent);
                    break;
                case R.id.item_version:
                    UmengUpdateAgent.forceUpdate(getActivity());
                    break;
            }
        }
//    }
}
