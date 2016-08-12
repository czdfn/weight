package com.chengsi.weightcalc.fragment.group;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import com.alibaba.fastjson.TypeReference;
import com.chengsi.weightcalc.R;
import com.chengsi.weightcalc.activity.CollectDetailActivity;
import com.chengsi.weightcalc.activity.DynamicDetailActivity;
import com.chengsi.weightcalc.adapter.SocialStreamAdapter;
import com.chengsi.weightcalc.bean.BaseBean;
import com.chengsi.weightcalc.bean.DynamicBean;
import com.chengsi.weightcalc.bean.ListBaseBean;
import com.chengsi.weightcalc.fragment.BaseFragment;
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

public class CollectionFragment extends BaseFragment {

    @InjectView(R.id.listview)
    FanrRefreshListView dynamicLv;
    private List<DynamicBean> dynamicBeanList = new ArrayList<>();
    private SocialStreamAdapter mAdapter = null;
    private List<Map<String, Object>> dataSource = new ArrayList<>();
    private static final int[] RESOUCE = {R.layout.item_dynamic_list};
    private String[] from = {ImageLoaderUtils.IMAGE_HEAD_PATH, "name", "date", "content", "imagePanel"};
    private int[] to = {R.id.iv_head, R.id.tv_name, R.id.tv_date, R.id.tv_content, R.id.panel_image_content};
    private int curPageNo = 1;

    public CollectionFragment() {
    }

    @Override
    public void onStart() {
        initView();
        super.onStart();
    }

    @Override
    public View getContentView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dynamic, container, false);
        ButterKnife.inject(this, view);
//        initView();
        return view;
    }

    private void initView() {
        HashMap<Integer, String[]> fromMap = new HashMap<Integer, String[]>();
        fromMap.put(RESOUCE[0], from);
        HashMap<Integer, int[]> toMap = new HashMap<Integer, int[]>();
        toMap.put(RESOUCE[0], to);
        mAdapter = new SocialStreamAdapter(getActivity(), dataSource, RESOUCE, fromMap, toMap, 0, 0, ImageLoaderUtils.headDisplayOpts);
        dynamicLv.setAdapter(mAdapter);
        dynamicLv.setOnPullRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                curPageNo = 1;
                dynamicBeanList.clear();
                dataSource.clear();
                mAdapter.notifyDataSetChanged();
                initDataSource();
            }
        });
        mAdapter.setViewBinder(new SocialStreamAdapter.ViewBinder() {
            @Override
            public boolean setViewValue(View view, Object data, Object comment) {
                if (view.getId() == R.id.tv_content && data != null) {
                    String str = (String) data;
                    TextView tv = (TextView) view;
                    tv.setText(Html.fromHtml(str));
                    return true;
                }
                return false;
            }
        });
        dynamicLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), CollectDetailActivity.class);
                CollectDetailActivity.dynamicBeanList = dynamicBeanList;
                intent.putExtra(DynamicDetailActivity.KEY_DYNAMIC_INDEX, position);
                intent.putExtra(DynamicDetailActivity.KEY_DYNAMIC_PAGE_INDEX, curPageNo);
                startActivityForResult(intent, 0);
            }
        });
        dynamicLv.setOnLoadMoreListener(new FanrRefreshListView.OnLoadMoreListener() {
            @Override
            public void loadMore() {
                initDataSource();
            }
        });

        initDataSource();
    }

    protected void initDataSource() {

        setLoadingState(JDLoadingView.STATE_LOADING);
        JDHttpClient.getInstance().reqCollectList(getActivity(), curPageNo, new JDHttpResponseHandler<ListBaseBean<List<DynamicBean>>>(new TypeReference<BaseBean<ListBaseBean<List<DynamicBean>>>>() {
        }) {

            @Override
            public void onRequestCallback(BaseBean<ListBaseBean<List<DynamicBean>>> result) {
                super.onRequestCallback(result);
                dismissLoadingView();
                dynamicLv.setPullRefreshing(false);
                if (result.isSuccess()) {
                    dataSource.clear();
                    dynamicBeanList.clear();
                    List<DynamicBean> addList = result.getData().getResult();
                    if (addList != null) {
                        for (DynamicBean bean : addList){
                            bean.setCollected(true);
                        }
                        dynamicBeanList.addAll(addList);
                    }
                    parseData(dynamicBeanList);
                    if (!ListUtils.isEmpty(addList) && addList.size() >= 10) {
                        curPageNo++;
                        dynamicLv.onLoadComplete();
                    } else {
                        dynamicLv.onLoadCompleteNoMore(FanrRefreshListView.NoMoreHandler.NO_MORE_LOAD_NOT_SHOW_FOOTER_VIEW);
                    }
                } else {
                    if (curPageNo == 1) {
                        setLoadingState(JDLoadingView.STATE_FAILED);
                    } else {
                        showToast(result.getMessage());
                    }
                }
            }
        });
    }


    private void parseData(List<DynamicBean> list) {
        if (!ListUtils.isEmpty(list)) {

            for (final DynamicBean bean : list) {
                Map<String, Object> map = new HashMap<>();
                map.put(from[0], JDUtils.getRemoteImagePath(bean.getIcon()));
                map.put(from[1], bean.getName());
                map.put(from[2], JDUtils.formatDate(bean.getCreateTime(), "yyyy-MM-dd HH:mm"));
                map.put(from[3], bean.getContent());
                dataSource.add(map);
            }
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0 && resultCode == Activity.RESULT_OK){
            curPageNo = 1;
            dynamicBeanList.clear();
            dataSource.clear();
            mAdapter.notifyDataSetChanged();
            initDataSource();
        }
    }
}
