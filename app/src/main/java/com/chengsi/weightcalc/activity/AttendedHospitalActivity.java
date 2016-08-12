package com.chengsi.weightcalc.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.chengsi.weightcalc.R;
import com.chengsi.weightcalc.bean.HospitalBean;
import com.chengsi.weightcalc.widget.FanrRefreshListView;
import com.chengsi.weightcalc.widget.JDLoadingView;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import cn.jiadao.corelibs.utils.ListUtils;

public class AttendedHospitalActivity extends BaseActivity {

    @InjectView(R.id.listview)
    FanrRefreshListView listView;
    private List<HospitalBean> dataSource;
    private SearchAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attended_hospital);
        ButterKnife.inject(this);

        adapter = new SearchAdapter();
        listView.setAdapter(adapter);
        listView.setCanLoadMore(false);
        listView.setOnPullRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                initDataSource();
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(AttendedHospitalActivity.this, HospitalPageActivity.class);
                intent.putExtra(HospitalPageActivity.KEY_CURRENT_HOSPITAL, dataSource.get(position));
                startActivity(intent);
            }
        });
    }

    protected void initDataSource() {
        setLoadingViewState(JDLoadingView.STATE_LOADING);
//        JDHttpClient.getInstance().reqAttendHospitalList(this, new JDHttpResponseHandler<List<HospitalBean>>(new TypeReference<BaseBean<List<HospitalBean>>>() {
//        }) {
//            @Override
//            public void onRequestCallback(BaseBean<List<HospitalBean>> result) {
//                super.onRequestCallback(result);
//                listView.setPullRefreshing(false);
//                dismissLoadingView();
//                if (result.isSuccess()) {
//                    dataSource = result.getData();
//                    UserBean userBean = application.userManager.getUserBean();
//                    userBean.setAttenHospitalList(result.getData());
//                    application.userManager.resetUser(userBean);
//                    if (ListUtils.isEmpty(result.getData())){
//                        setLoadingViewState(JDLoadingView.STATE_EMPTY);
//                    }
//                    adapter.notifyDataSetChanged();
//                }else{
//                    setLoadingViewState(JDLoadingView.STATE_FAILED);
//                    showToast(result.getMessage());
//                }
//            }
//        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        initDataSource();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_attended_hospital, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_add) {
            Intent intent = new Intent(AttendedHospitalActivity.this, HospitalListActivity.class);
            intent.putExtra(HospitalListActivity.KEY_IS_FOR_INDEX_PAGE, true);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    private class SearchAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return ListUtils.isEmpty(dataSource) ? 0 : dataSource.size();
        }

        @Override
        public Object getItem(int position) {
            return dataSource.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            TextView tv = (TextView) getLayoutInflater().inflate(R.layout.item_simple_select, null);
            tv.setText(dataSource.get(position).getName());
            return tv;
        }
    }
}
