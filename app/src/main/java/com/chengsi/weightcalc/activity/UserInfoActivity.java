package com.chengsi.weightcalc.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.alibaba.fastjson.TypeReference;
import com.chengsi.weightcalc.R;
import com.chengsi.weightcalc.bean.BaseBean;
import com.chengsi.weightcalc.bean.FileUploadBean;
import com.chengsi.weightcalc.bean.UserBean;
import com.chengsi.weightcalc.http.JDHttpClient;
import com.chengsi.weightcalc.http.JDHttpResponseHandler;
import com.chengsi.weightcalc.listener.CaptureImageFinishedListener;
import com.chengsi.weightcalc.listener.CaptureImageLisenter;
import com.chengsi.weightcalc.utils.BitmapUtils;
import com.chengsi.weightcalc.utils.CaptureMedia;
import com.chengsi.weightcalc.utils.JDUtils;
import com.chengsi.weightcalc.utils.UIUtils;
import com.chengsi.weightcalc.utils.imageloader.ImageLoaderUtils;
import com.chengsi.weightcalc.widget.PreferenceRightDetailView;

import java.io.File;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import cn.jiadao.corelibs.utils.ListUtils;

public class UserInfoActivity extends BaseActivity implements CaptureImageFinishedListener {

    public static final String KEY_USER_INFO = "KEY_USER_INFO";

    private UserBean userBean;

    @InjectView(R.id.panel_user_head)
    View panelUserInfo;
    @InjectView(R.id.item_my_nickname)
    PreferenceRightDetailView itemMyNickName;
    @InjectView(R.id.item_my_sex)
    PreferenceRightDetailView itemMySex;
    @InjectView(R.id.item_my_realname)
    PreferenceRightDetailView itemMyRealName;
    @InjectView(R.id.item_my_idcard)
    PreferenceRightDetailView itemMyIdCard;

    private Dialog loadingDialog;

    private String avatar;
    private int mSex;//0 空 1 男  2女

    @InjectView(R.id.iv_user_head)
    ImageView ivUserHead;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);
        ButterKnife.inject(this);

        userBean = (UserBean) getIntent().getSerializableExtra(KEY_USER_INFO);
        if (userBean == null){
            finish();
            showToast("无法获取用户信息");
            return;
        }else {
            initViews();
        }
    }

    private void initViews() {

//        if (application.userManager.isLogin() && userBean.getHxAccount().equals(application.userManager.getUserBean().getHxAccount())){
//            setMyTitle("个人资料编辑");
//            userBean = application.userManager.getUserBean();
//            panelUserInfo.setOnClickListener(new OnContinuousClickListener() {
//                @Override
//                public void onContinuousClick(View v) {
//                    showSelectPhotoDialog();
//                }
//            });
//
//            itemMySex.setOnClickListener(new OnContinuousClickListener() {
//                @Override
//                public void onContinuousClick(View v) {
//                    showSelectSexDialog(userBean.getSex());
//                }
//            });
//            itemMyIdCard.setOnClickListener(new OnContinuousClickListener() {
//                @Override
//                public void onContinuousClick(View v) {
////                    if (application.userManager.getUserBean().getHospital() != null){
////                        Intent intent = new Intent(UserInfoActivity.this, IdCardActivity.class);
////                        startActivity(intent);
////                    }else{
////                        showToast("已绑定生殖中心，身份证号不能修改");
////                    }
//                }
//            });
//            itemMyNickName.setOnClickListener(new OnContinuousClickListener() {
//                @Override
//                public void onContinuousClick(View v) {
//                    Intent intent = new Intent(UserInfoActivity.this, NameActivity.class);
//                    intent.putExtra(NameActivity.KEY_EDIT_INPUT_TYPE, NameActivity.KEY_EDIT_INPUT_NICKNAME);
//                    startActivity(intent);
//                }
//            });
//            itemMyRealName.setOnClickListener(new OnContinuousClickListener() {
//                @Override
//                public void onContinuousClick(View v) {
//                    Intent intent = new Intent(UserInfoActivity.this, NameActivity.class);
//                    intent.putExtra(NameActivity.KEY_EDIT_INPUT_TYPE, NameActivity.KEY_EDIT_INPUT_REALNAME);
//                    startActivity(intent);
//                }
//            });
////            ImageLoaderUtils.displayImageForDefaultHead(ivUserHead, JDUtils.getRemoteImagePath(application.userManager.getUserBean().getHeadImg()));
//        }else{
//            setMyTitle("TA的资料");
//            itemMyNickName.setAccessImageVisible(View.GONE);
//            itemMyRealName.setAccessImageVisible(View.GONE);
//            itemMySex.setAccessImageVisible(View.GONE);
//            itemMyIdCard.setAccessImageVisible(View.GONE);
//            itemMyIdCard.setVisibility(View.GONE);
//            itemMyRealName.setVisibility(View.GONE);
//            ImageLoaderUtils.displayImageForDefaultHead(ivUserHead, userBean.getHeadImg());
//        }

        itemMyIdCard.setContent(userBean.getIdNo());
        itemMyNickName.setContent(userBean.getNickname());
        itemMyRealName.setContent(userBean.getRealname());
        itemMySex.setContent(JDUtils.getSexInfo(this, userBean.getSex()));

        avatar = userBean.getHeadImg();
        mSex = userBean.getSex();
    }


    @Override
    public void onCaptureImageFinishedListener(String path) {
        if(TextUtils.isEmpty(path)){
            return;
        }
        String mPath = null;
        String compress = BitmapUtils.compressFile(path, -1).getAbsolutePath();
        if(compress != null && !TextUtils.isEmpty(compress)){
            mPath = compress;
        }else{
            mPath = path;
        }
        final String imagePath = mPath;
        loadingDialog = JDUtils.showLoadingDialog(this, "处理中...", false, null);
        JDHttpClient.getInstance().reqUploadPhoto(this, new File(mPath), new JDHttpResponseHandler<List<FileUploadBean>>(new TypeReference<BaseBean<List<FileUploadBean>>>(){}){
            @Override
            public void onRequestCallback(BaseBean<List<FileUploadBean>> result) {
                super.onRequestCallback(result);
                loadingDialog.dismiss();
                if (result.isSuccess()){
                    List<FileUploadBean> fileList = result.getData();
                    if(!ListUtils.isEmpty(fileList)){
                        FileUploadBean file = fileList.get(0);
                        avatar = file.getPath();
                        uploadProfile();
                    }else{
                        showToast("服务器出错");
                    }
                }else{
                    showToast(result.getMessage());
                }
            }
        });
    }


    private void uploadProfile(){
        showLoadingView();
        JDHttpClient.getInstance().reqModifyInfo(this, avatar, userBean.getNickname(), userBean.getRealname(), mSex, userBean.getIdNo(),
                new JDHttpResponseHandler<String>(new TypeReference<BaseBean<String>>(){}){
                    @Override
                    public void onRequestCallback(BaseBean<String> result) {
                        super.onRequestCallback(result);
                        dismissLoadingView();
                        if (result.isSuccess()){
                            itemMySex.setContent(JDUtils.getSexInfo(UserInfoActivity.this, mSex));
                            ImageLoaderUtils.displayImageForDefaultHead(ivUserHead, JDUtils.getRemoteImagePath(avatar));
                            showToast("修改成功！");
                            userBean.setHeadImg(avatar);
                            userBean.setSex(mSex);
//                            application.userManager.resetUser(userBean);
                        }else{
                            showToast(result.getMessage());
                        }
                    }
                });
    }

    private Dialog setSexDialog;
    private View dialogContentView;
    private RadioGroup sexGroup;
    private void showSelectSexDialog(int sexTag){
        if (sexGroup == null) {
            dialogContentView = LayoutInflater.from(this).inflate(R.layout.dialog_set_sex, null);
            sexGroup = (RadioGroup) dialogContentView.findViewById(R.id.rg_sex);
        }
        if (sexTag != 0) {
            RadioButton radioBtn = (RadioButton) sexGroup.findViewWithTag(String.valueOf(sexTag));
            radioBtn.setChecked(true);
        }
        if (setSexDialog == null) {
            setSexDialog = new Dialog(this, R.style.tipsDialog);
            setSexDialog.setContentView(dialogContentView);
            sexGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

                @Override
                public void onCheckedChanged(RadioGroup group, int checkedId) {
                    mSex = Integer.valueOf(group.findViewById(checkedId).getTag().toString());
                    uploadProfile();
                    setSexDialog.dismiss();
                }
            });
        }
        setSexDialog.show();
        setSexDialog.getWindow().setLayout(UIUtils.getScreenSize(this).x - 60, UIUtils.convertDpToPixel(180, this));
    }

    public final class ActivityCaptureImageListenter extends CaptureImageLisenter {

        private int picWidth;
        private Uri fileUri;
        private Context context;
        private CaptureImageFinishedListener listener;
        public ActivityCaptureImageListenter(Context activity, CaptureImageFinishedListener listener) {
            context = activity;
            this.listener = listener;
        }

        public int getPicWidth() {
            return picWidth;
        }

        public CaptureImageLisenter setPicWidth(int picSize) {
            this.picWidth = picSize;
            return this;
        }

        public Uri getFileUri() {
            return fileUri;
        }

        // controller.onCaptureImageFinishedListener(myBitmap);
        public CaptureImageLisenter setFileUri(Uri fileUri) {
            this.fileUri = fileUri;
            return this;
        }

        @Override
        public boolean onActivityResult(int requestCode, int resultCode, Intent data) {
            //String errorMsg = null;

            switch (resultCode) {
                case BitmapUtils.RESULT_OK:
                    switch (requestCode) {
                        case CaptureMedia.KEY_REQUEST_ALBUM:// 相册
                            Intent ALBUMIntent  = new Intent(context, HeadPhotoActivity.class);
                            ALBUMIntent.putExtra(BitmapUtils.BITMATSRC, data.getData());
                            ALBUMIntent.putExtra(BitmapUtils.OBTAINTYPE,BitmapUtils.ALBUM);
                            ALBUMIntent.putExtra(BitmapUtils.RATIO,(double)1);//表示相册，预览显示重选
                            startActivityForResult(ALBUMIntent, BitmapUtils.ALBUM, this);
                            break;
                        case CaptureMedia.KEY_REQUEST_CAREMA:
                            Intent CAREMAIntent  = new Intent(context, HeadPhotoActivity.class);
                            CAREMAIntent.putExtra(BitmapUtils.BITMATSRC, this.fileUri);
                            CAREMAIntent.putExtra(BitmapUtils.OBTAINTYPE,BitmapUtils.CAMERA);//表示相机，预览显示重拍
                            startActivityForResult(CAREMAIntent,BitmapUtils.CAMERA, this);
                            break;
                        case BitmapUtils.ALBUM:
                        case BitmapUtils.CAMERA:
                            String path = data.getStringExtra(BitmapUtils.BITMATSRC);
                            listener.onCaptureImageFinishedListener(path);
                            break;
                        case CaptureMedia.RESULT_CANCELED:
                            break;


                        default:
                            //errorMsg = context.getString(R.string.get_pic_falied);
                            break;
                    }

                case BitmapUtils.RESULT_CANCELED:
                    //取消
                    break;
                //重选
                case BitmapUtils.RESULT_RE_SELECT_PHOTO :
                    if(requestCode == BitmapUtils.ALBUM){
                        CaptureMedia.captureImage(CaptureMedia.KEY_TYPE_ALBUM, JDUtils.getRootActivity((Activity)context), new ActivityCaptureImageListenter(context, UserInfoActivity.this));
                    }else{
                        CaptureMedia.captureImage(CaptureMedia.KEY_TYPE_CAREMA, JDUtils.getRootActivity((Activity) context), new ActivityCaptureImageListenter(context, UserInfoActivity.this));
                    }
                    break;
            }
            return true;
        }

    }

    private AlertDialog selectPhotoDialog;
    private View selectPhotoContentView;
    private void showSelectPhotoDialog(){
        if (selectPhotoDialog == null) {
            selectPhotoContentView = LayoutInflater.from(this).inflate(R.layout.dialog_select_photo, null);
            selectPhotoDialog = new AlertDialog.Builder(this).create();
            selectPhotoContentView.findViewById(R.id.iv_album_selected).setOnClickListener(onDialogBtnClick);
            selectPhotoContentView.findViewById(R.id.iv_camera_selected).setOnClickListener(onDialogBtnClick);
            selectPhotoDialog.setCanceledOnTouchOutside(true);
        }

        selectPhotoDialog.show();
        selectPhotoDialog.setContentView(selectPhotoContentView);
        selectPhotoDialog.getWindow().setLayout(UIUtils.getScreenSize(this).x - 60, UIUtils.convertDpToPixel(180, this));
    }

    private View.OnClickListener onDialogBtnClick = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.iv_album_selected:
                    CaptureMedia.captureImage(CaptureMedia.KEY_TYPE_ALBUM, JDUtils.getRootActivity(UserInfoActivity.this),
                            new ActivityCaptureImageListenter(UserInfoActivity.this, UserInfoActivity.this));
                    selectPhotoDialog.dismiss();
                    break;
                case R.id.iv_camera_selected:
                    CaptureMedia.captureImage(CaptureMedia.KEY_TYPE_CAREMA,
                            JDUtils.getRootActivity(UserInfoActivity.this), new ActivityCaptureImageListenter(UserInfoActivity.this, UserInfoActivity.this));
                    selectPhotoDialog.dismiss();
                    break;
                default:
                    break;
            }
        }
    };

//    @Override
//    public void onUserChanged(UserBean userBean) {
//        super.onUserChanged(userBean);
//        initViews();
//    }
}
