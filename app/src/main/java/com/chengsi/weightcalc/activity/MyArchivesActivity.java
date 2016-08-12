package com.chengsi.weightcalc.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;

import com.alibaba.fastjson.TypeReference;
import com.chengsi.weightcalc.R;
import com.chengsi.weightcalc.bean.BaseBean;
import com.chengsi.weightcalc.bean.MyArchivesBean;
import com.chengsi.weightcalc.bean.UserArchiveBean;
import com.chengsi.weightcalc.http.JDHttpClient;
import com.chengsi.weightcalc.http.JDHttpResponseHandler;
import com.chengsi.weightcalc.listener.OnContinuousClickListener;
import com.chengsi.weightcalc.utils.JDUtils;
import com.chengsi.weightcalc.widget.JDLoadingView;
import com.chengsi.weightcalc.widget.PreferenceRightDetailView;
import com.chengsi.weightcalc.widget.dialog.SimpleSelectDialog;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class MyArchivesActivity extends BaseActivity implements View.OnClickListener{

    private static final int REQUEST_CODE_CELL_PHONE = 0x0001;
    private static final int REQUEST_CODE_LANDLINE = 0x0002;
    private static final int REQUEST_CODE_NAME = 0x0003;
    private static final int REQUEST_CODE_IDCARD = 0x0004;
    private static final int REQUEST_CODE_BIRTHPLACE = 0x0005;
    private static final int REQUEST_CODE_ADDRESS = 0x0006;
    private static final int REQUEST_CODE_POSTCODE = 0x0007;
    private static final int REQUEST_CODE_PROFESSION = 0x0008;

    @InjectView(R.id.item_name)
    PreferenceRightDetailView itemName;
    @InjectView(R.id.item_id_card)
    PreferenceRightDetailView itemIdCard;
    @InjectView(R.id.item_birthPlace)
    PreferenceRightDetailView itemBirthPlace;
    @InjectView(R.id.item_international)
    PreferenceRightDetailView itemInternational;
    @InjectView(R.id.item_nation)
    PreferenceRightDetailView itemNation;
    @InjectView(R.id.item_marriage)
    PreferenceRightDetailView itemMarriage;
    @InjectView(R.id.item_profession)
    PreferenceRightDetailView itemProfession;
    @InjectView(R.id.item_education)
    PreferenceRightDetailView itemEducation;
    @InjectView(R.id.item_postCode)
    PreferenceRightDetailView itemZipCode;
    @InjectView(R.id.item_address)
    PreferenceRightDetailView itemAddress;
    @InjectView(R.id.item_phone)
    PreferenceRightDetailView itemPhone;
    @InjectView(R.id.item_tel)
    PreferenceRightDetailView itemTel;

    @InjectView(R.id.panel_husband_area)
    View panel_husband_area;
    @InjectView(R.id.panel_wife_area)
    View panel_wife_area;
    private int type = 0;//默认为女

    private MyArchivesBean myArchivesBean;

    private static final List<String> marriageList = new ArrayList<>();
    private static final List<String> educationList = new ArrayList<>();
    private static final List<String> nationList = new ArrayList<>();
    private static final String nationStr = "汉族、蒙古族、回族、藏族、维吾尔族、苗族、彝族、壮族、布依族、朝鲜族、满族、侗族、瑶族、白族、土家族、哈尼族、哈萨克族、傣族、黎族、僳僳族、佤族、畲族、高山族、拉祜族、水族、东乡族、纳西族、景颇族、柯尔克孜族、土族、达斡尔族、仫佬族、羌族、布朗族、撒拉族、毛南族、仡佬族、锡伯族、阿昌族、普米族、塔吉克族、怒族、乌孜别克族、俄罗斯族、鄂温克族、德昂族、保安族、裕固族、京族、塔塔尔族、独龙族、鄂伦春族、赫哲族、门巴族、珞巴族、基诺族";
    ;

    static {
        marriageList.clear();
        educationList.clear();
        nationList.clear();

        marriageList.add("已婚");
        marriageList.add("再婚");
        marriageList.add("未婚");
        marriageList.add("离异");
        marriageList.add("丧偶");
        marriageList.add("同居");

        educationList.add("高中以下");
        educationList.add("高中");
        educationList.add("中专");
        educationList.add("大专");
        educationList.add("本科");
        educationList.add("研究生");
        educationList.add("博士及以上");
        nationList.addAll(Arrays.asList(nationStr.split("、")));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_archives);
        ButterKnife.inject(this);

        itemName.setHtmlTitle(getString(R.string.red_star, "姓名"));
//        itemPhone.setHtmlTitle(getString(R.string.red_star, "手机号码"));
        itemIdCard.setHtmlTitle(getString(R.string.red_star, "身份证号"));
        itemMarriage.setOnClickListener(new OnContinuousClickListener() {
            @Override
            public void onContinuousClick(View v) {
                final SimpleSelectDialog dialog = SimpleSelectDialog.getInstance();
                dialog.showSelect(getSupportFragmentManager(), marriageList, new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        itemMarriage.setContent(marriageList.get(position));
                        getCurrentArchive().setMarriage(itemMarriage.getContent().toString());
                        dialog.dismiss();
                    }
                });
            }
        });
        itemEducation.setOnClickListener(new OnContinuousClickListener() {
            @Override
            public void onContinuousClick(View v) {
                final SimpleSelectDialog dialog = SimpleSelectDialog.getInstance();
                dialog.showSelect(getSupportFragmentManager(), educationList, new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        itemEducation.setContent(educationList.get(position));
                        getCurrentArchive().setEducation(itemEducation.getContent().toString());
                        dialog.dismiss();
                    }
                });
            }
        });

        itemNation.setOnClickListener(new OnContinuousClickListener() {
            @Override
            public void onContinuousClick(View v) {
                final SimpleSelectDialog dialog = SimpleSelectDialog.getInstance();
                dialog.showSelect(getSupportFragmentManager(), nationList, new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        itemNation.setContent(nationList.get(position));
                        getCurrentArchive().setNation(itemNation.getContent().toString());
                        dialog.dismiss();
                    }
                });
            }
        });

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
        itemIdCard.setOnClickListener(this);
        itemBirthPlace.setOnClickListener(this);
        itemProfession.setOnClickListener(this);
        itemAddress.setOnClickListener(this);
        itemZipCode.setOnClickListener(this);
        itemPhone.setOnClickListener(this);
        itemTel.setOnClickListener(this);
        initDataSource();
    }

    protected void initDataSource() {
        setLoadingViewState(JDLoadingView.STATE_LOADING);
        JDHttpClient.getInstance().reqArchives(this, new JDHttpResponseHandler<MyArchivesBean>(new TypeReference<BaseBean<MyArchivesBean>>() {
        }) {
            @Override
            public void onRequestCallback(BaseBean<MyArchivesBean> result) {
                super.onRequestCallback(result);
                dismissLoadingView();
                if (result.isSuccess()) {
                    myArchivesBean = result.getData();
                    initViews();
                } else {
                    setLoadingViewState(JDLoadingView.STATE_FAILED);
                    showToast(result.getMessage());
                }
            }
        });
    }

    private void initViews() {
        UserArchiveBean bean = null;
        if (myArchivesBean == null){
            myArchivesBean = new MyArchivesBean();
        }
        if (myArchivesBean.getHusbandArchive() == null){
            myArchivesBean.setHusbandArchive(new UserArchiveBean());
        }
        myArchivesBean.getHusbandArchive().setType(1);
        if (myArchivesBean.getWifeArchive() == null){
            myArchivesBean.setWifeArchive(new UserArchiveBean());
        }
        myArchivesBean.getWifeArchive().setType(0);
        myArchivesBean.getWifeArchive().setInternational("中国");
        myArchivesBean.getHusbandArchive().setInternational("中国");
        if (type == 0){
            panel_wife_area.setSelected(true);
            panel_husband_area.setSelected(false);
                bean = myArchivesBean.getWifeArchive();
        }else{
            panel_wife_area.setSelected(false);
            panel_husband_area.setSelected(true);
            bean = myArchivesBean.getHusbandArchive();
        }

        if (bean != null){
            itemName.setContent(bean.getName());
            itemIdCard.setContent(bean.getCardNo());
            itemBirthPlace.setContent(bean.getBirthPlace());
            itemNation.setContent(bean.getNation());
            itemMarriage.setContent(bean.getMarriage());
            itemProfession.setContent(bean.getProfession());
            itemEducation.setContent(bean.getEducation());
            itemAddress.setContent(bean.getAddress());
            itemZipCode.setContent(bean.getPostCode());
            itemPhone.setContent(bean.getPhone());
            itemTel.setContent(bean.getTel());
        }
    }

    private void saveData(){
        if (myArchivesBean == null){
            showToast("没有任何修改！");
            return;
        }
        if (TextUtils.isEmpty(myArchivesBean.getWifeArchive().getName())){
            showToast("请输入妻子姓名！");
            return;
        }
        if (TextUtils.isEmpty(myArchivesBean.getHusbandArchive().getName())){
            showToast("请输入丈夫姓名！");
            return;
        }
        if (TextUtils.isEmpty(myArchivesBean.getWifeArchive().getCardNo())){
            showToast("请输入妻子身份证号！");
            return;
        }
        if (TextUtils.isEmpty(myArchivesBean.getHusbandArchive().getName())){
            showToast("请输入丈夫身份证号！");
            return;
        }
        if (TextUtils.isEmpty(myArchivesBean.getWifeArchive().getPhone()) && TextUtils.isEmpty(myArchivesBean.getHusbandArchive().getPhone())){
            showToast("请输入妻子手机号码或者丈夫手机号码！");
            return;
        }

        setLoadingViewState(JDLoadingView.STATE_LOADING);
        if (TextUtils.isEmpty(myArchivesBean.getToken())){
//            myArchivesBean.setToken(application.userManager.getUserBean().getToken());
        }
        JDHttpClient.getInstance().reqAddArchives(this, myArchivesBean, new JDHttpResponseHandler<String>(new TypeReference<BaseBean<String>>(){}){

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

    private UserArchiveBean getCurrentArchive(){
        UserArchiveBean bean = null;
        if (type == 0){
            bean = myArchivesBean.getWifeArchive();
        }else{
            bean = myArchivesBean.getHusbandArchive();
        }
        return bean;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_my_archives, menu);
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
                JDUtils.startContentInputActivity(this, "姓名", this.getString(R.string.hint_name_input), 20, itemName.getContent().toString(), REQUEST_CODE_NAME,ContentInputActivity.CAN_NOT_BLANK_NULL, new OnResultListener());
                break;
            case R.id.item_phone:
                JDUtils.startContentInputActivity(this, itemPhone.getTitle().toString(), this.getString(R.string.cell_phone_input), 11, itemPhone.getContent().toString(), REQUEST_CODE_CELL_PHONE,ContentInputActivity.CAN_RETURN_CELL_PHONE, new OnResultListener());
                break;
            case R.id.item_tel:
                JDUtils.startContentInputActivity(this, itemTel.getTitle().toString(), this.getString(R.string.land_line_input), 18, itemTel.getContent().toString(), REQUEST_CODE_LANDLINE, ContentInputActivity.CAN_RETURN_LAND_LINE, new OnResultListener());
                break;
            case R.id.item_id_card:
                JDUtils.startContentInputActivity(this, "身份证号码", this.getString(R.string.hint_idcard_input), 18, itemIdCard.getContent().toString(), REQUEST_CODE_IDCARD,ContentInputActivity.CAN_NOT_RETURN_BLANK, new OnResultListener());
                break;
            case R.id.item_birthPlace:
                JDUtils.startContentInputActivity(this, itemBirthPlace.getTitle().toString(), this.getString(R.string.hint_birthplace_input), 50, itemBirthPlace.getContent().toString(), REQUEST_CODE_BIRTHPLACE,ContentInputActivity.CAN_NOT_BLANK_NULL, new OnResultListener());
                break;
            case R.id.item_address:
                JDUtils.startContentInputActivity(this, itemAddress.getTitle().toString(), this.getString(R.string.hint_addr_input), 50, itemAddress.getContent().toString(), REQUEST_CODE_ADDRESS, ContentInputActivity.CAN_NOT_BLANK_NULL, new OnResultListener());
                break;
            case R.id.item_profession:
                JDUtils.startContentInputActivity(this, itemProfession.getTitle().toString(), this.getString(R.string.hint_profession_input), 50, itemProfession.getContent().toString(), REQUEST_CODE_PROFESSION, ContentInputActivity.CAN_NOT_BLANK_NULL, new OnResultListener());
                break;
            case R.id.item_postCode:
                JDUtils.startContentInputActivity(this, itemZipCode.getTitle().toString(), this.getString(R.string.hint_post_code_input), 6, itemZipCode.getContent().toString(), REQUEST_CODE_POSTCODE,ContentInputActivity.CAN_NOT_BLANK_NULL, new OnResultListener());
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
                    case REQUEST_CODE_IDCARD:
                        itemIdCard.setContent(content);
                        getCurrentArchive().setCardNo(content);
                        break;
                    case REQUEST_CODE_BIRTHPLACE:
                        itemBirthPlace.setContent(content);
                        getCurrentArchive().setBirthPlace(content);
                        break;
                    case REQUEST_CODE_ADDRESS:
                        itemAddress.setContent(content);
                        getCurrentArchive().setAddress(content);
                        break;
                    case REQUEST_CODE_CELL_PHONE:
                        itemPhone.setContent(content);
                        getCurrentArchive().setPhone(content);
                        break;
                    case REQUEST_CODE_LANDLINE:
                        itemTel.setContent(content);
                        getCurrentArchive().setTel(content);
                        break;
                    case REQUEST_CODE_POSTCODE:
                        itemZipCode.setContent(content);
                        getCurrentArchive().setPostCode(content);
                        break;
                    case REQUEST_CODE_PROFESSION:
                        itemProfession.setContent(content);
                        getCurrentArchive().setProfession(content);
                        break;
                }
            }
            return false;
        }
    }
}
