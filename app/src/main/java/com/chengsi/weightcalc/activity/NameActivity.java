package com.chengsi.weightcalc.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;

import com.chengsi.weightcalc.R;
import com.chengsi.weightcalc.widget.JDLoadingView;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class NameActivity extends BaseActivity {

    public static final String KEY_EDIT_INPUT_TYPE = "KEY_EDIT_INPUT_TYPE";

    public static final int KEY_EDIT_INPUT_NICKNAME = 0;
    public static final int KEY_EDIT_INPUT_REALNAME = 1;

    @InjectView(R.id.et_name)
    EditText etName;

    private int type;
    private String title = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_name);
        ButterKnife.inject(this);
        type = getIntent().getIntExtra(KEY_EDIT_INPUT_TYPE, KEY_EDIT_INPUT_NICKNAME);
//        if (type == KEY_EDIT_INPUT_NICKNAME){
//            title = "昵称";
//            etName.setText(application.userManager.getUserBean().getNickname());
//        }else if(type == KEY_EDIT_INPUT_REALNAME){
//            etName.setText(application.userManager.getUserBean().getRealname());
//            title = "真实姓名";
//        }
        setMyTitle(title);
        etName.setSelection(etName.getText().length());
        etName.setHint("请输入" + title);
    }

    @OnClick(R.id.btn_save)
    void save() {
        final String name = etName.getText().toString();
        if (TextUtils.isEmpty(name)) {
            showToast(title + "不能为空！");
            return;
        }

        setLoadingViewState(JDLoadingView.STATE_LOADING);
//        final UserBean userBean = application.userManager.getUserBean();
//        String nickName = null, realName = null;
//        if (type == KEY_EDIT_INPUT_NICKNAME){
//            nickName = etName.getText().toString();
//            realName = application.userManager.getUserBean().getRealname();
//        }else if(type == KEY_EDIT_INPUT_REALNAME){
//            realName = etName.getText().toString();
//            nickName = application.userManager.getUserBean().getNickname();
//        }
//        JDHttpClient.getInstance().reqModifyInfo(this, userBean.getHeadImg(), nickName, realName, userBean.getSex(), userBean.getIdNo(), new JDHttpResponseHandler<String>(new TypeReference<BaseBean<String>>() {
//        }) {
//            @Override
//            public void onRequestCallback(BaseBean<String> result) {
//                super.onRequestCallback(result);
//                dismissLoadingView();
//                if (result.isSuccess()) {
//                    if (type == KEY_EDIT_INPUT_NICKNAME){
//                        userBean.setNickname(etName.getText().toString());
//                    }else if(type == KEY_EDIT_INPUT_REALNAME){
//                        userBean.setRealname(etName.getText().toString());
//                    }
//                    application.userManager.resetUser(userBean);
//                    showToast("修改成功！");
//                    finish();
//                } else {
//                    showToast(result.getMessage());
//                }
//            }
//        });
    }
}
