package com.chengsi.weightcalc.widget.rightindexlistview;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;

public class RightIndexListViewWrap extends RightIndexListView {

	public RightIndexListViewWrap(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public void createScroller() {

		mScroller = new IndexScroller(getContext(), this);

		mScroller.setAutoHide(autoHide);

		// style 1
		 mScroller.setShowIndexContainer(false);
		 mScroller.setIndexPaintColor(Color.argb(255, 49, 64, 91));

		// style 2
//		mScroller.setShowIndexContainer(true);
//		mScroller.setIndexPaintColor(Color.WHITE);

		if (autoHide)
			mScroller.hide();
		else
			mScroller.show();

	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
				MeasureSpec.AT_MOST);
		super.onMeasure(widthMeasureSpec, expandSpec);
	}
}
