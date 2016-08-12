package com.chengsi.weightcalc.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;

import com.chengsi.weightcalc.R;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class GuidePageActivity extends BaseActivity {

    @InjectView(R.id.webview)
    WebView webview;
    @InjectView(R.id.skip_btn)
    Button skipbtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide_page);
        setMyTitle("使用指南");
        ButterKnife.inject(this);
        WebSettings settings = webview.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setJavaScriptCanOpenWindowsAutomatically(true);
        settings.setUseWideViewPort(true);
        settings.setBuiltInZoomControls(true);
        settings.setAllowFileAccess(true);
        settings.setDisplayZoomControls(false);
        settings.setSupportZoom(true);
        settings.setLoadWithOverviewMode(true);
        webview.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
        webview.loadUrl("http://120.24.79.125/MpWechat/medias/html/pregnantClass1446115804418.html");
        skipbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                jump2Main();
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_guide_page, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        jump2Main();
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        jump2Main();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK){
            jump2Main();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    public void jump2Main(){
        finish();
        startActivity(new Intent(this, MainActivity.class));
    }
}
