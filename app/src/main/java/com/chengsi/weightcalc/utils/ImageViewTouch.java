package com.chengsi.weightcalc.utils;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.RectF;
import android.util.AttributeSet;


public class ImageViewTouch extends ImageViewTouchBase {
    private boolean mEnableTrackballScroll;
    
 // HighlightView四个边坐标
 	private int leftHighlightView;
 	private int rightHighlightView;
 	private int topHighlightView;
 	private int bottomHighlightView;

	private int screenshotHight;
	private int screenshotWidth;
	
 	
    public ImageViewTouch(Context context) {
        super(context);
    }

    public ImageViewTouch(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setEnableTrackballScroll(boolean enable) {
        mEnableTrackballScroll = enable;
    }

    protected void postTranslateCenter(float dx, float dy) {
        super.postTranslate(dx, dy);
        center(true, true);
    }

    private static final float PAN_RATE = 20;

    // This is the time we allow the dpad to change the image position again.
    private long mNextChangePositionTime;

    
    @Override
    protected void onDraw(Canvas canvas) {
    	// TODO Auto-generated method stub
    	super.onDraw(canvas);
    	
    	leftHighlightView= ((getWidth() - screenshotWidth) / 2);
		rightHighlightView = ((getWidth() + screenshotWidth) / 2);
		topHighlightView = ((getHeight() - screenshotHight) / 2);
		bottomHighlightView = ((getHeight() + screenshotHight) / 2);
    }
    
    public void makeDefaultRect(int screenshotWidth, int screenshotHight) {
		this.screenshotHight = screenshotHight;
		this.screenshotWidth = screenshotWidth;
		
	}
    
    @Override
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
		} else {
			if (rect.top > topHighlightView) {
				deltaY = -rect.top + topHighlightView;
			} else if (rect.bottom < bottomHighlightView) {
				deltaY = getHeight() - rect.bottom
						- (getHeight() - bottomHighlightView);
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
		} else {
			if (rect.left > leftHighlightView) {
				deltaX = -rect.left + leftHighlightView;
			} else if (rect.right < rightHighlightView) {
				deltaX = viewWidth - rect.right
						- (viewWidth - rightHighlightView);
			}
		}
		
		
		postTranslate(deltaX, deltaY);
		setImageMatrix(getImageViewMatrix());
    }
    
  
}