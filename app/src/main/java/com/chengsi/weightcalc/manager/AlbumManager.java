package com.chengsi.weightcalc.manager;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.ContentObserver;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.provider.MediaStore.Audio.Albums;
import android.provider.MediaStore.Images.Media;

import com.chengsi.weightcalc.MyApplication;
import com.chengsi.weightcalc.R;
import com.chengsi.weightcalc.activity.AlbumListActivity;
import com.chengsi.weightcalc.bean.AlbumInfo;
import com.chengsi.weightcalc.bean.LocalImageInfo;
import com.chengsi.weightcalc.constants.FileConstants;
import com.chengsi.weightcalc.widget.JDToast;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;

/**
 * 专辑帮助类
 * 
 * @author Administrator
 * 
 */
public class AlbumManager {
	public static final String KEY_MODE = "select_mode";
	public static final String KEY_FOR_ZONE = "is_for_zone";
	public static final String KEY_RECHOOSE = "can_rechoose";
	public static final int MODE_SINGLE = 111;
	public static final int MODE_MULTI = 222;
	
	public interface OnGetAlbumListListener{
		public void OnFinished(List<AlbumInfo> list);
	}
	public interface OnAlbumChangedListener{
		public void OnChanged(List<AlbumInfo> list);
	}
	public interface OnSelectSinglePicListener{
		public void onFinished(boolean cancel, String picPath);
	}
	public interface OnSelectMultiPicListener{
		public void onFinished(boolean cancel, ArrayList<LocalImageInfo> selectList);
	}
	private final String TAG = getClass().getSimpleName();
	private Context context;
	private ContentResolver cr;

//	// 缩略图列表
//	private HashMap<String, String> thumbnailList = new HashMap<String, String>();
	// 专辑列表
	private List<HashMap<String, String>> albumList = new ArrayList<HashMap<String, String>>();
	private HashMap<String, AlbumInfo> bucketList = new HashMap<String, AlbumInfo>();
	private List<LocalImageInfo> myRecvImage = new ArrayList<LocalImageInfo>();
	
	private List<OnAlbumChangedListener> listenerList = new ArrayList<OnAlbumChangedListener>();
	private ContentObserver observer = new ContentObserver(MyApplication.getInstance().getMainHandler()) {
		@Override
		public void onChange(boolean selfChange) {
			bucketList.clear();
			albumList.clear();
//			thumbnailList.clear();
			if (listenerList.size() != 0) {
				List<AlbumInfo> dataList = getImagesBucketList(true);
				for (int i = 0; i < listenerList.size(); i++) {
					listenerList.get(i).OnChanged(dataList);
				}
			}
		}
	};
	private static AlbumManager instance;
	private ArrayList<LocalImageInfo> selectList = new ArrayList<LocalImageInfo>();
	private ArrayList<LocalImageInfo> selectCache = new ArrayList<LocalImageInfo>();
	
	private AlbumManager(Context context) {
		if (this.context == null) {
			this.context = context;
			cr = context.getContentResolver();
			cr.registerContentObserver(Albums.EXTERNAL_CONTENT_URI, true, observer);
		}
	}

	public static AlbumManager getInstance(Context context) {
		if (instance == null) {
			instance = new AlbumManager(context.getApplicationContext());
		}
		return instance;
	}
	public void addOnAlbumChangedListener(OnAlbumChangedListener listener){
		listenerList.add(listener);
	}
	public void removeOnAlbumChangedListener(OnAlbumChangedListener listener){
		listenerList.remove(listener);
	}
	public void clear(){
		listenerList.clear();
		cr.unregisterContentObserver(observer);
	}
	/**************************************************************************/
	private List<Activity> relatedActivity = new ArrayList<Activity>();
	public void registRelatedActivity(Activity activity){
		relatedActivity.add(activity);
	}
	public void unregistRelatedActivity(Activity activity){
		relatedActivity.remove(activity);
	}
	
	private void finishRelatedActivity(){
		for (int i = 0; i < relatedActivity.size(); i++) {
			if (relatedActivity.get(i) != null) {
				relatedActivity.get(i).finish();
			}
		}
	}
	/**************************************************************************/
	private OnSelectSinglePicListener selectSingleListener;
	private OnSelectMultiPicListener selectMultiListener;
	private int selectLimit;
	
	private int currentMode;
	private boolean isForZone = false;
	public void selectSinglePic(Activity activity, OnSelectSinglePicListener listener){
		selectSingleListener = listener;
		currentMode = MODE_SINGLE;
		Intent intent = new Intent();
		intent.putExtra(KEY_MODE, MODE_SINGLE);
		intent.setClass(context, AlbumListActivity.class);
		activity.startActivity(intent);
	}
	public void selectSinglePicCanReChoose(Activity activity, OnSelectSinglePicListener listener){
		selectSingleListener = listener;
		currentMode = MODE_SINGLE;
		Intent intent = new Intent();
		intent.putExtra(KEY_MODE, MODE_SINGLE);
		intent.putExtra(KEY_RECHOOSE, true);
		intent.setClass(context, AlbumListActivity.class);
		activity.startActivity(intent);
	}
	public void selectMultiPic(Activity activity, int limit, boolean isForZone, OnSelectMultiPicListener listener){
		this.isForZone = isForZone;
		selectLimit = limit;
		selectMultiListener = listener;
		currentMode = MODE_MULTI;
		Intent intent = new Intent();
		intent.putExtra(KEY_MODE, MODE_MULTI);
		intent.putExtra(KEY_FOR_ZONE, isForZone);
		intent.setClass(context, AlbumListActivity.class);
		activity.startActivity(intent);
	}
	public boolean selectPic(LocalImageInfo image){
		if (!isImage(image.imagePath)) {
			JDToast.makeText(context, R.string.image_illegal, JDToast.LENGTH_SHORT).show();
			return false;
		}
		File file = new File(image.imagePath);
		if (file.getName().toLowerCase().endsWith("gif") && file.length() > 2 * 1024 * 1024) {
			JDToast.makeText(context, context.getString(R.string.gif_length_limit, selectLimit), JDToast.LENGTH_SHORT).show();
			return false;
		}
		if (selectList.size() < selectLimit) {
			selectList.add(image);
			selectCache.add(image);
			return true;
		}
		JDToast.makeText(context, context.getString(R.string.image_select_limit, selectLimit), JDToast.LENGTH_SHORT).show();
		return false;
	}
	public void removePic(LocalImageInfo image){
		selectList.remove(image);
		selectCache.add(image);
	}
	public void addPicFromCamera(LocalImageInfo image){
		selectList.add(image);
	}
	public List<LocalImageInfo> getSelectList(){
		return selectList;
	}
	public void setForZone(){
		isForZone = true;
	}
	public void onCancel(){
		finishRelatedActivity();
		reset(isForZone, true);
		if (currentMode == MODE_SINGLE) {
			if (selectSingleListener != null) {
				selectSingleListener.onFinished(true, null);
			}
		}else {
			if (selectMultiListener != null) {
				selectMultiListener.onFinished(true, selectList);
			}
		}
	}
	public void onComplete(String picPath){
		if (!isImage(picPath)) {
			JDToast.makeText(context, R.string.image_illegal, JDToast.LENGTH_SHORT).show();
			return;
		}
		finishRelatedActivity();
		selectSingleListener.onFinished(false, picPath);
		reset(isForZone, false);
	}
	public void onCompleteAllowRechoose(String picPath){
		if (!isImage(picPath)) {
			JDToast.makeText(context, R.string.image_illegal, JDToast.LENGTH_SHORT).show();
			return;
		}
		if (selectSingleListener != null) {
			selectSingleListener.onFinished(false, picPath);
		}
	}
	public void onCompleteForRechoose(){
		if (!isForZone) {
			finishRelatedActivity();
			reset();
		}
	}
	public void onComplete(){
		finishRelatedActivity();
		selectMultiListener.onFinished(false, selectList);
		reset(isForZone, false);
	}
	public void reset(boolean isForZone, boolean isCancel){
		if (!isForZone) {
			selectSingleListener = null;
			selectMultiListener = null;
			selectList.clear();
			isForZone = false;
			selectLimit = 0;
			currentMode = 0;
		}else {
			if (isCancel) {
				for (LocalImageInfo image : selectCache) {
					if (selectList.contains(image)) {
						selectList.remove(image);
					}else {
						selectList.add(image);
					}
				}
			}
		}
		selectCache.clear();
	}
	public void reset(){
		selectSingleListener = null;
		selectMultiListener = null;
		selectList.clear();
		selectCache.clear();
		selectLimit = 0;
		currentMode = 0;
		isForZone = false;
	}
	public int getSelectCount(){
		return selectList.size();
	}
	public String getLimit(){
		return String.valueOf(selectLimit);
	}
	
	
	private boolean isImage(String path) {
		try {
			FileInputStream inputStream = new FileInputStream(path);
			byte[] buffer = new byte[2];
			int readIndex = inputStream.read(buffer);
			inputStream.close();
			if (readIndex != -1) {
				String filecode = "";
				for (int i = 0; i < buffer.length; i++) {
					filecode += Integer.toString((buffer[i] & 0xFF));
				}
				switch (Integer.valueOf(filecode)) {
				case 13780: // png
				case 6677: //bmp
				case 255216: // jpg,jpeg
				case 8273:
				case 7173: // gif
					
				case 7332: // tiff
				case 102: // 102/103/105 pcx
				case 103:
				case 105:
				case 5666: // psd
				case 6976: // cdr
				case 197208: // eps
					return true;
				}
			}else {
				return false;
			}
		} catch (FileNotFoundException e) {
			return false;
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			Bitmap bm = BitmapFactory.decodeFile(path);
			return bm != null;
		} catch (Throwable e) {
			return false;
		}
	}
	/**************************************************************************/
//	/**
//	 * 得到缩略图
//	 */
//	private void getThumbnail() {
//		String[] projection = { Thumbnails._ID, Thumbnails.IMAGE_ID, Thumbnails.DATA };
//		Cursor cursor = cr.query(Thumbnails.EXTERNAL_CONTENT_URI, projection, null, null, null);
//		getThumbnailColumnData(cursor);
//	}

//	/**
//	 * 从数据库中得到缩略图
//	 * @param cur
//	 */
//	private void getThumbnailColumnData(Cursor cur) {
//		if (cur.moveToFirst()) {
//			int image_id;
//			String image_path;
//			int image_idColumn = cur.getColumnIndex(Thumbnails.IMAGE_ID);
//			int dataColumn = cur.getColumnIndex(Thumbnails.DATA);
//			do {
//				// Get the field values
//				image_id = cur.getInt(image_idColumn);
//				image_path = cur.getString(dataColumn);
//				thumbnailList.put("" + image_id, image_path);
//			} while (cur.moveToNext());
//		}
//	}

	/**
	 * 得到原图
	 */
	void getAlbum() {
		String[] projection = { Albums._ID, Albums.ALBUM, Albums.ALBUM_ART, Albums.ALBUM_KEY, Albums.ARTIST, Albums.NUMBER_OF_SONGS };
		Cursor cursor = cr.query(Albums.EXTERNAL_CONTENT_URI, projection, null,	null, null);
		getAlbumColumnData(cursor);
	}
	/**
	 * 从本地数据库中得到原图
	 * @param cur
	 */
	private void getAlbumColumnData(Cursor cur) {
		if (cur.moveToFirst()) {
			int _id;
			String album;
			String albumArt;
			String albumKey;
			String artist;
			int numOfSongs;

			int _idColumn = cur.getColumnIndex(Albums._ID);
			int albumColumn = cur.getColumnIndex(Albums.ALBUM);
			int albumArtColumn = cur.getColumnIndex(Albums.ALBUM_ART);
			int albumKeyColumn = cur.getColumnIndex(Albums.ALBUM_KEY);
			int artistColumn = cur.getColumnIndex(Albums.ARTIST);
			int numOfSongsColumn = cur.getColumnIndex(Albums.NUMBER_OF_SONGS);

			do {
				// Get the field values
				_id = cur.getInt(_idColumn);
				album = cur.getString(albumColumn);
				albumArt = cur.getString(albumArtColumn);
				albumKey = cur.getString(albumKeyColumn);
				artist = cur.getString(artistColumn);
				numOfSongs = cur.getInt(numOfSongsColumn);

				// Do something with the values.
//				Log.i(TAG, _id + " album:" + album + " albumArt:" + albumArt
//						+ "albumKey: " + albumKey + " artist: " + artist
//						+ " numOfSongs: " + numOfSongs + "---");
				HashMap<String, String> hash = new HashMap<String, String>();
				hash.put("_id", _id + "");
				hash.put("album", album);
				hash.put("albumArt", albumArt);
				hash.put("albumKey", albumKey);
				hash.put("artist", artist);
				hash.put("numOfSongs", numOfSongs + "");
				albumList.add(hash);
			} while (cur.moveToNext());
		}
		cur.close();
	}

	/**
	 * 是否创建了图片集
	 */
	boolean hasBuildImagesBucketList = false;

	/**
	 * 得到图片集
	 */
	private void buildImagesBucketList() {
		long startTime = System.currentTimeMillis();

//		// 构造缩略图索引
//		getThumbnail();

		// 构造相册索引
		String columns[] = new String[] { Media._ID, Media.BUCKET_ID,
				Media.PICASA_ID, Media.DATA, Media.DISPLAY_NAME, Media.TITLE,
				Media.SIZE, Media.BUCKET_DISPLAY_NAME, Media.DATE_MODIFIED};
		// 得到一个游标
		Cursor cur = cr.query(Media.EXTERNAL_CONTENT_URI, columns, null, null,
				null);
		if (cur == null) {
			return;
		}
		if (cur.moveToFirst()) {
			// 获取指定列的索引
			int photoIDIndex = cur.getColumnIndexOrThrow(Media._ID);
			int photoPathIndex = cur.getColumnIndexOrThrow(Media.DATA);
			int photoNameIndex = cur.getColumnIndexOrThrow(Media.DISPLAY_NAME);
			int photoTitleIndex = cur.getColumnIndexOrThrow(Media.TITLE);
			int photoSizeIndex = cur.getColumnIndexOrThrow(Media.SIZE);
			int bucketDisplayNameIndex = cur.getColumnIndexOrThrow(Media.BUCKET_DISPLAY_NAME);
			int bucketIdIndex = cur.getColumnIndexOrThrow(Media.BUCKET_ID);
			int picasaIdIndex = cur.getColumnIndexOrThrow(Media.PICASA_ID);
			int dateModifiedIndex = cur.getColumnIndexOrThrow(Media.DATE_MODIFIED);
			// 获取图片总数

			do {
				String _id = cur.getString(photoIDIndex);
				String name = cur.getString(photoNameIndex);
				String path = cur.getString(photoPathIndex);
				String title = cur.getString(photoTitleIndex);
				String size = cur.getString(photoSizeIndex);
				String bucketName = cur.getString(bucketDisplayNameIndex);
				String bucketId = cur.getString(bucketIdIndex);
				String picasaId = cur.getString(picasaIdIndex);
				String dateModified = cur.getString(dateModifiedIndex);

//				Log.i(TAG, _id + ", bucketId: " + bucketId + ", picasaId: "
//						+ picasaId + " name:" + name + " path:" + path
//						+ " title: " + title + " size: " + size + " bucket: "
//						+ bucketName + "---");

				AlbumInfo bucket = bucketList.get(bucketId);
				if (bucket == null) {
					bucket = new AlbumInfo();
					bucket.id = bucketId;
					bucketList.put(bucketId, bucket);
					bucket.albumName = bucketName;
				}
				bucket.count++;
				LocalImageInfo imageItem = new LocalImageInfo();
				imageItem.imageId = _id;
				imageItem.imagePath = path;
//				imageItem.thumbnailPath = thumbnailList.get(_id);
				imageItem.dateModified = dateModified;
				bucket.imageList.add(imageItem);

			} while (cur.moveToNext());
		}
		cur.close();
		Iterator<Entry<String, AlbumInfo>> itr = bucketList.entrySet()
				.iterator();
		while (itr.hasNext()) {
			Entry<String, AlbumInfo> entry = (Entry<String, AlbumInfo>) itr.next();
			AlbumInfo bucket = entry.getValue();
			Collections.reverse(bucket.imageList);
		}
		hasBuildImagesBucketList = true;
		long endTime = System.currentTimeMillis();
//		Log.d(TAG, "use time: " + (endTime - startTime) + " ms");
	}

	/**
	 * 得到图片集
	 * 
	 * @param refresh
	 * @return
	 */
	private synchronized List<AlbumInfo> getImagesBucketList(boolean refresh) {
		if (refresh || (!refresh && !hasBuildImagesBucketList)) {
			bucketList.clear();
			buildImagesBucketList();
			buildMyRecvImageList();
		}
		AlbumInfo tmpAlbum = new AlbumInfo();
		tmpAlbum.albumName = context.getString(R.string.all_photo);
		tmpAlbum.id = "recent_list";
		List<AlbumInfo> tmpList = new ArrayList<AlbumInfo>();
		Iterator<Entry<String, AlbumInfo>> itr = bucketList.entrySet().iterator();
		while (itr.hasNext()) {
			Entry<String, AlbumInfo> entry = (Entry<String, AlbumInfo>) itr.next();
			tmpList.add(entry.getValue());
			tmpAlbum.imageList.addAll(entry.getValue().imageList);
		}
		AlbumInfo recvAlbum = new AlbumInfo();
		recvAlbum.albumName = context.getString(R.string.recv_photo);
		recvAlbum.id = "receive_image";
		recvAlbum.imageList = myRecvImage;
		recvAlbum.count = myRecvImage.size();
		if (recvAlbum.count != 0) {
			tmpAlbum.imageList.addAll(myRecvImage);
			tmpList.add(0, recvAlbum);
		}
		tmpAlbum.count = tmpAlbum.imageList.size();
		Collections.sort(tmpAlbum.imageList);
		tmpList.add(0, tmpAlbum);
		return tmpList;
	}
	public void getImagesBucketList(final boolean refresh, final OnGetAlbumListListener listener){
		new Thread(){
			@Override
			public void run() {
				final List<AlbumInfo> resultList = getImagesBucketList(refresh);
				MyApplication.getInstance().getMainHandler().post(new Runnable() {
					@Override
					public void run() {
						listener.OnFinished(resultList);
					}
				});
			}
		}.start();
	}
	private void buildMyRecvImageList(){
		myRecvImage.clear();
		File recvDir = new File(FileConstants.ROOT_IMAGE_PATH + File.separator + "recvFile");
		if (!recvDir.exists()) {
			return;
		}
		File[] files = recvDir.listFiles(new ImageFilter());
		for (int i = 0; i < files.length; i++) {
			LocalImageInfo info = new LocalImageInfo();
			info.dateModified = String.valueOf(files[i].lastModified());
			info.imagePath = files[i].getAbsolutePath();
			info.isSelected = false;
//			info.thumbnailPath = files[i].getAbsolutePath();
			info.imageId = "receive_image" + i;
			myRecvImage.add(info);
		}
		Collections.sort(myRecvImage);
	}
//	/**
//	 * 得到原始图像路径
//	 * 
//	 * @param image_id
//	 * @return
//	 */
//	String getOriginalImagePath(String image_id) {
//		String path = null;
//		String[] projection = { Media._ID, Media.DATA };
//		Cursor cursor = cr.query(Media.EXTERNAL_CONTENT_URI, projection, Media._ID + "=" + image_id, null, null);
//		if (cursor != null) {
//			cursor.moveToFirst();
//			path = cursor.getString(cursor.getColumnIndex(Media.DATA));
//		}
//		cursor.close();
//		return path;
//	}
	private class ImageFilter implements FileFilter{
		String[] formats = new String[] { "jpg", "png", "gif", "bmp", "icon", "tif", "jpeg", "psd" };
		Map<String, Boolean> map = new HashMap<String, Boolean>();
		public ImageFilter(){
			for (String format : formats) {
				map.put(format, true);
			}
		}
		@Override
		public boolean accept(File pathname) {
			String fileName = pathname.getName();
			String end = fileName.substring(fileName.lastIndexOf(".") + 1, fileName.length()).toLowerCase(Locale.CHINA);
			return map.get(end) == null ? false : true;
		}
		
	}
}
