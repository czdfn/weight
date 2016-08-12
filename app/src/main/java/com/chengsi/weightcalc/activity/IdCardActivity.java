package com.chengsi.weightcalc.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;

import com.chengsi.weightcalc.R;
import com.chengsi.weightcalc.utils.JDUtils;
import com.chengsi.weightcalc.widget.JDLoadingView;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class IdCardActivity extends BaseActivity {

    @InjectView(R.id.et_idcard)
    EditText etIdCard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_id_card);
        ButterKnife.inject(this);
//        etIdCard.setText(application.userManager.getUserBean().getIdNo());
        etIdCard.setSelection(etIdCard.getText().length());
    }

    @OnClick(R.id.btn_save)
    void save(){
        final String idcard = etIdCard.getText().toString();
        if (TextUtils.isEmpty(idcard)){
            showToast("身份证号不能为空！");
            return;
        }
        if (!JDUtils.validateIdCard(idcard)){
            showToast("身份证号码格式不正确！");
            return;
        }
        setLoadingViewState(JDLoadingView.STATE_LOADING);
//        final UserBean userBean = application.userManager.getUserBean();
//        JDHttpClient.getInstance().reqModifyInfo(this, userBean.getHeadImg(), userBean.getNickname(), userBean.getRealname(), userBean.getSex(), idcard, new JDHttpResponseHandler<String>(new TypeReference<BaseBean<String>>(){}){
//            @Override
//            public void onRequestCallback(BaseBean<String> result) {
//                super.onRequestCallback(result);
//                dismissLoadingView();
//                if (result.isSuccess()){
//                    userBean.setIdNo(idcard);
//                    application.userManager.resetUser(userBean);
//                    showToast("修改成功！");
//                    finish();
//                }else{
//                    showToast(result.getMessage());
//                }
//            }
//        });
    }
}
