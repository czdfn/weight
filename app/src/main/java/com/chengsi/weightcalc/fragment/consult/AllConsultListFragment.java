package com.chengsi.weightcalc.fragment.consult;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.alibaba.fastjson.TypeReference;
import com.chengsi.weightcalc.R;
import com.chengsi.weightcalc.activity.ConsultDetailActivity;
import com.chengsi.weightcalc.activity.ImagePreviewActivity;
import com.chengsi.weightcalc.adapter.SocialStreamAdapter;
import com.chengsi.weightcalc.bean.BaseBean;
import com.chengsi.weightcalc.bean.ConsultBean;
import com.chengsi.weightcalc.bean.ListBaseBean;
import com.chengsi.weightcalc.fragment.BaseFragment;
import com.chengsi.weightcalc.http.JDHttpClient;
import com.chengsi.weightcalc.http.JDHttpResponseHandler;
import com.chengsi.weightcalc.listener.OnContinuousClickListener;
import com.chengsi.weightcalc.utils.JDUtils;
import com.chengsi.weightcalc.utils.UIUtils;
import com.chengsi.weightcalc.utils.imageloader.ImageLoaderUtils;
import com.chengsi.weightcalc.widget.FanrRefreshListView;
import com.chengsi.weightcalc.widget.JDLoadingView;
import com.chengsi.weightcalc.widget.SquareImageView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.InjectView;
import cn.jiadao.corelibs.utils.ArrayUtils;
import cn.jiadao.corelibs.utils.ListUtils;

/**
 * A simple {@link Fragment} subclass.
 */
public class AllConsultListFragment extends BaseFragment {

    @InjectView(R.id.listview)
    FanrRefreshListView listView;
    private List<ConsultBean> consultBeanList = new ArrayList<>();
    private SocialStreamAdapter mAdapter = null;
    private List<Map<String, Object>> dataSource = new ArrayList<>();
    private static final int[] RESOUCE = {R.layout.item_consult_list};
    private String[] from = {"name", "date", "state", "content", "images", "title"};
    private int curPageNo = 1;
    private int[] to = {R.id.tv_consult_doctor, R.id.tv_date, R.id.tv_state, R.id.tv_consult_content, R.id.panel_image_content, R.id.tv_consult_title};

    public AllConsultListFragment() {
    }

    @Override
    public View getContentView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.lv_consult, container, false);
        ButterKnife.inject(this, view);
        initViews();
        initDataSource();
        return view;
    }

    private void initViews(){
        HashMap<Integer, String[]> fromMap = new HashMap<Integer, String[]>();
        fromMap.put(RESOUCE[0], from);
        HashMap<Integer, int[]> toMap = new HashMap<Integer, int[]>();
        toMap.put(RESOUCE[0], to);
        mAdapter = new SocialStreamAdapter(getActivity(), dataSource, RESOUCE, fromMap, toMap, 0, 0);
        mAdapter.setViewBinder(new SocialStreamAdapter.ViewBinder() {

            private LinearLayout initLayout(LinearLayout layout){
                LinearLayout linearLayout = new LinearLayout(getActivity());
                linearLayout.setOrientation(LinearLayout.HORIZONTAL);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                params.bottomMargin = UIUtils.convertDpToPixel(8, getActivity());
                linearLayout.setLayoutParams(params);
                layout.addView(linearLayout);
                return linearLayout;
            }
            @Override
            public boolean setViewValue(View view, Object data, Object comment) {
                if (view.getId() == R.id.panel_image_content){
                    String urls = (String) data;
                    final LinearLayout layout = (LinearLayout) view;
                    layout.removeAllViews();
                    int width = UIUtils.getScreenSize(getActivity()).x - UIUtils.convertDpToPixel(32, getActivity());
                    if (!TextUtils.isEmpty(urls)){
                        final String[] arr = urls.split(",");
                        int padding = UIUtils.convertDpToPixel(6, getActivity());
                        LinearLayout linearLayout = initLayout(layout);
                        for (int i = 0; i < arr.length; i ++){
                            ImageView imageView;
                            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                            if (arr.length == 1){
                                imageView = new ImageView(getActivity());
                                params.height = params.width = UIUtils.convertDpToPixel(180, getActivity());
                            }else{
                                params.width = (width - 3 * padding) / 4;
                                imageView = new SquareImageView(getActivity());
                            }
                            imageView.setLayoutParams(params);
                            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                            if ((i + 1) % 4 != 0){

                                params.rightMargin = padding;
                                linearLayout.addView(imageView);
                            }else{
                                params.rightMargin = 0;
                                linearLayout.addView(imageView);
                                linearLayout = initLayout(layout);
                            }
                            final String url = arr[i];
                            imageView.setOnClickListener(new OnContinuousClickListener() {
                                @Override
                                public void onContinuousClick(View v) {
                                    Intent intent = new Intent(getActivity(), ImagePreviewActivity.class);
                                    intent.putStringArrayListExtra(ImagePreviewActivity.KEY_IMAGE_URLLIST, ArrayUtils.toList(arr));
                                    intent.putExtra(ImagePreviewActivity.KEY_IMAGE_CURRENT_URL, url);
                                    startActivity(intent);
                                }
                            });
                            ImageLoaderUtils.displayImageForIv(imageView, JDUtils.getRemoteImagePath(arr[i]));
                        }
                    }
                    return true;
                }
                return false;
            }
        });
        listView.setAdapter(mAdapter);
        listView.setOnPullRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                curPageNo = 1;
                consultBeanList.clear();
                dataSource.clear();
                mAdapter.notifyDataSetChanged();
                initDataSource();
            }
        });

        listView.setOnLoadMoreListener(new FanrRefreshListView.OnLoadMoreListener() {
            @Override
            public void loadMore() {
                initDataSource();
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ConsultBean consultBean = consultBeanList.get(position);
                Intent intent = new Intent(getActivity(), ConsultDetailActivity.class);
                intent.putExtra(ConsultDetailActivity.KEY_CONSULT_BEAN, consultBean);
                startActivity(intent);
            }
        });

        initDataSource();
    }

    protected void initDataSource() {

        setLoadingState(JDLoadingView.STATE_LOADING);
        JDHttpClient.getInstance().reqConsultList(getActivity(), curPageNo, new JDHttpResponseHandler<ListBaseBean<List<ConsultBean>>>(new TypeReference<BaseBean<ListBaseBean<List<ConsultBean>>>>() {
        }) {

            @Override
            public void onRequestCallback(BaseBean<ListBaseBean<List<ConsultBean>>> result) {
                super.onRequestCallback(result);
                dismissLoadingView();
                if (getActivity() == null){
                    return;
                }
                listView.setPullRefreshing(false);
                if (result.isSuccess()) {
                    List<ConsultBean> addList = result.getData().getResult();
                    if (addList != null) {
                        consultBeanList.addAll(addList);
                    }
                    parseData(addList);
                    if (addList != null && addList.size() == result.getData().getPageSize()) {
                        listView.onLoadComplete();
                    } else {
                        listView.onLoadCompleteNoMore(FanrRefreshListView.NoMoreHandler.NO_MORE_LOAD_NOT_SHOW_FOOTER_VIEW);
                    }
                    curPageNo++;
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


    private void parseData(List<ConsultBean> consultBeanList){
        if (!ListUtils.isEmpty(consultBeanList)) {

            for (final ConsultBean bean : consultBeanList) {
                Map<String, Object> map = new HashMap<>();
                map.put(from[0], bean.getDoctorName());
                map.put(from[1], JDUtils.formatDate(bean.getAnswerTime(), "yyyy-MM-dd"));
                map.put(from[2], bean.isAnswer() ? "已回复":"未回复");
                map.put(from[3], bean.getContent());
                map.put(from[4], bean.getImageList());
                map.put(from[5], bean.getTitle());
                dataSource.add(map);
            }
            mAdapter.notifyDataSetChanged();
        }
    }

}
