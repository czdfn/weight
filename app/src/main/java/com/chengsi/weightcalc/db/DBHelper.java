package com.chengsi.weightcalc.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.chengsi.weightcalc.activity.measure.FrontMeasureActivity;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by lenovo on 2016-02-11.
 */
public class DBHelper extends SQLiteOpenHelper {
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
    private double jianchuanyongwuliao;
    private double weight_package;
    private double jiaozhenghou_average;
    private double shijipaishuiliang;

    private boolean jiandingmoshi;
    private boolean shifouduanzhong;
    private double jianqingzai_beiliao;
    private double qingzaijidingliangbeiliao;
    private double qiancepsl;
    private double qiancecywl;


    //    定义输入参数变量
    private String _id;
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
    private String qz;
    private String cs;

    private String jizhongliang;
    private String qiancepaishiuliang;
    private String qiancejiandinghuoliang;
    private String qiancechuanyongwuliao;
    private String jinkouguobie;
    private String shouhuoqiye;
    private String kuangming;
    private String kuangshizhonglei;
    private String tihuoliang;
    private String paiwuliang;
    private String dianzichengliang;
    private String kongzhizongliang;

    public DBHelper(Context context, String name, SQLiteDatabase.CursorFactory cursorFactory, int version) {
        super(context, name, cursorFactory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String measure_sql = "CREATE TABLE " + FrontMeasureActivity.MEASURE_TABLE + " (_id Integer PRIMARY KEY AUTOINCREMENT NOT NULL,ship_name VARCHAR,check_id VARCHAR," +
                "check_time DATE,ceshishuichi_frontLeft VARCHAR,ceshishuichi_frontRight VARCHAR,ceshishuichi_midLeft VARCHAR,ceshishuichi_midRight VARCHAR," +
                "ceshishuichi_backLeft VARCHAR,ceshishuichi_backRight VARCHAR,biaojijuli_front VARCHAR,biaojijuli_mid VARCHAR,biaojijuli_back VARCHAR," +
                "ship_length VARCHAR,near_shuichi VARCHAR,near_weight VARCHAR,tpc VARCHAR,LCF VARCHAR,DZ VARCHAR,M1 VARCHAR,M2 VARCHAR,zy VARCHAR,qy VARCHAR," +
                "rhy VARCHAR,ds VARCHAR,ycs VARCHAR,bzmd VARCHAR,scmd VARCHAR,qz VARCHAR,cs VARCHAR,check_status BOOLEAN,check_type Integer,jinkouguobie VARCHAR,shouhuoqiye VARCHAR,kuangming VARCHAR," +
                "kuangshizhonglei VARCHAR,tihuoliang VARCHAR,paiwuliang VARCHAR,dianzichengliang VARCHAR,kongzhizongliang VARCHAR,jiandingmoshi Boolean,shifouduanzhong Boolean,jizhongliang VARCHAR," +
                "qiancepaishiuliang VARCHAR,qiancejiandinghuoliang VARCHAR,qiancechuanyongwuliao VARCHAR,shijipaishuiliang VARCAHR,jianchuanyongwuliao VARCHAR)";
        String user_sql = "CREATE TABLE user (_id Integer PRIMARY KEY AUTOINCREMENT NOT NULL,nickname VARCHAR,username VARCHAR,authority VARCHAR,password VARCHAR,createtime DATE)";
        String company_sql = "CREATE TABLE company (_id Integer PRIMARY KEY AUTOINCREMENT NOT NULL,companyname VARCHAR,createtime DATE)";
        String material_sql = "CREATE TABLE material (_id Integer PRIMARY KEY AUTOINCREMENT NOT NULL,materialname VARCHAR,materialtype VARCHAR,createtime DATE)";
        db.execSQL(measure_sql);
        db.execSQL(user_sql);
        db.execSQL(company_sql);
        db.execSQL(material_sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public Map queryData(int id, int checkType) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from weightcalc where _id=? and check_type = ?", new String[]{String.valueOf(id) + "", String.valueOf(checkType)});
        Map<String, String> mData = new HashMap<>();
        if (cursor != null) {
            cursor.moveToFirst();
            _id = cursor.getString(cursor.getColumnIndex("_id"));
            ship_name = cursor.getString(cursor.getColumnIndex("ship_name"));
            check_id = cursor.getString(cursor.getColumnIndex("check_id"));
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
            if (cursor.getString(cursor.getColumnIndex("check_status")).equals("1")) {
                zongqingliju = (Double.valueOf(M2) - Double.valueOf(M1)) / Double.valueOf(DZ);
                jiaozhi = (100 * chishuicha_after * Double.valueOf(tpc) * Double.valueOf(LCF) + 50 * chishuicha_after * chishuicha_after * zongqingliju) / Double.valueOf(ship_length);
            } else {
                zongqingliju = 0;
                jiaozhi = 0;
            }
            jianchuanyongwuliao = Double.valueOf(zy) + Double.valueOf(qy) + Double.valueOf(rhy) + Double.valueOf(ds) + Double.valueOf(ycs);
            alterpaishui = shijipaishuizaizhong + jiaozhi;
            weight_before = shijipaishuizaizhong + jiaozhi;
            weight_after = (shijipaishuizaizhong + jiaozhi) * Double.valueOf(scmd) / Double.valueOf(bzmd);
            shijipaishuiliang = weight_after;
            if (checkType == 1 || checkType == 4) {
                qz = cursor.getString(cursor.getColumnIndex("qz"));
                cs = cursor.getString(cursor.getColumnIndex("cs"));
                mData.put("jianchuanyongwuliao", new DecimalFormat("0.000").format(jianchuanyongwuliao));
                weight_package = (shijipaishuizaizhong + jiaozhi) * Double.valueOf(scmd) / Double.valueOf(bzmd) - jianchuanyongwuliao - Double.valueOf(qz) - Double.valueOf(cs);
            } else if (checkType == 3) {
                jinkouguobie = cursor.getString(cursor.getColumnIndex("jinkouguobie"));
                shouhuoqiye = cursor.getString(cursor.getColumnIndex("shouhuoqiye"));
                kuangming = cursor.getString(cursor.getColumnIndex("kuangming"));
                kuangshizhonglei = cursor.getString(cursor.getColumnIndex("kuangshizhonglei"));
                qiancejiandinghuoliang = cursor.getString(cursor.getColumnIndex("qiancejiandinghuoliang"));
                tihuoliang = cursor.getString(cursor.getColumnIndex("tihuoliang"));
                paiwuliang = cursor.getString(cursor.getColumnIndex("paiwuliang"));
                dianzichengliang = cursor.getString(cursor.getColumnIndex("dianzichengliang"));
                kongzhizongliang = cursor.getString(cursor.getColumnIndex("kongzhizongliang"));
                qiancepaishiuliang = cursor.getString(cursor.getColumnIndex("qiancepaishiuliang"));
                qiancechuanyongwuliao = cursor.getString(cursor.getColumnIndex("qiancechuanyongwuliao"));
                mData.put("jinkouguobie", jinkouguobie);
                mData.put("shouhuoqiye", shouhuoqiye);
                mData.put("kuangming", kuangming);
                mData.put("kuangshizhonglei", kuangshizhonglei);
                mData.put("tihuoliang", tihuoliang);
                mData.put("paiwuliang", paiwuliang);
                mData.put("dianzichengliang", dianzichengliang);
                mData.put("kongzhizongliang", kongzhizongliang);
                mData.put("jizhongliang", jizhongliang);
                mData.put("qiancepaishiuliang", qiancepaishiuliang);
                mData.put("qiancejiandinghuoliang", qiancejiandinghuoliang);
                mData.put("qiancechuanyongwuliao", qiancechuanyongwuliao);
                qingzaijidingliangbeiliao = shijipaishuiliang - jianchuanyongwuliao;
                jianqingzai_beiliao = qingzaijidingliangbeiliao;
                mData.put("qingzaijidingliangbeiliang", new DecimalFormat("0.000").format(qingzaijidingliangbeiliao));
                mData.put("shijipaishuiliang_back", new DecimalFormat("0.000").format(shijipaishuiliang));
                jianqingzai_beiliao = qingzaijidingliangbeiliao;
                qiancepsl = Double.valueOf(qiancepaishiuliang);
                qiancecywl = Double.valueOf(qiancechuanyongwuliao);
                mData.put("qingzaijidingliangbeiliao", new DecimalFormat("0.000").format(qingzaijidingliangbeiliao));
                mData.put("qiancepsl", new DecimalFormat("0.000").format(qiancepsl));
                mData.put("qiancecywl", new DecimalFormat("0.000").format(qiancecywl));
                mData.put("jianchuanyongwuliao_back", new DecimalFormat("0.000").format(jianchuanyongwuliao));
                weight_package = Double.valueOf(qiancepaishiuliang) - Double.valueOf(qiancechuanyongwuliao) - jianqingzai_beiliao;
            } else {
                qiancepaishiuliang = cursor.getString(cursor.getColumnIndex("qiancepaishiuliang"));
                qiancechuanyongwuliao = cursor.getString(cursor.getColumnIndex("qiancechuanyongwuliao"));
                qingzaijidingliangbeiliao = shijipaishuiliang - jianchuanyongwuliao;
                jianqingzai_beiliao = qingzaijidingliangbeiliao;
                weight_package = Double.valueOf(qiancepaishiuliang) - Double.valueOf(qiancechuanyongwuliao) - jianqingzai_beiliao;
                mData.put("qingzaijidingliangbeiliao", new DecimalFormat("0.000").format(qingzaijidingliangbeiliao));
                mData.put("jianchuanyongwuliao", new DecimalFormat("0.000").format(jianchuanyongwuliao));
                mData.put("qiancepaishiuliang", new DecimalFormat("0.000").format(Double.valueOf(qiancepaishiuliang)));
                mData.put("qiancechuanyongwuliao", new DecimalFormat("0.000").format(Double.valueOf(qiancechuanyongwuliao)));
            }
            mData.put("_id", _id);
            mData.put("ship_name", ship_name);
            mData.put("check_id", check_id);
            mData.put("check_time", check_time);
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
            mData.put("qz", qz);
            mData.put("cs", cs);
            mData.put("check_status", cursor.getString(cursor.getColumnIndex("check_status")));
            mData.put("check_type", cursor.getString(cursor.getColumnIndex("check_type")));
            mData.put("average_front", new DecimalFormat("0.000").format(average_front));
            mData.put("average_mid", new DecimalFormat("0.000").format(average_mid));
            mData.put("average_back", new DecimalFormat("0.000").format(average_back));
            mData.put("alter_front", new DecimalFormat("0.000").format(alter_front));
            mData.put("alter_mid", new DecimalFormat("0.000").format(alter_mid));
            mData.put("alter_back", new DecimalFormat("0.000").format(alter_back));
            mData.put("afteralter_front", new DecimalFormat("0.000").format(afteralter_front));
            mData.put("afteralter_mid", new DecimalFormat("0.000").format(afteralter_mid));
            mData.put("afteralter_back", new DecimalFormat("0.000").format(afteralter_back));
            mData.put("chishuicha_before", new DecimalFormat("0.000").format(chishuicha_before));
            mData.put("chishuicha_after", new DecimalFormat("0.000").format(chishuicha_after));
            mData.put("chaeshuichi", new DecimalFormat("0.0").format(chaeshuichi));
            mData.put("chaezhongliang", new DecimalFormat("0.0").format(chaezhongliang));
            mData.put("shijishuichi", new DecimalFormat("0.000").format(shijishuichi));
            mData.put("shijipaishuizaizhong", new DecimalFormat("0.0").format(shijipaishuizaizhong));
            mData.put("zongqingliju", new DecimalFormat("0.000").format(zongqingliju));
            mData.put("jiaozhi", new DecimalFormat("0.000").format(jiaozhi));
            mData.put("alterpaishui", new DecimalFormat("0.000").format(alterpaishui));
            mData.put("weight_before", new DecimalFormat("0.0").format(weight_before));
            mData.put("weight_after", new DecimalFormat("0.000").format(weight_after));
            mData.put("jiaozhenghou_average", new DecimalFormat("0.000").format(jiaozhenghou_average));
            mData.put("weight_package", new DecimalFormat("0").format(weight_package));
            mData.put("check_status", cursor.getString(cursor.getColumnIndex("check_status")));
            mData.put("check_type", cursor.getString(cursor.getColumnIndex("check_type")));
            db.close();
            return mData;
        } else {
            db.close();
            return null;
        }
    }
}
