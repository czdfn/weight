//package com.jclick.pregnancy.fragment.group;
//
//
//import android.app.AlertDialog;
//import android.content.DialogInterface;
//import android.content.Intent;
//import android.os.Bundle;
//import android.support.v4.widget.SwipeRefreshLayout;
//import android.text.TextUtils;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.AdapterView;
//import android.widget.TextView;
//
//import com.alibaba.fastjson.TypeReference;
//import com.jclick.zhongyi.R;
//import SocialStreamAdapter;
//import BaseBean;
//import ChatGroup;
//import HospitalBean;
//import BaseFragment;
//import JDHttpClient;
//import JDHttpResponseHandler;
//import FanrRefreshListView;
//import JDLoadingView;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//import butterknife.ButterKnife;
//import butterknife.InjectView;
//import cn.jiadao.corelibs.utils.ListUtils;
//
//public class GroupChatFragment extends BaseFragment{
//
//    @InjectView(R.id.listview)
//    FanrRefreshListView listView;
//
//    private ChatGroup currentGroup = null;
//    private List<ChatGroup> groupChatList = new ArrayList<>();
//
//    private SocialStreamAdapter mAdapter = null;
//    private List<Map<String, Object>> dataSource = new ArrayList<>();
//    private static final int[] RESOUCE = {R.layout.item_group_chat_list};
//    private String[] from = {"name","join","roomNo"};
//    private int[] to = {R.id.tv_chat_group_name, R.id.tv_chat_join,R.id.tv_chat_No};
//
//
//    public GroupChatFragment() {
//    }
//
//    @Override
//    public View getContentView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        View view = inflater.inflate(R.layout.fragment_group_chat, container, false);
//        ButterKnife.inject(this, view);
//      HashMap<Integer, String[]> fromMap = new HashMap<Integer, String[]>();
//        fromMap.put(RESOUCE[0], from);
//        HashMap<Integer, int[]> toMap = new HashMap<Integer, int[]>();
//        toMap.put(RESOUCE[0], to);
//        mAdapter = new SocialStreamAdapter(getActivity(), dataSource, RESOUCE, fromMap, toMap, 0, 0);
//        mAdapter.setViewBinder(new SocialStreamAdapter.ViewBinder() {
//            @Override
//            public boolean setViewValue(View view, Object data, Object comment) {
//                if (view.getId() == R.id.tv_chat_No || view.getId() == R.id.tv_chat_join) {
//                    Log.d("GroupChatFragment", "setViewBinder");
//                    String join = (String) (data != null ? data : "");
//                    TextView tv = (TextView) view;
//                    tv.setText(String.valueOf(join));
//                    return true;
//                }
//                return false;
//            }
//        });
//        listView.setAdapter(mAdapter);
//        listView.setOnPullRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                setLoadingState(JDLoadingView.STATE_LOADING);
//                getGroupRoomFromDB();
//            }
//        });
//        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
//                Log.i("GroupChatFragment", "onItemClick");
//                TextView  clickRoomName = (TextView)view.findViewById(R.id.tv_chat_group_name);
//                if(null != currentGroup && !currentGroup.getRoomName().equals(clickRoomName.getText())) {
//
//                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
//                    builder.setTitle("切换后，您在"+currentGroup.getRoomName()+"的记录将被清空！");
//                    builder.setMessage("您确认从聊天室：" + currentGroup.getRoomName() + "切换到：" + clickRoomName.getText());
//                    builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                            //是否被禁言
//                            isBannedSpeak(position);//切换聊天室在禁言的回调事件中执行；
//                        }
//                    });
//                    builder.setNegativeButton("取消",null);
//                    AlertDialog ad = builder.create();
//                    ad.show();
//                }else
//                    isBannedSpeak(position);//切换聊天室在禁言的回调事件中执行；
//            }
//        });
//        initDataSource();
//        return view;
//    }
//
//    private boolean isBannedSpeak(final int position) {
//        setLoadingState(JDLoadingView.STATE_LOADING);
//        String phone = fanrApp.userManager.getUserBean().getPhone();
//        JDHttpClient.getInstance().reqGetUserBanned(getActivity(), phone, new JDHttpResponseHandler<String>(new TypeReference<BaseBean<String>>() {
//        }) {
//            @Override
//            public void onRequestCallback(BaseBean<String> result) {
//                Log.i("GroupChatFragment", "isBannedSpeak  " + result.getMessage());
//                if (result.isSuccess()) {
//                    //没有被禁言
//                    if("0".equals(result.getData())) {
//                        ChatGroup clickGroup = groupChatList.get(position);
//                        if (null != currentGroup && !currentGroup.equals(clickGroup)) {
//                            leaveGroupRoom(currentGroup);
//                        }
//                        enterChatRoom(clickGroup);
//                        getGroupRoomFromDB();
//                    }
//                    else{
//                        dismissLoadingView();
//                        showToast("您已经被管理员禁言！");
//                    }
//                } else {
//                    Log.i("GroupChatFragment", "OnleaveCurrentRoom fail");
//                    dismissLoadingView();
//                    showToast("获取用户发言状态失败，请检查您的网络");
//                }
//            }
//        });
//        return false;
//    }
//
//    private void leaveGroupRoom(ChatGroup groupRoom) {
//        Log.i("GroupChatFragment", "leaveGroupRoom");
//        /*退出当前聊天群        */
//        JDHttpClient.getInstance().reqDeleteUserFromGroup(getActivity(), groupRoom.getRoomId(), fanrApp.userManager.getUserBean().getHxAccount(),
//                new JDHttpResponseHandler<String>(new TypeReference<BaseBean<String>>() {
//                }) {
//                    @Override
//                    public void onRequestCallback(BaseBean<String> result) {
//                        super.onRequestCallback(result);
//                        if (result.isSuccess()) {
//                            refreshLocalDataSet("leaveGroupRoom");
//                            Log.i("GroupChatFragment", "OnleaveCurrentRoom  " + result.getMessage());
//                        } else {
//                            Log.i("GroupChatFragment", "OnleaveCurrentRoom fail");
//                        }
//                    }
//                });
//
//    }
//
//    private void enterChatRoom(final ChatGroup enterGroup){
//        Log.i("GroupChatFragment", "enterChatRoom begin");
//        //加入聊天室()
//        String HXCount = fanrApp.userManager.getUserBean().getHxAccount();
//        if(!enterGroup.equals(currentGroup) || null == currentGroup){
//            //如果不是当前加入的群组
//            //先取目标群组的人数，判断是否到达上线
//            if(!roomIsLimited(enterGroup)){
//                //如果没有达到上线，则可以加入
//                JDHttpClient.getInstance().reqAddUserToHXGroup(getActivity(), enterGroup.getRoomId(), HXCount, new JDHttpResponseHandler<String>(new TypeReference<BaseBean<String>>() {
//                }) {
//                    @Override
//                    public void onRequestCallback(BaseBean<String> result) {
//                        super.onRequestCallback(result);
//                        if (result.isSuccess()) {
//                            Log.i("GroupChatFragment", "onEnterChatRoom  " + result.getMessage());
//                            startChatActivity(enterGroup);
//                        } else {
//                            Log.i("GroupChatFragment", "onEnterChatRoom fail");
//                        }
//                    }
//                });
//            }
//            else{
//                showToast("该聊天室已满，请选择其他聊天室");
//            }
//        }else{
//            //如果是当前群组，直接进入；
//            startChatActivity(enterGroup);
//        }
//        Log.i("GroupChatFragment", "enterChatRoom :setCurrentRoom");
//        currentGroup = enterGroup;
//    }
//
//    private boolean roomIsLimited(ChatGroup enterGroup) {
//        getGroupRoomFromDB();
//        int limit = 0;
//        int count = 0;
//        for(ChatGroup group:groupChatList){
//            if(group.getRoomId().equals(enterGroup.getRoomId())){
//                limit=group.getPeopleLimit();
//                count = group.getPeopleCount();
//                break;
//            }
//        }
//        Log.i("GroupChatFragment","roomIsLimited result "+String.valueOf(count >= limit));
//        return count >= limit;
//    }
//
////    private void startChatActivity(ChatGroup enterGroup){
////        Log.i("GroupChatFragment", "startChatActivity");
////        Intent intent = new Intent(getActivity(), ChatActivity.class);
////        // it is group chat
////        intent.putExtra("chatType", ChatActivity.CHATTYPE_GROUP);
////        intent.putExtra("group", enterGroup);
////        intent.putExtra("groupId", enterGroup.getRoomId());
////        startActivityForResult(intent, 0);
////    }
//    public void initDataSource() {
//        Log.i("GroupChatFragment", "initDataSource");
//        setLoadingState(JDLoadingView.STATE_LOADING);
//        initJoinedHxGroup();
//    }
//
//    private void getGroupRoomFromDB(){
//        Log.i("GroupChatFragment", "getGroupRoomFromDB");
//        String phone = fanrApp.userManager.getUserBean().getPhone();
//        String hospitalId = "";
//        HospitalBean hospital = fanrApp.userManager.getUserBean().getHospital();
//        if(null != hospital)
//            hospitalId =  ((Long)hospital.getId()).toString();
//        if(!TextUtils.isEmpty(phone)) {
//            JDHttpClient.getInstance().reqGetHospitalChatRooms(getActivity(),phone,hospitalId, new JDHttpResponseHandler<List<ChatGroup>>(new TypeReference<BaseBean<List<ChatGroup>>>() {
//            }) {
//                @Override
//                public void onRequestCallback(BaseBean<List<ChatGroup>> result) {
//                    super.onRequestCallback(result);
//                    if (result.isSuccess()) {
//                        groupChatList = result.getData();
//                        Log.i("GroupChatFragment", "reqGetHospitalChatRooms");
//                        refreshLocalDataSet("reqChatGroupList");
//                        dismissLoadingView();
//                        listView.setPullRefreshing(false);
//                    }
//                }
//            });
//        }
//    }
//    private void initJoinedHxGroup() {
//        Log.i("GroupChatFragment", "initJoinedHxGroup");
//        JDHttpClient.getInstance().reqHXChatGroupList(getActivity(), "1", new JDHttpResponseHandler<List<ChatGroup>>(new TypeReference<BaseBean<List<ChatGroup>>>() {
//        }) {
//            @Override
//            public void onRequestCallback(BaseBean<List<ChatGroup>> result) {
//                super.onRequestCallback(result);
//                listView.setPullRefreshing(false);
//                if (result.isSuccess()) {
//                    List<ChatGroup> joinedGroupChatList = result.getData();
//                    if(!ListUtils.isEmpty(joinedGroupChatList)) {
//                        Log.i("GroupChatFragment", "initJoinedHxGroup size" +joinedGroupChatList.size() );
//                        currentGroup = joinedGroupChatList.get(0);
//                        enterChatRoom(currentGroup);
//                        //leaveGroupRoom(group);
//                       //fanrApp.userManager.setChatGroupList(joinedGroupChatList);
//                    }
//                    getGroupRoomFromDB();
//
//                } else {
//                    setLoadingState(JDLoadingView.STATE_FAILED);
//                }
//
//            }
//        });
//    }
//
//    public void refreshLocalDataSet(String caller) {
//        Log.i("GroupChatFragment", "refreshLocalDataSet begin :"+caller);
//        dataSource.clear();
//        if (!ListUtils.isEmpty(groupChatList)) {
//
//            for (final ChatGroup bean : groupChatList) {
//                Map<String, Object> map = new HashMap<>();
//                map.put(from[0], bean.getRoomName());
//                map.put(from[1], bean.getJoined());//暂时用不上
//                map.put(from[2], "当前聊天室人数："+bean.getPeopleCount()+"/"+bean.getPeopleLimit());
//                dataSource.add(map);
//            }
//        }
//        mAdapter.notifyDataSetChanged();
//        Log.i("GroupChatFragment", "refreshLocalDataSet end: " + caller );
//    }
//
//    @Override
//    public void onResume() {
//        super.onResume();
//        Log.i("GroupChatFragment","onResume");
//        getGroupRoomFromDB();
//    }
//
////    @Override
////    public void onDestroyView() {
////        super.onDestroyView();
////        EMChatManager.getInstance().unregisterEventListener(this);
////    }
////
////    @Override
////    public void onEvent(EMNotifierEvent emNotifierEvent) {
////        //refreshLocalDataSet("onEvent");
////    }
//}
