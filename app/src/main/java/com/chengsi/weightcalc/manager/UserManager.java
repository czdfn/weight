//package com.jclick.pregnancy.manager;
//
//import com.alibaba.fastjson.JSON;
//import MyApplication;
//import ChatGroup;
//import UserBean;
//import StorageConstants;
//import JDStorage;
//import LogUtil;
//
//import java.util.ArrayList;
//import java.util.List;
//
///**
// * Created by yingjianxu on 15/6/17.
// */
//public class UserManager {
//
//    private MyApplication application;
//    private UserBean userBean;
//
//    private List<OnUserStateChangeListener> listeners = new ArrayList<>();
//
//    public UserManager(MyApplication application){
//        this.application = application;
//    }
//
//    private List<ChatGroup> chatGroupList = new ArrayList<>();
//
//    public void setChatGroupList(List<ChatGroup> list){
//        this.chatGroupList = list;
//    }
//
//    public List<ChatGroup> getChatGroupList() {
//        return chatGroupList;
//    }
//
//    public void resetUser(UserBean userBean){
//        String lastNickName = "";
//        if (this.userBean != null && this.userBean.getNickname() != null){
//            lastNickName = this.userBean.getNickname();
//        }
//        this.userBean = userBean;
//        if (userBean == null){
//            JDStorage.getInstance().clearAll();
//        }else {
////            if (EMChatManager.getInstance().isConnected() && !lastNickName.equals(this.userBean.getNickname()) && this.userBean.getNickname() != null){
////                application.getWorkHandler().post(new Runnable() {
////                    @Override
////                    public void run() {
////                        EMChatManager.getInstance().updateCurrentUserNick(UserManager.this.userBean.getNickname());
////                    }
////                });
////            }
////            application.hxSDKHelper.setHXId(userBean.getHxAccount());
////            application.hxSDKHelper.setPassword(userBean.getHxPwd());
//            JDStorage.getInstance().resetUser(userBean);
//        }
//        for (OnUserStateChangeListener listener : listeners){
//            listener.onUserChanged(userBean);
//        }
//    }
//
//    public void registerOnUserChanged(OnUserStateChangeListener listener){
//        if (listener != null){
//            listeners.add(listener);
//        }
//    }
//
//    public void unRegisterOnUserChanged(OnUserStateChangeListener listener){
//        if (listener != null){
//            listeners.remove(listener);
//        }
//    }
//
//    public UserBean getUserBean() {
//        if (userBean == null){
//            userBean = JDStorage.getInstance().getObjectForKey(StorageConstants.KEY_USER_BEAN, UserBean.class);
//            LogUtil.i("==========================getCurrentUser:" + JSON.toJSONString(userBean));
//        }
//        return userBean;
//    }
//
//    public void resetUser(){
//        resetUser(this.userBean);
//    }
//
//    public boolean isLogin(){
//        return getUserBean() != null;
//    }
//
//    public interface OnUserStateChangeListener{
//        public void onUserChanged(UserBean userBean);
//    }
//}
