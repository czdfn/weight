package com.chengsi.weightcalc.activity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.chengsi.weightcalc.R;
import com.chengsi.weightcalc.widget.JDLoadingView;

import java.io.File;

public class TreatHistoryWebActivity extends BaseActivity {


    private WebView webview;
    private static final String APP_CACAHE_DIRNAME = "/webcache";
    private static final String WEBVIEW_CACAHE_DIRNAME = "/webviewCache";

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (isContinuousClick()) {
            return false;
        }
        return super.onTouchEvent(event);
    }

    private int maxTime = 1000;

    private boolean isContinuousClick(){
        long lastClickTime = 0;
        long time = System.currentTimeMillis();
        long sub = time - lastClickTime;
        Log.i("TOUCH", "click");
        if ( 0 < sub && sub < maxTime) {
            return true;
        }
        lastClickTime = time;
        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservation_web);
        showToolbar();
        Log.i("FILEPATH", getFilesDir().getAbsolutePath());
        setMyTitle("治疗历史");
        String token = getIntent().getStringExtra("token");
        String url = "http://www.haoyunt.com/MpWechat/hollybeacon/weixin/information/treathis.jsp?token="+token;
        webview = (WebView)findViewById(R.id.reservationwv);
        initWebView();
        setLoadingViewState(JDLoadingView.STATE_LOADING);

        webview.setWebViewClient(new WebViewClient() {
            @Override
            public void onLoadResource(WebView view, String url) {
                Log.i(TAG, "onLoadResource url=" + url);
                dismissLoadingView();
                super.onLoadResource(view,url);
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                Log.e(TAG, "onPageStarted");
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                Log.e(TAG, "onPageFinished");

            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
        webview.loadUrl(url);
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && webview.canGoBack()) {
            webview.goBack();
            return true;
        }
        finish();
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (onBackAction() && webview.canGoBack()) {
            webview.goBack();
            return true;
        }
        finish();
        return false;
    }

    private void initWebView(){
        WebSettings settings = webview.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setJavaScriptCanOpenWindowsAutomatically(true);
        settings.setUseWideViewPort(true);
//        settings.setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);
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
    protected void onDestroy() {
        clearWebViewCache();
        super.onDestroy();
    }

    public void clearWebViewCache(){
        try{
            deleteDatabase("webview.db");
            deleteDatabase("webviewCache.db");
        }catch (Exception e){
            e.printStackTrace();
        }
        String webDirPath = getFilesDir().getAbsolutePath()+WEBVIEW_CACAHE_DIRNAME;
        File appCacheDir = new File(getFilesDir().getAbsolutePath()+APP_CACAHE_DIRNAME);

        File webviewCacheDir  = new File(webDirPath);

        if(webviewCacheDir.exists()){
            deleteFile(webviewCacheDir);
        }
        if(appCacheDir.exists()){
            deleteFile(appCacheDir);
        }
    }

    public void deleteFile(File file){
        if(file.exists()){
            if(file.isFile()){
                file.delete();
            }else if(file.isDirectory()){
                File files[] = file.listFiles();
                for(int i=0;i<files.length;i++){
                    deleteFile(files[i]);
                }
            }
            file.delete();
        }else{
            Log.e(TAG, "delete file no exists " + file.getAbsolutePath());
        }
    }

}
