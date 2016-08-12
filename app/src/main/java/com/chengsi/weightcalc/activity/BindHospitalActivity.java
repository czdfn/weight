package com.chengsi.weightcalc.activity;

import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.alibaba.fastjson.TypeReference;
import com.chengsi.weightcalc.R;
import com.chengsi.weightcalc.bean.BaseBean;
import com.chengsi.weightcalc.bean.HospitalBean;
import com.chengsi.weightcalc.constants.StorageConstants;
import com.chengsi.weightcalc.http.JDHttpClient;
import com.chengsi.weightcalc.http.JDHttpResponseHandler;
import com.chengsi.weightcalc.listener.OnContinuousClickListener;
import com.chengsi.weightcalc.storage.JDStorage;
import com.chengsi.weightcalc.utils.JDUtils;
import com.chengsi.weightcalc.widget.JDLoadingView;
import com.chengsi.weightcalc.widget.PreferenceRightDetailView;
import com.chengsi.weightcalc.widget.dialog.FanrAlertDialog;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class BindHospitalActivity extends BaseActivity {
    private static final int REQUEST_CODE_HOSPITAL = 0x0001;
    private static final int REQUEST_CODE_IDCARD = 0x0002;
    private static final int REQUEST_CODE_MECARD = 0x0003;

    @InjectView(R.id.item_hospital)
    PreferenceRightDetailView itemHospital;
    @InjectView(R.id.item_id_card)
    PreferenceRightDetailView itemIdCard;
    @InjectView(R.id.item_medCard)
    PreferenceRightDetailView itemMeCard;
    @InjectView(R.id.btn_bind)
    Button bindButton;
    @InjectView(R.id.btn_unbind)
    Button unbindButton;
    @InjectView(R.id.btn_back2_home)
    Button btnBack;

    private String idCard;

    private HospitalBean hospitalBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bind_hospital);
        ButterKnife.inject(this);
        setMyTitle("用户中心");
        initViews();
    }

    private void initViews(){
//        if (application.userManager.getUserBean().getHospital() != null) {
//            hospitalBean = application.userManager.getUserBean().getHospital();
            itemHospital.setContent("中山一院生殖中心");
//        }

//        if (!TextUtils.isEmpty(application.userManager.getUserBean().getIdNo())) {
//            idCard = application.userManager.getUserBean().getIdNo();
//            itemIdCard.setContent(idCard);
//        }else{
//            itemIdCard.setContent(JDStorage.getInstance().getStringValue(StorageConstants.KEY_ID_CARD, null));
//        }
//
//        if (application.userManager.getUserBean().getHospital() != null){
//            itemMeCard.setContent(application.userManager.getUserBean().getVisitCard());
//        }
//        else{
//            itemMeCard.setContent(JDStorage.getInstance().getStringValue(StorageConstants.KEY_MED_CARD_NO, null));
//        }

        itemHospital.setAccessImageVisible(View.GONE);
        itemHospital.setEnabled(false);
        if(TextUtils.isEmpty(itemMeCard.getContent())){
            unbindButton.setVisibility(View.GONE);
            bindButton.setVisibility(View.VISIBLE);
            itemIdCard.setAccessImageVisible(View.VISIBLE);
            itemMeCard.setAccessImageVisible(View.VISIBLE);
            itemIdCard.setEnabled(true);
            itemMeCard.setEnabled(true);
            btnBack.setVisibility(View.GONE);
        }else{
            bindButton.setVisibility(View.GONE);
            unbindButton.setVisibility(View.VISIBLE);
            btnBack.setVisibility(View.VISIBLE);
            itemIdCard.setAccessImageVisible(View.GONE);
            itemMeCard.setAccessImageVisible(View.GONE);
            itemIdCard.setEnabled(false);
            itemMeCard.setEnabled(false);
        }
//        itemHospital.setOnClickListener(new OnContinuousClickListener() {
//            @Override
//            public void onContinuousClick(View v) {
//                Intent intent = new Intent(BindHospitalActivity.this, HospitalListActivity.class);
//                intent.putExtra(HospitalListActivity.KEY_SELECT_HOSPITAL, true);
//                startActivityForResult(intent, REQUEST_CODE_HOSPITAL);
//            }
//        });
        itemIdCard.setOnClickListener(new OnContinuousClickListener() {
            @Override
            public void onContinuousClick(View v) {
                JDUtils.startContentInputActivity(BindHospitalActivity.this, "身份证号码", getString(R.string.hint_idcard_input), 18, itemIdCard.getContent().toString(), REQUEST_CODE_IDCARD, ContentInputActivity.CAN_NOT_RETURN_BLANK, new OnResultListener());

            }
        });
        itemMeCard.setOnClickListener(new OnContinuousClickListener() {
            @Override
            public void onContinuousClick(View v) {
                JDUtils.startContentInputActivity(BindHospitalActivity.this, itemMeCard.getTitle().toString(), getString(R.string.hint_medcard_input), 20, itemMeCard.getContent().toString(), REQUEST_CODE_MECARD, ContentInputActivity.CAN_NOT_BLANK_NULL, new OnResultListener());
            }
        });
    }

    @OnClick(R.id.btn_unbind)
    void unbindHospital(){
        final FanrAlertDialog dialog = FanrAlertDialog.getInstance();
        dialog.showAlertContent(getSupportFragmentManager(), "确定要解除绑定么？", new OnContinuousClickListener() {
            @Override
            public void onContinuousClick(View v) {
                dialog.dismiss();
                unBindHospital();
            }
        });
    }

    private void unBindHospital() {
        setLoadingViewState(JDLoadingView.STATE_LOADING);
//        JDHttpClient.getInstance().unBindHospital(this, 17L,application.userManager.getUserBean().getIdNo(),application.userManager.getUserBean().getVisitCard(), new JDHttpResponseHandler<String>(new TypeReference<BaseBean<String>>() {
//        }) {
//            @Override
//            public void onRequestCallback(BaseBean<String> result) {
//                super.onRequestCallback(result);
//                dismissLoadingView();
//                if (result.isSuccess()) {
//                    showToast("操作成功");
//                    application.userManager.getUserBean().setVisitCard(null);
//                    application.userManager.getUserBean().setHospital(null);
//                    application.userManager.resetUser();
//                    itemMeCard.setContent("");
//                    itemHospital.setContent("");
//                    initViews();
//                } else {
//                    showToast(result.getMessage());
//                }
//            }
//        });
    }

    @OnClick(R.id.btn_back2_home)
    void back2Home() {

        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NO_HISTORY);
        startActivity(intent);
    }

    @OnClick(R.id.btn_bind)
    void bindHospital() {

//        if (hospitalBean == null){
//            showToast("请选择您要绑定的生殖中心！");
//            return;
//        }
        if (TextUtils.isEmpty(idCard)){
            showToast("请输入您的身份证号！");
            return;
        }
        if (TextUtils.isEmpty(itemMeCard.getContent())){
            showToast("请输入您的就诊卡号！");
            return;
        }

        setLoadingViewState(JDLoadingView.STATE_LOADING);
        JDHttpClient.getInstance().reqBindHospital(this, 17L, itemMeCard.getContent().toString(), idCard, new JDHttpResponseHandler<String>(new TypeReference<BaseBean<String>>() {
        }) {

            @Override
            public void onRequestCallback(BaseBean<String> result) {
                Log.i("result",""+result);
                super.onRequestCallback(result);
                dismissLoadingView();
                if (result.isSuccess()) {
                    showToast("绑定成功");
//                    application.userManager.getUserBean().setIdNo(idCard);
//                    application.userManager.getUserBean().setVisitCard(itemMeCard.getContent().toString());
//                    application.userManager.getUserBean().setHospital(hospitalBean);
//                    application.userManager.resetUser();
                    itemHospital.setAccessImageVisible(View.GONE);
                    itemIdCard.setAccessImageVisible(View.GONE);
                    itemMeCard.setAccessImageVisible(View.GONE);
                    unbindButton.setVisibility(View.VISIBLE);
                    bindButton.setVisibility(View.GONE);
                    btnBack.setVisibility(View.VISIBLE);
                } else {
                    showToast(result.getMessage());
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE_HOSPITAL){
            HospitalBean bean = (HospitalBean) data.getSerializableExtra(HospitalListActivity.KEY_SELECT_HOSPITAL);
            hospitalBean = bean;
            if (bean != null){
                itemHospital.setContent(bean.getAbbreviation());
            }else{
                itemHospital.setContent("");

            }
        }
    }

    private class OnResultListener implements PreferenceManager.OnActivityResultListener {

        @Override
        public boolean onActivityResult(int requestCode, int resultCode, Intent data) {
            if(data == null){
                return false;
            }
            final String content = data.getStringExtra(ContentInputActivity.KEY_TEXT_CONTENT);
            if(resultCode != RESULT_OK || content==null) {
                return false;
            }
            if (resultCode == RESULT_OK){
                switch (requestCode){
                    case REQUEST_CODE_IDCARD:
                        itemIdCard.setContent(content);
                        JDStorage.getInstance().storeStringValue(StorageConstants.KEY_ID_CARD, content);
                        idCard = content;
                        break;
                    case REQUEST_CODE_MECARD:
                        JDStorage.getInstance().storeStringValue(StorageConstants.KEY_MED_CARD_NO, content);
                        itemMeCard.setContent(content);
                        break;
                }
            }
            return false;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_add_user,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.adduser) {
        Intent intent = new Intent(this,AddUserDetailActivity.class);
            startActivity(intent);
        }
        return true;

    }
}