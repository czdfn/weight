/**   
* @Title: ImagePreviewController.java 
* @Package com.ruijie.anan.ui.im 
* @Description: TODO(用一句话描述该文件做什么) 
* @author xuyingjian@ruijie.com.cn   
* @date 2014年11月4日 上午11:40:25 
*/
package com.chengsi.weightcalc.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.GestureDetector.OnDoubleTapListener;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.chengsi.weightcalc.R;
import com.chengsi.weightcalc.activity.BaseActivity;
import com.chengsi.weightcalc.utils.JDUtils;
import com.chengsi.weightcalc.utils.imageloader.ImageLoaderUtils;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.util.List;

import cn.jiadao.corelibs.utils.ListUtils;

/**
 * 
 * @Description: 图片预览 
 * @author xuyingjian@ruijie.com.cn   
 * @date 2014年11月4日 上午11:41:45 
 * @version 3.10
 */
public class ImagePreviewController extends AbstractController {
	

	private ViewPager mViewPager;
	private boolean enableSingleTap = true;//轻点退出当前界面
	private boolean enableDoubleTap = true	;//双击放大图片
	
	private List<String> imageList;
	private TouchImageAdapter mAdapter;
	
	private String mCurrentImageName = null;
	
	/**
	 * @param context
	 */
	public ImagePreviewController(Context context, ViewPager pager) {
		super(context, pager);
		mViewPager = pager;
		BaseActivity holdAct = (BaseActivity) context;
		mAdapter = new TouchImageAdapter(holdAct.getSupportFragmentManager());
		mViewPager.setAdapter(mAdapter);
	}

	public void setImageSource(List<String> imageList){
		this.imageList = imageList;
		mAdapter.notifyDataSetChanged();
		if(mCurrentImageName != null){
			this.mViewPager.setCurrentItem(imageList.indexOf(mCurrentImageName));
		}
	}
	
	public void setCurrentImage(String image){
		if(imageList == null){
			mCurrentImageName = image;
		}else{
			mCurrentImageName = image;
			
			this.mViewPager.setCurrentItem(imageList.indexOf(image));
		}
	}
	
	/**
	 * 
	 * @Description: 返回当前照片的本地路径
	 * @return
	 */
	public String getCurrentImageLocalPath(){
		ImagePreviewFragment fragment = (ImagePreviewFragment) mAdapter.instantiateItem(mViewPager, mViewPager.getCurrentItem());
		return fragment.getCurrentImagePath();
	}
	
	/**
	 * 
	 * @Description: 返回当前照片的Url.为传递的ImageList里的Image url
	 * @return
	 */
	public String getCurrentImagePath(){
		int page = this.mViewPager.getCurrentItem();
		if(imageList != null && imageList.size() > page){
			return imageList.get(page);
		}
		return null;
	}

	/**
	 * @param enableSingleTap the enableSingleTap to set
	 */
	public void setEnableSingleTap(boolean enableSingleTap) {
		this.enableSingleTap = enableSingleTap;
	}

	/**
	 * @param enableDoubleTap the enableDoubleTap to set
	 */
	public void setEnableDoubleTap(boolean enableDoubleTap) {
		this.enableDoubleTap = enableDoubleTap;
	}

	class TouchImageAdapter extends FragmentStatePagerAdapter{

		/**
		 * @param fm
		 */
		public TouchImageAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public int getCount() {
			return ListUtils.isEmpty(imageList) ? 0 : imageList.size();
		}

		@Override
		public Fragment getItem(int position) {
			ImagePreviewFragment fragment = new ImagePreviewFragment();
			Bundle bundle = new Bundle();
			bundle.putString(ImagePreviewFragment.KEY_URL, imageList.get(position));
			fragment.setArguments(bundle);
			return fragment;
		}
	}

	@SuppressLint("ValidFragment")
	private class ImagePreviewFragment extends Fragment {
		public static final String KEY_URL = "key_url";
		private TouchImageView imageView;
		private ProgressBar progressBar;
		private RelativeLayout rootView;
		private String url;

		/**
		 *
		 */
		@Override
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			Bundle bundle = getArguments();
			url = bundle.getString(KEY_URL);
			url = JDUtils.getRemoteImagePath(url);
			rootView = (RelativeLayout) LayoutInflater.from(context).inflate(R.layout.photo_preview_fragment, null);
			imageView = (TouchImageView) rootView.findViewById(R.id.photo_preview_imageview);
			progressBar = (ProgressBar) rootView.findViewById(R.id.photo_preview_pb);
			OnClickListener singleTapFinishListener = new OnClickListener() {
				@Override
				public void onClick(View v) {
					if (enableSingleTap) {
						getActivity().finish();
					}
				}
			};
			imageView.setOnDoubleTapListener(new OnDoubleTapListener() {
				
				@Override
				public boolean onSingleTapConfirmed(MotionEvent e) {
					if (enableSingleTap) {
						getActivity().finish();
					}
					return false;
				}
				
				@Override
				public boolean onDoubleTapEvent(MotionEvent e) {
					return false;
				}
				
				@Override
				public boolean onDoubleTap(MotionEvent e) {
					return false;
				}
			});
			
			if (TextUtils.isEmpty(url)) {
				JDToast.makeText(getActivity(), R.string.load_pic_falied, JDToast.LENGTH_SHORT).show();
				return;
			}
			ImageLoaderUtils.displayImageForIv(imageView, url, ImageLoaderUtils.ImageType.ImageTypeDefault, new ImageLoadingListener() {
				@Override
				public void onLoadingStarted(String imageUri, View view) {
					progressBar.setVisibility(View.VISIBLE);
				}

				@Override
				public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
					progressBar.setVisibility(View.GONE);
					JDToast.makeText(getActivity(), R.string.load_pic_falied, JDToast.LENGTH_SHORT).show();
				}

				@Override
				public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
					progressBar.setVisibility(View.GONE);
				}

				@Override
				public void onLoadingCancelled(String imageUri, View view) {
					progressBar.setVisibility(View.GONE);
				}
			});
		}
		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			return rootView;
		}

		public String getCurrentImagePath() {

			return url;
		}
	}
}
