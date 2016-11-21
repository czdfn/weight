package com.chengsi.weightcalc.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.RelativeLayout;

import com.alibaba.fastjson.TypeReference;
//import com.chengsi.pregnancy.PayConfirmActivity;
import com.chengsi.weightcalc.R;
import com.chengsi.weightcalc.bean.BaseBean;
import com.chengsi.weightcalc.bean.DoctorBean;
import com.chengsi.weightcalc.bean.HospitalBean;
import com.chengsi.weightcalc.bean.OutDoctorArrangeBean;
import com.chengsi.weightcalc.bean.OutReservationHistoryBean;
import com.chengsi.weightcalc.bean.ReservationInfo;
import com.chengsi.weightcalc.bean.ReservationItem;
import com.chengsi.weightcalc.bean.ReservationTime;
import com.chengsi.weightcalc.bean.ReservationTimeListBean;
import com.chengsi.weightcalc.bean.ReservationType;
import com.chengsi.weightcalc.http.HttpConstants;
import com.chengsi.weightcalc.http.JDHttpClient;
import com.chengsi.weightcalc.http.JDHttpResponseHandler;
import com.chengsi.weightcalc.listener.OnContinuousClickListener;
import com.chengsi.weightcalc.widget.JDLoadingView;
import com.chengsi.weightcalc.widget.PreferenceRightDetailView;
import com.chengsi.weightcalc.widget.dialog.SimpleSelectDialog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import cn.jiadao.corelibs.utils.ListUtils;

public class ReservationActivity extends BaseActivity {

    public static final String KEY_RESERVATION_PROGRAM = "KEY_RESERVATION_PROGRAM";
    public static final String KEY_RESERVATION_TYPE = "KEY_RESERVATION_TYPE";
    public static final String KEY_RESERVATION_DOCTOR = "KEY_RESERVATION_DOCTOR";
    public static final String KEY_RESERVATION_TIME = "KEY_RESERVATION_TIME";
    public static final String KEY_RESERVATION_DATE = "KEY_RESERVATION_DATE";

    public static final String KEY_RESERVATION_DEPARTMENT = "KEY_RESERVATION_DEPARTMENT";
    public static final String KEY_RESERVATION_CATEGORY = "KEY_RESERVATION_CATEGORY";
    public static final String KEY_RESERVATION_OUT_ARRANGE = "KEY_RESERVATION_OUT_ARRANGE";


    private static final int REQUEST_CODE_SELECT_HOSPITAL = 0x0010;
    private static final int REQUEST_CODE_SELECT_DOCTOR = 0x0011;
    private static final int REQUEST_CODE_OUT_SELECT_DOCTOR = 0x0013;//外部挂号选医生
    private static final int REQUEST_CODE_SELECT_DATE = 0x0012;

    @InjectView(R.id.item_reservation_doctor)
    PreferenceRightDetailView itemDoctor;

    @InjectView(R.id.item_reservation_type)
    PreferenceRightDetailView itemReservationType;
    @InjectView(R.id.item_reservation_department)
    RelativeLayout itemReservationDepartment;//预约科室
    @InjectView(R.id.item_reservation_category)
    RelativeLayout itemReservationCategory;//号码类别
    @InjectView(R.id.item_reservation_time)
    PreferenceRightDetailView itemReservationTime;
    @InjectView(R.id.item_reservation_program)
    PreferenceRightDetailView itemReservationProgram;
    @InjectView(R.id.item_reservation_hospital)
    PreferenceRightDetailView itemReservationHospital;

    @InjectView(R.id.cb_sex_male)
    CheckBox cbMale;
    @InjectView(R.id.cb_sex_female)
    CheckBox cbFemale;
    @InjectView(R.id.cb_normal)
    CheckBox cbNormal;
    @InjectView(R.id.cb_innormal)
    CheckBox cbInnormal;

    private ReservationInfo reservationInfo = null;
    private ReservationItem mCurItem;
    private ReservationType mCurType;
    private DoctorBean mCurDoctor;
    private ReservationTime mCurTime;
    private String mReservationDate;

    private String mCurCategory;
    private String mCurDepartment;
    private OutDoctorArrangeBean mOutArrangeBean;

    private List<String> reservationTypeList = new ArrayList<>();

    private HospitalBean hospitalBean;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservation);
        setMyTitle("预约挂号");
        ButterKnife.inject(this);
//        if (application.userManager.getUserBean().getHospital() != null){
//            hospitalBean = application.userManager.getUserBean().getHospital();
//            itemReservationHospital.setContent(application.userManager.getUserBean().getHospital().getAbbreviation());
//            initReservationInfo();
//        }else{
//            initViews();
//        }
    }

    private void initViews() {
        itemDoctor.setOnClickListener(new OnContinuousClickListener() {
            @Override
            public void onContinuousClick(View v) {
                if (hospitalBean == null) {
                    showToast(R.string.err_please_select_hospital);
                } else {
                    if (!hospitalBean.getIsOutRegister()){
                        if (mCurType == null){
                            showToast(R.string.err_please_select_reservation_type);
                            return;
                        }
                    }else{
                        if (mCurCategory == null){
                            showToast(R.string.err_please_select_reservation_category);
                            return;
                        }
                    }
                    if (!hospitalBean.isOutRegister()){
                        Intent intent = new Intent(ReservationActivity.this, ReservationDoctorActivity.class);
                        intent.putExtra(KEY_RESERVATION_PROGRAM, mCurItem);
                        intent.putExtra(KEY_RESERVATION_TYPE, mCurType);
                        startActivityForResult(intent, REQUEST_CODE_SELECT_DOCTOR);
                    }else{
                        Intent intent = new Intent(ReservationActivity.this, ReservationDoctorActivity.class);
                        intent.putExtra(KEY_RESERVATION_DEPARTMENT, mCurDepartment);
                        intent.putExtra(KEY_RESERVATION_CATEGORY, mCurCategory);
                        startActivityForResult(intent, REQUEST_CODE_OUT_SELECT_DOCTOR);
                    }
                }
            }
        });

        itemReservationType.setOnClickListener(new OnContinuousClickListener() {
            @Override
            public void onContinuousClick(View v) {
                if (hospitalBean == null) {
                    showToast(R.string.err_please_select_hospital);
                } else {
                    if (reservationInfo == null) {
                        showToast("预约信息获取失败，请重新选择生殖中心");
                        return;
                    }
                    if (ListUtils.isEmpty(reservationInfo.getHospitalItemS())) {
                        showToast("该生殖中心没有可预约的项目");
                    } else {
                        final SimpleSelectDialog dialog = SimpleSelectDialog.getInstance();
                        final List<ReservationType> typeList = reservationInfo.getHospitalTypeS();
                        List<String> list = new ArrayList<String>();
                        for (ReservationType item : typeList) {
                            list.add(item.getTypeName());
                        }
                        dialog.showSelect(getSupportFragmentManager(), list, new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                if (mCurType != typeList.get(position)){
                                    itemDoctor.setContent(null);
                                    itemReservationTime.setContent(null);
                                    mCurDoctor = null;
                                    mCurTime = null;
                                    itemReservationType.setContent(null);
                                    mCurType = null;
                                }
                                mCurType = typeList.get(position);
                                itemReservationType.setContent(mCurType.getTypeName());
                                dialog.dismiss();
                            }
                        });
                    }
                }
            }
        });

        itemReservationTime.setOnClickListener(new OnContinuousClickListener() {
            @Override
            public void onContinuousClick(View v) {
                if (hospitalBean == null) {
                    showToast(R.string.err_please_select_hospital);
                } else {
                    if (hospitalBean.getIsOutRegister()){
                        if (mCurCategory == null){
                            showToast(R.string.err_please_select_reservation_category);
                            return;
                        }
                        if (mCurCategory.equals("1") && mOutArrangeBean == null){

                            showToast("请先选择医生");
                            return;
                        }

                        Intent intent = new Intent(ReservationActivity.this, ReservationDateActivity.class);
                        intent.putExtra(KEY_RESERVATION_DEPARTMENT, mCurDepartment);
                        intent.putExtra(KEY_RESERVATION_CATEGORY, mCurCategory);
                        intent.putExtra(KEY_RESERVATION_DOCTOR, mOutArrangeBean);
                        intent.putExtra(KEY_RESERVATION_DATE, mReservationDate);
                        intent.putExtra(ReservationDateActivity.KEY_IS_OUT_RESERVATION, true);
                        startActivityForResult(intent, REQUEST_CODE_OUT_SELECT_DOCTOR);
                        return;
                    }else{
                        if (mCurType == null) {
                            showToast(R.string.err_please_select_reservation_type);
                            return;
                        }
                    }

                    if (mCurDoctor == null) {
                        Intent intent = new Intent(ReservationActivity.this, ReservationDoctorActivity.class);
                        intent.putExtra(KEY_RESERVATION_PROGRAM, mCurItem);
                        intent.putExtra(KEY_RESERVATION_TYPE, mCurType);
                        startActivityForResult(intent, REQUEST_CODE_SELECT_DOCTOR);
                    } else {
                        Intent intent = new Intent(ReservationActivity.this, ReservationDateActivity.class);
                        intent.putExtra(KEY_RESERVATION_DOCTOR, mCurDoctor);
                        startActivityForResult(intent, REQUEST_CODE_SELECT_DATE);
                    }
                }
            }
        });

        itemReservationHospital.setOnClickListener(new OnContinuousClickListener() {
            @Override
            public void onContinuousClick(View v) {
//                Intent intent = new Intent(ReservationActivity.this, HospitalListActivity.class);
//                startActivityForResult(intent, REQUEST_CODE_SELECT_HOSPITAL);
            }
        });

        itemReservationProgram.setOnClickListener(new OnContinuousClickListener() {
            @Override
            public void onContinuousClick(View v) {
                if (hospitalBean == null) {
                    showToast(R.string.err_please_select_hospital);
                } else {
                    if (reservationInfo == null) {
                        showToast("预约信息获取失败，请重新选择生殖中心");
                        return;
                    }
                    if (ListUtils.isEmpty(reservationInfo.getHospitalItemS())) {
                        showToast("该生殖中心没有可预约的项目");
                    } else {
                        final SimpleSelectDialog dialog = SimpleSelectDialog.getInstance();
                        final List<ReservationItem> itemList = reservationInfo.getHospitalItemS();
                        List<String> list = new ArrayList<String>();
                        for (ReservationItem item : itemList) {
                            list.add(item.getItemName());
                        }
                        dialog.showSelect(getSupportFragmentManager(), list, new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                if (mCurItem != itemList.get(position)){
                                    itemDoctor.setContent(null);
                                    itemReservationTime.setContent(null);
                                    mCurDoctor = null;
                                    mCurTime = null;
                                }
                                mCurItem = itemList.get(position);
                                itemReservationProgram.setContent(mCurItem.getItemName());
                                dialog.dismiss();
                            }
                        });
                    }
                }
            }
        });
        mCurType = null;
        mCurItem = null;
        mCurDoctor = null;
        mCurTime = null;
        mCurCategory = null;
        mCurDepartment = null;

        itemDoctor.setContent(null);
        itemReservationProgram.setContent(null);
        itemReservationType.setContent(null);
        itemReservationTime.setContent(null);

        cbFemale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cbFemale.setChecked(true);
                cbMale.setChecked(false);
                mCurDepartment = "0";
                resetDoctor();
            }
        });
        cbMale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cbMale.setChecked(true);
                cbFemale.setChecked(false);
                mCurDepartment = "1";
                resetDoctor();
            }
        });
        cbNormal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cbNormal.setChecked(true);
                cbInnormal.setChecked(false);
                mCurCategory = "0";
                itemDoctor.setVisibility(View.GONE);
                resetDoctor();
            }
        });
        cbInnormal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cbInnormal.setChecked(true);
                cbNormal.setChecked(false);
                mCurCategory = "1";
                itemDoctor.setVisibility(View.VISIBLE);
                resetDoctor();
            }
        });
    }

    private void resetDoctor(){
        mCurDoctor = null;
        mOutArrangeBean = null;
        itemDoctor.setContent(null);
        itemReservationTime.setContent(null);
        mReservationDate = null;
    }

    @Override
    protected void initDataSource() {
        super.initDataSource();
        initReservationInfo();
    }

    private void initReservationInfo() {
        if (hospitalBean == null){
            return;
        }
        if(hospitalBean.getIsOutRegister()==null?false:hospitalBean.getIsOutRegister()){
            itemReservationCategory.setVisibility(View.VISIBLE);
            itemReservationDepartment.setVisibility(View.VISIBLE);
            itemReservationProgram.setVisibility(View.GONE);
            itemReservationType.setVisibility(View.GONE);

            initViews();
//            if (application.userManager.getUserBean().getSex() == 1){
//                cbFemale.setChecked(true);
//                cbMale.setChecked(false);
//
//                mCurDepartment = "0";
//                mCurCategory = "1";
//                cbNormal.setChecked(false);
//                cbInnormal.setChecked(true);
//                itemDoctor.setVisibility(View.VISIBLE);
//            }else if (application.userManager.getUserBean().getSex() == 2){
//                cbFemale.setChecked(false);
//                cbMale.setChecked(true);
//                mCurDepartment = "1";
//                mCurCategory = "0";
//                cbNormal.setChecked(true);
//                cbInnormal.setChecked(false);
//                itemDoctor.setVisibility(View.GONE);
//            }
            itemReservationHospital.setOnClickListener(null);
            itemReservationHospital.setAccessImageVisible(View.GONE);
        }else {
            itemReservationProgram.setVisibility(View.VISIBLE);
            itemReservationType.setVisibility(View.VISIBLE);
            itemReservationCategory.setVisibility(View.GONE);
            itemReservationDepartment.setVisibility(View.GONE);
            setLoadingViewState(JDLoadingView.STATE_LOADING);
            JDHttpClient.getInstance().reqReservationInfo(this, hospitalBean.getId() + "", new JDHttpResponseHandler<ReservationInfo>(new TypeReference<BaseBean<ReservationInfo>>() {
            }) {
                @Override
                public void onRequestCallback(BaseBean<ReservationInfo> result) {
                    super.onRequestCallback(result);
                    dismissLoadingView();
                    if (!result.isSuccess()) {
                        showToast(result.getMessage());
                        setLoadingViewState(JDLoadingView.STATE_FAILED);
                        return;
                    }
                    reservationInfo = result.getData();
                    initViews();
                }
            });
        }
    }

    @OnClick(R.id.btn_submit)
    void submit(){
        if (hospitalBean == null){
            showToast(R.string.err_please_select_hospital);
            return;
        }
        if (!hospitalBean.isOutRegister()){
            if (mCurType == null){
                showToast(R.string.err_please_select_reservation_type);
                return;
            }
            if (mCurDoctor == null){
                showToast(R.string.err_please_select_reservation_doctor);
                return;
            }
            if (mCurTime == null){
                showToast(R.string.err_please_select_reservation_date);
                return;
            }

            setLoadingViewState(JDLoadingView.STATE_LOADING);
            String itemId = mCurItem == null ? null : String.valueOf(mCurItem.getItemId());
            String typeId = mCurType == null ? null : String.valueOf(mCurType.getTypeId());
            JDHttpClient.getInstance().reqAddReservation(this, String.valueOf(mCurDoctor.getId()), itemId, mReservationDate, String.valueOf(mCurTime.getTimeId()),
                    String.valueOf(mCurTime.getArrangeId()), String.valueOf(mCurType.getTypeId()), new JDHttpResponseHandler<ReservationTimeListBean>(new TypeReference<BaseBean<ReservationTimeListBean>>() {
                    }) {
                        @Override
                        public void onRequestCallback(BaseBean<ReservationTimeListBean> result) {
                            super.onRequestCallback(result);
                            dismissLoadingView();
                            if (result.isSuccess()) {
                                Intent intent = new Intent(ReservationActivity.this, ReservationHistoryActivity.class);
                                startActivity(intent);
                                showToast(R.string.reservation_success);
                                finish();
                            } else {
                                showToast(result.getMessage());
                            }
                        }
                    });
        }else{
            if (mCurCategory == null){
                showToast(R.string.err_please_select_reservation_category);
                return;
            }
            if (mOutArrangeBean == null){
                showToast("请先选择就诊时间");
                return;
            }

            Map<String, Object> map = new HashMap<>();
            map.put("request_date", mReservationDate);
            map.put("time_code", mOutArrangeBean.getTimeCode());
            map.put("time_name", mOutArrangeBean.getTimeName());
            map.put("request_date", mReservationDate);
            map.put("record_sn", mOutArrangeBean.getRecord_sn());
            map.put("doctor_sn", mOutArrangeBean.getDoctor_sn());
            map.put("clinic_flag", mCurCategory);
            map.put("unit_type", mCurDepartment);
//            map.put("card_no", application.userManager.getUserBean().getVisitCard());
//            map.put("name", application.userManager.getUserBean().getRealname());
//            map.put("social_no", application.userManager.getUserBean().getIdNo());
//            map.put("mobile", application.userManager.getUserBean().getPhone());
            map.put("order_id", "");
            setLoadingViewState(JDLoadingView.STATE_LOADING);
            JDHttpClient.getInstance().reqHisRest(this, HttpConstants.REQUEST_OUT_RESERVATION_ADD_URL, map, new JDHttpResponseHandler<OutReservationHistoryBean>(new TypeReference<BaseBean<OutReservationHistoryBean>>() {
            }) {
                @Override
                public void onRequestCallback(final BaseBean<OutReservationHistoryBean> result) {
                    super.onRequestCallback(result);
                    dismissLoadingView();
                    if (result.isSuccess() && result.getData() != null) {
//                        Intent intent = new Intent(ReservationActivity.this, PayConfirmActivity.class);
//                        intent.putExtra(PayConfirmActivity.KEY_OUT_RESERVATION_BEAN, result.getData());
//                        startActivity(intent);
                        finish();
                        showToast(result.getData().getRemark());
                    } else {
                        showToast(result.getMessage());
                    }
                }
            });
        }
    }

    void payMoney(OutReservationHistoryBean bean){
        Map<String, Object> map = new HashMap<>();
        map.put("request_date", bean.getRequest_date());
        map.put("record_sn", bean.getRecord_sn());
        map.put("doctor_sn", bean.getDoctor_sn());
        map.put("time_code", mOutArrangeBean.getTimeCode());
        map.put("time_name", mOutArrangeBean.getTimeName());
        map.put("clinic_flag", mCurCategory);
        map.put("unit_type", mCurDepartment);
//        map.put("card_no", application.userManager.getUserBean().getVisitCard());
//        map.put("name", application.userManager.getUserBean().getRealname());
//        map.put("social_no", application.userManager.getUserBean().getIdNo());
//        map.put("mobile", application.userManager.getUserBean().getPhone());
        map.put("order_id", bean.getOrder_id());
        setLoadingViewState(JDLoadingView.STATE_LOADING);
        JDHttpClient.getInstance().reqHisRest(this, HttpConstants.REQUEST_OUT_RESERVATION_PAY_URL, map, new JDHttpResponseHandler<OutReservationHistoryBean>(new TypeReference<BaseBean<OutReservationHistoryBean>>() {
        }) {
            @Override
            public void onRequestCallback(final BaseBean<OutReservationHistoryBean> result) {
                super.onRequestCallback(result);
                dismissLoadingView();
                if (result.isSuccess() && result.getData() != null) {
                    OutReservationHistoryBean data = result.getData();
                    showToast("挂号成功!");
                    jumpToDetail(data);
                } else {
                    showToast(result.getMessage());
                }
            }
        });
    }

    void jumpToDetail(OutReservationHistoryBean bean){
        showToast(R.string.reservation_success);
        Intent intent = new Intent(ReservationActivity.this, OutReservationDetailActivity.class);
        intent.putExtra(OutReservationDetailActivity.KEY_IS_NEW_OUT_RESERVATION, true);
        intent.putExtra(OutReservationDetailActivity.KEY_OUT_RESERVATION_BEAN, bean);
        startActivity(intent);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_reservation, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.menu_reservation_history) {
            if (hospitalBean.isOutRegister()){
                Intent intent = new Intent(this, OutReservationHistoryActivity.class);
                startActivity(intent);
            }else{

                Intent intent = new Intent(this, ReservationHistoryActivity.class);
                startActivity(intent);
            }
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (REQUEST_CODE_SELECT_HOSPITAL == requestCode && resultCode == RESULT_OK) {
//            hospitalBean = (HospitalBean) data.getSerializableExtra(HospitalListActivity.KEY_SELECT_HOSPITAL);
//            itemReservationHospital.setContent(hospitalBean.getName());
//            initReservationInfo();
        }else if(REQUEST_CODE_SELECT_DOCTOR == requestCode && resultCode == RESULT_OK){
            mCurDoctor = (DoctorBean) data.getSerializableExtra(KEY_RESERVATION_DOCTOR);
            mCurTime = (ReservationTime) data.getSerializableExtra(KEY_RESERVATION_TIME);
            mReservationDate = data.getStringExtra(KEY_RESERVATION_DATE);
            itemDoctor.setContent(mCurDoctor.getRealName());
            itemReservationTime.setContent(mReservationDate + "    " + mCurTime.getTimeValue());
        }else if(REQUEST_CODE_SELECT_DATE == requestCode && resultCode == RESULT_OK){
            mReservationDate = data.getStringExtra(KEY_RESERVATION_DATE);
            mCurTime = (ReservationTime) data.getSerializableExtra(KEY_RESERVATION_TIME);
            itemReservationTime.setContent(mReservationDate + "    " + mCurTime.getTimeValue());
        }else if (REQUEST_CODE_OUT_SELECT_DOCTOR == requestCode && resultCode == RESULT_OK){
            mOutArrangeBean = (OutDoctorArrangeBean) data.getSerializableExtra(KEY_RESERVATION_OUT_ARRANGE);
            mReservationDate = data.getStringExtra(KEY_RESERVATION_DATE);
            if (mOutArrangeBean != null){
                itemDoctor.setContent(mOutArrangeBean.getDoctor_name());
                itemReservationTime.setContent(mReservationDate + "   "  + mOutArrangeBean.getTimeName());
            }
        }
    }
}
