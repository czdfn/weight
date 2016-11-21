package com.chengsi.weightcalc.activity;

import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;

import com.chengsi.weightcalc.R;
import com.chengsi.weightcalc.widget.CustomShareBoard;
import com.chengsi.weightcalc.widget.PreferenceRightDetailView;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class ShareActivity extends BaseActivity {

    @InjectView(R.id.share_tv)
    PreferenceRightDetailView shareTv;
    @InjectView(R.id.qr_code)
    ImageView qrIv;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share);
        ButterKnife.inject(this);
        setMyTitle("分享");
    }

}
