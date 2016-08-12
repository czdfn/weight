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
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

import com.alibaba.fastjson.TypeReference;
import com.chengsi.weightcalc.R;
import com.chengsi.weightcalc.bean.BaseBean;
import com.chengsi.weightcalc.bean.FileUploadBean;
import com.chengsi.weightcalc.bean.UserBean;
import com.chengsi.weightcalc.http.JDHttpClient;
import com.chengsi.weightcalc.http.JDHttpResponseHandler;
import com.chengsi.weightcalc.listener.CaptureImageFinishedListener;
import com.chengsi.weightcalc.listener.CaptureImageLisenter;
import com.chengsi.weightcalc.listener.OnContinuousClickListener;
import com.chengsi.weightcalc.utils.BitmapUtils;
import com.chengsi.weightcalc.utils.CaptureMedia;
import com.chengsi.weightcalc.utils.JDUtils;
import com.chengsi.weightcalc.utils.Position;
import com.chengsi.weightcalc.utils.UIUtils;
import com.chengsi.weightcalc.utils.imageloader.ImageLoaderUtils;
import com.chengsi.weightcalc.widget.PreferenceRightDetailView;
import com.nostra13.universalimageloader.core.download.ImageDownloader;

import java.io.File;
import java.util.List;

import cn.jiadao.corelibs.utils.ListUtils;


/**    
 * @Description: 完善资料 
 * @author xuyingjian@ruijie.com.cn   
 * @date 2015年6月4日 下午4:38:16 
 * @version 1.0  
 */
public class UploadProfileActivity extends BaseActivity implements CaptureImageFinishedListener {

	private PreferenceRightDetailView mSexView;
	private EditText mRealName;
	private EditText mUserNameEt;
	private EditText mIdCardEt;
	private ImageView mHeadPhotoIv;
	private View panelHeadPhoto;
	private TextView mUploadBtn;
	
	private Dialog loadingDialog;
	
	private String avatar;
	private int mSex;//0 空 1 男  2女

	private UserBean userBean;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_upload_profile);
		getSupportActionBar().setTitle(getString(R.string.finish_profile));
		
		initViews();
		new Position(getApplicationContext());

	}

	private void initViews() {

		if (getIntent().getSerializableExtra("user") != null){
			userBean = (UserBean) getIntent().getSerializableExtra("user");
		}

		mSexView = (PreferenceRightDetailView) findViewById(R.id.user_info_sex);
		mRealName = (EditText) findViewById(R.id.user_name_et);
		mHeadPhotoIv = (ImageView) findViewById(R.id.iv_head_photo);
		mUserNameEt = (EditText) findViewById(R.id.nick_name_et);
		mUploadBtn = (TextView) findViewById(R.id.tv_finish_btn);
		panelHeadPhoto = findViewById(R.id.panel_head_photo);
		mIdCardEt = (EditText) findViewById(R.id.user_idcard_et);
		
		
		mSexView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				showSelectSexDialog(0);
			}
		});
		panelHeadPhoto.setOnClickListener(new OnContinuousClickListener(500) {
			
			@Override
			public void onContinuousClick(View v) {
				showSelectPhotoDialog();
			}
		});
		mUploadBtn.setOnClickListener(new OnContinuousClickListener() {
			
			@Override
			public void onContinuousClick(View v) {
				uploadProfile();
			}
		});
	}
	
	private void uploadProfile(){
		if(TextUtils.isEmpty(mUserNameEt.getText().toString().trim())){
			showToast("请输入您的昵称");
			return;
		}
		if (!TextUtils.isEmpty(mIdCardEt.getText().toString())){
			if (!JDUtils.isIdentityCode(mIdCardEt.getText().toString())){
				showToast("身份证号格式出错");
				return;
			}
		}
		showLoadingView();
		JDHttpClient.getInstance().reqModifyInfo(this, avatar, mUserNameEt.getText().toString(), mRealName.getText().toString(), mSex, mIdCardEt.getText().toString(),
				new JDHttpResponseHandler<String>(new TypeReference<BaseBean<String>>(){}){
					@Override
					public void onRequestCallback(BaseBean<String> result) {
						super.onRequestCallback(result);
						dismissLoadingView();
						if (result.isSuccess()){
							showToast("个人资料上传成功");
							userBean.setHeadImg(avatar);
							userBean.setNickname(mUserNameEt.getText().toString());
							userBean.setRealname(mRealName.getText().toString());
							userBean.setSex(mSex);
//							application.userManager.resetUser(userBean);
							finish();
							if (TextUtils.isEmpty(userBean.getToken())){
								showToast("注册成功！");
							}else{
								startActivity(new Intent(UploadProfileActivity.this, GuidePageActivity.class));
							}
//							Intent intent = new Intent(UploadProfileActivity.this, HospitalListActivity.class);
//							intent.putExtra(HospitalListActivity.KEY_IS_FOR_LOGIN, true);
//							startActivity(intent);
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
			setSexDialog = new Dialog(UploadProfileActivity.this, R.style.tipsDialog);
			setSexDialog.setContentView(dialogContentView);
			sexGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {
				
				@Override
				public void onCheckedChanged(RadioGroup group, int checkedId) {
					mSex = Integer.valueOf(group.findViewById(checkedId).getTag().toString());
					mSexView.setContent(JDUtils.getSexInfo(UploadProfileActivity.this, mSex));
					setSexDialog.dismiss();
				}
			});
		}
		setSexDialog.show();
		setSexDialog.getWindow().setLayout(UIUtils.getScreenSize(UploadProfileActivity.this).x - 60, UIUtils.convertDpToPixel(180, UploadProfileActivity.this));
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
					CaptureMedia.captureImage(CaptureMedia.KEY_TYPE_ALBUM, JDUtils.getRootActivity((Activity)context), new ActivityCaptureImageListenter(context, UploadProfileActivity.this));
				}else{
					CaptureMedia.captureImage(CaptureMedia.KEY_TYPE_CAREMA, JDUtils.getRootActivity((Activity) context), new ActivityCaptureImageListenter(context, UploadProfileActivity.this));
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
			selectPhotoContentView = LayoutInflater.from(UploadProfileActivity.this).inflate(R.layout.dialog_select_photo, null);
			selectPhotoDialog = new AlertDialog.Builder(UploadProfileActivity.this).create();
			selectPhotoContentView.findViewById(R.id.iv_album_selected).setOnClickListener(onDialogBtnClick);
			selectPhotoContentView.findViewById(R.id.iv_camera_selected).setOnClickListener(onDialogBtnClick);
			selectPhotoDialog.setCanceledOnTouchOutside(true);
		}

		selectPhotoDialog.show();
		selectPhotoDialog.setContentView(selectPhotoContentView);
		selectPhotoDialog.getWindow().setLayout(UIUtils.getScreenSize(UploadProfileActivity.this).x - 60, UIUtils.convertDpToPixel(180, UploadProfileActivity.this));
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
		JDHttpClient.getInstance().reqUploadPhoto(this, new File(mPath), new JDHttpResponseHandler<List<FileUploadBean>>(new TypeReference<BaseBean<List<FileUploadBean>>>() {
		}) {
			@Override
			public void onRequestCallback(BaseBean<List<FileUploadBean>> result) {
				super.onRequestCallback(result);
				loadingDialog.dismiss();
				if (result.isSuccess()) {
					ImageLoaderUtils.displayImageForDefaultHead(mHeadPhotoIv, ImageDownloader.Scheme.FILE.wrap(imagePath));
					List<FileUploadBean> fileList = result.getData();
					if (!ListUtils.isEmpty(fileList)) {
						FileUploadBean file = fileList.get(0);
						avatar = file.getPath();
						userBean.setHeadImg(avatar);
						showToast("上传头像成功");
					} else {
						showToast("服务器出错");
					}
				} else {
					showToast(result.getMessage());
				}
			}
		});
	}
	private OnClickListener onDialogBtnClick = new OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.iv_album_selected:
				CaptureMedia.captureImage(CaptureMedia.KEY_TYPE_ALBUM, JDUtils.getRootActivity(UploadProfileActivity.this),
						new ActivityCaptureImageListenter(UploadProfileActivity.this, UploadProfileActivity.this));
				selectPhotoDialog.dismiss();
				break;
			case R.id.iv_camera_selected:
				CaptureMedia.captureImage(CaptureMedia.KEY_TYPE_CAREMA,
						JDUtils.getRootActivity(UploadProfileActivity.this), new ActivityCaptureImageListenter(UploadProfileActivity.this, UploadProfileActivity.this));
				selectPhotoDialog.dismiss();
				break;
			default:
				break;
			}
		}
	};
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
	}
//
//	@Override
//	public void onUserChanged(UserBean userBean) {
//	}
}
