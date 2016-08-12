/**   
* @Title: HeadPhotoRectView.java 
* @Package com.mobinweaver.app.ananim.widget 
* @Description: TODO(用一句话描述该文件做什么) 
* @author xuyingjian@ruijie.com.cn   
* @date 2014年7月2日 下午2:48:09 
*/
package com.chengsi.weightcalc.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.Rect;
import android.graphics.RectF;
import android.view.View;

import com.chengsi.weightcalc.R;


/**    
 * @Description: 头像截取 
 * @author xuyingjian@ruijie.com.cn   
 * @date 2014年7月2日 下午2:48:09 
 * @version 2.20  
 */
public class HeadPhotoRectView extends View {
	final Paint mPaint = new Paint();
	final Rect mRect = new Rect();
	private ImageViewTouch imageView;
	private int rectWidth;
	private int rectHight;

	public HeadPhotoRectView(ImageViewTouch imageView, Context context, int width, int hight) {
		super(context);
		this.imageView = imageView;
		this.rectWidth = width;
		this.rectHight = hight;// i1
	}
	
	public  Rect getRectViewDisplayRect()
	{
		return mRect;
	}

	protected final void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		
		canvas.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG|Paint.FILTER_BITMAP_FLAG));
		mPaint.setFlags(Paint.ANTI_ALIAS_FLAG);//去掉锯齿
		int height = getHeight();
		this.mRect.left = ((getWidth() - this.rectWidth) / 2);
		this.mRect.right = ((getWidth() + this.rectWidth) / 2);
		this.mRect.top = ((getHeight() - this.rectHight) / 2);
		this.mRect.bottom = ((getHeight() + this.rectHight) / 2);
		Rect[] rectArry = new Rect[8];
		rectArry[0] = new Rect(0, 0,mRect.left,mRect.top);
		rectArry[1] = new Rect(mRect.left, 0,mRect.right,
				this.mRect.top);
		rectArry[2] = new Rect(this.mRect.right, 0, getWidth(), this.mRect.top);
		rectArry[3] = new Rect(0,mRect.top,mRect.left,
				this.mRect.bottom);
		rectArry[4] = new Rect(mRect.right,mRect.top, getWidth(),
				this.mRect.bottom);
		rectArry[5] = new Rect(0,mRect.bottom, this.mRect.left,
				getHeight());
		rectArry[6] = new Rect(mRect.left,mRect.bottom,
				this.mRect.right, getHeight());
		rectArry[7] = new Rect(mRect.right,mRect.bottom, getWidth(),
				getHeight());
		this.mPaint.setColor(0x7f000000);
		this.mPaint.setStyle(Paint.Style.FILL);
		for (int i = 0; i < rectArry.length; ++i)
			canvas.drawRect(rectArry[i], this.mPaint);
		
		
		Rect localRect = new Rect(this.mRect);
		this.mPaint.setColor(0);
		canvas.drawRect(localRect, this.mPaint);
		
		mPaint.reset();
		Bitmap circleBm = BitmapFactory.decodeResource(getContext().getResources(), R.drawable.head_photo_preview_circle_mask);
		Rect src = new Rect(0, 0, circleBm.getWidth(), circleBm.getHeight());
		canvas.drawBitmap(circleBm, src, mRect, mPaint);
	}

	/**
	 * 
	 * @Description: 获取选中区域的快照，默认图片选中区域长宽相同。 
	 * @param maxWidth 最大长宽
	 * @param minWidth 最小长宽
	 * @return
	 */
	public final Bitmap screenshot(int maxWidth, int minWidth) {
		Bitmap mBitmap = imageView.getRotateBitmap()
				.getBitmap();
		RectF localRectF = imageView.getDisplayRect();
		float f = localRectF.width() / ((Bitmap) mBitmap).getWidth();
		int left = (int) ((this.mRect.left - localRectF.left) / f);
		int top = (int) ((this.mRect.top - localRectF.top) / f);
		int width = (int) (this.mRect.width() / f);
		int height = (int) (this.mRect.height() / f);
		if (left < 0)
			left = 0;
		if (top < 0)
			top = 0;
		if (left + width > ((Bitmap) mBitmap).getWidth())
			width = ((Bitmap) mBitmap).getWidth() - left;
		if (top + height > ((Bitmap) mBitmap).getHeight())
			height = ((Bitmap) mBitmap).getHeight() - top;
		
		int k = width;
		if(width < minWidth){
			k = minWidth;
		}
		if(width > maxWidth){
			k = maxWidth;
		}
		try {
			mBitmap = Bitmap.createBitmap((Bitmap) mBitmap, left, top, width, height);
			Bitmap scaledBitmap = Bitmap.createScaledBitmap((Bitmap) mBitmap,
					k, k, true); 
			mBitmap = scaledBitmap;

		} catch (OutOfMemoryError localOutOfMemoryError1) {
			try {
				System.out.println("OutOfMemoryError");
				// localObject = Bitmap.createBitmap(this.c, this.d,
				// Bitmap.Config.ARGB_8888);
				// Canvas localCanvas = new Canvas((Bitmap)localObject);
				// int i1 = (getWidth() - this.c) / 2;
				// int i2 = (getHeight() - this.d) / 2;
				// localCanvas.translate(-i1, -i2);
				// PhotoPreview.access$2600(this.jdField_a_of_type_ComTencentMobileqqActivityPhotoPreview).draw(localCanvas);
				// localCanvas.translate(i1, i2);
			} catch (OutOfMemoryError localOutOfMemoryError2) {
				// QLog.v("sven", "OOM when create bitmap");
			}
		}
		return mBitmap;
	}
}
