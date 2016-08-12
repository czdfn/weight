package com.chengsi.weightcalc.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
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

public class HospitalQuestionActivity extends BaseActivity {
    public static final String KEY_CURRENT_HOSPITAL = "KEY_CURRENT_HOSPITAL";
    @InjectView(R.id.webview)
       WebView webView;
    private HospitalBean hospitalBean;

    private static final String APP_CACAHE_DIRNAME = "/webcache";
    private boolean isAttended = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hospital_question);
        ButterKnife.inject(this);
        setLoadingViewState(JDLoadingView.STATE_LOADING);
        hospitalBean = (HospitalBean) getIntent().getSerializableExtra(KEY_CURRENT_HOSPITAL);
        if (hospitalBean == null) {
            finish();
            return;
        }
        initWebView();
        setMyTitle(hospitalBean.getName());
        //设置URL跳转事件
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
                if (failingUrl.equals(hospitalBean.getWebsite2())) {
                    setLoadingViewState(JDLoadingView.STATE_FAILED);
                }
            }
        });
        //获取医院问答的URL.
        Log.i("URL", "" + hospitalBean.getWebsite2());
        if (TextUtils.isEmpty(hospitalBean.getWebsite2())){
            JDHttpClient.getInstance().getHospitalDetail(this, hospitalBean.getId(), new JDHttpResponseHandler<HospitalBean>(new TypeReference<BaseBean<HospitalBean>>(){}){

                @Override
                public void onRequestCallback(BaseBean<HospitalBean> result) {
                    super.onRequestCallback(result);
                    if (result.isSuccess()){
                        hospitalBean = result.getData();
                        Log.i("HospitalQuestion", " url = " + hospitalBean.getWebsite2());
                        webView.loadUrl(hospitalBean.getWebsite2());
                    }else{
                        setLoadingViewState(JDLoadingView.STATE_FAILED);
                    }
                }
            });
        }else{
            webView.loadUrl(hospitalBean.getWebsite2());
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
}
