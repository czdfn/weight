package com.chengsi.weightcalc.storage;

import android.text.TextUtils;

import com.alibaba.fastjson.JSONObject;
import com.chengsi.weightcalc.MyApplication;
import com.chengsi.weightcalc.constants.StorageConstants;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by XuYingjian on 15/7/9.
 */
public class JDCustomStorage extends BaseStorage{

    private JDCustomStorage(MyApplication application) {
        super(application);
    }

    public static JDCustomStorage getInstance(){
        return JDCustomStorageHandler.INSTANCE;
    }

    private static class JDCustomStorageHandler{
        public static final JDCustomStorage INSTANCE = new JDCustomStorage(MyApplication.getInstance());
    }

    @Override
    public String getStorageFileName() {
//        return MyApplication.getInstance().userManager.isLogin() ? "custom_config_" + MyApplication.getInstance().userManager.getUserBean().getPhone()
//                : "custom_config";
        return "co";
    }


    public void setRollingList(List<String> list){
        if (list == null){
            storeStringValue(StorageConstants.KEY_ROLLING_IMAGE_LIST, null);
        }else{
            String data = JSONObject.toJSONString(list);
            storeStringValue(StorageConstants.KEY_ROLLING_IMAGE_LIST, data);
        }
    }

    public List<String> getRollingList(){
        String data = getStringValue(StorageConstants.KEY_ROLLING_IMAGE_LIST, null);
        if (!TextUtils.isEmpty(data)){
            return JSONObject.parseObject(data, List.class);
        }else {
            return new ArrayList<>();
        }
    }
}
