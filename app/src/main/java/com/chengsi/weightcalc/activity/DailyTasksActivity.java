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
import com.chengsi.weightcalc.bean.DailyTaskBean;
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

public class DailyTasksActivity extends BaseActivity {

    @InjectView(R.id.listview)
    FanrRefreshListView listView;
    private List<DailyTaskBean> dailyTaskList = new ArrayList<>();
    private SocialStreamAdapter mAdapter = null;
    private List<Map<String, Object>> dataSource = new ArrayList<>();
    private static final int[] RESOUCE = {R.layout.item_daily_task};
    private String[] from = {"content", "status"};
    private int[] to = {R.id.tv_task_content, R.id.iv_task_status};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_tasks);
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
                dailyTaskList.clear();
                dataSource.clear();
                mAdapter.notifyDataSetChanged();
                initDataSource();
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(DailyTasksActivity.this, TaskDetailActivity.class);
                intent.putExtra(TaskDetailActivity.KEY_TASK, dailyTaskList.get(position));
                startActivity(intent);
            }
        });

        listView.setCanLoadMore(false);
        listView.setPullRefreshing(true);
        initDataSource();
    }

    protected void initDataSource() {
        setLoadingViewState(JDLoadingView.STATE_LOADING);
        JDHttpClient.getInstance().reqTaskList(this, new JDHttpResponseHandler<List<DailyTaskBean>>(new TypeReference<BaseBean<List<DailyTaskBean>>>() {
        }) {
            @Override
            public void onRequestCallback(BaseBean<List<DailyTaskBean>> result) {
                super.onRequestCallback(result);
                listView.setPullRefreshing(false);
                dismissLoadingView();
                if (result.isSuccess()) {
                    dailyTaskList = result.getData();
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
        if (!ListUtils.isEmpty(dailyTaskList)) {

            for (final DailyTaskBean bean : dailyTaskList) {
                Map<String, Object> map = new HashMap<>();
                map.put(from[0], bean.getTaskTime() + "  " + bean.getContent());
                map.put(from[1], bean.isDeal());
                dataSource.add(map);
            }
        }
        mAdapter.notifyDataSetChanged();
    }
}
