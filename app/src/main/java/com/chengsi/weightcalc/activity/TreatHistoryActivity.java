//package com.chengsi.weightcalc.activity;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.support.v4.widget.SwipeRefreshLayout;
//import android.util.TypedValue;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.LinearLayout;
//import android.widget.TextView;
//
//import com.alibaba.fastjson.TypeReference;
//import com.chengsi.weightcalc.R;
//import com.chengsi.weightcalc.adapter.SocialStreamAdapter;
//import com.chengsi.weightcalc.bean.AdviceBean;
//import com.chengsi.weightcalc.bean.BaseBean;
//import com.chengsi.weightcalc.bean.CheckResultBean;
//import com.chengsi.weightcalc.bean.ObjectType;
//import com.chengsi.weightcalc.bean.OplanInforBean;
//import com.chengsi.weightcalc.bean.TreatHistoryBean;
//import com.chengsi.weightcalc.http.JDHttpClient;
//import com.chengsi.weightcalc.http.JDHttpResponseHandler;
//import com.chengsi.weightcalc.listener.OnContinuousClickListener;
//import com.chengsi.weightcalc.utils.JDUtils;
//import com.chengsi.weightcalc.utils.UIUtils;
//import com.chengsi.weightcalc.widget.FanrRefreshListView;
//import com.chengsi.weightcalc.widget.JDLoadingView;
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
//public class TreatHistoryActivity extends BaseActivity {
//
//    @InjectView(R.id.listview)
//    FanrRefreshListView listView;
//    private List<OplanInforBean> planList = new ArrayList<>();
//    private SocialStreamAdapter mAdapter = null;
//    private List<Map<String, Object>> dataSource = new ArrayList<>();
//    private static final int[] RESOUCE = {R.layout.item_treat_history};
//    private String[] from = {"topline", "icon", "bottomline", "info", "date", "hiddenClick", "date"};
//    private int[] to = {R.id.view_top_line, R.id.iv_start_icon, R.id.view_bottom_line, R.id.panel_treat_info, R.id.tv_treat_date, R.id.panel_main, R.id.tv_treat_date};
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_treat_history);
//        ButterKnife.inject(this);
//
//        initViews();
//    }
//
//    private void initViews() {
//        HashMap<Integer, String[]> fromMap = new HashMap<Integer, String[]>();
//        fromMap.put(RESOUCE[0], from);
//        HashMap<Integer, int[]> toMap = new HashMap<Integer, int[]>();
//        toMap.put(RESOUCE[0], to);
//        mAdapter = new SocialStreamAdapter(this, dataSource, RESOUCE, fromMap, toMap, 0, 0);
//        listView.setAdapter(mAdapter);
//        listView.setOnPullRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                planList.clear();
//                dataSource.clear();
//                mAdapter.notifyDataSetChanged();
//                initDataSource();
//            }
//        });
//
//        mAdapter.setViewBinder(new SocialStreamAdapter.ViewBinder() {
//            @Override
//            public boolean setViewValue(View view, Object data, Object comment) {
//                if (view.getId() == R.id.view_top_line || view.getId() == R.id.view_bottom_line) {
//                    if (data instanceof Boolean) {
//                        boolean flag = (boolean) data;
//                        if (flag) {
//                            view.setVisibility(View.VISIBLE);
//                        } else {
//                            view.setVisibility(View.INVISIBLE);
//                        }
//                        return true;
//                    }
//                }else if(view.getId() == R.id.panel_treat_info && data instanceof List){
//                    List<ObjectType> list = (List<ObjectType>) data;
//                    LinearLayout layout = (LinearLayout) view;
//                    layout.removeAllViews();
//                    for (final ObjectType obj : list){
//                        TextView tv = new TextView(TreatHistoryActivity.this);
//                        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//                        tv.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 14);
//                        tv.setTextColor(getResources().getColor(R.color.text_74));
//                        tv.setPadding(0, 0, 0, UIUtils.convertDpToPixel(8, TreatHistoryActivity.this));
//                        tv.setText(obj.getDesp());
//                        tv.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
//                                if (obj.getType() != null){
//                                    Intent intent = null;
//                                    switch (obj.getType().trim()){
//                                        case "chufang" :
//                                            intent = new Intent(TreatHistoryActivity.this, AdviceDetailActivity.class);
//                                            AdviceBean adviceBean = new AdviceBean();
//                                            adviceBean.setNum(obj.getContent());
//                                            intent.putExtra(AdviceDetailActivity.KEY_ADVICE_BEAN, adviceBean);
//                                            startActivity(intent);
//                                            break;
//                                        case "jiancha" :
//                                            intent = new Intent(TreatHistoryActivity.this, CheckDetailActivity.class);
//                                            CheckResultBean checkResultBean = new CheckResultBean();
//                                            checkResultBean.setCheckId(obj.getContent());
//                                            intent.putExtra(CheckDetailActivity.KEY_CHECK_RESULT_BEAN, checkResultBean);
//                                            startActivity(intent);
//                                            break;
//                                        case "doctor_advice" :
//                                            intent = new Intent(TreatHistoryActivity.this, AdviceDetailActivity.class);
//                                            AdviceBean bean = new AdviceBean();
//                                            bean.setId(obj.getId());
//                                            intent.putExtra(AdviceDetailActivity.KEY_ADVICE_BEAN, bean);
//                                            startActivity(intent);
//                                            break;
//                                    }
//                                }
//                            }
//                        });
//                        layout.addView(tv, params);
//                    }
//                    return true;
//                }else if(view.getId() == R.id.tv_treat_date && data !=null && data instanceof String){
//                    String str = (String) data;
//                    TextView tv = (TextView) view;
//                    if (str.contains("治疗周期")){
//                        tv.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 16);
//                    }else{
//                        tv.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 14);
//                    }
//                    tv.setText(str);
//                    return true;
//                }
//                return false;
//            }
//        });
//
//        listView.setCanLoadMore(false);
//        initDataSource();
//    }
//
//    protected void initDataSource() {
//        if (ListUtils.isEmpty(planList)){
//            setLoadingViewState(JDLoadingView.STATE_LOADING);
//        }else{
//            listView.setPullRefreshing(true);
//        }
//        JDHttpClient.getInstance().reqTreatHistory(this, new JDHttpResponseHandler<List<OplanInforBean>>(new TypeReference<BaseBean<List<OplanInforBean>>>() {
//        }) {
//            @Override
//            public void onRequestCallback(BaseBean<List<OplanInforBean>> result) {
//                super.onRequestCallback(result);
//                listView.setPullRefreshing(false);
//                dismissLoadingView();
//                if (result.isSuccess()) {
//                    List<OplanInforBean> list =  result.getData();
//                    if (list != null){
//                        for (OplanInforBean bean : list){
//                            if (!ListUtils.isEmpty(bean.getTreatHistory())) {
//                                planList.add(bean);
//                            }
//                        }
//                    }
//                    if (ListUtils.isEmpty(planList)) {
//                        setLoadingViewState(JDLoadingView.STATE_EMPTY);
//                    }else{
//                        for (int i = 0; i < planList.size(); i ++){
//                            if (i != 0){
//                                planList.get(i).setIsHidden(true);
//                            }else{
//                                planList.get(i).setIsHidden(false);
//                            }
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
//        if (!ListUtils.isEmpty(planList)) {
//            for (final OplanInforBean bean : planList) {
//                if (!ListUtils.isEmpty(bean.getTreatHistory())) {
//                    Map<String, Object> map = new HashMap<>();
//                    map.put(from[0], false);
//                    map.put(from[1], R.drawable.item_treat_start_icon);
//                    map.put(from[2], true);
//                    map.put(from[3], new ArrayList<>());
//                    String str = "治疗周期：" + JDUtils.formatDate(bean.getStartDate(), "yyyy-MM-dd");
//                    if (bean.getEndDate() != null){
//                        str +=  "至" + JDUtils.formatDate(bean.getEndDate(), "yyyy-MM-dd");
//                    }
//                    str += "\n周期方案：" + bean.getOpsName();
//                    map.put(from[4], str);
//                    map.put(from[5], new OnContinuousClickListener() {
//                        @Override
//                        public void onContinuousClick(View v) {
//                            bean.setIsHidden(!bean.isHidden());
//                            dataSource.clear();
//                            parseData();
//                            mAdapter.notifyDataSetChanged();
//                        }
//                    });
//                    if (bean.isHidden()){
//                        map.put(from[2], false);
//                    }
//                    dataSource.add(map);
//                    if(!bean.isHidden()){
//                        for (TreatHistoryBean taskBean : bean.getTreatHistory()) {
//                            map = new HashMap<>();
//
//                            map.put(from[0], true);
//                            boolean flag = bean.getTreatHistory().indexOf(taskBean) == bean.getTreatHistory().size() - 1 ? false : true;
//                            map.put(from[2], flag);
//                            if (!flag){
//                                map.put(from[1], R.drawable.item_treat_start_icon);
//                            }else{
//                                map.put(from[1], R.drawable.item_treat_middle_icon);
//                            }
//                            map.put(from[3], taskBean.getObjectList());
//                            map.put(from[4], taskBean.getDate());
//                            dataSource.add(map);
//                        }
//                    }
//                }
//            }
//        }
//        if (ListUtils.isEmpty(dataSource)){
//            setLoadingViewState(JDLoadingView.STATE_EMPTY);
//        }
//        mAdapter.notifyDataSetChanged();
//    }
//}
