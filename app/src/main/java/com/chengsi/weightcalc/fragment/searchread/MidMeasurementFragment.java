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
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.chengsi.weightcalc.activity.search.GroupActivity;
import com.chengsi.weightcalc.activity.search.SearchActivity;
import com.chengsi.weightcalc.db.DBHelper;
import com.chengsi.weightcalc.fragment.BaseFragment;
import com.chengsi.weightcalc.R;
import com.chengsi.weightcalc.utils.print.PrintMeasure;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

/**
 * 前测数据查看.
 */
public class MidMeasurementFragment extends BaseFragment {
    private String measureId;
    private View view;
    private Map<String, String> mData;
    DBHelper dbHelper;
    SQLiteDatabase db;
    Cursor cursor;
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
    String jianwuyoupaifang_mid;

    private String shijipaishuiliang_front;
    private String jianchuanyongwuliao_front;
    private double qingzaijidingliangbeiliao;
    private double shijipaishuiliang;
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
            cursor = db.rawQuery("select * from weightcalc where ship_name=? And check_id=? And check_type=?", new String[]{SearchActivity.shipName, measureId + "", 2 + ""});
            mData = new HashMap<>();
            if (cursor != null) {
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
                    jianwuyoupaifang_mid = cursor.getString(cursor.getColumnIndex("jianwuyoupaifang_mid"));
                    shijipaishuiliang_front = cursor.getString(cursor.getColumnIndex("qiancepaishiuliang"));
                    jianchuanyongwuliao_front = cursor.getString(cursor.getColumnIndex("qiancechuanyongwuliao"));
                    mData.put("check_time", check_time);
                    mData.put("check_id", measureId);
                    mData.put("ship_name",SearchActivity.shipName);
                    mData.put("ceshishuichi_frontLeft", ceshishuichi_frontLeft);
                    mData.put("ceshishuichi_frontRight", ceshishuichi_frontRight);
                    mData.put("ceshishuichi_midLeft", ceshishuichi_midLeft);
                    mData.put("ceshishuichi_midRight", ceshishuichi_midRight);
                    mData.put("ceshishuichi_backLeft", ceshishuichi_backLeft);
                    mData.put("ceshishuichi_backRight", ceshishuichi_backRight);
                    mData.put("biaojijuli_front", biaojijuli_front);
                    mData.put("biaojijuli_mid", biaojijuli_mid);
                    mData.put("biaojijuli_back", biaojijuli_back);
                    mData.put("ship_length", ship_length);
                    mData.put("near_shuichi", near_shuichi);
                    mData.put("near_weight", near_weight);
                    mData.put("tpc", tpc);
                    mData.put("LCF", LCF);
                    mData.put("DZ", DZ);
                    mData.put("M1", M1);
                    mData.put("M2", M2);
                    mData.put("zy", zy);
                    mData.put("qy", qy);
                    mData.put("rhy", rhy);
                    mData.put("ds", ds);
                    mData.put("ycs", ycs);
                    mData.put("bzmd", bzmd);
                    mData.put("scmd", scmd);
                    mData.put("jianwuyoupaifang_mid", jianwuyoupaifang_mid);
                    mData.put("shijipaishuiliang", shijipaishuiliang_front);
                    mData.put("jianchuanyongwuliao", jianchuanyongwuliao_front);
                    mData.put("check_status", cursor.getString(cursor.getColumnIndex("check_status")));
                    mData.put("check_type", cursor.getString(cursor.getColumnIndex("check_type")));
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
                    jianchuanyongwuliao = Double.valueOf(zy) + Double.valueOf(qy) + Double.valueOf(rhy) + Double.valueOf(ds) + Double.valueOf(ycs);
                    if (mData.get("check_status").equals("1")) {
                        zongqingliju = (Double.valueOf(M2) - Double.valueOf(M1)) / Double.valueOf(DZ);
                        jiaozhi = (100 * chishuicha_after * Double.valueOf(tpc) * Double.valueOf(LCF) + 50 * chishuicha_after * chishuicha_after * zongqingliju) / Double.valueOf(ship_length);
                    } else {
                        zongqingliju = 0;
                        jiaozhi = 0;
                    }
                    alterpaishui = shijipaishuizaizhong + jiaozhi;
                    weight_before = shijipaishuizaizhong + jiaozhi;
                    weight_after = (shijipaishuizaizhong + jiaozhi) * Double.valueOf(scmd) / Double.valueOf(bzmd);
                    shijipaishuiliang = weight_after;
                    qingzaijidingliangbeiliao = shijipaishuiliang - jianchuanyongwuliao;
                    jianqingzai_beiliao = qingzaijidingliangbeiliao;
                    weight_package = Double.valueOf(shijipaishuiliang_front) - Double.valueOf(jianchuanyongwuliao_front) - qingzaijidingliangbeiliao - Double.valueOf(jianwuyoupaifang_mid);
                    mData.put("average_front", String.valueOf(average_front));
                    mData.put("average_mid", String.valueOf(average_mid));
                    mData.put("average_back", String.valueOf(average_back));
                    mData.put("chishuicha_before", String.valueOf(chishuicha_before));
                    mData.put("alter_front", String.valueOf(alter_front));
                    mData.put("alter_mid", String.valueOf(alter_mid));
                    mData.put("alter_back", String.valueOf(alter_back));
                    mData.put("afteralter_front", String.valueOf(afteralter_front));
                    mData.put("afteralter_mid", String.valueOf(afteralter_mid));
                    mData.put("afteralter_back", String.valueOf(afteralter_back));
                    mData.put("chishuicha_after", String.valueOf(chishuicha_after));
                    mData.put("jiaozhenghou_average", String.valueOf(jiaozhenghou_average));
                    mData.put("chaeshuichi", String.valueOf(chaeshuichi));
                    mData.put("chaezhongliang", String.valueOf(chaezhongliang));
                    mData.put("shijishuichi", String.valueOf(shijishuichi));
                    mData.put("shijipaishuizaizhong", String.valueOf(shijipaishuizaizhong));
                    mData.put("jianchuanyongwuliao", String.valueOf(jianchuanyongwuliao));
                    mData.put("zongqingliju", String.valueOf(zongqingliju));
                    mData.put("jiaozhi", String.valueOf(jiaozhi));
                    mData.put("alterpaishui", String.valueOf(alterpaishui));
                    mData.put("weight_before", String.valueOf(weight_before));
                    mData.put("weight_after", String.valueOf(weight_after));
                    mData.put("weight_package", String.valueOf(weight_package));
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
        view = inflater.inflate(R.layout.fragment_mid_measure_read, null);
        initData();
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        db.close();
        cursor.close();
    }

    public MidMeasurementFragment() {
        this.measureId = GroupActivity.check_id;
    }

    private void initData() {
        new Thread(mQueryData).start();
    }

    private void initView() {
        button = (Button)view.findViewById(R.id.btn_print);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    new PrintMeasure(mData,"MID").print();
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
        });
        ((TextView) view.findViewById(R.id.tv1)).setText(mData.get("ship_name"));
        ((TextView) view.findViewById(R.id.tv2)).setText(mData.get("check_id"));
        ((TextView) view.findViewById(R.id.tv3)).setText(mData.get("check_time"));
        ((TextView) view.findViewById(R.id.tv4)).setText(mData.get("ceshishuichi_frontLeft") == null ? "000.00" : mData.get("ceshishuichi_frontLeft"));
        ((TextView) view.findViewById(R.id.tv5)).setText(mData.get("ceshishuichi_frontRight") == null ? "000.00" : mData.get("ceshishuichi_frontRight"));
        ((TextView) view.findViewById(R.id.tv6)).setText(mData.get("ceshishuichi_midLeft") == null ? "000.00" : mData.get("ceshishuichi_midLeft"));
        ((TextView) view.findViewById(R.id.tv7)).setText(mData.get("ceshishuichi_midRight") == null ? "000.00" : mData.get("ceshishuichi_midRight"));
        ((TextView) view.findViewById(R.id.tv8)).setText(mData.get("ceshishuichi_backLeft") == null ? "000.00" : mData.get("ceshishuichi_backLeft"));
        ((TextView) view.findViewById(R.id.tv9)).setText(mData.get("ceshishuichi_backRight") == null ? "000.00" : mData.get("ceshishuichi_backRight"));
        ((TextView) view.findViewById(R.id.tv10)).setText(mData.get("biaojijuli_front") == null ? "000.000" : mData.get("biaojijuli_front"));
        ((TextView) view.findViewById(R.id.tv11)).setText(mData.get("biaojijuli_mid") == null ? "000.000" : mData.get("biaojijuli_mid"));
        ((TextView) view.findViewById(R.id.tv12)).setText(mData.get("biaojijuli_back") == null ? "000.000" : mData.get("biaojijuli_back"));
        ((TextView) view.findViewById(R.id.tv13)).setText(mData.get("ship_length") == null ? "000.000" : mData.get("ship_length"));
        ((TextView) view.findViewById(R.id.tv14)).setText(mData.get("near_shuichi") == null ? "000.000" : mData.get("near_shuichi"));
        ((TextView) view.findViewById(R.id.tv15)).setText(mData.get("near_weight") == null ? "000.0" : mData.get("near_weight"));
        ((TextView) view.findViewById(R.id.tv16)).setText(mData.get("tpc") == null ? "000.000" : mData.get("tpc"));
        if (mData.get("check_status") != null) {
            if (mData.get("check_status").equals("1")) {
                CheckBox cb_jiaozhi = ((CheckBox) view.findViewById(R.id.checkBox));
                cb_jiaozhi.setChecked(true);
                cb_jiaozhi.setEnabled(false);
                ((TextView) view.findViewById(R.id.tv17)).setText(mData.get("LCF") == null ? "000.000" : mData.get("LCF"));
                ((TextView) view.findViewById(R.id.tv18)).setText(mData.get("DZ") == null ? "0" : mData.get("DZ"));
                ((TextView) view.findViewById(R.id.tv19)).setText(mData.get("M1") == null ? "0" : mData.get("M1"));
                ((TextView) view.findViewById(R.id.tv20)).setText(mData.get("M2") == null ? "0.0" : mData.get("M2"));
            } else {
                CheckBox cb_jiaozhi = ((CheckBox) view.findViewById(R.id.checkBox));
                cb_jiaozhi.setChecked(false);
                cb_jiaozhi.setEnabled(false);
                ((TextView) view.findViewById(R.id.tv17)).setText("000.000");
                ((TextView) view.findViewById(R.id.tv18)).setText("000.000");
                ((TextView) view.findViewById(R.id.tv19)).setText("000.000");
                ((TextView) view.findViewById(R.id.tv20)).setText("000.000");
            }
        }
        ((TextView) view.findViewById(R.id.tv21)).setText(mData.get("zy") == null ? "000.0" : mData.get("zy"));
        ((TextView) view.findViewById(R.id.tv22)).setText(mData.get("qy") == null ? "000.0" : mData.get("qy"));
        ((TextView) view.findViewById(R.id.tv23)).setText(mData.get("rhy") == null ? "000.0" : mData.get("rhy"));
        ((TextView) view.findViewById(R.id.tv24)).setText(mData.get("ds") == null ? "000.0" : mData.get("ds"));
        ((TextView) view.findViewById(R.id.tv25)).setText(mData.get("ycs") == null ? "000.0" : mData.get("ycs"));
        ((TextView) view.findViewById(R.id.tv26)).setText(mData.get("bzmd") == null ? "000.0000" : mData.get("bzmd"));
        ((TextView) view.findViewById(R.id.tv27)).setText(mData.get("scmd") == null ? "000.0000" : mData.get("scmd"));
        ((TextView) view.findViewById(R.id.tvv1)).setText(new DecimalFormat("0.000").format(average_front));
        ((TextView) view.findViewById(R.id.tvv2)).setText(new DecimalFormat("0.000").format(average_mid));
        ((TextView) view.findViewById(R.id.tvv3)).setText(new DecimalFormat("0.000").format(average_back));
        ((TextView) view.findViewById(R.id.tvv4)).setText(new DecimalFormat("0.000").format(alter_front));
        ((TextView) view.findViewById(R.id.tvv5)).setText(new DecimalFormat("0.000").format(alter_mid));
        ((TextView) view.findViewById(R.id.tvv6)).setText(new DecimalFormat("0.000").format(alter_back));
        ((TextView) view.findViewById(R.id.tvv7)).setText(new DecimalFormat("0.000").format(afteralter_front));
        ((TextView) view.findViewById(R.id.tvv8)).setText(new DecimalFormat("0.000").format(afteralter_mid));
        ((TextView) view.findViewById(R.id.tvv9)).setText(new DecimalFormat("0.000").format(afteralter_back));
        ((TextView) view.findViewById(R.id.tvv10)).setText(new DecimalFormat("0.000").format(chishuicha_before));
        ((TextView) view.findViewById(R.id.tvv11)).setText(new DecimalFormat("0.000").format(chishuicha_after));
        ((TextView) view.findViewById(R.id.tvv12)).setText(new DecimalFormat("0.0").format(chaeshuichi));
        ((TextView) view.findViewById(R.id.tvv13)).setText(new DecimalFormat("0.0").format(chaezhongliang));
        ((TextView) view.findViewById(R.id.tvv14)).setText(new DecimalFormat("0.000").format(shijishuichi));
        ((TextView) view.findViewById(R.id.tvv15)).setText(new DecimalFormat("0.0").format(shijipaishuizaizhong));
        ((TextView) view.findViewById(R.id.tvv16)).setText(new DecimalFormat("0.0").format(zongqingliju));
        ((TextView) view.findViewById(R.id.tvv17)).setText(new DecimalFormat("0.0").format(jiaozhi));
        ((TextView) view.findViewById(R.id.tvv18)).setText(new DecimalFormat("0.0").format(weight_before));
        ((TextView) view.findViewById(R.id.tvv19)).setText(new DecimalFormat("0.0").format(alterpaishui));
        ((TextView) view.findViewById(R.id.tvv20)).setText(new DecimalFormat("0.0").format(weight_after));
        ((TextView) view.findViewById(R.id.tv28)).setText(new DecimalFormat("0.0").format(Double.valueOf(shijipaishuiliang_front==null?"0":shijipaishuiliang_front)));
        ((TextView) view.findViewById(R.id.tv29)).setText(new DecimalFormat("0.0").format(Double.valueOf(jianchuanyongwuliao_front==null?"0":jianchuanyongwuliao_front)));
        ((TextView) view.findViewById(R.id.tv50)).setText(new DecimalFormat("0.0").format(Double.valueOf(jianwuyoupaifang_mid==null?"0":jianwuyoupaifang_mid)));
        ((TextView) view.findViewById(R.id.tvv22)).setText(new DecimalFormat("0").format(weight_package));
        ((TextView) view.findViewById(R.id.tvv23)).setText(new DecimalFormat("0.000").format(jiaozhenghou_average));
        ((TextView) view.findViewById(R.id.tvv24)).setText(new DecimalFormat("0.0").format(weight_after));
        ((TextView) view.findViewById(R.id.tvv25)).setText(new DecimalFormat("0.0").format(jianchuanyongwuliao));
        ((TextView) view.findViewById(R.id.tvv26)).setText(new DecimalFormat("0.0").format(weight_after-jianchuanyongwuliao));
        ((TextView) view.findViewById(R.id.tvv27)).setText(new DecimalFormat("0.0").format(weight_after-jianchuanyongwuliao));
    }
}
