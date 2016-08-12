package com.chengsi.weightcalc.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.chengsi.weightcalc.MyApplication;
import com.chengsi.weightcalc.R;
//import com.chengsi.pregnancy.manager.UserManager;
import com.chengsi.weightcalc.widget.JDLoadingView;
import com.chengsi.weightcalc.widget.JDToast;

import butterknife.ButterKnife;


public abstract class BaseFragment extends Fragment{
	
	private static final String TAG = BaseFragment.class.getSimpleName();
	
	protected View baseView = null;
	protected View mContentView = null;
	protected FrameLayout mLoadingView = null;
	protected JDLoadingView mAnanLoadingView = null;
	protected Activity holderAct = null;
	protected MyApplication fanrApp = null;

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		holderAct = activity;
		fanrApp = (MyApplication)holderAct.getApplication();
//		fanrApp.userManager.registerOnUserChanged(this);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if (baseView == null) {
			if (canShowLoading()) {
				baseView = inflater.inflate(R.layout.fragment_base, null);
				mLoadingView = (FrameLayout)baseView.findViewById(R.id.loading_view);
				mAnanLoadingView = (JDLoadingView)baseView.findViewById(R.id.anan_loading);
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
				mContentView = getContentView(inflater, container, savedInstanceState);
				if (mContentView != null){
					mLoadingView.addView(mContentView, 0);
				}
				checkShouldShowLoadingView();
			} else {
				baseView = getContentView(inflater, container, savedInstanceState);
				mContentView = baseView;
			}
			
		}
		return baseView;
	}

	protected void initDataSource(){}
	
	public abstract View getContentView (LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState);
	
	public boolean canShowLoading () {
		return true;
	}
	protected void checkShouldShowLoadingView(){
	}
	public void showLoadingView () {
		if (!canShowLoading()) {
			return;
		}
		if(mAnanLoadingView != null){
			mAnanLoadingView.setVisibility(View.VISIBLE);
		}
		if(mContentView != null){
			mContentView.setVisibility(View.VISIBLE);
		}
	}

	public void showLoadingView (int state) {
		if (!canShowLoading()) {
			return;
		}
		if(mAnanLoadingView != null){
			mAnanLoadingView.setVisibility(View.VISIBLE);
			mAnanLoadingView.setState(state);
		}
		if(mContentView != null){
			mContentView.setVisibility(View.VISIBLE);
		}
	}

	public void dismissLoadingView () {
		if (!canShowLoading()) {
			return;
		}
		mAnanLoadingView.setVisibility(View.GONE);
		mContentView.setVisibility(View.VISIBLE);
		/*for (int i = mLoadingView.getChildCount() - 1; i >= 0; i--) {
			View child = mLoadingView.getChildAt(i);
			if (child instanceof AnanLoadingView) {
				mLoadingView.removeView(child);
			}
		}*/
		
	}
	
	public void setLoadingState (int state) {
		if (!canShowLoading() || mAnanLoadingView == null) {
			return;
		}
		mAnanLoadingView.setState(state);
		showLoadingView(state);
	}
	public void setLoadingState (int state,String stateText) {
		if (!canShowLoading() || mAnanLoadingView == null) {
			return;
		}
		mAnanLoadingView.setTextState(state, stateText);
		mAnanLoadingView.setState(state);
	}
	
	public void setLoadingManageListener (JDLoadingView.OnManageClickListener listener) {
		if (!canShowLoading() || mAnanLoadingView == null) {
			return;
		}
		mAnanLoadingView.setOnManageListener(listener);
	}

	public final boolean defaultOnBackPressed () {
		if (this.isAdded()) {
			FragmentManager man = this.getFragmentManager();
			FragmentTransaction trans = man.beginTransaction();
			trans.remove(this);
			trans.commit();
			return true;
		}
		return false;
	}
	
	public String getTitle (Context context) {
		return "";
	}
	
	@Override
	public void onResume() {
		super.onResume();
	}
	
	@Override
	public void onPause() {
		super.onPause();
	}

	protected void showToast(String text){
		if (getActivity() == null){
			return;
		}
		JDToast.makeText(getActivity(), text, JDToast.LENGTH_SHORT).show();
	}

	protected void showToast(int resId){
		if (getActivity() == null){
			return;
		}
		JDToast.makeText(getActivity(), resId, JDToast.LENGTH_SHORT).show();
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		ButterKnife.reset(this);
//		fanrApp.userManager.unRegisterOnUserChanged(this);
	}

//	@Override
//	public void onUserChanged(UserBean userBean) {
//
//	}
}
