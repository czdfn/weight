package com.chengsi.weightcalc.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.View;

import com.chengsi.weightcalc.fragment.index.SearchFragment;
import com.chengsi.weightcalc.R;
import com.chengsi.weightcalc.bean.UserBean;
import com.chengsi.weightcalc.fragment.BaseFragment;
import com.chengsi.weightcalc.fragment.index.IndexPageFragment;
import com.chengsi.weightcalc.fragment.index.MeFragment;

public class MainActivity extends BaseActivity {

//    public static final int[] TAB_ID_ARR = {R.id.tab_index_page, R.id.tab_group, R.id.tab_settings};
    public static final int[] TAB_ID_ARR = {R.id.tab_group, R.id.tab_index_page, R.id.tab_settings};
    private BaseFragment mCurFragment;

    private IndexPageFragment mIndexPageFragment;
    private SearchFragment mSearchFragment;
    private MeFragment mMeFragment;
    private UserBean mUserBean;


    public static final String MESSAGE_RECEIVED_ACTION = "com.chengsi.pregnancy.MESSAGE_RECEIVED_ACTION";
    public static final String KEY_TITLE = "title";
    public static final String KEY_MESSAGE = "message";
    public static final String KEY_EXTRAS = "extras";
    public static boolean isForeground = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        if (application.userManager.getUserBean() == null) {
//            startActivity(new Intent(this, LoginActivity.class));
//            finish();
//            return;
//        }else if (TextUtils.isEmpty(application.userManager.getUserBean().getNickname())){
//            Intent in = new Intent(this, UploadProfileActivity.class);
//            in.putExtra("user", application.userManager.getUserBean());
//            startActivity(in);
//            finish();
//            return;
//        }
//        if (savedInstanceState != null && savedInstanceState.getBoolean(Constant.ACCOUNT_REMOVED, false)) {
//            // 防止被移除后，没点确定按钮然后按了home键，长期在后台又进app导致的crash
//            // 三个fragment里加的判断同理
//            finish();
//            startActivity(new Intent(this, LoginActivity.class));
//            return;
//        } else if (savedInstanceState != null && savedInstanceState.getBoolean("isConflict", false)) {
//            // 防止被T后，没点确定按钮然后按了home键，长期在后台又进app导致的crash
//            // 三个fragment里加的判断同理
//            finish();
//            startActivity(new Intent(this, LoginActivity.class));
//            return;
//        }
        //异步获取当前用户的昵称和头像
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        setContentView(R.layout.activity_main);
        setMyTitle("嵊泗出入境检验检疫局");
//        setMyTitle("中一");
        mIndexPageFragment = new IndexPageFragment();
        changeTab(findViewById(R.id.tab_index_page));


//        JDHttpClient.getInstance().reqUserInfo(this, new JDHttpResponseHandler<UserBean>(new TypeReference<BaseBean<UserBean>>() {
//        }) {
//            @Override
//            public void onRequestCallback(BaseBean<UserBean> result) {
//                super.onRequestCallback(result);
//                if (result.isSuccess()) {
//                    if (result.getData() != null) {
//                        if (TextUtils.isEmpty(result.getData().getNickname())) {
//                            Intent intent = new Intent(MainActivity.this, UploadProfileActivity.class);
//                            intent.putExtra("user", result.getData());
//                            startActivity(intent);
//                        } else {
////                            application.userManager.resetUser(result.getData());
//                        }
//                        //Add user phone as Alias and hospital name which was bound by user as Tag to JPush cloud
//                        mUserBean = result.getData();
//                        Set<String> set = new HashSet<>();
//                        if (mUserBean.getHospital() != null) {
//                            set.add(mUserBean.getHospital().getName());
//                            JPushInterface.setAliasAndTags(MainActivity.this, mUserBean.getPhone(), set, new TagAliasCallback() {
//                                @Override
//                                public void gotResult(int i, String s, Set<String> set) {
//                                    Log.i(TAG, s + " - " + i + " - " + set.toString());
//                                }
//                            });
//                        } else {
//                            set.add("undefined");
//                            JPushInterface.setAliasAndTags(MainActivity.this, mUserBean.getPhone(), set, new TagAliasCallback() {
//                                @Override
//                                public void gotResult(int i, String s, Set<String> set) {
//                                    Log.i(TAG, s + " - " + i + " - " + set.toString());
//                                }
//                            });
//                        }
//                    } else {
//                        finish();
//                    }
//                }
//            }
//        });
    }

//    public void updateUnreadLabel(){
//    }
//
//    public void changeTabIndex(int index){
//        if (index < TAB_ID_ARR.length && index >= 0){
//            changeTab(findViewById(TAB_ID_ARR[index]));
//        }
//    }
//
//    public void showDynamicList(){
//        if (mGroupFragment == null) {
//            mGroupFragment = new GroupFragment();
//            Bundle bundle = new Bundle();
//            bundle.putInt("index", 1);
//            mGroupFragment.setArguments(bundle);
//        }else{
//            mGroupFragment.showDynamic();
//        }
//        switchContent(mCurFragment, mGroupFragment);
//        for (int id : TAB_ID_ARR) {
//            if (R.id.tab_group == id) {
//                findViewById(id).setSelected(true);
//            } else {
//                findViewById(id).setSelected(false);
//            }
//        }
//    }

    public void changeTab(View view) {
        switch (view.getId()) {
            case R.id.tab_index_page: {
                switchContent(mCurFragment, mIndexPageFragment);
//                setMyTitle(R.string.tab_index_page);
                break;
            }
            case R.id.tab_group: {
                if (mSearchFragment == null) {
                    mSearchFragment = new SearchFragment();
                }
//                setMyTitle(R.string.tab_group);
                switchContent(mCurFragment, mSearchFragment);
                break;
            }
            case R.id.tab_settings: {
                if (mMeFragment == null) {
                    mMeFragment = new MeFragment();
                }
//                setMyTitle(R.string.tab_me);
                switchContent(mCurFragment, mMeFragment);
                break;
            }
        }
        for (int id : TAB_ID_ARR) {
            if (view.getId() == id) {
                view.setSelected(true);
            } else {
                findViewById(id).setSelected(false);
            }
        }
    }

    public BaseFragment getCurFragment() {
        return mCurFragment;
    }

    public void switchContent(BaseFragment from, BaseFragment to) {
        if (mCurFragment != to) {
            mCurFragment = to;

            FragmentTransaction transaction = getSupportFragmentManager()
                    .beginTransaction();
            if (from != null) {
                if (!to.isAdded()) { // 先判断是否被add过
                    transaction.hide(from).add(R.id.tab_container, to).commitAllowingStateLoss(); // 隐藏当前的fragment，add下一个到Activity中
                } else {
                    transaction.hide(from).show(to).commitAllowingStateLoss();
                }
            } else {
                if (!to.isAdded()) { // 先判断是否被add过
                    transaction.add(R.id.tab_container, to).commitAllowingStateLoss(); // 隐藏当前的fragment，add下一个到Activity中
                } else {
                    transaction.show(to).commitAllowingStateLoss();
                }
            }
        }
    }

//    @Override
//    public void onUserChanged(UserBean userBean) {
//        super.onUserChanged(userBean);
//        if (userBean != null && userBean.getHospital() != null) {
//            setMyTitle("中山大学附属第一院生殖中心");
//        }
//    }

    private long exitTime = 0;
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN){
            if((System.currentTimeMillis()-exitTime) > 2000){
                showToast(R.string.exit_app);
                exitTime = System.currentTimeMillis();
            } else {
                finish();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
