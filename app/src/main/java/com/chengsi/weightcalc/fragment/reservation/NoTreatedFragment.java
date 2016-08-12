package com.chengsi.weightcalc.fragment.reservation;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.alibaba.fastjson.TypeReference;
import com.chengsi.weightcalc.R;
import com.chengsi.weightcalc.adapter.SocialStreamAdapter;
import com.chengsi.weightcalc.bean.BaseBean;
import com.chengsi.weightcalc.bean.ListBaseBean;
import com.chengsi.weightcalc.bean.ReservationHistoryBean;
import com.chengsi.weightcalc.fragment.BaseFragment;
import com.chengsi.weightcalc.http.JDHttpClient;
import com.chengsi.weightcalc.http.JDHttpResponseHandler;
import com.chengsi.weightcalc.utils.JDUtils;
import com.chengsi.weightcalc.utils.imageloader.ImageLoaderUtils;
import com.chengsi.weightcalc.widget.FanrRefreshListView;
import com.chengsi.weightcalc.widget.JDLoadingView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.jiadao.corelibs.utils.ListUtils;

/**
 * A simple {@link Fragment} subclass.
 */
public class NoTreatedFragment extends BaseFragment {

    private FanrRefreshListView listView;
    private List<ReservationHistoryBean> historyBeanList = new ArrayList<>();
    private SocialStreamAdapter mAdapter = null;
    private List<Map<String, Object>> dataSource = new ArrayList<>();
    private static final int[] RESOUCE = {R.layout.item_reservation_order};
    private String[] from = {ImageLoaderUtils.IMAGE_HEAD_PATH, "name", "order_status", "time"};
    private int[] to = {R.id.iv_doctor_head, R.id.tv_doctor_name, R.id.iv_order_status, R.id.tv_reservation_time};

    private int curPageNo = 0;

    public NoTreatedFragment() {
    }

    @Override
    public View getContentView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_no_treated, container, false);
        listView = (FanrRefreshListView) view.findViewById(R.id.listview);


        HashMap<Integer, String[]> fromMap = new HashMap<Integer, String[]>();
        fromMap.put(RESOUCE[0], from);
        HashMap<Integer, int[]> toMap = new HashMap<Integer, int[]>();
        toMap.put(RESOUCE[0], to);
        mAdapter = new SocialStreamAdapter(getActivity(), dataSource, RESOUCE, fromMap, toMap, 0, 0, ImageLoaderUtils.headDisplayOpts);
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
        listView.setCanLoadMore(false);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });

        initDataSource();
        return view;
    }

    protected void initDataSource() {
        showLoadingView(JDLoadingView.STATE_LOADING);
        JDHttpClient.getInstance().reqUnTreatedReservation(getActivity(), curPageNo, new JDHttpResponseHandler<ListBaseBean<List<ReservationHistoryBean>>>(new TypeReference<BaseBean<ListBaseBean<List<ReservationHistoryBean>>>>() {
        }) {
            @Override
            public void onRequestCallback(BaseBean<ListBaseBean<List<ReservationHistoryBean>>> result) {
                super.onRequestCallback(result);
                dismissLoadingView();
                listView.setPullRefreshing(false);
                if (result.isSuccess()) {
                    List<ReservationHistoryBean> addList = result.getData().getResult();
                    if (addList != null) {
                        historyBeanList.addAll(addList);
                    }
                    parseData(addList);
                    if (addList != null && addList.size() % result.getData().getPageSize() == 0) {
                        listView.onLoadComplete();
                    } else {
                        listView.onLoadCompleteNoMore(FanrRefreshListView.NoMoreHandler.NO_MORE_LOAD_NOT_SHOW_FOOTER_VIEW);
                    }
                    curPageNo++;
                } else {
                    if (curPageNo == 0) {
                        showLoadingView(JDLoadingView.STATE_FAILED);
                    } else {
                        showToast(result.getMessage());
                    }
                }
            }
        });
    }

    private void parseData(List<ReservationHistoryBean> addList) {
        if (!ListUtils.isEmpty(addList)) {

            for (final ReservationHistoryBean bean : addList) {
                Map<String, Object> map = new HashMap<>();
                map.put(from[0], JDUtils.getRemoteImagePath(bean.getDoctorImg()));
                map.put(from[1], bean.getDoctorName());
                if (bean.getStatus() == 0){
                    map.put(from[2], R.drawable.icon_unregister);
                }else if(bean.getStatus() == 1){
                    map.put(from[2], R.drawable.icon_registered);
                }
                map.put(from[3], bean.getDate() + "    " + bean.getOrderTime());
                dataSource.add(map);
            }
            mAdapter.notifyDataSetChanged();
        }
    }


}
