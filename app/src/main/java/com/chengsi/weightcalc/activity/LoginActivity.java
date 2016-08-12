package com.chengsi.weightcalc.activity;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPropertyAnimatorCompat;
import android.support.v4.view.ViewPropertyAnimatorListener;
import android.support.v7.internal.view.ViewPropertyAnimatorCompatSet;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.animation.Interpolator;
import android.widget.EditText;
import android.widget.TextView;

import com.chengsi.weightcalc.db.DBHelper;
import com.chengsi.weightcalc.R;
import com.chengsi.weightcalc.listener.OnContinuousClickListener;
//import com.chengsi.pregnancy.manager.UserManager;
import com.chengsi.weightcalc.utils.JDUtils;
import com.chengsi.weightcalc.utils.UIUtils;

/**
 * @author xuyingjian@ruijie.com.cn
 * @version 1.0
 * @Description: 登录界面
 * @date 2015年5月27日 上午11:45:19
 */
public class LoginActivity extends BaseActivity{
    public static String TAG = LoginActivity.class.getSimpleName();

    private static final int REQUEST_CODE_START_REGISTER = 10001;// 完美授权成功

    private EditText mUserNameEt;
    private EditText mPasswordEt;
    private TextView mLoginBtnTv;
    public static String DB_NAME = "weightdatabase.db";
    public static String MEASURE_TABLE = "weightcalc";
    public static int DB_VERSION = 1;
    public DBHelper dbHelper = new DBHelper(this, DB_NAME, null, DB_VERSION);


    private ViewPropertyAnimatorCompatSet mErrorAnimSet = new ViewPropertyAnimatorCompatSet();

    private OnContinuousClickListener mClickListener = new OnContinuousClickListener(500) {

        @Override
        public void onContinuousClick(View v) {
            JDUtils.hideIME(LoginActivity.this);
            switch (v.getId()) {
                case R.id.tv_login_btn:

                    onLoginBtnClick();
                    break;
            }

        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
//        application.userManager.registerOnUserChanged(this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cr = db.query("user", new String[]{"username"}, "username like ?", new String[]{"admin"}, null, null, null);
        ContentValues cv1 = new ContentValues();
        cv1.put("username", "admin");
        cv1.put("password", "123456");
        db.insert("user", "", cv1);
		while(cr.moveToNext()){
			if(!cr.getString(1).equals("admin")){
				cv1 = new ContentValues();
				cv1.put("username", "admin");
				cv1.put("password", "123456");
				db.insert("user", "", cv1);
			}
		}
        initViews();

    }

    protected void onLoginBtnClick() {
        if (TextUtils.isEmpty(mUserNameEt.getText().toString())) {
            startWobbleAnimation(1, true, false);
            showToast("请输入用户名");
            return;
        }
        if (TextUtils.isEmpty(mPasswordEt.getText().toString())) {
            startWobbleAnimation(1, false, true);
            showToast(R.string.text_login_pwd_hint);
            return;
        }
//        showLoadingView();
//        JDHttpClient.getInstance().reqLogin(this, mUserNameEt.getText().toString(), mPasswordEt.getText().toString(), new JDHttpResponseHandler<UserBean>(new TypeReference<BaseBean<UserBean>>() {
//        }) {
//            @Override
//            public void onRequestCallback(BaseBean<UserBean> result) {
//                super.onRequestCallback(result);
//                dismissLoadingView();
//                if (result.isSuccess()) {
//                    showToast(result.getMessage());
//                    if (TextUtils.isEmpty(result.getData().getNickname())) {
////                        application.userManager.resetUser(result.getData());
//                        Intent intent = new Intent(LoginActivity.this, UploadProfileActivity.class);
//                        intent.putExtra("user", result.getData());
//                        startActivity(intent);
//                    } else {
////                        application.userManager.resetUser(result.getData());
//                        startActivity(new Intent(LoginActivity.this, WelcomeActivity.class));
//                    }
//                    finish();
//                } else {
//
//                    mUserNameEt.setTextColor(getResources().getColor(R.color.text_err_color));
//                    mPasswordEt.setTextColor(getResources().getColor(R.color.text_err_color));
//                    showToast(result.getMessage());
//                }
//            }
//        });
    }

    private void initViews() {
        hideToolbar();

        mUserNameEt = (EditText) findViewById(R.id.et_login_phone);
        mPasswordEt = (EditText) findViewById(R.id.et_login_pwd);

        mLoginBtnTv = (TextView) findViewById(R.id.tv_login_btn);

        mLoginBtnTv.setOnClickListener(mClickListener);
        mUserNameEt.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                mUserNameEt.setTextColor(getResources().getColor(R.color.text_5e));
            }
        });
        mPasswordEt.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                mPasswordEt.setTextColor(getResources().getColor(R.color.text_5e));
            }
        });
    }

    private void startWobbleAnimation(final int count, final boolean isUsernameError, final boolean isPasswordError) {
        int width = UIUtils.convertDpToPixel(8, this);
        if (count == 1) {
            if (isUsernameError) {
                mUserNameEt.setTextColor(getResources().getColor(R.color.text_err_color));
            }
            if (isPasswordError) {
                mPasswordEt.setTextColor(getResources().getColor(R.color.text_err_color));
            }
        }
        if (count == 4) {
            return;
        }
        if (isUsernameError) {
            ViewPropertyAnimatorCompat anim1 = ViewCompat.animate(mUserNameEt).translationX(width - (count / 4));
            mErrorAnimSet.play(anim1);
        }
        if (isPasswordError) {
            ViewPropertyAnimatorCompat anim2 = ViewCompat.animate(mPasswordEt).translationX(width - (count / 4));
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
                startWobbleAnimation(count + 1, isUsernameError, isPasswordError);
            }

            @Override
            public void onAnimationCancel(View arg0) {

            }
        }).start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        application.userManager.unRegisterOnUserChanged(this);
    }

//    @Override
//    public void onUserChanged(UserBean userBean) {
//    }
}
