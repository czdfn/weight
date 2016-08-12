package com.chengsi.weightcalc.fragment.index;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.TypeReference;
import com.chengsi.weightcalc.activity.measure.BackMeasureActivity;
import com.chengsi.weightcalc.activity.measure.ConstantActivity;
import com.chengsi.weightcalc.activity.measure.FrontMeasureActivity;
import com.chengsi.weightcalc.activity.measure.MidMeasureActivity;
import com.chengsi.weightcalc.R;
import com.chengsi.weightcalc.activity.DynamicDetailActivity;
import com.chengsi.weightcalc.activity.MainActivity;
import com.chengsi.weightcalc.bean.BaseBean;
import com.chengsi.weightcalc.bean.DynamicBean;
import com.chengsi.weightcalc.bean.GoodNewsBean;
import com.chengsi.weightcalc.bean.ListBaseBean;
import com.chengsi.weightcalc.fragment.BaseFragment;
import com.chengsi.weightcalc.http.JDHttpClient;
import com.chengsi.weightcalc.http.JDHttpResponseHandler;
import com.chengsi.weightcalc.listener.OnContinuousClickListener;
import com.chengsi.weightcalc.storage.JDCustomStorage;
import com.chengsi.weightcalc.utils.JDUtils;
import com.chengsi.weightcalc.utils.UIUtils;
import com.chengsi.weightcalc.utils.imageloader.ImageLoaderUtils;
import com.nostra13.universalimageloader.core.DisplayImageOptions;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ScheduledExecutorService;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import cn.jiadao.corelibs.utils.ListUtils;

public class IndexPageFragment extends BaseFragment {

@InjectView(R.id.panel_indicator_area)
LinearLayout mIndicatorPanel;
@InjectView(R.id.panel_dynamic_area)
LinearLayout panelDynamicArea;

@InjectView(R.id.panel_more)
View panelMore;
@InjectView(R.id.tv_good_news)
TextView tvGoodNews;

RollingPageAdapter mAdapter;
PageChangedListener mPageChangedListener;

ScheduledExecutorService executorService;

private List<GoodNewsBean> goodNewsBeanList;

private long lastRefreshTime = 0;

private int width, height;
private List<ImageView> imageViewList = new ArrayList<>();

private List<String> rollingImageList = new ArrayList<>();

public static final DisplayImageOptions rollingDisplayOpts = new DisplayImageOptions.Builder()
    .cacheInMemory(true).cacheOnDisk(true)
    .showImageOnFail(R.drawable.rolling_pic)
    .showImageForEmptyUri(R.drawable.rolling_pic)
    .showImageOnLoading(R.drawable.rolling_pic)
    .build();



public IndexPageFragment() {
}

@Override
public View getContentView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
View view = inflater.inflate(R.layout.fragment_index_page, container, false);
ButterKnife.inject(this, view);
rollingImageList = JDCustomStorage.getInstance().getRollingList();

width = UIUtils.getScreenSize(getActivity()).x;
height = UIUtils.convertDpToPixel(160, getActivity());
mAdapter = new RollingPageAdapter();

panelMore.setOnClickListener(new OnContinuousClickListener() {
    @Override
    public void onContinuousClick(View v) {
        MainActivity holdAct = (MainActivity) getActivity();
//                holdAct.showDynamicList();
    }
});
return view;
}


private void initDynamic() {
if (System.currentTimeMillis() - lastRefreshTime > 5 * 60 * 1000) {

    JDHttpClient.getInstance().reqDynamicList(getActivity(), 1, new JDHttpResponseHandler<ListBaseBean<List<DynamicBean>>>(new TypeReference<BaseBean<ListBaseBean<List<DynamicBean>>>>() {
    }) {

        @Override
        public void onRequestCallback(final BaseBean<ListBaseBean<List<DynamicBean>>> result) {
            super.onRequestCallback(result);
            if (result.isSuccess()) {
                if (panelDynamicArea == null) {
                    return;
                }
                panelDynamicArea.removeAllViews();
                if (ListUtils.isEmpty(result.getData().getResult())) {
                    TextView tv = new TextView(getActivity());
                    tv.setGravity(Gravity.CENTER);
                    tv.setText("暂时没有动态！");
                    tv.setMinHeight(UIUtils.convertDpToPixel(60, getActivity()));
                    tv.setTextColor(getResources().getColor(R.color.text_74));
                    panelDynamicArea.addView(tv);
                } else {
                    for (int i = 0; i < result.getData().getResult().size(); i++) {
                        LinearLayout convertView = (LinearLayout) LayoutInflater.from(getActivity()).inflate(R.layout.item_dynamic_index, null);
                        ImageView ivHead = (ImageView) convertView.findViewById(R.id.iv_head);
                        TextView tvName = (TextView) convertView.findViewById(R.id.tv_name);
                        TextView tvDate = (TextView) convertView.findViewById(R.id.tv_date);
                        TextView tvContent = (TextView) convertView.findViewById(R.id.tv_content);
                        DynamicBean dynamicBean = result.getData().getResult().get(i);
                        ImageLoaderUtils.displayImageForDefaultHead(ivHead, JDUtils.getRemoteImagePath(dynamicBean.getIcon()));
                        tvName.setText(dynamicBean.getName());
                        tvDate.setText(JDUtils.formatDate(dynamicBean.getCreateTime(), "yyyy-MM-dd HH:mm"));
                        tvContent.setText(dynamicBean.getContent());
                        final int position = i;
                        convertView.setOnClickListener(new OnContinuousClickListener() {
                            @Override
                            public void onContinuousClick(View v) {
                                Intent intent = new Intent(getActivity(), DynamicDetailActivity.class);
                                DynamicDetailActivity.dynamicBeanList = result.getData().getResult();
                                intent.putExtra(DynamicDetailActivity.KEY_DYNAMIC_INDEX, position);
                                intent.putExtra(DynamicDetailActivity.KEY_DYNAMIC_PAGE_INDEX, 1);
                                startActivity(intent);
                            }
                        });
                        if (i != result.getData().getResult().size() - 1) {
                            View view = new View(getActivity());
                            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 1);
                            view.setLayoutParams(params);
                            view.setBackgroundColor(getResources().getColor(R.color.divider_color));
                            convertView.addView(view);
                        } else {
                        }
                        panelDynamicArea.addView(convertView);
                    }
                }
            }
        }
    });
}
}


@OnClick(R.id.panel_visit_area)
void showReservation() {
Intent intent = new Intent(getActivity(), FrontMeasureActivity.class);
startActivity(intent);
}

@OnClick(R.id.panel_chat_room_area)
void showChatGroup() {
Intent intent = new Intent(getActivity(), MidMeasureActivity.class);
startActivity(intent);

}

@OnClick(R.id.panel_answers_area)
void showAnswer() {
Intent intent = new Intent(getActivity(), BackMeasureActivity.class);
startActivity(intent);

}

    @OnClick(R.id.constant_ll)
    void constantMeasure() {
        Intent intent = new Intent(getActivity(), ConstantActivity.class);
        startActivity(intent);

    }

@Override
public void onDestroy() {
super.onDestroy();
}

private class RollingPageAdapter extends PagerAdapter {
@Override
public boolean isViewFromObject(View arg0, Object arg1) {
    return arg0 == arg1;
}

@Override
public int getCount() {
    return rollingImageList.size() > 1 ? Integer.MAX_VALUE : rollingImageList.size();
}

@Override
public Object instantiateItem(ViewGroup container, int position) {
    int pos = position % rollingImageList.size();
    if (imageViewList.size() - 1 < pos) {
        ImageView iv = new ImageView(getActivity());
        iv.setLayoutParams(new ViewGroup.LayoutParams(width, height));
        iv.setScaleType(ImageView.ScaleType.CENTER_CROP);
        ImageLoaderUtils.displayImageForIv(iv, JDUtils.getRemoteImagePath(rollingImageList.get(pos)), rollingDisplayOpts);
        imageViewList.add(iv);
    }
    ImageView view = null;
    if (imageViewList.size() <= pos) {
        view = imageViewList.get(imageViewList.size() - 1);
    } else {
        view = imageViewList.get(pos);
    }
    ViewParent vp = view.getParent();
    if (vp != null) {
        ViewGroup parent = (ViewGroup) vp;
        parent.removeView(view);
    }

    container.addView(view);
    return view;
}

@Override
public void destroyItem(ViewGroup container, int position,
                        Object object) {
}
}

private class PageChangedListener implements ViewPager.OnPageChangeListener {

@Override
public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
}

@Override
public void onPageSelected(int position) {
    if (mIndicatorPanel != null && mIndicatorPanel.getChildCount() > 0) {
        int pos = position % mIndicatorPanel.getChildCount();
        for (int i = 0; i < mIndicatorPanel.getChildCount(); i++) {
            ImageView iv = (ImageView) mIndicatorPanel.getChildAt(i);
            if (i == pos) {
                iv.setImageResource(R.drawable.pager_indicator_selected);
            } else {
                iv.setImageResource(R.drawable.pager_indicator_nor);
            }
        }
    }
}

@Override
public void onPageScrollStateChanged(int state) {

}
}
}
