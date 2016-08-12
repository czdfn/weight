package com.chengsi.weightcalc.activity;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Transformation;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.TypeReference;
import com.chengsi.weightcalc.R;
import com.chengsi.weightcalc.bean.BaseBean;
import com.chengsi.weightcalc.bean.HospitalBean;
import com.chengsi.weightcalc.bean.ListBaseBean;
import com.chengsi.weightcalc.http.HttpConstants;
import com.chengsi.weightcalc.http.JDHttpClient;
import com.chengsi.weightcalc.http.JDHttpResponseHandler;
import com.chengsi.weightcalc.storage.JDStorage;
import com.chengsi.weightcalc.utils.JDUtils;
import com.chengsi.weightcalc.utils.Position;
import com.chengsi.weightcalc.utils.imageloader.ImageLoaderUtils;
import com.chengsi.weightcalc.widget.JDLoadingView;
import com.chengsi.weightcalc.widget.rightindexlistview.RightIndexListViewWrap;
import com.chengsi.weightcalc.widget.rightindexlistview.RightIndexerListAdapter;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class HospitalListActivity extends BaseActivity implements TextWatcher {
    private final static String TAG = HospitalListActivity.class.getSimpleName();
    public static final String KEY_SELECT_HOSPITAL = "KEY_SELECT_HOSPITAL";
    public static final String KEY_IS_FOR_LOGIN = "KEY_IS_FOR_LOGIN";
    public static final String KEY_IS_FOR_INDEX_PAGE = "KEY_IS_FOR_INDEX_PAGE";

    public static HashMap<Integer,Integer> typemap = new HashMap<>();
    public static final int COOPERATIVE_NORMAL = 1;
    public static final int COOPERATIVE_DEEP = 2;
    public static final int COOPERATIVE_STRATAGY = 3;
    public static final int COOPERATIVE_OTHER = 4;
    static{
        typemap.put(3,COOPERATIVE_NORMAL);
        typemap.put(1,COOPERATIVE_DEEP);
        typemap.put(2,COOPERATIVE_STRATAGY);
        typemap.put(4,COOPERATIVE_OTHER);
    }
    public static final int POPULARITY_COUNTRY = 1;
    public static final int POPULARITY_CITY = 2;
    private boolean isForLogin = false;
    private boolean isForIndexPage = false;
    private static final String KEY_STORAGR_HISTORY = "search_history";
    private Context context_ = HospitalListActivity.this;
    private RightIndexListViewWrap totalListview;
    private ListView searchResultList;
    private View searchInputPanel;
    private View vSearchClear;
    private View vSearchClose;
    private EditText searchInput;
    private String searchString;
    private Object searchLock = new Object();
    boolean inSearchMode = false;
    private HospitalBean hos;
    private List<HospitalBean> dataList;
    private List<HospitalBean> filterList;
    private SearchListTask curSearchTask = null;
    private static double Latitude;
    private static double Longitude;
    private HospitalAdapter adapter1;
    private HospitalAdapter adapter;
    private TextView currentCity;
    private boolean isInputing = false;
    private ScrollView sv;
    private TextView currentCityLable;

    @Override
    protected void onStart() {
        super.onStart();
        if(adapter!=null) {
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hospital_list);
        setMyTitle("生殖中心列表");
        currentCityLable = (TextView) findViewById(R.id.curcity_lable);
        currentCity = (TextView) findViewById(R.id.curcity);

        currentCity.setText(JDStorage.getInstance().getStringValue("city", null));
        isForLogin = getIntent().getBooleanExtra(KEY_IS_FOR_LOGIN, false);
        isForIndexPage = getIntent().getBooleanExtra(KEY_IS_FOR_INDEX_PAGE, false);

        filterList = new ArrayList<HospitalBean>();
        dataList = new ArrayList<>();
        totalListview = (RightIndexListViewWrap) findViewById(R.id.listview);
        if (JDStorage.getInstance().getStringValue("lat", null) == null
                || JDStorage.getInstance().getStringValue("lng", null) == null) {
            currentCity.setVisibility(View.GONE);
            currentCityLable.setVisibility(View.GONE);
        }
        sv = (ScrollView) findViewById(R.id.scrollV);
        sv.smoothScrollTo(0, 0);
        searchInputPanel = findViewById(R.id.search_input_panel);
        adapter1 = new HospitalAdapter(HospitalListActivity.this, R.layout.item_city_list,
                dataList);
        totalListview.setFastScrollEnabled(false);
        totalListview.setAdapter(adapter1);
        totalListview.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView parent, View v, int position, long id) {
                int count = totalListview.getHeaderViewsCount();
                if (position < count) {
                    return;//header的点击事件也会在这触发
                }
                onCitySelected(dataList.get(position - count));//listview需要减去header数
            }
        });
        //初始化搜索历史列表
        searchResultList = (ListView) findViewById(R.id.city_search_result);
        searchResultList.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                onCitySelected(filterList.get(position));
            }
        });

        //搜索输入框
        searchInput = (EditText) findViewById(R.id.search_btn);
        searchInput.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                currentCityLable.setVisibility(View.GONE);
                currentCity.setVisibility(View.GONE);
                if (isInputing) {
                    return;
                }
                JDUtils.hideIME(HospitalListActivity.this);
                isInputing = true;
                Animation animation = new TransYAnimation(toolbar, TransYAnimation.ACTION_UP);
                animation.setDuration(150);
                DecelerateInterpolator inter = new DecelerateInterpolator();
                animation.setInterpolator(inter);
                animation.setAnimationListener(new AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {
                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        searchInputPanel.setBackgroundColor(getResources().getColor(R.color.color_theme));
                        searchInput.setInputType(EditorInfo.TYPE_TEXT_VARIATION_PHONETIC);
                        ScaleXAnimation scaleAnim = new ScaleXAnimation(vSearchClose, ScaleXAnimation.ACTION_PLUS, vSearchClose.getMeasuredHeight());
                        scaleAnim.setDuration(150);
                        DecelerateInterpolator interPolator = new DecelerateInterpolator();
                        scaleAnim.setInterpolator(interPolator);
                        scaleAnim.setAnimationListener(new AnimationListener() {
                            @Override
                            public void onAnimationStart(Animation animation) {
                            }

                            @Override
                            public void onAnimationRepeat(Animation animation) {
                            }

                            @Override
                            public void onAnimationEnd(Animation animation) {
                                totalListview.setVisibility(View.GONE);

                                currentCity.setVisibility(View.GONE);
                                searchInput.setFocusable(true);
                                searchInput.setFocusableInTouchMode(true);
                                JDUtils.showIME(context_, searchInput);
                                toolbar.clearAnimation();
                            }
                        });
                        vSearchClose.startAnimation(scaleAnim);
                    }
                });
                toolbar.startAnimation(animation);
            }
        });
        searchInput.addTextChangedListener(this);

        //输入框右侧清空按钮
        vSearchClear = findViewById(R.id.input_clear_iv);
        vSearchClear.setVisibility(View.GONE);
        vSearchClear.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                searchInput.setText("");//TODO
            }
        });

        //输入框左侧取消按钮
        vSearchClose = findViewById(R.id.search_close);
        vSearchClose.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isInputing) {
                    return;
                }
                isInputing = false;
                searchInputPanel.setBackgroundColor(getResources().getColor(R.color.default_bg));
                ScaleXAnimation scaleXAnim = new ScaleXAnimation(vSearchClose, ScaleXAnimation.ACTION_MINUS, 0);
                scaleXAnim.setDuration(100);
                DecelerateInterpolator inter = new DecelerateInterpolator();
                scaleXAnim.setInterpolator(inter);
                vSearchClose.startAnimation(scaleXAnim);
                scaleXAnim.setAnimationListener(new AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {
                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        Animation transYAnim = new TransYAnimation(toolbar, TransYAnimation.ACTION_DOWN);
                        transYAnim.setDuration(150);
                        DecelerateInterpolator interpolator = new DecelerateInterpolator();
                        transYAnim.setInterpolator(interpolator);
                        toolbar.startAnimation(transYAnim);
                        transYAnim.setAnimationListener(new AnimationListener() {
                            @Override
                            public void onAnimationStart(Animation animation) {
                            }

                            @Override
                            public void onAnimationRepeat(Animation animation) {
                            }

                            @Override
                            public void onAnimationEnd(Animation animation) {
                                totalListview.setVisibility(View.VISIBLE);
                                searchResultList.setVisibility(View.GONE);
                                currentCityLable.setVisibility(View.VISIBLE);
                                currentCity.setVisibility(View.VISIBLE);
                                searchInput.setInputType(EditorInfo.TYPE_NULL);
                                searchInput.setText("");
                                searchInput.clearFocus();
                            }
                        });
                    }
                });
            }
        });

        initDataSource("");
        //initAmapLocation();
    }

    private void initDataSource(String name) {
        setLoadingViewState(JDLoadingView.STATE_LOADING);
        if (isForIndexPage) {
            JDHttpClient.getInstance().reqAttendHospitalList(this, new JDHttpResponseHandler<List<HospitalBean>>(new TypeReference<BaseBean<List<HospitalBean>>>(){}){

                @Override
                public void onRequestCallback(BaseBean<List<HospitalBean>> result) {
                    super.onRequestCallback(result);
                    if (result.isSuccess()){
//                        UserBean userBean = application.userManager.getUserBean();
//                        userBean.setAttenHospitalList(result.getData());
                        Log.i("RESULTSTATE_LOADING", "" + result.getData().size());
                        if (adapter1 != null) {
                            adapter1.notifyDataSetChanged();
                        }
                    } else {
                        Log.i("RESULTSTATE_LOADING", "error");
                        setLoadingViewState(JDLoadingView.STATE_FAILED);
                        showToast(result.getMessage());
                    }
                }
            });
        }
        JDHttpClient.getInstance().reqHospitalList(this, name, new JDHttpResponseHandler<ListBaseBean<List<HospitalBean>>>(new TypeReference<BaseBean<ListBaseBean<List<HospitalBean>>>>(){}) {
                    @Override
                    public void onRequestCallback(BaseBean<ListBaseBean<List<HospitalBean>>> result) {
                        super.onRequestCallback(result);
                        if (result.isSuccess()) {
                            dataList.clear();
                            //获取全部医院数据，无删减
                            if (result.getData() != null && result.getData().getResult() != null) {
                                dataList.addAll(result.getData().getResult());
                            }
                            dismissLoadingView();
                            if (JDStorage.getInstance().getStringValue("lat", null) != null
                                    && JDStorage.getInstance().getStringValue("lng", null) != null) {
                                //获取省内医院数据，无排序
                                int len = dataList.size();
                                //对省内医院按离用户当前位置距离排序
                                Collections.sort(dataList, new Comparator<HospitalBean>() {
                                    @Override
                                    public int compare(HospitalBean lhs, HospitalBean rhs) {

                                        Latitude = Double.valueOf(JDStorage.getInstance().getStringValue("lat", null));
                                        Longitude = Double.valueOf(JDStorage.getInstance().getStringValue("lng", null));
                                        double lat1 = Double.valueOf(lhs.getLatitude());
                                        double lon1 = Double.valueOf(lhs.getLongitude());
                                        double lat2 = Double.valueOf(rhs.getLatitude());
                                        double lon2 = Double.valueOf(rhs.getLongitude());

                                        double dis1 = Position.getDistance(Latitude, Longitude, lat1, lon1);
                                        double dis2 = Position.getDistance(Latitude, Longitude, lat2, lon2);
                                        if(dis1<dis2){
                                            return -1;
                                        }else if(dis1>dis2) {
                                            return 1;
                                        }else
                                            return 0;
                                    }
                                });
                                List<HospitalBean> temp = new ArrayList<>();

                                for(int i=1;i<3;i++){
                                    getlist(temp,typemap.get(i));
                                }
                                dataList.addAll(2,temp);
                                adapter1.notifyDataSetChanged();
                            }else{
                                Toast.makeText(HospitalListActivity.this, "不能获取您的当前位置", Toast.LENGTH_SHORT);
                            }
                        }
                    }
                }
        );
    }
    private void getlist(List<HospitalBean> temp, int type){
        for (int a = 2; a < 6; a++) {
            String curCity = dataList.get(a).getCity();
            String inprovince = JDStorage.getInstance().getStringValue("province", null);
            String incity = JDStorage.getInstance().getStringValue("city", null);
            if(curCity.contains(inprovince) ||curCity.contains(incity)) {
                int cooperative = dataList.get(a).getCooperativeType();
                int propular = dataList.get(a).getPopularity();
                if(cooperative==type) {
                    temp.add(dataList.get(a));
                    dataList.remove(a);
                }
            }
        }
    }

    private void onCitySelected(HospitalBean city) {
        if (isForIndexPage){
            Intent intent = new Intent(this, HospitalPageActivity.class);
            intent.putExtra(HospitalPageActivity.KEY_CURRENT_HOSPITAL, city);
            startActivity(intent);
            return;
        }
        if (isForLogin){
            Intent intent = new Intent(this, HospitalPageActivity.class);
            intent.putExtra(HospitalPageActivity.KEY_CURRENT_HOSPITAL, city);
            startActivity(intent);
            return;
        }else{
            Intent data = new Intent();
            data.putExtra(KEY_SELECT_HOSPITAL, city);
            setResult(RESULT_OK, data);
            finish();
        }
    }

    @Override
    public void afterTextChanged(Editable s) {
        searchString = searchInput.getText().toString().trim().toUpperCase(Locale.CHINA);

        if (!TextUtils.isEmpty(searchString)) {
            vSearchClear.setVisibility(View.VISIBLE);
        } else {
            vSearchClear.setVisibility(View.GONE);
        }

        if (curSearchTask != null
                && curSearchTask.getStatus() != AsyncTask.Status.FINISHED) {
            try {
                curSearchTask.cancel(true);
            } catch (Exception e) {
                Log.i(TAG, "Fail to cancel running search task");
            }

        }
        curSearchTask = new SearchListTask();
        curSearchTask.execute(searchString);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count,
                                  int after) {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        // do nothing
    }

    private class SearchListTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            filterList.clear();

            String keyword = params[0];

            inSearchMode = (keyword.length() > 0);
            if (inSearchMode) {
//                currentCityLable.setVisibility(View.GONE);
//                currentCity.setVisibility(View.GONE);
                // get all the items matching this
                for (HospitalBean item : dataList) {
                    HospitalBean contact = (HospitalBean) item;

                    boolean isPinyin = false;
                    if (!TextUtils.isEmpty(contact.getPinyin())){
                        isPinyin = contact.getPinyin().toUpperCase().indexOf(keyword) > -1;
                    }
                    boolean isChinese = false;
                    if (!TextUtils.isEmpty(contact.getCity())){
                        isChinese = contact.getCity().indexOf(keyword) > -1;
                    }
                    boolean flag = false;
                    if(!TextUtils.isEmpty(contact.getName())){
                        flag = contact.getName().indexOf(keyword) > -1;
                    }

                    if (isPinyin || isChinese || flag) {
                        filterList.add(item);
                    }

                }

//			}else{
//				filterList.addAll(contactList);
            }
            return null;
        }

        protected void onPostExecute(String result) {

            synchronized (searchLock) {
                searchResultList.setVisibility(inSearchMode ? View.VISIBLE : View.GONE);
                if (inSearchMode) {

                    adapter = new HospitalAdapter(context_,
                            R.layout.item_city_list, filterList);
                    adapter.setInSearchMode(true);
                    searchResultList.setAdapter(adapter);
                }else {
//                    HospitalAdapter adapter = new HospitalAdapter(context_,
//                            R.layout.item_city_list, contactList);
//                    adapter.setInSearchMode(false);
//                    totalListview.setInSearchMode(false);
//                    totalListview.setAdapter(adapter);
                }
            }

        }
    }

    class HospitalAdapter extends RightIndexerListAdapter {

        public HospitalAdapter(Context _context, int _resource,
                               List<HospitalBean> _items) {
            super(_context, _resource, _items);
        }

        public void populateDataForRow(View parentView,
                                       final HospitalBean item, int position) {
            View infoView = parentView.findViewById(R.id.infoRowContainer);

            TextView nicknameView = (TextView) infoView.findViewById(R.id.cityName);
            TextView attendTv = (TextView) infoView.findViewById(R.id.tv_attend);
            ImageView hosView = (ImageView) infoView.findViewById(R.id.hos_imgv);
            ImageLoaderUtils.displayImageForIv(hosView, HttpConstants.HTTP_BASE_URL + item.getImgUrl());
            if (isForIndexPage) {
//                if (application.userManager.getUserBean().getAttenHospitalList().contains(item)) {
//                    attendTv.setText("取消关注");
//                    attendTv.setSelected(true);
//                    attendTv.setOnClickListener(new OnContinuousClickListener() {
//                        @Override
//                        public void onContinuousClick(View v) {
//                            attendHospital(item, 0);
//                        }
//                    });
//                } else {
//                    attendTv.setText("关注");
//                    attendTv.setSelected(false);
//                    attendTv.setOnClickListener(new OnContinuousClickListener() {
//                        @Override
//                        public void onContinuousClick(View v) {
//                            attendHospital(item, 1);
//                        }
//                    });
//                }
            } else {
                attendTv.setVisibility(View.GONE);
            }
            double lat1 = Double.valueOf(item.getLatitude());
            double lon1 = Double.valueOf(item.getLongitude());
            double dis = Position.getDistance(Latitude, Longitude, lat1, lon1);
            nicknameView.setText(item.getAbbreviation());
            DecimalFormat df = new DecimalFormat(".##");
//            disView.setText(String.valueOf(df.format(dis)) + " " + "公里");
////            if (dis > 50D) {
////                disView.setVisibility(View.GONE);
////                imgdis.setVisibility(View.GONE);
////            }
//            if (dis < 1) {
//                disView.setText(String.valueOf(df.format(dis * 1000)) + " " + "米");
//            }
        }
    }

    private void attendHospital(final HospitalBean hospitalBean, final int type) {
        setLoadingViewState(JDLoadingView.STATE_LOADING);
//        JDHttpClient.getInstance().reqAttendHospital(this, hospitalBean.getId(), type, new JDHttpResponseHandler<String>(new TypeReference<BaseBean<String>>() {
//        }) {
//            @Override
//            public void onRequestCallback(BaseBean<String> result) {
//                super.onRequestCallback(result);
//                dismissLoadingView();
//                if (result.isSuccess()) {
//                    UserBean userBean = application.userManager.getUserBean();
//                    if (type == 0) {
//                        userBean.getAttenHospitalList().remove(hospitalBean);
//                        showToast("取消关注成功");
//                    } else {
//                        userBean.getAttenHospitalList().add(hospitalBean);
//                        showToast("关注成功");
//                    }
//                    application.userManager.resetUser(userBean);
//                    if (adapter1 != null) {
//                        adapter1.notifyDataSetChanged();
//                        if(adapter != null) {
//                            adapter.notifyDataSetChanged();
//                        }
//                    }
//                } else {
//                    showToast(result.getMessage());
//                }
//            }
//        });
    }

    class TransYAnimation extends Animation {
        public static final int ACTION_UP = 0;
        public static final int ACTION_DOWN = 1;

        private View view;
        private int action;
        private float height;

        public TransYAnimation(View view, int action) {
            this.view = view;
            this.action = action;
            this.height = view.getMeasuredHeight();
        }

        @Override
        protected void applyTransformation(float interpolatedTime,
                                           Transformation t) {
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) view.getLayoutParams();
            if (action == ACTION_UP) {
                params.topMargin = (int) -(height * interpolatedTime);
            } else {
                params.topMargin = (int) -(height * (1 - interpolatedTime));
            }
            view.setLayoutParams(params);
        }
    }

    class ScaleXAnimation extends Animation {
        public static final int ACTION_PLUS = 0;
        public static final int ACTION_MINUS = 1;

        private View view;
        private int action;
        private float width;
        private float toWidth;

        public ScaleXAnimation(View view, int action, float toWidth) {
            this.view = view;
            this.action = action;
            this.width = view.getMeasuredWidth();
            this.toWidth = toWidth;
        }

        @Override
        protected void applyTransformation(float interpolatedTime,
                                           Transformation t) {
            ViewGroup.LayoutParams params = view.getLayoutParams();
            if (action == ACTION_PLUS) {
                params.width = (int) (toWidth * interpolatedTime);
            } else {
                params.width = (int) (width * (1 - interpolatedTime));
            }
            view.setLayoutParams(params);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!isForLogin) {
            isForLogin = getIntent().getBooleanExtra(KEY_IS_FOR_LOGIN, false);
            getMenuInflater().inflate(R.menu.menu_hospital_list, menu);
            menu.findItem(R.id.menu_skip).setVisible(false);
        }
        if (isForLogin) {
            getMenuInflater().inflate(R.menu.menu_hospital_list, menu);
        }
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.menu_skip) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

//    @Override
//    public void onUserChanged(UserBean userBean) {
//        if (userBean != null && adapter1 != null) {
//            adapter1.notifyDataSetChanged();
//        }
//    }
}
