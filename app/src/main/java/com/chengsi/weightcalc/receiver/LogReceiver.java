package com.chengsi.weightcalc.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.chengsi.weightcalc.constants.FileConstants;
import com.chengsi.weightcalc.constants.IntentConstants;

import java.io.File;


public class LogReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		if(intent.getAction().equals(IntentConstants.ACTION_LOG_DELETE)){
			File file = new File(FileConstants.ROOT_LOG_PATH);
			if(file.exists()){
				File[] files = file.listFiles();
				for(File f : files){
					if(System.currentTimeMillis() - f.lastModified() > 6 * 24 * 60 * 60 * 1000){
						f.delete();
					}
				}
			}else{
				file.mkdirs();
			}
			file = new File(FileConstants.CRASH_LOG_PATH);
			if(file.exists()){
				uploadCrashLog(file);
			}
		}
	}

	private void uploadCrashLog(File file){
//		FileReader fileReader = null;
//		try{
//			fileReader = new FileReader(file);
//			AsyncHttpClient client = new AsyncHttpClient();
//			StringBuilder sb = new StringBuilder();
//			char[] buffer = new char[2048];
//			while (fileReader.read(buffer) != -1) {
//				sb.append(buffer);
//				sb.append("\n");
//				if(sb.length() > 1024 * 1024){
//					RequestParams params = new RequestParams();
//					params.add("log", sb.toString());
//					client.post(HttpConstants.URL_LOG_UPLOAD, params, new TextHttpResponseHandler() {
//
//						@Override
//						public void onSuccess(int statusCode, Header[] headers,
//								String responseString) {
//
//						}
//
//						@Override
//						public void onFailure(int statusCode, Header[] headers,
//								String responseString, Throwable throwable) {
//
//						}
//					});
//					sb = new StringBuilder();
//				}
//			}
//		}catch(Exception e){
//			e.printStackTrace();
//		}finally{
//			try {
//				if(fileReader != null){
//					fileReader.close();
//					fileReader = null;
//				}
//				file = null;
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
//		}
	}
}
