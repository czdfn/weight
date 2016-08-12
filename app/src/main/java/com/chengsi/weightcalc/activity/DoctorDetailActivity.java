package com.chengsi.weightcalc.activity;

import android.net.http.SslError;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.SslErrorHandler;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.alibaba.fastjson.TypeReference;
import com.chengsi.weightcalc.R;
import com.chengsi.weightcalc.bean.BaseBean;
import com.chengsi.weightcalc.bean.DoctorBean;
import com.chengsi.weightcalc.http.HttpConstants;
import com.chengsi.weightcalc.http.JDHttpClient;
import com.chengsi.weightcalc.http.JDHttpResponseHandler;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class DoctorDetailActivity extends BaseActivity {
    public static final String KEY_CURRENT_DOCTOR = "KEY_CURRENT_DOCTOR";

    @InjectView(R.id.webview)
    WebView webView;
    private DoctorBean doctorBean;
    private Menu menu;

    private boolean isAttended = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_detail);
        ButterKnife.inject(this);
        doctorBean = (DoctorBean) getIntent().getSerializableExtra(KEY_CURRENT_DOCTOR);
        if (doctorBean == null) {
            finish();
            return;
        }
        setMyTitle(doctorBean.getRealName());
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setJavaScriptCanOpenWindowsAutomatically(true);
        settings.setUseWideViewPort(true);
        settings.setBuiltInZoomControls(true);
        settings.setAllowFileAccess(true);
        settings.setDisplayZoomControls(false);
        settings.setSupportZoom(true);
        settings.setLoadWithOverviewMode(true);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error){
                handler.proceed();
            }
        });
        webView.loadUrl(HttpConstants.HTTP_BASE_URL + "/doctor/todisplay.do?id="+doctorBean.getId());
        getAttendList();

    }


    private void getAttendList() {
//        JDHttpClient.getInstance().reqAttentDoctorList(this, new JDHttpResponseHandler<List<DoctorBean>>(new TypeReference<BaseBean<List<DoctorBean>>>() {
//        }) {
//            @Override
//            public void onRequestCallback(BaseBean<List<DoctorBean>> result) {
//                super.onRequestCallback(result);
//                if (result.isSuccess()) {
//                    UserBean userBean = application.userManager.getUserBean();
//                    userBean.setAttenDoctorList(result.getData());
//                    application.userManager.resetUser(userBean);
//                    if (!ListUtils.isEmpty(result.getData())) {
//                        if (userBean.getAttenDoctorList().contains(doctorBean)) {
//                            MenuItem menuItem = menu.findItem(R.id.action_add_attention);
//                            menuItem.setTitle("取消关注");
//                            isAttended = true;
//                        }else{
//                            MenuItem menuItem = menu.findItem(R.id.action_add_attention);
//                            menuItem.setTitle("添加关注");
//                            isAttended = false;
//                        }
//                    }
//                }
//            }
//        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_doctor_detail, menu);
        this.menu = menu;
//        if (application.userManager.getUserBean().getAttenDoctorList().contains(doctorBean)) {
//            MenuItem menuItem = menu.findItem(R.id.action_add_attention);
//            menuItem.setTitle("取消关注");
//            isAttended = true;
//        }else{
//            MenuItem menuItem = menu.findItem(R.id.action_add_attention);
//            menuItem.setTitle("添加关注");
//            isAttended = false;
//        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_add_attention) {
            if (!isAttended) {
                followDoctor();
            } else {
                unFollowDoctor();
            }
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void unFollowDoctor() {
        JDHttpClient.getInstance().reqCancelAttentDoctor(this, doctorBean.getId(), new JDHttpResponseHandler<String>(new TypeReference<BaseBean<String>>() {
        }) {
            @Override
            public void onRequestCallback(BaseBean<String> result) {
                super.onRequestCallback(result);
                if (result.isSuccess()) {
                    isAttended = false;
                    MenuItem menuItem = menu.findItem(R.id.action_add_attention);
                    menuItem.setTitle("添加关注");
                } else {
                    showToast(result.getMessage());
                }
            }
        });
    }

    private void followDoctor() {
        JDHttpClient.getInstance().reqAddAttentDoctor(this, doctorBean.getId(), new JDHttpResponseHandler<String>(new TypeReference<BaseBean<String>>() {
        }) {
            @Override
            public void onRequestCallback(BaseBean<String> result) {
                super.onRequestCallback(result);
                if (result.isSuccess()) {
                    isAttended = true;
                    MenuItem menuItem = menu.findItem(R.id.action_add_attention);
                    menuItem.setTitle("取消关注");
                } else {
                    showToast(result.getMessage());
                }
            }
        });
    }
}
