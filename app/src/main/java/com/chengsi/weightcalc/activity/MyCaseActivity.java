package com.chengsi.weightcalc.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.alibaba.fastjson.TypeReference;
import com.chengsi.weightcalc.R;
import com.chengsi.weightcalc.bean.BaseBean;
import com.chengsi.weightcalc.bean.CaseBean;
import com.chengsi.weightcalc.bean.RequestCaseBean;
import com.chengsi.weightcalc.http.JDHttpClient;
import com.chengsi.weightcalc.http.JDHttpResponseHandler;
import com.chengsi.weightcalc.listener.OnContinuousClickListener;
import com.chengsi.weightcalc.utils.JDUtils;
import com.chengsi.weightcalc.widget.JDLoadingView;
import com.chengsi.weightcalc.widget.PreferenceRightDetailView;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class MyCaseActivity extends BaseActivity implements View.OnClickListener{

    private static final int REQUEST_CODE_ZHUSU = 0x0001;
    private static final int REQUEST_CODE_MEDICAL_HISTORY = 0x0002;
    private static final int REQUEST_CODE_NAME = 0x0003;
    private static final int REQUEST_CODE_INTERMARRIAGE = 0x0004;
    private static final int REQUEST_CODE_EXIT_CHILDREN = 0x0005;
    private static final int REQUEST_CODE_ADOPTED_CHILDREN = 0x0006;
    private static final int REQUEST_CODE_END_TIME = 0x0007;
    private static final int REQUEST_CODE_MENARCHE = 0x0008;
    private static final int REQUEST_CODE_MENSTRUALCYCLE = 0x0009;

    @InjectView(R.id.item_name)
    PreferenceRightDetailView itemName;
    @InjectView(R.id.item_zhusu)
    PreferenceRightDetailView itemZhusu;
    @InjectView(R.id.item_medical_history)
    PreferenceRightDetailView itemMedicalHistory;
    @InjectView(R.id.item_intermarriage)
    PreferenceRightDetailView itemInterMarriage;
    @InjectView(R.id.item_exist_children)
    PreferenceRightDetailView itemExistChildren;
    @InjectView(R.id.item_adopted_children)
    PreferenceRightDetailView itemAdoptedChildren;
    @InjectView(R.id.item_end_time)
    PreferenceRightDetailView itemEndTime;
    @InjectView(R.id.item_menarche)
    PreferenceRightDetailView itemMenarche;
    @InjectView(R.id.item_menstrual_cycle)
    PreferenceRightDetailView itemMenstrualCycle;

    @InjectView(R.id.panel_husband_area)
    View panel_husband_area;
    @InjectView(R.id.panel_wife_area)
    View panel_wife_area;
    private int type = 0;//默认为女

    private RequestCaseBean myCasesBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_case);
        ButterKnife.inject(this);

        panel_wife_area.setOnClickListener(new OnContinuousClickListener() {
            @Override
            public void onContinuousClick(View v) {
                type = 0;
                initViews();
            }
        });
        panel_husband_area.setOnClickListener(new OnContinuousClickListener() {
            @Override
            public void onContinuousClick(View v) {
                type = 1;
                initViews();
            }
        });
        itemName.setOnClickListener(this);
        itemZhusu.setOnClickListener(this);
        itemMedicalHistory.setOnClickListener(this);
        itemInterMarriage.setOnClickListener(this);
        itemExistChildren.setOnClickListener(this);
        itemAdoptedChildren.setOnClickListener(this);
        itemMenarche.setOnClickListener(this);
        itemMenstrualCycle.setOnClickListener(this);
        initDataSource();
    }

    protected void initDataSource() {
        setLoadingViewState(JDLoadingView.STATE_LOADING);
        JDHttpClient.getInstance().reqCases(this, new JDHttpResponseHandler<RequestCaseBean>(new TypeReference<BaseBean<RequestCaseBean>>() {
        }) {
            @Override
            public void onRequestCallback(BaseBean<RequestCaseBean> result) {
                super.onRequestCallback(result);
                dismissLoadingView();
                if (result.isSuccess()) {
                    myCasesBean = result.getData();
                    initViews();
                } else {
                    setLoadingViewState(JDLoadingView.STATE_FAILED);
                    showToast(result.getMessage());
                }
            }
        });
    }

    private void initViews() {
        CaseBean bean = null;
        if (myCasesBean == null){
            myCasesBean = new RequestCaseBean();
        }
        if (myCasesBean.getHusbandCase() == null){
            myCasesBean.setHusbandCase(new CaseBean());
        }
        myCasesBean.getHusbandCase().setType(1);
        if (myCasesBean.getWifeCase() == null){
            myCasesBean.setWifeCase(new CaseBean());
        }
        myCasesBean.getWifeCase().setType(0);
        if (type == 0){
            itemMenarche.setVisibility(View.VISIBLE);
            itemMenstrualCycle.setVisibility(View.VISIBLE);
            itemEndTime.setVisibility(View.VISIBLE);
            panel_wife_area.setSelected(true);
            panel_husband_area.setSelected(false);
            bean = myCasesBean.getWifeCase();
        }else{
            itemMenarche.setVisibility(View.GONE);
            itemMenstrualCycle.setVisibility(View.GONE);
            itemEndTime.setVisibility(View.GONE);
            panel_wife_area.setSelected(false);
            panel_husband_area.setSelected(true);
            bean = myCasesBean.getHusbandCase();
        }

        if (bean != null){
            itemName.setContent(bean.getName());
            itemZhusu.setContent(bean.getComplaint());
            itemMedicalHistory.setContent(bean.getMedicalHistory());
            itemExistChildren.setContent(bean.getChild());
            itemAdoptedChildren.setContent(bean.getAdopt());
            itemEndTime.setContent(bean.getPregnancyLastTime());
            itemMenarche.setContent(bean.getMenarche());
            itemInterMarriage.setContent(bean.getRelative());
            itemMenstrualCycle.setContent(bean.getMenstrualCycle());
        }
    }

    private void saveData(){
        if (myCasesBean == null){
            showToast("没有任何修改！");
            return;
        }
//        if (TextUtils.isEmpty(myCasesBean.getWifeArchive().getName())){
//            showToast("请输入妻子姓名！");
//            return;
//        }
//        if (TextUtils.isEmpty(myCasesBean.getHusbandArchive().getName())){
//            showToast("请输入丈夫姓名！");
//            return;
//        }
//        if (TextUtils.isEmpty(myCasesBean.getWifeArchive().getCardNo())){
//            showToast("请输入妻子身份证号！");
//            return;
//        }
//        if (TextUtils.isEmpty(myCasesBean.getHusbandArchive().getName())){
//            showToast("请输入丈夫身份证号！");
//            return;
//        }
//        if (TextUtils.isEmpty(myCasesBean.getWifeArchive().getCardNo()) && TextUtils.isEmpty(myCasesBean.getHusbandArchive().getPhone())){
//            showToast("请输入妻子手机号码或者丈夫手机号码！");
//            return;
//        }

        setLoadingViewState(JDLoadingView.STATE_LOADING);
        if (TextUtils.isEmpty(myCasesBean.getToken())){
//            myCasesBean.setToken(application.userManager.getUserBean().getToken());
        }
        JDHttpClient.getInstance().reqAddCases(this, myCasesBean, new JDHttpResponseHandler<String>(new TypeReference<BaseBean<String>>(){}){

            @Override
            public void onRequestCallback(BaseBean<String> result) {
                super.onRequestCallback(result);
                dismissLoadingView();
                if (result.isSuccess()){
                    showToast(R.string.success_operate);
                    finish();
                }else{
                    showToast(result.getMessage());
                }
            }
        });
    }

    private CaseBean getCurrentArchive(){
        CaseBean bean = null;
        if (type == 0){
            bean = myCasesBean.getWifeCase();
        }else{
            bean = myCasesBean.getHusbandCase();
        }
        return bean;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_my_case, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_save) {
            saveData();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.item_name:
                JDUtils.startContentInputActivity(this, itemName.getTitle().toString(), this.getString(R.string.hint_name_input), 20, itemName.getContent().toString(), REQUEST_CODE_NAME, ContentInputActivity.CAN_NOT_BLANK_NULL, new OnResultListener());
                break;
            case R.id.item_zhusu:
                JDUtils.startContentInputActivity(this, itemZhusu.getTitle().toString(), this.getString(R.string.cell_phone_input), 11, itemZhusu.getContent().toString(), REQUEST_CODE_ZHUSU,ContentInputActivity.CAN_NOT_BLANK_NULL, new OnResultListener());
                break;
            case R.id.item_medical_history:
                JDUtils.startContentInputActivity(this, itemMedicalHistory.getTitle().toString(), this.getString(R.string.hint_medical_history_input), 18, itemMedicalHistory.getContent().toString(), REQUEST_CODE_MEDICAL_HISTORY, ContentInputActivity.CAN_NOT_BLANK_NULL, new OnResultListener());
                break;
            case R.id.item_exist_children:
                JDUtils.startContentInputActivity(this, itemExistChildren.getTitle().toString(), this.getString(R.string.hint_exist_children_input), 50, itemExistChildren.getContent().toString(), REQUEST_CODE_EXIT_CHILDREN,ContentInputActivity.CAN_NOT_BLANK_NULL, new OnResultListener());
                break;
            case R.id.item_adopted_children:
                JDUtils.startContentInputActivity(this, itemAdoptedChildren.getTitle().toString(), this.getString(R.string.hint_adopted_input), 50, itemAdoptedChildren.getContent().toString(), REQUEST_CODE_ADOPTED_CHILDREN, ContentInputActivity.CAN_NOT_BLANK_NULL, new OnResultListener());
                break;
            case R.id.item_end_time:
                JDUtils.startContentInputActivity(this, 2, itemEndTime.getTitle().toString(), this.getString(R.string.hint_last_pregnant_input), 6, itemEndTime.getContent().toString(), REQUEST_CODE_END_TIME,ContentInputActivity.CAN_NOT_BLANK_NULL, new OnResultListener());
                break;
            case R.id.item_menarche:
                JDUtils.startContentInputActivity(this, 2, itemMenarche.getTitle().toString(), this.getString(R.string.hint_menarche_input), 6, itemMenarche.getContent().toString(), REQUEST_CODE_MENARCHE,ContentInputActivity.CAN_NOT_BLANK_NULL, new OnResultListener());
                break;
            case R.id.item_menstrual_cycle:
                JDUtils.startContentInputActivity(this, itemMenstrualCycle.getTitle().toString(), this.getString(R.string.hint_menstrual_cycle_input), 6, itemMenstrualCycle.getContent().toString(), REQUEST_CODE_MENSTRUALCYCLE,ContentInputActivity.CAN_RETURN_INT, new OnResultListener());
                break;
        }
    }

    private class OnResultListener implements PreferenceManager.OnActivityResultListener {

        @Override
        public boolean onActivityResult(int requestCode, int resultCode, Intent data) {
            if(data == null){
                return false;
            }
            final String content = data.getStringExtra(ContentInputActivity.KEY_TEXT_CONTENT);
            if(resultCode != Activity.RESULT_OK || content==null) {
                return false;
            }
            if (resultCode == RESULT_OK){
                switch (requestCode){
                    case REQUEST_CODE_NAME:
                        itemName.setContent(content);
                        getCurrentArchive().setName(content);
                        break;
                    case REQUEST_CODE_ZHUSU:
                        itemZhusu.setContent(content);
                        getCurrentArchive().setComplaint(content);
                        break;
                    case REQUEST_CODE_MEDICAL_HISTORY:
                        itemMedicalHistory.setContent(content);
                        getCurrentArchive().setMedicalHistory(content);
                        break;
                    case REQUEST_CODE_INTERMARRIAGE:
                        break;
                    case REQUEST_CODE_EXIT_CHILDREN:
                        itemExistChildren.setContent(content);
                        getCurrentArchive().setChild(content);
                        break;
                    case REQUEST_CODE_ADOPTED_CHILDREN:
                        itemAdoptedChildren.setContent(content);
                        getCurrentArchive().setAdopt(content);
                        break;
                    case REQUEST_CODE_END_TIME:
                        itemEndTime.setContent(content);
                        getCurrentArchive().setPregnancyLastTime(content);
                        break;
                    case REQUEST_CODE_MENARCHE:
                        itemMenarche.setContent(content);
                        getCurrentArchive().setMenarche(content);
                        break;
                    case REQUEST_CODE_MENSTRUALCYCLE:
                        itemMenstrualCycle.setContent(content);
                        getCurrentArchive().setMenstrualCycle(content);
                        break;
                }
            }
            return false;
        }
    }
}
