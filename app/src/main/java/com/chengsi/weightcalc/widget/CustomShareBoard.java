/**
 * 
 */

package com.chengsi.weightcalc.widget;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.PopupWindow;

import com.chengsi.weightcalc.R;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.bean.SocializeEntity;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.controller.listener.SocializeListeners.SnsPostListener;

/**
 * 
 */
public class CustomShareBoard extends PopupWindow implements OnClickListener {

    private UMSocialService mController = UMServiceFactory.getUMSocialService("com.umeng.share");
    private Activity mActivity;

    public CustomShareBoard(Activity activity) {
        super(activity);
        this.mActivity = activity;
        initView(activity);
    }

    @SuppressWarnings("deprecation")
    private void initView(Context context) {
        View rootView = LayoutInflater.from(context).inflate(R.layout.custom_board, null);
        rootView.findViewById(R.id.wechat).setOnClickListener(this);
        rootView.findViewById(R.id.qq).setOnClickListener(this);
        setContentView(rootView);
        setWidth(LayoutParams.MATCH_PARENT);
        setHeight(LayoutParams.WRAP_CONTENT);
        setFocusable(true);
        setBackgroundDrawable(new BitmapDrawable());
        setTouchable(true);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.wechat:
                performShare(SHARE_MEDIA.WEIXIN);
                break;
            case R.id.qq:
                performShare(SHARE_MEDIA.QQ);
                break;
            default:
                break;
        }
    }

    private void performShare(SHARE_MEDIA platform) {
        mController.postShare(mActivity, platform, new SnsPostListener() {

            @Override
            public void onStart() {
            }

            @Override
            public void onComplete(SHARE_MEDIA platform, int eCode, SocializeEntity entity) {
//                String showText = platform.toString();
//                if (eCode == StatusCode.ST_CODE_SUCCESSED) {
//                    showText += "分享成功";
//                } else {
//                    showText += "分享失败";
//                }
//                Toast.makeText(mActivity, showText, Toast.LENGTH_SHORT).show();
            }
        });
    }

}
