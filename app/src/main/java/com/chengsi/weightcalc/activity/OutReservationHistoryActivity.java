package com.chengsi.weightcalc.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import com.chengsi.weightcalc.R;
import com.chengsi.weightcalc.adapter.SocialStreamAdapter;
import com.chengsi.weightcalc.bean.OutReservationHistoryBean;
import com.chengsi.weightcalc.bean.ReservationHistoryBean;
import com.chengsi.weightcalc.constants.IntentConstants;
import com.chengsi.weightcalc.utils.JDUtils;
import com.chengsi.weightcalc.utils.imageloader.ImageLoaderUtils;
import com.chengsi.weightcalc.widget.FanrRefreshListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.InjectView;
import cn.jiadao.corelibs.utils.ListUtils;

public class OutReservationHistoryActivity extends BaseActivity {

    @InjectView(R.id.listview)
    FanrRefreshListView listView;
    private List<ReservationHistoryBean> historyBeanList = new ArrayList<>();
    private SocialStreamAdapter mAdapter = null;
    private List<Map<String, Object>> dataSource = new ArrayList<>();
    private static final int[] RESOUCE = {R.layout.item_reservation_out_order};
    private String[] from = {"name", "order_status", "time", "money", "status_color"};
    private int[] to = {R.id.tv_doctor_name, R.id.tv_reservation_status, R.id.tv_reservation_time, R.id.tv_reservation_money, R.id.tv_reservation_status};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_out_reservation_history);
        ButterKnife.inject(this);
        HashMap<Integer, String[]> fromMap = new HashMap<Integer, String[]>();
        fromMap.put(RESOUCE[0], from);
        HashMap<Integer, int[]> toMap = new HashMap<Integer, int[]>();
        toMap.put(RESOUCE[0], to);
        mAdapter = new SocialStreamAdapter(this, dataSource, RESOUCE, fromMap, toMap, 0, 0, ImageLoaderUtils.headDisplayOpts);
        listView.setAdapter(mAdapter);
        listView.setOnPullRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                historyBeanList.clear();
                dataSource.clear();
                mAdapter.notifyDataSetChanged();
                initDataSource();
            }
        });

        mAdapter.setViewBinder(new SocialStreamAdapter.ViewBinder() {
            @Override
            public boolean setViewValue(View view, Object data, Object comment) {
                if (view.getId() == R.id.tv_reservation_status && data != null && data instanceof Integer) {

                    int status = (int) data;
                    TextView tv = (TextView) view;
                    if (status == 0) {
                        tv.setTextColor(getResources().getColor(R.color.color_theme));
                        tv.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.tobepaid), null, null, null);
                    } else {
                        tv.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.icon_finish), null, null, null);
                        tv.setTextColor(getResources().getColor(R.color.text_74));
                    }
                    return true;
                }
                return false;
            }
        });
        listView.setCanLoadMore(false);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(OutReservationHistoryActivity.this, OutReservationDetailActivity.class);
                OutReservationHistoryBean bean = (OutReservationHistoryBean) dataSource.get(position).get("data");
                intent.putExtra(OutReservationDetailActivity.KEY_OUT_RESERVATION_BEAN, bean);
                startActivity(intent);
            }
        });

        JDUtils.registerLocalReceiver(mReceiver, IntentConstants.ACTION_RESERVATION_REFRESH);
        initDataSource();
    }

    protected void initDataSource() {
//        setLoadingViewState(JDLoadingView.STATE_LOADING);
//        JDHttpClient.getInstance().reqOutReservationHistory(this, "99", application.userManager.getUserBean().getVisitCard(), new JDHttpResponseHandler<List<OutReservationHistoryBean>>(new TypeReference<BaseBean<List<OutReservationHistoryBean>>>() {
//        }) {
//            @Override
//            public void onRequestCallback(BaseBean<List<OutReservationHistoryBean>> result) {
//                super.onRequestCallback(result);
//                listView.setPullRefreshing(false);
//                dismissLoadingView();
//                if (result.isSuccess()) {
//                    listView.setPullRefreshing(false);
//                    dataSource.clear();
//                    parseData(result.getData());
//                    if (ListUtils.isEmpty(dataSource)){
//                        setLoadingViewState(JDLoadingView.STATE_EMPTY);
//                    }
//                } else {
//                    setLoadingViewState(JDLoadingView.STATE_FAILED);
//                    showToast(result.getMessage());
//                }
//            }
//        });
    }

    private void parseData(List<OutReservationHistoryBean> addList) {
        if (!ListUtils.isEmpty(addList)) {

            for (final OutReservationHistoryBean bean : addList) {
                Map<String, Object> map = new HashMap<>();
                map.put(from[0], bean.getClinic_flag() == 0 ? "普通号" : bean.getDoctor_name());
                map.put(from[1], bean.getVisit_name());
                map.put(from[2], bean.getRequest_date() + "   " +  bean.getTime_name());
                if(!TextUtils.isEmpty(bean.getCharge_price())){
                    map.put(from[3], "￥" + String.format(bean.getCharge_price(), ".2f"));
                }else{
                    map.put(from[3], "");
                }
                map.put(from[4], bean.getVisit_flag());
                map.put("data", bean);
                dataSource.add(map);
            }
        }
        mAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mReceiver);
    }

    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(IntentConstants.ACTION_RESERVATION_REFRESH)){
                initDataSource();
            }
        }
    };
}
