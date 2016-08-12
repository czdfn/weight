/**   
* @Title: TipsDialog.java 
* @Package com.whistle.gamefanr.widget 
* @Description: TODO(用一句话描述该文件做什么) 
* @author xuyingjian@ruijie.com.cn   
* @date 2015年4月21日 下午3:34:40 
*/
package com.chengsi.weightcalc.widget.dialog;

import android.support.v4.app.FragmentManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import com.chengsi.weightcalc.R;
import com.chengsi.weightcalc.utils.JDUtils;
import com.chengsi.weightcalc.utils.UIUtils;

import java.util.List;

import cn.jiadao.corelibs.utils.ListUtils;

/**
 * @Description: 提示信息弹出框 
 * @author xuyingjian@ruijie.com.cn   
 * @date 2015年4月21日 下午3:34:40 
 * @version 3.10  
 */
public class SimpleSelectDialog extends BaseSelectDialog {

	private List<String> dataSource;
	private AdapterView.OnItemClickListener onItemClickListener;

	protected static SimpleSelectDialog mDialog;
	public SimpleSelectDialog() {
	}
	
	public static SimpleSelectDialog getInstance(){
		return new SimpleSelectDialog();
	}
	@Override
	protected int getContentViewId() {
		return R.layout.view_select_dialog;
	}

	@Override
	protected void initViews() {
		super.initViews();
		listView.setAdapter(new ArrayAdapter<String>(getActivity(), R.layout.item_simple_select, dataSource));
		if (!ListUtils.isEmpty(dataSource) && dataSource.size() <= 6){
			JDUtils.setListViewHeightBasedOnChildren(listView);
		}else{
			listView.getLayoutParams().height = UIUtils.convertDpToPixel(320, getActivity());
		}
		listView.setOnItemClickListener(onItemClickListener);
	}

	public void showSelect(FragmentManager manager, List<String> list, AdapterView.OnItemClickListener onItemClickListener){
		this.dataSource = list;
		this.onItemClickListener = onItemClickListener;
		show(manager, list + SimpleSelectDialog.class.getName());
	}
}
