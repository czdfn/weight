package com.chengsi.weightcalc.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.AdapterView;

import com.alibaba.fastjson.TypeReference;
import com.chengsi.weightcalc.R;
import com.chengsi.weightcalc.adapter.SocialStreamAdapter;
import com.chengsi.weightcalc.bean.BaseBean;
import com.chengsi.weightcalc.bean.CheckResultBean;
import com.chengsi.weightcalc.http.JDHttpClient;
import com.chengsi.weightcalc.http.JDHttpResponseHandler;
import com.chengsi.weightcalc.utils.JDUtils;
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

public class CheckListActivity extends BaseActivity {

    @InjectView(R.id.listview)
    FanrRefreshListView listView;
    private List<CheckResultBean> checkResultBeanList = new ArrayList<>();
    private SocialStreamAdapter mAdapter = null;
    private List<Map<String, Object>> dataSource = new ArrayList<>();
    private static final int[] RESOUCE = {R.layout.item_check_list};
    private String[] from = {"date", "doctor"};
    private int[] to = {R.id.tv_check_date, R.id.tv_check_doctor};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_list);
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
                checkResultBeanList.clear();
                dataSource.clear();
                mAdapter.notifyDataSetChanged();
                initDataSource();
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(CheckListActivity.this, CheckDetailActivity.class);
                intent.putExtra(CheckDetailActivity.KEY_CHECK_RESULT_BEAN, checkResultBeanList.get(position));
                startActivity(intent);
            }
        });

        listView.setCanLoadMore(false);
        listView.setPullRefreshing(true);
        initDataSource();
    }

    protected void initDataSource() {
        if (ListUtils.isEmpty(checkResultBeanList)){
            setLoadingViewState(JDLoadingView.STATE_LOADING);
        }else{
            listView.setPullRefreshing(true);
        }
        JDHttpClient.getInstance().reqCheckResultList(this, "online",new JDHttpResponseHandler<List<CheckResultBean>>(new TypeReference<BaseBean<List<CheckResultBean>>>() {
        }) {
            @Override
            public void onRequestCallback(BaseBean<List<CheckResultBean>> result) {
                super.onRequestCallback(result);
                listView.setPullRefreshing(false);
                dismissLoadingView();
                if (result.isSuccess()) {
                    checkResultBeanList = result.getData();
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
        if (!ListUtils.isEmpty(checkResultBeanList)) {

            for (final CheckResultBean bean : checkResultBeanList) {
                Map<String, Object> map = new HashMap<>();
                map.put(from[0], JDUtils.formatDate(bean.getCheckDate(), "yyyy-MM-dd HH:mm:ss"));
                CheckResultBean.CheckType checkType = CheckResultBean.CheckType.getCheckType(bean.getCheckType());
                if (checkType != null) {
                    map.put(from[1], checkType.getDesc());
                } else {
                    map.put(from[1], "");
                }
                dataSource.add(map);
            }
        }
        mAdapter.notifyDataSetChanged();
    }
}
