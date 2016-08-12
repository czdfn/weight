package com.chengsi.weightcalc.utils;

import android.app.Activity;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;

import com.chengsi.weightcalc.constants.FileConstants;
import com.chengsi.weightcalc.widget.JDToast;

import java.io.File;
import java.io.FileOutputStream;

//import org.spongycastle.util.encoders.Hex;


public class FileUtil {
	private static final String TAG = "FileUtil";

	public static String getChoosedPicturePath(Uri uri, Activity activity) {
		String imagePath = "";
		if (null == uri) {
			return "";
		}
		if (uri.toString().startsWith("content://")) {
			String[] projection = { MediaStore.Images.Media.DATA };
			Cursor cursor = activity.managedQuery(uri, projection, null, null,
					null);
			int column_index = cursor
					.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
			cursor.moveToFirst();
			imagePath = cursor.getString(column_index);
		} else {
			if (uri.toString().startsWith("file://")) {
				imagePath = uri.toString().substring("file://".length());
			} else {
				imagePath = uri.toString();
			}
		}
		String path = null;
		// 若用户选择的是图片文件,则返回路径。
		String tempPath = imagePath.toLowerCase();
		if (tempPath.endsWith(".jpg") || tempPath.endsWith(".png")
				|| tempPath.endsWith(".jpeg")) {
			path = imagePath;
		} else {
			JDToast.makeText(activity, "图片格式出错", JDToast.LENGTH_SHORT).show();
		}
		return path;
	}

	/*
	 * private Bitmap getimage(String srcPath) { BitmapFactory.Options newOpts =
	 * new BitmapFactory.Options(); newOpts.inJustDecodeBounds = true; Bitmap
	 * bitmap = BitmapFactory.decodeFile(srcPath,newOpts);//此时返回bm为空
	 * newOpts.inJustDecodeBounds = false; float reqHeight =
	 * 1280f;//这里设置高度为1280f float reqWidth = 720f;//这里设置宽度为720f
	 * 
	 * int height = newOpts.outHeight; int width = newOpts.outWidth; int
	 * inSampleSize = 1;
	 * 
	 * if (height > reqHeight || width > reqWidth) { int heightRatio =
	 * Math.round((float) height/ (float) reqHeight); int widthRatio =
	 * Math.round((float) width / (float) reqWidth); inSampleSize = heightRatio
	 * > widthRatio ? heightRatio : widthRatio; }
	 * 
	 * if (inSampleSize < 1) inSampleSize = 1; newOpts.inSampleSize =
	 * inSampleSize;//设置缩放比例 bitmap = BitmapFactory.decodeFile(srcPath,
	 * newOpts); FileOutputStream fos = null; fos = new
	 * FileOutputStream(file.getPath());
	 * resizedBitmap.compress(Bitmap.CompressFormat.JPEG, 50, fos); return
	 * compressImage(bitmap);//压缩好比例大小后再进行质量压缩 }
	 */

	/**
	 * 压缩图片从相册返回
	 * */
	public static File compressFile(String filepath, int rotato) {

		File PHOTO_DIR = new File(FileConstants.ROOT_IMAGE_PATH);

		if (!PHOTO_DIR.exists()) {
			PHOTO_DIR.mkdirs();
		}
		File file = new File(PHOTO_DIR, System.currentTimeMillis() + ".jpg");
		if (file.exists()) {
			file.delete();
		}
		try {
			ExifInterface eff = new ExifInterface(filepath);
			int dree = eff.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);

			Options options = new Options();
			options.inJustDecodeBounds = true;
			Bitmap photo = BitmapFactory.decodeFile(filepath, options);
			options.inJustDecodeBounds = false;
			float reqHeight = 1280f;// 这里设置高度为1280f
			float reqWidth = 720f;// 这里设置宽度为720f
			
			int height = options.outHeight;
			int width = options.outWidth;
			int inSampleSize = 1;
			
			if (height > reqHeight || width > reqWidth) {
				int heightRatio = Math
						.round((float) height / (float) reqHeight);
				int widthRatio = Math.round((float) width / (float) reqWidth);
				inSampleSize = heightRatio > widthRatio ? heightRatio
						: widthRatio;
			}

			if (inSampleSize < 1)
				inSampleSize = 1;
			options.inSampleSize = inSampleSize;// 设置缩放比例
			photo = BitmapFactory.decodeFile(filepath, options);
			Matrix matrix1 = new Matrix();
			if (rotato != -1) {
				matrix1.setRotate(rotato);
			} else {
				matrix1.setRotate(JDUtils.getPictureDegree(dree));
			}
			Bitmap b = Bitmap.createBitmap(photo, 0, 0, photo.getWidth(),
					photo.getHeight(), matrix1, true);
			FileOutputStream fos = null;
			fos = new FileOutputStream(file.getPath());
			b.compress(Bitmap.CompressFormat.JPEG, 70, fos);
			photo.recycle();
			b.recycle();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return file;

	}

	/**
	 * 清空临时文件
	 * */
	public static void deleteTimePhoto() {
		
		File PHOTO_DIR = new File(Environment.getExternalStorageDirectory().getPath() +"/myPhoto");

		if (!PHOTO_DIR.exists()) {
			return;
		}
		File[] files = PHOTO_DIR.listFiles();
		if (files.length > 0) {
			for (File f : files) {
				f.delete();
			}
		}
		
	}


	/**
	 * 清空临时文件
	 * */
	public static void deleteTempFiles(File file) {
		if (null == file || !file.exists()) {
			return;
		}
		File[] files = file.listFiles();
		if (files.length > 0) {
			for (File f : files) {
				f.delete();
			}
		}
	}
	
	/**
	 * 
	 * @Description: 删除该文件或者删除该目录下面的所有文件包括该目录 
	 * @param file
	 */
	public static void deleteAllFiles(File file){
		if(file.isDirectory()){
			File[] fileArr = file.listFiles();
			for(File temp : fileArr){
				deleteAllFiles(temp);
			}
			if(file.listFiles().length == 0){
				file.delete();
			}
		}else{
			file.delete();
		}
	}
}
