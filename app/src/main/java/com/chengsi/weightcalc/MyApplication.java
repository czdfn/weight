package com.chengsi.weightcalc;

import android.app.AlarmManager;
import android.app.Application;
import android.app.KeyguardManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.hardware.SensorManager;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.PowerManager;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;

import com.chengsi.weightcalc.constants.IntentConstants;
import com.chengsi.weightcalc.exception.CrashHandler;
import com.chengsi.weightcalc.manager.AlbumManager;
import com.chengsi.weightcalc.receiver.LogReceiver;
import com.chengsi.weightcalc.utils.LogUtil;
import com.chengsi.weightcalc.utils.imageloader.ImageLoaderUtils;

import java.util.HashMap;
import java.util.Map;

import cn.jpush.android.api.JPushInterface;

/**
 * Created by yingjianxu on 15/6/17.
 */
public class MyApplication extends Application{
    private static final String TAG = MyApplication.class.getName();

    public static final String SMS_SDK_APPKEY = "b592827123e0";
    public static final String SMS_SDK_APPSECRET = "ac61016f678b4dcee7ef3ffc1ebf1a91";

    private static MyApplication mInstance;

    private static int sSdkVersion;
    private static int sVersionCode;
    private static PackageManager sPackageManager;
    private static String sPackageName;
    private static String sDeviceIMEI;

    private static SensorManager sSensorManager;
    private static KeyguardManager sKeyguardManager;
    private static ConnectivityManager sConnectivityManager;
    private static PowerManager sPowerManager;
    private static PowerManager.WakeLock sPowerManagerLock;
    private static AlarmManager mAlarmManager;

    private LogReceiver mLogReceiver;
    public AlbumManager albumManager;

//    public UserManager userManager;

    private Handler workHandler;
    private Handler mainHandler;
    public static Map<String, String> rubbishWords = new HashMap<>();
    private static String registerId;

    public MyApplication(){
        mInstance = this;
    }

    public static MyApplication getInstance(){
        return mInstance;
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        System.gc();
    }

    /**
     * Retrieve application's context
     * @return Android context
     */
    public static Context getContext() {
        return getInstance();
    }

    public String getRegisterId(){
        registerId = JPushInterface.getRegistrationID(getApplicationContext());
        return registerId;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        JPushInterface.setDebugMode(true);
        JPushInterface.init(this);
        String str = getString(R.string.minganci);
        String[] arr = str.split(",");
        for (String s : arr){
            String[] a = s.split("=");
            if (a.length == 2){
                rubbishWords.put(a[0], a[1]);
            }
        }
        /**
         * this function will initialize the HuanXin SDK
         *
         * @return boolean true if caller can continue to call HuanXin related APIs after calling onInit, otherwise false.
         *
         * 环信初始化SDK帮助函数
         * 返回true如果正确初始化，否则false，如果返回为false，请在后续的调用中不要调用任何和环信相关的代码
         *
         * for example:
         * 例子：
         *
         * public class DemoHXSDKHelper extends HXSDKHelper
         *
         * HXHelper = new DemoHXSDKHelper();
         * if(HXHelper.onInit(context)){
         *     // do HuanXin related work
         * }
         */

        ImageLoaderUtils.initImageLoader(this);
        albumManager = AlbumManager.getInstance(this);

        HandlerThread workerThread = new HandlerThread("global_worker_thread");
        workerThread.start();
        workHandler = new Handler(workerThread.getLooper());
        mainHandler = new Handler(Looper.getMainLooper());

//        userManager = new UserManager(this);
        sPackageName = getPackageName();
        sPackageManager = getPackageManager();
        LogUtil.plant(new LogUtil.DebugTree());
        registerLogReceiver();

        CrashHandler.getInstance().init(this);
    }

    private void registerLogReceiver(){
        if(mLogReceiver == null){
            mLogReceiver = new LogReceiver();
            IntentFilter filter = new IntentFilter();
            filter.addAction(IntentConstants.ACTION_LOG_DELETE);
            registerReceiver(mLogReceiver, filter);
            PendingIntent pi = PendingIntent.getBroadcast(getContext(), 0, new Intent(IntentConstants.ACTION_LOG_DELETE), PendingIntent.FLAG_UPDATE_CURRENT);
            getAlarmManager().setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), 3 * 60 * 60 * 1000, pi);
        }
    }

    /**
     * Gets Android SDK version
     * @return Android SDK version used to build the project
     */
    public static int getSDKVersion(){
        if(sSdkVersion == 0){
            sSdkVersion = Integer.parseInt(Build.VERSION.SDK);
        }
        return sSdkVersion;
    }

    public static int getVersionCode(){
        if(sVersionCode == 0){
            try {
                sVersionCode = sPackageManager.getPackageInfo(sPackageName, 0).versionCode;
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
        }
        return sVersionCode;
    }

    public static String getVersionName(){
        if(sPackageManager != null){
            try {
                return sPackageManager.getPackageInfo(sPackageName, 0).versionName;
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
        }
        return "0.0";
    }

    public static String getDeviceIMEI(){
        if(TextUtils.isEmpty(sDeviceIMEI)){
            final TelephonyManager telephonyMgr = (TelephonyManager) getContext().getSystemService(Context.TELEPHONY_SERVICE);
            sDeviceIMEI = telephonyMgr.getDeviceId();
        }
        return sDeviceIMEI;
    }

    public static SensorManager getSensorManager(){
        if(sSensorManager == null){
            sSensorManager = (SensorManager)getContext().getSystemService(Context.SENSOR_SERVICE);
        }
        return sSensorManager;
    }

    public static KeyguardManager getKeyguardManager(){
        if(sKeyguardManager == null){
            sKeyguardManager = (KeyguardManager)getContext().getSystemService(Context.KEYGUARD_SERVICE);
        }
        return sKeyguardManager;
    }

    public static ConnectivityManager getConnectivityManager(){
        if(sConnectivityManager == null){
            sConnectivityManager = (ConnectivityManager) getContext().getSystemService(CONNECTIVITY_SERVICE);
        }
        return sConnectivityManager;
    }

    public static PowerManager getPowerManager(){
        if(sPowerManager == null){
            sPowerManager = (PowerManager) getContext().getSystemService(POWER_SERVICE);
        }
        return sPowerManager;
    }

    public static AlarmManager getAlarmManager(){
        if(mAlarmManager == null){
            mAlarmManager = (AlarmManager) getContext().getSystemService(ALARM_SERVICE);
        }
        return mAlarmManager;
    }

    public static Display getDefaultDisplay(){
        return ((WindowManager)getContext().getSystemService(WINDOW_SERVICE)).getDefaultDisplay();
    }

    public static boolean acquirePowerLock(){
        if(sPowerManagerLock == null){
            final PowerManager powerManager = getPowerManager();
            if(powerManager == null){
                Log.e(TAG, "Null Power manager from the system");
                return false;
            }

            if((sPowerManagerLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, TAG)) == null){
                Log.e(TAG, "Null Power manager lock from the system");
                return false;
            }
            sPowerManagerLock.setReferenceCounted(false);
        }

        synchronized(sPowerManagerLock){
            if(!sPowerManagerLock.isHeld()){
                Log.d(TAG,"acquirePowerLock()");
                sPowerManagerLock.acquire();
            }
        }
        return true;
    }

    public Handler getWorkHandler() {
        return workHandler;
    }

    public Handler getMainHandler() {
        return mainHandler;
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        if(mLogReceiver != null){
            unregisterReceiver(mLogReceiver);
        }
    }

    public static boolean releasePowerLock(){
        if(sPowerManagerLock != null){
            synchronized(sPowerManagerLock){
                if(sPowerManagerLock.isHeld()){
                    Log.d(TAG,"releasePowerLock()");
                    sPowerManagerLock.release();
                }
            }
        }
        return true;
    }

}
