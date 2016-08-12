package com.chengsi.weightcalc.storage;

import android.content.SharedPreferences;

import com.chengsi.weightcalc.MyApplication;

/**    
 * @Description: 数据存储中心基类
 * @date 20141014
 */
public abstract class BaseStorage {

	protected SharedPreferences mSharedPreference = null;
	protected MyApplication mApplication;
	protected String mStorageFileName = "";
	
	public BaseStorage(MyApplication application) {
		this.mApplication = application;
		mStorageFileName = getStorageFileName();
		mSharedPreference = mApplication.getSharedPreferences(mStorageFileName, 0 );
	}

	
	/**
	 * 根据key值取对应的SharedPreference存储文件，不存在则自动新建
	 * 可以规范缓存文件命名：cache_game_jid_subname
	 */
	public abstract String getStorageFileName();

	public boolean storeBooleanValue(String key, boolean value) {		
		return mSharedPreference.edit().putBoolean(key, value).commit();		
	}
	
	public boolean storeIntValue(String key, int value) {
		return mSharedPreference.edit().putInt(key, value).commit();		
	}

	public boolean storeLongValue(String key, long value) {
		return mSharedPreference.edit().putLong(key, value).commit();
	}

	public boolean storeStringValue(String key, String value) {
		return mSharedPreference.edit().putString(key, value).commit();
	}
	
	public String getStringValue(String key, String defValue){
		if(mSharedPreference != null){
			return mSharedPreference.getString(key, defValue);
		}else{
			return defValue;
		}
	}
	
	public Boolean getBooleanValue(String key, Boolean defValue){
		if(mSharedPreference != null){
			return mSharedPreference.getBoolean(key, defValue);
		}else{
			return defValue;
		}
	}
	
	public int getIntValue(String key, int defValue){
		if(mSharedPreference != null){
			return mSharedPreference.getInt(key, defValue);
		}else{
			return defValue;
		}
	}

	public long getLongValue(String key, long defValue){
		if(mSharedPreference != null){
			return mSharedPreference.getLong(key, defValue);
		}else{
			return defValue;
		}
	}

	public boolean clearAll(){
		return mSharedPreference.edit().clear().commit();		
	}
	
	
}
