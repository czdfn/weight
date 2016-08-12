/**   
* @Title: TipsDialog.java 
* @Package com.whistle.gamefanr.widget 
* @Description: TODO(用一句话描述该文件做什么) 
* @author xuyingjian@ruijie.com.cn   
* @date 2015年4月21日 下午3:34:40 
*/
package com.chengsi.weightcalc.widget.dialog;

import android.support.v4.app.FragmentManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.chengsi.weightcalc.R;

/**
 * @Description: 提示信息弹出框 
 * @author xuyingjian@ruijie.com.cn   
 * @date 2015年4月21日 下午3:34:40 
 * @version 3.10  
 */
public class FanrTipsDialog extends FanrDialog {
	
	protected static FanrTipsDialog mDialog;
	protected TextView tvContent;
	
	private String tipsText;
	public FanrTipsDialog() {
	}
	
	public static FanrTipsDialog getInstance(){
		return new FanrTipsDialog();
	}
	@Override
	protected int getContentViewId() {
		return R.layout.view_tips_dialog;
	}
	
	@Override
	protected void initViews() {
		super.initViews();
		
		tvContent = (TextView) contentView.findViewById(R.id.tips_content_tv);
		TextView tv = (TextView) contentView.findViewById(R.id.dialog_cancel_tv);
		tvContent.setText(tipsText);
		tv.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				dismiss();
			}
		});
	}
	
	/**
	 * 
	 * @Description: 展示弹出框
	 * @param manager
	 * @param tipsText
	 */
	public void showTipsContent(FragmentManager manager, String tipsText){
		show(manager, tipsText + FanrTipsDialog.class.getName());
		this.tipsText = tipsText;
	}
	
	/**
	 * 
	 * @Description: 展示弹出框
	 * @param manager
	 * @param tipsText
	 */
	public void showTipsContent(FragmentManager manager, String tipsText, OnFanrDismissListener dismissListener){
		show(manager, tipsText + FanrTipsDialog.class.getName());
		this.tipsText = tipsText;
		this.mDismissListener = dismissListener;
	}
}
