/**   
* @Title: ImageDisplayOpts.java 
* @Package com.ruijie.anan.util.imageloader 
* @Description: TODO(用一句话描述该文件做什么) 
* @author xuyingjian@ruijie.com.cn   
* @date 2015年1月19日 下午5:55:36 
*/
package com.chengsi.weightcalc.utils.imageloader;

import android.graphics.drawable.Drawable;

import com.chengsi.weightcalc.MyApplication;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.DisplayImageOptions.Builder;
import com.nostra13.universalimageloader.core.assist.ImageSize;
import com.nostra13.universalimageloader.core.display.BitmapDisplayer;

/**    
 * @Description: 封装了Imageloader默认的DisplayImageOptions
 * @author xuyingjian@ruijie.com.cn   
 * @date 2015年1月19日 下午5:55:36 
 * @version 3.10  
 */
public class WSImageDisplayOpts {
	
	private int stubRes;
	private int errRes;
	private int loadingRes;
	
	private Drawable imageOnLoading;
	private Drawable imageForEmptyUri;
	private Drawable imageOnFail;
	private BitmapDisplayer displayer;
	private boolean isNeedProcess = true;
	
	private int width;
	private int height;
	private int radius;

	public WSImageDisplayOpts(int defaultRes) {
		this.stubRes = defaultRes;
		this.errRes = defaultRes;
		this.loadingRes = defaultRes;
	}

	public WSImageDisplayOpts(int defaultRes, int radius) {
		this.stubRes = defaultRes;
		this.errRes = defaultRes;
		this.loadingRes = defaultRes;
		this.radius = radius;
	}

	public WSImageDisplayOpts(int stubRes, int errRes, int radius) {
		this.stubRes = stubRes;
		this.errRes = errRes;
		this.radius = radius;
	}
	
	public WSImageDisplayOpts setImageWidth(int width){
		this.width = width;
		return this;
	}
	
	public WSImageDisplayOpts setImageHeight(int height){
		this.height = height;
		return this;
	}
	
	public WSImageDisplayOpts setStubImageRes(int res){
		this.stubRes = res;
		return this;
	}

	public WSImageDisplayOpts setLoadingImageRes(int res){
		this.loadingRes = res;
		return this;
	}
	
	public WSImageDisplayOpts setErrorImageRes(int res){
		this.errRes = res;
		return this;
	}
	
	public WSImageDisplayOpts setRadius(int radius){
		this.radius = radius;
		return this;
	}
	
	public WSImageDisplayOpts setDisplayer(BitmapDisplayer bitmapDisplayer){
		this.displayer = bitmapDisplayer;
		return this;
	}
	
	public WSImageDisplayOpts setNeedProcessDefRes(boolean isNeed){
		this.isNeedProcess = isNeed;
		return this;
	}
	
	public ImageSize getImageSize(){
		if(this.width > 0 && this.height > 0){
			return new ImageSize(width, height);
		}
		return null;
	}
	
	public Drawable getStubDrawable(){
		return getDrawable(stubRes, imageForEmptyUri);
	}
	
	public Drawable getLoadingDrawable(){
		return getDrawable(loadingRes, imageOnLoading);
	}
	
	public Drawable getFailedDrawable(){
		return getDrawable(errRes, imageOnFail);
	}
	
	private Drawable getDrawable(int res, Drawable drawable){
		if(res <= 0 && drawable != null){
			return drawable;
		}
		if(res > 0){
			if(isNeedProcess){
				return ImageLoaderUtils.changeDrawableToExactSize(res, width, height, radius, radius);
			}else{
				return MyApplication.getInstance().getResources().getDrawable(res);
			}
		}else{
			return null;
		}
	}
	
	public Builder toImageLoaderBuilder(){
		Builder builder = new Builder();
		builder.showImageForEmptyUri(getStubDrawable())
		.showImageOnLoading(getLoadingDrawable())
		.cacheInMemory(true)
		.cacheOnDisk(true)
		.showImageOnFail(getFailedDrawable());
		if(radius != 0 && displayer == null){
			builder.displayer(new WSHeadDisplayer(true, radius, width, height));
		}
		if(displayer != null){
			builder.displayer(displayer);
		}
		return builder;
	}
	
	public DisplayImageOptions toImageLoaderOpts(){
		return toImageLoaderBuilder().build();
	}
}
