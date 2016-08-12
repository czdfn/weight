/**   
* @Title: AnanFooterRefreshListView.java 
* @Package com.mobinweaver.app.ananim.widget 
* @Description: TODO(用一句话描述该文件做什么) 
* @author xuyingjian@ruijie.com.cn   
* @date 2014年6月17日 上午11:10:13 
*/
package com.chengsi.weightcalc.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ListView;

import com.chengsi.weightcalc.R;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;


/**    
 * @Description: 上拉加载更多数据
 * @author xuyingjian@ruijie.com.cn   
 * @date 2014年6月17日 上午11:10:13 
 * @version 2.20  
 */
public class FanrRefreshListView extends ListView implements OnScrollListener{

	private boolean pullRefreshEnable = false;
	
	private boolean canLoadMore;
	private OnLoadMoreListener onLoadMoreListener;
	private OnSizeChangedListener onSizeChangedListener;
	private JDProgressBar footerPanel;
	private View footerParent;
	
	private WeakReference<SwipeRefreshLayout> mParentLayout;
	
	private Context mContext;
	
	private NoMoreHandler mNoMoreHandler;
	
	private JDToast mToast;
	
	private List<OnScrollListener> scrollListenerList = new ArrayList<OnScrollListener>();
	
	private enum LoadingState{
		LOADING_STATE_START, LOADING_STATE_FINISH
	}
	
	private Object lockObject = new Object();
	
	private LoadingState mCurrentState = LoadingState.LOADING_STATE_FINISH;
	
	public static enum NoMoreHandler{
		NO_MORE_LOAD_SHOW_FOOTER_VIEW, NO_MORE_LOAD_NOT_SHOW_FOOTER_VIEW, NO_MORE_LOAD_SHOW_TOAST
	}
	/**
	 * @param context
	 * @param attrs
	 */
	public FanrRefreshListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.mContext = context;
		init(context, attrs, 0);
	}

	/**
	 * 
	 */
	public FanrRefreshListView(Context context) {
		super(context);
		this.mContext = context;
		init(context, null, 0);
	}
	
	/**
	 * 
	 */
	public FanrRefreshListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		this.mContext = context;
		init(context, attrs, defStyle);
	}
	
	private void init(Context context, AttributeSet attrs, int defStyle) {
		initFooterView(context);
		super.setOnScrollListener(this);
	}
	
	/**
	 * 
	 * @Description: 下拉刷新事件监听
	 * @param listener
	 */
	public void setOnPullRefreshListener(OnRefreshListener listener){
		if (mParentLayout == null || mParentLayout.get() != null) {
			initRefreshLayout();
		}
		mParentLayout.get().setOnRefreshListener(listener);
	}
	
	public void setOnSizeChangedListener(OnSizeChangedListener onSizeChangedListener){
		this.onSizeChangedListener = onSizeChangedListener;
	}
	
	@Override
	public void setOnScrollListener(OnScrollListener l) {
		scrollListenerList.add(l);
	}
	
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		if (onSizeChangedListener != null) {
			onSizeChangedListener.onSizeChanged(w, h, oldw, oldh);
		}
	}
	/**
	 * 
	 * @Description: 设置是否允许下拉刷新
	 * @param isEnable
	 */
	public void setPullRefreshEnable(boolean isEnable){
		pullRefreshEnable = isEnable;
		if (mParentLayout == null || mParentLayout.get() != null) {
			initRefreshLayout();
			if (pullRefreshEnable) {
				mParentLayout.get().setEnabled(pullRefreshEnable);
			}
		}else{
			mParentLayout.get().setEnabled(pullRefreshEnable);
		}
	}
	
	/**
	 * 
	 * @Description: 设置下拉刷新状态
	 * @param refreshing
	 */
	public void setPullRefreshing(boolean refreshing){
		if (mParentLayout == null || mParentLayout.get() != null) {
			initRefreshLayout();
		}
		mParentLayout.get().setRefreshing(refreshing);
	}
	
	private void initRefreshLayout(){
		if(getParent() != null){
			if(getParent() instanceof SwipeRefreshLayout){
				mParentLayout =  new WeakReference<SwipeRefreshLayout>((SwipeRefreshLayout) getParent());
			}else if(getParent() instanceof ViewGroup){
				SwipeRefreshLayout refreshLayout = new SwipeRefreshLayout(getContext());
				refreshLayout.setColorSchemeResources(R.color.pull_refresh_color_1, R.color.pull_refresh_color_2, R.color.pull_refresh_color_3);
				if(this.getLayoutParams() != null){
					refreshLayout.setLayoutParams(this.getLayoutParams());
				}
				ViewGroup vp = (ViewGroup) getParent();
				for(int i = 0; i < vp.getChildCount(); i ++){
					if(vp.getChildAt(i) == this){
						vp.removeViewAt(i);
						refreshLayout.addView(this);
						vp.addView(refreshLayout, i);
					}
				}
				mParentLayout = new WeakReference<SwipeRefreshLayout>(refreshLayout);
			}else{
				throw new RuntimeException("You must attach this listview to a ViewGroup!!");
			}
		}
	}

	private View initFooterView(Context context){
		footerParent = LayoutInflater.from(context).inflate(R.layout.app_lv_footer, null);
		footerPanel = (JDProgressBar) footerParent.findViewById(R.id.app_comment_lv_footer);
		footerPanel.setHintText(R.string.loading_wait);
		footerPanel.setAnimImageVisibility(View.VISIBLE);
		footerPanel.setVisibility(View.GONE);
		
		//避免代码中由于addFooterView晚于setAdapter，造成footerView不显示
		setFooterDividersEnabled(false);
		this.addFooterView(footerParent);
		
		this.getViewTreeObserver().addOnGlobalLayoutListener(mOnGlobalLayoutListener);
		return footerParent;
	}
	
	public void removeDefaultFooter(){
		if(getFooterViewsCount() > 0 && footerParent != null){
			removeFooterView(footerParent);
			footerParent = null;
		}
	}
	
	/**
	 * 
	 * @Description: 添加上拉加载数据的监听器，用于在回调方法里加载数据
	 * @param onLoadMoreListener
	 */
	public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener){
		setCanLoadMore(true);
		this.onLoadMoreListener = onLoadMoreListener;
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		if(scrollState == OnScrollListener.SCROLL_STATE_IDLE && view.getLastVisiblePosition() == view.getCount()-1){
			if(this.canLoadMore){
				
				if(onLoadMoreListener != null){
					footerPanel.setVisibility(View.VISIBLE);
					footerPanel.setHintText(R.string.loading_wait);
					footerPanel.setAnimImageVisibility(View.VISIBLE);
					
					synchronized (lockObject) {
						if(mCurrentState != LoadingState.LOADING_STATE_START){
							mCurrentState = LoadingState.LOADING_STATE_START;
							this.onLoadMoreListener.loadMore();
						}
					}
				}
			}else{
				if(this.mNoMoreHandler == NoMoreHandler.NO_MORE_LOAD_SHOW_TOAST && this.getFirstVisiblePosition() > 0){
					if(mToast != null){
						mToast.cancel();
					}
					mToast = JDToast.makeText(mContext, mContext.getString(R.string.no_more), JDToast.LENGTH_SHORT);
					mToast.show();
				}
			}
		}
		
//		if(scrollState == OnScrollListener.SCROLL_STATE_FLING){
//			ImageLoaderUtils.pause();
//		}else{
//			ImageLoaderUtils.resume();
//		}
		for (OnScrollListener listener : scrollListenerList) {
			listener.onScrollStateChanged(view, scrollState);
		}
	}

	/**
	 * @return the canLoadMore
	 */
	public boolean isCanLoadMore() {
		return canLoadMore;
	}

	/**
	 * @param canLoadMore the canLoadMore to set
	 */
	public void setCanLoadMore(boolean canLoadMore) {
		this.canLoadMore = canLoadMore;
		if(!canLoadMore){
			if(this.footerPanel != null){
				this.footerPanel.setOnClickListener(null);
				this.footerPanel.setEnabled(false);
			}
		}else{
			footerPanel.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					if(onLoadMoreListener != null){
						onLoadMoreListener.loadMore();
					}
				}
			});
		}
	}
	
	/**
	 * @deprecated 使用onLoadComplete()和onLoadCompleteNoMore(NoMoreHandler handler, int resId)
	 * @Description: 加载完数据后调用该方法， 在没有更多的时候隐藏加载条
	 * @param canLoadMore 是否还可以再加载数据
	 */
	@Deprecated
	public void onLoadComplete(boolean canLoadMore){
		this.onLoadCallback();
		setCanLoadMore(canLoadMore);
		if(!canLoadMore){
			if(getFooterViewsCount() > 0 && footerParent != null){
				removeFooterView(footerParent);
			}
		}else{
			if(footerPanel != null){
				footerPanel.setVisibility(View.VISIBLE);
				footerPanel.setHintText(R.string.pull_up_more);
				footerPanel.setAnimImageVisibility(View.GONE);
			}
		}
	}
	
	/**
	 * @deprecated 使用onLoadComplete()和onLoadCompleteNoMore(NoMoreHandler handler, int resId)
	 * @Description: 加载完数据后调用。在没有更多的时候不隐藏footerView 
	 * @param canLoadMore
	 */
	@Deprecated
	public void onLoadCompleteNotShowFooterView(boolean canLoadMore){
		this.onLoadCallback();
		setCanLoadMore(canLoadMore);
		if(!canLoadMore){
			footerPanel.setHintText(R.string.no_more);
			footerPanel.setAnimImageVisibility(View.GONE);
			footerPanel.setVisibility(View.VISIBLE);
		}else{
			footerPanel.setVisibility(View.VISIBLE);
			footerPanel.setHintText(R.string.pull_up_more);
			footerPanel.setAnimImageVisibility(View.GONE);
		}
	}
	
	public void onLoadFailed(int tipsResId){
		this.onLoadCallback();
		if(getFirstVisiblePosition() != 0){
			footerPanel.setVisibility(View.VISIBLE);
			footerPanel.setHintText(tipsResId);
			footerPanel.setAnimImageVisibility(View.GONE);
		}
	}
	
	/**
	 * 
	 * @Description: 加载完数据后, 还有更多时调用该方法
	 */
	public void onLoadComplete(){
		this.onLoadCallback();
		setCanLoadMore(true);
		footerPanel.setVisibility(View.VISIBLE);
		footerPanel.setHintText(R.string.pull_up_more);
		footerPanel.setAnimImageVisibility(View.GONE);
	}
	
	/**
	 * 
	 * @Description: 没有更多时调用该方法 
	 * @param handler
	 */
	public void onLoadCompleteNoMore(NoMoreHandler handler){
		this.onLoadCallback();
		this.mNoMoreHandler = handler;
		switch (handler) {
		case NO_MORE_LOAD_NOT_SHOW_FOOTER_VIEW:
			if(getAdapter() != null && getFooterViewsCount() > 0 && footerParent != null){
				removeFooterView(footerParent);
			}
			break;
		case NO_MORE_LOAD_SHOW_FOOTER_VIEW:
			if(footerPanel != null){
				footerPanel.setHintText(R.string.no_more);
				footerPanel.setAnimImageVisibility(View.GONE);
				footerPanel.setVisibility(View.VISIBLE);
			}
			break;
		case NO_MORE_LOAD_SHOW_TOAST:
			if(getAdapter() != null && getFooterViewsCount() > 0 && footerParent != null){
				removeFooterView(footerParent);
			}
			break;
		default:
			break;
		}
		setCanLoadMore(false);
	}
	
	private void onLoadCallback(){
		synchronized (lockObject) {
			mCurrentState = LoadingState.LOADING_STATE_FINISH;
		}
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		for (OnScrollListener listener : scrollListenerList) {
			listener.onScroll(view, firstVisibleItem, visibleItemCount, totalItemCount);
		}
	}
	
	public interface OnLoadMoreListener{
		
		/**
		 * 
		 * @Description: 在此方法里加载数据，若耗时过久，请异步处理
		 */
		public void loadMore();
	}
	
	public interface OnSizeChangedListener{
		public void onSizeChanged(int w, int h, int oldw, int oldh);
	}
	
	@TargetApi(16)
	@Override
	protected void onDetachedFromWindow() {
		super.onDetachedFromWindow();
		if(Build.VERSION.SDK_INT >= 16){
			this.getViewTreeObserver().removeOnGlobalLayoutListener(mOnGlobalLayoutListener);
		}else{
			this.getViewTreeObserver().removeGlobalOnLayoutListener(mOnGlobalLayoutListener);
		}
	}
	
	private OnGlobalLayoutListener mOnGlobalLayoutListener = new OnGlobalLayoutListener() {
		
		@Override
		public void onGlobalLayout() {
			if(getAdapter() == null){
				return;
			}
			
			if(mNoMoreHandler == NoMoreHandler.NO_MORE_LOAD_NOT_SHOW_FOOTER_VIEW || mNoMoreHandler == NoMoreHandler.NO_MORE_LOAD_SHOW_TOAST){
				if(getFooterViewsCount() > 0 && footerParent != null){
					footerPanel.setVisibility(View.GONE);
				}
				return;
			}
			
			if(getFirstVisiblePosition() == 0 && getLastVisiblePosition() == getAdapter().getCount() - 1 && !canLoadMore){
				if(getFooterViewsCount() > 0 && footerParent != null){
					footerPanel.setVisibility(View.GONE);
				}
			}
			
		
		}
	};
}
