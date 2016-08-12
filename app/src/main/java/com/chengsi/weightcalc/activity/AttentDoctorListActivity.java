package com.chengsi.weightcalc.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.Menu;
import android.view.MenuItem;

import com.chengsi.weightcalc.R;
import com.chengsi.weightcalc.adapter.SocialStreamAdapter;
import com.chengsi.weightcalc.bean.DoctorBean;
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

public class AttentDoctorListActivity extends BaseActivity {

    @InjectView(R.id.listview)
    FanrRefreshListView doctorLv;
    private List<DoctorBean> doctorBeanList = new ArrayList<>();
    private SocialStreamAdapter mAdapter = null;
    private List<Map<String, Object>> dataSource = new ArrayList<>();
    private static final int[] RESOUCE = {R.layout.item_doctor_list};
    private String[] from = {ImageLoaderUtils.IMAGE_HEAD_PATH, "name", "duty", "hospital"};
    private int[] to = {R.id.iv_doctor_head, R.id.tv_doctor_name, R.id.tv_doctor_duty, R.id.tv_doctor_hospital};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attent_doctor_list);
        ButterKnife.inject(this);
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

    }

    @Override
    protected void onResume() {
        super.onResume();
        initDataSource();
    }

    protected void initDataSource() {

        setLoadingViewState(JDLoadingView.STATE_LOADING);
//        JDHttpClient.getInstance().reqAttentDoctorList(this, new JDHttpResponseHandler<List<DoctorBean>>(new TypeReference<BaseBean<List<DoctorBean>>>() {
//        }) {
//
//            @Override
//            public void onRequestCallback(BaseBean<List<DoctorBean>> result) {
//                super.onRequestCallback(result);
//                dismissLoadingView();
//                doctorLv.setPullRefreshing(false);
//                if (result.isSuccess()) {
//                    dataSource.clear();
//                    application.userManager.getUserBean().setAttenDoctorList(result.getData());
//                    application.userManager.resetUser(application.userManager.getUserBean());
//                    doctorBeanList = result.getData();
//                    if (ListUtils.isEmpty(result.getData())) {
//                        setLoadingViewState(JDLoadingView.STATE_EMPTY);
//                    }
//                    parseData(doctorBeanList);
//                    mAdapter.notifyDataSetChanged();
//                } else {
//                    setLoadingViewState(JDLoadingView.STATE_FAILED);
//                    showToast(result.getMessage());
//                }
//            }
//        });
    }


    private void parseData(List<DoctorBean> doctorList){
        if (!ListUtils.isEmpty(doctorList)) {

            for (final DoctorBean bean : doctorList) {
                Map<String, Object> map = new HashMap<>();
                map.put(from[0], JDUtils.getRemoteImagePath(bean.getHeadImg()));
                map.put(from[1], bean.getRealName());
                map.put(from[2], bean.getDuty());
                map.put(from[3], bean.getHospitalName());
                dataSource.add(map);
            }
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_attent_doctor_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_add_attention) {
            Intent intent = new Intent(this, DoctorListActivity.class);
            intent.putExtra(DoctorListActivity.DOCTOR_LIST_FOR_USE_STATE, DoctorListActivity.DOCTOR_LIST_FOR_ATTEND);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
