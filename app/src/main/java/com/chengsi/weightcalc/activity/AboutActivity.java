package com.chengsi.weightcalc.activity;

import android.os.Bundle;
import android.widget.TextView;

import com.chengsi.weightcalc.R;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class AboutActivity extends BaseActivity {


    @InjectView(R.id.version_tv)
    TextView tvVersion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        ButterKnife.inject(this);

        String versionName = application.getVersionName();
        if(versionName != null) {
            tvVersion.setText("当前版本：" + versionName);
        }

    }

    @OnClick(R.id.item_check_update)
    void checkUpdate(){
    }
}
