package com.chengsi.weightcalc.activity;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;

import com.alibaba.fastjson.TypeReference;
import com.chengsi.weightcalc.R;
import com.chengsi.weightcalc.adapter.SocialStreamAdapter;
import com.chengsi.weightcalc.bean.BaseBean;
import com.chengsi.weightcalc.bean.GoodNewsBean;
import com.chengsi.weightcalc.http.JDHttpClient;
import com.chengsi.weightcalc.http.JDHttpResponseHandler;
import com.chengsi.weightcalc.utils.UIUtils;
import com.chengsi.weightcalc.widget.FanrRefreshListView;
import com.chengsi.weightcalc.widget.JDLoadingView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.InjectView;
import cn.jiadao.corelibs.utils.ListUtils;

public class GoodNewsActivity extends BaseActivity {

    @InjectView(R.id.listview)
    FanrRefreshListView listView;
    private List<GoodNewsBean> goodNewsBeanList = new ArrayList<>();
    private SocialStreamAdapter mAdapter = null;
    private List<Map<String, Object>> dataSource = new ArrayList<>();
    private static final int[] RESOUCE = {R.layout.item_good_news};
    private String[] from = {"goodnews"};
    private int[] to = {R.id.tv_good_news};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_good_news);
        ButterKnife.inject(this);

        initViews();
    }

    private void initViews() {
        HashMap<Integer, String[]> fromMap = new HashMap<Integer, String[]>();
        fromMap.put(RESOUCE[0], from);
        HashMap<Integer, int[]> toMap = new HashMap<Integer, int[]>();
        toMap.put(RESOUCE[0], to);
        mAdapter = new SocialStreamAdapter(this, dataSource, RESOUCE, fromMap, toMap, 0, UIUtils.convertDpToPixel(3, this));
        listView.setAdapter(mAdapter);
        listView.setOnPullRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                goodNewsBeanList.clear();
                dataSource.clear();
                mAdapter.notifyDataSetChanged();
                initDataSource();
            }
        });

        listView.setCanLoadMore(false);
        listView.setPullRefreshing(true);
        initDataSource();
    }

    protected void initDataSource() {
        setLoadingViewState(JDLoadingView.STATE_LOADING);
        JDHttpClient.getInstance().reqGoodNews(this, new JDHttpResponseHandler<List<GoodNewsBean>>(new TypeReference<BaseBean<List<GoodNewsBean>>>() {
        }) {
            @Override
            public void onRequestCallback(BaseBean<List<GoodNewsBean>> result) {
                super.onRequestCallback(result);
                listView.setPullRefreshing(false);
                dismissLoadingView();
                if (result.isSuccess()) {
                    goodNewsBeanList = result.getData();
                    if (ListUtils.isEmpty(result.getData())) {
                        setLoadingViewState(JDLoadingView.STATE_EMPTY);
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
        if (!ListUtils.isEmpty(goodNewsBeanList)) {

            for (final GoodNewsBean bean : goodNewsBeanList) {
                Map<String, Object> map = new HashMap<>();
                map.put(from[0], bean.getDescription());
                dataSource.add(map);
            }
        }
        mAdapter.notifyDataSetChanged();
    }
}
