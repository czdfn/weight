package com.chengsi.weightcalc.widget.dialog;

import android.support.v4.app.FragmentManager;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.chengsi.weightcalc.R;

/**
 * @Description: 确定取消提示框
 * @author xuyingjian@ruijie.com.cn
 * @date 2015年4月22日 下午2:10:40
 * @version 3.10
 */
public class FanrAlertDialog extends FanrDialog {

	private TextView tvContent;
	private String alertText;
	private String confirmText;
	private String cancelText;

	private OnClickListener confirmListener, cancelListener;
	protected static FanrAlertDialog mDialog;
	public FanrAlertDialog() {
	}
	
	public static FanrAlertDialog getInstance(){
		return new FanrAlertDialog();
	}
	@Override
	protected int getContentViewId() {
		return R.layout.view_alert_dialog;
	}

	@Override
	protected void initViews() {
		super.initViews();
		tvContent = (TextView) contentView.findViewById(R.id.tips_content_tv);
		TextView cancelTv = (TextView) contentView
				.findViewById(R.id.dialog_cancel_tv);
		tvContent.setText(alertText);
		if (cancelListener != null) {
			cancelTv.setOnClickListener(cancelListener);
		} else {
			cancelTv.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					dismiss();
				}
			});

		}
		TextView confirmTv = (TextView) contentView
				.findViewById(R.id.dialog_confirm_tv);

		if (confirmListener == null) {
			confirmTv.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					dismiss();
				}
			});
		} else {
			confirmTv.setOnClickListener(confirmListener);
		}
		
		if(!TextUtils.isEmpty(cancelText)){
			cancelTv.setText(cancelText);
		}else{
			cancelTv.setText(R.string.back);
		}
		if(!TextUtils.isEmpty(confirmText)){
			confirmTv.setText(confirmText);
		}else{
			confirmTv.setText(R.string.confirm);
		}
	}

	/**
	 * 
	 * @Description: 展示弹出框
	 * @param manager
	 * @param alertText
	 */
	public void showAlertContent(FragmentManager manager, String alertText) {
		this.alertText = alertText;
		this.confirmText = null;
		this.cancelText = null;
		show(manager, alertText + FanrAlertDialog.class.getName());
	}

	public void showAlertContent(FragmentManager manager, String alertText, OnClickListener confirmListner) {
		this.alertText = alertText;
		this.confirmListener = confirmListner;
		this.confirmText = null;
		this.cancelText = null;
		show(manager, alertText + FanrAlertDialog.class.getName());
	}
	
	public void showAlertContent(FragmentManager manager, String alertText, OnClickListener confirmListner, OnClickListener cancelListener) {
		this.alertText = alertText;
		this.confirmText = null;
		this.cancelText = null;
		this.confirmListener = confirmListner;
		this.cancelListener = cancelListener;
		show(manager, alertText + FanrAlertDialog.class.getName());
	}

	public void showAlertContent(FragmentManager manager, String confirmText, String cancelText, String alertText, OnClickListener confirmListner, OnClickListener cancelListener) {
		this.alertText = alertText;
		this.confirmText = confirmText;
		this.cancelText = cancelText;
		this.confirmListener = confirmListner;
		this.cancelListener = cancelListener;
		show(manager, alertText + FanrAlertDialog.class.getName());
	}
}
