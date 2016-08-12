package com.chengsi.weightcalc.listener;

import android.content.Intent;
import android.preference.PreferenceManager.OnActivityResultListener;

public interface StartActivityForResultInterface {
	public void startActivityForResult(Intent intent, int requestCode, OnActivityResultListener listener);
}
