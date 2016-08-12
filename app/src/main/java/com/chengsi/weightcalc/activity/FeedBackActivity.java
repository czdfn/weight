package com.chengsi.weightcalc.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.chengsi.weightcalc.R;
import com.chengsi.weightcalc.bean.UserBean;
import com.umeng.fb.FeedbackAgent;
import com.umeng.fb.model.Conversation;
import com.umeng.fb.model.UserInfo;

import java.util.HashMap;
import java.util.Map;

import butterknife.InjectView;

public class FeedBackActivity extends BaseActivity {
	private EditText numberEt, contentEt;
	private FeedbackAgent mAgent;
	private Conversation mComversation;
	private UserBean userInfo;

	@InjectView(R.id.btn_sendMsg)
	Button msgBtn;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.feed_back_activity);
		setMyTitle(R.string.title_feedback);
		//numberEt = (EditText) findViewById(R.id.number_et);
		contentEt = (EditText) findViewById(R.id.content_et);
		msgBtn = (Button)findViewById(R.id.btn_sendMsg);
		mAgent = new FeedbackAgent(this);
		mComversation = mAgent.getDefaultConversation();
		//发送一遍
		mComversation.sync(null);
//		userInfo = application.userManager.getUserBean();
		if(userInfo != null) {
			initView();
		}
//		LinearLayout.LayoutParams params = (LinearLayout.LayoutParams)contentEt.getLayoutParams();
//		params.setMargins(0, 0, 0, 200);
//		contentEt.setLayoutParams(params);
	}

	private void initView() {
		String nickName = userInfo.getNickname();
		UserInfo info = mAgent.getUserInfo();
		if (info == null) {
			info = new UserInfo();
		}
		Map<String, String> contact = info.getContact();
		if (contact == null) {
			contact = new HashMap<String, String>();
		}
		contact.put("phone", userInfo.getPhone());
		if(TextUtils.isEmpty(nickName)) {
			contact.remove("nick_name");
		}
		else {
			contact.put("nick_name", nickName);
		}
		info.setContact(contact);
		mAgent.setUserInfo(info);
		application.getWorkHandler().post(new Runnable() {
			@Override
			public void run() {
				mAgent.updateUserInfo();
			}
		});

		//numberEt.setText(userInfo.getPhone());

	}

	public void doClick(View view){
		switch(view.getId()){
			case R.id.btn_sendMsg:
				sendFeedBack();
				break;
		}
	}

	void sendFeedBack() {
		String content = contentEt.getText().toString();
//		String number = numberEt.getText().toString();
		String number = userInfo.getPhone();
		if(TextUtils.isEmpty(content)) {
			showToast("请输入反馈内容");
			return;
		}
		UserInfo info = mAgent.getUserInfo();
		if (info == null) {
			info = new UserInfo();
		}
		Map<String, String> contact = info.getContact();
		if (contact == null) {
			contact = new HashMap<String, String>();
		}

		if(TextUtils.isEmpty(number)) {
			contact.remove("contact");
		}
		else {
			contact.put("contact", number);
		}
		info.setContact(contact);
		mAgent.setUserInfo(info);
		application.getWorkHandler().post(new Runnable() {
			@Override
			public void run() {
				mAgent.updateUserInfo();
			}
		});
		if (!TextUtils.isEmpty(content)) {
			mComversation.addUserReply(content);//添加到会话列表
			mComversation.sync(null);
		}
		showToast(R.string.feedback_send_succeed);
		new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {

			@Override
			public void run() {
				finish();
			}
		}, 500);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_feedback, menu);
		return true;
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		return super.onPrepareOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
//		if (item.getItemId() == R.id.action_send) {
//			sendFeedBack();
//
//			return true;
//
//		}else {
		return super.onOptionsItemSelected(item);
//		}
	}





}
