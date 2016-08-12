package com.chengsi.weightcalc.activity;

import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;

import com.chengsi.weightcalc.R;
import com.chengsi.weightcalc.widget.CustomShareBoard;
import com.chengsi.weightcalc.widget.PreferenceRightDetailView;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.media.QQShareContent;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.sso.UMQQSsoHandler;
import com.umeng.socialize.weixin.controller.UMWXHandler;
import com.umeng.socialize.weixin.media.WeiXinShareContent;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class ShareActivity extends BaseActivity {

    @InjectView(R.id.share_tv)
    PreferenceRightDetailView shareTv;
    @InjectView(R.id.qr_code)
    ImageView qrIv;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share);
        ButterKnife.inject(this);
        setMyTitle("分享");
        configPlatforms();
        setplatShow();
        shareTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                postShare();
            }
        });
    }

    private final UMSocialService mController = UMServiceFactory.getUMSocialService("com.umeng.share");

    private void configPlatforms(){
        // 添加QQ、QZone平台
        addQQPlatform();
        // 添加微信、微信朋友圈平台
        addWXPlatform();
    }
    private void addQQPlatform() {
        String appId = "1105051247";
        String appKey = "EryLMlAYZ8EwCqSI";
        // 添加QQ支持, 并且设置QQ分享内容的target url
        UMQQSsoHandler qqSsoHandler = new UMQQSsoHandler(this,
                appId, appKey);
        qqSsoHandler.setTargetUrl("http://www.umeng.com/social");
        qqSsoHandler.addToSocialSDK();
    }
    private void addWXPlatform() {
        // 注意：在微信授权的时候，必须传递appSecret
        // wx967daebe835fbeac是你在微信开发平台注册应用的AppID, 这里需要替换成你注册的AppID
        String appId = "wx3be199c0409d59c7";
        String appSecret = "a0fc351427c8195a7b9c9ff7fefa9a39";
        // 添加微信平台
        UMWXHandler wxHandler = new UMWXHandler(this, appId, appSecret);
        wxHandler.showCompressToast(false);
        wxHandler.addToSocialSDK();
    }
//    private String url =
    private void setplatShow(){
        UMImage urlImage = new UMImage(this,R.drawable.ic_launcher);
        mController.getConfig().closeToast();
        WeiXinShareContent weixinContent = new WeiXinShareContent();
        weixinContent
                .setShareContent("在迎接宝贝的路上有些坎坷，从今天起，让我们陪伴您。");
        weixinContent.setTitle("中山一院助孕宝圆你一生好孕");
        weixinContent.setTargetUrl("http://t.cn/Rbm37ry");
        weixinContent.setShareMedia(urlImage);
        mController.setShareMedia(weixinContent);

        QQShareContent qqShareContent = new QQShareContent();
        qqShareContent.setShareContent("在迎接宝贝的路上有些坎坷，从今天起，让我们陪伴您。");
        qqShareContent.setTitle("中山一院助孕宝圆你一生好孕");
        qqShareContent.setShareImage(new UMImage(this,R.drawable.ic_launcher));
        qqShareContent.setTargetUrl("http://t.cn/Rbm37ry");
        mController.setShareMedia(qqShareContent);
    }

    private void postShare() {
        CustomShareBoard shareBoard = new CustomShareBoard(this);
        shareBoard.showAtLocation(this.getWindow().getDecorView(), Gravity.BOTTOM, 0, 0);
    }

}
