package com.chengsi.weightcalc.widget;


import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chengsi.weightcalc.R;


public class JDLoadingView extends RelativeLayout {
	
	public static final int STATE_EMPTY = 0, STATE_LOADING = 1,
			STATE_FAILED = 2;

	private int mState = STATE_LOADING;
	private OnManageClickListener mListener = null;
	private View mEmptyView = null;
	private RelativeLayout mLoadingPanel = null;
	private View mFailedView;

	private ImageView ivEmpty;
	private TextView tvEmptyTips;

	public JDLoadingView(Context context) {
		this(context, null);

	}

	public JDLoadingView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public JDLoadingView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		LayoutInflater.from(context).inflate(R.layout.progress_view, this);
		mEmptyView = this.findViewById(R.id.empty_panel);
		mFailedView = this.findViewById(R.id.failed_panel);
		mLoadingPanel = (RelativeLayout) this.findViewById(R.id.loading_panel);
		ivEmpty = (ImageView) this.findViewById(R.id.empty_iv);
		tvEmptyTips = (TextView) this.findViewById(R.id.tv_empty_tips);
		this.setClickable(true);
		setState(STATE_LOADING);
		
		ImageButton retryBtn = (ImageButton) findViewById(R.id.failed_btn);
		mEmptyView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {
				if (mListener == null) {
					return;
				}
				mListener.onEmptyClick(view);
			}
		});
		mFailedView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {
				if (mListener == null) {
					return;
				}
				mListener.onFailedClick(view);
			}
		});
		mLoadingPanel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {
				if (mListener == null) {
					return;
				}
				mListener.onLoadingClick(view);
			}
		});
	}

	public boolean isShowing() {
		if (mState == STATE_LOADING) {
			return true;
		}
		return false;
	}

	private String mStrLoading;

	public void setTextState(int state, String str) {
		setState(state);

		switch (state) {
		case STATE_EMPTY:
			mStrLoading = str;
			if (TextUtils.isEmpty(str)) {
				ivEmpty.setImageResource(R.drawable.empty_tips);
				tvEmptyTips.setVisibility(GONE);
			}else{
				ivEmpty.setImageResource(R.drawable.img_empty);
				tvEmptyTips.setText(str);
				tvEmptyTips.setVisibility(VISIBLE);
			}
			break;
		case STATE_LOADING:
			break;
		case STATE_FAILED:
			break;
		}
	}

	public void setState(int state) {
		mState = state;
		switch (mState) {
		case STATE_EMPTY:
			mEmptyView.setVisibility(View.VISIBLE);
			mLoadingPanel.setVisibility(View.GONE);
			mFailedView.setVisibility(View.GONE);
			ivEmpty.setImageResource(R.drawable.empty_tips);
			tvEmptyTips.setVisibility(GONE);
			break;
		case STATE_LOADING:
			mEmptyView.setVisibility(View.GONE);
			mFailedView.setVisibility(View.GONE);
			mLoadingPanel.setVisibility(View.VISIBLE);
			ImageView img = (ImageView) mLoadingPanel
					.findViewById(R.id.loading_img);
			final AnimationDrawable animationDrawable = (AnimationDrawable) img
					.getDrawable();
			if (animationDrawable != null){
				img.post(new Runnable() {
					@Override
					public void run() {
						animationDrawable.start();
					}
				});
			}
			break;
		case STATE_FAILED:
			mEmptyView.setVisibility(View.GONE);
			mLoadingPanel.setVisibility(View.GONE);
			mFailedView.setVisibility(View.VISIBLE);
			break;
		}
	}

	public void setOnManageListener(OnManageClickListener listener) {
		mListener = listener;
	}

	public static interface OnManageClickListener {
		public void onEmptyClick(View view);

		public void onLoadingClick(View view);

		public void onFailedClick(View view);
	}

}
