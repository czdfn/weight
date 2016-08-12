package com.chengsi.weightcalc.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;

import com.alibaba.fastjson.TypeReference;
import com.chengsi.weightcalc.R;
import com.chengsi.weightcalc.adapter.SocialStreamAdapter;
import com.chengsi.weightcalc.bean.BaseBean;
import com.chengsi.weightcalc.bean.DoctorBean;
import com.chengsi.weightcalc.bean.ListBaseBean;
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

import butterknife.ButterKnife;
import butterknife.InjectView;
import cn.jiadao.corelibs.utils.ListUtils;

public class DoctorListActivity extends BaseActivity {

    public static final String DOCTOR_LIST_FOR_USE_STATE = "DOCTOR_LIST_FOR_USE_STATE";
    public static final String DOCTOR_ITEM_SELECTED = "DOCTOR_ITEM_SELECTED";

    public static final int DOCTOR_LIST_FOR_SELECT = 0;
    public static final int DOCTOR_LIST_FOR_ATTEND = 1;

    @InjectView(R.id.listview)
    FanrRefreshListView doctorLv;
    private List<DoctorBean> doctorBeanList = new ArrayList<>();
    private SocialStreamAdapter mAdapter = null;
    private List<Map<String, Object>> dataSource = new ArrayList<>();
    private static final int[] RESOUCE = {R.layout.item_doctor_list};
    private String[] from = {ImageLoaderUtils.IMAGE_HEAD_PATH, "name", "duty", "hospital", "dutyVisible"};
    private int[] to = {R.id.iv_doctor_head, R.id.tv_doctor_name, R.id.tv_doctor_duty, R.id.tv_doctor_hospital, R.id.tv_doctor_duty};
    private int doctorListForUse = 0;

    private int curPageNo = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_list);
        ButterKnife.inject(this);
        doctorListForUse = getIntent().getIntExtra(DOCTOR_LIST_FOR_USE_STATE, DOCTOR_LIST_FOR_SELECT);

        if (doctorListForUse == DOCTOR_LIST_FOR_SELECT) {
            setMyTitle("选择医生");
        } else if (doctorListForUse == DOCTOR_LIST_FOR_ATTEND) {
            setMyTitle("关注医生");
        }

        initViews();
    }

    private void initViews() {
        HashMap<Integer, String[]> fromMap = new HashMap<Integer, String[]>();
        fromMap.put(RESOUCE[0], from);
        HashMap<Integer, int[]> toMap = new HashMap<Integer, int[]>();
        toMap.put(RESOUCE[0], to);
        mAdapter = new SocialStreamAdapter(this, dataSource, RESOUCE, fromMap, toMap, 0, 0, ImageLoaderUtils.headDisplayOpts);
        doctorLv.setAdapter(mAdapter);
        doctorLv.setOnPullRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                curPageNo = 1;
                doctorBeanList.clear();
                dataSource.clear();
                mAdapter.notifyDataSetChanged();
                initDataSource();
            }
        });

        doctorLv.setOnLoadMoreListener(new FanrRefreshListView.OnLoadMoreListener() {
            @Override
            public void loadMore() {
                initDataSource();
            }
        });

        if (doctorListForUse == DOCTOR_LIST_FOR_SELECT) {
            doctorLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    DoctorBean bean = doctorBeanList.get(position);
                    Intent intent = new Intent();
                    intent.putExtra(DOCTOR_ITEM_SELECTED, bean);
                    setResult(RESULT_OK, intent);
                    finish();
                }
            });
        } else if (doctorListForUse == DOCTOR_LIST_FOR_ATTEND) {
            doctorLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    DoctorBean bean = doctorBeanList.get(position);
                    Intent intent = new Intent(DoctorListActivity.this, DoctorDetailActivity.class);
                    intent.putExtra(DoctorDetailActivity.KEY_CURRENT_DOCTOR, bean);
                    startActivity(intent);
                }
            });
        }

        initDataSource();
    }

    protected void initDataSource() {

        setLoadingViewState(JDLoadingView.STATE_LOADING);
        JDHttpClient.getInstance().reqDoctorList(this, "", "17", curPageNo, new JDHttpResponseHandler<ListBaseBean<List<DoctorBean>>>(new TypeReference<BaseBean<ListBaseBean<List<DoctorBean>>>>() {
        }) {

            @Override
            public void onRequestCallback(BaseBean<ListBaseBean<List<DoctorBean>>> result) {
                super.onRequestCallback(result);
                dismissLoadingView();
                doctorLv.setPullRefreshing(false);
                if (result.isSuccess()) {
                    List<DoctorBean> addList = result.getData().getResult();
                    if (addList != null) {
                        doctorBeanList.addAll(addList);
                    }
                    parseData(addList);
                    if (result.getData().isHasNext()) {
                        doctorLv.onLoadComplete();
                    } else {
                        doctorLv.onLoadCompleteNoMore(FanrRefreshListView.NoMoreHandler.NO_MORE_LOAD_NOT_SHOW_FOOTER_VIEW);
                    }
                    curPageNo++;
                } else {
                    if (curPageNo == 1) {
                        setLoadingViewState(JDLoadingView.STATE_FAILED);
                    } else {
                        showToast(result.getMessage());
                    }
                }
            }
        });
    }


    private void parseData(List<DoctorBean> doctorList) {
        if (!ListUtils.isEmpty(doctorList)) {

            for (final DoctorBean bean : doctorList) {
                Map<String, Object> map = new HashMap<>();
                map.put(from[0], JDUtils.getRemoteImagePath(bean.getHeadImg()));
                map.put(from[1], bean.getRealName());
                map.put(from[2], bean.getDuty());
                map.put(from[3], bean.getHospitalName());
                if (TextUtils.isEmpty(bean.getDuty())){
                    map.put(from[4], false);
                }else{
                    map.put(from[4], false);
                }
                dataSource.add(map);
            }
            mAdapter.notifyDataSetChanged();
        }
    }
}
