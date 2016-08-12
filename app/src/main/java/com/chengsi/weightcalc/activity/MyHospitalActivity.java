package com.chengsi.weightcalc.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.chengsi.weightcalc.R;
import com.chengsi.weightcalc.widget.JDLoadingView;
import com.chengsi.weightcalc.widget.PreferenceRightDetailView;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class MyHospitalActivity extends BaseActivity {

    @InjectView(R.id.item_my_hospital)
    PreferenceRightDetailView item_my_hospital;
    @InjectView(R.id.tv_my_hospital_title)
    TextView tvMyHospital;

    @InjectView(R.id.btn_bind)
    Button btnBind;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_hospital);
        ButterKnife.inject(this);
        initViews();
    }

    private void initViews() {
//        if (application.userManager.getUserBean().getHospital() != null){
//            btnBind.setText("解除绑定");
//            tvMyHospital.setVisibility(View.VISIBLE);
//            item_my_hospital.setVisibility(View.VISIBLE);
//            item_my_hospital.setTitle(application.userManager.getUserBean().getHospital().getAbbreviation());
//            item_my_hospital.setContent(application.userManager.getUserBean().getVisitCard());
//            btnBind.setOnClickListener(new OnContinuousClickListener() {
//                @Override
//                public void onContinuousClick(View v) {
//                    final FanrAlertDialog dialog = FanrAlertDialog.getInstance();
//                    dialog.showAlertContent(getSupportFragmentManager(), "确定要解除绑定么？", new OnContinuousClickListener() {
//                        @Override
//                        public void onContinuousClick(View v) {
//                            dialog.dismiss();
//                            unBindHospital();
//                        }
//                    });
//                }
//            });
//        } else {
//            btnBind.setText("绑定就诊中心");
//            tvMyHospital.setVisibility(View.GONE);
//            item_my_hospital.setVisibility(View.GONE);
//            btnBind.setOnClickListener(new OnContinuousClickListener() {
//                @Override
//                public void onContinuousClick(View v) {
//                    Intent intent = new Intent(MyHospitalActivity.this, BindHospitalActivity.class);
//                    startActivityForResult(intent, 0);
//                }
//            });
//        }
    }

    private void unBindHospital() {

        setLoadingViewState(JDLoadingView.STATE_LOADING);
//        JDHttpClient.getInstance().unBindHospital(this, application.userManager.getUserBean().getHospital().getId(), application.userManager.getUserBean().getVisitCard(), application.userManager.getUserBean().getIdNo(), new JDHttpResponseHandler<String>(new TypeReference<BaseBean<String>>() {
//        }) {
//
//            @Override
//            public void onRequestCallback(BaseBean<String> result) {
//                super.onRequestCallback(result);
//                dismissLoadingView();
//                if (result.isSuccess()) {
//                    showToast("操作成功");
//                    application.userManager.getUserBean().setVisitCard(null);
//                    application.userManager.getUserBean().setHospital(null);
//                    application.userManager.resetUser();
//                    initViews();
//                } else {
//                    showToast(result.getMessage());
//                }
//            }
//        });
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.menu_my_hospital, menu);
//        return true;
//    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK){
            initViews();
        }
    }
}
