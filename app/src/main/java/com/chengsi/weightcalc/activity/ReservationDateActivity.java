package com.chengsi.weightcalc.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.TypeReference;
import com.chengsi.weightcalc.R;
import com.chengsi.weightcalc.bean.BaseBean;
import com.chengsi.weightcalc.bean.DoctorBean;
import com.chengsi.weightcalc.bean.OutDoctorArrangeBean;
import com.chengsi.weightcalc.bean.ReservationTime;
import com.chengsi.weightcalc.bean.ReservationTimeListBean;
import com.chengsi.weightcalc.http.JDHttpClient;
import com.chengsi.weightcalc.http.JDHttpResponseHandler;
import com.chengsi.weightcalc.utils.JDUtils;
import com.chengsi.weightcalc.widget.FanrRefreshListView;
import com.chengsi.weightcalc.widget.JDLoadingView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import cn.jiadao.corelibs.utils.ListUtils;

public class ReservationDateActivity extends BaseActivity {

    public static final String KEY_IS_OUT_RESERVATION = "KEY_IS_OUT_RESERVATION";

    @InjectView(R.id.listview)
    FanrRefreshListView listView;
    private List dataSource = new ArrayList<>();
    private ReservationTimeAdapter mAdapter = null;

    @InjectView(R.id.panel_weak_area)
    LinearLayout panelWeekArea;

    private int curSelectIndex = 0;
    private String curSelectDate;
    private DoctorBean curDoctor;
    private List<String> fullTimeList = new ArrayList<>();

    private boolean isOutReservation = false;
    private OutDoctorArrangeBean outDoctorArrangeBean;

    private String category;
    private String department;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservation_date);
        setMyTitle(R.string.title_activity_reservation_date);
        ButterKnife.inject(this);

        isOutReservation = getIntent().getBooleanExtra(KEY_IS_OUT_RESERVATION, false);
        if (isOutReservation){
            category = getIntent().getStringExtra(ReservationActivity.KEY_RESERVATION_CATEGORY);
            department = getIntent().getStringExtra(ReservationActivity.KEY_RESERVATION_DEPARTMENT);
            outDoctorArrangeBean = (OutDoctorArrangeBean) getIntent().getSerializableExtra(ReservationActivity.KEY_RESERVATION_DOCTOR);
        }else{
            curDoctor = (DoctorBean) getIntent().getSerializableExtra(ReservationActivity.KEY_RESERVATION_DOCTOR);
        }
        initViews();
    }

    private void initViews() {
        initWeekViews();

        mAdapter = new ReservationTimeAdapter();
        listView.setAdapter(mAdapter);
        listView.setOnPullRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                listView.setPullRefreshing(true);
                dataSource.clear();
                mAdapter.notifyDataSetChanged();
                initDataSource();
            }
        });
        listView.setCanLoadMore(false);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent();
                if (!isOutReservation) {
                    if (dataSource.get(position) instanceof ReservationTime) {
                        ReservationTime time = (ReservationTime) dataSource.get(position);
                        if (time.getNum() <= 0){
                            showToast("该时段没有可预约的名额！");
                            return;
                        }else{
                            intent.putExtra(ReservationActivity.KEY_RESERVATION_TIME, time);
                        }
                    }
                    intent.putExtra(ReservationActivity.KEY_RESERVATION_DATE, curSelectDate);
                } else {
                    if (dataSource.get(position) instanceof OutDoctorArrangeBean) {
                        OutDoctorArrangeBean time = (OutDoctorArrangeBean) dataSource.get(position);
                        intent.putExtra(ReservationActivity.KEY_RESERVATION_OUT_ARRANGE, time);
                        intent.putExtra(ReservationActivity.KEY_RESERVATION_DATE, curSelectDate);
                    }
                }
                setResult(RESULT_OK, intent);
                finish();
            }
        });
        setEmptyViewToListView(listView);
        initDataSource();
    }

    private void initWeekViews() {
        panelWeekArea.removeAllViews();
        for (int i = 0; i < 7; i++) {
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
            if (fullTimeList.contains(dateStr)) {
                ivFull.setVisibility(View.VISIBLE);
            } else {
                ivFull.setVisibility(View.GONE);
            }
            if (curSelectIndex == i) {
                view.setSelected(true);
                curSelectDate = dateStr;
            } else {
                view.setSelected(false);
            }
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DateItem item = (DateItem) v.getTag();
                    curSelectIndex = item.getIndex();
                    initWeekViews();
                    initDataSource();
                }
            });
        }
    }

    protected void initDataSource() {
        setLoadingViewState(JDLoadingView.STATE_LOADING);
        if (!isOutReservation){
            JDHttpClient.getInstance().reqReservationTimeInfo(this, curSelectDate, String.valueOf(curDoctor.getId()), new JDHttpResponseHandler<ReservationTimeListBean>(new TypeReference<BaseBean<ReservationTimeListBean>>() {
            }) {
                @Override
                public void onRequestCallback(BaseBean<ReservationTimeListBean> result) {
                    super.onRequestCallback(result);
                    listView.setPullRefreshing(false);
                    dismissLoadingView();
                    if (result.isSuccess()) {
                        ReservationTimeListBean bean = result.getData();
                        if (bean == null || ListUtils.isEmpty(bean.getArrangeS())) {
                            setEmptyViewState(JDLoadingView.STATE_EMPTY, "查询时间段内没有剩余号源！");
                        }else{
                            dismissEmptyView();
                        }
                        dataSource = bean.getArrangeS();
                        mAdapter.notifyDataSetChanged();
                    } else {
                        dataSource.clear();
                        mAdapter.notifyDataSetChanged();
                        setEmptyViewState(JDLoadingView.STATE_FAILED);
                        showToast(result.getMessage());
                    }
                }
            });
        }else{
            String doctor_sn = outDoctorArrangeBean == null ? "" : outDoctorArrangeBean.getDoctor_sn();
            JDHttpClient.getInstance().reqOutReservationTimeInfo(this, curSelectDate, department, category, doctor_sn, new JDHttpResponseHandler<List<OutDoctorArrangeBean>>(new TypeReference<BaseBean<List<OutDoctorArrangeBean>>>() {
            }) {
                @Override
                public void onRequestCallback(BaseBean<List<OutDoctorArrangeBean>> result) {
                    super.onRequestCallback(result);
                    listView.setPullRefreshing(false);
                    dismissLoadingView();
                    if (result.isSuccess()) {
                        dataSource = result.getData();
                        if (dataSource == null || ListUtils.isEmpty(dataSource)) {
                            setEmptyViewState(JDLoadingView.STATE_EMPTY, "查询时间段内没有剩余号源！");
                        } else {
                            dismissEmptyView();
                        }
                        mAdapter.notifyDataSetChanged();
                    } else {
                        dataSource.clear();
                        mAdapter.notifyDataSetChanged();
                        showToast(result.getMessage());
                        setEmptyViewState(JDLoadingView.STATE_FAILED);
                    }
                }
            });
        }
    }

    private class DateItem {
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

    private class ReservationTimeAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return ListUtils.isEmpty(dataSource) ? 0 : dataSource.size();
        }

        @Override
        public Object getItem(int position) {
            return dataSource.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = mInflater.inflate(R.layout.item_reservation_time, null);
            TextView tvTime = (TextView) view.findViewById(R.id.tv_reservation_time);
            TextView tvNum = (TextView) view.findViewById(R.id.tv_reservation_num);
            if (dataSource.get(position) instanceof ReservationTime){
                ReservationTime reservationTime = (ReservationTime) dataSource.get(position);
                if (reservationTime.getNum() <= 0){
                    tvNum.setText("0");
                    tvNum.setTextColor(getResources().getColor(R.color.text_5e));
                }else{
                    tvNum.setText(String.valueOf(reservationTime.getNum()));
                    tvNum.setTextColor(getResources().getColor(R.color.text_reservation_num));
                }
                tvTime.setText(reservationTime.getTimeValue());
            }else if (dataSource.get(position) instanceof OutDoctorArrangeBean){
                OutDoctorArrangeBean reservationTime = (OutDoctorArrangeBean) dataSource.get(position);
                if (reservationTime.getSurplus_numbers() <= 0){
                    tvNum.setText("0");
                    tvNum.setTextColor(getResources().getColor(R.color.text_5e));
                }else{
                    tvNum.setText(String.valueOf(reservationTime.getSurplus_numbers()));
                    tvNum.setTextColor(getResources().getColor(R.color.text_reservation_num));
                }
                tvTime.setText(reservationTime.getTimeName());
            }
            return view;
        }
    }
}
