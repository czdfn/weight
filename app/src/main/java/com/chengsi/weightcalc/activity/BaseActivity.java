package com.chengsi.weightcalc.activity;

import android.annotation.TargetApi;
import android.app.Dialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager.OnActivityResultListener;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

import com.chengsi.weightcalc.MyApplication;
import com.chengsi.weightcalc.R;
import com.chengsi.weightcalc.constants.StorageConstants;
import com.chengsi.weightcalc.listener.OnContinuousClickListener;
import com.chengsi.weightcalc.listener.StartActivityForResultInterface;
//import com.chengsi.pregnancy.manager.UserManager;
import com.chengsi.weightcalc.storage.JDStorage;
import com.chengsi.weightcalc.utils.JDUtils;
import com.chengsi.weightcalc.utils.LogUtil;
import com.chengsi.weightcalc.utils.UIUtils;
import com.chengsi.weightcalc.widget.FanrRefreshListView;
import com.chengsi.weightcalc.widget.JDLoadingView;
import com.chengsi.weightcalc.widget.JDToast;
import com.chengsi.weightcalc.widget.dialog.FanrAlertDialog;
import com.umeng.analytics.MobclickAgent;
import com.umeng.update.UmengUpdateAgent;
import com.umeng.update.UmengUpdateListener;
import com.umeng.update.UpdateResponse;


public class BaseActivity extends AppCompatActivity implements StartActivityForResultInterface {
	protected static final String TAG = BaseActivity.class.getSimpleName();
	
	protected MyApplication application;
	
	protected Toolbar toolbar;
	
	protected RelativeLayout mainPanel;
	protected ImageView ivTitleRedDot;//右上角小红点

	private long lastCheckUpdateTime = 0;


	private RelativeLayout mLoadingCantainer = null;

	private boolean isDestoryed = false;

	protected LayoutInflater mInflater;
	
	private JDLoadingView mAnanLoadingView = null;
		
	
	public View getMyTitleView () {
		return this.toolbar;
	}
	
	protected boolean shouldRunAfterRosterFected() {
		return true;
	}
//	public static String DB_NAME = "weightdatabase.db";
//	public static String MEASURE_TABLE = "weightcalc";
//	public static int DB_VERSION = 1;
//	public DBHelper dbHelper = new DBHelper(this, DB_NAME, null, DB_VERSION);

	/**
	 * 个别Activity需要自定义title样式可以重写这个方法
	 * @param paramIntent
	 */
	protected void initToolbar(Intent paramIntent){
		
		ivTitleRedDot = (ImageView) findViewById(R.id.iv_right_reddot);
		ivTitleRedDot.setVisibility(View.GONE);//默认隐藏小红点
		
		this.toolbar = (Toolbar) findViewById(R.id.toolbar);
		if (toolbar != null) {
			toolbar.bringToFront();
			setSupportActionBar(toolbar);
			getSupportActionBar().setDisplayHomeAsUpEnabled(true);
			getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
			getSupportActionBar().setHomeButtonEnabled(true);
			getSupportActionBar().setDisplayShowHomeEnabled(true);
			
		}

	}
	public void onknow(String string) {
		Toast.makeText(this, string, Toast.LENGTH_SHORT).show();
		finish();
	}

	public void onknow() {
		Toast.makeText(this, "出现未知错误", Toast.LENGTH_SHORT).show();
		finish();
	}
	/**
	 * 显示小红点
	 */
	protected void showTitleRedDot(){
		ivTitleRedDot.setVisibility(View.VISIBLE);
	}
	/**
	 * 隐藏小红点
	 */
	protected void hideTitleRedDot(){
		ivTitleRedDot.setVisibility(View.GONE);
	}


	public void hideToolbar () {
		this.toolbar.setVisibility(View.GONE);
	}
	public void showToolbar () {
		this.toolbar.setVisibility(View.VISIBLE);
	}

	public void setMyTitle(String text) {
		setTitle(text);		
	}
	public void setMyTitle(int resID){
		setTitle(resID);		
	}
	
	@Override
	public void onBackPressed() {
		if(onBackAction()){
			super.onBackPressed();			
		}else{
			LogUtil.i(TAG, "-----you have already handle onBackPressed() yourself");
		}
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == android.R.id.home) {
			if(onBackAction() ){
				finish();				
			}else{
				LogUtil.i(TAG, "-----you have already handle onOptionsItemSelected(MenuItem item) yourself");
			}
			return true;
		} else {
			return super.onOptionsItemSelected(item);
		}
	}

	/**
	 * 返回的操作处理事件
	 * @return false表示自己完全处理，不会finish操作
	 * @return true表示添加自定义操作后，自动finish
	 */
	protected boolean onBackAction(){
		return true;
	}
	
	
	
	
	protected int fetchTitleLayout() {
		return R.layout.title_layout;
	}
	
	protected boolean isInAbnormalState(Bundle savedInstanceState) {
//		return FanrUtils.isInAbnormalState(application, savedInstanceState, shouldRunAfterRosterFected());
		return false;
	}
	
	private boolean isFullScreenEnable = false;
	
	protected boolean isFullScreenEnable() {
		return isFullScreenEnable;
	}

	@TargetApi(21)
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		this.application = (MyApplication) getApplicationContext();



		mInflater = LayoutInflater.from(this);
		if(Build.VERSION.SDK_INT >= 21){
			getWindow().setNavigationBarColor(getResources().getColor(R.color.color_theme));
		}
		
		super.onCreate(savedInstanceState);
		
		showEnterAnimation();
		
		if(isFullScreenEnable()){
			getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		}
		isDestoryed = false;
		
		super.setContentView(fetchTitleLayout());
		

		initToolbar(getIntent());//初始化title
		
		mLoadingCantainer = (RelativeLayout)this.findViewById(R.id.loading_view);

//		application.userManager.registerOnUserChanged(this);
	}
	
	protected void showEnterAnimation(){
		//overridePendingTransition(R.anim.right_in, R.anim.left_out);
		
	}
	
	protected void showExitAnimation(){
		//overridePendingTransition(R.anim.left_in, R.anim.right_out);
		// 关闭窗体动画显示
		//this.overridePendingTransition(R.anim.bottom_out, 0);
	}

	public boolean isDestoryed () {
		return isDestoryed;
	}
	
	
	@Override
	public void finish() {
		super.finish();
		showExitAnimation();
		
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if(loadingDialog!=null && loadingDialog.isShowing()) {
			loadingDialog.dismiss();
		}
		isDestoryed = true;
//		application.userManager.unRegisterOnUserChanged(this);
	}

	protected View generateDefaultLeftView() {
		return null;
	}

	
	@Override
	public void setContentView(int layoutResID) {
		View childView = LayoutInflater.from(this).inflate(layoutResID, null);
		this.setContentView(childView);
	}

	@Override
	public void setContentView(View view) {
		this.setContentView(view, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
	}

	@Override
	public void setContentView(View view, ViewGroup.LayoutParams params) {
		
		mainPanel = (RelativeLayout) findViewById(R.id.main_panel);
		LayoutParams relativeParams = new LayoutParams(params);
		relativeParams.addRule(RelativeLayout.BELOW, R.id.error_tip_panel);
		mainPanel.addView(view, relativeParams);
	}

	private Dialog loadingDialog = null;
	public void showLoadingPanel(String msg, boolean cancelable) {
		if(loadingDialog != null) {
			if(!loadingDialog.isShowing()) {
				loadingDialog.show();
			}
			TextView tv = (TextView) loadingDialog.findViewById(R.id.loading_tv);
			tv.setText(msg);
			return;
		}
		loadingDialog = JDUtils.showLoadingDialog(this, msg, cancelable, null);
	}
	
	public boolean isDialogShowing () {
		return loadingDialog != null && loadingDialog.isShowing();
	}

	public void hideLoadingPanel() {
		if(loadingDialog!=null && loadingDialog.isShowing()) {
			loadingDialog.dismiss();
		}
	}
	
	public Dialog getLoadingDialog () {
		return loadingDialog;
	}
	
	public void showLoadingView () {
		showLoadingView(JDLoadingView.STATE_LOADING, -1);
	}
	
	public void showLoadingView (int state, int stringId) {
		mLoadingCantainer.setVisibility(View.VISIBLE);
		if(mAnanLoadingView == null){
			mAnanLoadingView = JDUtils.addLoadingView(mLoadingCantainer);
		}
		if(stringId >0){
			try {
				mAnanLoadingView.setTextState(state, this.getString(stringId));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		mAnanLoadingView.setState(state);
		
	}
	
	public void setLoadingViewListener (JDLoadingView.OnManageClickListener listener) {
		if (mAnanLoadingView == null) {
			mAnanLoadingView = JDUtils.addLoadingView(mLoadingCantainer);
		}
		mAnanLoadingView.setOnManageListener(listener);
	}
	
	public void setLoadingViewState (int state) {
		if (mAnanLoadingView == null) {
			mAnanLoadingView = JDUtils.addLoadingView(mLoadingCantainer);
			mAnanLoadingView.setOnManageListener(new JDLoadingView.OnManageClickListener() {
				@Override
				public void onEmptyClick(View view) {
					initDataSource();
				}

				@Override
				public void onLoadingClick(View view) {

				}

				@Override
				public void onFailedClick(View view) {
					initDataSource();
				}
			});
		}
		mAnanLoadingView.setState(state);
		showLoadingView(state, -1);
	}
	public void setLoadingViewState (int state,String tip) {
		if (mAnanLoadingView == null) {
			mAnanLoadingView = JDUtils.addLoadingView(mLoadingCantainer);
		}
		mAnanLoadingView.setTextState(state, tip);
		mAnanLoadingView.setState(state);
		showLoadingView(state, -1);
	}
	
	public void dismissLoadingView () {
		mLoadingCantainer.setVisibility(View.GONE);
		mainPanel.setVisibility(View.VISIBLE);
	}
	
	private JDLoadingView listEmptyView;
	private ListView emptyListView;
	public void setEmptyViewToListView(ListView listView){
		this.emptyListView = listView;
		if (listEmptyView == null && listView != null) {
			listEmptyView = new JDLoadingView(this);
			listEmptyView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
			if (listView instanceof FanrRefreshListView){
				((ViewGroup) listView.getParent().getParent()).addView(listEmptyView);
			}else{
				((ViewGroup) listView.getParent()).addView(listEmptyView);
			}
			listEmptyView.setState(JDLoadingView.STATE_EMPTY);
			listView.setEmptyView(listEmptyView);
			listEmptyView.setOnManageListener(new JDLoadingView.OnManageClickListener() {
				@Override
				public void onEmptyClick(View view) {
					initDataSource();
				}

				@Override
				public void onLoadingClick(View view) {

				}

				@Override
				public void onFailedClick(View view) {
					initDataSource();
				}
			});
		}
	}
	public void setEmptyViewListener (JDLoadingView.OnManageClickListener listener) {
		if (listEmptyView == null) {
			return;
		}
		listEmptyView.setOnManageListener(listener);
	}
	public void setEmptyViewState(int state){
		if (listEmptyView == null) {
			return;
		}
		if (this.emptyListView != null){
			if (this.emptyListView instanceof FanrRefreshListView){
				View view = (View) this.emptyListView.getParent();
				view.setVisibility(View.GONE);
			}else{
				this.emptyListView.setVisibility(View.GONE);
			}
			emptyListView.requestLayout();
		}
		listEmptyView.setVisibility(View.VISIBLE);
//		listEmptyView.getParent().requestLayout();
		listEmptyView.setState(state);
	}
	public void setEmptyViewState(int state, String emptyTips){
		if (listEmptyView == null) {
			return;
		}
		if (this.emptyListView != null){
			if (this.emptyListView instanceof FanrRefreshListView){
				View view = (View) this.emptyListView.getParent();
				view.setVisibility(View.GONE);
			}else{
				this.emptyListView.setVisibility(View.GONE);
			}
			emptyListView.requestLayout();
		}
		listEmptyView.setTextState(state, emptyTips);
		listEmptyView.setVisibility(View.VISIBLE);
//		listEmptyView.getParent().requestLayout();
	}

	public void dismissEmptyView(){
		if (this.emptyListView != null){
			if (this.emptyListView instanceof FanrRefreshListView){
				View view = (View) this.emptyListView.getParent();
				view.setVisibility(View.VISIBLE);
			}else{
				this.emptyListView.setVisibility(View.VISIBLE);
			}
		}
		listEmptyView.setVisibility(View.GONE);
	}

	@Override
	protected void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);

		lastCheckUpdateTime = JDStorage.getInstance().getLongValue(StorageConstants.KEY_LAST_UPDATE_TIME, 0);
		if (System.currentTimeMillis() / 1000 - lastCheckUpdateTime > 180 * 60){
			UmengUpdateAgent.setUpdateListener(new UmengUpdateListener() {

				@Override
				public void onUpdateReturned(int arg0, UpdateResponse arg1) {
				}
			});
			lastCheckUpdateTime = System.currentTimeMillis() / 1000;
			JDStorage.getInstance().storeLongValue(StorageConstants.KEY_LAST_UPDATE_TIME, lastCheckUpdateTime);
			UmengUpdateAgent.update(this);
			UmengUpdateAgent.silentUpdate(this);

		}
	}
	
	
	@Override
	protected void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
	}

	public int getTitleHeight(){
		return toolbar.getHeight();
	}
	
	protected View generateTextRightView(String text) {
		TextView btn = new TextView(this);
		if (text!=null) {
			btn.setText(text);
		}
		btn.setTextColor(getResources().getColorStateList(R.color.right_btn_color_sel));
		btn.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 16);
		btn.setGravity(Gravity.CENTER);
		btn.setPadding(UIUtils.convertDpToPixel(10, this), 0, UIUtils.convertDpToPixel(10, this), 0);
		
		btn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
		return btn;
	}
	
	protected View generateTextRightView(int resId) {
		return generateTextRightView(getString(resId));
	}
	protected View generateTextRightView() {
		return generateTextRightView("");
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		Log.d("caca", "onActivityResult llllll");
		if(listener != null)
		{
			this.listener.onActivityResult(requestCode, resultCode, data);
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
	

	protected void showToast(String text) {
		if(TextUtils.isEmpty(text)){
			return;
		}
		JDToast.makeText(this, text, JDToast.LENGTH_SHORT).show();
	}
	
	protected void showToast(int resId) {
		String str = getString(resId);
		if(TextUtils.isEmpty(str)){
			return;
		}
		JDToast.makeText(this, str, JDToast.LENGTH_SHORT).show();
	}

	protected void initDataSource() {

	}

	protected boolean checkStatusBarWhileLifecycleChanged() {
		return true;
	}
	private OnActivityResultListener listener;

	@Override
	public void startActivityForResult(Intent intent, int requestCode, OnActivityResultListener listener) {
		if(listener != null)
		{
			this.listener =listener;
		}
		super.startActivityForResult(intent, requestCode);
	}

//	@Override
//	public void onUserChanged(UserBean userBean) {
//		if (userBean == null)
//			finish();
//	}


	public void showUpdateDialog() {

		final FanrAlertDialog dialog = FanrAlertDialog.getInstance();
		dialog.setCancelable(true);
		dialog.showAlertContent(getSupportFragmentManager(), "确定","取消","确定返回到之前页面？", new OnContinuousClickListener() {
			@Override
			public void onContinuousClick(View v) {
				finish();
			}
		}, new OnContinuousClickListener() {
			@Override
			public void onContinuousClick(View v) {
				dialog.dismiss();
			}
		});
	}
}
