/**   
* @Title: RegisterActivity.java 
* @Package com.whistle.gamefanr.activity 
* @Description: TODO(用一句话描述该文件做什么) 
* @author xuyingjian@ruijie.com.cn   
* @date 2015年5月28日 上午11:16:00 
*/
package com.chengsi.weightcalc.activity;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPropertyAnimatorCompat;
import android.support.v4.view.ViewPropertyAnimatorListener;
import android.support.v7.internal.view.ViewPropertyAnimatorCompatSet;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Interpolator;
import android.widget.EditText;
import android.widget.TextView;

import com.alibaba.fastjson.TypeReference;
import com.chengsi.weightcalc.R;
import com.chengsi.weightcalc.bean.BaseBean;
import com.chengsi.weightcalc.http.JDHttpClient;
import com.chengsi.weightcalc.http.JDHttpResponseHandler;
import com.chengsi.weightcalc.listener.OnContinuousClickListener;
import com.chengsi.weightcalc.utils.JDUtils;
import com.chengsi.weightcalc.utils.UIUtils;


/**    
 * @Description: 注册界面
 * @date 2015年5月28日 上午11:16:00
 * @version 1.0  
 */
public class RegisterActivity extends BaseActivity {

	private EditText mRegPhoneEt;
	private EditText mValifyCodeEt;
	private EditText mPwdEt;
	
	private TextView mRegBtnTv;
	private TextView mSendValifyBtnTv;
	
	private boolean isSendVarifySuccess = false;
	
	private ViewPropertyAnimatorCompatSet mErrorAnimSet = new ViewPropertyAnimatorCompatSet();

	private DelaySender mDelaySender;

	private boolean isValifySuccess = false;//是否验证短信成功
	private String lastValifyPwd = "";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);
		getSupportActionBar().setTitle(getString(R.string.reg_account));
		
		initViews();
	}

	private void initViews() {

		mRegPhoneEt = (EditText) findViewById(R.id.et_reg_phone);
		mValifyCodeEt = (EditText) findViewById(R.id.et_reg_valify);
		mPwdEt = (EditText) findViewById(R.id.et_reg_pwd);
		
		mRegPhoneEt.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {}
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {}
			
			@Override
			public void afterTextChanged(Editable s) {
				mRegPhoneEt.setTextColor(getResources().getColor(R.color.text_5e));
			}
		});
		
		mValifyCodeEt.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {}
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {}
			
			@Override
			public void afterTextChanged(Editable s) {
				mValifyCodeEt.setTextColor(getResources().getColor(R.color.text_5e));
			}
		});
		
		mPwdEt.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {}
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {}
			
			@Override
			public void afterTextChanged(Editable s) {
				mPwdEt.setTextColor(getResources().getColor(R.color.text_5e));
			}
		});
		
		mRegBtnTv = (TextView) findViewById(R.id.tv_reg_btn);
		mSendValifyBtnTv = (TextView) findViewById(R.id.tv_send_valify);
		
		mSendValifyBtnTv.setOnClickListener(new OnContinuousClickListener() {

			@Override
			public void onContinuousClick(View v) {
				JDUtils.hideIME(RegisterActivity.this);
				if (JDUtils.checkIsPhoneNum(mRegPhoneEt.getText().toString())) {
					showLoadingView();
					mSendValifyBtnTv.setEnabled(false);
					mSendValifyBtnTv.setText(R.string.waiting_for_send_varify);

					JDHttpClient.getInstance().reqSendSms(RegisterActivity.this, mRegPhoneEt.getText().toString(), new JDHttpResponseHandler<String>(new TypeReference<BaseBean<String>>(){}){

						@Override
						public void onRequestCallback(BaseBean<String> result) {
							super.onRequestCallback(result);
							dismissLoadingView();
							if (result.isSuccess()){
								isSendVarifySuccess = true;
								showToast(getString(R.string.get_valify_code_success));
								startCountTime();
							}else{
								mSendValifyBtnTv.setText(getString(R.string.re_send_valify_code));
								mSendValifyBtnTv.setEnabled(true);
								showToast(result.getMessage());
							}
						}
					});
				} else {
					startWobbleAnimation(1, true, false, false);
					showToast(R.string.error_phone_input);
				}
			}
		});
		
		mRegBtnTv.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				startRegister();
			}
		});
	}
	
	
	protected void startRegister() {
		JDUtils.hideIME(this);
		if(!JDUtils.checkIsPhoneNum(mRegPhoneEt.getText().toString())){
			startWobbleAnimation(1, true, false, false);
			showToast(R.string.error_phone_input);
			return;
		}
		if(TextUtils.isEmpty(mValifyCodeEt.getText().toString())){
			startWobbleAnimation(1, false, false, true);
			showToast(R.string.text_reg_varify_hint);
			return;
		}
		if (TextUtils.isEmpty(mPwdEt.getText().toString())) {
			startWobbleAnimation(1, false, false, true);
			showToast(R.string.text_login_pwd_hint);
			return;
		}
		showLoadingView();
		if (isValifySuccess && mValifyCodeEt.getText().toString().equals(lastValifyPwd)){
			submitPwd();
		}else{
			JDHttpClient.getInstance().reqValifySms(this, mRegPhoneEt.getText().toString(), mValifyCodeEt.getText().toString(), new JDHttpResponseHandler<String>(new TypeReference<BaseBean<String>>(){}){
				@Override
				public void onRequestCallback(BaseBean<String> result) {
					super.onRequestCallback(result);
					dismissLoadingView();
					if (result.isSuccess()){
						submitPwd();
					}else{
						showToast(result.getMessage());
					}
				}
			});
		}
	}

	private void submitPwd(){
		showLoadingView();
//		JDHttpClient.getInstance().reqRegister(this, mRegPhoneEt.getText().toString(), mPwdEt.getText().toString(), new JDHttpResponseHandler<UserBean>(new TypeReference<BaseBean<UserBean>>(){}){
//			@Override
//			public void onRequestCallback(BaseBean<UserBean> result) {
//				super.onRequestCallback(result);
//				dismissLoadingView();
//				if (result.isSuccess()){
//					application.userManager.resetUser(result.getData());
//					Intent intent = new Intent(RegisterActivity.this, UploadProfileActivity.class);
//					intent.putExtra("user", result.getData());
//					startActivity(intent);
//					finish();
//				}else{
//					showToast(result.getMessage());
//				}
//			}
//		});
	}

	private void startWobbleAnimation(final int count, final boolean isUsernameError, final boolean isPasswordError, final boolean isValifyError){
		if(count == 1){
			if(isUsernameError){
				mRegPhoneEt.setTextColor(getResources().getColor(R.color.text_err_color));
			}
			if(isValifyError){
				mValifyCodeEt.setTextColor(getResources().getColor(R.color.text_err_color));
			}
			if(isPasswordError){
				mPwdEt.setTextColor(getResources().getColor(R.color.text_err_color));
			}
		}
		int width = UIUtils.convertDpToPixel(8, this);
		if(count == 4){
			return;
		}
		if(isUsernameError){
			ViewPropertyAnimatorCompat anim1 = ViewCompat.animate(mRegPhoneEt).translationX(width - (count / 4));
			mErrorAnimSet.play(anim1);
		}
		if(isPasswordError){
			ViewPropertyAnimatorCompat anim2 = ViewCompat.animate(mPwdEt).translationX(width - (count / 4));
			mErrorAnimSet.play(anim2);
		}
		if(isValifyError){
			ViewPropertyAnimatorCompat anim2 = ViewCompat.animate(mValifyCodeEt).translationX(width - (count / 4));
			mErrorAnimSet.play(anim2);
		}
		mErrorAnimSet.setDuration(100).setInterpolator(new Interpolator() {
			
			@Override
			public float getInterpolation(float input) {
				return (float) Math.sin(input * 2 * Math.PI);
			}
		});
		mErrorAnimSet.setListener(new ViewPropertyAnimatorListener() {
			
			@Override
			public void onAnimationStart(View arg0) {
				
			}
			
			@Override
			public void onAnimationEnd(View arg0) {
				startWobbleAnimation(count + 1, isUsernameError, isPasswordError, isValifyError);
			}
			
			@Override
			public void onAnimationCancel(View arg0) {
				
			}
		}).start();
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		stopCountTime();
	}
	
	private void startCountTime(){
		mSendValifyBtnTv.setEnabled(false);
		if(mDelaySender != null){
			mDelaySender.cancel();
			mDelaySender = null;
		}
		mDelaySender = new DelaySender(60 * 1000, 1000);
		mDelaySender.start();
	}
	
	private void stopCountTime(){
		if(mDelaySender != null){
			mDelaySender.cancel();
			mDelaySender = null;
		}
	}
	
	private class DelaySender extends CountDownTimer{

		/**
		 * @param millisInFuture
		 * @param countDownInterval
		 */
		public DelaySender(long millisInFuture, long countDownInterval) {
			super(millisInFuture, countDownInterval);
		}

		@Override
		public void onTick(long millisUntilFinished) {
			mSendValifyBtnTv.setText(getString(R.string.re_send_valify_code_delay, millisUntilFinished / 1000));
		}

		@Override
		public void onFinish() {
			mSendValifyBtnTv.setText(getString(R.string.re_send_valify_code));
			mSendValifyBtnTv.setEnabled(true);
		}
	}
}
