package com.chengsi.weightcalc.storage;

import android.text.TextUtils;

import com.alibaba.fastjson.JSONObject;
import com.chengsi.weightcalc.MyApplication;
import com.chengsi.weightcalc.bean.UserBean;
import com.chengsi.weightcalc.constants.StorageConstants;


/**
 * Created by yingjianxu on 15/6/18.
 */
public class JDStorage extends BaseStorage{
    private JDStorage(MyApplication application) {
        super(application);
    }

    public static JDStorage getInstance(){
        return JDStorageHandler.INSTANCE;
    }

    private static class JDStorageHandler{
        public static final JDStorage INSTANCE = new JDStorage(MyApplication.getInstance());
    }

    @Override
    public String getStorageFileName() {
        return "jd_local_cache";
    }

    public void resetUser(UserBean userBean){
        if (userBean == null){
            storeStringValue(StorageConstants.KEY_USER_BEAN, null);
        }else{
            String data = JSONObject.toJSONString(userBean);
            storeStringValue(StorageConstants.KEY_USER_BEAN, data);
        }
    }

    public <T> T getObjectForKey(String key, Class<T> clazz){
        String data = getStringValue(key, null);
        if (!TextUtils.isEmpty(data)){
            return JSONObject.parseObject(data, clazz);
        }else {
            return null;
        }
    }
}
