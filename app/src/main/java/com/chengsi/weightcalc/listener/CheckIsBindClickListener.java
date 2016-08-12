package com.chengsi.weightcalc.listener;

import android.support.v7.app.AppCompatActivity;
import android.view.View;

//import LoginActivity;


/**
 * Created by apple on 15/9/22.
 */
public abstract class CheckIsBindClickListener extends OnContinuousClickListener{
    private AppCompatActivity activity;
    public CheckIsBindClickListener(AppCompatActivity activity) {
        super();
        this.activity = activity;
    }

    public abstract void doClick(View v);

    @Override
    public void onContinuousClick(View v) {
//        if (!MyApplication.getInstance().userManager.isLogin()) {
//            final FanrAlertDialog dialog = FanrAlertDialog.getInstance();
//            dialog.showAlertContent(activity.getSupportFragmentManager(), "需要登录才能继续使用，确定要登录么？", new OnContinuousClickListener() {
//                @Override
//                public void onContinuousClick(View v) {
//                    dialog.dismiss();
//                    activity.startActivity(new Intent(activity, LoginActivity.class));
//                }
//            });
//            return;
//        }
//        if (MyApplication.getInstance().userManager.getUserBean().getHospital() == null){
//            final FanrAlertDialog dialog = FanrAlertDialog.getInstance();
//            dialog.showAlertContent(activity.getSupportFragmentManager(), "需要绑定生殖中心才能继续使用该功能，确定要绑定么？", new OnContinuousClickListener() {
//                @Override
//                public void onContinuousClick(View v) {
//                    dialog.dismiss();
//                    activity.startActivity(new Intent(activity, BindHospitalActivity.class));
//                }
//            });
//            return;
//        }
        doClick(v);
    }
}
