package com.chengsi.weightcalc.fragment.notification;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.chengsi.weightcalc.activity.ImagePreviewActivity;
import com.chengsi.weightcalc.activity.NotificationDetailActivity;
import com.chengsi.weightcalc.adapter.SocialStreamAdapter;
import com.chengsi.weightcalc.bean.Notificationhistory;
import com.chengsi.weightcalc.fragment.BaseFragment;
import com.chengsi.weightcalc.listener.OnContinuousClickListener;
import com.chengsi.weightcalc.utils.JDUtils;
import com.chengsi.weightcalc.utils.UIUtils;
import com.chengsi.weightcalc.utils.imageloader.ImageLoaderUtils;
import com.chengsi.weightcalc.widget.FanrRefreshListView;
import com.chengsi.weightcalc.widget.JDLoadingView;
import com.chengsi.weightcalc.widget.SquareImageView;
import com.chengsi.weightcalc.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.InjectView;
import cn.jiadao.corelibs.utils.ArrayUtils;
import cn.jiadao.corelibs.utils.ListUtils;

public class CheckFragment extends BaseFragment {

    public static final String KEY_NOTIFICATION_BEAN = "KEY_NOTIFICATION_BEAN";

    @InjectView(R.id.notification_list)
    FanrRefreshListView listView;
    private List<Notificationhistory> notificationlist = new ArrayList<>();
    private SocialStreamAdapter mAdapter = null;
    private List<Map<String, Object>> dataSource = new ArrayList<>();
    private static final int[] RESOUCE = {R.layout.item_notification};
    private String[] from = {"title", "date", "state", "content"};
    private int[] to = {R.id.tv_notification_title, R.id.tv_notification_date, R.id.tv_notification_state, R.id.tv_notification_content};

    public CheckFragment() {
    }

    @Override
    public View getContentView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sys_notification, container, false);
        ButterKnife.inject(this, view);
        initViews();
        return view;
    }

    private void initViews() {
        HashMap<Integer, String[]> fromMap = new HashMap<Integer, String[]>();
        fromMap.put(RESOUCE[0], from);
        HashMap<Integer, int[]> toMap = new HashMap<Integer, int[]>();
        toMap.put(RESOUCE[0], to);
        mAdapter = new SocialStreamAdapter(getActivity(), dataSource, RESOUCE, fromMap, toMap, 0, 0);
        mAdapter.setViewBinder(new SocialStreamAdapter.ViewBinder() {

            private LinearLayout initLayout(LinearLayout layout) {
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
                if (view.getId() == R.id.panel_image_content) {
                    String urls = (String) data;
                    final LinearLayout layout = (LinearLayout) view;
                    layout.removeAllViews();
                    int width = UIUtils.getScreenSize(getActivity()).x - UIUtils.convertDpToPixel(32, getActivity());
                    if (!TextUtils.isEmpty(urls)) {
                        final String[] arr = urls.split(",");
                        int padding = UIUtils.convertDpToPixel(6, getActivity());
                        LinearLayout linearLayout = initLayout(layout);
                        for (int i = 0; i < arr.length; i++) {
                            ImageView imageView;
                            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                            if (arr.length == 1) {
                                imageView = new ImageView(getActivity());
                                params.height = params.width = UIUtils.convertDpToPixel(180, getActivity());
                            } else {
                                params.width = (width - 3 * padding) / 4;
                                imageView = new SquareImageView(getActivity());
                            }
                            imageView.setLayoutParams(params);
                            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                            if ((i + 1) % 4 != 0) {

                                params.rightMargin = padding;
                                linearLayout.addView(imageView);
                            } else {
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
                notificationlist.clear();
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
                Notificationhistory notification = notificationlist.get(position);
                Intent intent = new Intent(getActivity(), NotificationDetailActivity.class);
                intent.putExtra(SysNotification.KEY_NOTIFICATION_BEAN, notification);
                startActivity(intent);
            }
        });

        initDataSource();
    }
    @Override
    public void onResume() {
        super.onResume();
        initDataSource();
    }
    protected void initDataSource() {

        setLoadingState(JDLoadingView.STATE_LOADING);
//        JDHttpClient.getInstance().reqGetPushNotification(getActivity(), fanrApp.userManager.getUserBean().getToken(), 1, new JDHttpResponseHandler<List<Notificationhistory>>(new TypeReference<BaseBean<List<Notificationhistory>>>() {
//        }) {
//
//            @Override
//            public void onRequestCallback(BaseBean<List<Notificationhistory>> result) {
//                super.onRequestCallback(result);
//                dismissLoadingView();
//                listView.setPullRefreshing(false);
//                if (result.isSuccess()) {
//                    dataSource.clear();
//                    notificationlist = result.getData();
//                    if (notificationlist != null) {
//                        parseData(notificationlist);
//                    }
//                }
//            }
//        });
    }


    private void parseData(List<Notificationhistory> notificationlist) {
        if (!ListUtils.isEmpty(notificationlist)) {

            for (final Notificationhistory bean : notificationlist) {
                Map<String, Object> map = new HashMap<>();
                map.put(from[0], bean.getTitle());
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                String datestr = sdf.format(new Date());
                if(datestr.equals(JDUtils.formatDate(bean.getPushtime(), "yyyy-MM-dd"))){
                    map.put(from[1], "今天");
                }else{
                    map.put(from[1], JDUtils.formatDate(bean.getPushtime(), "yyyy-MM-dd"));
                }
                map.put(from[2], bean.getReadstatus().equals("1") ? "已读" : "未读");
                map.put(from[3], bean.getContent());
                dataSource.add(map);
            }
            mAdapter.notifyDataSetChanged();
        }
    }

}
