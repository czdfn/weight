package com.chengsi.weightcalc.fragment.searchread;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
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
import com.chengsi.weightcalc.utils.print.PrintMeasure;
import com.chengsi.weightcalc.R;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Hook on 2016/2/18.
 */
public class BackMeasurementFragment extends BaseFragment {
    private View view;
    private Context context;
    private Map<String, String> mData;
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
    private double jiaozhenghou_average;
    private double qingzaijidingliangbeiliao;
    private String measureId;
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
    String biaojijuli_mid;
    String biaojijuli_back;
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
    private String jiandingmoshi;
    private String shifouduanzhong;
    private String jizhongliang;
    public SQLiteDatabase db;
    public DBHelper dbHelper;
    private Cursor cursor;
    private double weight_package;
    private double jianqingzai_beiliao;
    private double qiancepsl;
    private double qiancecywl;

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == GroupActivity.SUCCES) {
                Log.i("TAG", mData.toString());
                initView();
            } else {
                Toast.makeText(context, "暂无记录", Toast.LENGTH_SHORT).show();
            }
        }
    };
    Runnable mQueryData = new Runnable() {
        @Override
        public void run() {
            Message msg = new Message();
            dbHelper = new DBHelper(getActivity(), "weightdatabase.db", null, 1);
            db = dbHelper.getReadableDatabase();
            cursor = db.rawQuery("select * from weightcalc where ship_name=? And check_id=? And check_type=?", new String[]{SearchActivity.shipName, measureId + "", 3 + ""});
            mData = new HashMap<>();
            if (cursor != null) {
                while (cursor.moveToNext()) {
                    check_time = cursor.getString(cursor.getColumnIndex("check_time"));
                    jinkouguobie = cursor.getString(cursor.getColumnIndex("jinkouguobie"));
                    shouhuoqiye = cursor.getString(cursor.getColumnIndex("shouhuoqiye"));
                    kuangming = cursor.getString(cursor.getColumnIndex("kuangming"));
                    kuangshizhonglei = cursor.getString(cursor.getColumnIndex("kuangshizhonglei"));
                    tihuoliang = cursor.getString(cursor.getColumnIndex("tihuoliang"));
                    paiwuliang = cursor.getString(cursor.getColumnIndex("paiwuliang"));
                    dianzichengliang = cursor.getString(cursor.getColumnIndex("dianzichengliang"));
                    kongzhizongliang = cursor.getString(cursor.getColumnIndex("kongzhizongliang"));
                    qiancejiandinghuoliang = cursor.getString(cursor.getColumnIndex("qiancejiandinghuoliang"));
                    qiancepaishiuliang = cursor.getString(cursor.getColumnIndex("qiancepaishiuliang"));
                    qiancechuanyongwuliao = cursor.getString(cursor.getColumnIndex("qiancechuanyongwuliao"));
                    jiandingmoshi = cursor.getString(cursor.getColumnIndex("jiandingmoshi"));
                    shifouduanzhong = cursor.getString(cursor.getColumnIndex("shifouduanzhong"));
                    jizhongliang = cursor.getString(cursor.getColumnIndex("jizhongliang"));
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
                    chaezhongliang = chaeshuichi * Double.valueOf(tpc);
                    shijishuichi = jiaozhenghou_average;
                    shijipaishuizaizhong = Double.valueOf(near_weight) + chaezhongliang;
                    jianchuanyongwuliao_back = Double.valueOf(zy) + Double.valueOf(qy) + Double.valueOf(rhy) + Double.valueOf(ds) + Double.valueOf(ycs);
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
                    shijipaishuiliang_back = weight_after;
                    qingzaijidingliangbeiliao = shijipaishuiliang_back - jianchuanyongwuliao_back;
                    jianqingzai_beiliao = qingzaijidingliangbeiliao;
                    qiancepsl = Double.valueOf(qiancepaishiuliang);
                    qiancecywl = Double.valueOf(qiancechuanyongwuliao);
                    weight_package = Double.valueOf(qiancepaishiuliang) - Double.valueOf(qiancechuanyongwuliao) - jianqingzai_beiliao;
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
                    mData.put("jianchuanyongwuliao", String.valueOf(jianchuanyongwuliao_back));
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

    public BackMeasurementFragment() {
        this.measureId = GroupActivity.check_id;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        db.close();
        cursor.close();
    }


    @Override
    public View getContentView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_back_measure_read, null);
        context = this.getActivity();
        initData();
        return view;
    }

    private void initData() {
        new Thread(mQueryData).start();
    }

    private Button button;
    private void initView() {
        button = (Button)view.findViewById(R.id.btn_print);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    new PrintMeasure(mData).print();
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
        });
        ((TextView) view.findViewById(R.id.tv1)).setText(mData.get("ship_name"));
        ((TextView) view.findViewById(R.id.tv2)).setText(mData.get("check_id"));
        ((TextView) view.findViewById(R.id.tv3)).setText(mData.get("check_time"));
        ((TextView) view.findViewById(R.id.tv4)).setText(mData.get("ceshishuichi_frontLeft") == null ? "0.000" : mData.get("ceshishuichi_frontLeft"));
        ((TextView) view.findViewById(R.id.tv5)).setText(mData.get("ceshishuichi_frontRight") == null ? "0.000" : mData.get("ceshishuichi_frontRight"));
        ((TextView) view.findViewById(R.id.tv6)).setText(mData.get("ceshishuichi_midLeft") == null ? "0.000" : mData.get("ceshishuichi_midLeft"));
        ((TextView) view.findViewById(R.id.tv7)).setText(mData.get("ceshishuichi_midRight") == null ? "0.000" : mData.get("ceshishuichi_midRight"));
        ((TextView) view.findViewById(R.id.tv8)).setText(mData.get("ceshishuichi_backLeft") == null ? "0.000" : mData.get("ceshishuichi_backLeft"));
        ((TextView) view.findViewById(R.id.tv9)).setText(mData.get("ceshishuichi_backRight") == null ? "0.000" : mData.get("ceshishuichi_backRight"));
        ((TextView) view.findViewById(R.id.tv10)).setText(mData.get("biaojijuli_front") == null ? "0.000" : mData.get("biaojijuli_front"));
        ((TextView) view.findViewById(R.id.tv11)).setText(mData.get("biaojijuli_mid") == null ? "0.000" : mData.get("biaojijuli_mid"));
        ((TextView) view.findViewById(R.id.tv12)).setText(mData.get("biaojijuli_back") == null ? "0.000" : mData.get("biaojijuli_back"));
        ((TextView) view.findViewById(R.id.tv13)).setText(mData.get("ship_length") == null ? "0.000" : mData.get("ship_length"));
        ((TextView) view.findViewById(R.id.tv14)).setText(mData.get("near_shuichi") == null ? "0.000" : mData.get("near_shuichi"));
        ((TextView) view.findViewById(R.id.tv15)).setText(mData.get("near_weight") == null ? "0.0" : mData.get("near_weight"));
        ((TextView) view.findViewById(R.id.tv16)).setText(mData.get("near_weight") == null ? "0.000" : mData.get("tpc"));
        if (mData.get("check_status") != null) {
            if (mData.get("check_status").equals("1")) {
                CheckBox cb_jiaozhi = ((CheckBox) view.findViewById(R.id.checkBox));
                cb_jiaozhi.setChecked(true);
                cb_jiaozhi.setEnabled(false);
                ((TextView) view.findViewById(R.id.tv17)).setText(mData.get("LCF") == null ? "000.000" : mData.get("LCF"));
                ((TextView) view.findViewById(R.id.tv18)).setText(mData.get("DZ") == null ? "000.000" : mData.get("DZ"));
                ((TextView) view.findViewById(R.id.tv19)).setText(mData.get("M1") == null ? "000.000" : mData.get("M1"));
                ((TextView) view.findViewById(R.id.tv20)).setText(mData.get("M2") == null ? "000.000" : mData.get("M2"));
            } else {
                CheckBox cb_jiaozhi = ((CheckBox) view.findViewById(R.id.checkBox));
                cb_jiaozhi.setChecked(false);
                cb_jiaozhi.setEnabled(false);
                ((TextView) view.findViewById(R.id.tv17)).setText(R.string.content_label);
                ((TextView) view.findViewById(R.id.tv18)).setText(R.string.content_label);
                ((TextView) view.findViewById(R.id.tv19)).setText(R.string.content_label);
                ((TextView) view.findViewById(R.id.tv20)).setText(R.string.content_label);
            }
        }
        ((TextView) view.findViewById(R.id.tv21)).setText(mData.get("zy") == null ? "000.0" : mData.get("zy"));
        ((TextView) view.findViewById(R.id.tv22)).setText(mData.get("qy") == null ? "000.0" : mData.get("qy"));
        ((TextView) view.findViewById(R.id.tv23)).setText(mData.get("rhy") == null ? "000.0" : mData.get("rhy"));
        ((TextView) view.findViewById(R.id.tv24)).setText(mData.get("ds") == null ? "000.0" : mData.get("ds"));
        ((TextView) view.findViewById(R.id.tv25)).setText(mData.get("ycs") == null ? "000.0" : mData.get("ycs"));
        ((TextView) view.findViewById(R.id.tv26)).setText(mData.get("bzmd") == null ? "000.0000" : mData.get("bzmd"));
        ((TextView) view.findViewById(R.id.tv27)).setText(mData.get("scmd") == null ? "000.0000" : mData.get("scmd"));
        ((TextView) view.findViewById(R.id.tv30)).setText(jinkouguobie);
        ((TextView) view.findViewById(R.id.tv31)).setText(shouhuoqiye);
        ((TextView) view.findViewById(R.id.tv32)).setText(kuangming);
        ((TextView) view.findViewById(R.id.tv33)).setText(kuangshizhonglei);
        ((TextView) view.findViewById(R.id.tv34)).setText(tihuoliang);
        ((TextView) view.findViewById(R.id.tv35)).setText(paiwuliang);
        ((TextView) view.findViewById(R.id.tv36)).setText(dianzichengliang);
        ((TextView) view.findViewById(R.id.tv37)).setText(kongzhizongliang);
        ((TextView) view.findViewById(R.id.tv38)).setText(qiancejiandinghuoliang);
        ((TextView) view.findViewById(R.id.tv39)).setText(qiancepaishiuliang);
        ((TextView) view.findViewById(R.id.tv40)).setText(qiancechuanyongwuliao);

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
        ((TextView) view.findViewById(R.id.tvv16)).setText(new DecimalFormat("0.000").format(zongqingliju));
        ((TextView) view.findViewById(R.id.tvv17)).setText(new DecimalFormat("0.000").format(jiaozhi));
        ((TextView) view.findViewById(R.id.tvv18)).setText(new DecimalFormat("0.0").format(weight_before));
        ((TextView) view.findViewById(R.id.tvv19)).setText(new DecimalFormat("0.0").format(alterpaishui));
        ((TextView) view.findViewById(R.id.tvv20)).setText(new DecimalFormat("0.0").format(weight_after));
        ((TextView) view.findViewById(R.id.tvv22)).setText(new DecimalFormat("0").format(weight_package));
        ((TextView) view.findViewById(R.id.tvv23)).setText(new DecimalFormat("0.0").format(jiaozhenghou_average));
        ((TextView) view.findViewById(R.id.tvv24)).setText(new DecimalFormat("0.0").format(shijipaishuiliang_back));
        ((TextView) view.findViewById(R.id.tvv25)).setText(new DecimalFormat("0.0").format(jianchuanyongwuliao_back));
        ((TextView) view.findViewById(R.id.tvv26)).setText(new DecimalFormat("0.0").format(jianqingzai_beiliao));
        ((TextView) view.findViewById(R.id.tvv27)).setText(new DecimalFormat("0.0").format(qingzaijidingliangbeiliao));
        ((TextView) view.findViewById(R.id.tvv28)).setText(new DecimalFormat("0.0").format(qiancepsl));
        ((TextView) view.findViewById(R.id.tvv29)).setText(new DecimalFormat("0.0").format(qiancecywl));
//        ((TextView) view.findViewById(R.id.tv_jizhongliang)).setText(jizhongliang);
        ((CheckBox) view.findViewById(R.id.jdms_cb)).setChecked(Boolean.valueOf(jiandingmoshi));
        ((CheckBox) view.findViewById(R.id.sfdz_cb)).setChecked(Boolean.valueOf(shifouduanzhong));
    }
}
