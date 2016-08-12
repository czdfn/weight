package com.chengsi.weightcalc.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;

import com.chengsi.weightcalc.R;
import com.chengsi.weightcalc.widget.dialog.FanrDialog;
import com.chengsi.weightcalc.widget.dialog.FanrTipsDialog;

public class ExitDialogActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exit_dialog);
        final FanrTipsDialog dialog = FanrTipsDialog.getInstance();
        dialog.setCancelable(false);
        dialog.showTipsContent(getSupportFragmentManager(), "该账号已在其他设备登录，当前账号将被退出", new FanrDialog.OnFanrDismissListener() {
            @Override
            public void onDismiss(DialogFragment dialogFragment) {
                dialog.dismiss();
                Intent intent = new Intent(ExitDialogActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }
}
