package com.chengsi.weightcalc.utils;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.provider.MediaStore.Images;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;

import com.chengsi.weightcalc.MyApplication;
import com.chengsi.weightcalc.constants.FileConstants;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.lang.ref.ReferenceQueue;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileChannel.MapMode;
import java.util.Hashtable;
import java.util.Map;


public class BitmapUtils {

    public static final int IMG_SCALE_ORG = 0;
    public static final int IMG_SCALE_120 = 120;
    public static final int IMG_SCALE_160 = 160;
    public static final int IMG_SCALE_640 = 640;
    public static final int IMG_SCALE_960 = 960;
    public static final int RESULT_CANCELED = 0;
    public static final int RESULT_RE_SELECT_PHOTO = 100;
    public static final int RESULT_OK = -1;
    public static final int PREVIEW = 2011;
    public static final int ALBUM = 2012;
    public static final int CAMERA = 2013;
    public static final int HEADER = 2014;
    public static final int CHAT = 2015;
    public static final int BACKGROUND = 2016;
    public static final int BULLETIN = 2017;
    public static final int PREVIEW_BEFORE_PUBLISH = 2018;
    public static final int ZONE = 2019;
    /**
     * 图片路径
     */
    public static final String BITMATSRC = "BITMATSRC";
    public static final String OBTAINTYPE = "OBTAINTYPE"; //暂时数据只有两个，CHAT和PREVIEW, CHAT用于发送图片的确认页面，PREVIEW用于图片的预览页面
    //Drawable目录下面的图片
    public static final String BITMAP_DRAWABLE_SRC = "BITMAP_DRAWABLE_SRC";

    public static final String RATIO = "RATIO";


    public static final String TAG = "BitmapManager";
    private static Map<String, Object> monitorMap = new Hashtable<String, Object>();
    private static ReferenceQueue queue = new ReferenceQueue();

    public static Bitmap decodeFile(String paramString) {
        return decodeFile(paramString, null);
    }


    public static Bitmap decodeResource(Resources res, int id) {
        Bitmap bitmap = getFromCache(id + "");
        if (bitmap == null || bitmap.isRecycled()) {
            try {
                long time = System.currentTimeMillis();
                bitmap = BitmapFactory.decodeResource(res, id);
                Log.d("BitmapManager", Thread.currentThread().getName() + " decode image ID " + id + ",cost " + (System.currentTimeMillis() - time) + "ms");
                if (bitmap != null) ;
                MobinSoftReference localabt = new MobinSoftReference(bitmap, queue, id + "");
                String str = id + "";
                monitorMap.put(str, localabt);
            } catch (OutOfMemoryError localOutOfMemoryError1) {
                Log.w(TAG, "memory low!!! images:" + monitorMap);
                System.gc();
                Thread.yield();
                try {
                    long l1 = System.currentTimeMillis();
                    bitmap = BitmapFactory.decodeResource(res, id);
                    Log.d(TAG, "decode image " + id + ",cost " + (System.currentTimeMillis() - l1) + "ms");
                } catch (OutOfMemoryError localOutOfMemoryError2) {
                    localOutOfMemoryError2.printStackTrace();
                    Log.e(TAG, "Out of Memory on decode file " + id + "|" + showLog() + " images:" + monitorMap);
                }
            }
        }
        return bitmap;
    }


    public static Bitmap decodeFile(String path, BitmapFactory.Options paramOptions) {
        Bitmap bitmap = getFromCache(path);
        if (bitmap == null || bitmap.isRecycled()) {
            try {
                long time = System.currentTimeMillis();
                bitmap = BitmapFactory.decodeFile(path, paramOptions);
                Log.d("BitmapManager", Thread.currentThread().getName() + " decode image " + path + ",cost " + (System.currentTimeMillis() - time) + "ms");
                if (bitmap != null) {
                    MobinSoftReference localabt = new MobinSoftReference(bitmap, queue, path);
                    String str = path;
                    monitorMap.put(str, localabt);
                }

            } catch (OutOfMemoryError localOutOfMemoryError1) {
                Log.w(TAG, "memory low!!! images:" + monitorMap);
                System.gc();
                Thread.yield();
                try {
                    long l1 = System.currentTimeMillis();
                    bitmap = BitmapFactory.decodeFile(path, paramOptions);
                    Log.d(TAG, "decode image " + path + ",cost " + (System.currentTimeMillis() - l1) + "ms");
                } catch (OutOfMemoryError localOutOfMemoryError2) {
                    localOutOfMemoryError2.printStackTrace();
                    Log.e(TAG, "Out of Memory on decode file " + path + "|" + showLog() + " images:" + monitorMap);
                }
            }
        }


        return bitmap;

    }

    public static Bitmap decodeBitmapFromPath(String path, int maxW, int maxH, Config config) {
        Bitmap bitmap = getFromCache(path);
        if (bitmap == null) {
            BitmapFactory.Options options = null;
            try {
                options = getSizeOpt(new File(path), maxW, maxH);
            } catch (IOException e) {
                e.printStackTrace();
                options = new BitmapFactory.Options();
                options.inPurgeable = true;
                options.inInputShareable = true;
                options.inPreferredConfig = config;
                options.inSampleSize = 1;
                try {
                    BufferedInputStream inputStream = new BufferedInputStream(new FileInputStream(path));
                    inputStream.mark(Integer.MAX_VALUE);
                    options.inSampleSize = (int) Math.round(BitmapUtils.getOptRatio(inputStream, maxW, maxH));
                } catch (FileNotFoundException ex) {
                    ex.printStackTrace();
                    return null;
                }
            }
            try {
                bitmap = BitmapFactory.decodeFile(path, options);
            } catch (OutOfMemoryError error) {
                System.gc();
                bitmap = decodeBitmapChangeSampleSize(path, options);
            }
            bitmap = rotate(bitmap, getExifOrientation(path));
            if (bitmap != null) {
                MobinSoftReference localabt = new MobinSoftReference(bitmap, queue, path);
                String str = path;
                monitorMap.put(str, localabt);
            }
        }
        return bitmap;
    }

    public static Bitmap decodeBitmapChangeSampleSize(String path, BitmapFactory.Options paramOptions) {
        Bitmap bitmap = null;
        try {
            bitmap = BitmapFactory.decodeFile(path, paramOptions);
        } catch (OutOfMemoryError localOutOfMemoryError2) {
//        	  if (paramOptions.inSampleSize == 6) {
//				return null;
//        	  }
            paramOptions.inSampleSize++;
            bitmap = decodeBitmapChangeSampleSize(path, paramOptions);
        }
        return bitmap;
    }

    /**
     * @param src
     * @return
     * @Description: n my testing, GIF images in 4.4 have transparency values as either white (-1) or black (-16777216).
     * After you load a Bitmap, you can convert the white/black pixels back to transparent.
     * Of course this will only work if the rest of the image doesn't use the same color.
     * If it does then you will also convert parts of your image to transparent that were not transparent in the original image.
     * In my case this wasn't a problem.
     */
    public static Bitmap eraseBGForSDK19(Bitmap src) {
        if (Build.VERSION.SDK_INT != 19) {
            return src;
        }
        src = eraseBG(src, -1);         // use for white background
        src = eraseBG(src, -16777216);  // use for black background
        return src;
    }

    @TargetApi(12)
    private static Bitmap eraseBG(Bitmap src, int color) {
        int width = src.getWidth();
        int height = src.getHeight();
        Bitmap b = src.copy(Config.ARGB_8888, true);
        b.setHasAlpha(true);

        int[] pixels = new int[width * height];
        src.getPixels(pixels, 0, width, 0, 0, width, height);

        for (int i = 0; i < width * height; i++) {
            if (pixels[i] == color) {
                pixels[i] = 0;
            }
        }

        b.setPixels(pixels, 0, width, 0, 0, width, height);

        return b;
    }

//	  public static Bitmap decodeFile(String paramString, BitmapFactory.Options paramOptions, boolean paramBoolean)
//	  {
//	    Object localObject = getFromCache(paramString);
//	    if (localObject != null);
//	      return ;
//	      try
//	      {
//	        long l2 = System.currentTimeMillis();
//	        localObject = BitmapFactory.decodeFile(paramString, paramOptions);
//	        QLog.d("BitmapManager", Thread.currentThread().getName() + " decode image " + paramString + ",cost " + (System.currentTimeMillis() - l2) + "ms");
//	        if (paramBoolean)
//	        {
//	          Bitmap localBitmap = ImageUtil.grey((Bitmap)localObject);
//	          localObject = localBitmap;
//	        }
//	        if (localObject != null);
//	        abt localabt = new abt(paramString, localObject, queue);
//	        File localFile = new File(paramString);
//	        String str = paramString + "," + localFile.lastModified();
//	        monitorMap.put(str, localabt);
//	      }
//	      catch (OutOfMemoryError localOutOfMemoryError1)
//	      {
//	        while (true)
//	        {
//	          QLog.w("BitmapManager", "memory low!!! images:" + monitorMap);
//	          System.gc();
//	          Thread.yield();
//	          try
//	          {
//	            long l1 = System.currentTimeMillis();
//	            localObject = BitmapFactory.decodeFile(paramString, paramOptions);
//	            QLog.d("decode image " + paramString + ",cost " + (System.currentTimeMillis() - l1) + "ms");
//	          }
//	          catch (OutOfMemoryError localOutOfMemoryError2)
//	          {
//	            QLog.e("BitmapManager", "Out of Memory on decode file " + paramString + "|" + showLog() + " images:" + monitorMap);
//	          }
//	        }
//	      }
//	    }
//	  localObject
//	  }

    public static Bitmap getFromCache(String path) {
        while (true) {
            MobinSoftReference ref = (MobinSoftReference) queue.poll();
            if (ref == null)
                break;
            monitorMap.remove(ref.getKey());
        }
        Bitmap bitmap = null;
        String str = path;
        MobinSoftReference ref2 = (MobinSoftReference) monitorMap.get(str);
        if (ref2 != null)
            bitmap = (Bitmap) ref2.get();
        return bitmap;
    }

    public static String showImagesLog() {
        MobinSoftReference localabt = (MobinSoftReference) queue.poll();
        if (localabt != null)
            monitorMap.remove(localabt.getKey());
        return "images:" + monitorMap;
    }

    public static String showLog() {
        long l2 = (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / 1024L;
        Object[] arrayOfObject = new Object[1];
        arrayOfObject[0] = Long.valueOf(l2);
        return l2 + "";
    }

    /**
     * 读取图片防止OOM
     *
     * @param path
     * @param maxH
     * @param maxW
     * @param context
     * @return
     */
    public static Bitmap loadBitmap(Uri path, int maxH, int maxW, Context context, Config config) {
        String srcPath = getRealPathFromURI(context, path);

        if (srcPath == null) {
            srcPath = path.getPath().toString();
        }
        return decodeFromPath(srcPath, maxH, maxW, config, true);
    }

    public static Bitmap loadBitmapWittOutHandle(Uri path, int maxH, int maxW, Context context, Config config) {

        String srcPath = getRealPathFromURI(context, path);
        if (srcPath == null) {
            srcPath = path.getPath().toString();
        }
        return decodeFromPath(srcPath, maxH, maxW, config, false);
    }

    private static Bitmap decodeFromPath(String path, int maxH, int maxW, Config config, boolean flag) {
        Bitmap bitmap = null;
        try {
            bitmap = captureImage(path, maxH, maxW, config, flag);

        } catch (OutOfMemoryError localOutOfMemoryError1) {
            Log.w(TAG, "memory low!!! images:" + monitorMap);
            System.gc();
            Thread.yield();
            try {
                long l1 = System.currentTimeMillis();
                bitmap = captureImage(path, maxH, maxW, config, true);
                Log.d(TAG, "decode image " + path + ",cost " + (System.currentTimeMillis() - l1) + "ms");
            } catch (OutOfMemoryError localOutOfMemoryError2) {
                localOutOfMemoryError2.printStackTrace();
                Log.e(TAG, "Out of Memory on decode file " + path + "|" + showLog() + " images:" + monitorMap);
            }
        }
        return bitmap;
    }

    public static Bitmap loadBtimap(String path, int maxH, int maxW, Activity context, Config config) {
        Bitmap bitmap = getFromCache(path);

        if (bitmap == null || bitmap.isRecycled()) {
            System.out.println("not getFromCache");
            if (!TextUtils.isEmpty(path)) {
                bitmap = decodeFromPath(path, maxH, maxW, config, true);
                if (bitmap != null) {
                    MobinSoftReference localabt = new MobinSoftReference(bitmap, queue, path + "");
                    String str = path + "";
                    monitorMap.put(str, localabt);
                }
            }
        } else {
            System.out.println("getFromCache getFromCache");
        }

        // switch (type) {
        // case BitmapUtils.ALBUM:
        //
        // return captureFromAlbum();
        // case BitmapUtils.CAMERA:
        //
        // return captureFromCamera();
        // }
        return bitmap;
    }

    private static Bitmap captureImage(String path, int maxH, int maxW, Config config, boolean flag) {
        if (path == null)
            return null;
        InputStream is = null;
        Bitmap bitmap = null;
        int degree = 0;
        try {
            is = new BufferedInputStream(new FileInputStream(path));//谁遇到过这问题 从HTC 那个新手机M8 里读取相册的图片 路径转换/document/image:19511
            Bitmap tempbitmap = getSampleSizeBitmap(is, path, maxH, maxW, config);
            if (flag) {
                degree = BitmapUtils.getExifOrientation(path);
                bitmap = BitmapUtils.rotate(tempbitmap, degree);
            } else {
                bitmap = tempbitmap;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return bitmap;
    }

    /**
     * 获取压缩后的图片防止oom
     *
     * @param inputStream
     * @return
     */
    public static Bitmap getSampleSizeBitmap(InputStream inputStream, String path, int maxH, int maxW, Config config) {

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPurgeable = true;
        options.inInputShareable = true;

        int widthPixels = maxW;
        int heightPixels = maxH;
        options.inPreferredConfig = config;
        Bitmap bitmap = null;
        inputStream.mark(Integer.MAX_VALUE);
        options.inSampleSize = (int) Math.round(BitmapUtils.getOptRatio(
                inputStream, widthPixels, heightPixels));
        // 必须重置
        try {
            inputStream.reset();
            bitmap = BitmapFactory.decodeStream(inputStream, null, options);
        } catch (IOException e) {
            e.printStackTrace();
            bitmap = BitmapFactory.decodeFile(path, options);
        }
//		inputStream.mark(Integer.MAX_VALUE);
//		long pixes = 0;
//		try {
//			pixes= BitmapUtils.getImagePixes(inputStream);
//		} catch (OutOfMemoryError e) {
//			pixes =  -1;
//		}
//		try {
//			inputStream.reset();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//		
//		if ((pixes < 0L) || (pixes > widthPixels * heightPixels)) {
//			inputStream.mark(Integer.MAX_VALUE);
//			options.inSampleSize = (int) Math.round(BitmapUtils.getOptRatio(
//					inputStream, widthPixels, heightPixels));
//			// 必须重置
//			try {
//				inputStream.reset();
//			} catch (IOException e) {
//				e.printStackTrace();
//				bitmap = BitmapFactory.decodeFile(path, options);
//			}
//			bitmap = BitmapFactory.decodeStream(inputStream, null, options);
//		} else {
//			options.inSampleSize = 2;
//			bitmap = BitmapFactory.decodeStream(inputStream);
//		}

        return bitmap;
    }


    /**
     * 获取缩放标准
     *
     * @param res
     * @return
     */
    public static int getScaleRate(Resources res) {
        DisplayMetrics localDisplayMetrics = res.getDisplayMetrics();
        int widthPixels = localDisplayMetrics.widthPixels;
        int heightPixels = localDisplayMetrics.heightPixels;
        if (widthPixels > heightPixels)
            heightPixels = widthPixels;
        if (heightPixels >= 800) {
            return IMG_SCALE_960;
        } else {
            return IMG_SCALE_640;
        }

    }

    /**
     * 通过Uri获取文件在本地存储的真实路径
     *
     * @param contentUri
     * @return
     */
    public static String getRealPathFromURI(Context context, Uri contentUri) {
        String scheme = contentUri.getScheme();
        if ("file".equals(scheme)) {
            return contentUri.getPath().toString();
        }
        if (!"content".equals(scheme)) {
            if (scheme == null && new File(contentUri.getPath()).exists()) {
                return contentUri.getPath();
            }
            return null;
        }

        String[] proj = {Images.Media.DATA};
        Cursor cursor = MyApplication.getInstance().getContentResolver().query(contentUri, proj, // Which columns to return
                null, // WHERE clause; which rows to return (all rows)
                null, // WHERE clause selection arguments (none)
                null); // Order-by clause (ascending by name)
        int column_index = cursor.getColumnIndexOrThrow(Images.Media.DATA);
        cursor.moveToFirst();
        String result = cursor.getString(column_index);

        //4.4下会去拿最近照片  uri取得方式
        if (result == null) {
            result = getPathByKITKAT(context, contentUri);
        }

        return result;
    }


    /**
     * Get a file path from a Uri. This will get the the path for Storage Access
     * Framework Documents, as well as the _data field for the MediaStore and
     * other file-based ContentProviders.
     *
     * @param context The context.
     * @param uri     The Uri to query.
     * @author paulburke
     */
    @TargetApi(19)
    @SuppressLint("NewApi")
    public static String getPathByKITKAT(final Context context, final Uri uri) {

        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }

                // TODO handle non-primary volumes
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{
                        split[1]
                };

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {
            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

    /**
     * Get the value of the data column for this Uri. This is useful for
     * MediaStore Uris, and other file-based ContentProviders.
     *
     * @param context       The context.
     * @param uri           The Uri to query.
     * @param selection     (Optional) Filter used in the query.
     * @param selectionArgs (Optional) Selection arguments used in the query.
     * @return The value of the _data column, which is typically a file path.
     */
    public static String getDataColumn(Context context, Uri uri, String selection,
                                       String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int column_index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(column_index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }


    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }


    /**
     * 插入图片到相册
     *
     * @param context
     * @param filename 文件路径
     * @return
     */
    public static Uri exportToGallery(Context context, String filename) {
        // Save the name and description of a video in a ContentValues map.
        final ContentValues values = new ContentValues(2);
        values.put(Images.Media.MIME_TYPE, "image/jpeg");
        values.put(Images.Media.MIME_TYPE, "image/png");
        values.put(Images.Media.DATA, filename);
        values.put(Images.Media.DATE_TAKEN, System.currentTimeMillis());
        // Add a new record (identified by uri)
        final Uri uri = context.getContentResolver().insert(
                Images.Media.EXTERNAL_CONTENT_URI, values);
        context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,
                Uri.parse("file://" + filename)));
        return uri;
    }

    /**
     * 获取一张图片所包含的像素数
     *
     * @param
     * @return
     */
    public static long getImagePixes(InputStream is) {
        if (is == null) {
            return -1;
        }
        try {
            int b = is.read();
            if (b == 0xff) {
                b = is.read();
                if (b == 0xd8) {
                    return getJpgPixes(is);
                }
            } else if (b == 0x47) {
                b = is.read();
                if (b == 0x49) {
                    b = is.read();
                    if (b == 0x46) {
                        is.skip(3);//跳过3字节
                        int w = (is.read() | (is.read() << 8));
                        int h = (is.read() | (is.read() << 8));
                        return w * h;
                    }
                }
            } else if (b == 0x42) {
                b = is.read();
                if (b == 0x4d) {
                    is.skip(16);
                    long w = (is.read() | (is.read() << 8) | (is.read() << 16) | (is.read() << 24));
                    long h = (is.read() | (is.read() << 8) | (is.read() << 16) | (is.read() << 24));
                    return w * h;
                }

            } else if (b == 0x89) {
                b = is.read();
                if (b == 0x50) {
                    b = is.read();
                    if (b == 0x4E) {
                        is.skip(13);//跳过3字节
                        long w = ((is.read() << 24) | (is.read() << 16) | (is.read() << 8) | is.read());
                        long h = ((is.read() << 24) | (is.read() << 16) | (is.read() << 8) | is.read());
                        return w * h;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }

    /**
     * 获取 Jpg图片的像素数
     *
     * @param is
     * @return
     */
    private static long getJpgPixes(InputStream is) {
        int w = 0;
        int h = 0;
        int b = 0;
        try {
            while ((b = is.read()) != -1) {
                if (b == 0xff) {
                    b = is.read();
                    if (b >= 0xc0 && b <= 0xc3) {
                        is.skip(3);//跳过3字节
                        h = (is.read() << 8) | (is.read());
                        w = (is.read() << 8) | (is.read());
                        return w * h;
                    } else if (b != 0 && b != 0xd9 && b != 0xd8) {
                        int length = (is.read() << 8) | (is.read());
                        is.skip(length - 2);
                    } else if (b == -1) {
                        break;
                    }
                }
            }
            return -1;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }

    /**
     * 将给定图片维持宽高比缩放后，截取正中间的正方形部分。
     *
     * @param bitmap     原图
     * @param edgeLength 希望得到的正方形部分的边长
     * @return 缩放截取正中部分后的位图。
     */
    public static Bitmap centerSquareScaleBitmap(Bitmap bitmap, int edgeLength) {
        if (null == bitmap || edgeLength <= 0) {
            return null;
        }

        Bitmap result = bitmap;
        int widthOrg = bitmap.getWidth();
        int heightOrg = bitmap.getHeight();

        if (widthOrg > edgeLength && heightOrg > edgeLength) {
            //压缩到一个最小长度是edgeLength的bitmap
            int longerEdge = (int) (edgeLength * Math.max(widthOrg, heightOrg) / Math.min(widthOrg, heightOrg));
            int scaledWidth = widthOrg > heightOrg ? longerEdge : edgeLength;
            int scaledHeight = widthOrg > heightOrg ? edgeLength : longerEdge;
            Bitmap scaledBitmap;

            try {
                scaledBitmap = Bitmap.createScaledBitmap(bitmap, scaledWidth, scaledHeight, true);
            } catch (Exception e) {
                return null;
            }

            //从图中截取正中间的正方形部分。
            int xTopLeft = (scaledWidth - edgeLength) / 2;
            int yTopLeft = (scaledHeight - edgeLength) / 2;

            try {
                result = Bitmap.createBitmap(scaledBitmap, xTopLeft, yTopLeft, edgeLength, edgeLength);
                scaledBitmap.recycle();
            } catch (Exception e) {
                return null;
            }
        }

        return result;
    }

    private static String CompressJpgFile(InputStream is, BitmapFactory.Options newOpts, String filePath, int degree, int rate) {
        Bitmap destBm = BitmapFactory.decodeStream(is, null, newOpts);
        destBm = rotate(destBm, degree);
        return CompressJpgFile(destBm, newOpts, filePath, rate);
    }

    private static String CompressJpgFile(Bitmap destBm, BitmapFactory.Options newOpts, String filePath, int rate) {
        //newOpts.inPreferredConfig = Bitmap.Config.ARGB_8888;
        if (destBm == null) {
            return null;
        } else {
            File destFile = createNewFile(filePath);

            // 创建文件输出流
            OutputStream os = null;
            try {
                os = new FileOutputStream(destFile);
                // 存储
                //int rate = 80;
                destBm.compress(CompressFormat.JPEG, rate, os);

            } catch (Exception e) {
                filePath = null;
            } finally {
                if (os != null) {
                    try {
                        os.close();
                    } catch (IOException e) {
                    }
                }
            }
            return filePath;
        }
    }

    private static String CompressPngFile(InputStream is, BitmapFactory.Options newOpts, String filePath, int degree, int rate) {
        Bitmap destBm = BitmapFactory.decodeStream(is, null, newOpts);
        destBm = rotate(destBm, degree);
        return CompressPngFile(destBm, newOpts, filePath, rate);
    }

    private static String CompressPngFile(Bitmap destBm, BitmapFactory.Options newOpts, String filePath, int rate) {
        newOpts.inPreferredConfig = Config.ARGB_8888;
        if (destBm == null) {
            return null;
        } else {
            File destFile = createNewFile(filePath);

            // 创建文件输出流
            OutputStream os = null;
            try {
                os = new FileOutputStream(destFile);
                // 存储
                //int rate = 100;
                destBm.compress(CompressFormat.PNG, rate, os);

            } catch (Exception e) {
                filePath = null;
            } finally {
                if (os != null) {
                    try {
                        os.close();
                    } catch (IOException e) {
                    }
                }
            }
            return filePath;
        }
    }

    private static long SIZE_200K = 200 * 1024;

    public static String compressImage4Zone(Context context, String srcPath, String destPath) {
        //InputStream is = null;
        File f = new File(srcPath);
        //is = new FileInputStream(f);
        String type = destPath.substring(destPath.lastIndexOf(".") + 1).toLowerCase();
        int degree = BitmapUtils.getExifOrientation(srcPath);
        if ("png".equals(type)) {
            return CompressPngFile4Zone(srcPath, destPath, degree);
        } else {
            return CompressJpgFile4Zone(srcPath, destPath, degree);
        }

    }

    private static int getSampleSizeByFileSize(long srcFileSize, long destFileSize) {
        int scale = (int) (srcFileSize / destFileSize);
        if (scale < 1) {
            return 1;
        }
        double d = Math.sqrt(scale);
        return (int) Math.ceil(d);
    }

    private static String CompressPngFile4Zone(String srcPath, String filePath, int degree) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = false;
        options.inSampleSize = getSampleSizeByFileSize(new File(srcPath).length(), SIZE_200K);
        Bitmap destBm = BitmapFactory.decodeFile(srcPath, options);
        destBm = rotate(destBm, degree);
        return CompressPngFile(destBm, options, filePath, 75);
    }

    private static String CompressJpgFile4Zone(String srcPath, String filePath, int degree) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = false;
        options.inSampleSize = getSampleSizeByFileSize(new File(srcPath).length(), SIZE_200K);
        Bitmap destBm = BitmapFactory.decodeFile(srcPath, options);
        destBm = rotate(destBm, degree);
        return CompressJpgFile(destBm, options, filePath, 75);
    }


    public static String compressImage(Context context, String srcPath, String destPath, int maxW, int maxH, int rate) {
        InputStream is = null;
        try {
            File f = new File(srcPath);
            BitmapFactory.Options newOpts = getSizeOpt(f, maxW, maxH);
            is = new FileInputStream(f);
            String type = destPath.substring(destPath.lastIndexOf(".") + 1).toLowerCase();
            int degree = BitmapUtils.getExifOrientation(srcPath);
            if ("png".equals(type)) {
                return CompressPngFile(is, newOpts, destPath, degree, rate);
            } else {
                return CompressJpgFile(is, newOpts, destPath, degree, rate);
            }

        } catch (Exception e) {
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                }
            }
        }
        return null;
    }

    public static String compressImage(Context context, String srcPath, String destPath, int maxW, int maxH) {
        return compressImage(context, srcPath, destPath, maxW, maxH, 100);
    }


    public static String compressImage(Context context, Bitmap bitmap, String destPath, int rate) {
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        String type = destPath.substring(destPath.lastIndexOf(".") + 1).toLowerCase();
        if ("png".equals(type)) {
            return CompressPngFile(bitmap, newOpts, destPath, rate);
        } else {
            return CompressJpgFile(bitmap, newOpts, destPath, rate);
        }
    }

    public static String compressImage(Context context, Bitmap bitmap, String destPath) {
        return compressImage(context, bitmap, destPath, 100);
    }


    /**
     * 先压缩图片大小
     *
     * @return
     * @throws IOException
     */
    public static BitmapFactory.Options getSizeOpt(File file, int maxWidth, int maxHeight) throws IOException {
        // 对图片进行压缩，是在读取的过程中进行压缩，而不是把图片读进了内存再进行压缩
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        InputStream is = new FileInputStream(file);
        double ratio = getOptRatio(is, maxWidth, maxHeight);
        is.close();
        newOpts.inSampleSize = (int) ratio;
        newOpts.inJustDecodeBounds = true;
        is = new FileInputStream(file);
        BitmapFactory.decodeStream(is, null, newOpts);
        is.close();
        int loopcnt = 0;
        while (newOpts.outWidth > maxWidth) {
            newOpts.inSampleSize += 1;
            is = new FileInputStream(file);
            BitmapFactory.decodeStream(is, null, newOpts);
            is.close();
            if (loopcnt > 3) break;
            loopcnt++;
        }
        newOpts.inJustDecodeBounds = false;
        return newOpts;
    }

    /**
     * 计算起始压缩比例
     * 先根据实际图片大小估算出最接近目标大小的压缩比例
     * 减少循环压缩的次数
     *
     * @param is
     * @return
     */
    public static double getOptRatio(InputStream is, int maxWidth, int maxHeight) {
        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(is, null, opts);
        int srcWidth = opts.outWidth;
        int srcHeight = opts.outHeight;
        int destWidth = 0;
        int destHeight = 0;
        // 缩放的比例
        double ratio = 1.0;
        double ratio_w = 0.0;
        double ratio_h = 0.0;
        // 按比例计算缩放后的图片大小，maxLength是长或宽允许的最大长度
        if (srcWidth <= maxWidth && srcHeight <= maxHeight) {
            return ratio;   //小于屏幕尺寸时，不压缩
        }
        if (srcWidth > srcHeight) {
            ratio_w = srcWidth / maxWidth;
            ratio_h = srcHeight / maxHeight;
        } else {
            ratio_w = srcHeight / maxWidth;
            ratio_h = srcWidth / maxHeight;
        }
        if (ratio_w > ratio_h) {
            ratio = ratio_w;
        } else {
            ratio = ratio_h;
        }
        return ratio;
    }


    public static enum ScalingLogic {
        CROP, FIT, SCALE_CROP
    }

    public static Rect calculateSrcRect(int srcWidth, int srcHeight, int dstWidth, int dstHeight,
                                        ScalingLogic scalingLogic) {
        if (scalingLogic == ScalingLogic.CROP) {
            final float srcAspect = (float) srcWidth / (float) srcHeight;
            final float dstAspect = (float) dstWidth / (float) dstHeight;
            if (srcAspect > dstAspect) {
                final int srcRectWidth = (int) (srcHeight * dstAspect);
                final int srcRectLeft = (srcWidth - srcRectWidth) / 2;
                return new Rect(srcRectLeft, 0, srcRectLeft + srcRectWidth, srcHeight);
            } else {
                final int srcRectHeight = (int) (srcWidth / dstAspect);
                final int scrRectTop = (int) (srcHeight - srcRectHeight) / 2;
                return new Rect(0, scrRectTop, srcWidth, scrRectTop + srcRectHeight);
            }
        } else {
            return new Rect(0, 0, srcWidth, srcHeight);
        }
    }

    public static Rect calculateDstRect(int srcWidth, int srcHeight, int dstWidth, int dstHeight,
                                        ScalingLogic scalingLogic) {
        if (scalingLogic == ScalingLogic.FIT) {
            final float srcAspect = (float) srcWidth / (float) srcHeight;
            final float dstAspect = (float) dstWidth / (float) dstHeight;
            if (srcAspect > dstAspect) {
                return new Rect(0, 0, dstWidth, (int) (dstWidth / srcAspect));
            } else {
                return new Rect(0, 0, (int) (dstHeight * srcAspect), dstHeight);
            }
        } else {
            return new Rect(0, 0, dstWidth, dstHeight);
        }
    }

    public static Bitmap createScaledBitmap(Bitmap unscaledBitmap, int dstWidth, int dstHeight,
                                            ScalingLogic scalingLogic) {
        if (unscaledBitmap == null)
            return null;

        Rect srcRect = calculateSrcRect(unscaledBitmap.getWidth(), unscaledBitmap.getHeight(),
                dstWidth, dstHeight, scalingLogic);
        Rect dstRect = calculateDstRect(unscaledBitmap.getWidth(), unscaledBitmap.getHeight(),
                dstWidth, dstHeight, scalingLogic);
        Bitmap scaledBitmap = Bitmap.createBitmap(dstRect.width(), dstRect.height(),
                Config.ARGB_8888);
        Canvas canvas = new Canvas(scaledBitmap);
        canvas.drawBitmap(unscaledBitmap, srcRect, dstRect, new Paint(Paint.FILTER_BITMAP_FLAG | Paint.ANTI_ALIAS_FLAG));
        return scaledBitmap;
    }

    private static String getFileRoundSize(long paramLong) {
        return String.valueOf(Math.round(10.0F * ((float) paramLong / 1024.0F)) / 10.0F);
    }

    public static Bitmap createScaledBitmap(Bitmap unscaledBitmap, float zoomFactor,
                                            ScalingLogic scalingLogic) {
        if (unscaledBitmap == null)
            return null;
        int dstWidth = (int) (zoomFactor * unscaledBitmap.getWidth());
        int dstHeight = (int) (zoomFactor * unscaledBitmap.getHeight());

        return createScaledBitmap(unscaledBitmap, dstWidth, dstHeight, scalingLogic);
    }

    public static File createNewFile(String filePath) {
        if (filePath == null)
            return null;
        File newFile = new File(filePath);
        try {
            if (!newFile.exists()) {
                int slash = filePath.lastIndexOf('/');
                if (slash > 0 && slash < filePath.length() - 1) {
                    String dirPath = filePath.substring(0, slash);
                    File destDir = new File(dirPath);
                    if (!destDir.exists()) {
                        destDir.mkdirs();
                    }
                }
            } else {
                newFile.delete();
            }
            newFile.createNewFile();
        } catch (IOException e) {
            return null;
        }
        return newFile;
    }

    public static Bitmap getMutableBitmap(Bitmap bitmap) {
        if (bitmap == null || bitmap.isMutable()) {
            return bitmap;
        }

        try {
            File file = new File("/mutable.txt");
            file.getParentFile().mkdirs();

            RandomAccessFile randomAccessFile = new RandomAccessFile(file, "rw");

            int width = bitmap.getWidth();
            int height = bitmap.getHeight();

            FileChannel channel = randomAccessFile.getChannel();
            MappedByteBuffer map = channel.map(MapMode.READ_WRITE, 0, width * height * 4);
            bitmap.copyPixelsToBuffer(map);
            bitmap.recycle();

            bitmap = Bitmap.createBitmap(width, height, Config.ARGB_8888);
            map.position(0);
            bitmap.copyPixelsFromBuffer(map);
            channel.close();
            randomAccessFile.close();

        } catch (Exception e) {

            return bitmap.copy(Config.ARGB_8888, true);
        }

        return bitmap;
    }
//// 2. inPurgeable 设定为 true，可以让java系统, 在内存不足时先行回收部分的内存  
//         options.inPurgeable = true;  
    // 与inPurgeable 一起使用
//         options.inInputShareable = true;

    /**
     * 获取图片文件头信息
     *
     * @param in
     * @return
     */
    public static BitmapFactory.Options getImageOptions(InputStream in) {
        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(in, null, opts);
        return opts;
    }

    /**
     * float angle 旋转角度 如果为零 顺时针转90°
     *
     * @param bitmap
     * @param angle
     * @return
     */
    public static Bitmap rotateAndFrame(Bitmap bitmap, float angle) {
        final double radAngle = Math.toRadians(angle);
        final int bitmapWidth = bitmap.getWidth();
        final int bitmapHeight = bitmap.getHeight();
        final double cosAngle = Math.abs(Math.cos(radAngle));
        final double sinAngle = Math.abs(Math.sin(radAngle));
        final int width = (int) (bitmapHeight * sinAngle + bitmapWidth * cosAngle);
        final int height = (int) (bitmapWidth * sinAngle + bitmapHeight * cosAngle);
        final float x = (width - bitmapWidth) / 2.0f;
        final float y = (height - bitmapHeight) / 2.0f;
        final Bitmap decored = Bitmap.createBitmap(width, height, Config.ARGB_8888);
        final Canvas canvas = new Canvas(decored);
        canvas.rotate(angle, width / 2.0f, height / 2.0f);
        canvas.drawBitmap(bitmap, x, y, null);

        return decored;
    }


    public static final Bitmap grey(Bitmap bitmap) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();

        Bitmap faceIconGreyBitmap = Bitmap
                .createBitmap(width, height, Config.ARGB_8888);

        Canvas canvas = new Canvas(faceIconGreyBitmap);
        Paint paint = new Paint();
        ColorMatrix colorMatrix = new ColorMatrix();
        colorMatrix.setSaturation(0);
        ColorMatrixColorFilter colorMatrixFilter = new ColorMatrixColorFilter(
                colorMatrix);
        paint.setColorFilter(colorMatrixFilter);
        canvas.drawBitmap(bitmap, 0, 0, paint);
        return faceIconGreyBitmap;
    }


    // Rotates the bitmap by the specified degree.
    // If a new bitmap is created, the original bitmap is recycled.
    public static Bitmap rotate(Bitmap b, int degrees) {
        return rotateAndMirror(b, degrees, false);
    }

    // Rotates and/or mirrors the bitmap. If a new bitmap is created, the
    // original bitmap is recycled.
    public static Bitmap rotateAndMirror(Bitmap b, int degrees, boolean mirror) {
        if (degrees != 0 && b != null) {
            Matrix m = new Matrix();
            m.setRotate(degrees,
                    (float) b.getWidth() / 2, (float) b.getHeight() / 2);
            if (mirror) {
                m.postScale(-1, 1);
                degrees = (degrees + 360) % 360;
                if (degrees == 0 || degrees == 180) {
                    m.postTranslate((float) b.getWidth(), 0);
                } else if (degrees == 90 || degrees == 270) {
                    m.postTranslate((float) b.getHeight(), 0);
                } else {
                    throw new IllegalArgumentException("Invalid degrees=" + degrees);
                }
            }

            try {
                Bitmap b2 = Bitmap.createBitmap(
                        b, 0, 0, b.getWidth(), b.getHeight(), m, true);
                if (b != b2) {
                    b.recycle();
                    b = b2;
                }
            } catch (OutOfMemoryError ex) {
                // We have no memory to rotate. Return the original bitmap.
            }
        }
        return b;
    }

    public static int getExifOrientation(String filepath) {
        int degree = 0;
        ExifInterface exif = null;
        try {
            exif = new ExifInterface(filepath);
        } catch (IOException ex) {
            Log.e("mq", "cannot read exif", ex);
        }
        if (exif != null) {
            int orientation = exif.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION, -1);
            if (orientation != -1) {
                // We only recognize a subset of orientation tag values.
                switch (orientation) {
                    case ExifInterface.ORIENTATION_ROTATE_90:
                        degree = 90;
                        break;
                    case ExifInterface.ORIENTATION_ROTATE_180:
                        degree = 180;
                        break;
                    case ExifInterface.ORIENTATION_ROTATE_270:
                        degree = 270;
                        break;
                }

            }
        }
        return degree;
    }

    public static String restoreOrientation(String oldPath, String newPath) {
        try {
            ExifInterface oldExif = new ExifInterface(oldPath);
            ExifInterface newExif = new ExifInterface(newPath);
            int orientation = oldExif.getAttributeInt(ExifInterface.TAG_ORIENTATION, -1);
            newExif.setAttribute(ExifInterface.TAG_ORIENTATION, String.valueOf(orientation));
            newExif.saveAttributes();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return newPath;
    }


    /**
     * 压缩图片从相册返回
     * */
    public static File compressFile(String filepath, int rotato) {

        File PHOTO_DIR = new File(FileConstants.ROOT_IMAGE_PATH + File.separator + "myPhoto");

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

            BitmapFactory.Options options = new BitmapFactory.Options();
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
}
