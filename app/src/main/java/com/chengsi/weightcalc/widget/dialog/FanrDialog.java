/**   
* @Title: FanrDialog.java 
* @Package com.whistle.gamefanr.widget 
* @Description: TODO(用一句话描述该文件做什么) 
* @author xuyingjian@ruijie.com.cn   
* @date 2015年4月21日 下午3:33:17 
*/
package com.chengsi.weightcalc.widget.dialog;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.chengsi.weightcalc.R;
import com.chengsi.weightcalc.utils.UIUtils;

/**
 * @Description: 弹出框
 * @author xuyingjian@ruijie.com.cn   
 * @date 2015年4月21日 下午3:33:17 
 * @version 3.10  
 */
public abstract class FanrDialog extends DialogFragment {

	protected static final int DEFAULT_LEFT_MARGIN_DIP = 20; 
	protected FragmentActivity baseActivity;
	protected View contentView;
	protected OnFanrDismissListener mDismissListener;

	
	protected abstract int getContentViewId();
	
	/**
	 * 
	 */
	public FanrDialog() {
		baseActivity = getActivity();
	}
	
	protected int getMargins() {
		return UIUtils.convertDpToPixel(DEFAULT_LEFT_MARGIN_DIP, getActivity());
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		contentView = inflater.inflate(getContentViewId(), container); 
		initViews();
		return contentView;
	}
	
	protected void initViews() {
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setStyle(DialogFragment.STYLE_NO_FRAME, R.style.tipsDialog);
	}

	@Override
	public void onStart() {
		super.onStart();
		WindowManager m = getActivity().getWindow().getWindowManager();
		Display d = m.getDefaultDisplay();
		Dialog dialog = getDialog();
        if (dialog != null) {
        	dialog.getWindow().setLayout(d.getWidth() - 2 * getMargins(), ViewGroup.LayoutParams.WRAP_CONTENT);
        }
	}
	
	@Override
	public void show(FragmentManager manager, String tag) {
		if(isAdded()){
			return;
		}
		super.show(manager, tag);
	}

	/**
	 * @param mDismissListener the mDismissListener to set
	 */
	public void setmDismissListener(OnFanrDismissListener mDismissListener) {
		this.mDismissListener = mDismissListener;
	}
	
	@Override
	public void onDestroyView() {
		if(mDismissListener != null){
			mDismissListener.onDismiss(this);
		}
		super.onDestroyView();
	}
	
	public interface OnFanrDismissListener{
		public void onDismiss(DialogFragment dialogFragment);
	}
}
