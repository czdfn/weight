package com.chengsi.weightcalc.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.alibaba.fastjson.TypeReference;
//import com.chengsi.pregnancy.PayConfirmActivity;
import com.chengsi.weightcalc.R;
import com.chengsi.weightcalc.bean.BaseBean;
import com.chengsi.weightcalc.bean.OutReservationHistoryBean;
import com.chengsi.weightcalc.constants.IntentConstants;
import com.chengsi.weightcalc.http.HttpConstants;
import com.chengsi.weightcalc.http.JDHttpClient;
import com.chengsi.weightcalc.http.JDHttpResponseHandler;
import com.chengsi.weightcalc.utils.JDUtils;
import com.chengsi.weightcalc.widget.JDLoadingView;
import com.chengsi.weightcalc.widget.dialog.FanrAlertDialog;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class OutReservationDetailActivity extends BaseActivity {

    public static final String KEY_OUT_RESERVATION_BEAN = "KEY_OUT_RESERVATION_BEAN";
    public static final String KEY_IS_NEW_OUT_RESERVATION = "KEY_IS_NEW_OUT_RESERVATION";

    @InjectView(R.id.tv_reservation_status)
    TextView tvReservationStatus;
    @InjectView(R.id.panel_doctor)
    View panelDoctorArea;
    @InjectView(R.id.tv_reservation_doctor)
    TextView tvDoctorName;
    @InjectView(R.id.tv_reservation_state)
    TextView tvState;
    @InjectView(R.id.tv_reservation_type)
    TextView tvType;
    @InjectView(R.id.tv_reservation_date)
    TextView tvDate;
    @InjectView(R.id.tv_reservation_place)
    TextView tvPlace;
    @InjectView(R.id.tv_reservation_transportation)
    TextView tvTransportation;
    @InjectView(R.id.tv_reservation_money)
    TextView tvMoney;
    @InjectView(R.id.tv_reservation_balance)
    TextView tvBalance;
    @InjectView(R.id.panel_balance)
    View panelBalance;
    @InjectView(R.id.btn_cancel)
    Button btnCancel;

    private OutReservationHistoryBean reservationHistoryBean;
    private boolean isNewReservation = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_out_reservation_detail);
        ButterKnife.inject(this);

        reservationHistoryBean = (OutReservationHistoryBean) getIntent().getSerializableExtra(KEY_OUT_RESERVATION_BEAN);
        isNewReservation = getIntent().getBooleanExtra(KEY_IS_NEW_OUT_RESERVATION, false);
        if (reservationHistoryBean != null){
            initViews();
        }else{
            showToast("无法获取挂号信息");
            finish();
            return;
        }
    }

    private void initViews() {
        boolean isValid = false;
        Date date = JDUtils.parseDate(reservationHistoryBean.getRequest_date(), "yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        if (date != null){
            c.setTime(date);
        }
        c.add(Calendar.DAY_OF_YEAR, 1);
        if (date != null && c.getTime().compareTo(new Date()) > 0){
            isValid = true;
        }
        if (reservationHistoryBean.getVisit_flag() == 0 || reservationHistoryBean.getVisit_flag() == 1 && isValid){
            if (reservationHistoryBean.getVisit_flag() == 0){
                btnCancel.setText("去支付");
            }else if (reservationHistoryBean.getVisit_flag() == 1){
                btnCancel.setText("取消挂号");
            }
            btnCancel.setVisibility(View.VISIBLE);
        }else{
            btnCancel.setVisibility(View.GONE);
        }
        if (!TextUtils.isEmpty(reservationHistoryBean.getRemark())){
            tvReservationStatus.setText(reservationHistoryBean.getRemark());
            tvReservationStatus.setVisibility(View.VISIBLE);
        }else{
            tvReservationStatus.setVisibility(View.GONE);
        }
        if(reservationHistoryBean.getVisit_flag() == 1 || reservationHistoryBean.getVisit_flag() == 9){
            panelBalance.setVisibility(View.VISIBLE);
            tvBalance.setText("余        额：" + JDUtils.getMoneyFormat(reservationHistoryBean.getBalance()));
            tvBalance.setVisibility(View.VISIBLE);
        }else{
            panelBalance.setVisibility(View.GONE);
            tvBalance.setVisibility(View.GONE);
        }
        if (reservationHistoryBean.getClinic_flag() == 0){
            tvType.setText("号码类别：普通号");
            panelDoctorArea.setVisibility(View.GONE);
        }else{
            panelDoctorArea.setVisibility(View.VISIBLE);
            tvType.setText("号码类别：专家号");
            tvDoctorName.setText("医        生：" + reservationHistoryBean.getDoctor_name());
        }
        tvState.setText("状        态：" + reservationHistoryBean.getVisit_name());
        tvMoney.setText("费        用：" + reservationHistoryBean.getCharge_price() + "元");
        if (TextUtils.isEmpty(reservationHistoryBean.getGh_sequence())){
            tvDate.setText("预约时间：" + reservationHistoryBean.getRequest_date() + "   " + reservationHistoryBean.getTime_name());
        }else{
            tvDate.setText("预约时间：" + reservationHistoryBean.getRequest_date() + "   " + reservationHistoryBean.getTime_name() + "   " +  reservationHistoryBean.getGh_sequence() + "号");
        }
//        if (!TextUtils.isEmpty(application.userManager.getUserBean().getHospital().getAddress())){
//            tvPlace.setText("医院地址：" + application.userManager.getUserBean().getHospital().getAddress().trim());
//        }else{
//            tvPlace.setText("医院地址：");
//        }
//        if (!TextUtils.isEmpty(application.userManager.getUserBean().getHospital().getTransportation())) {
//            tvTransportation.setText("交通方式：" + application.userManager.getUserBean().getHospital().getTransportation().trim());
//        }else{
//            tvPlace.setText("交通方式：");
//        }
    }

    @OnClick(R.id.btn_history)
    void showHistory(){
        Intent intent = new Intent(this, OutReservationHistoryActivity.class);
        startActivity(intent);
        finish();
    }


    @OnClick(R.id.btn_cancel)
    void cancelReservation(){
        if (reservationHistoryBean.getVisit_flag() == 0){
//            Intent intent = new Intent(OutReservationDetailActivity.this, PayConfirmActivity.class);
//            intent.putExtra(PayConfirmActivity.KEY_OUT_RESERVATION_BEAN, reservationHistoryBean);
//            startActivity(intent);
            finish();
        }else if (reservationHistoryBean.getVisit_flag() == 1){

            final FanrAlertDialog dialog = FanrAlertDialog.getInstance();
//            dialog.showAlertContent(getSupportFragmentManager(), "确定要取消该挂号么？", new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    dialog.dismiss();
//                    setLoadingViewState(JDLoadingView.STATE_LOADING);
//                    JDHttpClient.getInstance().reqOutCancelReservation(OutReservationDetailActivity.this, reservationHistoryBean.getPatient_id(), reservationHistoryBean.getGh_sequence(), reservationHistoryBean.getRequest_date(),
//                            reservationHistoryBean.getRecord_sn(), application.userManager.getUserBean().getVisitCard(), reservationHistoryBean.getOrder_id(), reservationHistoryBean.getHis_order_id(), new JDHttpResponseHandler<OutReservationHistoryBean>(new TypeReference<BaseBean<OutReservationHistoryBean>>(){}){
//                                @Override
//                                public void onRequestCallback(BaseBean<OutReservationHistoryBean> result) {
//                                    super.onRequestCallback(result);
//                                    dismissLoadingView();
//                                    if (result.isSuccess()){
//                                        isNewReservation = false;
//                                        reservationHistoryBean.setRemark(result.getData().getRemark());
//                                        reservationHistoryBean.setVisit_flag(9);
//                                        reservationHistoryBean.setBalance(result.getData().getBalance());
//                                        showToast(reservationHistoryBean.getRemark());
//                                        JDUtils.sendLocationBroadcast(IntentConstants.ACTION_RESERVATION_REFRESH);
//                                        initViews();
//                                    }else{
//                                        showToast(result.getMessage());
//                                    }
//                                }
//                            });
//                }
//            });
        }
    }


    void payMoney(){
        Map<String, Object> map = new HashMap<>();
        map.put("request_date", reservationHistoryBean.getRequest_date());
        map.put("record_sn", reservationHistoryBean.getRecord_sn());
        map.put("doctor_sn", reservationHistoryBean.getDoctor_sn());
        map.put("time_code", reservationHistoryBean.getTime_code());
        map.put("time_name", reservationHistoryBean.getTime_name());
        map.put("clinic_flag", reservationHistoryBean.getClinic_flag());
        map.put("unit_type", reservationHistoryBean.getUnit_type());
//        map.put("card_no", application.userManager.getUserBean().getVisitCard());
//        map.put("name", application.userManager.getUserBean().getRealname());
//        map.put("social_no", application.userManager.getUserBean().getIdNo());
//        map.put("mobile", application.userManager.getUserBean().getPhone());
//        map.put("order_id", reservationHistoryBean.getOrder_id());
        setLoadingViewState(JDLoadingView.STATE_LOADING);
        JDHttpClient.getInstance().reqHisRest(this, HttpConstants.REQUEST_OUT_RESERVATION_PAY_URL, map, new JDHttpResponseHandler<OutReservationHistoryBean>(new TypeReference<BaseBean<OutReservationHistoryBean>>() {
        }) {
            @Override
            public void onRequestCallback(final BaseBean<OutReservationHistoryBean> result) {
                super.onRequestCallback(result);
                dismissLoadingView();
                if (result.isSuccess() && result.getData() != null) {
                    reservationHistoryBean = result.getData();
                    showToast(result.getData().getRemark());
                    JDUtils.sendLocationBroadcast(IntentConstants.ACTION_RESERVATION_REFRESH);
                    initViews();
                } else {
                    showToast(result.getMessage());
                }
            }
        });
    }
}
