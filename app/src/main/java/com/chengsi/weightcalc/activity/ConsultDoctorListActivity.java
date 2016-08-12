package com.chengsi.weightcalc.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Transformation;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.alibaba.fastjson.TypeReference;
import com.chengsi.weightcalc.R;
import com.chengsi.weightcalc.adapter.SocialStreamAdapter;
import com.chengsi.weightcalc.bean.BaseBean;
import com.chengsi.weightcalc.bean.ConsultDoctorList;
import com.chengsi.weightcalc.bean.DoctorBean;
import com.chengsi.weightcalc.http.JDHttpClient;
import com.chengsi.weightcalc.http.JDHttpResponseHandler;
import com.chengsi.weightcalc.utils.JDUtils;
import com.chengsi.weightcalc.utils.imageloader.ImageLoaderUtils;
import com.chengsi.weightcalc.widget.FanrRefreshListView;
import com.chengsi.weightcalc.widget.JDLoadingView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.InjectView;
import cn.jiadao.corelibs.utils.ListUtils;

public class ConsultDoctorListActivity extends BaseActivity implements TextWatcher {

    public static final String DOCTOR_ITEM_SELECTED = "DOCTOR_ITEM_SELECTED";

    @InjectView(R.id.listview)
    FanrRefreshListView listView;

    private View searchInputPanel;
    private View vSearchClear;
    private View vSearchClose;
    private EditText searchInput;
    private boolean isInputing;
    private String searchString = "";

    private ConsultDoctorList consultDoctorList;

    private SocialStreamAdapter mAdapter = null;
    private List<Map<String, Object>> dataSource = new ArrayList<>();
    private static final int[] RESOUCE = {R.layout.item_doctor_list, R.layout.item_doctor_header};
    private static final String[] ITEM_CONTENT_FROM = {ImageLoaderUtils.IMAGE_HEAD_PATH, "name", "duty", "hospital", "dutyVisible"};
    private static final int[] ITEM_CONTENT_TO = {R.id.iv_doctor_head, R.id.tv_doctor_name, R.id.tv_doctor_duty, R.id.tv_doctor_hospital, R.id.tv_doctor_duty};

    private static final String[] ITEM_HEADER_FROM = {"type"};
    private static final int[] ITEM_HEADER_TO = {R.id.tv_type_name};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consult_doctor_list);
        ButterKnife.inject(this);
        initViews();
        initDataSource();
    }

    private void initViews() {
        searchInputPanel = findViewById(R.id.search_input_panel);
        //搜索输入框
        searchInput = (EditText) findViewById(R.id.search_btn);
        searchInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isInputing) {
                    return;
                }
                isInputing = true;
                Animation animation = new TransYAnimation(toolbar, TransYAnimation.ACTION_UP);
                animation.setDuration(150);
                DecelerateInterpolator inter = new DecelerateInterpolator();
                animation.setInterpolator(inter);
                animation.setAnimationListener(new Animation.AnimationListener() {
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
                        scaleAnim.setAnimationListener(new Animation.AnimationListener() {
                            @Override
                            public void onAnimationStart(Animation animation) {
                            }

                            @Override
                            public void onAnimationRepeat(Animation animation) {
                            }

                            @Override
                            public void onAnimationEnd(Animation animation) {
                                searchInput.setFocusable(true);
                                searchInput.setFocusableInTouchMode(true);
                                JDUtils.showIME(ConsultDoctorListActivity.this, searchInput);
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
        vSearchClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchInput.setText("");//TODO
            }
        });

        //输入框左侧取消按钮
        vSearchClose = findViewById(R.id.search_close);
        vSearchClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isInputing) {
                    return;
                }
                JDUtils.hideIME(ConsultDoctorListActivity.this);
                isInputing = false;
                searchInputPanel.setBackgroundColor(getResources().getColor(R.color.default_bg));
                ScaleXAnimation scaleXAnim = new ScaleXAnimation(vSearchClose, ScaleXAnimation.ACTION_MINUS, 0);
                scaleXAnim.setDuration(100);
                DecelerateInterpolator inter = new DecelerateInterpolator();
                scaleXAnim.setInterpolator(inter);
                vSearchClose.startAnimation(scaleXAnim);
                scaleXAnim.setAnimationListener(new Animation.AnimationListener() {
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
                        transYAnim.setAnimationListener(new Animation.AnimationListener() {
                            @Override
                            public void onAnimationStart(Animation animation) {
                            }

                            @Override
                            public void onAnimationRepeat(Animation animation) {
                            }

                            @Override
                            public void onAnimationEnd(Animation animation) {
                                searchInput.setInputType(EditorInfo.TYPE_NULL);
                                //searchInput.setText("");
                                searchInput.clearFocus();
                            }
                        });
                    }
                });
            }
        });

        HashMap<Integer, String[]> fromMap = new HashMap<Integer, String[]>();
        fromMap.put(RESOUCE[0], ITEM_CONTENT_FROM);
        fromMap.put(RESOUCE[1], ITEM_HEADER_FROM);
        HashMap<Integer, int[]> toMap = new HashMap<Integer, int[]>();
        toMap.put(RESOUCE[0], ITEM_CONTENT_TO);
        toMap.put(RESOUCE[1], ITEM_HEADER_TO);
        mAdapter = new SocialStreamAdapter(this, dataSource, RESOUCE, fromMap, toMap, 0, 0, ImageLoaderUtils.headDisplayOpts);
        listView.setAdapter(mAdapter);
        listView.setCanLoadMore(false);
        listView.setOnPullRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                initDataSource();
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                DoctorBean doctorBean = (DoctorBean) dataSource.get(position).get("data");
                if (doctorBean != null){
                    Intent intent = new Intent();
                    intent.putExtra(DOCTOR_ITEM_SELECTED, doctorBean);
                    setResult(RESULT_OK, intent);
                    finish();
                }
            }
        });

    }

    @Override
    protected void initDataSource() {
        super.initDataSource();
        setLoadingViewState(JDLoadingView.STATE_LOADING);
        listView.setPullRefreshing(true);
        JDHttpClient.getInstance().reqConsultDoctorList(this, new JDHttpResponseHandler<ConsultDoctorList>(new TypeReference<BaseBean<ConsultDoctorList>>() {
        }) {

            @Override
            public void onRequestCallback(BaseBean<ConsultDoctorList> result) {
                super.onRequestCallback(result);
                listView.setPullRefreshing(false);
                dismissLoadingView();
                if (result.isSuccess()) {
                    consultDoctorList = result.getData();
                    parseData();
                } else {
                    setLoadingViewState(JDLoadingView.STATE_FAILED);
                    showToast(result.getMessage());
                }
            }
        });
    }

    private void parseData() {
        dataSource.clear();
        if (consultDoctorList != null){
//            if (application.userManager.getUserBean().getDoctorBean() != null){
//                addHeader("主治医生");
//                addDoctorToList(application.userManager.getUserBean().getDoctorBean());
//            }
            if(!ListUtils.isEmpty(consultDoctorList.getAttentList())){
                addHeader("我关注的医生");
                for (DoctorBean bean : consultDoctorList.getAttentList()){
                    addDoctorToList(bean);
                }
            }
            if(!ListUtils.isEmpty(consultDoctorList.getMyDoctorList())){
                addHeader("所在医院的医生");
                for (DoctorBean bean : consultDoctorList.getMyDoctorList()){
                    addDoctorToList(bean);
                }
            }
        }
        searchInput.setText("");
        mAdapter.notifyDataSetChanged();
    }

    private void addHeader(String headerName){
        Map<String, Object> headerMap = new HashMap<String, Object>();
        headerMap.put(ITEM_HEADER_FROM[0], headerName);
        headerMap.put(SocialStreamAdapter.CommonDataKey.ITEM_TYPE, 1);
        dataSource.add(headerMap);
    }

    private void addDoctorToList(DoctorBean bean){
        Map<String, Object> map = new HashMap<>();
        map.put(ITEM_CONTENT_FROM[0], JDUtils.getRemoteImagePath(bean.getHeadImg()));
        map.put(ITEM_CONTENT_FROM[1], bean.getRealName());
        map.put(ITEM_CONTENT_FROM[2], bean.getDuty());
        map.put(ITEM_CONTENT_FROM[3], bean.getBrief());
        if (TextUtils.isEmpty(bean.getDuty())){
            map.put(ITEM_CONTENT_FROM[4], false);
        }else{
            map.put(ITEM_CONTENT_FROM[4], false);
        }
        map.put(SocialStreamAdapter.CommonDataKey.ITEM_TYPE, 0);
        map.put("data", bean);
        dataSource.add(map);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        String text = searchInput.getText().toString().trim().toUpperCase(Locale.CHINA);

        if (text.equals(searchString)){
            return;
        }
        searchString = text;
        if (!TextUtils.isEmpty(searchString)) {
            vSearchClear.setVisibility(View.VISIBLE);
        } else {
            vSearchClear.setVisibility(View.GONE);
        }

        if (!TextUtils.isEmpty(searchString)){
            if(consultDoctorList != null){
                dataSource.clear();
                List<DoctorBean> list = consultDoctorList.getAllDoctorList();
                for (DoctorBean doctorBean : list){
                    if (doctorBean.getRealName().contains(searchString)){
                        addDoctorToList(doctorBean);
                    }else if(!TextUtils.isEmpty(doctorBean.getPinyin()) && doctorBean.getPinyin().toUpperCase().contains(searchString)){
                        addDoctorToList(doctorBean);
                    }
                }
                mAdapter.notifyDataSetChanged();
            }
        }else{
            parseData();
        }
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
}
