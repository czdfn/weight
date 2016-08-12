package com.chengsi.weightcalc.widget;

/**   
* @Title: PreferenceRightDetail.java 
* @author xuyingjian@jiadao.cn
* @date 2014年8月20日 下午3:12:18 
*/

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.text.Html;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chengsi.weightcalc.R;


/**
 * @Description: 左侧为标题，右侧为详情
 * @author xuyingjian@ruijie.com.cn   
 * @date 2014年8月20日 下午3:12:18 
 * @version 2.30  
 */
public class PreferenceRightDetailView extends RelativeLayout {
	
	private TextView titleTv;
	private TextView contentTv;
	private ImageView rightIV;
	private Context mContext;

    private int location = 2;

	/**
	 * @param context
	 * @param attrs
	 */
	public PreferenceRightDetailView(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
		initView(attrs);
	}
	
	/**
	 * 
	 */
	public PreferenceRightDetailView(Context context, AttributeSet attrs, int defaultStyle) {
		super(context, attrs, defaultStyle);
		mContext = context;
		initView(attrs);
	}

	private void initView(AttributeSet attrs) {
		RelativeLayout layout = (RelativeLayout) LayoutInflater.from(mContext).inflate(R.layout.preference_detail_item, this, true);
		titleTv = (TextView) findViewById(R.id.title);
		contentTv = (TextView) findViewById(R.id.content);
		rightIV = (ImageView) findViewById(R.id.arrow);
		View dividerTopView = findViewById(R.id.divider_top);
        View dividerBottomView = findViewById(R.id.divider_bottom);
		TypedArray type = mContext.getTheme().obtainStyledAttributes(attrs, R.styleable.PreferenceRightDetailView, 0, 0);
		titleTv.setText(type.getString(R.styleable.PreferenceRightDetailView_prefrence_title));
		contentTv.setText(type.getString(R.styleable.PreferenceRightDetailView_content));
		if(type.getColorStateList(R.styleable.PreferenceRightDetailView_titleColor) != null){
			titleTv.setTextColor(type.getColorStateList(R.styleable.PreferenceRightDetailView_titleColor));
		}
		if(type.getColorStateList(R.styleable.PreferenceRightDetailView_contentColor) != null){
			contentTv.setTextColor(type.getColorStateList(R.styleable.PreferenceRightDetailView_contentColor));
		}
		titleTv.setTextSize(TypedValue.COMPLEX_UNIT_PX, type.getDimensionPixelSize(R.styleable.PreferenceRightDetailView_titleSize, dip2px(mContext, 18)));
		contentTv.setTextSize(TypedValue.COMPLEX_UNIT_PX, type.getDimensionPixelSize(R.styleable.PreferenceRightDetailView_contentSize, dip2px(mContext, 16)));
		int style = type.getInteger(R.styleable.PreferenceRightDetailView_accessStyle, 1);
		rightIV.setImageResource(type.getResourceId(R.styleable.PreferenceRightDetailView_accessImage, R.drawable.setting_right_arrow));
		if(style != 1){
			rightIV.setVisibility(View.GONE);
		}else{
			rightIV.setVisibility(View.VISIBLE);
		}
		location = type.getInteger(R.styleable.PreferenceRightDetailView_divider_location, 2);
		
		int contentGravity = type.getInteger(R.styleable.PreferenceRightDetailView_content_gravity, 0);
		LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, 1);
		if(location == 0){
            dividerTopView.setVisibility(View.GONE);
            dividerBottomView.setVisibility(View.GONE);
		}else if(location == 1){
            dividerTopView.setVisibility(View.VISIBLE);
            dividerBottomView.setVisibility(View.GONE);
		}else if(location == 2){
            dividerTopView.setVisibility(View.GONE);
            dividerBottomView.setVisibility(View.VISIBLE);
		}else{
            dividerTopView.setVisibility(View.VISIBLE);
            dividerBottomView.setVisibility(View.VISIBLE);
		}
		
		switch (contentGravity) {
		case 0:
			contentTv.setGravity(Gravity.RIGHT|Gravity.CENTER_VERTICAL);
			break;
		case 1:
			contentTv.setGravity(Gravity.LEFT|Gravity.CENTER_VERTICAL);
			break;
		case 2:
			contentTv.setGravity(Gravity.CENTER);
			break;

		default:
			break;
		}
	}
	
	 public static int dip2px(Context context, float dpValue) {  
	     final float scale = context.getResources().getDisplayMetrics().density;  
	     return (int) (dpValue * scale + 0.5f);  
	 }
	
	@Override
	protected void onLayout(boolean changed, int left, int top, int right,
			int bottom) {
		super.onLayout(changed, left, top, right, bottom);
	}
	
	public void setContent(String content){
		this.contentTv.setText(content);
	}

	public void setHtmlTitle(String htmlTitle){
		this.titleTv.setText(Html.fromHtml(htmlTitle));
	}
	public void setTitle(String title){
		this.titleTv.setText(title);
	}
	
	public void setContentSize(int unit, int size){
		this.contentTv.setTextSize(unit, size);
	}
	
	public void setTitleSize(int unit, int size){
		this.titleTv.setTextSize(unit, size);
	}
	
	public void setRightBitmap(Bitmap bitmap){
		this.rightIV.setImageBitmap(bitmap);
	}
	
	public CharSequence getContent(){
		return this.contentTv.getText(); 
	}

	public CharSequence getTitle(){
		return this.titleTv.getText(); 
	}
	
	public void setContentGravity(int gravity){
		this.contentTv.setGravity(gravity);
	}

	public void setTitleGravity(int gravity){
		this.titleTv.setGravity(gravity);
	}

	public void setAccessImageVisible(int visible){
		this.rightIV.setVisibility(visible);
	}
}
