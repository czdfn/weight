package com.chengsi.weightcalc.widget;

import android.content.Context;
import android.view.View;

import com.chengsi.weightcalc.MyApplication;

public class AbstractController {

	protected Context context;
	protected MyApplication application;
	protected View parentView;

	public AbstractController(Context context) {
		this(context, null);
	}

	public AbstractController(Context context, View view) {
		this.context = context;
		this.parentView = view;
		application = (MyApplication) context.getApplicationContext();
	}

	public void clear() {

	}

}