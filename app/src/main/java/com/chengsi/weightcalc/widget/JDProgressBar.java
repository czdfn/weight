package com.chengsi.weightcalc.widget;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.drawable.AnimationDrawable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chengsi.weightcalc.R;


public class JDProgressBar extends RelativeLayout {
	private TextView progressHint;
	private ImageView progressImage;
	private View footerTopDivider;
    public JDProgressBar(Context context) {
        super(context);
        init(context);
    }
    public JDProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }
//    public AnanProgressBar(Context context, AttributeSet attrs, int defStyle) {
//        super(context, attrs, defStyle);
//        init(context);
//    }
    private void init(Context context) {
        LayoutInflater inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View  progressBarItemView=(View)inflater.inflate(R.layout.progressbar_item, null);
        addView(progressBarItemView);
        progressImage = (ImageView)progressBarItemView.findViewById(R.id.progress_item_image);
        progressHint = (TextView) progressBarItemView.findViewById(R.id.progress_item_text);
        footerTopDivider = progressBarItemView.findViewById(R.id.footer_top_divider);
        final AnimationDrawable animationDrawable = (AnimationDrawable) progressImage.getDrawable();
        progressImage.post(new Runnable() {
			@Override
			public void run() {
				animationDrawable.start();
			}
		});
    }
    public void setHintSize(float size){
    	progressHint.setTextSize(TypedValue.COMPLEX_UNIT_DIP, size);
    }
    public void setHintColor(int resId){
    	progressHint.setTextColor(resId);
    }
    public void setHintColorList (ColorStateList list) {
    	progressHint.setTextColor(list);
    }
    public void setHintVisibility(int visibility){
    	progressHint.setVisibility(visibility);
    }
    public void setHintText(int resId){
    	progressHint.setText(resId);
    }
    public void setAnimImageVisibility(int visibility){
    	if (visibility == View.GONE) {
    		progressImage.setVisibility(visibility);
		}else if (visibility == View.VISIBLE) {
			progressImage.setVisibility(visibility);
		} 
    }
    public void setAnimImageSize(int size){
    	LayoutParams params = new LayoutParams(size, size);
    	progressImage.setLayoutParams(params);
    }
    public void setTopDividerVisible(boolean visible){
    	footerTopDivider.setVisibility(visible ? View.VISIBLE : View.GONE);
    }
}
