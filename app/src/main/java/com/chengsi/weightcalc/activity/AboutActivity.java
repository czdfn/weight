package com.chengsi.weightcalc.activity;

import android.os.Bundle;
import android.widget.TextView;

import com.chengsi.weightcalc.R;
import com.umeng.update.UmengUpdateAgent;
import com.umeng.update.UmengUpdateListener;
import com.umeng.update.UpdateResponse;
import com.umeng.update.UpdateStatus;

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

        UmengUpdateAgent.setUpdateListener(new UmengUpdateListener() {

            @Override
            public void onUpdateReturned(int arg0, UpdateResponse arg1) {
                switch (arg0) {
                    case UpdateStatus.No: // has no update
                        showToast("当前已是最新版本");
                        break;
                    case UpdateStatus.NoneWifi: // none wifi
                        showToast("没有wifi连接， 只在wifi下更新");
                        break;
                    case UpdateStatus.Timeout: // time out
                        showToast("无法连接网络服务器");
                        break;
                }
            }
        });
    }

    @OnClick(R.id.item_check_update)
    void checkUpdate(){
        UmengUpdateAgent.forceUpdate(this);
    }
}
