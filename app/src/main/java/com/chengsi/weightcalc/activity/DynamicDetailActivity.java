package com.chengsi.weightcalc.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;

import com.alibaba.fastjson.TypeReference;
import com.chengsi.weightcalc.R;
import com.chengsi.weightcalc.bean.BaseBean;
import com.chengsi.weightcalc.bean.DynamicBean;
import com.chengsi.weightcalc.bean.ListBaseBean;
import com.chengsi.weightcalc.fragment.DynamicDetailFragment;
import com.chengsi.weightcalc.http.JDHttpClient;
import com.chengsi.weightcalc.http.JDHttpResponseHandler;
import com.chengsi.weightcalc.widget.JDLoadingView;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import cn.jiadao.corelibs.utils.ListUtils;

public class DynamicDetailActivity extends BaseActivity {

    public static final String KEY_DYNAMIC_INDEX = "KEY_DYNAMIC_INDEX";
    public static final String KEY_DYNAMIC_PAGE_INDEX = "KEY_DYNAMIC_PAGE_INDEX";


    @InjectView(R.id.viewpager)
    ViewPager viewPager;

    private DynamicBean dynamicBean;
    public static List<DynamicBean> dynamicBeanList = new ArrayList<>();
    private DynamicAdapter adapter = null;
    private int curPageNo = 1;
    private int curItem = 0;

    private MenuItem menuItem;

    private boolean canLoadMore = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dynamic_detail);
        ButterKnife.inject(this);

        curPageNo = getIntent().getIntExtra(KEY_DYNAMIC_PAGE_INDEX, 0);
        curItem = getIntent().getIntExtra(KEY_DYNAMIC_INDEX, 0);
        adapter = new DynamicAdapter(getSupportFragmentManager());
        dynamicBean = dynamicBeanList.get(curItem);
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (canLoadMore && (dynamicBeanList == null || dynamicBeanList.size() - position <= 3)) {
                    initDataSource();
                }
                if (dynamicBeanList != null && dynamicBeanList.size() > position) {
                    dynamicBean = dynamicBeanList.get(position);
                    if (menuItem != null){
                        if (dynamicBean != null && dynamicBean.isCollected()){
                            menuItem.setTitle("取消收藏");
                        }else{
                            menuItem.setTitle("收藏");
                        }
                    }
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        viewPager.setCurrentItem(getIntent().getIntExtra(KEY_DYNAMIC_INDEX, 0));
        initDataSource();
    }

    protected void initDataSource() {
        if (curPageNo == 1) {
            setLoadingViewState(JDLoadingView.STATE_LOADING);
        }
        JDHttpClient.getInstance().reqDynamicList(this, curPageNo, new JDHttpResponseHandler<ListBaseBean<List<DynamicBean>>>(new TypeReference<BaseBean<ListBaseBean<List<DynamicBean>>>>() {
        }) {

            @Override
            public void onRequestCallback(BaseBean<ListBaseBean<List<DynamicBean>>> result) {
                super.onRequestCallback(result);
                dismissLoadingView();
                if (result.isSuccess()) {
                    List<DynamicBean> addList = result.getData().getResult();
                    if (!ListUtils.isEmpty(addList)) {
                        dynamicBeanList.addAll(addList);
                    } else {
                        canLoadMore = false;
                    }
                    adapter.notifyDataSetChanged();
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_dynamic_detail, menu);
        menuItem = menu.findItem(R.id.action_collect);
        if (dynamicBean != null){
            if (dynamicBean != null && dynamicBean.isCollected()){
                menuItem.setTitle("取消收藏");
            }else{
                menuItem.setTitle("收藏");
            }
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_collect) {
            if (dynamicBean.isCollected()){
                cancelCollect();
            }else{
                collect();
            }
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void collect() {
        menuItem.setEnabled(false);
        final  DynamicBean bean = dynamicBeanList.get(viewPager.getCurrentItem());
        JDHttpClient.getInstance().reqAddCollect(this, bean.getId(), new JDHttpResponseHandler<String>(new TypeReference<BaseBean<String>>() {
        }) {
            @Override
            public void onRequestCallback(BaseBean<String> result) {
                super.onRequestCallback(result);
                bean.setCollected(true);
                if (bean.getId() == dynamicBean.getId()) {
                    menuItem.setEnabled(true);
                    if (result.isSuccess()) {
                        menuItem.setTitle("取消收藏");
                    } else {
                        showToast(result.getMessage());
                    }
                }
            }
        });
    }

    private void cancelCollect() {
        menuItem.setEnabled(false);
        final  DynamicBean bean = dynamicBeanList.get(viewPager.getCurrentItem());
        JDHttpClient.getInstance().reqDelCollect(this, bean.getId(), new JDHttpResponseHandler<String>(new TypeReference<BaseBean<String>>() {
        }) {
            @Override
            public void onRequestCallback(BaseBean<String> result) {
                super.onRequestCallback(result);
                bean.setCollected(false);
                if (bean.getId() == dynamicBean.getId()) {
                    menuItem.setEnabled(true);
                    if (result.isSuccess()) {
                        menuItem.setTitle("收藏");
                    } else {
                        showToast(result.getMessage());
                    }
                }
            }
        });
    }


    public class DynamicAdapter extends FragmentPagerAdapter {

        public DynamicAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            DynamicBean bean = null;
            if (dynamicBeanList != null && dynamicBeanList.size() > position) {
                bean = dynamicBeanList.get(position);
            }
            return DynamicDetailFragment.newInstance(bean);
        }

        @Override
        public int getCount() {
            return dynamicBeanList == null ? 0 : dynamicBeanList.size();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dynamicBeanList = null;
    }
}
