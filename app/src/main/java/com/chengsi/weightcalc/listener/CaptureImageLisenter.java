package com.chengsi.weightcalc.listener;

import android.net.Uri;
import android.preference.PreferenceManager.OnActivityResultListener;

public abstract class CaptureImageLisenter implements OnActivityResultListener {

	private int picWidth;
	private Uri fileUri;
	public int getPicWidth() {
		return picWidth;
	}
	public CaptureImageLisenter setPicWidth(int picWidth) {
		this.picWidth = picWidth;
		return this;
	}
	public Uri getFileUri() {
		return fileUri;
	}
	public CaptureImageLisenter setFileUri(Uri fileUri) {
		this.fileUri = fileUri;
		return this;
	}
	
	
	
	

}
