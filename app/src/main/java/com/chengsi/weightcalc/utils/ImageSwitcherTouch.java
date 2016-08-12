package com.chengsi.weightcalc.utils;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageSwitcher;

public class ImageSwitcherTouch extends ImageSwitcher {

	public ImageSwitcherTouch(Context context) {
		super(context);

	}

	public ImageSwitcherTouch(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public void setDrawable(Drawable paramDrawable, int paramInt1, int paramInt2) {
		ImageViewTouch imageViewTouche = (ImageViewTouch) getCurrentView();
		imageViewTouche.setAdjustViewBounds(true);
		imageViewTouche.setMaxWidth(paramInt1);
		imageViewTouche.setMaxHeight(paramInt2);
		imageViewTouche.setImageDrawable(paramDrawable);
	}

	public void setImageBitmap(Bitmap paramBitmap) {
		ImageViewTouch localImageViewTouche = (ImageViewTouch) getCurrentView();
		localImageViewTouche.setImageBitmap(paramBitmap);
		localImageViewTouche.setImageRotateBitmapResetBase(new RotateBitmap(
				paramBitmap), true);
	}
}
