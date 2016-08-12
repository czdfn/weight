package com.chengsi.weightcalc.widget;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.chengsi.weightcalc.MyApplication;
import com.chengsi.weightcalc.R;


public class JDToast extends Toast {
	
	private TextView textView = null;
	
	public static JDToast makeText (Context context, int textId, int duration) {
		return makeText(context, context.getString(textId), duration);
	}
	
	public static JDToast makeText (Context context, CharSequence str, int duration) {
		JDToast poast = new JDToast(context);
		poast.textView.setText(str);
		poast.setDuration(duration);
		return poast;
	}

	public JDToast(Context context) {
		super(context);
		View view = LayoutInflater.from(context).inflate(R.layout.poast, null);
		textView = (TextView)view.findViewById(R.id.text);
		this.setView(view);
		this.setGravity(Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, (int)(92 * context.getResources().getDisplayMetrics().density));
	}
	@Override
	public void show() {
		if (MyApplication.getInstance() != null) {
			super.show();
		}
	}
	@Override
	public void setText(CharSequence s) {
		textView.setText(s);
    }
	@Override
	public void setText(int resId) {
		textView.setText(resId);
	}
}
