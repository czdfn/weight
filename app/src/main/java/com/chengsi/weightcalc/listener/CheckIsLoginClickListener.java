package com.chengsi.weightcalc.listener;

import android.support.v7.app.AppCompatActivity;
import android.view.View;

//import LoginActivity;


public abstract class CheckIsLoginClickListener extends OnContinuousClickListener {
	private AppCompatActivity activity;
	public CheckIsLoginClickListener(AppCompatActivity activity) {
		super();
		this.activity = activity;
	}
	
	public abstract void doClick(View v);

	@Override
	public void onContinuousClick(View v) {
//		if (!MyApplication.getInstance().userManager.isLogin()) {
//			final FanrAlertDialog dialog = FanrAlertDialog.getInstance();
//			dialog.showAlertContent(activity.getSupportFragmentManager(), "需要登录才能继续使用，确定要登录么？", new OnContinuousClickListener() {
//				@Override
//				public void onContinuousClick(View v) {
//					dialog.dismiss();
//					activity.startActivity(new Intent(activity, LoginActivity.class));
//				}
//			});
//			return;
//		}
		doClick(v);	}
}
