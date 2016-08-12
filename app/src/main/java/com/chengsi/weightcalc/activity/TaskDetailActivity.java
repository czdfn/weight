package com.chengsi.weightcalc.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.view.View;

import com.alibaba.fastjson.TypeReference;
import com.chengsi.weightcalc.R;
import com.chengsi.weightcalc.adapter.SocialStreamAdapter;
import com.chengsi.weightcalc.bean.AdviceBean;
import com.chengsi.weightcalc.bean.BaseBean;
import com.chengsi.weightcalc.bean.DailyTaskBean;
import com.chengsi.weightcalc.http.JDHttpClient;
import com.chengsi.weightcalc.http.JDHttpResponseHandler;
import com.chengsi.weightcalc.listener.OnContinuousClickListener;
import com.chengsi.weightcalc.utils.UIUtils;
import com.chengsi.weightcalc.widget.FanrRefreshListView;
import com.chengsi.weightcalc.widget.JDLoadingView;
import com.chengsi.weightcalc.widget.dialog.FanrAlertDialog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.InjectView;
import cn.jiadao.corelibs.utils.ListUtils;

public class TaskDetailActivity extends BaseActivity {

    public static final String KEY_IS_TODAY_TASK = "KEY_IS_TODAY_TASK";
    public static final String KEY_TASK = "KEY_TASK";

    private boolean isTodayTask = false;

    @InjectView(R.id.listview)
    FanrRefreshListView listView;
    private List<DailyTaskBean> dailyTaskList = new ArrayList<>();
    private SocialStreamAdapter mAdapter = null;
    private List<Map<String, Object>> dataSource = new ArrayList<>();
    private static final int[] RESOUCE = {R.layout.item_task_detail};
    private String[] from = {"name", "alert", "source", "status", "alertVisible", "sourceClick", "taskStatusClick","taskTime"};
    private int[] to = {R.id.tv_task_name, R.id.tv_task_alert, R.id.tv_task_source, R.id.iv_task_status, R.id.tv_task_alert, R.id.tv_task_source, R.id.iv_task_status, R.id.tv_task_time};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_detail);

        isTodayTask = getIntent().getBooleanExtra(KEY_IS_TODAY_TASK, false);
        if (isTodayTask){
            setMyTitle("今日任务");
        }else{
            DailyTaskBean task = (DailyTaskBean) getIntent().getSerializableExtra(KEY_TASK);
            if (task == null){
                finish();
                showToast("没有该任务信息");
                return;
            }else{
                dailyTaskList.add(task);
            }
        }
        ButterKnife.inject(this);

        initViews();
    }
//
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.menu_archives, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        int id = item.getItemId();
//
//        if (id == R.id.action_send) {
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }

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

        listView.setCanLoadMore(false);
        listView.setPullRefreshing(true);
        initDataSource();
    }

    @Override
    protected void initDataSource() {
        super.initDataSource();

        if (isTodayTask){
            JDHttpClient.getInstance().reqTodayTaskList(this, new JDHttpResponseHandler<List<DailyTaskBean>>(new TypeReference<BaseBean<List<DailyTaskBean>>>(){}){
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
        }else{
            parseData();
        }
    }


    private void parseData() {
        if (!ListUtils.isEmpty(dailyTaskList)) {

            for (final DailyTaskBean bean : dailyTaskList) {
                final Map<String, Object> map = new HashMap<>();
                map.put(from[0], bean.getContent());
                map.put(from[1], "提醒：" + bean.getRemind());
                if (TextUtils.isEmpty(bean.getRemind())){
                    map.put(from[4], false);
                }else{
                    map.put(from[4], true);
                }
                if (bean.isDeal()){
                    map.put(from[3], R.drawable.iv_task_finish);
                }else{
                    map.put(from[3], R.drawable.iv_task_todo);
                }
                if (bean.getType() != null){
                    String type = "任务来源：";
                    if (bean.getType().equals("doctor_advice")){
                        type += "医嘱处方 >>";
                        map.put(from[2], type);
                    }else if (bean.getType().equals("doctor")){
                        type += "医生 >>";
                        map.put(from[2], type);
                    }else if (bean.getType().equals("appointment")){
                        type += "就诊预约 >>";
                        map.put(from[2], type);
                    }else if (bean.getType().equals("operation_notice")){
                        type += "手术通知 >>";
                        map.put(from[2], type);
                    }else if (bean.getType().equals("patient")){
                        type += "本人添加 >>";
                        map.put(from[2], type);
                    }else{
                        map.put(from[2], "");
                    }
                }else{
                    map.put(from[2], "");
                }
                map.put(from[7], bean.getTaskTime());
                map.put(from[5], new OnContinuousClickListener() {
                    @Override
                    public void onContinuousClick(View v) {
                        Intent intent = new Intent(TaskDetailActivity.this, AdviceDetailActivity.class);
                        AdviceBean adviceBean = new AdviceBean();
                        adviceBean.setId(bean.getObjectId());
                        intent.putExtra(AdviceDetailActivity.KEY_ADVICE_BEAN, adviceBean);
                        startActivity(intent);
                    }
                });
                map.put(from[6], new OnContinuousClickListener() {
                    @Override
                    public void onContinuousClick(View v) {
                        if (!bean.isDeal()){
                            final FanrAlertDialog dialog = FanrAlertDialog.getInstance();
                            String title = "确定完成该任务了么？";
                            if (bean.isDeal()){
                                title = "确定要重置该任务么？";
                            }
                            dialog.showAlertContent(getSupportFragmentManager(), title, new OnContinuousClickListener() {
                                @Override
                                public void onContinuousClick(View v) {
                                    dialog.dismiss();
                                    setLoadingViewState(JDLoadingView.STATE_LOADING);
                                    int type = 1;
                                    if (bean.isDeal()){
                                        type = 0;
                                    }
                                    JDHttpClient.getInstance().reqDealTask(TaskDetailActivity.this, bean.getId(), type, new JDHttpResponseHandler<String>(new TypeReference<BaseBean<String>>() {
                                    }) {

                                        @Override
                                        public void onRequestCallback(BaseBean<String> result) {
                                            super.onRequestCallback(result);
                                            dismissLoadingView();
                                            if (result.isSuccess()) {
                                                showToast("操作成功");

                                                bean.setIsDeal(!bean.isDeal());
                                                if (bean.isDeal()){
                                                    map.put(from[3], R.drawable.iv_task_finish);
                                                }else{
                                                    map.put(from[3], R.drawable.iv_task_todo);
                                                }
                                                mAdapter.notifyDataSetChanged();
                                            } else {
                                                showToast(result.getMessage());
                                            }
                                        }
                                    });
                                }
                            });
                        }
                    }
                });
                dataSource.add(map);
            }
        }
        mAdapter.notifyDataSetChanged();
    }
}
