//package com.chengsi.weightcalc.activity;
//
//import android.os.Bundle;
//import android.support.v4.widget.SwipeRefreshLayout;
//import android.text.TextUtils;
//
//import com.alibaba.fastjson.TypeReference;
//import com.chengsi.weightcalc.R;
//import com.chengsi.weightcalc.adapter.SocialStreamAdapter;
//import com.chengsi.weightcalc.bean.BaseBean;
//import com.chengsi.weightcalc.bean.CheckResultBean;
//import com.chengsi.weightcalc.http.JDHttpClient;
//import com.chengsi.weightcalc.http.JDHttpResponseHandler;
//import com.chengsi.weightcalc.utils.JDUtils;
//import com.chengsi.weightcalc.utils.UIUtils;
//import com.chengsi.weightcalc.widget.FanrRefreshListView;
//import com.chengsi.weightcalc.widget.JDLoadingView;
//import com.chengsi.weightcalc.widget.PreferenceRightDetailView;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//import butterknife.ButterKnife;
//import butterknife.InjectView;
//import cn.jiadao.corelibs.utils.ListUtils;
//
//public class CheckDetailActivity extends BaseActivity {
//
////    public static final String KEY_CHECK_RESULT_BEAN = "KEY_CHECK_RESULT_BEAN";
//
//    @InjectView(R.id.listview)
//    FanrRefreshListView listView;
//    private List<CheckResultBean> checkResultList = new ArrayList<>();
//    private SocialStreamAdapter mAdapter = null;
//    private List<Map<String, Object>> dataSource = new ArrayList<>();
//    private static final int[] RESOUCE = {R.layout.item_check_detail};
//    private String[] from = {"name", "result", "unit", "arrange"};
//    private int[] to = {R.id.tv_check_name, R.id.tv_check_result, R.id.tv_check_unit, R.id.tv_resultRange};
//
//    @InjectView(R.id.item_my_check)
//    PreferenceRightDetailView itemInfo;
//
//    private CheckResultBean checkResultBean;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_check_detail);
//        ButterKnife.inject(this);
//
//        checkResultBean = (CheckResultBean) getIntent().getSerializableExtra(KEY_CHECK_RESULT_BEAN);
//        if (checkResultBean == null) {
//            finish();
//            return;
//        }
//
//        initViews();
//    }
//
//    private void initViews() {
//        if (!TextUtils.isEmpty(checkResultBean.getCheckDoctor())) {
//            itemInfo.setTitle("检查医生：" + checkResultBean.getCheckDoctor());
//            itemInfo.setContent(JDUtils.formatDate(checkResultBean.getCheckDate(), "yyyy-MM-dd"));
//        } else {
//            itemInfo.setTitle("检查日期：" + JDUtils.formatDate(checkResultBean.getCheckDate(), "yyyy-MM-dd"));
//        }
//
//        HashMap<Integer, String[]> fromMap = new HashMap<Integer, String[]>();
//        fromMap.put(RESOUCE[0], from);
//        HashMap<Integer, int[]> toMap = new HashMap<Integer, int[]>();
//        toMap.put(RESOUCE[0], to);
//        mAdapter = new SocialStreamAdapter(this, dataSource, RESOUCE, fromMap, toMap, 0, UIUtils.convertDpToPixel(3, this));
//        listView.setAdapter(mAdapter);
//        listView.setOnPullRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                checkResultList.clear();
//                dataSource.clear();
//                mAdapter.notifyDataSetChanged();
//                initDataSource();
//            }
//        });
//
//        listView.setCanLoadMore(false);
//        listView.setPullRefreshing(true);
//        initDataSource();
//    }
//
//    protected void initDataSource() {
//        setLoadingViewState(JDLoadingView.STATE_LOADING);
//        JDHttpClient.getInstance().reqCheckDetail(this, checkResultBean.getCheckId(),"online"
//                ,new JDHttpResponseHandler<List<CheckResultBean>>(new TypeReference<BaseBean<List<CheckResultBean>>>() {
//        }) {
//            @Override
//            public void onRequestCallback(BaseBean<List<CheckResultBean>> result) {
//                super.onRequestCallback(result);
//                listView.setPullRefreshing(false);
//                dismissLoadingView();
//                if (result.isSuccess()) {
//                    checkResultList = result.getData();
//                    if (ListUtils.isEmpty(result.getData())) {
//                        setLoadingViewState(JDLoadingView.STATE_EMPTY);
//                    }else{
//                        checkResultBean = checkResultList.get(0);
//                        if (!TextUtils.isEmpty(checkResultBean.getCheckDoctor())) {
//                            itemInfo.setTitle("检查医生：" + checkResultBean.getCheckDoctor());
//                            itemInfo.setContent(JDUtils.formatDate(checkResultBean.getCheckDate(), "yyyy-MM-dd HH:mm:ss"));
//                        } else {
//                            itemInfo.setTitle("检查日期：" + JDUtils.formatDate(checkResultBean.getCheckDate(), "yyyy-MM-dd"));
//                        }
//                    }
//                    parseData();
//                } else {
//                    setLoadingViewState(JDLoadingView.STATE_FAILED);
//                    showToast(result.getMessage());
//                }
//            }
//        });
//    }
//
//    private void parseData() {
//        if (!ListUtils.isEmpty(checkResultList)) {
//            for (final CheckResultBean bean : checkResultList) {
//                Map<String, Object> map = new HashMap<>();
//                map.put(from[0], bean.getItemName());
//                map.put(from[1], bean.getCheckResult());
//                map.put(from[2], bean.getItemUnit());
//                map.put(from[3], bean.getResultRange());
//                if(dataSource.contains(map)){
//                    continue;
//                }else{
//                    dataSource.add(map);
//                }
//
//            }
//            for(int i=0;i<dataSource.size();i++){
//                for(int j=0;j<dataSource.size();j++){
//                    if(i==j){
//
//                    }else if (dataSource.get(i).get(from[0]).toString().equals(dataSource.get(j).get(from[0]).toString())){
//                        dataSource.remove(j);
//                    }
//                }
//            }
//
//        }
//
//        mAdapter.notifyDataSetChanged();
//    }
//}
