package com.chengsi.weightcalc.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.CheckBox;
import android.widget.Toast;

//import com.chengsi.pregnancy.DemoHXSDKHelper;
import com.chengsi.weightcalc.R;
import com.chengsi.weightcalc.constants.StorageConstants;
import com.chengsi.weightcalc.listener.OnContinuousClickListener;
import com.chengsi.weightcalc.storage.JDStorage;
import com.chengsi.weightcalc.widget.PreferenceRightDetailView;
import com.chengsi.weightcalc.widget.dialog.FanrAlertDialog;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import cn.jpush.android.api.JPushInterface;

public class SettingsActivity extends BaseActivity {


    @InjectView(R.id.cb_not_disturb)
    CheckBox cbNotDisturb;

    @InjectView(R.id.cb_not_notify)
    CheckBox cbNotNotify;

    @InjectView(R.id.item_mod_pwd)
    PreferenceRightDetailView itemModPwd;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    cbNotNotify.setChecked(true);
                    JDStorage.getInstance().storeBooleanValue(StorageConstants.KEY_IS_NOTIFY, cbNotNotify.isChecked());
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        ButterKnife.inject(this);
        cbNotDisturb.setChecked(JDStorage.getInstance().getBooleanValue(StorageConstants.KEY_IS_DISTURB, true));
        cbNotNotify.setChecked(JDStorage.getInstance().getBooleanValue(StorageConstants.KEY_IS_NOTIFY, false));
    }

    @OnClick(R.id.panel_not_disturb)
    void disturbClicked() {
        cbNotDisturb.setChecked(!cbNotDisturb.isChecked());
        JDStorage.getInstance().storeBooleanValue(StorageConstants.KEY_IS_DISTURB, cbNotDisturb.isChecked());
    }

    @OnClick(R.id.panel_not_notify)
    void setNotify() {

        if (!cbNotNotify.isChecked()) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    Message msg = Message.obtain();
                    msg.what = 1;
                    handler.sendMessage(msg);
                    JPushInterface.stopPush(getApplicationContext());
                }
            }).start();
            Toast.makeText(this, "推送服务已停止", Toast.LENGTH_SHORT).show();
        } else {
            cbNotNotify.setChecked(false);
            JDStorage.getInstance().storeBooleanValue(StorageConstants.KEY_IS_NOTIFY, cbNotNotify.isChecked());
            if (JPushInterface.isPushStopped(getApplicationContext())) {
                JPushInterface.resumePush(getApplicationContext());
                Toast.makeText(this, "推送服务已启动", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "推送服务正在运行", Toast.LENGTH_SHORT).show();
            }
        }

    }

    @OnClick(R.id.item_mod_pwd)
    void modPwd() {
        Intent in = new Intent(SettingsActivity.this, ModifyPwdActivity.class);
        startActivity(in);
    }

    @OnClick(R.id.btn_logout)
    void logout() {
        FanrAlertDialog dialog = FanrAlertDialog.getInstance();
        dialog.showAlertContent(getSupportFragmentManager(), "确定退出当前账号？", new OnContinuousClickListener() {
            @Override
            public void onContinuousClick(View v) {
//                application.userManager.resetUser(null);
                Intent intent = new Intent(SettingsActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }
}
