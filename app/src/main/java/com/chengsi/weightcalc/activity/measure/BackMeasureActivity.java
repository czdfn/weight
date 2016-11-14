package com.chengsi.weightcalc.activity.measure;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.EditorInfo;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.chengsi.weightcalc.activity.BaseActivity;
import com.chengsi.weightcalc.db.DBHelper;
import com.chengsi.weightcalc.utils.MeasureType;
import com.chengsi.weightcalc.R;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Map;
import java.util.regex.Pattern;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class BackMeasureActivity extends BaseActivity implements TextWatcher,TextView.OnEditorActionListener {

    @InjectView(R.id.et1)
    EditText editText1;
    @InjectView(R.id.et2)
    EditText editText2;
    @InjectView(R.id.et3)
    EditText editText3;
    @InjectView(R.id.et4)
    EditText editText4;
    @InjectView(R.id.et5)
    EditText editText5;
    @InjectView(R.id.et6)
    EditText editText6;
    @InjectView(R.id.et7)
    EditText editText7;
    @InjectView(R.id.et8)
    EditText editText8;
    @InjectView(R.id.et9)
    EditText editText9;
    @InjectView(R.id.et10)
    EditText editText10;
    @InjectView(R.id.et11)
    EditText editText11;
    @InjectView(R.id.et12)
    EditText editText12;
    @InjectView(R.id.et13)
    EditText editText13;
    @InjectView(R.id.et14)
    EditText editText14;
    @InjectView(R.id.et15)
    EditText editText15;
    @InjectView(R.id.et16)
    EditText editText16;
    @InjectView(R.id.et17)
    EditText editText17;
    @InjectView(R.id.et18)
    EditText editText18;
    @InjectView(R.id.et19)
    EditText editText19;
    @InjectView(R.id.et20)
    EditText editText20;
    @InjectView(R.id.et21)
    EditText editText21;
    @InjectView(R.id.et22)
    EditText editText22;
    @InjectView(R.id.et23)
    EditText editText23;
    @InjectView(R.id.et24)
    EditText editText24;
    @InjectView(R.id.et25)
    EditText editText25;
    @InjectView(R.id.et26)
    EditText editText26;
    @InjectView(R.id.et27)
    EditText editText27;

    @InjectView(R.id.et30)
    EditText editText30;
    @InjectView(R.id.et31)
    EditText editText31;
    @InjectView(R.id.et32)
    EditText editText32;
    @InjectView(R.id.et33)
    EditText editText33;
    @InjectView(R.id.et34)
    EditText editText34;
    @InjectView(R.id.et35)
    EditText editText35;
    @InjectView(R.id.et36)
    EditText editText36;
//    @InjectView(R.id.et37)
//    EditText editText37;
    @InjectView(R.id.et38)
    EditText editText38;
    @InjectView(R.id.et39)
    EditText editText39;
    @InjectView(R.id.et40)
    EditText editText40;
    @InjectView(R.id.tvv1)
    TextView textView1;
    @InjectView(R.id.tvv2)
    TextView textView2;
    @InjectView(R.id.tvv3)
    TextView textView3;
    @InjectView(R.id.tvv4)
    TextView textView4;
    @InjectView(R.id.tvv5)
    TextView textView5;
    @InjectView(R.id.tvv6)
    TextView textView6;
    @InjectView(R.id.tvv7)
    TextView textView7;
    @InjectView(R.id.tvv8)
    TextView textView8;
    @InjectView(R.id.tvv9)
    TextView textView9;
    @InjectView(R.id.tvv10)
    TextView textView10;
    @InjectView(R.id.tvv11)
    TextView textView11;
    @InjectView(R.id.tvv12)
    TextView textView12;
    @InjectView(R.id.tvv13)
    TextView textView13;
    @InjectView(R.id.tvv14)
    TextView textView14;
    @InjectView(R.id.tvv15)
    TextView textView15;
    @InjectView(R.id.tvv16)
    TextView textView16;
    @InjectView(R.id.tvv17)
    TextView textView17;
    @InjectView(R.id.tvv18)
    TextView textView18;
    @InjectView(R.id.tvv19)
    TextView textView19;
    @InjectView(R.id.tvv20)
    TextView textView20;
    @InjectView(R.id.tvv22)
    TextView textView22;
    @InjectView(R.id.tvv23)
    TextView textView23;
    @InjectView(R.id.tvv24)
    TextView textView24;
    @InjectView(R.id.tvv25)
    TextView textView25;
    @InjectView(R.id.tvv26)
    TextView textView26;
    @InjectView(R.id.tvv27)
    TextView textView27;
    @InjectView(R.id.tvv28)
    TextView textView28;
    @InjectView(R.id.tvv29)
    TextView textView29;
    @InjectView(R.id.checkBox)
    CheckBox checkbox;
    @InjectView(R.id.jdms_cb)
    CheckBox jdms_cb;
    @InjectView(R.id.sfdz_cb)
    CheckBox sfdz_cb;

    public static String DB_NAME = "weightdatabase.db";
    public static String MEASURE_TABLE = "weightcalc";
    public static int DB_VERSION = 1;
    public DBHelper dbHelper = new DBHelper(this, DB_NAME, null, DB_VERSION);
    //   定义结果变量
    private double average_front;
    private double average_mid;
    private double average_back;
    private double alter_front;
    private double alter_mid;
    private double alter_back;
    private double afteralter_front;
    private double afteralter_mid;
    private double afteralter_back;
    private double chishuicha_before;
    private double chishuicha_after;
    private double chaeshuichi;
    private double chaezhongliang;
    private double shijishuichi;
    private double shijipaishuizaizhong;
    private double zongqingliju;
    private double jiaozhi;
    private double alterpaishui;
    private double weight_before;
    private double weight_after;
    private double jianchuanyongwuliao_back;
    private double shijipaishuiliang_back;
    private double weight_package;
    private double jiaozhenghou_average;
    private double qingzaijidingliangbeiliao;
    private double jianqingzai_beiliao;
    private double qiancepsl;
    private double qiancecywl;

    //    定义输入参数变量
    private String ship_name;
    private String check_id;
    private String check_time;
    private String ceshishuichi_frontLeft;
    private String ceshishuichi_frontRight;
    private String ceshishuichi_midLeft;
    private String ceshishuichi_midRight;
    private String ceshishuichi_backLeft;
    private String ceshishuichi_backRight;
    private String biaojijuli_front;
    private String biaojijuli_mid;
    private String biaojijuli_back;
    private String ship_length;
    private String near_shuichi;
    private String near_weight;
    private String tpc;
    private String LCF;
    private String DZ;
    private String M1;
    private String M2;
    private String zy;
    private String qy;
    private String rhy;
    private String ds;
    private String ycs;
    private String bzmd;
    private String scmd;
    private String jinkouguobie;
    private String shouhuoqiye;
    private String kuangming;
    private String kuangshizhonglei;
    private String tihuoliang;
    private String paiwuliang;
    private String dianzichengliang;
    private String kongzhizongliang;
    private String qiancejiandinghuoliang;
    private String qiancepaishiuliang;
    private String qiancechuanyongwuliao;
    private String jizhongliang;

    private Pattern pattern = Pattern.compile("^[+-]?([1-9]\\d*|0)(\\.\\d{1,3})?$");
    private int data_id;
    private int check_type;
    private Map<String, String> mData;
    Handler mQueryHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
//            Log.i("TAG", mData.toString());
            if (mData == null) {
                onknow();
            } else {
                initView();
            }
        }
    };
    Runnable mQuery = new Runnable() {
        @Override
        public void run() {
            query();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_back_measure);
        ButterKnife.inject(this);
        Intent intent = getIntent();
        if (intent.getIntExtra("type", MeasureType.MEASURE_DEFAUL) == MeasureType.MEASURE_CHANGE) {
            data_id = Integer.valueOf(intent.getStringExtra("id"));
            check_type = intent.getIntExtra("check_type", 0);
            new Thread(mQuery).start();
        }
//        initView();
        setMyTitle("后测");
        editText3.setText(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(System.currentTimeMillis()));
        editText4.addTextChangedListener(this);
        editText5.addTextChangedListener(this);
        editText6.addTextChangedListener(this);
        editText7.addTextChangedListener(this);
        editText8.addTextChangedListener(this);
        editText9.addTextChangedListener(this);
        editText10.addTextChangedListener(this);
        editText11.addTextChangedListener(this);
        editText12.addTextChangedListener(this);
        editText13.addTextChangedListener(this);
        editText14.addTextChangedListener(this);
        editText1.setOnEditorActionListener(this);
        editText2.setOnEditorActionListener(this);
        editText3.setOnEditorActionListener(this);
        editText4.setOnEditorActionListener(this);
        editText5.setOnEditorActionListener(this);
        editText6.setOnEditorActionListener(this);
        editText7.setOnEditorActionListener(this);
        editText8.setOnEditorActionListener(this);
        editText9.setOnEditorActionListener(this);
        editText10.setOnEditorActionListener(this);
        editText11.setOnEditorActionListener(this);
        editText12.setOnEditorActionListener(this);
        editText13.setOnEditorActionListener(this);
        editText14.setOnEditorActionListener(this);
        editText15.setOnEditorActionListener(this);
        editText16.setOnEditorActionListener(this);
        editText17.setOnEditorActionListener(this);
        editText18.setOnEditorActionListener(this);
        editText19.setOnEditorActionListener(this);
        editText20.setOnEditorActionListener(this);
        editText21.setOnEditorActionListener(this);
        editText22.setOnEditorActionListener(this);
        editText23.setOnEditorActionListener(this);
        editText24.setOnEditorActionListener(this);
        editText25.setOnEditorActionListener(this);
        editText26.setOnEditorActionListener(this);
        editText27.setOnEditorActionListener(this);
        editText30.setOnEditorActionListener(this);
        editText31.setOnEditorActionListener(this);
        editText32.setOnEditorActionListener(this);
        editText33.setOnEditorActionListener(this);
        editText34.setOnEditorActionListener(this);
        editText35.setOnEditorActionListener(this);
        editText36.setOnEditorActionListener(this);
//        editText37.setOnEditorActionListener(this);
        editText38.setOnEditorActionListener(this);
        editText39.setOnEditorActionListener(this);
        editText40.setOnEditorActionListener(this);
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        doWhichOperation(actionId);
        Log.e("BALLACK", "event: " + event);
        Log.e("BALLACK", "v.getImeActionId(): " + v.getImeActionId());
        Log.e("BALLACK", "v.getImeOptions(): " + v.getImeOptions());
        Log.e("BALLACK", "----------------------------------------------");
        if(actionId == EditorInfo.IME_ACTION_NEXT){

        }
        return true;
    }
    private void doWhichOperation(int actionId) {
        switch (actionId) {
            case EditorInfo.IME_ACTION_DONE:
                Log.e("BALLACK", "IME_ACTION_DONE");
                break;
            case EditorInfo.IME_ACTION_GO:
                Log.e("BALLACK", "IME_ACTION_GO");
                break;
            case EditorInfo.IME_ACTION_NEXT:
                Integer viewId = getCurrentFocus().getId();
                switch (viewId){
                    case R.id.et1:
                        editText2.setFocusable(true);
                        editText2.setFocusableInTouchMode(true);
                        editText2.requestFocus();
                        break;
                    case R.id.et2:
                        editText3.setFocusable(true);
                        editText3.setFocusableInTouchMode(true);
                        editText3.requestFocus();
                        break;
                    case R.id.et3:
                        editText30.setFocusable(true);
                        editText30.setFocusableInTouchMode(true);
                        editText30.requestFocus();
                        break;
                    case R.id.et30:
                        editText31.setFocusable(true);
                        editText31.setFocusableInTouchMode(true);
                        editText31.requestFocus();
                        break;
                    case R.id.et31:
                        editText32.setFocusable(true);
                        editText32.setFocusableInTouchMode(true);
                        editText32.requestFocus();
                        break;
                    case R.id.et32:
                        editText33.setFocusable(true);
                        editText33.setFocusableInTouchMode(true);
                        editText33.requestFocus();
                        break;
                    case R.id.et33:
                        editText34.setFocusable(true);
                        editText34.setFocusableInTouchMode(true);
                        editText34.requestFocus();
                        break;
                    case R.id.et34:
                        editText35.setFocusable(true);
                        editText35.setFocusableInTouchMode(true);
                        editText35.requestFocus();
                        break;
                    case R.id.et35:
                        editText36.setFocusable(true);
                        editText36.setFocusableInTouchMode(true);
                        editText36.requestFocus();
                        break;
                    case R.id.et36:
                        editText38.setFocusable(true);
                        editText38.setFocusableInTouchMode(true);
                        editText38.requestFocus();
                        break;
//                    case R.id.et37:
//                        editText38.setFocusable(true);
//                        editText38.setFocusableInTouchMode(true);
//                        editText38.requestFocus();
//                        break;
                    case R.id.et38:
                        editText39.setFocusable(true);
                        editText39.setFocusableInTouchMode(true);
                        editText39.requestFocus();
                        break;
                    case R.id.et39:
                        editText40.setFocusable(true);
                        editText40.setFocusableInTouchMode(true);
                        editText40.requestFocus();
                        break;
                    case R.id.et40:
                        editText4.setFocusable(true);
                        editText4.setFocusableInTouchMode(true);
                        editText4.requestFocus();
                        break;
                    case R.id.et4:
                        editText5.setFocusable(true);
                        editText5.setFocusableInTouchMode(true);
                        editText5.requestFocus();
                        break;
                    case R.id.et5:
                        editText6.setFocusable(true);
                        editText6.setFocusableInTouchMode(true);
                        editText6.requestFocus();
                        break;
                    case R.id.et6:
                        editText7.setFocusable(true);
                        editText7.setFocusableInTouchMode(true);
                        editText7.requestFocus();
                        break;
                    case R.id.et7:
                        editText8.setFocusable(true);
                        editText8.setFocusableInTouchMode(true);
                        editText8.requestFocus();
                        break;
                    case R.id.et8:
                        editText9.setFocusable(true);
                        editText9.setFocusableInTouchMode(true);
                        editText9.requestFocus();
                        break;
                    case R.id.et9:
                        editText10.setFocusable(true);
                        editText10.setFocusableInTouchMode(true);
                        editText10.requestFocus();
                        break;
                    case R.id.et10:
                        editText11.setFocusable(true);
                        editText11.setFocusableInTouchMode(true);
                        editText11.requestFocus();
                        break;
                    case R.id.et11:
                        editText12.setFocusable(true);
                        editText12.setFocusableInTouchMode(true);
                        editText12.requestFocus();
                        break;
                    case R.id.et12:
                        editText13.setFocusable(true);
                        editText13.setFocusableInTouchMode(true);
                        editText13.requestFocus();
                        break;
                    case R.id.et13:
                        editText14.setFocusable(true);
                        editText14.setFocusableInTouchMode(true);
                        editText14.requestFocus();
                        break;
                    case R.id.et14:
                        editText15.setFocusable(true);
                        editText15.setFocusableInTouchMode(true);
                        editText15.requestFocus();
                        break;
                    case R.id.et15:
                        editText16.setFocusable(true);
                        editText16.setFocusableInTouchMode(true);
                        editText16.requestFocus();
                        break;
                    case R.id.et16:
                        editText17.setFocusable(true);
                        editText17.setFocusableInTouchMode(true);
                        editText17.requestFocus();
                        break;
                    case R.id.et17:
                        editText18.setFocusable(true);
                        editText18.setFocusableInTouchMode(true);
                        editText18.requestFocus();
                        break;
                    case R.id.et18:
                        editText19.setFocusable(true);
                        editText19.setFocusableInTouchMode(true);
                        editText19.requestFocus();
                        break;
                    case R.id.et19:
                        editText20.setFocusable(true);
                        editText20.setFocusableInTouchMode(true);
                        editText20.requestFocus();
                        break;
                    case R.id.et20:
                        editText21.setFocusable(true);
                        editText21.setFocusableInTouchMode(true);
                        editText21.requestFocus();
                        break;
                    case R.id.et21:
                        editText22.setFocusable(true);
                        editText22.setFocusableInTouchMode(true);
                        editText22.requestFocus();
                        break;
                    case R.id.et22:
                        editText23.setFocusable(true);
                        editText23.setFocusableInTouchMode(true);
                        editText23.requestFocus();
                        break;
                    case R.id.et23:
                        editText24.setFocusable(true);
                        editText24.setFocusableInTouchMode(true);
                        editText24.requestFocus();
                        break;
                    case R.id.et24:
                        editText25.setFocusable(true);
                        editText25.setFocusableInTouchMode(true);
                        editText25.requestFocus();
                        break;
                    case R.id.et25:
                        editText26.setFocusable(true);
                        editText26.setFocusableInTouchMode(true);
                        editText26.requestFocus();
                        break;
                    case R.id.et26:
                        editText27.setFocusable(true);
                        editText27.setFocusableInTouchMode(true);
                        editText27.requestFocus();
                        break;
                }
                break;
            case EditorInfo.IME_ACTION_NONE:
                Log.e("BALLACK", "IME_ACTION_NONE");
                break;
            case EditorInfo.IME_ACTION_PREVIOUS:
                Log.e("BALLACK", "IME_ACTION_PREVIOUS");
                break;
            case EditorInfo.IME_ACTION_SEARCH:
                Log.e("BALLACK", "IME_ACTION_SEARCH");
                break;
            case EditorInfo.IME_ACTION_SEND:
                Log.e("BALLACK", "IME_ACTION_SEND");
                break;
            case EditorInfo.IME_ACTION_UNSPECIFIED:
                Log.e("BALLACK", "IME_ACTION_UNSPECIFIED");
                break;
            default:
                break;
        }
    }

    private void query() {
        mData = dbHelper.queryData(data_id, check_type);

        mQueryHandler.sendMessage(new Message());
    }

    public void initView() {
        editText1.setText(mData.get("ship_name"));
        editText2.setText(mData.get("check_id"));
        editText3.setText(mData.get("check_time"));
        editText4.setText(mData.get("ceshishuichi_frontLeft"));
        editText5.setText(mData.get("ceshishuichi_frontRight"));
        editText6.setText(mData.get("ceshishuichi_midLeft"));
        editText7.setText(mData.get("ceshishuichi_midRight"));
        editText8.setText(mData.get("ceshishuichi_backLeft"));
        editText9.setText(mData.get("ceshishuichi_backRight"));
        editText10.setText(mData.get("biaojijuli_front"));
        editText11.setText(mData.get("biaojijuli_mid"));
        editText12.setText(mData.get("biaojijuli_back"));
        editText13.setText(mData.get("ship_length"));
        editText14.setText(mData.get("near_shuichi"));
        editText15.setText(mData.get("near_weight"));
        editText16.setText(mData.get("tpc"));
        if (mData.get("check_status").equals("1")) {
            checkbox.setChecked(true);
            editText17.setText(mData.get("LCF"));
            editText18.setText(mData.get("DZ"));
            editText19.setText(mData.get("M1"));
            editText20.setText(mData.get("M2"));
        }
        editText21.setText(mData.get("zy"));
        editText22.setText(mData.get("qy"));
        editText23.setText(mData.get("rhy"));
        editText24.setText(mData.get("ds"));
        editText25.setText(mData.get("ycs"));
        editText26.setText(mData.get("bzmd"));
        editText27.setText(mData.get("scmd"));
        editText30.setText(mData.get("jinkouguobie"));
        editText31.setText(mData.get("shouhuoqiye"));
        editText32.setText(mData.get("kuangming"));
        editText33.setText(mData.get("kuangshizhonglei"));
        editText34.setText(mData.get("tihuoliang"));
        editText35.setText(mData.get("paiwuliang"));
        editText36.setText(mData.get("dianzichengliang"));
//        editText37.setText(mData.get("kongzhizongliang"));
        editText38.setText(mData.get("qiancejiandinghuoliang"));
        editText39.setText(mData.get("qiancepaishiuliang"));
        editText40.setText(mData.get("qiancechuanyongwuliao"));
        textView1.setText(mData.get("average_front"));
        textView2.setText(mData.get("average_mid"));
        textView3.setText(mData.get("average_back"));
        textView4.setText(mData.get("alter_front"));
        textView5.setText(mData.get("alter_mid"));
        textView6.setText(mData.get("alter_back"));
        textView7.setText(mData.get("afteralter_front"));
        textView8.setText(mData.get("afteralter_mid"));
        textView9.setText(mData.get("afteralter_back"));
        textView10.setText(mData.get("chishuicha_before"));
        textView11.setText(mData.get("chishuicha_after"));
        textView12.setText(mData.get("chaeshuichi"));
        textView13.setText(mData.get("chaezhongliang"));
        textView14.setText(mData.get("shijishuichi"));
        textView15.setText(mData.get("shijipaishuizaizhong"));
        textView16.setText(mData.get("zongqingliju"));
        textView17.setText(mData.get("jiaozhi"));
        textView18.setText(mData.get("weight_before"));
        textView19.setText(mData.get("alterpaishui"));
        textView20.setText(mData.get("weight_after"));
        textView22.setText(mData.get("weight_package"));
        textView23.setText(mData.get("jiaozhenghou_average"));
        textView24.setText(mData.get("shijipaishuiliang_back"));
        textView25.setText(mData.get("jianchuanyongwuliao_back"));
        textView26.setText(mData.get("qingzaijidingliangbeiliao"));
        textView27.setText(mData.get("qingzaijidingliangbeiliao"));
        textView28.setText(mData.get("qiancepsl"));
        textView29.setText(mData.get("qiancecywl"));
        jdms_cb.setChecked(Boolean.valueOf(mData.get("jiandingmoshi")));
        sfdz_cb.setChecked(Boolean.valueOf(mData.get("shifouduanzhong")));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_front_measure, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_calc) {
            calc();
            return true;

        } else if (item.getItemId() == R.id.action_save) {
            save(getIntent().getIntExtra("type", MeasureType.MEASURE_DEFAUL));
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public boolean initData() {
        if (!pattern.matcher(editText4.getText().toString()).matches()
                || !pattern.matcher(editText5.getText().toString()).matches() || !pattern.matcher(editText6.getText().toString()).matches()
                || !pattern.matcher(editText7.getText().toString()).matches() || !pattern.matcher(editText8.getText().toString()).matches()
                || !pattern.matcher(editText9.getText().toString()).matches() || !pattern.matcher(editText10.getText().toString()).matches()
                || !pattern.matcher(editText11.getText().toString()).matches() || !pattern.matcher(editText12.getText().toString()).matches()
                || !pattern.matcher(editText13.getText().toString()).matches()) {
            Toast.makeText(this, "请输入正确的数字", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            ship_name = editText1.getText().toString().trim();
            check_id = editText2.getText().toString().trim();
            check_time = editText3.getText().toString().trim();
            jinkouguobie = editText30.getText().toString().trim();
            shouhuoqiye = editText31.getText().toString().trim();
            kuangming = editText32.getText().toString().trim();
            kuangshizhonglei = editText33.getText().toString().trim();
            tihuoliang = new DecimalFormat("0.000").format(Double.valueOf(editText34.getText().toString().trim().equals("") ? 0 : Double.valueOf(editText34.getText().toString().trim())));
            paiwuliang = new DecimalFormat("0.000").format(Double.valueOf(editText35.getText().toString().trim().equals("") ? 0 : Double.valueOf(editText35.getText().toString().trim())));
            dianzichengliang = new DecimalFormat("0.000").format(Double.valueOf(editText36.getText().toString().trim().equals("") ? 0 : Double.valueOf(editText36.getText().toString().trim())));
//            kongzhizongliang = new DecimalFormat("0.000").format(Double.valueOf(editText37.getText().toString().trim().equals("") ? 0 : Double.valueOf(editText37.getText().toString().trim())));
            qiancejiandinghuoliang = new DecimalFormat("0.000").format(Double.valueOf(editText38.getText().toString().trim().equals("") ? 0 : Double.valueOf(editText38.getText().toString().trim())));
            qiancepaishiuliang = new DecimalFormat("0.000").format(Double.valueOf(editText39.getText().toString().trim().equals("") ? 0 : Double.valueOf(editText39.getText().toString().trim())));
            qiancechuanyongwuliao = new DecimalFormat("0.000").format(Double.valueOf(editText40.getText().toString().trim().equals("") ? 0 : Double.valueOf(editText40.getText().toString().trim())));
            ceshishuichi_frontLeft = new DecimalFormat("0.00").format(Double.valueOf(editText4.getText().toString().trim()));
            ceshishuichi_frontRight = new DecimalFormat("0.00").format(Double.valueOf(editText5.getText().toString().trim()));
            ceshishuichi_midLeft = new DecimalFormat("0.00").format(Double.valueOf(editText6.getText().toString().trim()));
            ceshishuichi_midRight = new DecimalFormat("0.00").format(Double.valueOf(editText7.getText().toString().trim()));
            ceshishuichi_backLeft = new DecimalFormat("0.00").format(Double.valueOf(editText8.getText().toString().trim()));
            ceshishuichi_backRight = new DecimalFormat("0.00").format(Double.valueOf(editText9.getText().toString().trim()));
            biaojijuli_front = new DecimalFormat("0.000").format(Double.valueOf(editText10.getText().toString().trim()));
            biaojijuli_mid = new DecimalFormat("0.000").format(Double.valueOf(editText11.getText().toString().trim()));
            biaojijuli_back = new DecimalFormat("0.000").format(Double.valueOf(editText12.getText().toString().trim()));
            ship_length = new DecimalFormat("0.000").format(Double.valueOf(editText13.getText().toString().trim()));
            near_shuichi = new DecimalFormat("0.000").format(Double.valueOf(editText14.getText().toString().trim().equals("") ? 0 : Double.valueOf(editText14.getText().toString().trim())));
            near_weight = new DecimalFormat("0.0").format(Double.valueOf(editText15.getText().toString().trim().equals("") ? 0 : Double.valueOf(editText15.getText().toString().trim())));
            tpc = new DecimalFormat("0.000").format(Double.valueOf(editText16.getText().toString().trim().equals("") ? 0 : Double.valueOf(editText16.getText().toString().trim())));
            LCF = new DecimalFormat("0.000").format(Double.valueOf(editText17.getText().toString().trim().equals("") ? 0 : Double.valueOf(editText17.getText().toString().trim())));
            DZ = new DecimalFormat("0.000").format(Double.valueOf(editText18.getText().toString().trim().equals("") ? 0 : Double.valueOf(editText18.getText().toString().trim())));
            M1 = new DecimalFormat("0.000").format(Double.valueOf(editText19.getText().toString().trim().equals("") ? 0 : Double.valueOf(editText19.getText().toString().trim())));
            M2 = new DecimalFormat("0.000").format(Double.valueOf(editText20.getText().toString().trim().equals("") ? 0 : Double.valueOf(editText20.getText().toString().trim())));
            zy = new DecimalFormat("0.0").format(Double.valueOf(editText21.getText().toString().trim().equals("") ? 0 : Double.valueOf(editText21.getText().toString().trim())));
            qy = new DecimalFormat("0.0").format(Double.valueOf(editText22.getText().toString().trim().equals("") ? 0 : Double.valueOf(editText22.getText().toString().trim())));
            rhy = new DecimalFormat("0.0").format(Double.valueOf(editText23.getText().toString().trim().equals("") ? 0 : Double.valueOf(editText23.getText().toString().trim())));
            ds = new DecimalFormat("0.0").format(Double.valueOf(editText24.getText().toString().trim().equals("") ? 0 : Double.valueOf(editText24.getText().toString().trim())));
            ycs = new DecimalFormat("0.0").format(Double.valueOf(editText25.getText().toString().trim().equals("") ? 0 : Double.valueOf(editText25.getText().toString().trim())));
            bzmd = new DecimalFormat("0.0000").format(Double.valueOf(editText26.getText().toString().trim().equals("") ? 0 : Double.valueOf(editText26.getText().toString().trim())));
            scmd = new DecimalFormat("0.0000").format(Double.valueOf(editText27.getText().toString().trim().equals("") ? 0 : Double.valueOf(editText27.getText().toString().trim())));
//            editText4.setText(ceshishuichi_frontLeft);
//            editText5.setText(ceshishuichi_frontRight);
//            editText6.setText(ceshishuichi_midLeft);
//            editText7.setText(ceshishuichi_midRight);
//            editText8.setText(ceshishuichi_backLeft);
//            editText9.setText(ceshishuichi_backRight);
//            editText10.setText(biaojijuli_front);
//            editText11.setText(biaojijuli_mid);
//            editText12.setText(biaojijuli_back);
//            editText13.setText(ship_length);
//            editText14.setText(near_shuichi);
//            editText15.setText(near_weight);
//            editText16.setText(tpc);
//            editText17.setText(LCF);
//            editText18.setText(DZ);
//            editText19.setText(M1);
//            editText20.setText(M2);
//            editText21.setText(zy);
//            editText22.setText(qy);
//            editText23.setText(rhy);
//            editText24.setText(ds);
//            editText25.setText(ycs);
//            editText26.setText(bzmd);
//            editText27.setText(scmd);
//            editText30.setText(jinkouguobie);
//            editText31.setText(shouhuoqiye);
//            editText32.setText(kuangming);
//            editText33.setText(kuangshizhonglei);
//            editText34.setText(tihuoliang);
//            editText35.setText(paiwuliang);
//            editText36.setText(dianzichengliang);
//            editText37.setText(kongzhizongliang);
//            editText38.setText(qiancejiandinghuoliang);
//            editText39.setText(qiancepaishiuliang);
//            editText40.setText(qiancechuanyongwuliao);
            average_front = (Double.valueOf(ceshishuichi_frontLeft) + Double.valueOf(ceshishuichi_frontRight)) / 2;
            average_mid = (Double.valueOf(ceshishuichi_midLeft) + Double.valueOf(ceshishuichi_midRight)) / 2;
            average_back = (Double.valueOf(ceshishuichi_backLeft) + Double.valueOf(ceshishuichi_backRight)) / 2;
            chishuicha_before = average_back - average_front;
            alter_front = chishuicha_before * Double.valueOf(biaojijuli_front) / (Double.valueOf(ship_length) + Double.valueOf(biaojijuli_front) - Double.valueOf(biaojijuli_back));
            alter_mid = chishuicha_before * Double.valueOf(biaojijuli_mid) / (Double.valueOf(ship_length) + Double.valueOf(biaojijuli_front) - Double.valueOf(biaojijuli_back));
            alter_back = chishuicha_before * Double.valueOf(biaojijuli_back) / (Double.valueOf(ship_length) + Double.valueOf(biaojijuli_front) - Double.valueOf(biaojijuli_back));
            afteralter_front = average_front + alter_front;
            afteralter_mid = average_mid + alter_mid;
            afteralter_back = average_back + alter_back;
            chishuicha_after = Double.valueOf(new DecimalFormat("0.000").format(afteralter_back - afteralter_front));
            jiaozhenghou_average = (afteralter_back + afteralter_front + 6 * afteralter_mid) / 8;
            chaeshuichi = (jiaozhenghou_average - Double.valueOf(near_shuichi)) * 100;
            chaezhongliang = new BigDecimal(chaeshuichi).setScale(1,BigDecimal.ROUND_HALF_UP).doubleValue()* Double.valueOf(tpc);
            shijishuichi = jiaozhenghou_average;
            shijipaishuizaizhong = Double.valueOf(near_weight) + Double.valueOf(new DecimalFormat("0.0").format(chaezhongliang));
            jianchuanyongwuliao_back = Double.valueOf(zy) + Double.valueOf(qy) + Double.valueOf(rhy) + Double.valueOf(ds) + Double.valueOf(ycs);
            if (checkbox.isChecked()) {
                zongqingliju = (Double.valueOf(M2) - Double.valueOf(M1)) / Double.valueOf(DZ);
                jiaozhi = (100 * chishuicha_after * Double.valueOf(tpc) * Double.valueOf(LCF) + 50 * chishuicha_after * chishuicha_after * zongqingliju) / Double.valueOf(ship_length);
            } else {
                zongqingliju = 0;
                jiaozhi = 0;
            }
            alterpaishui = shijipaishuizaizhong + jiaozhi;
            weight_before = shijipaishuizaizhong + jiaozhi;
            weight_after = (shijipaishuizaizhong + jiaozhi) * Double.valueOf(scmd) / Double.valueOf(bzmd);
            shijipaishuiliang_back = weight_after;
            qingzaijidingliangbeiliao = shijipaishuiliang_back - jianchuanyongwuliao_back;
            jianqingzai_beiliao = qingzaijidingliangbeiliao;
            qiancepsl = Double.valueOf(qiancepaishiuliang);
            qiancecywl = Double.valueOf(qiancechuanyongwuliao);
            weight_package = Double.valueOf(qiancepaishiuliang) - Double.valueOf(qiancechuanyongwuliao) - jianqingzai_beiliao;
        }
        return true;
    }

    private void save(int type) {
        if (initData()) {
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            ContentValues cv = new ContentValues();
            cv.put("ship_name", ship_name);
            cv.put("check_id", check_id);
            cv.put("check_time", check_time);
            cv.put("jinkouguobie", jinkouguobie);
            cv.put("shouhuoqiye", shouhuoqiye);
            cv.put("kuangming", kuangming);
            cv.put("kuangshizhonglei", kuangshizhonglei);
            cv.put("jizhongliang", jizhongliang);
            cv.put("tihuoliang", tihuoliang);
            cv.put("paiwuliang", paiwuliang);
            cv.put("dianzichengliang", dianzichengliang);
            cv.put("kongzhizongliang", kongzhizongliang);
            cv.put("qiancejiandinghuoliang", qiancejiandinghuoliang);
            cv.put("qiancepaishiuliang", qiancepaishiuliang);
            cv.put("qiancechuanyongwuliao", qiancechuanyongwuliao);
            cv.put("ceshishuichi_frontLeft", ceshishuichi_frontLeft);
            cv.put("ceshishuichi_frontRight", ceshishuichi_frontRight);
            cv.put("ceshishuichi_midLeft", ceshishuichi_midLeft);
            cv.put("ceshishuichi_midRight", ceshishuichi_midRight);
            cv.put("ceshishuichi_backLeft", ceshishuichi_backLeft);
            cv.put("ceshishuichi_backRight", ceshishuichi_backRight);
            cv.put("biaojijuli_front", biaojijuli_front);
            cv.put("biaojijuli_mid", biaojijuli_mid);
            cv.put("biaojijuli_back", biaojijuli_back);
            cv.put("ship_length", ship_length);
            cv.put("near_shuichi", near_shuichi);
            cv.put("near_weight", near_weight);
            cv.put("tpc", tpc);
            cv.put("LCF", LCF);
            cv.put("DZ", DZ);
            cv.put("M1", M1);
            cv.put("M2", M2);
            cv.put("zy", zy);
            cv.put("qy", qy);
            cv.put("rhy", rhy);
            cv.put("ds", ds);
            cv.put("ycs", ycs);
            cv.put("bzmd", bzmd);
            cv.put("scmd", scmd);
//            cv.put("average_front", new DecimalFormat("0.000").format(average_front));
//            cv.put("average_mid", new DecimalFormat("0.000").format(average_mid));
//            cv.put("average_back", new DecimalFormat("0.000").format(average_back));
//            cv.put("chishuicha_before", new DecimalFormat("0.000").format(chishuicha_before));
//            cv.put("alter_front", new DecimalFormat("0.000").format(alter_front));
//            cv.put("alter_mid", new DecimalFormat("0.000").format(alter_mid));
//            cv.put("alter_back", new DecimalFormat("0.000").format(alter_back));
//            cv.put("afteralter_front", new DecimalFormat("0.000").format(afteralter_front));
//            cv.put("afteralter_mid", new DecimalFormat("0.000").format(afteralter_mid));
//            cv.put("afteralter_back", new DecimalFormat("0.000").format(afteralter_back));
//            cv.put("chishuicha_after", new DecimalFormat("0.000").format(chishuicha_after));
//            cv.put("jiaozhenghou_average", new DecimalFormat("0.000").format(jiaozhenghou_average));
//            cv.put("chaeshuichi", new DecimalFormat("0.000").format(chaeshuichi));
//            cv.put("chaezhongliang", new DecimalFormat("0.000").format(chaezhongliang));
//            cv.put("shijishuichi", new DecimalFormat("0.000").format(shijishuichi));
//            cv.put("shijipaishuizaizhong", new DecimalFormat("0.000").format(shijipaishuizaizhong));
//            cv.put("jianchuanyongwuliao", new DecimalFormat("0.000").format(jianchuanyongwuliao_back));
//            cv.put("shijipaishuiliang", new DecimalFormat("0.000").format(shijipaishuiliang_back));
//            cv.put("qingzaijidingliangbeiliang", new DecimalFormat("0.000").format(qingzaijidingliangbeiliao));
//            cv.put("zongqingliju", new DecimalFormat("0.000").format(zongqingliju));
//            cv.put("jiaozhi", new DecimalFormat("0.000").format(jiaozhi));
//            cv.put("alterpaishui", new DecimalFormat("0.000").format(alterpaishui));
//            cv.put("weight_before", new DecimalFormat("0.000").format(weight_before));
//            cv.put("weight_after", new DecimalFormat("0.000").format(weight_after));
//            cv.put("weight_package", new DecimalFormat("0.000").format(weight_package));
            cv.put("check_type", 3);

            if (checkbox.isChecked()) {
                cv.put("check_status", true);
            } else {
                cv.put("check_status", false);
            }
            if (jdms_cb.isChecked()) {
                cv.put("jiandingmoshi", true);
            } else {
                cv.put("jiandingmoshi", false);
            }
            if (sfdz_cb.isChecked()) {
                cv.put("shifouduanzhong", true);
            } else {
                cv.put("shifouduanzhong", false);
            }

            if (type == MeasureType.MEASURE_DEFAUL) {
                db.insert("weightcalc", "", cv);
            } else {
                cv.put("_id", data_id);
                db.delete("weightcalc", "_id=?", new String[]{data_id + ""});
                db.insert("weightcalc", "", cv);
                setResult(MeasureType.MEASURE_CHANGE);
            }
            db.close();
        }
    }

    private void calc() {
        if (initData()) {
            textView1.setText(String.valueOf(new DecimalFormat("0.000").format(average_front)));
            textView2.setText(String.valueOf(new DecimalFormat("0.000").format(average_mid)));
            textView3.setText(String.valueOf(new DecimalFormat("0.000").format(average_back)));
            textView10.setText(String.valueOf(new DecimalFormat("0.000").format(chishuicha_before)));
            textView4.setText(String.valueOf(new DecimalFormat("0.000").format(alter_front)));
            textView5.setText(String.valueOf(new DecimalFormat("0.000").format(alter_mid)));
            textView6.setText(String.valueOf(new DecimalFormat("0.000").format(alter_back)));
            textView7.setText(String.valueOf(new DecimalFormat("0.000").format(afteralter_front)));
            textView8.setText(String.valueOf(new DecimalFormat("0.000").format(afteralter_mid)));
            textView9.setText(String.valueOf(new DecimalFormat("0.000").format(afteralter_back)));
            textView11.setText(String.valueOf(new DecimalFormat("0.000").format(chishuicha_after)));
            textView23.setText(String.valueOf(new DecimalFormat("0.000").format(jiaozhenghou_average)));
            textView12.setText(String.valueOf(new DecimalFormat("0.0").format(chaeshuichi)));
            textView13.setText(String.valueOf(new DecimalFormat("0.0").format(chaezhongliang)));
            textView14.setText(String.valueOf(new DecimalFormat("0.000").format(shijishuichi)));
            textView15.setText(String.valueOf(new DecimalFormat("0.0").format(shijipaishuizaizhong)));
            textView16.setText(String.valueOf(new DecimalFormat("0.000").format(zongqingliju)));
            textView17.setText(String.valueOf(new DecimalFormat("0.0").format(jiaozhi)));
            textView19.setText(String.valueOf(new DecimalFormat("0.0").format(alterpaishui)));
            textView18.setText(String.valueOf(new DecimalFormat("0.0").format(weight_before)));
            textView20.setText(String.valueOf(new DecimalFormat("0.0").format(weight_after)));
            textView22.setText(String.valueOf(new DecimalFormat("0").format(weight_package)));
            textView24.setText(String.valueOf(new DecimalFormat("0.0").format(shijipaishuiliang_back)));
            textView25.setText(String.valueOf(new DecimalFormat("0.0").format(jianchuanyongwuliao_back)));
            textView26.setText(String.valueOf(new DecimalFormat("0.0").format(qingzaijidingliangbeiliao)));
            textView27.setText(String.valueOf(new DecimalFormat("0.0").format(qingzaijidingliangbeiliao)));
            textView28.setText(String.valueOf(new DecimalFormat("0.0").format(qiancepsl)));
            textView29.setText(String.valueOf(new DecimalFormat("0.0").format(qiancecywl)));
            editText4.setText(ceshishuichi_frontLeft);
            editText5.setText(ceshishuichi_frontRight);
            editText6.setText(ceshishuichi_midLeft);
            editText7.setText(ceshishuichi_midRight);
            editText8.setText(ceshishuichi_backLeft);
            editText9.setText(ceshishuichi_backRight);
            editText10.setText(biaojijuli_front);
            editText11.setText(biaojijuli_mid);
            editText12.setText(biaojijuli_back);
            editText13.setText(ship_length);
//            editText14.setText(near_shuichi);
//            editText15.setText(near_weight);
//            editText16.setText(tpc);
//            editText17.setText(LCF);
//            editText18.setText(DZ);
//            editText19.setText(M1);
//            editText20.setText(M2);
//            editText21.setText(zy);
//            editText22.setText(qy);
//            editText23.setText(rhy);
//            editText24.setText(ds);
//            editText25.setText(ycs);
//            editText26.setText(bzmd);
//            editText27.setText(scmd);
            editText30.setText(jinkouguobie);
            editText31.setText(shouhuoqiye);
            editText32.setText(kuangming);
            editText33.setText(kuangshizhonglei);
            editText34.setText(tihuoliang);
            editText35.setText(paiwuliang);
            editText36.setText(dianzichengliang);
//            editText37.setText(kongzhizongliang);
            editText38.setText(qiancejiandinghuoliang);
            editText39.setText(qiancepaishiuliang);
            editText40.setText(qiancechuanyongwuliao);
        }
    }


    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {

        ceshishuichi_frontLeft = new DecimalFormat("0.00").format(Double.valueOf(editText4.getText().toString().trim().equals("") ? "0" : editText4.getText().toString().trim()));
        ceshishuichi_frontRight = new DecimalFormat("0.00").format(Double.valueOf(editText5.getText().toString().trim().equals("") ? "0" : editText5.getText().toString().trim()));
        ceshishuichi_midLeft = new DecimalFormat("0.00").format(Double.valueOf(editText6.getText().toString().trim().equals("") ? "0" : editText6.getText().toString().trim()));
        ceshishuichi_midRight = new DecimalFormat("0.00").format(Double.valueOf(editText7.getText().toString().trim().equals("") ? "0" : editText7.getText().toString().trim()));
        ceshishuichi_backLeft = new DecimalFormat("0.00").format(Double.valueOf(editText8.getText().toString().trim().equals("") ? "0" : editText8.getText().toString().trim()));
        ceshishuichi_backRight = new DecimalFormat("0.00").format(Double.valueOf(editText9.getText().toString().trim().equals("") ? "0" : editText9.getText().toString().trim()));
        average_front = (Double.valueOf(ceshishuichi_frontLeft) + Double.valueOf(ceshishuichi_frontRight)) / 2;
        average_mid = (Double.valueOf(ceshishuichi_midLeft) + Double.valueOf(ceshishuichi_midRight)) / 2;
        average_back = (Double.valueOf(ceshishuichi_backLeft) + Double.valueOf(ceshishuichi_backRight)) / 2;
        if (!pattern.matcher(editText10.getText().toString()).matches()
                || !pattern.matcher(editText11.getText().toString()).matches() || !pattern.matcher(editText12.getText().toString()).matches()
                || !pattern.matcher(editText13.getText().toString()).matches()) {

        } else {
            biaojijuli_front = new DecimalFormat("0.000").format(Double.valueOf(editText10.getText().toString().trim().equals("") ? "0" : editText10.getText().toString().trim()));
            biaojijuli_mid = new DecimalFormat("0.000").format(Double.valueOf(editText11.getText().toString().trim().equals("") ? "0" : editText11.getText().toString().trim()));
            biaojijuli_back = new DecimalFormat("0.000").format(Double.valueOf(editText12.getText().toString().trim().equals("") ? "0" : editText12.getText().toString().trim()));
            ship_length = new DecimalFormat("0.000").format(Double.valueOf(editText13.getText().toString().trim().equals("") ? "0" : editText13.getText().toString().trim()));
            near_shuichi = new DecimalFormat("0.000").format(Double.valueOf(editText14.getText().toString().trim().equals("") ? "0" : editText14.getText().toString().trim()));
            chishuicha_before = average_back - average_front;
            alter_front = chishuicha_before * Double.valueOf(biaojijuli_front) / (Double.valueOf(ship_length) + Double.valueOf(biaojijuli_front) - Double.valueOf(biaojijuli_back));
            alter_mid = chishuicha_before * Double.valueOf(biaojijuli_mid) / (Double.valueOf(ship_length) + Double.valueOf(biaojijuli_front) - Double.valueOf(biaojijuli_back));
            alter_back = chishuicha_before * Double.valueOf(biaojijuli_back) / (Double.valueOf(ship_length) + Double.valueOf(biaojijuli_front) - Double.valueOf(biaojijuli_back));
            afteralter_front = average_front + alter_front;
            afteralter_mid = average_mid + alter_mid;
            afteralter_back = average_back + alter_back;
            chishuicha_after = Double.valueOf(new DecimalFormat("0.000").format(afteralter_back - afteralter_front));
            jiaozhenghou_average = (afteralter_back + afteralter_front + 6 * afteralter_mid) / 8;
            chaeshuichi = (jiaozhenghou_average - Double.valueOf(near_shuichi)) * 100;
            textView4.setText(new DecimalFormat("0.000").format(alter_front));
            textView5.setText(new DecimalFormat("0.000").format(alter_mid));
            textView6.setText(new DecimalFormat("0.000").format(alter_back));
            textView7.setText(new DecimalFormat("0.000").format(afteralter_front));
            textView8.setText(new DecimalFormat("0.000").format(afteralter_mid));
            textView9.setText(new DecimalFormat("0.000").format(afteralter_back));
            textView10.setText(new DecimalFormat("0.000").format(chishuicha_before));
            textView11.setText(new DecimalFormat("0.000").format(chishuicha_after));
            textView23.setText(new DecimalFormat("0.000").format(jiaozhenghou_average));
            textView14.setText(new DecimalFormat("0.000").format(jiaozhenghou_average));
            textView12.setText(new DecimalFormat("0.0").format(chaeshuichi));
        }
        textView1.setText(new DecimalFormat("0.000").format(average_front));
        textView2.setText(new DecimalFormat("0.000").format(average_mid));
        textView3.setText(new DecimalFormat("0.000").format(average_back));
    }


    @Override
    public void onBackPressed() {
        super.showUpdateDialog();
    }

}