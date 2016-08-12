package com.chengsi.weightcalc.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.alibaba.fastjson.TypeReference;
import com.chengsi.weightcalc.R;
import com.chengsi.weightcalc.bean.BaseBean;
import com.chengsi.weightcalc.bean.HospitalBean;
import com.chengsi.weightcalc.http.JDHttpClient;
import com.chengsi.weightcalc.http.JDHttpResponseHandler;
import com.chengsi.weightcalc.widget.JDLoadingView;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class HospitalPageActivity extends BaseActivity {
    public static final String KEY_CURRENT_HOSPITAL = "KEY_CURRENT_HOSPITAL";

    @InjectView(R.id.webview)
    WebView webView;

    private HospitalBean hospitalBean;
    private Menu menu;

    private static final String APP_CACAHE_DIRNAME = "/webcache";
    private boolean isAttended = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hospital_page);
        ButterKnife.inject(this);
        setLoadingViewState(JDLoadingView.STATE_LOADING);
        hospitalBean = (HospitalBean) getIntent().getSerializableExtra(KEY_CURRENT_HOSPITAL);
        if (hospitalBean == null) {
            finish();
            return;
        }
        setMyTitle(hospitalBean.getName());
        getAttendList();
        initWebView();
        webView.setWebViewClient(new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);

                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                dismissLoadingView();
            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                super.onReceivedError(view, errorCode, description, failingUrl);
                if (failingUrl.equals(hospitalBean.getWebsite())) {
                    setLoadingViewState(JDLoadingView.STATE_FAILED);
                }
            }
        });
        Log.i("URL", "" + hospitalBean.getWebsite());
       if (TextUtils.isEmpty(hospitalBean.getWebsite())){
           JDHttpClient.getInstance().getHospitalDetail(this, hospitalBean.getId(), new JDHttpResponseHandler<HospitalBean>(new TypeReference<BaseBean<HospitalBean>>(){}){

               @Override
               public void onRequestCallback(BaseBean<HospitalBean> result) {
                   super.onRequestCallback(result);
                   if (result.isSuccess()){
                       hospitalBean = result.getData();
                       webView.loadUrl(hospitalBean.getWebsite());
                   }else{
                       setLoadingViewState(JDLoadingView.STATE_FAILED);
                   }
               }
           });
       }else{
           webView.loadUrl(hospitalBean.getWebsite());
       }
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && webView.canGoBack()) {
            webView.goBack();
            return true;
        }
        finish();
        return false;
    }
    private void initWebView(){
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setJavaScriptCanOpenWindowsAutomatically(true);
        settings.setUseWideViewPort(true);
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        settings.setBuiltInZoomControls(true);
        settings.setAllowFileAccess(true);
        settings.setDisplayZoomControls(false);
        settings.setDefaultFontSize(100);
        settings.setSupportZoom(true);
        settings.setLoadWithOverviewMode(true);
        settings.setCacheMode(WebSettings.LOAD_DEFAULT);
        settings.setDomStorageEnabled(true);
        settings.setDatabaseEnabled(true);
        String cacheDirPath = getFilesDir().getAbsolutePath()+APP_CACAHE_DIRNAME;
        settings.setDatabasePath(cacheDirPath);
        settings.setAppCachePath(cacheDirPath);
        settings.setAppCacheEnabled(true);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_hospital_page, menu);
        this.menu = menu;
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.menu_attend) {
            if (!isAttended) {
                attendHospital(1);
            } else {
                attendHospital(0);
            }
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void getAttendList() {
//        JDHttpClient.getInstance().reqAttendHospitalList(this, new JDHttpResponseHandler<List<HospitalBean>>(new TypeReference<BaseBean<List<HospitalBean>>>() {
//        }) {
//            @Override
//            public void onRequestCallback(BaseBean<List<HospitalBean>> result) {
//                super.onRequestCallback(result);
//                if (result.isSuccess()) {
//                    UserBean userBean = application.userManager.getUserBean();
//                    userBean.setAttenHospitalList(result.getData());
//                    application.userManager.resetUser(userBean);
//                    if (!ListUtils.isEmpty(result.getData())) {
//                        if (userBean.getAttenHospitalList().contains(hospitalBean)) {
//                            MenuItem menuItem = menu.findItem(R.id.menu_attend);
//                            menuItem.setTitle("取消关注");
//                            isAttended = true;
//                        }
//                    }
//                }
//            }
//        });
    }

    private void attendHospital(final int type) {
        setLoadingViewState(JDLoadingView.STATE_LOADING);
//        JDHttpClient.getInstance().reqAttendHospital(this, hospitalBean.getId(), type, new JDHttpResponseHandler<String>(new TypeReference<BaseBean<String>>() {
//        }) {
//            @Override
//            public void onRequestCallback(BaseBean<String> result) {
//                super.onRequestCallback(result);
//                dismissLoadingView();
//                if (result.isSuccess()) {
//                    MenuItem menuItem = menu.findItem(R.id.menu_attend);
////                    UserBean userBean = application.userManager.getUserBean();
//                    if (type == 0) {
//                        userBean.getAttenHospitalList().remove(hospitalBean);
//                        menuItem.setTitle(getString(R.string.action_add_attend));
//                        showToast("取消关注成功");
//                        isAttended = false;
//                    } else {
//                        userBean.getAttenHospitalList().add(hospitalBean);
//                        menuItem.setTitle("取消关注");
//                        showToast("关注成功");
//                        isAttended = true;
//                    }
//                    application.userManager.resetUser(userBean);
//
//                } else {
//                    showToast(result.getMessage());
//                }
//            }
//        });
    }
}
