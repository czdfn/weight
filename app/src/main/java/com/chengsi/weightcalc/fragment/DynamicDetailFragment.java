package com.chengsi.weightcalc.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chengsi.weightcalc.R;
import com.chengsi.weightcalc.activity.ImagePreviewActivity;
import com.chengsi.weightcalc.bean.DynamicBean;
import com.chengsi.weightcalc.listener.OnContinuousClickListener;
import com.chengsi.weightcalc.utils.JDUtils;
import com.chengsi.weightcalc.utils.UIUtils;
import com.chengsi.weightcalc.utils.imageloader.ImageLoaderUtils;
import com.chengsi.weightcalc.widget.SquareImageView;

import butterknife.ButterKnife;
import butterknife.InjectView;
import cn.jiadao.corelibs.utils.ArrayUtils;

public class DynamicDetailFragment extends BaseFragment {

    public static final String KEY_DYNAMIC_DETAIL_BEAN = "KEY_DYNAMIC_DETAIL_BEAN";

    private DynamicBean myDynamicBean = null;

    @InjectView(R.id.iv_head)
    ImageView ivHead;
    @InjectView(R.id.tv_name)
    TextView tvName;
    @InjectView(R.id.tv_date)
    TextView tvDate;
    @InjectView(R.id.tv_content)
    TextView tvContent;

    @InjectView(R.id.panel_image_content)
    LinearLayout panelImageContent;

    public static DynamicDetailFragment newInstance(DynamicBean dynamicBean) {
        DynamicDetailFragment fragment = new DynamicDetailFragment();
        Bundle args = new Bundle();
        args.putSerializable(KEY_DYNAMIC_DETAIL_BEAN, dynamicBean);
        fragment.setArguments(args);
        return fragment;
    }

    public DynamicDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            myDynamicBean = (DynamicBean) getArguments().getSerializable(KEY_DYNAMIC_DETAIL_BEAN);
        }
    }

    @Override
    public View getContentView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dynamic_detail, container, false);
        ButterKnife.inject(this, view);

        initViews();
        return view;
    }

    private void initViews() {
        if (myDynamicBean != null){
            ImageLoaderUtils.displayImageForDefaultHead(ivHead, JDUtils.getRemoteImagePath(myDynamicBean.getIcon()));
            tvName.setText(myDynamicBean.getName());
            tvContent.setText(myDynamicBean.getContent());
            tvDate.setText(JDUtils.formatDate(myDynamicBean.getCreateTime(), "yyyy-MM-dd HH:mm"));
            panelImageContent.removeAllViews();
            panelImageContent.removeAllViews();
            int width = UIUtils.getScreenSize(getActivity()).x - UIUtils.convertDpToPixel(32, getActivity());
            if (!TextUtils.isEmpty(myDynamicBean.getUrl())){
                final String[] arr = myDynamicBean.getUrl().split(",");
                int padding = UIUtils.convertDpToPixel(6, getActivity());
                LinearLayout linearLayout = initLayout(panelImageContent);
                for (int i = 0; i < arr.length; i ++){
                    ImageView imageView;
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    if (arr.length == 1){
                        imageView = new ImageView(getActivity());
                        params.height = params.width = UIUtils.convertDpToPixel(width, getActivity());
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
                        linearLayout = initLayout(panelImageContent);
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
        }
    }

    private LinearLayout initLayout(LinearLayout layout){
        LinearLayout linearLayout = new LinearLayout(getActivity());
        linearLayout.setOrientation(LinearLayout.HORIZONTAL);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.bottomMargin = UIUtils.convertDpToPixel(8, getActivity());
        linearLayout.setLayoutParams(params);
        layout.addView(linearLayout);
        return linearLayout;
    }
}
