package com.chengsi.weightcalc.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.TypeReference;
import com.chengsi.weightcalc.R;
import com.chengsi.weightcalc.adapter.SocialStreamAdapter;
import com.chengsi.weightcalc.bean.BaseBean;
import com.chengsi.weightcalc.bean.DoctorBean;
import com.chengsi.weightcalc.bean.HospitalBean;
import com.chengsi.weightcalc.bean.OutDoctorArrangeBean;
import com.chengsi.weightcalc.bean.OutDoctorBean;
import com.chengsi.weightcalc.bean.ReservationDoctorListBean;
import com.chengsi.weightcalc.bean.ReservationItem;
import com.chengsi.weightcalc.bean.ReservationTime;
import com.chengsi.weightcalc.bean.ReservationTimeListBean;
import com.chengsi.weightcalc.bean.ReservationType;
import com.chengsi.weightcalc.http.JDHttpClient;
import com.chengsi.weightcalc.http.JDHttpResponseHandler;
import com.chengsi.weightcalc.listener.OnContinuousClickListener;
import com.chengsi.weightcalc.utils.JDUtils;
import com.chengsi.weightcalc.utils.UIUtils;
import com.chengsi.weightcalc.utils.imageloader.ImageLoaderUtils;
import com.chengsi.weightcalc.widget.FanrRefreshListView;
import com.chengsi.weightcalc.widget.JDLoadingView;
import com.chengsi.weightcalc.widget.dialog.SelectReservationTimeDialog;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.InjectView;
import cn.jiadao.corelibs.utils.ListUtils;

public class ReservationDoctorActivity extends BaseActivity {


    @InjectView(R.id.listview)
    FanrRefreshListView listView;
    private List<DoctorBean> doctorBeanList = new ArrayList<>();
    private SocialStreamAdapter mAdapter = null;
    private List<Map<String, Object>> dataSource = new ArrayList<>();
    private static final int[] RESOUCE = {R.layout.item_reservation_doctor};
    private String[] from = {ImageLoaderUtils.IMAGE_HEAD_PATH, "name", "brief", "isBtnVisible", "reservationListener", "btnVisible", "remainNum", "numVisible"};
    private int[] to = {R.id.iv_doctor_head, R.id.tv_doctor_name, R.id.tv_doctor_duty, R.id.tv_doctor_duty, R.id.panel_reservation_btn, R.id.panel_reservation_btn, R.id.tv_reservation_num, R.id.tv_reservation_num};


    @InjectView(R.id.panel_weak_area)
    LinearLayout panelWeekArea;

    private  int curSelectIndex = 0;
    private String curSelectDate;
    private ReservationItem curItem;
    private ReservationType curType;
    private DoctorBean curDoctor;
    private List<String> fullTimeList = new ArrayList<>();


    private String mCurDepartment;
    private String mCurCategory;
    private HospitalBean mCurHospitalBean;
    private OutDoctorBean curOutDoctor;

    private boolean isOutRegister =false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservation_doctor);
        setMyTitle(R.string.title_activity_reservation_doctor);
        ButterKnife.inject(this);

//        mCurHospitalBean = application.userManager.getUserBean().getHospital();
        if (mCurHospitalBean == null || !mCurHospitalBean.isOutRegister()){
            curItem = (ReservationItem) getIntent().getSerializableExtra(ReservationActivity.KEY_RESERVATION_PROGRAM);
            curType = (ReservationType) getIntent().getSerializableExtra(ReservationActivity.KEY_RESERVATION_TYPE);
        }else{
            isOutRegister = true;
            mCurDepartment = getIntent().getStringExtra(ReservationActivity.KEY_RESERVATION_DEPARTMENT);
            mCurCategory = getIntent().getStringExtra(ReservationActivity.KEY_RESERVATION_CATEGORY);
        }
        initViews();
    }

    private void initViews() {
        initWeeView();
        HashMap<Integer, String[]> fromMap = new HashMap<Integer, String[]>();
        fromMap.put(RESOUCE[0], from);
        HashMap<Integer, int[]> toMap = new HashMap<Integer, int[]>();
        toMap.put(RESOUCE[0], to);
        mAdapter = new SocialStreamAdapter(this, dataSource, RESOUCE, fromMap, toMap, 0, UIUtils.convertDpToPixel(3, this), ImageLoaderUtils.headDisplayOpts);
        listView.setAdapter(mAdapter);
        listView.setOnPullRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                doctorBeanList.clear();
                dataSource.clear();
                mAdapter.notifyDataSetChanged();
                initDataSource();
            }
        });
        listView.setCanLoadMore(false);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });
        setEmptyViewToListView(listView);
        initDataSource();
    }

    private void initWeeView() {
        panelWeekArea.removeAllViews();
        for (int i = 0; i < 7; i ++){
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.DAY_OF_YEAR, i);
            View view = mInflater.inflate(R.layout.view_reservation_date, null);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT);
            params.weight = 1;
            params.gravity = Gravity.CENTER;
            panelWeekArea.addView(view, params);
            TextView tvDay = (TextView) view.findViewById(R.id.tv_week_day);
            TextView tvDate = (TextView) view.findViewById(R.id.tv_week_date);
            ImageView ivFull = (ImageView) view.findViewById(R.id.iv_full);

            tvDay.setText(JDUtils.getWeekday(calendar.getTime()));
            tvDate.setText(String.valueOf(calendar.get(Calendar.DAY_OF_MONTH)));
            DateItem dateItem = new DateItem();
            dateItem.setDate(calendar.getTime());
            dateItem.setIndex(i);
            view.setTag(dateItem);
            String dateStr = JDUtils.formatDate(dateItem.getDate(), "yyyy-MM-dd");
            if (fullTimeList.contains(dateStr)){
                ivFull.setVisibility(View.VISIBLE);
            }else{
                ivFull.setVisibility(View.GONE);
            }
            if (curSelectIndex == i){
                view.setSelected(true);
                curSelectDate = dateStr;
            }else{
                view.setSelected(false);
            }
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DateItem item = (DateItem) v.getTag();
                    curSelectIndex = item.getIndex();
                    initWeeView();
                    initDataSource();
                }
            });
        }
    }

    protected void initDataSource() {
        setLoadingViewState(JDLoadingView.STATE_LOADING);
        dataSource.clear();
        mAdapter.notifyDataSetChanged();
        if (isOutRegister){
            final String searchDate = curSelectDate;
            JDHttpClient.getInstance().reqOutReservationDoctorInfo(this, curSelectDate, curSelectDate, mCurDepartment, mCurCategory, new JDHttpResponseHandler<List<OutDoctorBean>>(new TypeReference<BaseBean<List<OutDoctorBean>>>() {
            }) {
                @Override
                public void onRequestCallback(BaseBean<List<OutDoctorBean>> result) {
                    super.onRequestCallback(result);
                    listView.setPullRefreshing(false);
                    dismissLoadingView();
                    if (searchDate != null && searchDate.equals(curSelectDate)){
                        if (result.isSuccess()) {
                            parseOutDoctorData(result.getData());
                        } else {
                            showToast(result.getMessage());
                            setEmptyViewState(JDLoadingView.STATE_FAILED);
                        }
                    }
                }
            });
        }else{
            String itemId = curItem == null ? null : String.valueOf(curItem.getItemId());
            String typeId = curType == null ? null : String.valueOf(curType.getTypeId());
            JDHttpClient.getInstance().reqReservationDoctorInfo(this, curSelectDate, itemId, typeId, new JDHttpResponseHandler<ReservationDoctorListBean>(new TypeReference<BaseBean<ReservationDoctorListBean>>() {
            }) {
                @Override
                public void onRequestCallback(BaseBean<ReservationDoctorListBean> result) {
                    super.onRequestCallback(result);
                    dismissLoadingView();
                    listView.setPullRefreshing(false);
                    if (result.isSuccess()) {
                        doctorBeanList = result.getData().getDoctorDtoS();
                        if (!ListUtils.isEmpty(result.getData().getFullTime())) {
                            fullTimeList = result.getData().getFullTime();
                        } else {
                            fullTimeList.clear();
                        }
                        parseData();
                    } else {
                        doctorBeanList.clear();
                        parseData();
                        showToast(result.getMessage());
                    }
                }
            });
        }

    }

    private void showReservationTime(){
        setLoadingViewState(JDLoadingView.STATE_LOADING);
        JDHttpClient.getInstance().reqReservationTimeInfo(this, curSelectDate, String.valueOf(curDoctor.getId()), new JDHttpResponseHandler<ReservationTimeListBean>(new TypeReference<BaseBean<ReservationTimeListBean>>(){}){
            @Override
            public void onRequestCallback(BaseBean<ReservationTimeListBean> result) {
                super.onRequestCallback(result);
                dismissLoadingView();
                if (result.isSuccess()){
                    ReservationTimeListBean bean = result.getData();
                    if (bean == null || ListUtils.isEmpty(bean.getArrangeS())){
                        showToast(R.string.err_no_reservation_time);
                    }else{
                        final List<ReservationTime> timeList = bean.getArrangeS();
                        final SelectReservationTimeDialog dialog = SelectReservationTimeDialog.getInstance();
                        dialog.showSelect(getSupportFragmentManager(), timeList, new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                dialog.dismiss();
                                ReservationTime time = timeList.get(position);
                                Intent intent = new Intent();
                                intent.putExtra(ReservationActivity.KEY_RESERVATION_DOCTOR, curDoctor);
                                intent.putExtra(ReservationActivity.KEY_RESERVATION_TIME, time);
                                intent.putExtra(ReservationActivity.KEY_RESERVATION_DATE, curSelectDate);
                                setResult(RESULT_OK, intent);
                                finish();
                            }
                        });
                    }
                }else{
                    showToast(result.getMessage());
                }
            }
        });
    }

    private void showOutReservationTime(){
        setLoadingViewState(JDLoadingView.STATE_LOADING);
        JDHttpClient.getInstance().reqOutReservationTimeInfo(this, curSelectDate, mCurDepartment, mCurCategory, curOutDoctor.getDoctor_sn(), new JDHttpResponseHandler<List<OutDoctorArrangeBean>>(new TypeReference<BaseBean<List<OutDoctorArrangeBean>>>(){}){
            @Override
            public void onRequestCallback(BaseBean<List<OutDoctorArrangeBean>> result) {
                super.onRequestCallback(result);
                dismissLoadingView();
                if (result.isSuccess()) {
                    final List<OutDoctorArrangeBean> list = result.getData();
                    if (ListUtils.isEmpty(list)) {
                        showToast(R.string.err_no_reservation_time);
                    } else {
                        Collections.sort(list);
                        final SelectReservationTimeDialog dialog = SelectReservationTimeDialog.getInstance();
                        dialog.showOutSelect(getSupportFragmentManager(), list, new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                dialog.dismiss();
                                OutDoctorArrangeBean time = list.get(position);
                                if (time.getSurplus_numbers() > 0){
                                    Intent intent = new Intent();
                                    intent.putExtra(ReservationActivity.KEY_RESERVATION_OUT_ARRANGE, time);
                                    intent.putExtra(ReservationActivity.KEY_RESERVATION_DATE, curSelectDate);
                                    setResult(RESULT_OK, intent);
                                    finish();
                                }else{
                                    showToast("该时段没有可预约的名额！");
                                }
                            }
                        });
                    }
                } else {
                    showToast(result.getMessage());
                }
            }
        });
    }


    private void parseData(){
        dataSource.clear();
        if (!ListUtils.isEmpty(doctorBeanList)) {

            for (final DoctorBean bean : doctorBeanList) {
                Map<String, Object> map = new HashMap<>();
                map.put(from[0], JDUtils.getRemoteImagePath(bean.getHeadImg()));
                map.put(from[1], bean.getRealName());
                map.put(from[2], bean.getBrief());
                map.put(from[3], true);
                map.put(from[4], new OnContinuousClickListener() {
                    @Override
                    public void onContinuousClick(View v) {
                        curDoctor = bean;
                        showReservationTime();
                    }
                });
                map.put(from[5], true);
                map.put(from[7], false);
                dataSource.add(map);
            }
            dismissEmptyView();
        }else{
            setEmptyViewState(JDLoadingView.STATE_EMPTY, "查询时间段内没有医生！");
        }
        mAdapter.notifyDataSetChanged();
    }

    private void parseOutDoctorData(List<OutDoctorBean> list){
        dataSource.clear();
        if (!ListUtils.isEmpty(list)) {

            for (final OutDoctorBean bean : list) {
                Map<String, Object> map = new HashMap<>();
                map.put(from[0], JDUtils.getRemoteImagePath(bean.getHeadImg()));
                map.put(from[1], bean.getDoctor_name());
                map.put(from[2], bean.getBrief());
                map.put(from[3], true);
                map.put(from[4], new OnContinuousClickListener() {
                    @Override
                    public void onContinuousClick(View v) {
                        curOutDoctor = bean;
                        showOutReservationTime();
                    }
                });
                map.put(from[5], true);

                map.put(from[6], "挂号费：" + bean.getCharge_price() + "元");
                map.put(from[7], true);

                dataSource.add(map);
            }
            dismissEmptyView();
        } else {
            setEmptyViewState(JDLoadingView.STATE_EMPTY, "查询时间段内没有医生！");
        }
        mAdapter.notifyDataSetChanged();

    }

    private class DateItem{
        private int index;
        private Date date;

        public int getIndex() {
            return index;
        }

        public void setIndex(int index) {
            this.index = index;
        }

        public Date getDate() {
            return date;
        }

        public void setDate(Date date) {
            this.date = date;
        }
    }
}
