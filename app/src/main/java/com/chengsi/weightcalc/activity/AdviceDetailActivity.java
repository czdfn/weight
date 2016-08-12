package com.chengsi.weightcalc.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.AdapterView;

import com.alibaba.fastjson.TypeReference;
import com.chengsi.weightcalc.R;
import com.chengsi.weightcalc.adapter.SocialStreamAdapter;
import com.chengsi.weightcalc.bean.AdviceBean;
import com.chengsi.weightcalc.bean.BaseBean;
import com.chengsi.weightcalc.http.JDHttpClient;
import com.chengsi.weightcalc.http.JDHttpResponseHandler;
import com.chengsi.weightcalc.utils.UIUtils;
import com.chengsi.weightcalc.widget.FanrRefreshListView;
import com.chengsi.weightcalc.widget.JDLoadingView;
import com.chengsi.weightcalc.widget.PreferenceRightDetailView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.InjectView;
import cn.jiadao.corelibs.utils.ListUtils;

public class AdviceDetailActivity extends BaseActivity {

    public static final String KEY_ADVICE_BEAN = "KEY_ADVICE_BEAN";

    @InjectView(R.id.listview)
    FanrRefreshListView listView;
    private List<AdviceBean> adviceBeanList = new ArrayList<>();
    private SocialStreamAdapter mAdapter = null;
    private List<Map<String, Object>> dataSource = new ArrayList<>();
    private static final int[] RESOUCE = {R.layout.item_advice_detail};
    private String[] from = {"name", "使用方式", "day"};
    private int[] to = {R.id.tv_advice_name, R.id.tv_advice_frequency, R.id.tv_advice_days};

    @InjectView(R.id.item_advice_info)
    PreferenceRightDetailView itemInfo;

    private AdviceBean adviceBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advice_detail);
        ButterKnife.inject(this);

        adviceBean = (AdviceBean) getIntent().getSerializableExtra(KEY_ADVICE_BEAN);
        if (adviceBean == null) {
            finish();
            return;
        }

        initViews();
    }

    private void initViews() {
        itemInfo.setTitle("开立医生：" + adviceBean.getDoctorName());
        itemInfo.setContent(adviceBean.getBillingDate());

        HashMap<Integer, String[]> fromMap = new HashMap<Integer, String[]>();
        fromMap.put(RESOUCE[0], from);
        HashMap<Integer, int[]> toMap = new HashMap<Integer, int[]>();
        toMap.put(RESOUCE[0], to);
        mAdapter = new SocialStreamAdapter(this, dataSource, RESOUCE, fromMap, toMap, 0, UIUtils.convertDpToPixel(3, this));
        listView.setAdapter(mAdapter);
        listView.setOnPullRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                adviceBeanList.clear();
                dataSource.clear();
                mAdapter.notifyDataSetChanged();
                initDataSource();
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(AdviceDetailActivity.this, AdviceDetailActivity.class);
                intent.putExtra("num", adviceBeanList.get(position).getNum());
                startActivity(intent);
            }
        });

        listView.setCanLoadMore(false);
        listView.setPullRefreshing(true);
        initDataSource();
    }

    protected void initDataSource() {
        setLoadingViewState(JDLoadingView.STATE_LOADING);
        JDHttpClient.getInstance().reqAdviceDetail(this, adviceBean.getNum(), adviceBean.getId(), new JDHttpResponseHandler<List<AdviceBean>>(new TypeReference<BaseBean<List<AdviceBean>>>() {
        }) {
            @Override
            public void onRequestCallback(BaseBean<List<AdviceBean>> result) {
                super.onRequestCallback(result);
                listView.setPullRefreshing(false);
                dismissLoadingView();
                if (result.isSuccess()) {
                    adviceBeanList = result.getData();
                    if (ListUtils.isEmpty(result.getData())) {
                        setLoadingViewState(JDLoadingView.STATE_EMPTY);
                    }else{
                        adviceBean = adviceBeanList.get(0);
                        itemInfo.setTitle("开立医生：" + adviceBean.getDoctorName());
                        itemInfo.setContent(adviceBean.getBillingDate());
                    }
                    parseData();
                } else {
                    setLoadingViewState(JDLoadingView.STATE_FAILED);
                    showToast(result.getMessage());
                }
            }
        });
    }

    private void parseData() {
        if (!ListUtils.isEmpty(adviceBeanList)) {
            for (final AdviceBean bean : adviceBeanList) {
                Map<String, Object> map = new HashMap<>();
                map.put(from[0], bean.getContent());
                map.put(from[1], bean.getUsage());
                map.put(from[2], String.valueOf(bean.getDay()));
                dataSource.add(map);
            }
        }
        mAdapter.notifyDataSetChanged();
    }

}
