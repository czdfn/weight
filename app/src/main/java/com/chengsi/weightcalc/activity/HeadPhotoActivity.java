/**   
* @Package com.ruijie.anan.ui.im 
* @Description: TODO(用一句话描述该文件做什么) 
* @author xuyingjian@ruijie.com.cn   
* @date 2014年6月30日 下午5:24:45 
*/
package com.chengsi.weightcalc.activity;

import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Matrix;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chengsi.weightcalc.R;
import com.chengsi.weightcalc.utils.BitmapCache;
import com.chengsi.weightcalc.utils.BitmapUtils;
import com.chengsi.weightcalc.utils.CaptureMedia;
import com.chengsi.weightcalc.utils.HeadPhotoRectView;
import com.chengsi.weightcalc.utils.ImageSwitcherTouch;
import com.chengsi.weightcalc.utils.ImageViewTouch;
import com.chengsi.weightcalc.widget.JDToast;

import java.io.File;


/**    
 * @Description: 头像展示界面以及头像编辑界面 
 * @author xuyingjian@ruijie.com.cn   
 * @date 2014年6月30日 下午5:24:45 
 * @version 2.20  
 */
public class HeadPhotoActivity extends BaseActivity implements OnClickListener,
	OnTouchListener {

	private static final int PHOTO_MAX_WIDTH = 720;
	private static final int PHOTO_MIN_WIDTH = 60;
	private String srcPath;
	private String dstPath;
	private Bitmap mBitmap;
	private int screenHight;
	private int screenWidth;
	
	private int margin = 60;
	
	
	private TextView mLeftBtn, mRightBtn, mSaveBtn;//左侧按钮, 右侧按钮, 保存按钮
	private View mToolBtnArea;//左侧按钮、右侧按钮的显示区域
	
	private View dock;
	private ProgressBar pb;
	private RelativeLayout rootView;
	private Boolean gestureFlag;

	private ImageSwitcherTouch mImageSwitcher;
	private ImageViewTouch mTouchView;
	private GestureDetector mGestureDetector;
	private HeadPhotoRectView mRectView;
	private int resType;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		gestureFlag = true;
		if(isInAbnormalState(savedInstanceState)) {
			return;
		}
		setContentView(R.layout.activity_head_photo_layout);
		hideToolbar();
		initLayout();

		try {
			Intent result = getIntent();
			DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
			int widthPixels = displayMetrics.widthPixels;
			int heightPixels = displayMetrics.heightPixels;
			if(resType == BitmapUtils.PREVIEW){
				margin = 0;
				srcPath = result.getStringExtra(BitmapUtils.BITMATSRC);
				if(TextUtils.isEmpty(srcPath)){
					int srcId = result.getIntExtra(BitmapUtils.BITMAP_DRAWABLE_SRC, -1);
					if(srcId > 0){
						mBitmap = BitmapFactory.decodeResource(getResources(), srcId);
					}else{
						finish();
					}
				}else{
					String bigName = srcPath.replace("_small", "");
					File file = new File(bigName);
					if(file.exists()){
						srcPath = bigName;
						mBitmap = BitmapFactory.decodeFile(bigName);
					}else{
						mBitmap = BitmapFactory.decodeFile(srcPath);
					}
				}
			}else{
				Uri path = result.getParcelableExtra(BitmapUtils.BITMATSRC);
				srcPath = BitmapUtils.getRealPathFromURI(this, path);
				if(srcPath == null){
					srcPath = path.getPath();
				}
				Options options = new Options();
				options.inJustDecodeBounds = true;
				BitmapFactory.decodeFile(srcPath, options);
				if(options.outWidth < PHOTO_MIN_WIDTH || options.outHeight < PHOTO_MIN_WIDTH){
					JDToast.makeText(application, getString(R.string.head_photo_too_small, PHOTO_MIN_WIDTH + "*" + PHOTO_MIN_WIDTH), JDToast.LENGTH_LONG).show();
					onClick(findViewById(R.id.btn_photo_preview_left));
					return;
				}
				mBitmap = BitmapUtils.loadBitmap(path, heightPixels, widthPixels, this, Bitmap.Config.ARGB_8888);
			}
			if(mBitmap == null)
			{
				JDToast.makeText(getApplicationContext(), getString(R.string.load_pic_falied), JDToast.LENGTH_SHORT).show();
				setResult(BitmapUtils.RESULT_CANCELED);
				finish();
				return;
			}
			onBitmapLoadFinished();
		} catch (OutOfMemoryError e) {
			e.printStackTrace();
			JDToast.makeText(getApplicationContext(), R.string.img_load_failed,
					JDToast.LENGTH_SHORT).show();
			setResult(BitmapUtils.RESULT_CANCELED);
			finish();
		}
	}
	
	/**
	 * 初始化布局
	 */
	private void initLayout() {
		this.getMyTitleView().setVisibility(View.GONE);
		rootView = (RelativeLayout) findViewById(R.id.photo_preview_root);
		pb = (ProgressBar) findViewById(R.id.photo_preview_pb);
		pb.setVisibility(View.VISIBLE);
		dock = findViewById(R.id.photo_preview_dock);
		mLeftBtn = (TextView) findViewById(R.id.btn_photo_preview_left);
		mRightBtn = (TextView) findViewById(R.id.btn_photo_preview_right);
		mSaveBtn = (TextView) findViewById(R.id.btn_photo_save);
		mToolBtnArea = findViewById(R.id.tool_btn_area);
		mLeftBtn.setOnClickListener(this);
		mRightBtn.setOnClickListener(this);
		mSaveBtn.setOnClickListener(this);
		resType = getIntent().getIntExtra(BitmapUtils.OBTAINTYPE, 0);
		if(resType == BitmapUtils.ALBUM){
			mLeftBtn.setText("重选");
		}else if(resType == BitmapUtils.CAMERA){
			mLeftBtn.setText("重拍");
		}else if(resType == BitmapUtils.PREVIEW){
			mSaveBtn.setVisibility(View.VISIBLE);
			mToolBtnArea.setVisibility(View.GONE);
		}
		// Image Layout
		mImageSwitcher = (ImageSwitcherTouch) findViewById(R.id.photo_preview_imageswitchertouch);
		mTouchView = new ImageViewTouch(this);
		android.widget.FrameLayout.LayoutParams touchLayoutParams = new android.widget.FrameLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		touchLayoutParams.gravity = Gravity.CENTER;
		mImageSwitcher.addView(this.mTouchView, 0, touchLayoutParams);
		RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) this.mImageSwitcher
				.getLayoutParams();
		
		DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
		//设置imageview的宽高为实际的宽高，可解决在某些手机上出现抖动的问题
		layoutParams.width = displayMetrics.widthPixels;
		layoutParams.height = (int) (displayMetrics.heightPixels - 69 * displayMetrics.density);  //69包含25和44两部分，25是statusBar的高度，44是下面操作区域的高度。
		

		this.mImageSwitcher.setLayoutParams(layoutParams);
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		screenHight = dm.heightPixels;;  
		screenWidth = dm.widthPixels;

		rootView.setOnTouchListener(this);

		mGestureDetector = new GestureDetector(this,
				new ImageGestureListener(), null, false);

	}
	
	private void onBitmapLoadFinished() {
		if(resType != BitmapUtils.PREVIEW){
			RelativeLayout.LayoutParams rlLayoutParams = new RelativeLayout.LayoutParams(
					LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
			rlLayoutParams.addRule(RelativeLayout.ABOVE, R.id.photo_preview_dock);
			mRectView = new HeadPhotoRectView(mTouchView, this, screenWidth - margin, screenWidth - margin);
			rootView.addView(mRectView, 1, rlLayoutParams);
			initTouchView(true);
		}else{
			File file = new File(srcPath);
			String name = file.getName();
			if(file.exists() && name.contains("_small")){
				margin = 0;
				initTouchView(false);
				name = name.replace("_small", "");
				//TODO加载图片
//				srcPath = imageUrl;
//				mBitmap = BitmapFactory.decodeFile(imageUrl);
//				initTouchView(true);
			}else{
				initTouchView(true);
				pb.setVisibility(View.GONE);
			}
		}
	}
	
	/**
	 * 
	 * @Description: 初始化图片展示区域
	 * @param isLimitedDisplay 是否限制长宽展示  防止超过全屏
	 */
	private void initTouchView(final boolean isLimitedDisplay){
		
		final int circleSize = screenWidth - margin;
		
		mTouchView.setImageBitmap(mBitmap);
		mTouchView.setImageBitmapResetBase(mBitmap, true);
		
		mTouchView.setRecycler(new BitmapCache(1));
		if(isLimitedDisplay){
			mTouchView.makeDefaultRect(circleSize, circleSize);	
		}else{
			mTouchView.makeDefaultRect(mBitmap.getWidth(), mBitmap.getHeight());	
		}
		mTouchView.getViewTreeObserver().addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
			
			@Override
			public void onGlobalLayout() {
				
				Matrix matrix = mTouchView.getImageMatrix(); 
				float[] values = new float[9];
				matrix.getValues(values); 
				float scale = 1;
				float width, height;//距离左右各30px
				float ratio = (float)mBitmap.getWidth() / (float)mBitmap.getHeight();
				//根君中间圆圈的值进行计算
				if(ratio > 1){
					height = circleSize;
					width = height * ratio;
				}else{
					width = circleSize;
					height = width / ratio;
				}
				if(!isLimitedDisplay){
					if(mBitmap.getWidth() < width){
						scale = 1;
					}
				}
				else{
					int instrinsicWidth = mTouchView.getDrawable().getIntrinsicWidth();
					int instrinsicHeight = mTouchView.getDrawable().getIntrinsicHeight();
					float bitmapWidth = instrinsicWidth * values[0];
					float bitmaphight = instrinsicHeight * values[4];

					if(bitmapWidth > bitmaphight){
						scale = (float) (height/bitmaphight); 
					}else{
						scale = (float) (width/bitmapWidth);
					}
					
					
				}
				mTouchView.SetMinZoom(scale);
				mTouchView.zoomTo(scale);
				
				if(Build.VERSION.SDK_INT < 16){
					mTouchView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
				}else{
					removeGlobalOnLayoutListener(mTouchView, this);
				}
			}
		});
		pb.setVisibility(View.GONE);
	}
	
	@TargetApi(16)
	private void removeGlobalOnLayoutListener(View view, OnGlobalLayoutListener listener){
		view.getViewTreeObserver().removeOnGlobalLayoutListener(listener);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_photo_preview_left:
			setResult(BitmapUtils.RESULT_RE_SELECT_PHOTO);
			finish();
			break;
		case R.id.btn_photo_preview_right:
			Bitmap resultPic = mRectView.screenshot(PHOTO_MAX_WIDTH, PHOTO_MIN_WIDTH);
			String imagePath = saveHeadPic(resultPic);
			application.albumManager.onCompleteForRechoose();
			sendPic(imagePath);
			break;
		case R.id.btn_photo_save:
			finish();
			break; 
		}

	}

	private class ImageGestureListener extends
			GestureDetector.SimpleOnGestureListener {
		private float baseValue, lastScale;
		private float originalScale;
		private Boolean menuFlag = true;
		@Override
		public boolean onDown(MotionEvent e) {
			baseValue = 0;
			lastScale = 0;
			originalScale = mTouchView.getScale();
			return true;
		}

		@Override
		public boolean onScroll(MotionEvent e1, MotionEvent e2,
				float distanceX, float distanceY) {
			// 这里的代码得到bitmap矩形位置
			// Matrix m = mTouchView.getImageViewMatrix();
			// RectF rect = new RectF(0, 0, mBitmapWidth, mBitmapHeight);
			// m.mapRect(rect);

			// 多点处理
			// 如果为多点：多点的处理方式为不移动放大

			int pointerCount = e2.getPointerCount();
			if (pointerCount == 2) {
				float x = e2.getX(0) - e2.getX(1);
				float y = e2.getY(0) - e2.getY(1);
				float value = (float) Math.sqrt(x * x + y * y);// 计算两点的距离
				if (baseValue == 0) {
					baseValue = value;
				} else {
					float scale = value / baseValue;// 当前两点间的距离除以手指落下时两点间的距离就是需要缩放的比例。
					if (Math.abs(lastScale - scale) > 0.05) {// 只有比例变化大于0.1时才进行缩放。
																// &&mTouchView.getScale()<=3F
						lastScale = scale;
						mTouchView.zoomTo(originalScale * scale,
								x + e2.getX(1), y + e2.getY(1));
					}

				}
			} else if (pointerCount == 1) {
				mTouchView.postTranslate(-distanceX, -distanceY);
				mTouchView.center(false, false);
			}
			return false;
		}

		@Override
		public boolean onSingleTapUp(MotionEvent e) {
			return true;
		}

		
		@Override
		public boolean onSingleTapConfirmed(MotionEvent e) {
//			hideMenu(menuFlag);
//			menuFlag = !menuFlag;
			if(resType == BitmapUtils.PREVIEW){
				recycleCurrImage();
				finish();
			}
			return true;
		}

		@Override
		public boolean onDoubleTap(MotionEvent e) {
			if (mTouchView.getScale() > 2F) {
				mTouchView.zoomTo(1f);
			} else {
				mTouchView.zoomToPoint(3f, e.getX(), e.getY());
			}
			return true;
		}
		
		@Override
		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
				float velocityY) {
			return super.onFling(e1, e2, velocityX, velocityY);
		}

	}


	/**
	 * 压缩后保存头像图片
	 */
	private String saveHeadPic(Bitmap bitmap) { 
		mTouchView.setEnabled(false);
		dstPath = getDstPath(resType);
		BitmapUtils.compressImage(this, bitmap, dstPath/*, bitmap.getWidth(), bitmap.getHeight()*/);
		MediaScannerConnection.scanFile(this, new String[]{dstPath}, null, null);
		return dstPath;
	}
	
	/**
	 * 获取目标路径
	 * @return 目标路径
	 */
	private String getDstPath(int resType)
	{
		String type = srcPath.substring(srcPath.lastIndexOf(".")+1);
		String name = srcPath.substring(srcPath.lastIndexOf("/")+1,srcPath.lastIndexOf("."));
		if(resType == BitmapUtils.BACKGROUND)
		{
			File dir = new File(CaptureMedia.getBackgroundDir());
			if(!dir.exists())
				dir.mkdirs();
			return CaptureMedia.getBackgroundDir()+File.separator+name+System.currentTimeMillis()+"_back."+type;
		}
		
		return CaptureMedia.getPhotoDir()+File.separator+name+"_header."+type;
	}
	
	/**
	 * 上传并返回头像地址
	 * 
	 * @param imgPath
	 */
	private void sendPic(String imgPath) {
		gestureFlag = false;
		Intent resultIntent = new Intent();
		resultIntent.putExtra(BitmapUtils.BITMATSRC, imgPath);
		setResult(BitmapUtils.RESULT_OK, resultIntent);
		pb.setVisibility(View.GONE);
		finish();
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		if(gestureFlag)
			mGestureDetector.onTouchEvent(event);
		return true;
	}

	public ImageViewTouch getImageTouchView() {
		return mTouchView;
	}

	/**
	 * 释放资源
	 */
	private void recycleCurrImage() {
		try {
			((ImageViewTouch) this.mImageSwitcher.getCurrentView()).setImageBitmap(null);
			mBitmap.recycle();
			System.gc();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onBackPressed() {
		recycleCurrImage();
		super.onBackPressed();
	}

	@Override
	protected void onDestroy() {
		recycleCurrImage();
		super.onDestroy();
	}
	
	@Override
	protected void showEnterAnimation() {
		overridePendingTransition(R.anim.zoom_in, R.anim.alpha);
	}
	
	@Override
	protected void showExitAnimation() {
		overridePendingTransition(0, R.anim.zoom_out);
	}
}
