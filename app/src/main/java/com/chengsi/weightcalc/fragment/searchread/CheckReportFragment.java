package com.chengsi.weightcalc.fragment.searchread;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.chengsi.weightcalc.activity.search.GroupActivity;
import com.chengsi.weightcalc.activity.search.SearchActivity;
import com.chengsi.weightcalc.db.DBHelper;
import com.chengsi.weightcalc.fragment.BaseFragment;
import com.chengsi.weightcalc.listener.OnContinuousClickListener;
import com.chengsi.weightcalc.utils.print.PrintPDF;
import com.chengsi.weightcalc.R;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

/**
 * 前测数据查看.
 */
public class CheckReportFragment extends BaseFragment {
    private String measureId;
    private View view;
    private Map<String, String> mData;
    DBHelper dbHelper;
    SQLiteDatabase db;
    Cursor cursor;
    Cursor cursor1;
    Cursor cursor2;
    double average_front;
    double average_mid;
    double average_back;
    double alter_front;
    double alter_mid;
    double alter_back;
    double afteralter_front;
    double afteralter_mid;
    double afteralter_back;
    double chishuicha_before;
    double chishuicha_after;
    double chaeshuichi;
    double chaezhongliang;
    double shijishuichi;
    double shijipaishuizaizhong;
    double zongqingliju;
    double jiaozhi;
    double alterpaishui;
    double weight_before;
    double weight_after;
    double jianchuanyongwuliao;
    double weight_package;
    double jiaozhenghou_average;

    String ship_name;
    String check_id;
    String check_time;
    String ceshishuichi_frontLeft;
    String ceshishuichi_frontRight;
    String ceshishuichi_midLeft;
    String ceshishuichi_midRight;
    String ceshishuichi_backLeft;
    String ceshishuichi_backRight;
    String biaojijuli_front;
    String biaojijuli_mid;
    String biaojijuli_back;
    String ship_length;
    String near_shuichi;
    String near_weight;
    String tpc;
    String LCF;
    String DZ;
    String M1;
    String M2;
    String zy;
    String qy;
    String rhy;
    String ds;
    String ycs;
    String bzmd;
    String scmd;
    String qz;
    String cs;
    double jianchuanyongwuliao_back;
    double shijipaishuiliang_back;
    double qiancepsl;
    double qiancecywl;
    String qiancepaishiuliang;
    String qiancechuanyongwuliao;
    private double qingzaijidingliangbeiliao;
    private double jianqingzai_beiliao;
    private Button button;

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 0) {
                initView();
            } else {
                Toast.makeText(getActivity(), "没有记录", Toast.LENGTH_SHORT).show();
            }
        }
    };
    Runnable mQueryData = new Runnable() {
        @Override
        public void run() {
            Message msg = new Message();
            dbHelper = new DBHelper(getActivity(), "weightdatabase.db", null, 1);
            db = dbHelper.getReadableDatabase();
            cursor = db.rawQuery("select * from weightcalc where ship_name=? And check_id=? And check_type=?", new String[]{SearchActivity.shipName, measureId + "", 1 + ""});
            cursor1 = db.rawQuery("select * from weightcalc where ship_name=? And check_id=? And check_type=?", new String[]{SearchActivity.shipName, measureId + "", 3 + ""});
            cursor2 = db.rawQuery("select * from weightcalc where ship_name=? And check_id=? And check_type=?", new String[]{SearchActivity.shipName, measureId + "", 2 + ""});
            mData = new HashMap<>();

            if (cursor != null && cursor1 != null && cursor2 !=null) {
                while (cursor.moveToNext()) {
                    check_time = cursor.getString(cursor.getColumnIndex("check_time"));
                    ceshishuichi_frontLeft = cursor.getString(cursor.getColumnIndex("ceshishuichi_frontLeft"));
                    ceshishuichi_frontRight = cursor.getString(cursor.getColumnIndex("ceshishuichi_frontRight"));
                    ceshishuichi_midLeft = cursor.getString(cursor.getColumnIndex("ceshishuichi_midLeft"));
                    ceshishuichi_midRight = cursor.getString(cursor.getColumnIndex("ceshishuichi_midRight"));
                    ceshishuichi_backLeft = cursor.getString(cursor.getColumnIndex("ceshishuichi_backLeft"));
                    ceshishuichi_backRight = cursor.getString(cursor.getColumnIndex("ceshishuichi_backRight"));
                    biaojijuli_front = cursor.getString(cursor.getColumnIndex("biaojijuli_front"));
                    biaojijuli_mid = cursor.getString(cursor.getColumnIndex("biaojijuli_mid"));
                    biaojijuli_back = cursor.getString(cursor.getColumnIndex("biaojijuli_back"));
                    ship_length = cursor.getString(cursor.getColumnIndex("ship_length"));
                    near_shuichi = cursor.getString(cursor.getColumnIndex("near_shuichi"));
                    near_weight = cursor.getString(cursor.getColumnIndex("near_weight"));
                    tpc = cursor.getString(cursor.getColumnIndex("tpc"));
                    LCF = cursor.getString(cursor.getColumnIndex("LCF"));
                    DZ = cursor.getString(cursor.getColumnIndex("DZ"));
                    M1 = cursor.getString(cursor.getColumnIndex("M1"));
                    M2 = cursor.getString(cursor.getColumnIndex("M2"));
                    zy = cursor.getString(cursor.getColumnIndex("zy"));
                    qy = cursor.getString(cursor.getColumnIndex("qy"));
                    rhy = cursor.getString(cursor.getColumnIndex("rhy"));
                    ds = cursor.getString(cursor.getColumnIndex("ds"));
                    ycs = cursor.getString(cursor.getColumnIndex("ycs"));
                    bzmd = cursor.getString(cursor.getColumnIndex("bzmd"));
                    scmd = cursor.getString(cursor.getColumnIndex("scmd"));
                    qz = cursor.getString(cursor.getColumnIndex("qz"));
                    cs = cursor.getString(cursor.getColumnIndex("cs"));
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
                    chishuicha_after = afteralter_back - afteralter_front;
                    jiaozhenghou_average = (afteralter_back + afteralter_front + 6 * afteralter_mid) / 8;
                    chaeshuichi = (jiaozhenghou_average - Double.valueOf(near_shuichi)) * 100;
                    chaezhongliang = new BigDecimal(chaeshuichi).setScale(1,BigDecimal.ROUND_HALF_UP).doubleValue()* Double.valueOf(tpc);
                    shijishuichi = jiaozhenghou_average;
                    shijipaishuizaizhong = Double.valueOf(near_weight) + Double.valueOf(new DecimalFormat("0.0").format(chaezhongliang));
                    jianchuanyongwuliao = Double.valueOf(zy) + Double.valueOf(qy) + Double.valueOf(rhy) + Double.valueOf(ds) + Double.valueOf(ycs);
                    if (cursor.getString(cursor.getColumnIndex("scmd")).equals("1")) {
                        zongqingliju = (Double.valueOf(M2) - Double.valueOf(M1)) / Double.valueOf(DZ);
                        jiaozhi = (100 * chishuicha_after * Double.valueOf(tpc) * Double.valueOf(LCF) + 50 * chishuicha_after * chishuicha_after * zongqingliju) / Double.valueOf(ship_length);
                    } else {
                        zongqingliju = 0;
                        jiaozhi = 0;
                    }
                    alterpaishui = shijipaishuizaizhong + jiaozhi;
                    weight_before = shijipaishuizaizhong + jiaozhi;
                    weight_after = (shijipaishuizaizhong + jiaozhi) * Double.valueOf(scmd) / Double.valueOf(bzmd);
                    weight_package = (shijipaishuizaizhong + jiaozhi) * Double.valueOf(scmd) / Double.valueOf(bzmd) - jianchuanyongwuliao - Double.valueOf(qz) - Double.valueOf(cs);

                    mData.put("ship_name", cursor.getString(cursor.getColumnIndex("ship_name")));
                    mData.put("check_time_f", cursor.getString(cursor.getColumnIndex("check_time")));
                    mData.put("alter_f_qian", new DecimalFormat("0.000").format(afteralter_front));
                    mData.put("alter_m_qian", new DecimalFormat("0.000").format(afteralter_mid));
                    mData.put("alter_h_qian", new DecimalFormat("0.000").format(afteralter_back));
                    mData.put("jiaozhenghou_average_qian", new DecimalFormat("0.000").format(jiaozhenghou_average));
                    mData.put("scmd_qian", scmd);
                    mData.put("shijipaishuizaizhong_qian", new DecimalFormat("0.0").format(shijipaishuizaizhong));
                    mData.put("alterpaishui_qian", new DecimalFormat("0.0").format(alterpaishui));
                    mData.put("weight_after_qian", new DecimalFormat("0.0").format(weight_after));
                    mData.put("oil_qian", new DecimalFormat("0.0").format(Double.parseDouble(qy) + Double.parseDouble(zy) + Double.parseDouble(rhy)));
                    mData.put("ds_qian", ds);
                    mData.put("ycs_qian", ycs);
                    mData.put("other_qian", "/");
                }
                while (cursor1.moveToNext()) {
                    check_time = cursor1.getString(cursor1.getColumnIndex("check_time"));
                    ceshishuichi_frontLeft = cursor1.getString(cursor1.getColumnIndex("ceshishuichi_frontLeft"));
                    ceshishuichi_frontRight = cursor1.getString(cursor1.getColumnIndex("ceshishuichi_frontRight"));
                    ceshishuichi_midLeft = cursor1.getString(cursor1.getColumnIndex("ceshishuichi_midLeft"));
                    ceshishuichi_midRight = cursor1.getString(cursor1.getColumnIndex("ceshishuichi_midRight"));
                    ceshishuichi_backLeft = cursor1.getString(cursor1.getColumnIndex("ceshishuichi_backLeft"));
                    ceshishuichi_backRight = cursor1.getString(cursor1.getColumnIndex("ceshishuichi_backRight"));
                    biaojijuli_front = cursor1.getString(cursor1.getColumnIndex("biaojijuli_front"));
                    biaojijuli_mid = cursor1.getString(cursor1.getColumnIndex("biaojijuli_mid"));
                    biaojijuli_back = cursor1.getString(cursor1.getColumnIndex("biaojijuli_back"));
                    ship_length = cursor1.getString(cursor1.getColumnIndex("ship_length"));
                    near_shuichi = cursor1.getString(cursor1.getColumnIndex("near_shuichi"));
                    near_weight = new DecimalFormat("0.0").format(Double.valueOf(cursor1.getString(cursor1.getColumnIndex("near_weight"))));
                    tpc = cursor1.getString(cursor1.getColumnIndex("tpc"));
                    LCF = cursor1.getString(cursor1.getColumnIndex("LCF"));
                    DZ = cursor1.getString(cursor1.getColumnIndex("DZ"));
                    M1 = cursor1.getString(cursor1.getColumnIndex("M1"));
                    M2 = cursor1.getString(cursor1.getColumnIndex("M2"));
                    zy = cursor1.getString(cursor1.getColumnIndex("zy"));
                    qy = cursor1.getString(cursor1.getColumnIndex("qy"));
                    rhy = cursor1.getString(cursor1.getColumnIndex("rhy"));
                    ds = cursor1.getString(cursor1.getColumnIndex("ds"));
                    ycs = cursor1.getString(cursor1.getColumnIndex("ycs"));
                    bzmd = cursor1.getString(cursor1.getColumnIndex("bzmd"));
                    scmd = cursor1.getString(cursor1.getColumnIndex("scmd"));
                    qiancepaishiuliang = cursor1.getString(cursor1.getColumnIndex("qiancepaishiuliang"));
                    qiancechuanyongwuliao = cursor1.getString(cursor1.getColumnIndex("qiancechuanyongwuliao"));

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
                    if (cursor1.getString(cursor1.getColumnIndex("check_status")).equals("1")) {
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

                    mData.put("check_time_b", cursor1.getString(cursor1.getColumnIndex("check_time")));
                    mData.put("alter_f_hou", new DecimalFormat("0.000").format(afteralter_front));
                    mData.put("alter_m_hou", new DecimalFormat("0.000").format(afteralter_mid));
                    mData.put("alter_h_hou", new DecimalFormat("0.000").format(afteralter_back));
                    mData.put("jiaozhenghou_average_hou", new DecimalFormat("0.000").format(jiaozhenghou_average));
                    mData.put("scmd_hou", scmd);
                    mData.put("shijipaishuizaizhong_hou", new DecimalFormat("0.0").format(shijipaishuizaizhong));
                    mData.put("alterpaishui_hou", new DecimalFormat("0.0").format(alterpaishui));
                    mData.put("weight_after_hou", new DecimalFormat("0.0").format(weight_after));
                    mData.put("oil_hou", new DecimalFormat("0.0").format(Double.parseDouble(qy) + Double.parseDouble(zy) + Double.parseDouble(rhy)));
                    mData.put("ds_hou", ds);
                    mData.put("ycs_hou", ycs);
                    mData.put("other_hou", "/");
                    mData.put("weight_package", new DecimalFormat("0").format(weight_package));
                }

                while (cursor2.moveToNext()) {
//                    check_time = cursor2.getString(cursor2.getColumnIndex("check_time"));
                    ceshishuichi_frontLeft = cursor2.getString(cursor2.getColumnIndex("ceshishuichi_frontLeft"));
                    ceshishuichi_frontRight = cursor2.getString(cursor2.getColumnIndex("ceshishuichi_frontRight"));
                    ceshishuichi_midLeft = cursor2.getString(cursor2.getColumnIndex("ceshishuichi_midLeft"));
                    ceshishuichi_midRight = cursor2.getString(cursor2.getColumnIndex("ceshishuichi_midRight"));
                    ceshishuichi_backLeft = cursor2.getString(cursor2.getColumnIndex("ceshishuichi_backLeft"));
                    ceshishuichi_backRight = cursor2.getString(cursor2.getColumnIndex("ceshishuichi_backRight"));
                    biaojijuli_front = cursor2.getString(cursor2.getColumnIndex("biaojijuli_front"));
                    biaojijuli_mid = cursor2.getString(cursor2.getColumnIndex("biaojijuli_mid"));
                    biaojijuli_back = cursor2.getString(cursor2.getColumnIndex("biaojijuli_back"));
                    ship_length = cursor2.getString(cursor2.getColumnIndex("ship_length"));
                    near_shuichi = cursor2.getString(cursor2.getColumnIndex("near_shuichi"));
                    near_weight = new DecimalFormat("0.0").format(Double.valueOf(cursor2.getString(cursor2.getColumnIndex("near_weight"))));
                    tpc = cursor2.getString(cursor2.getColumnIndex("tpc"));
                    LCF = cursor2.getString(cursor2.getColumnIndex("LCF"));
                    DZ = cursor2.getString(cursor2.getColumnIndex("DZ"));
                    M1 = cursor2.getString(cursor2.getColumnIndex("M1"));
                    M2 = cursor2.getString(cursor2.getColumnIndex("M2"));
                    zy = cursor2.getString(cursor2.getColumnIndex("zy"));
                    qy = cursor2.getString(cursor2.getColumnIndex("qy"));
                    rhy = cursor2.getString(cursor2.getColumnIndex("rhy"));
                    ds = cursor2.getString(cursor2.getColumnIndex("ds"));
                    ycs = cursor2.getString(cursor2.getColumnIndex("ycs"));
                    bzmd = cursor2.getString(cursor2.getColumnIndex("bzmd"));
                    scmd = cursor2.getString(cursor2.getColumnIndex("scmd"));
                    qiancepaishiuliang = cursor2.getString(cursor2.getColumnIndex("qiancepaishiuliang"));
                    qiancechuanyongwuliao = cursor2.getString(cursor2.getColumnIndex("qiancechuanyongwuliao"));

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
                    if (cursor2.getString(cursor2.getColumnIndex("check_status")).equals("1")) {
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
//                    weight_package = Double.valueOf(qiancepaishiuliang) - Double.valueOf(qiancechuanyongwuliao) - jianqingzai_beiliao;

                    mData.put("alter_f_zhong", new DecimalFormat("0.000").format(afteralter_front));
                    mData.put("alter_m_zhong", new DecimalFormat("0.000").format(afteralter_mid));
                    mData.put("alter_h_zhong", new DecimalFormat("0.000").format(afteralter_back));
                    mData.put("jiaozhenghou_average_zhong", new DecimalFormat("0.000").format(jiaozhenghou_average));
                    mData.put("scmd_zhong", scmd);
                    mData.put("shijipaishuizaizhong_zhong", new DecimalFormat("0.0").format(shijipaishuizaizhong));
                    mData.put("alterpaishui_zhong", new DecimalFormat("0.0").format(alterpaishui));
                    mData.put("weight_after_zhong", new DecimalFormat("0.0").format(weight_after));
                    mData.put("oil_zhong", new DecimalFormat("0.0").format(Double.parseDouble(qy) + Double.parseDouble(zy) + Double.parseDouble(rhy)));
                    mData.put("ds_zhong", ds);
                    mData.put("ycs_zhong", ycs);
                    mData.put("other_zhong", "/");
                }
                msg.what = 0;
            } else {
                msg.what = 1;
            }
            handler.sendMessage(msg);
        }
    };


    @Override
    public View getContentView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_check_report, null);
        button = (Button)view.findViewById(R.id.doprint);
        button.setOnClickListener(new OnContinuousClickListener() {
            @Override
            public void onContinuousClick(View v) {
                PrintPDF printPDF = new PrintPDF(mData);
                printPDF.print();
            }
        });
        initData();
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        db.close();
        cursor.close();
        cursor1.close();
        cursor2.close();
    }

    public CheckReportFragment() {
        this.measureId = GroupActivity.check_id;
    }

    private void initData() {
        new Thread(mQueryData).start();
    }

    private void initView() {
        ((EditText) view.findViewById(R.id.shipname_report)).setText(mData.get("ship_name"));
        String duration,from,to;
        if(mData.get("check_time_f") != null){
            from = mData.get("check_time_f").substring(0,10);
        }else{
            from = "unknown";
        }
        if(mData.get("check_time_b") != null){
            to = mData.get("check_time_b").substring(0,10);
        }else{
            to = "unknown";
        }
        ((EditText) view.findViewById(R.id.date_report)).setText(from+" - "+to);
        ((EditText) view.findViewById(R.id.souqian_report)).setText(mData.get("alter_f_qian") == null ? "000.000" : mData.get("alter_f_qian"));
        ((EditText) view.findViewById(R.id.weiqian_report)).setText(mData.get("alter_h_qian") == null ? "000.000" : mData.get("alter_h_qian"));
        ((EditText) view.findViewById(R.id.zhongqian_report)).setText(mData.get("alter_m_qian") == null ? "000.000" : mData.get("alter_m_qian"));
        ((EditText) view.findViewById(R.id.pingjunqian_report)).setText(mData.get("jiaozhenghou_average_qian") == null ? "000.000" : mData.get("jiaozhenghou_average_qian"));
        ((TextView) view.findViewById(R.id.md_f_report)).setText(mData.get("scmd_qian") == null ? "000.000" : mData.get("scmd_qian"));
        ((EditText) view.findViewById(R.id.sjpsl_qian_report)).setText(mData.get("shijipaishuizaizhong_qian") == null ? "000.0" : mData.get("shijipaishuizaizhong_qian"));
        ((EditText) view.findViewById(R.id.jzhpsl_qian_report)).setText(mData.get("alterpaishui_qian") == null ? "000.000" : mData.get("alterpaishui_qian"));
        ((EditText) view.findViewById(R.id.mdjzhpsl_qian_report)).setText(mData.get("weight_after_qian") == null ? "000.000" : mData.get("weight_after_qian"));
        ((EditText) view.findViewById(R.id.you_qian_report)).setText(mData.get("oil_qian") == null ? "000.000" : mData.get("oil_qian"));
        ((EditText) view.findViewById(R.id.danshui_f_report)).setText(mData.get("ds_qian") == null ? "000.000" : mData.get("ds_qian"));
        ((TextView) view.findViewById(R.id.ycs_f_report)).setText(mData.get("ycs_qian") == null ? "000.000" : mData.get("ycs_qian"));
        ((TextView) view.findViewById(R.id.other_f_report)).setText(mData.get("other_qian"));
//        ((EditText) view.findViewById(R.id.date_report)).setText(mData.get("check_time"));
        ((EditText) view.findViewById(R.id.souhou_report)).setText(mData.get("alter_f_hou") == null ? "000.000" : mData.get("alter_f_hou"));
        ((EditText) view.findViewById(R.id.weihou_report)).setText(mData.get("alter_h_hou") == null ? "000.000" : mData.get("alter_h_hou"));
        ((EditText) view.findViewById(R.id.zhonghou_report)).setText(mData.get("alter_m_hou") == null ? "000.000" : mData.get("alter_m_hou"));
        ((EditText) view.findViewById(R.id.pingjunhou_report)).setText(mData.get("jiaozhenghou_average_hou") == null ? "000.000" : mData.get("jiaozhenghou_average_hou"));
        ((TextView) view.findViewById(R.id.md_b_report)).setText(mData.get("scmd_hou") == null ? "000.000" : mData.get("scmd_hou"));
        ((EditText) view.findViewById(R.id.sjpsl_hou_report)).setText(mData.get("shijipaishuizaizhong_hou") == null ? "000.000" : mData.get("shijipaishuizaizhong_hou"));
        ((EditText) view.findViewById(R.id.jzhpsl_hou_report)).setText(mData.get("alterpaishui_hou") == null ? "000.000" : mData.get("alterpaishui_hou"));
        ((EditText) view.findViewById(R.id.mdjzhpsl_hou_report)).setText(mData.get("weight_after_hou") == null ? "000.000" : mData.get("weight_after_hou"));
        ((EditText) view.findViewById(R.id.you_hou_report)).setText(mData.get("oil_hou") == null ? "000.000" : mData.get("oil_hou"));
        ((EditText) view.findViewById(R.id.danshui_b_report)).setText(mData.get("ds_hou") == null ? "000.000" : mData.get("ds_hou"));
        ((TextView) view.findViewById(R.id.ycs_b_report)).setText(mData.get("ycs_hou") == null ? "000.000" : mData.get("ycs_hou"));
        ((TextView) view.findViewById(R.id.other_b_report)).setText(mData.get("other_hou"));

        ((EditText) view.findViewById(R.id.souzhong_report)).setText(mData.get("alter_f_zhong") == null ? "000.000" : mData.get("alter_f_zhong"));
        ((EditText) view.findViewById(R.id.weizhong_report)).setText(mData.get("alter_h_zhong") == null ? "000.000" : mData.get("alter_h_zhong"));
        ((EditText) view.findViewById(R.id.zhongzhong_report)).setText(mData.get("alter_m_zhong") == null ? "000.000" : mData.get("alter_m_zhong"));
        ((EditText) view.findViewById(R.id.pingjunzhong_report)).setText(mData.get("jiaozhenghou_average_zhong") == null ? "000.000" : mData.get("jiaozhenghou_average_zhong"));
        ((TextView) view.findViewById(R.id.md_z_report)).setText(mData.get("scmd_zhong") == null ? "000.000" : mData.get("scmd_zhong"));
        ((EditText) view.findViewById(R.id.sjpsl_zhong_report)).setText(mData.get("shijipaishuizaizhong_zhong") == null ? "000.000" : mData.get("shijipaishuizaizhong_zhong"));
        ((EditText) view.findViewById(R.id.jzhpsl_zhong_report)).setText(mData.get("alterpaishui_zhong") == null ? "000.000" : mData.get("alterpaishui_zhong"));
        ((EditText) view.findViewById(R.id.mdjzhpsl_zhong_report)).setText(mData.get("weight_after_zhong") == null ? "000.000" : mData.get("weight_after_zhong"));
        ((EditText) view.findViewById(R.id.you_zhong_report)).setText(mData.get("oil_zhong") == null ? "000.000" : mData.get("oil_zhong"));
        ((EditText) view.findViewById(R.id.danshui_zhong_report)).setText(mData.get("ds_zhong") == null ? "000.000" : mData.get("ds_zhong"));
        ((TextView) view.findViewById(R.id.ycs_zhong_report)).setText(mData.get("ycs_zhong") == null ? "000.000" : mData.get("ycs_zhong"));
        ((TextView) view.findViewById(R.id.other_zhong_report)).setText(mData.get("other_zhong"));
        ((TextView) view.findViewById(R.id.weight_package)).setText(mData.get("weight_package"));
    }


}
