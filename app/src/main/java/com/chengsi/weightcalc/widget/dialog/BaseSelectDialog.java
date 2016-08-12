/**   
* @Title: TipsDialog.java 
* @Package com.whistle.gamefanr.widget 
* @Description: TODO(用一句话描述该文件做什么) 
* @author xuyingjian@ruijie.com.cn   
* @date 2015年4月21日 下午3:34:40 
*/
package com.chengsi.weightcalc.widget.dialog;

import android.widget.ListView;

import com.chengsi.weightcalc.R;

/**
 * @Description: 提示信息弹出框 
 * @author xuyingjian@ruijie.com.cn   
 * @date 2015年4月21日 下午3:34:40 
 * @version 3.10  
 */
public class BaseSelectDialog extends FanrDialog {

	protected static BaseSelectDialog mDialog;
	protected ListView listView;
	public BaseSelectDialog() {
	}
	
	@Override
	protected int getContentViewId() {
		return R.layout.view_select_dialog;
	}
	
	@Override
	protected void initViews() {
		super.initViews();
		listView = (ListView) contentView.findViewById(R.id.listview);
	}
}
