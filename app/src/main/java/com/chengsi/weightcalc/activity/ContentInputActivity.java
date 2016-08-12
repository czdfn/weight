package com.chengsi.weightcalc.activity;


import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.chengsi.weightcalc.R;
import com.chengsi.weightcalc.listener.OnContinuousClickListener;
import com.chengsi.weightcalc.utils.JDUtils;
import com.chengsi.weightcalc.widget.PreferenceRightDetailView;

public class ContentInputActivity extends BaseActivity {
	
	private static final String TAG = ContentInputActivity.class.getName();
	public static final String KEY_TEXT_CONTENT = "KEY_TEXT_CONTENT";

//	private Button mActionBtn = null;
	public static final int CAN_RETURN_ALL = 0;
	public static final int CAN_NOT_RETURN_BLANK = 1;
	public static final int CAN_NOT_RETURN_NULL = 2;
	public static final int CAN_NOT_BLANK_NULL = 3;
	public static final int CAN_RETURN_EMAIL = 4;
	public static final int CAN_RETURN_CELL_PHONE = 5;
	public static final int CAN_RETURN_LAND_LINE = 6;
	public static final int CAN_RETURN_INT = 7;

	public static final String KEY_TEXT_STYLE = "KEY_TEXT_STYLE";
	public static final String KEY_HINT_TEXT = "HintText";
//	public static final String KEY_INIT_TEXT = "InitText";

	public static final String KEY_MAX_TEXT_LENGTH = "MaxTextLength";
	public static final String KEY_ACTION_NAME = "ActionName";
	public static final String KEY_TITLE = "Title";
	public static final String KEY_CONTENT = "Content";
	public static final String KEY_TIPS = "Tips";
	public static final String KEY_CAN_COMMIT_NULL = "canCommitNull";
	public static final String KEY_IS_SINGLE_LINE = "isSingleLine";
	public static final String KEY_CAN_TURN_LINE = "canTurnLine";
	
	private EditText mInputEt = null;
	private TextView mNumberTipTv = null, mTipsView = null;
	private ImageView ivClearText;
	private View mTipsParent;
	private PreferenceRightDetailView detailView;

	private String hintText, tipsText;
//	private String initText;
	private String btnText;
	private int maxTextLength = 12;
	private int returnKey = 0;
	public final int MAX_INPUT_NUMBER_DEFAULT = 999;
	
	private boolean isSingleLine = false;
	private boolean canTurnLine = true;//是否可以输入回车换行
	
	private int style;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if(isInAbnormalState(savedInstanceState)) {
			return;
		}
		final Intent intent = this.getIntent();
		returnKey = intent.getIntExtra(KEY_CAN_COMMIT_NULL, 0);
		Intent data = new Intent();
		data.putExtras(intent);
		setResult(RESULT_CANCELED, data);
		style = intent.getIntExtra(KEY_TEXT_STYLE, 0);
		if(style == 0){
			setContentView(R.layout.content_input_one_line_layout);
			mNumberTipTv = (TextView) findViewById(R.id.NumberTip);
			ivClearText = (ImageView) findViewById(R.id.input_clear_iv);
			ivClearText.setOnClickListener(new OnContinuousClickListener() {
				@Override
				public void onContinuousClick(View v) {
					mInputEt.setText("");
				}
			});
		}else if(style == 1){
			setContentView(R.layout.content_input_layout);
			mNumberTipTv = (TextView) findViewById(R.id.NumberTip);
			mNumberTipTv.setVisibility(View.GONE);
		}else if(style == 2){
			setContentView(R.layout.content_input_select_layout);
			detailView = (PreferenceRightDetailView) findViewById(R.id.inputEt);
			detailView.setTitle(intent.getStringExtra(KEY_TITLE));
			setMyTitle(detailView.getTitle().toString());
			mTipsView = (TextView) findViewById(R.id.tips);
			mTipsView.setText(intent.getStringExtra(KEY_TIPS));
			
			JDUtils.hideIME(this);
			detailView.setContent(intent.getStringExtra(KEY_CONTENT));
			detailView.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					int year = 1993, month = 1, day = 1;
					try{
						String date = detailView.getContent().toString();
						if(!TextUtils.isEmpty(date)){
							String[] args = date.split("-");
							if(args.length == 3){
								year = Integer.parseInt(args[0]);
								month = Integer.parseInt(args[1]);
								day = Integer.parseInt(args[2]);
							}
						}
					}catch(Exception e){
						e.printStackTrace();
					}
//					AnanDatePickerPopupWindow window = new AnanDatePickerPopupWindow(ContentInputActivity.this, year, month, day);
//					window.setOnDateSelectedListener(new OnDateSelectedListenr() {
//
//						@Override
//						public void onDateSelected(int year, int month, int day) {
//							detailView.setContent(year + "-" + String.format("%02d", month) + "-" + String.format("%02d", day));
//						}
//					});
				}
			});	
			return;
		}
		
		mInputEt = (EditText) findViewById(R.id.inputEt);
		mTipsParent = findViewById(R.id.tips_parent);
		mTipsView = (TextView) findViewById(R.id.tips);
		Bundle bundle = intent.getExtras();
		if (bundle != null) {
			isSingleLine = bundle.getBoolean(KEY_IS_SINGLE_LINE, true);
			canTurnLine = bundle.getBoolean(KEY_CAN_TURN_LINE,false);
			mInputEt.setSingleLine(isSingleLine);//设置为单行显示[不能换行]
			
			hintText = bundle.getString(KEY_HINT_TEXT);
			tipsText = bundle.getString(KEY_TIPS);
			if(!TextUtils.isEmpty(tipsText)){
				mTipsView.setText(tipsText);
				if(mTipsParent != null){
					mTipsParent.setVisibility(View.VISIBLE);
				}
			}else{
				if(mTipsParent != null){
					mTipsParent.setVisibility(View.GONE);
				}
			}
			String inputTitle = bundle.getString(KEY_TITLE);
			if(inputTitle != null) {
				setMyTitle(inputTitle);
			}
			maxTextLength = bundle.getInt(KEY_MAX_TEXT_LENGTH,
					MAX_INPUT_NUMBER_DEFAULT);
			mInputEt.setFilters(new InputFilter[]{
					new InputFilter.LengthFilter(maxTextLength)
			});
			mInputEt.setHint(hintText);
			String content = bundle.getString(KEY_CONTENT);

			if(!canTurnLine){
				/**
				 * 禁掉输入的换行
				 */
				mInputEt.setOnEditorActionListener(new EditText.OnEditorActionListener() {				
					@Override
					public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
						if(actionId==EditorInfo.IME_ACTION_DONE || (actionId==EditorInfo.IME_ACTION_UNSPECIFIED && event!=null && event.getKeyCode()==KeyEvent.KEYCODE_ENTER)) {
							return true;
						}
						return false;
					}
				});
			}
			
			if(!TextUtils.isEmpty(content)) {
				if (content.length() > maxTextLength) {
					content = content.substring(0, maxTextLength);
				}
				mInputEt.setText(content);
				setNumberTipText();
				mInputEt.setSelection(content.length());

			}else{
			}
			mNumberTipTv.setText(String.valueOf(maxTextLength - mInputEt.getText().length()));
		}

		mInputEt.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {

			}

			@Override
			public void afterTextChanged(Editable s) {
				mNumberTipTv.setText(String.valueOf(maxTextLength - mInputEt.getText().length()));
				if (mInputEt.getText().length() > 0){
					ivClearText.setVisibility(View.VISIBLE);
				}else{
					ivClearText.setVisibility(View.GONE);
				}
			}
		});
		
		JDUtils.showIME(this, mInputEt);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_content_input, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()){
			case R.id.action_save:
				save();
				break;
		}
		return super.onOptionsItemSelected(item);
	}

	protected void save() {
		String input = "";
		if(style != 3 && style != 2){
			if(input != null){
				input = mInputEt.getText().toString();
			}
			if(Integer.parseInt(mNumberTipTv.getText().toString()) < 0){
				Toast.makeText(ContentInputActivity.this, R.string.content_too_long, Toast.LENGTH_SHORT).show();
				return;
			}
		}else{
			if(detailView != null){
				input = detailView.getContent().toString();
			}
		}
		switch (returnKey) {
			case CAN_RETURN_ALL:
				break;
			case CAN_NOT_RETURN_BLANK:
				if (!TextUtils.isEmpty(input)) {
					if (input.trim().length() == 0) {
						Toast.makeText(ContentInputActivity.this, R.string.content_input_blank, Toast.LENGTH_SHORT).show();
						return;
					}
				}
				break;
			case CAN_NOT_RETURN_NULL:
				if (input.length() == 0) {
					Toast.makeText(ContentInputActivity.this, R.string.content_input_null, Toast.LENGTH_SHORT).show();
					return;
				}
				break;
			case CAN_NOT_BLANK_NULL:
				if (input.length() == 0) {
					Toast.makeText(ContentInputActivity.this, R.string.content_input_null, Toast.LENGTH_SHORT).show();
					return;
				}
				if (input.trim().length() == 0) {
					Toast.makeText(ContentInputActivity.this, R.string.content_input_blank, Toast.LENGTH_SHORT).show();
					return;
				}
				break;
			case CAN_RETURN_EMAIL:
				if (!JDUtils.checkEmail(input)) {
					Toast.makeText(ContentInputActivity.this, R.string.content_input_email, Toast.LENGTH_SHORT).show();
					return;
				}
				break;
			case CAN_RETURN_CELL_PHONE:
				if (!JDUtils.checkPhoneNum(input)) {
					Toast.makeText(ContentInputActivity.this, R.string.content_input_cell_phone, Toast.LENGTH_SHORT).show();
					return;
				}
				break;
			case CAN_RETURN_LAND_LINE:
				if (!JDUtils.checkLandLine(input)) {
					Toast.makeText(ContentInputActivity.this, R.string.content_input_land_line, Toast.LENGTH_SHORT).show();
					return;
				}
				break;
			case CAN_RETURN_INT:
				if (!TextUtils.isDigitsOnly(input)) {
					Toast.makeText(ContentInputActivity.this, "格式不正确！", Toast.LENGTH_SHORT).show();
					return;
				}
				break;
			default:
				break;
		}
		Intent result = new Intent();
		result.putExtras(getIntent());
		result.putExtra(KEY_TEXT_CONTENT, input.trim());

		setResult(RESULT_OK, result);
		finish();
	}
	




	private void setNumberTipText() {
		Object obj = mInputEt.getText();
		if (obj != null) {
			String text = obj.toString();
			if (text != null) {
				int displayNum = maxTextLength - text.length();
				mNumberTipTv.setText(displayNum+"");

				if (mNumberTipTv != null && text != null) {
					mNumberTipTv.setEnabled(text.length() <= 0 ? false : true);
				}
			}
		}
	}

	public void showClearMessage(View view) {
		this.showDialog(0);
	}

	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		boolean retBool = true;
		switch (keyCode) {
		case KeyEvent.KEYCODE_BACK:
			finish ();
			break;
		}
		return retBool;
	}
}
