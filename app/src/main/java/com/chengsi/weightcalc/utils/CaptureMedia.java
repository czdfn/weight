package com.chengsi.weightcalc.utils;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.os.StatFs;
import android.provider.MediaStore;
import android.util.Log;

import com.chengsi.weightcalc.MyApplication;
import com.chengsi.weightcalc.R;
import com.chengsi.weightcalc.bean.LocalImageInfo;
import com.chengsi.weightcalc.constants.FileConstants;
import com.chengsi.weightcalc.listener.CaptureImageLisenter;
import com.chengsi.weightcalc.listener.StartActivityForResultInterface;
import com.chengsi.weightcalc.manager.AlbumManager;
import com.chengsi.weightcalc.widget.JDToast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


/**
 * Capture Photo
 * 
 * @author mobinweaver
 * 
 */
public class CaptureMedia {

	public static final String ANANROOTDIR = "Whistle";
	public static final String ANANDIR = "Whistle";
	public static final String PHOTOD_IR = "photo";
	public static final String THUMB_IR = "thumb";
	public static final String BG_IR = "background";
	public static final String VEDIO_DIR = "vedio";
	public static final String IMAGE_TYPE = ".jpg";
	public static final String IMAGE_NAME = "IMG_";
	public static final String VEDIO_TYPE = ".mp4";
	public static final String VEDIO_NAME = "VID_";
	public static final String TAG = "MediaUtils";
	public static final int MEDIA_TYPE_IMAGE = 1;
	public static final int MEDIA_TYPE_VIDEO = 2;
	public static final String KEY_TYPE_CAREMA = "camera";
	public static final String KEY_TYPE_ALBUM = "album";
	private static final String KEY_IMAGE_TYPE = "image/*";
	public static final int KEY_REQUEST_CAREMA = 101;
	public static final int KEY_REQUEST_ALBUM = 102;
	private static final int KEY_REQUEST_VIDEO = 103;
	private static final int KEY_REQUEST_AUDIO = 104;
	public static final int RESULT_CANCELED = 0;
	public static final int RESULT_OK = -1;
	public static final int STATUS_SUCCESS = 1;
	public static final int STATUS_FAILDED = 2;
	public static final int STATUS_CANCELED = 3;

	/**
	 * 获取拍照、相册的图片
	 * 
	 * 
	 */
	public static void captureImage(String type, StartActivityForResultInterface activity,
			final CaptureImageLisenter lisenter) {
		if (KEY_TYPE_CAREMA.equals(type)) {
			Intent captureImageCamera = new Intent(
					MediaStore.ACTION_IMAGE_CAPTURE);
			Uri fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);
			if(fileUri == null){
				return;
			}
			captureImageCamera.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
			lisenter.setFileUri(fileUri).setPicWidth(300);
			activity.startActivityForResult(captureImageCamera,
					KEY_REQUEST_CAREMA, lisenter);
		} else if (KEY_TYPE_ALBUM.equals(type)) {
//			Intent captureImageAlbum = new Intent(Intent.ACTION_GET_CONTENT);
//			captureImageAlbum.addCategory(Intent.CATEGORY_OPENABLE);
//			captureImageAlbum.setType(KEY_IMAGE_TYPE);
//			Uri fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);
//			lisenter.setFileUri(fileUri).setPicWidth(300);
//			activity.startActivityForResult(captureImageAlbum, KEY_REQUEST_ALBUM, lisenter);
			MyApplication.getInstance().albumManager.selectSinglePicCanReChoose((Activity)activity, new AlbumManager.OnSelectSinglePicListener() {
				
				@Override
				public void onFinished(boolean cancel, String picPath) {
					// TODO Auto-generated method stub
					Intent data = new Intent();
					data.putExtra(BitmapUtils.BITMATSRC, picPath);
					data.setData(Uri.parse(picPath));
					lisenter.onActivityResult(CaptureMedia.KEY_REQUEST_ALBUM, cancel ? BitmapUtils.RESULT_CANCELED : BitmapUtils.RESULT_OK, data);
				}
			});
		}
	}

	/**
	 * 获取拍照、相册的图片
	 *
	 *
	 */
	public static void captureMultiImage(String type, StartActivityForResultInterface activity,
									final CaptureImageLisenter lisenter) {
		if (KEY_TYPE_CAREMA.equals(type)) {
			Intent captureImageCamera = new Intent(
					MediaStore.ACTION_IMAGE_CAPTURE);
			Uri fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);
			if(fileUri == null){
				return;
			}
			captureImageCamera.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
			lisenter.setFileUri(fileUri).setPicWidth(300);
			activity.startActivityForResult(captureImageCamera,
					KEY_REQUEST_CAREMA, lisenter);
		} else if (KEY_TYPE_ALBUM.equals(type)) {
//			Intent captureImageAlbum = new Intent(Intent.ACTION_GET_CONTENT);
//			captureImageAlbum.addCategory(Intent.CATEGORY_OPENABLE);
//			captureImageAlbum.setType(KEY_IMAGE_TYPE);
//			Uri fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);
//			lisenter.setFileUri(fileUri).setPicWidth(300);
//			activity.startActivityForResult(captureImageAlbum, KEY_REQUEST_ALBUM, lisenter);
			MyApplication.getInstance().albumManager.selectMultiPic((Activity) activity, 12, true, new AlbumManager.OnSelectMultiPicListener() {
				@Override
				public void onFinished(boolean cancel, ArrayList<LocalImageInfo> selectList) {

				}
			});
		}
	}


	private Bitmap getPicFromBytes(byte[] bytes, BitmapFactory.Options opts) {
		if (bytes != null)
			if (opts != null)
				return BitmapFactory.decodeByteArray(bytes, 0, bytes.length,
						opts);
			else
				return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
		return null;
	}

	private byte[] readStream(InputStream inStream) throws Exception {
		byte[] buffer = new byte[1024];
		int len = -1;
		ByteArrayOutputStream outStream = new ByteArrayOutputStream();
		while ((len = inStream.read(buffer)) != -1) {
			outStream.write(buffer, 0, len);
		}
		byte[] data = outStream.toByteArray();
		outStream.close();
		inStream.close();
		return data;

	}


	/**
	 * 获取照片存储文件URi
	 * 
	 * @param type
	 *            MEDIA_TYPE_IMAGE =1 c =2
	 * @return 文件URi
	 */
	private static Uri getOutputMediaFileUri(int type) {
		File file = getOutputMediaFile(type);
		if(file == null){
			return null;
		}
		return Uri.fromFile(file);
	}
	
	public static String getPhotoDir()
	{
		return FileConstants.ROOT_IMAGE_PATH + File.separator +PHOTOD_IR;
	}
	public static String getThumbDir()
	{
		return FileConstants.ROOT_IMAGE_PATH + File.separator +THUMB_IR;
	}
	
	public static String getBackgroundDir()
	{
		return FileConstants.ROOT_IMAGE_PATH+ File.separator + BG_IR;
	}
	
	private static File getOutputMediaFile(int type) {
		// To be safe, you should check that the SDCard is mounted
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			// using Environment.getExternalStorageState() before doing this.
			File mediaStorageDir = new File(FileConstants.ROOT_IMAGE_PATH + File.separator +PHOTOD_IR);
			// This location works best if you want the created images to be shared
			// between applications and persist after your app has been uninstalled.
			
			// Create the storage directory if it does not exist
			if (!mediaStorageDir.exists()) {
				File path = Environment.getExternalStorageDirectory();
				// 取得sdcard文件路径
				StatFs statfs = new StatFs(path.getPath());
				// 获取block的SIZE
				long blocSize = statfs.getBlockSize();
				// 获取BLOCK数量
				long totalBlocks = statfs.getBlockCount();
				// 己使用的Block的数量
				long availaBlock = statfs.getAvailableBlocks();

				float rate = (availaBlock * blocSize)
						/ (float) (totalBlocks * blocSize);
				if (rate < 0.02) {
					JDToast.makeText(MyApplication.getInstance(), MyApplication.getInstance().getString(R.string.storage_is_full), JDToast.LENGTH_SHORT).show();
					return null;
				}
				if (!mediaStorageDir.mkdirs()) {
					Log.d(TAG, "failed to create directory");
					return null;
				}
			}

			// Create a media file name
			String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss")
					.format(new Date());
			File mediaFile;
			if (type == MEDIA_TYPE_IMAGE) {
				mediaFile = new File(mediaStorageDir.getPath()+ File.separator + IMAGE_NAME + timeStamp
						+ IMAGE_TYPE);
			} else if (type == MEDIA_TYPE_VIDEO) {
				mediaFile = new File(mediaStorageDir.getPath() + File.separator
						+ VEDIO_DIR + File.separator + VEDIO_NAME + timeStamp
						+ VEDIO_TYPE);
			} else {
				return null;
			}

			return mediaFile;
		} else {
			JDToast.makeText(MyApplication.getInstance(), MyApplication.getInstance().getString(R.string.sdcard_not_found), JDToast.LENGTH_SHORT).show();
			return null;
		}
	}
}
