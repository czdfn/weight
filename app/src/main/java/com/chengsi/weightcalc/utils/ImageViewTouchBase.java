package com.chengsi.weightcalc.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.widget.ImageView;

public class ImageViewTouchBase extends ImageView {

	private boolean bShadow = false;
	static final float SCALE_RATE = 1.15F;

	
	private RectF tmpRect = new RectF();

	public ImageViewTouchBase(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	public ImageViewTouchBase(Context context) {
		super(context);
		init();
	}

	public ImageViewTouchBase(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	private void init() {
		setScaleType(ScaleType.MATRIX);
	}

	@SuppressWarnings("unused")
	private static final String TAG = "ImageViewTouchBase";

	// This is the base transformation which is used to show the image
	// initially. The current computation for this shows the image in
	// it's entirety, letterboxing as needed. One could choose to
	// show the image as cropped instead.
	//
	// This matrix is recomputed when we go from the thumbnail image to
	// the full size image.
	public Matrix mBaseMatrix = new Matrix();

	// This is the supplementary transformation which reflects what
	// the user has done in terms of zooming and panning.
	//
	// This matrix remains the same when we go from the thumbnail image
	// to the full size image.
	public Matrix mSuppMatrix = new Matrix();

	// This is the final matrix which is computed as the concatentation
	// of the base matrix and the supplementary matrix.
	private final Matrix mDisplayMatrix = new Matrix();

	// Temporary buffer used for getting the values out of a matrix.
	private final float[] mMatrixValues = new float[9];

	// The current bitmap being displayed.
	public final RotateBitmap mBitmapDisplayed = new RotateBitmap(null);

	int mThisWidth = -1, mThisHeight = -1;

	float mMaxZoom;

	// ImageViewTouchBase will pass a Bitmap to the Recycler if it has finished
	// its use of that Bitmap.
	

	public void setRecycler(ImageBitmapRecycler r) {
		mRecycler = r;
	}

	@Override
	protected void onDraw(Canvas canvas) {
//		try
//	    {

		
		
		
	      super.onDraw(canvas);
	      //阴影
//	      if ((!this.bShadow) || (this.mBitmapDisplayed == null) || (this.mBitmapDisplayed.getBitmap() == null))
//	        return;
//	      Paint localPaint = new Paint();
//	      Matrix localMatrix = getImageViewMatrix();
//	      this.tmpRect.set(0.0F, 0.0F, this.mBitmapDisplayed.getBitmap().getWidth(), this.mBitmapDisplayed.getBitmap().getHeight());
//	      localMatrix.mapRect(this.tmpRect);
//	      localPaint.setStyle(Paint.Style.STROKE);
//	      localPaint.setStrokeWidth(1.0F);
//	      RectF localRectF1 = this.tmpRect;
//	      localRectF1.right -= 1.0F;
//	      int i = -14606047;
//	      int j = 0;
//	      if (j >= 5)
//	        return;
//	      RectF localRectF2 = this.tmpRect;
//	      localRectF2.left -= 1.0F;
//	      RectF localRectF3 = this.tmpRect;
//	      localRectF3.top -= 1.0F;
//	      RectF localRectF4 = this.tmpRect;
//	      localRectF4.right = (1.0F + localRectF4.right);
//	      RectF localRectF5 = this.tmpRect;
//	      localRectF5.bottom = (1.0F + localRectF5.bottom);
//	      localPaint.setColor(0xFF712121);
//	      canvas.drawRoundRect(this.tmpRect, j, j, localPaint);
//	      i += (5 - j << 16 | 5 - j << 8 | 5 - j);
//	      ++j;
//	    }
//	    catch (RuntimeException localRuntimeException)
//	    {
//	      localRuntimeException.printStackTrace();
//	    }
		
	}
	
	
	
	private ImageBitmapRecycler mRecycler;

	@Override
	public void onLayout(boolean changed, int left, int top, int right,
			int bottom) {
		super.onLayout(changed, left, top, right, bottom);
		mThisWidth = right - left;
		mThisHeight = bottom - top;
		Runnable r = mOnLayoutRunnable;
		if (r != null) {
			mOnLayoutRunnable = null;
			r.run();
		}
		if (mBitmapDisplayed.getBitmap() != null) {
			getProperBaseMatrix(mBitmapDisplayed, mBaseMatrix);
			setImageMatrix(getImageViewMatrix());
		}
	}




	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			event.startTracking();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.isTracking()
				&& !event.isCanceled()) {
			if (getScale() > 1.0f) {
				// If we're zoomed in, pressing Back jumps out to show the
				// entire image, otherwise Back returns the user to the gallery.
				zoomTo(1.0f);
				return true;
			}
		}
		return super.onKeyUp(keyCode, event);
	}

	public Handler mHandler = new Handler();

	@Override
	public void setImageBitmap(Bitmap bitmap) {
		setImageBitmap(bitmap, 0);
	}

	private void setImageBitmap(Bitmap bitmap, int rotation) {
		super.setImageBitmap(bitmap);
		Drawable d = getDrawable();
		if (d != null) {
			d.setDither(true);
		}

		Bitmap old = mBitmapDisplayed.getBitmap();
		mBitmapDisplayed.setBitmap(bitmap);
		mBitmapDisplayed.setRotation(rotation);

		if (old != null && old != bitmap && mRecycler != null) {
			mRecycler.recycle(old);
		}
	}

	public void clear() {
		setImageBitmapResetBase(null, true);
	}

	private Runnable mOnLayoutRunnable = null;
	private float mMinZoom =0.0f;

	// This function changes bitmap, reset base matrix according to the size
	// of the bitmap, and optionally reset the supplementary matrix.
	public void setImageBitmapResetBase(final Bitmap bitmap,
			final boolean resetSupp) {
		setImageRotateBitmapResetBase(new RotateBitmap(bitmap), resetSupp);

	}

	public void setImageRotateBitmapResetBase(final RotateBitmap bitmap,
			final boolean resetSupp) {
		final int viewWidth = getWidth();

		if (viewWidth <= 0) {
			mOnLayoutRunnable = new Runnable() {
				public void run() {
					setImageRotateBitmapResetBase(bitmap, resetSupp);
				}
			};
			return;
		}

		if (bitmap.getBitmap() != null) {
			getProperBaseMatrix(bitmap, mBaseMatrix);
			setImageBitmap(bitmap.getBitmap(), bitmap.getRotation());
		} else {
			mBaseMatrix.reset();
			setImageBitmap(null);
		}

		if (resetSupp) {
			mSuppMatrix.reset();
		}
		setImageMatrix(getImageViewMatrix());
		mMaxZoom = maxZoom();
	}

	// Center as much as possible in one or both axis. Centering is
	// defined as follows: if the image is scaled down below the
	// view's dimensions then center it (literally). If the image
	// is scaled larger than the view and is translated out of view
	// then translate it back into view (i.e. eliminate black bars).
	public void center(boolean horizontal, boolean vertical) {
		if (mBitmapDisplayed.getBitmap() == null) {
			return;
		}
		RectF rect = getDisplayRect();

		// 得到现在Bitmap的高 宽
		float height = rect.height();
		float width = rect.width();
		// Log.i(TAG, "rect :" + rect);

		float deltaX = 0, deltaY = 0;

		int viewHeight = getHeight();
		if (vertical) {
			if (height < viewHeight) {
				deltaY = (viewHeight - height) / 2 - rect.top;
			} else if (rect.top > 0) {
				deltaY = -rect.top;
			} else if (rect.bottom < viewHeight) {
				deltaY = getHeight() - rect.bottom;
			}
		}

		int viewWidth = getWidth();
		if (horizontal) {
			if (width < viewWidth) {
				deltaX = (viewWidth - width) / 2 - rect.left;
			} else if (rect.left > 0) {
				deltaX = -rect.left;
			} else if (rect.right < viewWidth) {
				deltaX = viewWidth - rect.right;
			}
		}
		
		
		postTranslate(deltaX, deltaY);
		setImageMatrix(getImageViewMatrix());
	}


	public int getImageHeight() {
		return this.mBitmapDisplayed.getBitmap().getHeight();
	}

	public int getImageWidth() {
		return this.mBitmapDisplayed.getBitmap().getWidth();
	}

	public RotateBitmap getRotateBitmap() {
		return this.mBitmapDisplayed;
	}

	public float getValue(Matrix matrix, int whichValue) {
		matrix.getValues(mMatrixValues);
		return mMatrixValues[whichValue];
	}
	

	// Get the scale factor out of the matrix.
	public float getScale(Matrix matrix) {
		return getValue(matrix, Matrix.MSCALE_X);
	}

	public float getScale() {
		return getScale(mSuppMatrix);
	}

	// Setup the base matrix so that the image is centered and scaled properly.
	private void getProperBaseMatrix(RotateBitmap bitmap, Matrix matrix) {
		float viewWidth = getWidth();
		float viewHeight = getHeight();

		float w = bitmap.getWidth();
		float h = bitmap.getHeight();
		matrix.reset();

		// We limit up-scaling to 3x otherwise the result may look bad if it's
		// a small icon.
		float widthScale = Math.min(viewWidth / w, 3.0f);
		float heightScale = Math.min(viewHeight / h, 3.0f);
		float scale = Math.min(widthScale, heightScale);

		matrix.postConcat(bitmap.getRotateMatrix());
		matrix.postScale(scale, scale);

		matrix.postTranslate((viewWidth - w * scale) / 2F, (viewHeight - h
				* scale) / 2F);
	}

	// Combine the base matrix and the supp matrix to make the final matrix.
	public Matrix getImageViewMatrix() {
		// The final matrix is computed as the concatentation of the base matrix
		// and the supplementary matrix.
		
		mDisplayMatrix.set(mBaseMatrix);
		mDisplayMatrix.postConcat(mSuppMatrix);
		return mDisplayMatrix;
	}

	// Sets the maximum zoom, which is a scale relative to the base matrix. It
	// is calculated to show the image at 400% zoom regardless of screen or
	// image orientation. If in the future we decode the full 3 megapixel image,
	// rather than the current 1024x768, this should be changed down to 200%.
	public float maxZoom() {
		if (mBitmapDisplayed.getBitmap() == null) {
			return 1F;
		}

		float fw = (float) mBitmapDisplayed.getWidth() / (float) mThisWidth;
		float fh = (float) mBitmapDisplayed.getHeight() / (float) mThisHeight;
		float max = Math.max(fw, fh) * 4;
		
		if(getMinZoom() < 1){
			max = 3.0f;
		}else{
			max = 3 * getMinZoom();
		}
		return max;
	}

	public float zoomTo(float scale, float centerX, float centerY) {
		if (scale > mMaxZoom) {
			scale = mMaxZoom;
		}
		if (scale < this.mMinZoom)
		{
			scale = this.mMinZoom;
		}

		float oldScale = getScale();
		float deltaScale = scale / oldScale;

		mSuppMatrix.postScale(deltaScale, deltaScale, centerX, centerY);
		setImageMatrix(getImageViewMatrix());
		center(true, true);
		return scale;
	}

	public void zoomTo(final float scale, final float centerX,
			final float centerY, final float durationMs) {
		final float incrementPerMs = (scale - getScale()) / durationMs;
		final float oldScale = getScale();
		final long startTime = System.currentTimeMillis();

		mHandler.post(new Runnable() {
			public void run() {
				long now = System.currentTimeMillis();
				float currentMs = Math.min(durationMs, now - startTime);
				float target = oldScale + (incrementPerMs * currentMs);
				zoomTo(target, centerX, centerY);

				if (currentMs < durationMs) {
					mHandler.post(this);
				}
			}
		});
	}

	public float zoomTo(float scale) {
		float cx = getWidth() / 2F;
		float cy = getHeight() / 2F;

		return zoomTo(scale, cx, cy); 
	}

	public void zoomToPoint(float scale, float pointX, float pointY) {
		float cx = getWidth() / 2F;
		float cy = getHeight() / 2F;

		panBy(cx - pointX, cy - pointY);
		zoomTo(scale, cx, cy);
	}

	public void zoomIn() {
		zoomIn(SCALE_RATE);
	}

	public void zoomOut() {
		zoomOut(SCALE_RATE);
	}

	public void zoomIn(float rate) {
		if (getScale() >= mMaxZoom) {
			return; // Don't let the user zoom into the molecular level.
		}
		if (mBitmapDisplayed.getBitmap() == null) {
			return;
		}

		float cx = getWidth() / 2F;
		float cy = getHeight() / 2F;

		mSuppMatrix.postScale(rate, rate, cx, cy);
		setImageMatrix(getImageViewMatrix());
	}

	public void zoomOut(float rate) {
		if (mBitmapDisplayed.getBitmap() == null) {
			return;
		}

		float cx = getWidth() / 2F;
		float cy = getHeight() / 2F;

		if (getScale() / rate < this.mMinZoom)
			rate = getScale() / this.mMinZoom;
		// Zoom out to at most 1x.
		Matrix tmp = new Matrix(mSuppMatrix);
		tmp.postScale(1F / rate, 1F / rate, cx, cy);

		tmp.postScale(1.0F / rate, 1.0F / rate, cx, cy);
	     if (getScale(tmp) > 0.5F)
	        this.mSuppMatrix.postScale(1.0F / rate, 1.0F / rate, cx, cy);
		 
//		if (getScale(tmp) < 1F) {
//			mSuppMatrix.setScale(1F, 1F, cx, cy);
//		} else {
//			mSuppMatrix.postScale(1F / rate, 1F / rate, cx, cy);
//		}
		setImageMatrix(getImageViewMatrix());
		center(true, true);
	}

	public void postTranslate(float dx, float dy) {
		mSuppMatrix.postTranslate(dx, dy);
	}

	public void panBy(float dx, float dy) {
		postTranslate(dx, dy);
		setImageMatrix(getImageViewMatrix());
	}

	public RectF getDisplayRect() {
		Matrix matrix = getImageViewMatrix();
		RectF rectF = new RectF(0.0F, 0.0F, 0.0F, 0.0F);
		if (this.mBitmapDisplayed.getBitmap() != null) {
			rectF.set(0.0F, 0.0F, this.mBitmapDisplayed.getBitmap().getWidth(),
					this.mBitmapDisplayed.getBitmap().getHeight());
		}
		matrix.mapRect(rectF);
		return rectF;
	}

	public void SetMinZoom(float minZoom) {
		this.mMinZoom = minZoom;
	}


	public float getMinZoom() {
		return mMinZoom;
	}
	/**
	 * 设置阴影
	 * 
	 * @param paramBoolean
	 */
	public void setShadow(boolean paramBoolean) {
		this.bShadow = paramBoolean;
	}

	public float zoomBy(float scale) {
		float f1 = getWidth() / 2.0F;
		float f2 = getHeight() / 2.0F;
		float oldScale = getScale();
		float deltaScale = scale / oldScale;
		
		this.mSuppMatrix.postScale(deltaScale, deltaScale, f1, f2);
		setImageMatrix(getImageViewMatrix());
		center(true, true);
		System.out.println(":getScale():"+getScale());
		


		
		
		return getScale();
	}
}
