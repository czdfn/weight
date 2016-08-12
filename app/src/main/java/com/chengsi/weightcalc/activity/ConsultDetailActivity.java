package com.chengsi.weightcalc.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chengsi.weightcalc.R;
import com.chengsi.weightcalc.bean.ConsultBean;
import com.chengsi.weightcalc.listener.OnContinuousClickListener;
import com.chengsi.weightcalc.utils.JDUtils;
import com.chengsi.weightcalc.utils.UIUtils;
import com.chengsi.weightcalc.utils.imageloader.ImageLoaderUtils;
import com.chengsi.weightcalc.widget.SquareImageView;

import cn.jiadao.corelibs.utils.ArrayUtils;

public class ConsultDetailActivity extends BaseActivity {

    public static final String KEY_CONSULT_BEAN = "KEY_CONSULT_BEAN";

    private ConsultBean consultBean;

    private LinearLayout contentView;

    private TextView tvTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        contentView = (LinearLayout) mInflater.inflate(R.layout.activity_consult_detail, null);
        setContentView(contentView);
        tvTitle = (TextView) findViewById(R.id.tv_consult_title);
        initDataSource();
    }

    protected void initDataSource() {
        consultBean = (ConsultBean) getIntent().getSerializableExtra(KEY_CONSULT_BEAN);
        tvTitle.setText(consultBean.getTitle());
        if (consultBean == null){
            finish();
            return;
        }else{
            contentView.addView(generateContentView("咨询内容", JDUtils.formatDate(consultBean.getAnswerTime(), "yyyy-MM-dd HH:mm"), consultBean.getContent(), false));
            if (consultBean.isAnswer()){

                View view = generateContentView("回复内容", JDUtils.formatDate(consultBean.getReplyTime(), "yyyy-MM-dd HH:mm"), consultBean.getReplyContent(), true);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                params.topMargin = UIUtils.convertDpToPixel(8, this);
                contentView.addView(view, params);
            }
        }
    }

    private View generateContentView(String status, String date, String content, boolean isReply){
        View view = mInflater.inflate(R.layout.item_consult_content, null);
        TextView tvConsultStatus = (TextView) view.findViewById(R.id.tv_consult_status);
        TextView tvConsultDate = (TextView) view.findViewById(R.id.tv_consult_date);
        TextView tvConsultContent = (TextView) view.findViewById(R.id.tv_consult_content);
        tvConsultStatus.setText(status);
        tvConsultDate.setText(date);
        tvConsultContent.setText(content);
        if (!isReply){
            final LinearLayout layout = (LinearLayout) view.findViewById(R.id.panel_image_content);
            int width = UIUtils.getScreenSize(ConsultDetailActivity.this).x - UIUtils.convertDpToPixel(32, ConsultDetailActivity.this);
            if (!TextUtils.isEmpty(consultBean.getImageList())){
                final String[] arr = consultBean.getImageList().split(",");
                int padding = UIUtils.convertDpToPixel(6, ConsultDetailActivity.this);
                LinearLayout linearLayout = initLayout(layout);
                for (int i = 0; i < arr.length; i ++){
                    ImageView imageView;
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    if (arr.length == 1){
                        imageView = new ImageView(ConsultDetailActivity.this);
                        params.height = params.width = UIUtils.convertDpToPixel(180, ConsultDetailActivity.this);
                    }else{
                        params.width = (width - 3 * padding) / 4;
                        imageView = new SquareImageView(ConsultDetailActivity.this);
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
                            Intent intent = new Intent(ConsultDetailActivity.this, ImagePreviewActivity.class);
                            intent.putStringArrayListExtra(ImagePreviewActivity.KEY_IMAGE_URLLIST, ArrayUtils.toList(arr));
                            intent.putExtra(ImagePreviewActivity.KEY_IMAGE_CURRENT_URL, url);
                            startActivity(intent);
                        }
                    });
                    ImageLoaderUtils.displayImageForIv(imageView, JDUtils.getRemoteImagePath(arr[i]));
                }
            }
        }
        return view;
    }

    private LinearLayout initLayout(LinearLayout layout){
        LinearLayout linearLayout = new LinearLayout(ConsultDetailActivity.this);
        linearLayout.setOrientation(LinearLayout.HORIZONTAL);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.bottomMargin = UIUtils.convertDpToPixel(8, ConsultDetailActivity.this);
        linearLayout.setLayoutParams(params);
        layout.addView(linearLayout);
        return linearLayout;
    }
}
