package com.chengsi.weightcalc.activity;

import android.content.Intent;
import android.os.Bundle;

import com.chengsi.weightcalc.R;
import com.chengsi.weightcalc.utils.Position;

public class WelcomeActivity extends BaseActivity {

    private Position mPosition;
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_welcome);

            hideToolbar();
            application.getMainHandler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    startActivity(new Intent(WelcomeActivity.this, MainActivity.class));
                    finish();
                }
            }, 2000L);
//            if (!application.userManager.isLogin()){
//                application.getMainHandler().postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        startActivity(new Intent(WelcomeActivity.this, MainActivity.class));
//                        finish();
//                    }
//                }, 2000L);
//            }else{
//                application.getMainHandler().postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        startActivity(new Intent(WelcomeActivity.this, MainActivity.class));
//                        finish();
//                    }
//                }, 2000L);
//            }

        mPosition = new Position(getApplicationContext());
    }

//    @Override
//    public void onUserChanged(UserBean userBean) {
//
//    }
}
